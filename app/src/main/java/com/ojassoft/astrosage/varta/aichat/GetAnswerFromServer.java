package com.ojassoft.astrosage.varta.aichat;

import static com.ojassoft.astrosage.utils.CUtils.getCoordinateValue;
import static com.ojassoft.astrosage.utils.CUtils.prepareUserProfile;
import static com.ojassoft.astrosage.utils.CUtils.urlEncodeData;


import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dao.ChatHistoryDAO;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.ui.activity.AIChatMessageEngine;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CreateCustomLocalNotification;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.firebase.database.ServerValue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles communication with the AI chat server to get answers for user queries.
 * 
 * This class manages the complete lifecycle of AI chat interactions including:
 * - Preparing user profile data for astrological calculations
 * - Making HTTP requests to the AI server
 * - Processing streaming responses from the server
 * - Handling question limits and subscription checks
 * - Converting markdown responses to HTML
 * - Managing chat history and message storage
 * - Error handling and logging
 * 
 * The class supports both streaming and non-streaming response modes,
 * and handles various response formats including JSON, markdown, and plain text.
 */
public class GetAnswerFromServer {

    /**
     * Appends one compact server-side AI trace line for the latest request session.
     *
     * @param stage short stage identifier for the current server event
     * @param details stage-specific state details
     */
    private void appendTrace(String stage, String details) {
        AIChatTraceLogger.append(activity, stage, details);
    }

	/**
	 * Returns true only when the chat screen is currently visible to the user.
	 *
	 * Note: We treat "home button / background / other screen on top" as NOT alive for UI,
	 * even if the Activity object still exists, because in that state we should show a
	 * response-ready notification instead of updating the UI.
	 */
	private boolean isAiChatWindowActivityAlive() {
		if (aiChatWindowActivity == null) {
			return false;
		}
        if (!AIChatWindowActivity.isAiChatWindowInForeground) {
            return false;
        }
        if (aiChatWindowActivity.isFinishing()) {
            return false;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
                && aiChatWindowActivity.isDestroyed()) {
            return false;
        }
		return true;
	}

    /**
     * Builds a short, single-line preview for a notification body from a (potentially long) answer string.
     */
    private static String buildAnswerPreview(String answer) {
        if (answer == null) {
            return "";
        }
        String preview = answer
                .replace("<br><br>", "\n\n")
                .replace("<br>", "\n")
                .replace("\\n", "\n")
                .replace("\n", " ")
                .trim();
        // Keep notifications compact and avoid overly long bodies.
        final int maxLen = 140;
        if (preview.length() > maxLen) {
            preview = preview.substring(0, maxLen).trim() + "...";
        }
        return preview;
    }

    /**
     * Builds expanded notification text from the full answer. Kept within a safe length for notifications.
     */
    private static String buildAnswerExpandedText(String answer) {
        if (answer == null) {
            return "";
        }
        String expanded = answer
                .replace("<br><br>", "\n\n")
                .replace("<br>", "\n")
                .replace("\\n", "\n")
                .trim();
        // Keep within a reasonable size to avoid OEM/system truncation.
        final int maxLen = 140;
        if (expanded.length() > maxLen) {
            expanded = expanded.substring(0, maxLen).trim() + "...";
        }
        return expanded;
    }

    /**
     * Shows the "response ready" notification only when the chat screen is not currently visible.
     *
     * This is called only for final answers (not streaming chunks) to avoid spamming notifications.
     */
    private void notifyResponseReadyIfNeeded(String finalHtml) {
        if (TextUtils.isEmpty(finalHtml)) {
            return;
        }
        if (isAiChatWindowActivityAlive()) {
            return;
        }
        new CreateCustomLocalNotification(activity.getApplicationContext())
                .showAiResponseReadyNotification(
                        activity.getString(R.string.app_name),
                        buildAnswerPreview(finalHtml),
                        buildAnswerExpandedText(finalHtml)
                );
    }

    /**
     * Persists the message to Firebase through the activity when possible and falls back otherwise.
     *
     * This prevents losing the final answer when the user backgrounds/leaves chat and later taps Join Chat.
     */
    private void sendMsgToFirebaseSafely(String message, long chatId, String from, String to, String msgType) {
        try {
            if (aiChatWindowActivity != null && !TextUtils.isEmpty(AIChatWindowActivity.CHANNEL_ID)) {
                aiChatWindowActivity.sendMsgToFirebase(message, chatId, from, to, msgType);
                return;
            }
        } catch (Exception e) {
            // Ignore and use the fallback below.
        }
        sendMsgToFirebaseFallback(message, chatId, from, to, msgType);
    }

