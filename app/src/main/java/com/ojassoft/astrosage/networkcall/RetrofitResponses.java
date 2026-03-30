package com.ojassoft.astrosage.networkcall;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface RetrofitResponses {
    void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response);

    void onFailure(Call<ResponseBody> call, Throwable t);
}
