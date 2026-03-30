package com.ojassoft.astrosage.networkcall;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LIVE_DOMAIN_UPI_PAYMENT;

import com.ojassoft.astrosage.varta.utils.CGlobalVariables;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static retrofit2.Retrofit retrofitInstance;
    private static retrofit2.Retrofit retrofitInstance3;
    private static retrofit2.Retrofit retrofitAIInstance;
    private static retrofit2.Retrofit retrofitKundliAIInstance;
    private static retrofit2.Retrofit retrofitImageInstance;
    // public static String DOMAIN = "https://vartaapi.astrosage.com";

    public static retrofit2.Retrofit getInstance() {
        if (retrofitInstance == null) {

            CookieHandler cookieHandler = new CookieManager();

            OkHttpClient client = new OkHttpClient.Builder()
                    .cookieJar(new JavaNetCookieJar(cookieHandler))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofitInstance = new retrofit2.Retrofit.Builder()
                    .baseUrl(CGlobalVariables.LIVE_DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitInstance;
    }

    public static retrofit2.Retrofit getInstance2() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (retrofitInstance == null) {
            retrofitInstance = new retrofit2.Retrofit.Builder()
                    .baseUrl(com.ojassoft.astrosage.utils.CGlobalVariables.API2_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofitInstance;
    }

    public static retrofit2.Retrofit getInstance3() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (retrofitInstance3 == null) {
            retrofitInstance3 = new retrofit2.Retrofit.Builder()
                    .baseUrl(LIVE_DOMAIN_UPI_PAYMENT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofitInstance3;
    }

    public static retrofit2.Retrofit getImageInstance() {
        /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);*/

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //httpClient.addInterceptor(logging);

        if (retrofitImageInstance == null) {
            retrofitImageInstance = new retrofit2.Retrofit.Builder()
                    .baseUrl(CGlobalVariables.IMAGE_DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofitImageInstance;
    }

    public static retrofit2.Retrofit getAIInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieHandler cookieHandler = new CookieManager();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
                //.addInterceptor(logging);

        if (retrofitAIInstance == null) {

            retrofitAIInstance = new retrofit2.Retrofit.Builder()
                    .baseUrl(com.ojassoft.astrosage.utils.CGlobalVariables.AI_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofitAIInstance;
    }

    public static retrofit2.Retrofit getKundliAIInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieHandler cookieHandler = new CookieManager();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
                //.addInterceptor(logging);

        if (retrofitKundliAIInstance == null) {

            retrofitKundliAIInstance = new retrofit2.Retrofit.Builder()
                    .baseUrl(com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofitKundliAIInstance;
    }
}
