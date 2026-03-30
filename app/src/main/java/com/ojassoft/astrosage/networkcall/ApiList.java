package com.ojassoft.astrosage.networkcall;

import com.ojassoft.astrosage.varta.aichat.models.GreetingsModel;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiList {
    @POST("astrologers-list-v3")
    Call<ResponseBody> getAstrologerList(@QueryMap Map<String, String> stringMap);

    @POST("astrologers-list-v3")
    Call<ResponseBody> getAstrologerListMiniList(@QueryMap Map<String, String> stringMap);

    @POST("live-astrologers-list")
    Call<ResponseBody> getAstrologerLiveList(@QueryMap Map<String, String> stringMap);

    @POST("consultation-history")
    Call<ResponseBody> getRecentHistory(@QueryMap Map<String, String> stringMap);

    @POST("userlogin")
    Call<ResponseBody> backgroundLogin(@QueryMap Map<String, String> stringMap);

    @POST("registerAS")
    Call<ResponseBody> sendOtp(@QueryMap Map<String, String> stringMap);

    @POST("registerwithtruecallerASv3")
    Call<ResponseBody> registerWithTrueCaller(@QueryMap Map<String, String> headers);


    @POST("registerAS")
    Call<ResponseBody> reSendOtp(@QueryMap Map<String, String> headers);

    @POST("send-otp-via-call")
    Call<ResponseBody> reSendViaCallOtp(@QueryMap Map<String, String> headers);


    @POST("submitASotp")
    Call<ResponseBody> verifyOtp(@QueryMap Map<String, String> headers);

    @FormUrlEncoded
    @POST("partnerid-session")
    Call<ResponseBody> createSession(@Field("key") String key, @Field("prtnr_id") String prtnr_id);

    @POST("consultation-history")
    Call<ResponseBody> getHistory(@QueryMap Map<String, String> headers);

    @POST("disableusercallrecord")
    Call<ResponseBody> callDisableUserCallRecord(@QueryMap Map<String, String> headers);

    @POST("live-astrologers-list")
    Call<ResponseBody> getLiveAstrologerList(@QueryMap Map<String, String> headers);

    @POST("astrologer-live-schedules")
    Call<ResponseBody> getScheduleLiveAstrologerList(@QueryMap Map<String, String> headers);

    @POST("get-wallet-balance")
    Call<ResponseBody> getWalletBalance(@QueryMap Map<String, String> headers);

    @POST("get-chat-history-v2")
    Call<ResponseBody> getChatHistoryV2(@QueryMap Map<String, String> headers);


    @POST("get-chat-history")
    Call<ResponseBody> getChatHistory(@QueryMap Map<String, String> headers);

    @POST("astrologer-status-price")
    Call<ResponseBody> getAstrologerStatusPrice(@QueryMap Map<String, String> headers);

    @POST("astrologer-details-v2")
    Call<ResponseBody> getAstrologerDetails(@QueryMap Map<String, String> headers);

    @POST("newfollow")
    Call<ResponseBody> followAstrologer(@QueryMap Map<String, String> headers);

    @POST("add-to-queue")
    Call<ResponseBody> getNotificationIfAstroBusy(@QueryMap Map<String, String> headers);

    @POST("user-review-report-abuse")
    Call<ResponseBody> reviewReport(@QueryMap Map<String, String> headers);

    @POST("send-gift")
    Call<ResponseBody> sendGift(@QueryMap Map<String, String> headers);

    @POST("loadmorefeedback")
    Call<ResponseBody> getAstrologerFeedback(@QueryMap Map<String, String> headers);

    @POST("connect-call")
    Call<ResponseBody> connectNetworkCall(@QueryMap Map<String, String> headers);

    @POST("connect-call-ai")
    Call<ResponseBody> connectAICall(@QueryMap Map<String, String> headers);

    @POST("end-chat-v2")
    Call<ResponseBody> endChat(@QueryMap Map<String, String> headers, @Query("callsid") String callsId, @Query("activity") String activity);

    @POST("validate-coupon-code")
    Call<ResponseBody> validateCouponCode(@QueryMap Map<String, String> headers);

    @POST("fetch-user-token")
    Call<ResponseBody> fetchFireBaseToken(@QueryMap Map<String, String> headers);

    @POST("connect-call")
    Call<ResponseBody> connectCall(@QueryMap Map<String, String> headers);

    @POST("connect-chat-v2")
    Call<ResponseBody> connectChat(@QueryMap Map<String, String> headers);

    @POST("get-random-ai-astrologer")
    Call<ResponseBody> aiChatInitiate(@QueryMap Map<String, String> headers);


    @POST("wallet-recharge")
    Call<ResponseBody> walletRecharge(@QueryMap Map<String, String> headers);

    @POST("join-audience")
    Call<ResponseBody> liveJoinAudiance(@QueryMap Map<String, String> headers);

    @POST("connect-live-call")
    Call<ResponseBody> liveConnectLiveCall(@QueryMap Map<String, String> headers);

    @POST("live-astrologers-list")
    Call<ResponseBody> liveAstroList(@QueryMap Map<String, String> headers);

    @POST("end-live-call")
    Call<ResponseBody> liveEndLiveCall(@QueryMap Map<String, String> headers);

    @POST("nextrecharge")
    Call<ResponseBody> nextRecharge(@QueryMap Map<String, String> headers);

    @POST("extend-recharge-info")
    Call<ResponseBody> extendRechargeInfo(@QueryMap Map<String, String> headers);

    @POST("getcountrylist")
    Call<ResponseBody> getCountryList(@QueryMap Map<String, String> headers);


    @POST("user-chat-accept-v2")
    Call<ResponseBody> userChatAcceptV2(@QueryMap Map<String, String> headers);

    @POST("user-ai-call-accept")
    Call<ResponseBody> userAICallAccept(@QueryMap Map<String, String> headers);

    @POST("submit-feedback")
    Call<ResponseBody> submitFeedback(@QueryMap Map<String, String> headers);

    @POST("end-internet-call")
    Call<ResponseBody> endInternetcall(@QueryMap Map<String, String> headers);

    @POST("end-ai-call")
    Call<ResponseBody> endAIcall(@QueryMap Map<String, String> headers);

    @POST("refresh-user-profile")
    Call<ResponseBody> refreshUserProfile(@QueryMap Map<String, String> headers);


    @POST("update-user-profile")
    Call<ResponseBody> updateUserProfile(@QueryMap Map<String, String> headers);

    @POST("get-followed-astrologers")
    Call<ResponseBody> getFollowedAstrologers(@QueryMap Map<String, String> headers);

    @POST("is-user-following-astrologer")
    Call<ResponseBody> getIsUserFollowingAstrologer(@QueryMap Map<String, String> headers);

    @POST("newfollow")
    Call<ResponseBody> newFollowAstrologer(@QueryMap Map<String, String> headers);


    @POST("connectinternetvideocall")
    Call<ResponseBody> connectinternetvideocall(@QueryMap Map<String, String> headers);


    @POST("get-recharge-services")
    Call<ResponseBody> getRechargeServices(@QueryMap Map<String, String> headers);

    @POST("getaipassplan")
    Call<ResponseBody> getAIPassPlan(@QueryMap Map<String, String> headers);

    @POST("astrologers-list-ai")
    Call<ResponseBody> getAIAstrologerList(@QueryMap Map<String, String> stringMap);

    @POST("connect-chat-ai")
    Call<ResponseBody> connectChatAI(@QueryMap Map<String, String> headers);

    @Streaming
    @POST("pred")
    Call<ResponseBody> getAnswerFromServer(@QueryMap Map<String, String> headers);

    @POST("aiastrologer")
    Call<GreetingsModel> getGreetingMessage(@QueryMap Map<String, String> map);

    @POST("log-data")
    Call<ResponseBody> logDataReq(@QueryMap Map<String, String> headers);

    @POST("is-user-enable-for-feedback")
    Call<ResponseBody> getAstrologerFeedbackStatus(@QueryMap Map<String, String> headers);

    @GET("pred")
    Call<ResponseBody> setLikeUnlike(@Query("androidid") String androidid,
                                     @Query("pkgname") String pkgname,
                                     @Query("appversion") String appversion,
                                     @Query("key") String key,
                                     @Query("methodname") String methodname,
                                     @Query("answerid") String answerid,
                                     @Query("isliked") int isLiked,
                                     @Query("userid") String userId);

    @FormUrlEncoded
    @POST("set-config-data")
    Call<ResponseBody> getConfigDataReq(@Field("key") String key, @Field("countrycode") String countryCode, @Field("appversion") String appversion, @Field("pkgname") String pkgname);

    @POST("user-ad-exit-screen-comments")
    Call<ResponseBody> appExitApi(@QueryMap Map<String, String> headers);

    @POST("aiastrologer")
    Call<ResponseBody> getSuggestedQuestionModule(@QueryMap Map<String, String> headers);

    @GET("get-play-recording-url")
    Call<ResponseBody> getPlayRecordingUrl(@QueryMap Map<String, String> query);


    @POST("/astrogpt/userbirthdetailsextraction")
    Call<ResponseBody> getBirthDetails(@QueryMap Map<String, String> headers);

    @POST("/astrogpt/userbirthdetailsextractionMM")
    Call<ResponseBody> getBirthDetailsMatching(@QueryMap Map<String, String> headers);

    @GET("notification")
    Call<ResponseBody> callAIServiceNotification(@QueryMap Map<String, String> query);


    @GET()
    Call<ResponseBody> getImageBitmap(@Url String url);

    @POST("cloud-kundali-topup-services")
    Call<ResponseBody> getTopupRecharges(@QueryMap Map<String, String> headers);

    @POST("user-topup-recharge")
    Call<ResponseBody> sendTopupPurchaseRequest(@QueryMap Map<String, String> headers);

    @POST("get-random-ai-astrologer")
    Call<ResponseBody> getAIRandomAstrologer(@QueryMap Map<String, String> headers);

    @GET("aiastrologer")
    Call<ResponseBody> getRatingResponse(
            @Query("astroid") String astroid,
            @Query("userrating") String userrating,
            @Query("usercomment") String usercomment,
            @Query("userid") String userid,
            @Query("countrycode") String countrycode,
            @Query("deviceid") String deviceid,
            @Query("packagename") String packagename,
            @Query("appversion") String appversion,
            @Query("usergender") String usergender,
            @Query("day") String day,
            @Query("month") String month,
            @Query("year") String year,
            @Query("hour") String hour,
            @Query("min") String min,
            @Query("sec") String sec,
            @Query("lang") String lang,
            @Query("key") String key,
            @Query("methodname") String methodname,
            @Query("conversationid") String conversationid,
            @Query("answerid") String answerid
    );

    @GET("aiastrologer")
    Call<ResponseBody> sendAnswerFeedback(@QueryMap Map<String, String> map);

    @POST("aiastrologer")
    Call<ResponseBody> getUserChatHistory(@QueryMap Map<String, String> map);


    @POST("generate-variable-amount")
    Call<ResponseBody> generateOrderForVariableAmount(@QueryMap Map<String, String> headers);

    @POST("extend-chat")
    Call<ResponseBody> extendChatOnRegularPrice(@QueryMap Map<String, String> headers);

    @GET("download-brihat-kundli")
    Call<ResponseBody> getDownloadBrihatKundliUrl(@QueryMap Map<String, String> map);

    @GET("storeuserinstalltime")
    Call<ResponseBody> setAppInstallTime(@QueryMap Map<String, String> map);

    @GET("screen-count-data")
    Call<ResponseBody> updateClickCount(@QueryMap Map<String, String> map);

    @GET("registeremailforfreereport")
    Call<ResponseBody> registerEmailForFreeReport(@QueryMap Map<String, String> map);

    @GET("homescreenproductlist")
    Call<ResponseBody> getHomeScreenStaticList(@QueryMap Map<String, String> map);

    @POST("create-order-id")
    Call<ResponseBody> getOrderId(@QueryMap Map<String, String> headers);


    @POST("phonepe-order-generate")
    Call<ResponseBody> getPhonePeGatewayOrderId(@QueryMap Map<String, String> headers);

    @POST("recharge-pop-up-canel-data")
    Call<ResponseBody> cancelRechargeAfterChat(@QueryMap Map<String, String> appInstanceIDParams);

    @GET("verifyotpforfreereport")
    Call<ResponseBody> verifyEmailByOtp(@QueryMap Map<String, String> map);


    @POST("purchasereportfromwallet")
    Call<ResponseBody> purchaseReportFromWallet(@QueryMap Map<String, String> params);

    @POST("getuseruniquerefercode")
    Call<ResponseBody> getUserUniqueReferCode(@QueryMap Map<String, String> params);


    @POST("ai-pass-details")
    Call<ResponseBody> getAiPassDetails(@QueryMap Map<String, String> params);

    @POST("aiastrologer")
    Call<ResponseBody> getMemoryAPI(@QueryMap Map<String, String> headers);


    @POST("isaipasssubscriber")
    Call<ResponseBody> isAIPassSubscriber(@QueryMap Map<String, String> params );

    @GET
    Call<ResponseBody> getRemainingReportCount(@Url String url, @QueryMap Map<String, String> params);

    @GET("phonepeordergenerate")
    Call<ResponseBody> phonePayOrderCreation(@QueryMap Map<String, String> params );

    @GET("orderStatus")
    Call<ResponseBody> getUpiOrdersatusApi(@QueryMap Map<String, String> params );

    @GET
    Call<ResponseBody> getRequest(@Url String url, @QueryMap Map<String, String> params);

    @POST
    Call<ResponseBody> postRequest(@Url String url, @QueryMap Map<String, String> params);

    @POST("get-recharge-services-details")
    Call<ResponseBody> getReducedPriceRechargeServiceDetails(@QueryMap Map<String, String> headers);

    @GET("getaiplayrecordingurl")
    Call<ResponseBody> getAiCallRecordingUrl(@QueryMap Map<String, String> params);

    @POST("getcategory")
    Call<ResponseBody> getUserAIChatCategory(@QueryMap Map<String, String> headers);

    @GET("subscription-status")
    Call<ResponseBody> getSubscriptionList(@QueryMap Map<String, String> stringMap);

    @GET("cancel-subscription")
    Call<ResponseBody> cancelSubscription(@QueryMap Map<String, String> stringMap);

    @POST("horoscope")
    Call<ResponseBody> getAIHoroscopeResponse(@QueryMap Map<String, String> params);

    @POST("stripe-order-genenate")
    Call<ResponseBody> generateOrderForStripe(@QueryMap Map<String, String> headers);

    @POST("stripe-order-status")
    Call<ResponseBody> getStripeOrderStatus(@QueryMap Map<String, String> headers);

    @POST("extendconsultation")
    Call<ResponseBody> extendConsultation(@QueryMap Map<String, String> headers);

}
