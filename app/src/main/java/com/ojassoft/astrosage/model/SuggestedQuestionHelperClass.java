package com.ojassoft.astrosage.model;

import static com.ojassoft.astrosage.utils.CGlobalVariables.LAST_LANG_CODE_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_DATE_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY;
import static com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestedQuestionHelperClass {
    Activity activity;
    public HashMap<String, ArrayList<String>> questionMap;
    String moduleId;
    int APP_LANG_CODE;

    public SuggestedQuestionHelperClass(Activity activity, int moduleId) {
        this.activity = activity;
        this.moduleId = String.valueOf(moduleId);
        APP_LANG_CODE = com.ojassoft.astrosage.utils.CUtils.getLanguageCode(activity);
        this.questionMap = new HashMap<>(); // Ensure questionMap is never null
    }

    public void getSuggestQuestionForModule(SuggestedQuestionListener suggestedQuestionListener) {
        try{
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date curentDate = new Date();
        String todayDate = formatter.format(curentDate);

        String moduleKeyForCachedQuestion = moduleId + "_" + MODULE_SUGGESTED_QUESTIONS_KEY;

        String moduleKeyForCachedDate = moduleId + "_" + MODULE_SUGGESTED_QUESTIONS_DATE_KEY;


        /**@Gaurav added check lastLanguage, to clean caching if language change detected
         * create module wise key because suggested questions came with modules(store different caching for different modules)**/
        String lastLangCodeKey = moduleId + "_" + LAST_LANG_CODE_KEY;


        String lastLangCode = CUtils.getStringData(activity, lastLangCodeKey, "0");
        if (!lastLangCode.equalsIgnoreCase(String.valueOf(APP_LANG_CODE))) {
            CUtils.saveStringData(activity, moduleKeyForCachedDate, "");
        }

        String moduleSuggestedQuestionDate = CUtils.getStringData(activity, moduleKeyForCachedDate, "");

        if(moduleSuggestedQuestionDate.equalsIgnoreCase(todayDate)){
                String suggestedQuestion = CUtils.getStringData(activity,moduleKeyForCachedQuestion,"");
                if(!TextUtils.isEmpty(suggestedQuestion)){ //check in local
                    parseSuggestedQuestion(suggestedQuestion, moduleKeyForCachedQuestion,suggestedQuestionListener);
                    return;
                }
            }

            Call<ResponseBody> call = RetrofitClient.getKundliAIInstance().create(ApiList.class).getSuggestedQuestionModule(getQuestionListParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseString = response.body().string();
                            Log.e("TestQuestion", "onResponse: " + responseString);
                            parseSuggestedQuestion(responseString, moduleKeyForCachedQuestion,suggestedQuestionListener);
                            CUtils.saveStringData(activity,moduleSuggestedQuestionDate,todayDate);
                            CUtils.saveStringData(activity, lastLangCodeKey, String.valueOf(APP_LANG_CODE));
                        }

                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        } catch (Exception e) {
            Log.e("TestQuestion", "getSuggestQuestionForModule: exception: "+e.getMessage() );
        }
    }

    private void parseSuggestedQuestion(String responseString, String suggestedQuestionKey, SuggestedQuestionListener suggestedQuestionListener) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            CUtils.saveStringData(activity,suggestedQuestionKey,jsonObject.toString());
            Iterator<String> keys = jsonObject.keys();

            Gson gson = new Gson();
            // Iterate over the keys
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> questionList = (gson.fromJson(jsonArray.toString(), listType));

                questionMap.put(key, questionList);
            }
            suggestedQuestionListener.onSuggestedQuestionReady();
        } catch (Exception e){
            //
        }
    }

    private Map<String, String> getQuestionListParams() {
        Map<String, String> headers = new HashMap<>();
        headers.put("lang",String.valueOf(APP_LANG_CODE));
        headers.put("moduleid", moduleId);
        headers.put(APP_KEY, getApplicationSignatureHashCode(activity));
        headers.put("methodname", "suggestedquesakmodules");
        return headers;
    }


    public ArrayList<String> getSuggestedQuestionsForScreenId(int screenId) {
        try {
            return new ArrayList<>(questionMap.get(String.valueOf(screenId)));
        } catch (Exception e) {
            return null;
        }
    }

 public static interface SuggestedQuestionListener {
        void onSuggestedQuestionReady();
    }

}
