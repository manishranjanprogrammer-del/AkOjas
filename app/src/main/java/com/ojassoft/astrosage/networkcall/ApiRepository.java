package com.ojassoft.astrosage.networkcall;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ojassoft.astrosage.varta.model.GiftModel;
import com.ojassoft.astrosage.varta.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.QueryMap;

public class ApiRepository {

    private ApiList apiService;
    public ApiRepository(Context context) {
        apiService = RetrofitClient.getInstance().create(ApiList.class);
    }

    // Example method to call your API
    public void getGiftDataFromServer(@QueryMap Map<String, String> stringMap, final ApiCallback callback) {
        try {
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getAstrologerLiveList(stringMap);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Success: Return the result to the callback
                        String myResponse = null;
                        try {
                            myResponse = response.body().string();
                            parseGiftList(myResponse,callback);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        // Error response: Return the error message to the callback
                        callback.onError("API Error: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("testGift","called 5");
                    // Network error: Return the error message to the callback
                    callback.onError("Network Error: " + t.getMessage());
                }
            });
        }catch (Exception e){
           //
        }

    }


    public static void parseGiftList(String liveAstroData, ApiCallback callback) {
        if (TextUtils.isEmpty(liveAstroData)) {
            return;
        }
        try {
            //Log.e("SAN ", " gift parselist " + liveAstroData);
            JSONObject jsonObject = new JSONObject(liveAstroData);
            String imageBaseUrl = jsonObject.getString("servicesimgbaseurl");
            JSONArray jsonArray = jsonObject.getJSONArray("giftservices");
            ArrayList<GiftModel> giftModelArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                GiftModel giftModel = new GiftModel();
                giftModel.setServiceid(object.getString("serviceid"));
                giftModel.setServicename(object.getString("servicename"));
                giftModel.setSmalliconfile(object.getString("smalliconfile"));
                giftModel.setCategoryid(object.getString("categoryid"));
                giftModel.setRate(object.getString("rate"));
                giftModel.setRaters(object.getString("raters"));
                giftModel.setActualrate(object.getString("actualrate"));
                giftModel.setActualraters(object.getString("actualraters"));
                giftModel.setPaymentamount(object.getString("paymentamount"));
                giftModel.setOffermessage(object.getString("offermessage"));
                giftModel.setOffermessage(object.optString("offeramout"));
                giftModel.setIconcode(object.optString("iconcode"));
                giftModelArrayList.add(giftModel);
            }
            CUtils.setGiftModelArrayList(giftModelArrayList);
            CUtils.setGiftImageBaseUrl(imageBaseUrl);
            callback.onSuccess();
        } catch (Exception e) {
            Log.d("testGift","Error Called :-- "+e.getMessage());
        }
    }
    // Callback interface to handle success and error
    public interface ApiCallback {
        void onSuccess();
        void onError(String errorMessage);
    }
}

