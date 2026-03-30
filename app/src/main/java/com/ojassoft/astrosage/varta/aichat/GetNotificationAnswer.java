package com.ojassoft.astrosage.varta.aichat;

import static com.ojassoft.astrosage.utils.CUtils.KEY_LATITUDE;
import static com.ojassoft.astrosage.utils.CUtils.KEY_LONGITUDE;
import static com.ojassoft.astrosage.utils.CUtils.getCoordinateValue;
import static com.ojassoft.astrosage.utils.CUtils.prepareUserProfile;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.ui.activity.AINotificationChatActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetNotificationAnswer {

    private final Activity activity;
    private AINotificationChatActivity AINotificationChatActivity;
    public String answerId;
    String response;
    int count = 0;
    String updatedString = "";

    private final String revertQuestionCount;

    public GetNotificationAnswer(Activity activity, String revertQuestionCount) {
        this.activity = activity;
        this.revertQuestionCount = revertQuestionCount;
        if (activity instanceof AINotificationChatActivity) {
            this.AINotificationChatActivity = ((AINotificationChatActivity) activity);
        }
    }


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

    //HttpURLConnection GET request
    public void getQueryAnswer(Activity activity, String question) {

        StringBuilder postDataParams = new StringBuilder();

        try {
            UserProfileData userProfileData = prepareUserProfile(activity);

            postDataParams.append("&name=").append(urlEncodeData(userProfileData.getName()));
            postDataParams.append("&sex=").append(urlEncodeData(userProfileData.getGender()));
            postDataParams.append("&day=").append(urlEncodeData(userProfileData.getDay()));
            postDataParams.append("&month=").append(urlEncodeData(userProfileData.getMonth()));
            postDataParams.append("&year=").append(urlEncodeData(userProfileData.getYear()));
            postDataParams.append("&hrs=").append(urlEncodeData(userProfileData.getHour()));
            postDataParams.append("&min=").append(urlEncodeData(userProfileData.getMinute()));
            postDataParams.append("&sec=").append(urlEncodeData(userProfileData.getSecond()));
            postDataParams.append("&dst=" + "0");

            postDataParams.append("&place=").append(urlEncodeData(setExtraLocationParams(userProfileData.getPlace(), "place")));
            postDataParams.append("&longdeg=").append(urlEncodeData(getCoordinateValue(userProfileData.getLongdeg(), KEY_LONGITUDE)));
            postDataParams.append("&longmin=").append(urlEncodeData(setExtraLocationParams(userProfileData.getLongmin(), "longmin")));
            postDataParams.append("&longew=").append(urlEncodeData(setExtraLocationParams(userProfileData.getLongew(), "longew")));
            postDataParams.append("&latdeg=").append(urlEncodeData(getCoordinateValue(userProfileData.getLatdeg(), KEY_LATITUDE)));
            postDataParams.append("&latmin=").append(urlEncodeData(setExtraLocationParams(userProfileData.getLatmin(), "latmin")));
            postDataParams.append("&latns=").append(urlEncodeData(setExtraLocationParams(userProfileData.getLatns(), "latns")));
            postDataParams.append("&timezone=").append(urlEncodeData(setExtraLocationParams(userProfileData.getTimezone(), "timezone")));

            postDataParams.append("&ayanamsa=" + "0");
            postDataParams.append("&charting=" + "0");
            postDataParams.append("&kphn=" + "0");
            postDataParams.append("&appversion=" + BuildConfig.VERSION_NAME);
            postDataParams.append("&pkgname=" + BuildConfig.APPLICATION_ID);
            postDataParams.append("&methodname=" + "getanswer");
            postDataParams.append("&key=").append(CUtils.getApplicationSignatureHashCode(AINotificationChatActivity));
            postDataParams.append("&userid=").append(CUtils.getCountryCode(AINotificationChatActivity)).append(CUtils.getUserID(AINotificationChatActivity));
            postDataParams.append("&androidid=").append(CUtils.getMyAndroidId(activity));
            postDataParams.append("&aiprofile=").append(AstrosageKundliApplication.selectedAstrologerDetailBean.getAiAstrologerId());
            postDataParams.append("&cid=").append(AINotificationChatActivity.CHANNEL_ID);
            postDataParams.append("&isnewsession=").append(AINotificationChatActivity.isNewSession);
            if (AINotificationChatActivity.isNewSession.equals("0")) {
                postDataParams.append("&islocallyanswered=").append(AINotificationChatActivity.isLocallyAnswered);
                if (AINotificationChatActivity.isLocallyAnswered.equals("1")) {
                    AINotificationChatActivity.isLocallyAnswered = "0";
                    postDataParams.append("&previousques=").append(AINotificationChatActivity.previousQues);
                    postDataParams.append("&previousans=").append(AINotificationChatActivity.previousAns);
                }
            }
            postDataParams.append("&question=").append(urlEncodeData(question.trim()));
            postDataParams.append("&isnoti=").append("1");
            postDataParams.append("&r=").append(urlEncodeData(revertQuestionCount));

            /*int chatLangCode = ((AINotificationChatActivity) activity).AI_LANGUAGE_CODE;
            if (AINotificationChatActivity.langCode.equals("hi")) {
                chatLangCode = CGlobalVariables.HINDI;
            } else if (AINotificationChatActivity.langCode.equals("en")) {
                chatLangCode = CGlobalVariables.ENGLISH;
            }

            int isRoman = AINotificationChatActivity.isRomanLang();
            Log.d("languageCodeIssue", "getQueryAnswer isRoman: "+isRoman);
            if (isRoman == 1)
                postDataParams.append("&languagecode=" + "1");
            else
                postDataParams.append("&languagecode=").append(chatLangCode);*/

            int languageCode = com.ojassoft.astrosage.utils.CUtils.getLanguageCodeFromString(AINotificationChatActivity.langCode);

            postDataParams.append("&languagecode=").append(languageCode);
            postDataParams.append("&isroman=").append(AINotificationChatActivity.isRoman); // isRoman

            //Log.d("languageCodeIssue", "params: " + postDataParams);

        } catch (Exception e) {
            //Log.d("languageCodeIssue", "params e: " + e);
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        response = "";
        updatedString = "";
        count = 0;

        executor.execute(() -> {
            try {
                HttpURLConnection connection = getHttpURLConnection(postDataParams);

                int responseCode = connection.getResponseCode();

                //Log.d("languageCodeIssue", "responseCode: "+responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    boolean typeWriterEffect = true;
                    //Log.d("languageCodeIssue", "br.readLine(): "+br.readLine());
                    while ((line = br.readLine()) != null) {
                        count += 1;
                        int finalCount = count;
                        if (!TextUtils.isEmpty(line)) {
                            response += line;
                            Log.d("languageCodeIssue", "line: " + line);
                            if (finalCount == 1) {
                                if (response.charAt(0) == '{' && response.charAt(response.length() - 1) == '}') {
//                                    JSONObject responseJSON = new JSONObject(response);
//                                    answerId = (responseJSON.has("ANSWERID")) ? responseJSON.getString("ANSWERID") : "";
//                                    AINotificationChatActivity.setResponse(convertMarkdownToHTML(responseJSON.getString("RESULT")),answerId);
                                    parseJsonAtOnce(response, false, question);
                                } else {
                                    response = response.replace("null", "");
                                    answerId = response.substring(response.indexOf("\"ANSWERID\":\"") + 12);
                                    answerId = answerId.substring(0, answerId.indexOf("\""));
                                    //AINotificationChatActivity.setResponse("", true, answerId);
                                }
                            } else {
                                updatedString = "";
                                if (response.charAt(0) == '{' && response.charAt(response.length() - 1) == '}') {
                                    try {
                                        //Log.d("languageCodeIssue", "complete: " + response);
                                        JSONObject jsonObject = new JSONObject(response);
                                        updatedString = jsonObject.getString("RESULT");
                                        //updatedString = updatedString.replace("\n\n", "<br><br>");
                                        if(!updatedString.isEmpty()) {
                                            AINotificationChatActivity.setResponse(convertMarkdownToHTML(updatedString), typeWriterEffect, answerId);
                                            typeWriterEffect = false;
                                        }
                                    } catch (Exception e) {
                                        AINotificationChatActivity.responseErrorHandling(false);
                                    }
                                } else {
                                    try {
                                        //Log.d("languageCodeIssue", "incomplete: " + response);
                                        updatedString = (String) response.subSequence(response.indexOf("\"RESULT\":\"") + 10, response.length() - 1);
                                        //updatedString = updatedString.replace("\n\n", "<br><br>");
                                        if(!updatedString.isEmpty()) {
                                            AINotificationChatActivity.setResponse(convertMarkdownToHTML(updatedString), typeWriterEffect, answerId);
                                            typeWriterEffect = false;
                                        }
                                    } catch (Exception e1) {
                                        //
                                    }
                                }

                            }
                        }
                    }
                    if(!updatedString.equals("")) {
                        AINotificationChatActivity.updateMessageInItem(convertMarkdownToHTML(updatedString).replace("\n","<br>"));
                    }
                } else {
                    AINotificationChatActivity.responseErrorHandling(false);
                }

            } catch (IOException e) {
                //Log.e("languageCodeIssue", "Exception 2->" + e);
                AINotificationChatActivity.responseErrorHandling(false);
            } catch (Exception e) {
                //Log.e("languageCodeIssue", "Exception 3->" + e);
                AINotificationChatActivity.responseErrorHandling(false);
            }
        });

    }

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

    private String urlEncodeData(String string) {
        try {
            string = URLEncoder.encode(string, "UTF-8");
        } catch (Exception e) {
            //
        }
        return string;
    }

    public void parseJsonAtOnce(String responseData, Boolean isStreamedResponse, String question) {
        //Log.d("languageCodeIssue", "parseJsonAtOnce responseData->" + responseData);
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
                    //CUtils.saveUserQuestionLimit(AINotificationChatActivity, questionLimitReached);
                    //String questionLimitReached = "1";
                    if (questionLimitReached.equals(CGlobalVariables.QUESTION_LIMIT_EXCEEDED) && result.length() == 0) {
                        //Log.d("languageCodeIssue", "questionLimitReached->");
                        CUtils.showSnackbar(AINotificationChatActivity.findViewById(android.R.id.content),activity.getString(R.string.there_is_no_chat_ongoing), AINotificationChatActivity);
                        AINotificationChatActivity.responseErrorHandling(false);
                        return;
                    }
                }
                /*if (response.has("QUESREMAIN")) {
                    CUtils.saveQuestionRemaining(AINotificationChatActivity, response.getString("QUESREMAIN"));
                }*/
                try {
                    if (response.has("buttons")) {
                        jsonArray = response.getJSONArray("buttons");
                    }
                } catch (Exception e) {
                    //Log.d("languageCodeIssue", "Exception 11->" + e);
                    AINotificationChatActivity.responseErrorHandling(false);
                }

                String answerId = (response.has("ANSWERID")) ? response.getString("ANSWERID") : "";

                if (!isStreamedResponse) {

                    String videoId = CUtils.extractYTId(link);

                    qType = ""; // remove this line to enable varta intents
                    if (!TextUtils.isEmpty(CUtils.getAppIntentCode(qType))) {
                        AINotificationChatActivity.speakOut(result.toString(), link, CUtils.getAppIntentCode(qType));
                        return;
                    } else if (result.length() == 0) {
                        String questionType = response.getString("QUESTYPE");
                        AINotificationChatActivity.isLocallyAnswered = "1";
                        result = new StringBuilder(LocalAIAnswers.getInstance(AINotificationChatActivity).searchAnswer(question, questionType));
                        //Log.d("appHandIssue", "parseJsonAtOnce result: "+result);
                        AINotificationChatActivity.previousAns = URLEncoder.encode(String.valueOf(result), "UTF-8");
                        AINotificationChatActivity.responseErrorHandling(true);
                    } else if (videoId != null) {
                        result.insert(0, answerId + "___");
                        link = answerId + "___youtube" + link;
                        AINotificationChatActivity.speakOut(result.toString(), link, "");
                        return;
                    }

                }

                result = addButtons(result, jsonArray, link);
                StringBuilder finalResult = result;
                String finalLink = link;
                if (!TextUtils.isEmpty(finalResult)) {
                    AINotificationChatActivity.runOnUiThread(() -> {
                        AINotificationChatActivity.isNewSession = "0";
                        AINotificationChatActivity.speakOut(finalResult.toString(), finalLink, answerId);
                    });
                }
            }
        } catch (Exception e) {
            //Log.d("languageCodeIssue", "parseJsonAtOnce e responseData: "+responseData);
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                String status = jsonObject.optString("status");
                if (status.equals("0")) {
                    //Log.d("languageCodeIssue", "parseJsonAtOnce e status: "+status);
                    AINotificationChatActivity.showQuestionExpiredDialog();
                }
            } catch (Exception ex) {
                //Log.d("languageCodeIssue", "parseJsonAtOnce ex: "+ex);
            } finally {
                AINotificationChatActivity.responseErrorHandling(false);
            }
        }


    }

    public  String convertMarkdownToHTML(String text) {
        // Convert ###### Header 6 to <Strong>Header 6</Strong>
        text = replaceMarkdown(text, "(?m)^###### (.*?)$", "<strong>$1</strong>");
        // Convert ##### Header 5 to <Strong>Header 5</Strong>
        text = replaceMarkdown(text, "(?m)^##### (.*?)$", "<strong>$1</strong>");
        // Convert #### Header 4 to <Strong>Header 4</Strong>
        text = replaceMarkdown(text, "(?m)^#### (.*?)$", "<strong>$1</strong>");
        // Convert ### Header 3 to <Strong>Header 3</Strong>
        text = replaceMarkdown(text, "(?m)^### (.*?)$", "<strong>$1</strong>");
        // Convert ## Header 2 to <Strong>Header 2</Strong>
        text = replaceMarkdown(text, "(?m)^## (.*?)$", "<strong>$1</strong>");
        // Convert # Header 1 to <Strong>Header 1</Strong>
        text = replaceMarkdown(text, "(?m)^# (.*?)$", "<strong>$1</strong>");
        // Convert **bold** or __bold__ to <b>bold</b>
        text = replaceMarkdown(text, "(\\*\\*|__)(.*?)\\1", "<strong>$2</strong>");
        // Convert *italic* or _italic_ to <i>italic</i>
        text = replaceMarkdown(text, "(\\*|_)(.*?)\\1", "<em>$2</em>");
        return text;
    }
    private String replaceMarkdown(String text, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replacement);
    }
    private StringBuilder addButtons(StringBuilder result, JSONArray jsonArray, String link) {
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
            AINotificationChatActivity.responseErrorHandling(false);
        }
        return result;
    }

}