    /**
     * Writes the message directly to Firebase without depending on {@link AIChatWindowActivity}.
     */
    private void sendMsgToFirebaseFallback(String message, long chatId, String from, String to, String msgType) {
        try {
            String resolvedChannelId = TextUtils.isEmpty(AIChatWindowActivity.CHANNEL_ID) ? chatChannelId : AIChatWindowActivity.CHANNEL_ID;
            if (TextUtils.isEmpty(resolvedChannelId)) {
                return;
            }
            Map<String, Object> chatMap = AIChatMessageEngine.buildFirebasePayload(message, chatId, from, to, msgType);
            AstrosageKundliApplication.getmFirebaseDatabase(resolvedChannelId)
                    .child(CGlobalVariables.MESSAGES_FBD_KEY)
                    .push()
                    .setValue(chatMap);
            AstrosageKundliApplication.getmFirebaseDatabase(resolvedChannelId)
                    .child(CGlobalVariables.LAST_MSG_TIME_FBD_KEY)
                    .setValue(ServerValue.TIMESTAMP);
        } catch (Exception e) {
            // Ignore persistence failures to avoid breaking UI flows.
        }
    }


    // Notification rendering is managed by CreateCustomLocalNotification to keep behavior consistent across the app.

    /** The current activity context */
    private final Activity activity;
    
    /** Reference to the AI chat window activity for UI updates */
    private AIChatWindowActivity aiChatWindowActivity;

    /** Stores the current chat channel id so final answers can still be saved after the UI goes to background. */
    private final String chatChannelId;

    /** Stores the current astrologer id so DB history writes do not depend on a live activity instance. */
    private final String chatAstrologerId;
    
    /** Accumulated response from the server */
    String response;
    
    /** Counter for response chunks */
    int count = 0;
    
    /** Final processed response string */
    String updatedString = "";
    
    /** Flag indicating if question limit is reached for users*/
    public String isQuestionLimitReached="0";
    
    /** Number of questions remaining for AI Pro users */
    public String questionNoLimit="0";

    /** Constant for latitude coordinate type */
    private final int KEY_LATITUDE = 1;
    
    /** Constant for longitude coordinate type */
    private final int KEY_LONGITUDE = 2;

    /** ID of the AI astrologer profile being used */
    private String aiAstrologerId;
    
    /** Unique user identifier */
    final String userId;
    
    /**
     * Constructor for GetAnswerFromServer.
     * 
     * @param activity The activity context for UI operations and preferences
     * @param aiAstrologerId The ID of the AI astrologer profile to use for responses
     */
    public GetAnswerFromServer(Activity activity, String aiAstrologerId) {
        this.activity = activity;
        this.aiAstrologerId = aiAstrologerId;
        if (activity instanceof AIChatWindowActivity) {
            this.aiChatWindowActivity = ((AIChatWindowActivity) activity);
        }
        this.chatChannelId = AIChatWindowActivity.CHANNEL_ID;
        this.chatAstrologerId = aiChatWindowActivity != null && aiChatWindowActivity.astrologerId != null
                ? aiChatWindowActivity.astrologerId
                : "";
        userId = CUtils.getCountryCode(aiChatWindowActivity)+(CUtils.getUserID(aiChatWindowActivity));
    }


    /**
     * Sets default values for location parameters if they are empty.
     * 
     * @param value The parameter value to check
     * @param which The type of parameter ("place", "timezone", or other)
     * @return The parameter value with default fallback if empty
     */
    private String setExtraLocationParams(String value, String which) {
        if (which.equals("place")) {
            value = (TextUtils.isEmpty(value) ? CGlobalVariables.defaultPlace : value);
        } else if (which.equals("timezone")) {
            value = (TextUtils.isEmpty(value) ? CUtils.getCurrentTimeZone() : value);
        } else {
            value = (TextUtils.isEmpty(value) ? "0" : value);
        }
        return value;
    }

