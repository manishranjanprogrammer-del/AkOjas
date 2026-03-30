package com.ojassoft.astrosage.varta.aichat.models;

import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CUtils.urlEncodeData;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AK_FREE_QUESTION_LIMIT_EXCEED_KEY;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles communication with the Kundli AI server to get astrological answers for user queries for kundli AI.
 * 
 * This class manages the complete lifecycle of Kundli AI chat interactions including:
 * - Preparing user horoscope data for astrological calculations
 * - Making HTTP requests to the Kundli AI server
 * - Processing streaming responses from the server
 * - Handling question limits and subscription checks
 * - Managing chat history and message storage
 * - Error handling and logging for Kundli-specific features
 * 
 * The class supports various modules including:
 * - Personal horoscope analysis
 * - Matching horoscope analysis
 * - Numerology calculations
 * - Varshphal (yearly horoscope)
 * - Rashi-based predictions
 * - Video horoscope integration
 */
public class GetAnswerFromServerForKundali {

    /** The current activity context */
    private final Activity activity;
    
    /** Reference to the mini chat window for UI updates */
    private MiniChatWindow miniChatWindow;
    
    /** Unique identifier for the AI response */
    public long answerId;
    
    /** Accumulated response from the server */
    StringBuilder response;
    
    /** Counter for response chunks */
    int count = 0;
    
    /** Final processed response string */
    String updatedString = "";
    
    /** Log data for sharing and debugging */
    String sharableLog = "";
    
    /** Website link from the response */
    String websiteLink = "";
    
    /** Flag indicating if answer ID is missing from response */
    boolean isAnswerIdMissing = false;
    
    /** Unique user identifier */
    final String userId;
    
    /** Constant for latitude coordinate type */
    private final int KEY_LATITUDE = 1;
    
    /** Constant for longitude coordinate type */
    private final int KEY_LONGITUDE = 2;

    /**
     * Constructor for GetAnswerFromServerForKundali.
     * 
     * @param activity The activity context for UI operations and preferences
     */
    public GetAnswerFromServerForKundali(Activity activity) {
        this.activity = activity;
        userId = CUtils.getCountryCode(activity)+(CUtils.getUserID(activity));
        if (activity instanceof MiniChatWindow) {
            this.miniChatWindow = ((MiniChatWindow) activity);
        }
    }

    /**
     * Processes coordinate values and returns integer part only.
     * 
     * This method handles latitude and longitude coordinates by:
     * - Using default values if the input is empty
     * - Extracting only the integer part of the coordinate
     * - Handling exceptions gracefully
     * 
     * @param value The coordinate value to process
     * @param which Type of coordinate (KEY_LATITUDE or KEY_LONGITUDE)
     * @return Processed coordinate value as string
     */
    private String getCoordinateValue(String value, int which) {
        if (TextUtils.isEmpty(value)) {
            if (which == KEY_LATITUDE) {
                value = com.ojassoft.astrosage.utils.CGlobalVariables.defaultLatitude;
            } else if (which == KEY_LONGITUDE) {
                value = com.ojassoft.astrosage.utils.CGlobalVariables.defaultLongitude;
            }
        }
        try {
            value = value.substring(0, value.indexOf("."));
        } catch (Exception e) {
            //
        }
        return value;
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
     * Sends a query to the Kundli AI server using HttpURLConnection for streaming responses.
     *
     * This method handles the complete flow of getting Kundli AI responses:
     * - Makes HTTP GET request to the Kundli AI server
     * - Processes streaming response chunks
     * - Handles JSON parsing and response formatting
     * - Manages question limits and error scenarios
     * - Updates UI with responses in real-time
     * - Saves questions and answers to database
     *
     * The method supports various Kundli features including:
     * - Personal horoscope analysis
     * - Matching horoscope analysis
     * - Numerology calculations
     * - Varshphal (yearly horoscope)
     * - Rashi-based predictions
     *
     * @param activity The activity context
     * @param question The user's question to send to the Kundli AI server
     */
    public void getQueryAnswer(Activity activity, String question) {
        StringBuilder postDataParams = getPostDataParams(activity, question);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        response = new StringBuilder();
        updatedString = "";
        count = 0;
        sharableLog = "\nQuestion==> " + question + "\nAnswer==>";
        isAnswerIdMissing = false;

        executor.execute(() -> {
            try {
                HttpURLConnection connection = getHttpURLConnection(postDataParams);

                int responseCode = connection.getResponseCode();

                miniChatWindow.chatDao.saveQuestionToDb(question,CUtils.getDbNowDateAndTime());

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    boolean typeWriterEffect = true;
//                    Log.d("languageCodeIssue", "br.readLine(): "+br.readLine());
                    while ((line = br.readLine()) != null) {
                        count += 1;
                        int finalCount = count;
                        if (!TextUtils.isEmpty(line)) {
                            response.append(line);
                            sharableLog += ", " + line;
                            CUtils.saveStringData(activity, "sharableLog" ,sharableLog);
                            //Log.d("TestCloudChat", "line: " + line);
                            if (finalCount == 1) {
                                if (response.charAt(0) == '{' && response.charAt(response.length() - 1) == '}') {
                                    //Log.d("TestCloudChat", "Response1: " + response);
                                    parseJsonAtOnce(response.toString(), false, question);
                                } else {
                                    //Log.d("TestCloudChat", "Response2: " + response);
                                    try {
                                        updatedString = "ParseAnswerId";
                                        replaceString(response, "null", "");
                                        String str_answerId = response.substring(response.indexOf("\"ANSWERID\":\"") + 12);
                                        answerId = Long.parseLong(str_answerId.substring(0, str_answerId.indexOf("\"")));
                                    }catch(Exception e){
                                        isAnswerIdMissing = true;
                                        answerId = System.currentTimeMillis();
                                    }
                                }
                            } else {
                                updatedString = "";
                                if (response.charAt(0) == '{' && response.charAt(response.length() - 1) == '}') {
                                    try {
                                        //Log.d("languageCodeIssue", "complete: " + response);
                                        AstrosageKundliApplication.brokenMessageLogs += "\n"+"Kundli AI newMessage = \n"+response;
                                        JSONObject jsonObject = new JSONObject(response.toString());
                                        updatedString = jsonObject.getString("RESULT");
                                        if(jsonObject.has("ANSWERLINK") && !jsonObject.getString("ANSWERLINK").isEmpty()){
                                            websiteLink = jsonObject.getString("ANSWERLINK");
                                        }
                                        if (!updatedString.isEmpty()) {
                                            miniChatWindow.setResponse(CUtils.convertMarkdownToHTML(updatedString), answerId,typeWriterEffect,true);
                                            typeWriterEffect = false;
                                        }
                                    } catch (Exception e) {
                                        miniChatWindow.responseErrorHandling(false);
                                        dumpLogDataOnServer(userId, "KUNDALI_AI_"+miniChatWindow.conversationId, question, "After chunck response parsing exception1 = "+e);
                                    }
                                } else {
                                    try {
                                        //Log.d("languageCodeIssue", "incomplete: " + response);
                                        updatedString = response.substring(response.indexOf("\"RESULT\":\"") + 10, response.length() - 1);
                                        if (!updatedString.isEmpty()) {
                                            miniChatWindow.setResponse(CUtils.convertMarkdownToHTML(updatedString),answerId,typeWriterEffect,false);
                                            typeWriterEffect = false;
                                        }
                                    } catch (Exception e1) {
                                        //Log.d("TestCloudChat", "e1: "+e1);
                                        miniChatWindow.responseErrorHandling(false);
                                        dumpLogDataOnServer(userId, "KUNDALI_AI_"+miniChatWindow.conversationId, question, "After chunck response parsing exception2 = "+e1);
                                    }
                                }

                            }
                        }
                    }

                    //Log.e("TestCloudChat", "updatedString = "+updatedString);

                    if (!updatedString.isEmpty() && !updatedString.equalsIgnoreCase("ParseAnswerId")) {
                        AstrosageKundliApplication.brokenMessageLogs += "\n"+"updatedString = \n"+updatedString;
                        if(!websiteLink.isEmpty()){
                            updatedString = addButtons(updatedString,new JSONArray(),websiteLink);
                        }
                        //miniChatWindow.updateMessageInItem(convertMarkdownToHTML(updatedString).replace("\n", "<br>"));
                        addMessageToHistoryList(CUtils.convertMarkdownToHTML(updatedString).replace("\n", "<br>"), CGlobalVariables.ASTROLOGER,answerId, false);
                        miniChatWindow.chatDao.saveAnswerToDb(String.valueOf(answerId),question,CUtils.convertMarkdownToHTML(updatedString).replace("\n", "<br>"));
                    }else {
                        if(count == 0){
                            dumpLogDataOnServer(userId, "KUNDALI_AI_"+miniChatWindow.conversationId,question, "Response is empty and count is 0");
                        } else if (count == 1 && updatedString.equalsIgnoreCase("ParseAnswerId")) {
                            dumpLogDataOnServer(userId, "KUNDALI_AI_"+miniChatWindow.conversationId, question, "Count is 1 and response="+response);
                        }
                    }
                } else {
                    miniChatWindow.responseErrorHandling(false);
                    dumpLogDataOnServer(userId, "KUNDALI_AI_"+miniChatWindow.conversationId,question, "API hit failed responseCode = "+responseCode);
                }

            } catch (IOException e) {
                Log.e("languageCodeIssue", "Exception 2->" + e);
                miniChatWindow.responseErrorHandling(false);
                AstrosageKundliApplication.brokenMessageLogs += "\n"+"exp 1 = \n"+e.getMessage();
                dumpLogDataOnServer(userId, "KUNDLI_AI_"+miniChatWindow.conversationId, question, "API hit failed IOException = "+e);
            } catch (Exception e) {
                Log.e("languageCodeIssue", "Exception 3->" + e);
                miniChatWindow.responseErrorHandling(false);
                AstrosageKundliApplication.brokenMessageLogs += "\n"+"exp 2 = \n"+e.getMessage();
                dumpLogDataOnServer(userId, "KUNDLI_AI_"+miniChatWindow.conversationId, question, "API hit failed Exception = "+e);
            }
        });

    }