    /**
     * Sends a query to the AI server using HttpURLConnection for streaming responses.
     *
     * This method handles the complete flow of getting AI responses:
     * - Prepares user profile and request parameters
     * - Makes HTTP GET request to the AI server
     * - Processes streaming response chunks
     * - Handles JSON parsing and response formatting
     * - Manages question limits and error scenarios
     * - Updates UI with responses in real-time
     *
     * The method uses a background executor to avoid blocking the UI thread
     * and supports both complete JSON responses and streaming chunked responses.
     *
     * @param activity The activity context
     * @param question The user's question to send to the AI server
     */
    public void getQueryAnswer(Activity activity, String question) {
        StringBuilder postDataParams = new StringBuilder();
        String userOffer = CUtils.getCallChatOfferType(activity);
        if(aiChatWindowActivity != null && aiChatWindowActivity.isAiProPlan){
            String aiPassPlanId = aiChatWindowActivity.aiPassPlanId;
            if(!TextUtils.isEmpty(aiPassPlanId)){
                userOffer = CUtils.getAIPassCouponByPlanServiceId(aiPassPlanId);
            }
        }
        // Prepare user profile data and request parameters
        try {
            UserProfileData userProfileData = prepareUserProfile(activity);
            postDataParams.append("&isnotbd=").append(urlEncodeData(userProfileData.isBirthDetailsAvailable()? "0" : "1"));
            postDataParams.append("&name=").append(urlEncodeData(userProfileData.getName()));
            postDataParams.append("&sex=").append(urlEncodeData(userProfileData.getGender()));
            postDataParams.append("&day=").append(urlEncodeData(userProfileData.getDay()));
            postDataParams.append("&month=").append(urlEncodeData(userProfileData.getMonth()));
            postDataParams.append("&year=").append(urlEncodeData(userProfileData.getYear()));
            postDataParams.append("&hrs=").append(urlEncodeData(userProfileData.getHour()));
            postDataParams.append("&min=").append(urlEncodeData(userProfileData.getMinute()));
            postDataParams.append("&sec=").append(urlEncodeData(userProfileData.getSecond()));
            postDataParams.append("&dst=" + "0");

            // Add extra location parameters
            postDataParams.append("&place=").append(urlEncodeData(setExtraLocationParams(userProfileData.getPlace(), "place")));
            postDataParams.append("&longdeg=").append(urlEncodeData(getCoordinateValue(userProfileData.getLongdeg(), KEY_LONGITUDE)));
            postDataParams.append("&longmin=").append(urlEncodeData(setExtraLocationParams(userProfileData.getLongmin(), "longmin")));
            postDataParams.append("&longew=").append(urlEncodeData(setExtraLocationParams(userProfileData.getLongew(), "longew")));
            postDataParams.append("&latdeg=").append(urlEncodeData(getCoordinateValue(userProfileData.getLatdeg(), KEY_LATITUDE)));
            postDataParams.append("&latmin=").append(urlEncodeData(setExtraLocationParams(userProfileData.getLatmin(), "latmin")));
            postDataParams.append("&latns=").append(urlEncodeData(setExtraLocationParams(userProfileData.getLatns(), "latns")));
            postDataParams.append("&timezone=").append(urlEncodeData(setExtraLocationParams(userProfileData.getTimezone(), "timezone")));

            postDataParams.append("&ms=").append(urlEncodeData(userProfileData.getMaritalStatus()));
            postDataParams.append("&oc=").append(urlEncodeData(userProfileData.getOccupation()));

            postDataParams.append("&ayanamsa=" + "0");
            postDataParams.append("&charting=" + "0");
            postDataParams.append("&kphn=" + "0");
            postDataParams.append("&pkgname=" + BuildConfig.APPLICATION_ID);
            postDataParams.append("&key=").append(CUtils.getApplicationSignatureHashCode(aiChatWindowActivity));

            // Add app version and method name
            postDataParams.append("&appversion=" + BuildConfig.VERSION_NAME);
            postDataParams.append("&methodname=" + "getanswer");

            // Add user ID and application signature
            postDataParams.append("&userid=").append(userId);
            postDataParams.append("&androidid=").append(CUtils.getMyAndroidId(activity));
            postDataParams.append("&aiprofile=").append(aiAstrologerId);
            postDataParams.append("&cid=").append(AIChatWindowActivity.CHANNEL_ID);
            postDataParams.append("&isnewsession=").append(aiChatWindowActivity.isNewSession);
            postDataParams.append("isns").append(aiChatWindowActivity.isNotNeedSteam);

            // Add previous question and answer if available
            if (!TextUtils.isEmpty(aiChatWindowActivity.previousQues)) {
                postDataParams.append("&islocallyanswered=").append(aiChatWindowActivity.isLocallyAnswered);
                postDataParams.append("&previousques=").append(urlEncodeData(aiChatWindowActivity.previousQues));
                postDataParams.append("&previousans=").append(urlEncodeData(aiChatWindowActivity.previousAns));
                aiChatWindowActivity.previousQues = "";
                aiChatWindowActivity.previousAns = "";
                aiChatWindowActivity.isLocallyAnswered = "";
            }

            // Add the question itself
            postDataParams.append("&question=").append(urlEncodeData(question.trim()));

            // Add language code
            int languageCode = com.ojassoft.astrosage.utils.CUtils.getLanguageCodeFromString(AIChatWindowActivity.langCode);
            postDataParams.append("&languagecode=").append(languageCode);

            // Add isRoman and reguserid parameters
            postDataParams.append("&isroman=").append(AIChatWindowActivity.isRoman); // isRoman
            postDataParams.append("&reguserid=").append(urlEncodeData(CUtils.getUserIdForBlock(aiChatWindowActivity)));
            postDataParams.append("&couponcode=").append(userOffer);
            postDataParams.append("&sfc=").append(CUtils.isSecondFreeChat(activity) ? "1" : "0");

        } catch (Exception e) {
            // Log parameter preparation errors
        }

        // Execute the request on a background thread
        ExecutorService executor = Executors.newSingleThreadExecutor();

        response = "";
        updatedString = "";
        count = 0;
        AIChatTraceLogger.startRequestTrace(activity, question, AIChatWindowActivity.CHANNEL_ID);
        appendTrace("REQUEST_META", "userId=" + userId
                + ", questionLen=" + (question == null ? 0 : question.length())
                + ", userOffer=" + userOffer
                + ", lang=" + AIChatWindowActivity.langCode
                + ", isRoman=" + AIChatWindowActivity.isRoman
                + ", traceFile=" + AIChatTraceLogger.getTraceFilePath(activity));

        executor.execute(() -> {
            long answerId = -1;
            try {
                // Make the HTTP GET request
                HttpURLConnection connection = getHttpURLConnection(postDataParams);

                // Get the response code and handle errors
                int responseCode = connection.getResponseCode();
                appendTrace("HTTP_STATUS", "code=" + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response line by line and handle each chunk
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    boolean typeWriterEffect = true;
                    while ((line = br.readLine()) != null) {
                        count += 1;
                        int finalCount = count;
                        if (!TextUtils.isEmpty(line)) {
                            response += line;
                            appendTrace("SERVER_LINE", "chunk=" + finalCount
                                    + ", lineLen=" + line.length()
                                    + ", responseLen=" + response.length()
                                    + ", answerIdBefore=" + answerId
                                    + ", preview=" + AIChatTraceLogger.preview(line));
                            AIChatStreamingResponseParser.StreamStep streamStep = AIChatStreamingResponseParser.parseAccumulatedResponse(
                                    response,
                                    finalCount,
                                    answerId > 0 ? answerId : System.currentTimeMillis()
                            );
                            appendTrace("SERVER_PARSE", "chunk=" + finalCount
                                    + ", mode=" + streamStep.mode()
                                    + ", parsedAnswerId=" + streamStep.answerId()
                                    + ", resultLen=" + (streamStep.resultText() == null ? 0 : streamStep.resultText().length())
                                    + ", responseLen=" + response.length());
                            if (finalCount == 1 && streamStep.mode() == AIChatStreamingResponseParser.Mode.COMPLETE_JSON) {
                                appendTrace("SERVER_COMPLETE_JSON_DIRECT", "chunk=" + finalCount
                                        + ", answerId=" + streamStep.answerId()
                                        + ", responseLen=" + response.length());
                                parseJsonAtOnce(response, false, question);
                            } else if (streamStep.mode() == AIChatStreamingResponseParser.Mode.ANSWER_ID_ONLY) {
                                updatedString = AIChatStreamingResponseParser.ANSWER_ID_PLACEHOLDER;
                                answerId = streamStep.answerId();
                                appendTrace("SERVER_ANSWER_ID_ONLY", "chunk=" + finalCount + ", answerId=" + answerId);
                            } else if (streamStep.mode() == AIChatStreamingResponseParser.Mode.COMPLETE_JSON
                                    || streamStep.mode() == AIChatStreamingResponseParser.Mode.STREAM_TEXT) {
                                try {
                                    if (streamStep.mode() == AIChatStreamingResponseParser.Mode.COMPLETE_JSON && streamStep.jsonObject() != null) {
                                        AstrosageKundliApplication.brokenMessageLogs += "\n" + "AI Chat newMessage = \n" + response;
                                        checkIsQuestionLimit(streamStep.jsonObject());
                                        answerId = streamStep.answerId();
                                        appendTrace("SERVER_COMPLETE_JSON", "chunk=" + finalCount
                                                + ", answerId=" + answerId
                                                + ", jsonKeys=" + streamStep.jsonObject().length());
                                    }

                                    updatedString = streamStep.resultText();
                                    if (!TextUtils.isEmpty(updatedString)) {
                                        appendTrace("SERVER_TO_UI", "chunk=" + finalCount
                                                + ", answerId=" + answerId
                                                + ", typeWriter=" + typeWriterEffect
                                                + ", bodyLen=" + updatedString.length()
                                                + ", preview=" + AIChatTraceLogger.preview(updatedString));
                                        aiChatWindowActivity.setResponse(convertMarkdownToHTML(updatedString), typeWriterEffect, answerId);
                                        typeWriterEffect = false;
                                    }
                                } catch (Exception e) {
                                    appendTrace("SERVER_PARSE_EXCEPTION", "chunk=" + finalCount + ", error=" + e);
                                    dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "After chunck response parsing exception = " + e);
                                    aiChatWindowActivity.responseErrorHandling(false);
                                }
                            }
                        }
                    }
                    // Handle the final response and notify the user when the chat UI is not visible.
                    if (AIChatStreamingResponseParser.shouldPersistFinalMessage(updatedString)) {
                        String finalHtml = convertMarkdownToHTML(updatedString).replace("\n", "<br>");
                        appendTrace("SERVER_FINAL_PERSIST", "answerId=" + answerId
                                + ", visible=" + isAiChatWindowActivityAlive()
                                + ", finalLen=" + finalHtml.length()
                                + ", preview=" + AIChatTraceLogger.preview(finalHtml));
                        notifyResponseReadyIfNeeded(finalHtml);
                        sendMsgToFirebaseSafely(finalHtml, answerId, CGlobalVariables.ASTROLOGER, CGlobalVariables.USER, "");
                        addMessageToList(finalHtml, CGlobalVariables.ASTROLOGER, answerId, false);
                    }/* else {
                        if (count == 0) {
                            dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "Response is empty and count is 0");
                        } else if (count == 1 && updatedString.equalsIgnoreCase("ParseAnswerId")) {
                            dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "Count is 1 and response=" + response);
                        }
                    }*/
                } else {
                    appendTrace("HTTP_ERROR", "code=" + responseCode + ", message=non-200");
                    aiChatWindowActivity.responseErrorHandling(false);
                   // dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "API hit failed responseCode = " + responseCode);
                }

            } catch (IOException e) {
                appendTrace("HTTP_IO_EXCEPTION", String.valueOf(e));
                aiChatWindowActivity.responseErrorHandling(false);
                dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "API hit failed IOException = " + e);
            } catch (Exception e) {
                appendTrace("HTTP_EXCEPTION", String.valueOf(e));

                if (AIChatStreamingResponseParser.shouldPersistFinalMessage(updatedString)) {
                    String finalHtml = convertMarkdownToHTML(updatedString).replace("\n", "<br>");
                    notifyResponseReadyIfNeeded(finalHtml);
                    sendMsgToFirebaseSafely(finalHtml, answerId, CGlobalVariables.ASTROLOGER, CGlobalVariables.USER, "");
                    addMessageToList(finalHtml, CGlobalVariables.ASTROLOGER, answerId, false);
                }
                dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "API hit failed Exception = " + e);
                aiChatWindowActivity.responseErrorHandling(false);
            }
        });
    }
    /**
     * Checks if the user has reached their question limit for AI Pro plan.
     *
     * This method examines the response JSON to determine if the user has exceeded
     * their allowed question limit. If the limit is reached, it:
     * - Sets the limit reached flag
     * - Shows appropriate error message to user
     * - Triggers chat termination
     *
     * @param jsonObject The response JSON object from the server
     */
    private void checkIsQuestionLimit(JSONObject jsonObject) {

        try {
            aiChatWindowActivity.isAiProPlanLimitReached = false;
            // Check if the plan is AI Pro
            if (aiChatWindowActivity.isAiProPlan) {
                // Check if the question limit has been exceeded
                if (jsonObject.has("QUESLIMIT")) {
                    String questionLimitReached = jsonObject.getString("QUESLIMIT");

                    // If the limit has been exceeded, set the flag, show an error message and terminate the chat
                    if (questionLimitReached.equals(CGlobalVariables.QUESTION_LIMIT_EXCEEDED)) {
                        isQuestionLimitReached = "1";
                        questionNoLimit = jsonObject.getString("QUESNUMBER");
                        aiChatWindowActivity.isAiProPlanLimitReached = true;
                        CUtils.showSnackbar(aiChatWindowActivity.findViewById(android.R.id.content), activity.getResources().getString(R.string.error_ai_chat_limit_reached), activity);
                        aiChatWindowActivity.runOnUiThread(() -> {
                            aiChatWindowActivity.sendCustomStatusMessage(CGlobalVariables.AI_PASS_QUESTION_LIMIT_REACHED);
                            aiChatWindowActivity.finishChat(CGlobalVariables.AI_PASS_QUESTION_LIMIT_REACHED);
                        });
                    }
                }
            }
        } catch (Exception e) {
            // Ignore exceptions in question limit checking
        }
    }

    /**
     * Adds a message to the chat history and local storage.
     *
     * This method creates a Message object and adds it to:
     * - Global chat messages history
     * - Current session chat history (if service is running)
     * - Local database storage via ChatHistoryDAO
     *
     * @param messageText The text content of the message
     * @param messageFrom The sender of the message (ASTROLOGER or USER)
     * @param chatId Unique identifier for the chat message
     * @param animate Whether to animate the message display
     */
    public void addMessageToList(String messageText, String messageFrom, long chatId, boolean animate) {
        // Create a new Message object
        Message message = new Message();

        // Set message attributes
        message.setAuthor(messageFrom);
        message.setDateCreated(AIChatWindowActivity.getMessageDate());
        message.setTimeStamp(System.currentTimeMillis());
        message.setMessageBody(messageText);
        message.setSeen(animate);
        message.setChatId(chatId);
        message.setDelayed(false);

        // Add message to global chat history
        if (AstrosageKundliApplication.allChatMessagesHistory == null) {
            AstrosageKundliApplication.allChatMessagesHistory = new ArrayList<>();
        }
        AstrosageKundliApplication.allChatMessagesHistory.add(new UserMessage(message));

        // Add message to current session chat history if service is running
        if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
            if (AstrosageKundliApplication.chatMessagesHistory != null) {
                AstrosageKundliApplication.chatMessagesHistory.add(new UserMessage(message));
            }
        }

        // Add message to local database storage
        String resolvedChannelId = TextUtils.isEmpty(AIChatWindowActivity.CHANNEL_ID) ? chatChannelId : AIChatWindowActivity.CHANNEL_ID;
        String resolvedAstrologerId = !TextUtils.isEmpty(chatAstrologerId)
                ? chatAstrologerId
                : (aiChatWindowActivity != null ? aiChatWindowActivity.astrologerId : "");
        ChatHistoryDAO.getInstance(activity).addMessage(new UserMessage(message), resolvedChannelId, resolvedAstrologerId);
    }

    /**
     * Creates and configures an HttpURLConnection for making requests to the AI server.
     * 
     * @param postDataParams The query parameters to append to the URL
     * @return Configured HttpURLConnection object
     * @throws IOException If connection creation fails
     */
    private static @NonNull HttpURLConnection getHttpURLConnection(StringBuilder postDataParams) throws IOException {
        URL url = new URL(com.ojassoft.astrosage.utils.CGlobalVariables.AI_BASE_URL + "/pred?" + postDataParams);
        //Log.d("languageCodeIssue", "url: " + url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(60000);
        connection.setUseCaches(false);
        connection.setAllowUserInteraction(false);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        connection.setDoInput(true);
        return connection;
    }

    /**
     * Parses a complete JSON response from the AI server.
     *
     * This method handles complete (non-streaming) responses and:
     * - Extracts the answer text and metadata
     * - Checks for question limits and subscription status
     * - Processes buttons and interactive elements
     * - Handles special response types (videos, intents)
     * - Updates the UI with the response
     * - Manages chat history and analytics
     *
     * @param responseData The complete JSON response string
     * @param isStreamedResponse Whether this is a streaming response
     * @param question The original question that generated this response
     */
    public void parseJsonAtOnce(String responseData, Boolean isStreamedResponse, String question) {
        //Log.d("testAiChatPass", "parseJsonAtOnce responseData->" + responseData);
        try {
            if (!TextUtils.isEmpty(responseData)) {
                //BaseActivity.isNewSession = "0";
                JSONObject response = new JSONObject(responseData);
                String qType = response.getString("QUESTYPE");
                StringBuilder result = new StringBuilder(convertMarkdownToHTML(response.getString("RESULT")));
                String link = response.getString("ANSWERLINK");
                JSONArray jsonArray = new JSONArray();
                if (response.has("QUESLIMIT")) {
                    String questionLimitReached = response.getString("QUESLIMIT");
                    //CUtils.saveUserQuestionLimit(aiChatWindowActivity, questionLimitReached);
                    //String questionLimitReached = "1";
                    if (questionLimitReached.equals(CGlobalVariables.QUESTION_LIMIT_EXCEEDED) && result.length() == 0) {
                        //Log.d("languageCodeIssue", "questionLimitReached->");
                        CUtils.showSnackbar(aiChatWindowActivity.findViewById(android.R.id.content),activity.getString(R.string.there_is_no_chat_ongoing), aiChatWindowActivity);
                        aiChatWindowActivity.responseErrorHandling(false);
                        aiChatWindowActivity.runOnUiThread(()->{
                            aiChatWindowActivity.sendCustomStatusMessage(CGlobalVariables.USER_INACTIVITY);
                            aiChatWindowActivity.finishChat(CGlobalVariables.USER_INACTIVITY);
                        });
                        return;
                    }
                }
                /*if (response.has("QUESREMAIN")) {
                    CUtils.saveQuestionRemaining(aiChatWindowActivity, response.getString("QUESREMAIN"));
                }*/
                try {
                    if (response.has("buttons")) {
                        jsonArray = response.getJSONArray("buttons");
                    }
                } catch (Exception e) {
                    //Log.d("languageCodeIssue", "Exception 11->" + e);
                    aiChatWindowActivity.responseErrorHandling(false);
                    dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "parseJsonAtOnce response has buttons Exception="+e);
                }

                String str_answerId = (response.has("ANSWERID")) ? response.getString("ANSWERID") : "";
                long answerId = -1;
                try{
                    answerId = Long.parseLong(str_answerId);
                }catch(Exception e){
                    answerId = System.currentTimeMillis();
                }

                String persistedResult = convertMarkdownToHTML(result.toString()).replace("\n","<br>");
                if (AIChatStreamingResponseParser.shouldPersistFinalMessage(persistedResult)) {
                    notifyResponseReadyIfNeeded(persistedResult);
                }
                sendMsgToFirebaseSafely(persistedResult, answerId, CGlobalVariables.ASTROLOGER, CGlobalVariables.USER,"");
                addMessageToList(persistedResult, CGlobalVariables.ASTROLOGER,answerId,false);

                if (!isStreamedResponse) {

                    String videoId = CUtils.extractYTId(link);

                    qType = ""; // remove this line to enable varta intents
                    if (!TextUtils.isEmpty(CUtils.getAppIntentCode(qType))) {
                        aiChatWindowActivity.speakOut(result.toString(), link, CUtils.getAppIntentCode(qType));
                        return;
                    } else if (result.length() == 0) {
                        String questionType = response.getString("QUESTYPE");
                        aiChatWindowActivity.isLocallyAnswered = "1";
                        result = new StringBuilder(LocalAIAnswers.getInstance(aiChatWindowActivity).searchAnswer(question, questionType));
                        //Log.d("appHandIssue", "parseJsonAtOnce result: "+result);
                        aiChatWindowActivity.previousAns = URLEncoder.encode(String.valueOf(result), "UTF-8");
                        aiChatWindowActivity.responseErrorHandling(true);
                    } else if (videoId != null) {
                        result.insert(0, answerId + "___");
                        link = answerId + "___youtube" + link;
                        aiChatWindowActivity.speakOut(result.toString(), link, "");
                        return;
                    }

                }

                result = addButtons(result, jsonArray, link);
                StringBuilder finalResult = result;
                if (!TextUtils.isEmpty(finalResult)) {

                    long finalAnswerId = answerId;
                    aiChatWindowActivity.runOnUiThread(() -> {
                        aiChatWindowActivity.isNewSession = "0";
                        aiChatWindowActivity.setResponse(finalResult.toString(), true, finalAnswerId);
                    });
                } else {
                    dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "parseJsonAtOnce finalResult is empty");
                }
            } else {
                dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "parseJsonAtOnce response is empty");
            }
        } catch (Exception e) {
            //Log.d("languageCodeIssue", "Exception 2->" + e);
            aiChatWindowActivity.responseErrorHandling(false);
            dumpLogDataOnServer(userId, AIChatWindowActivity.CHANNEL_ID, question, "parseJsonAtOnce exception1 = "+e);
        }


    }

    /**
     * Converts markdown text to HTML format for display in the chat UI.
     * 
     * This method handles various markdown elements:
     * - Headers (H1-H6) converted to strong tags
     * - Bold text (**text** or __text__) converted to strong tags
     * - Italic text (*text* or _text_) converted to em tags
     * 
     * @param text The markdown text to convert
     * @return HTML formatted text
     */
    public  String convertMarkdownToHTML(String text) {
        return AIChatServerResponseFormatter.convertMarkdownToHtml(text);
    }
    
    /**
     * Adds interactive buttons and links to the response text.
     * 
     * This method appends button elements and external links to the response
     * in a special format that the UI can parse and display as interactive elements.
     * 
     * @param result The response text to append buttons to
     * @param jsonArray Array of button objects from the server response
     * @param link External link URL if provided
     * @return Response text with buttons and links appended
     */
    private StringBuilder addButtons(StringBuilder result, JSONArray jsonArray, String link) {
        try {
            return new StringBuilder(
                    AIChatServerResponseFormatter.appendButtons(
                            result.toString(),
                            AIChatServerResponseFormatter.extractButtons(jsonArray),
                            link,
                            activity.getResources().getString(R.string.open_site)
                    )
            );
        } catch (Exception e) {
            //Log.d("languageCodeIssue", "addButtons ex->" + e);
            aiChatWindowActivity.responseErrorHandling(false);
        }
        return result;
    }

    /**
     * Sends a query to the AI server using Retrofit for non-streaming responses.
     *
     * This method provides an alternative to getQueryAnswer() using Retrofit instead
     * of HttpURLConnection. It's used for simpler, non-streaming requests where
     * real-time response updates are not needed.
     *
     * @param activity The activity context
     * @param question The user's question to send to the AI server
     */
    public void getAnswerResponse(final Activity activity, String question){
        HashMap<String, String> postDataParams = new HashMap<String, String>();

        try {

            UserProfileData userProfileData = prepareUserProfile(activity);

            postDataParams.put("reguserid", CUtils.getUserIdForBlock(aiChatWindowActivity));
            postDataParams.put("name", userProfileData.getName());
            postDataParams.put("sex", userProfileData.getGender());
            postDataParams.put("day", userProfileData.getDay());
            postDataParams.put("month", userProfileData.getMonth());
            postDataParams.put("year", userProfileData.getYear());
            postDataParams.put("hrs", userProfileData.getHour());
            postDataParams.put("min", userProfileData.getMinute());
            postDataParams.put("sec", userProfileData.getSecond());
            postDataParams.put("dst", "0");
            postDataParams.put("place", userProfileData.getPlace());
            postDataParams.put("longdeg", userProfileData.getLongdeg());
            postDataParams.put("longmin", userProfileData.getLongmin());
            postDataParams.put("longew", userProfileData.getLongew());
            postDataParams.put("latdeg", userProfileData.getLatdeg());
            postDataParams.put("latmin", userProfileData.getLatmin());
            postDataParams.put("latns", userProfileData.getLatns());
            postDataParams.put("timezone", userProfileData.getTimezone());
            postDataParams.put("ayanamsa", "0");
            postDataParams.put("charting", "0");
            postDataParams.put("kphn", "0");
            postDataParams.put("appversion", BuildConfig.VERSION_NAME);
            postDataParams.put("pkgname", BuildConfig.APPLICATION_ID);
            postDataParams.put("methodname", "getanswer");
            postDataParams.put("key", CUtils.getApplicationSignatureHashCode(aiChatWindowActivity));
            postDataParams.put("userid", CUtils.getCountryCode(aiChatWindowActivity) + CUtils.getUserID(aiChatWindowActivity));
            postDataParams.put("androidid", CUtils.getMyAndroidId(activity));
            postDataParams.put("aiprofile", AstrosageKundliApplication.selectedAstrologerDetailBean.getAiAstrologerId());
            postDataParams.put("cid", AIChatWindowActivity.CHANNEL_ID);
            postDataParams.put("isnewsession", aiChatWindowActivity.isNewSession);

            if (aiChatWindowActivity.isNewSession.equals("0")) {
                postDataParams.put("islocallyanswered", aiChatWindowActivity.isLocallyAnswered);
                if (aiChatWindowActivity.isLocallyAnswered.equals("1")) {
                    aiChatWindowActivity.isLocallyAnswered = "0";
                    postDataParams.put("previousques", aiChatWindowActivity.previousQues);
                    postDataParams.put("previousans", aiChatWindowActivity.previousAns);
                }
            }
            postDataParams.put("isns",aiChatWindowActivity.isNotNeedSteam+"");
            postDataParams.put("question", question.trim());
            int languageCode = com.ojassoft.astrosage.utils.CUtils.getLanguageCodeFromString(AIChatWindowActivity.langCode);
            postDataParams.put("languagecode", languageCode+"");
            postDataParams.put("isroman", AIChatWindowActivity.isRoman + ""); // isRoman

            //Log.d("languageCodeIssue", "call headers: " + postDataParams);

        } catch (Exception e) {
            Log.d("languageCodeIssue", "params e: " + e);
        }

        Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getAnswerFromServer(postDataParams);

        // Make the API call
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Process the response
                    if (response.isSuccessful() && response.body() != null) {
                        String responseString = response.body().string();
                        parseJsonAtOnce(responseString, false, question);
                    }else{
                        // Handle the case where the response is empty or unsuccessful
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                // Handle the case where the API call fails
                aiChatWindowActivity.responseErrorHandling(false);
                throwable.printStackTrace();
            }
        });

    }

    /**
     * Sends error and debugging information to the server for analysis.
     * 
     * This method is used to log various error conditions, response issues,
     * and debugging information to help with troubleshooting AI chat problems.
     * 
     * @param userId The user identifier
     * @param channelId The chat channel identifier
     * @param question The original question that caused the issue
     * @param message The error or debug message to log
     */
    private void dumpLogDataOnServer(String userId, String channelId, String question, String message){
        CUtils.sendLogDataRequest(userId, channelId, question+"###"+message);
    }
}