    /**
     * Returns a StringBuilder containing the post data parameters for the HTTP request.
     *
     * This method generates the parameters for the Kundli AI server request. It takes the
     * user profile data and request parameters and encodes them in a StringBuilder.
     *
     * @param activity The activity context
     * @param question The user's question to send to the AI server
     * @return StringBuilder containing the post data parameters
     */
    private StringBuilder getPostDataParams(Activity activity, String question) {
        StringBuilder postDataParams = new StringBuilder();

        // Get the user profile data
        BeanHoroPersonalInfo beanHoroPersonalInfo;
        if (miniChatWindow.isMatching) {
            beanHoroPersonalInfo = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail();
        } else if (!TextUtils.isEmpty(miniChatWindow.clnyType) || miniChatWindow.selectedModule == com.ojassoft.astrosage.utils.CGlobalVariables.AI_HOME_SCREEN_MODULE) {
            beanHoroPersonalInfo = com.ojassoft.astrosage.utils.CUtils.getNumeroBeanHoroPersonalInfo();
        } else {
            beanHoroPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
        }

        try {
            // Add the user profile data to the post data parameters
            postDataParams.append("&name=").append(urlEncodeData(beanHoroPersonalInfo.getName()));
            postDataParams.append("&sex=").append(urlEncodeData(beanHoroPersonalInfo.getGender()));
            postDataParams.append("&day=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getDay() + ""));
            postDataParams.append("&month=").append(urlEncodeData((beanHoroPersonalInfo.getDateTime().getMonth() + 1) + ""));
            postDataParams.append("&year=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getYear() + ""));
            postDataParams.append("&hrs=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getHour() + ""));
            postDataParams.append("&min=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getMin() + ""));
            postDataParams.append("&sec=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getSecond() + ""));
            postDataParams.append("&dst=").append(beanHoroPersonalInfo.getDST());

            // Add the location parameters
            postDataParams.append("&place=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getCityName(), "place")));
            postDataParams.append("&longdeg=").append(urlEncodeData(getCoordinateValue(beanHoroPersonalInfo.getPlace().getLongDeg(), KEY_LONGITUDE)));
            postDataParams.append("&longmin=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getLongMin(), "longmin")));
            postDataParams.append("&longew=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getLongDir(), "longew")));
            postDataParams.append("&latdeg=").append(urlEncodeData(getCoordinateValue(beanHoroPersonalInfo.getPlace().getLatDeg(), KEY_LATITUDE)));
            postDataParams.append("&latmin=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getLatMin(), "latmin")));
            postDataParams.append("&latns=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getLatDir(), "latns")));
            postDataParams.append("&timezone=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getTimeZoneValue() + "", "timezone")));

            // Add the horoscope parameters
            postDataParams.append("&ayanamsa=").append(beanHoroPersonalInfo.getAyanIndex());
            postDataParams.append("&charting=" + "0");
            postDataParams.append("&kphn=").append(beanHoroPersonalInfo.getHoraryNumber());
            postDataParams.append("&appversion=" + BuildConfig.VERSION_NAME);
            postDataParams.append("&pkgname=" + ASTROSAGE_AI_PACKAGE_NAME);//ASTROSAGE_AI_PACKAGE_NAME

            // Add the request parameters
            postDataParams.append("&methodname=" + "getanswer");
            postDataParams.append("&key=").append(CUtils.getApplicationSignatureHashCode(miniChatWindow));
            postDataParams.append("&userid=").append(urlEncodeData(com.ojassoft.astrosage.utils.CUtils.getUserName(miniChatWindow)));
            postDataParams.append("&pw=").append(urlEncodeData(com.ojassoft.astrosage.utils.CUtils.getUserPassword(miniChatWindow)));
            postDataParams.append("&androidid=").append(CUtils.getMyAndroidId(activity));
            postDataParams.append("&aiprofile=").append("38");//astroID for Kundli AI
            postDataParams.append("&cid=").append(miniChatWindow.conversationId);
            postDataParams.append("&klid=").append(miniChatWindow.conversationId);
            postDataParams.append("&isnewsession=").append("1");
            postDataParams.append("&iscl=").append("1");
            postDataParams.append("&question=").append(urlEncodeData(question.trim()));
            postDataParams.append("&isclyr=").append(miniChatWindow.varshphalYear);
            postDataParams.append("&sid=").append(miniChatWindow.screenId);
            if (miniChatWindow.clnyType != null) {
                postDataParams.append("&clnytype=").append(miniChatWindow.clnyType);
            }
            if (miniChatWindow.isMatching) {
                postDataParams.append(getMatchingGirlParameter());
            }
            if (miniChatWindow.rashiType != -1) {
                postDataParams.append("&rashi=").append(miniChatWindow.rashiType);
                postDataParams.append("&horoscopeyear=").append(miniChatWindow.horoscopeYear);
            }
            if (!TextUtils.isEmpty(miniChatWindow.lid)) {
                postDataParams.append("&lid=").append(miniChatWindow.lid);
                postDataParams.append("&pandate=").append(miniChatWindow.panDate);
            }
            if (!TextUtils.isEmpty(miniChatWindow.videoId)) {
                postDataParams.append("&videoid=").append(miniChatWindow.videoId);
            }
            postDataParams.append("&cc=").append(miniChatWindow.openedFrom);
            miniChatWindow.openedFrom = "0"; //set to default after sending

            // Add the language code
            int languageCode = com.ojassoft.astrosage.utils.CUtils.getLanguageCodeFromString(MiniChatWindow.langCode);
            postDataParams.append("&languagecode=").append(languageCode);
            postDataParams.append("&isroman=").append(AIChatWindowActivity.isRomanLang());// isRoman
            postDataParams.append("&cplace=").append(MiniChatWindow.cplace);// cplace
            postDataParams.append("&clat=").append(MiniChatWindow.clat);// clat
            postDataParams.append("&clong=").append(MiniChatWindow.clong);// cplace
            postDataParams.append("&ctimezone=").append(MiniChatWindow.ctimezone);// cplace
            postDataParams.append("&ctimezoneid=").append(MiniChatWindow.ctimezoneid);// cplace

        } catch (Exception e) {
            //Log.d("languageCodeIssue", "params e: " + e);
            miniChatWindow.responseErrorHandling(false);
        }

        return postDataParams;
    }

    /**
     * Adds a message to the Kundli chat history list.
     * 
     * This method creates a Message object and adds it to the global Kundli chat messages
     * history. It also tracks whether the answer ID was missing from the response.
     * 
     * @param messageText The text content of the message
     * @param messageFrom The sender of the message (ASTROLOGER or USER)
     * @param chatId Unique identifier for the chat message
     * @param animate Whether to animate the message display
     */
    public void addMessageToHistoryList(String messageText, String messageFrom, long chatId, boolean animate) {
        //Log.e("TestCloudChat", "addMessage() text = "+messageText);
        Message message = new Message();
        message.setAuthor(messageFrom);
        message.setDateCreated(CUtils.getDbNowDateAndTime());
        message.setMessageBody(messageText);
        message.setSeen(false);
        message.setChatId(chatId);
        message.setDelayed(false);
        message.setAnswerIdMissing(isAnswerIdMissing);

        if (AstrosageKundliApplication.kundliChatMessages == null) {
            AstrosageKundliApplication.kundliChatMessages = new ArrayList<>();
        }
        AstrosageKundliApplication.kundliChatMessages.add(new UserMessage(message));
        //Log.e("TestCloudChat", "size = "+AstrosageKundliApplication.kundliChatMessages.size());

    }

    /**
     * Creates and configures an HttpURLConnection for making requests to the Kundli AI server.
     * 
     * @param postDataParams The query parameters to append to the URL
     * @return Configured HttpURLConnection object
     * @throws IOException If connection creation fails
     */
    private static @NonNull HttpURLConnection getHttpURLConnection(StringBuilder postDataParams) throws IOException {
        URL url = new URL(com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_BASE_URL + "/pred?" + postDataParams);
        //Log.d("KundliAIChatTesting", "url: " + url);
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
     * Parses a complete JSON response from the Kundli AI server.
     * 
     * This method handles complete (non-streaming) responses and:
     * - Extracts the answer text and metadata
     * - Checks for question limits and subscription status
     * - Processes buttons and interactive elements
     * - Handles special response types (videos, intents)
     * - Updates the UI with the response
     * - Manages chat history and database storage
     * - Handles server busy errors and question limit exceeded scenarios
     * 
     * @param responseData The complete JSON response string
     * @param isStreamedResponse Whether this is a streaming response
     * @param question The original question that generated this response
     */
    public void parseJsonAtOnce(String responseData, Boolean isStreamedResponse, String question) {
        //Log.d("languageCodeIssue", "parseJsonAtOnce responseData->" + responseData);
        try {
            if (!TextUtils.isEmpty(responseData)) {
                JSONObject response = new JSONObject(responseData);

                String qType = response.getString("QUESTYPE");
                StringBuilder result = new StringBuilder(CUtils.convertMarkdownToHTML(response.getString("RESULT")));
                String link = response.getString("ANSWERLINK");
                JSONArray jsonArray = new JSONArray();

                if(response.has("ERRORCODE")&&response.getInt("ERRORCODE") == 404 ){
                    if (miniChatWindow != null) {
                        miniChatWindow.showServerIsBusy();
                        return;
                    }
                }
                if (response.has("QUESLIMIT")){

                    String questionLimitReached = response.getString("QUESLIMIT");
                    if (questionLimitReached.equals(CGlobalVariables.QUESTION_LIMIT_EXCEEDED) && result.length() == 0) {
                        CUtils.saveBooleanData(activity, AK_FREE_QUESTION_LIMIT_EXCEED_KEY, true);
                        if (miniChatWindow != null) {
                            miniChatWindow.showAlertForDhruvPurchase();
                        }
                        return;
                    }
                }
                try {
                    if (response.has("buttons")) {
                        jsonArray = response.getJSONArray("buttons");
                    }
                } catch (Exception e) {
                  //  Log.d("KundliAIChatTesting", "Exception 11->" + e);
                    miniChatWindow.responseErrorHandling(false);
                    dumpLogDataOnServer(userId, "KUNDLI_AI_"+miniChatWindow.conversationId, question, "parseJsonAtOnce response has buttons Exception="+e);
                }

                long answerId;
                try {
                    answerId = Long.parseLong(response.optString("ANSWERID"));
                }catch(Exception e){
                    answerId = System.currentTimeMillis();
                    isAnswerIdMissing = true;
                }


                if (!isStreamedResponse) {

                    String videoId = CUtils.extractYTId(link);

                    qType = ""; // remove this line to enable varta intents
                    if (!TextUtils.isEmpty(CUtils.getAppIntentCode(qType))) {
                        miniChatWindow.setResponse(CUtils.convertMarkdownToHTML(result.toString()),Long.parseLong(CUtils.getAppIntentCode(qType)),false,true);
                        return;
                    } else if (result.length() == 0) {
                        String questionType = response.getString("QUESTYPE");
                        miniChatWindow.responseErrorHandling(false);
                    } else if (videoId != null) {
                        result.insert(0, answerId + "___");
                        miniChatWindow.speakOut(result.toString(),  "");
                        return;
                    }

                }

                String finalResult = addButtons(result.toString(), jsonArray, link);
                //Log.d("TestCloudChat", "Link : " + link);
                Log.d("TestCloudChat", "Final Result : " + finalResult);

                if (!TextUtils.isEmpty(finalResult)) {
                    miniChatWindow.setResponse(finalResult,answerId,true,true);
                    addMessageToHistoryList(CUtils.convertMarkdownToHTML(finalResult).replace("\n", "<br>"), CGlobalVariables.ASTROLOGER,answerId, false);
                    miniChatWindow.chatDao.saveAnswerToDb(String.valueOf(answerId),question,CUtils.convertMarkdownToHTML(finalResult).replace("\n", "<br>"));
                }

               // Log.e("TestCloudChat", "saveAnswerToDb() text = "+finalResult);
            }else {
                dumpLogDataOnServer(userId, "KUNDLI_AI_"+miniChatWindow.conversationId, question, "parseJsonAtOnce response is empty");
            }
        } catch (Exception e) {
            //Log.e("TestCloudChat", "error() text = "+e);
            miniChatWindow.responseErrorHandling(false);
            dumpLogDataOnServer(userId, "KUNDLI_AI_"+miniChatWindow.conversationId, question, "parseJsonAtOnce exception1 = "+e);
        }


    }

    /**
     * Adds interactive buttons and links to the response text.
     * 
     * This method appends button elements and external links to the response
     * in a special format that the UI can parse and display as interactive elements.
     * 
     * @param updatedString The response text to append buttons to
     * @param jsonArray Array of button objects from the server response
     * @param link External link URL if provided
     * @return Response text with buttons and links appended
     */
    private String  addButtons(String  updatedString, JSONArray jsonArray, String link) {
        StringBuilder result = new StringBuilder(updatedString);
        try {
            if (!TextUtils.isEmpty(link)) {
                result.append("~~").append(activity.getResources().getString(R.string.open_site)).append("~~").append(link);
            }
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getJSONObject(i).getString("text");
                    String value = jsonArray.getJSONObject(i).getString("value");
                    if (TextUtils.isEmpty(value)) {
                        value = "@";
                    }
                    result.append("~~").append(name).append("~~").append(value);
                }
            }
        } catch (Exception e) {
            //Log.d("languageCodeIssue", "addButtons ex->" + e);
            miniChatWindow.responseErrorHandling(false);
        }
        return result.toString();
    }

    /**
     * Generates parameters for matching horoscope analysis.
     * 
     * This method creates the additional parameters needed when performing
     * horoscope matching analysis, including the girl's birth details and location.
     * 
     * @return StringBuilder containing the matching parameters
     */
    private StringBuilder getMatchingGirlParameter() {

        BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail();

        StringBuilder postDataParams = new StringBuilder();

        postDataParams.append("&name1=").append(urlEncodeData(beanHoroPersonalInfo.getName()));
        postDataParams.append("&day1=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getDay() + ""));
        postDataParams.append("&month1=").append(urlEncodeData((beanHoroPersonalInfo.getDateTime().getMonth() + 1) + ""));
        postDataParams.append("&year1=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getYear() + ""));
        postDataParams.append("&hrs1=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getHour() + ""));
        postDataParams.append("&min1=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getMin() + ""));
        postDataParams.append("&sec1=").append(urlEncodeData(beanHoroPersonalInfo.getDateTime().getSecond() + ""));
        postDataParams.append("&dst1=" + beanHoroPersonalInfo.getDST());

        postDataParams.append("&place1=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getCityName(), "place")));
        postDataParams.append("&longdeg1=").append(urlEncodeData(getCoordinateValue(beanHoroPersonalInfo.getPlace().getLongDeg(), KEY_LONGITUDE)));
        postDataParams.append("&longmin1=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getLongMin(), "longmin")));
        postDataParams.append("&longew1=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getLongDir(), "longew")));
        postDataParams.append("&latdeg1=").append(urlEncodeData(getCoordinateValue(beanHoroPersonalInfo.getPlace().getLatDeg(), KEY_LATITUDE)));
        postDataParams.append("&latmin1=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getLatMin(), "latmin")));
        postDataParams.append("&latns1=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getLatDir(), "latns")));
        postDataParams.append("&timezone1=").append(urlEncodeData(setExtraLocationParams(beanHoroPersonalInfo.getPlace().getTimeZoneValue() + "", "timezone")));

        return postDataParams;
    }

    /**
     * Replaces all occurrences of a target string with a replacement string in a StringBuilder.
     * 
     * @param sb The StringBuilder to modify
     * @param target The string to replace
     * @param replace The replacement string
     */
    private void replaceString(StringBuilder sb,String target,String replace){
        int index;
        while ((index = sb.indexOf(target)) != -1) {
            sb.replace(index, index + target.length(), replace);
        }
    }

    /**
     * Sends error and debugging information to the server for analysis.
     * 
     * This method is used to log various error conditions, response issues,
     * and debugging information to help with troubleshooting Kundli AI chat problems.
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
