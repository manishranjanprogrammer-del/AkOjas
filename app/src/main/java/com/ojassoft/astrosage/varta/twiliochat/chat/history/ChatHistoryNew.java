package com.ojassoft.astrosage.varta.twiliochat.chat.history;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dialog.FeedbackDialog;
import com.ojassoft.astrosage.varta.dialog.JoinChatDialog;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.model.ChatHistoryBean;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.MessageAdapter;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatHistoryNew extends BaseActivity implements VolleyResponse {

    RequestQueue queue;
    ImageView ivback;
    TextView title_text, endChatButton, infoTextView;
    CircularNetworkImageView astrologer_profile_pic;
    String channelId = "", astrologerEmailID = "", astrologerProfileImage = "", astrologerName = "", astrologerPhone = "", urlText = "";
    CustomProgressDialog pd;
    ConstraintLayout mainlayout;
    RecyclerView recyclerview;
    MessageAdapter messageAdapter;
    ArrayList<MessageHistory> messageHistoryArrayList = new ArrayList<>();
    MessageChatHistoryAdapter messageChatHistoryAdapter;
    public int pageNo = 1;
    boolean isPagingCalled = false;
    static String totalPages = "1";
    boolean isShowProgressBar = true;
    private  JoinChatDialog joinChatDialog;
    AppCompatButton btnChatAgain, btnCallAgain;

    ChatHistoryBean chatHistoryBean;
    CallHistoryBean callHistoryBean;

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                getChatHistory(channelId, pageNo, isShowProgressBar);
            } else {
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivback = findViewById(R.id.ivback);
        title_text = findViewById(R.id.title_text);
        infoTextView = findViewById(R.id.infoTextView);
        FontUtils.changeFont(this, infoTextView, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        astrologer_profile_pic = findViewById(R.id.astrologer_profile_pic);
        endChatButton = findViewById(R.id.end_chat_button);
        endChatButton.setVisibility(View.GONE);
        mainlayout = findViewById(R.id.mainlayout);
        btnCallAgain = findViewById(R.id.btnCallAgain);
        btnChatAgain = findViewById(R.id.btnChatAgain);
        queue = VolleySingleton.getInstance(ChatHistoryNew.this).getRequestQueue();

        if (getIntent().getExtras() != null) {
            channelId = getIntent().getExtras().getString(CGlobalVariables.CHANNEL_ID);
            astrologerName = getIntent().getExtras().getString(CGlobalVariables.ASTROLOGER_NAME);
            astrologerProfileImage = getIntent().getExtras().getString(CGlobalVariables.ASTROLOGER_PROFILE_PIC);
            astrologerPhone = getIntent().getExtras().getString("astrologerphone");
            urlText = getIntent().getExtras().getString(CGlobalVariables.URL_TEXT);
            try {
                chatHistoryBean = (ChatHistoryBean) getIntent().getSerializableExtra(CGlobalVariables.CHAT_HISTORY_BEAN);
            } catch (Exception e) {
                //
            }
            try {
                callHistoryBean = (CallHistoryBean) getIntent().getSerializableExtra(CGlobalVariables.CALL_HISTORY_BEAN);
            } catch (Exception e) {
                //
            }
        }
        //Log.e("twiliochat", "channelid oncreate"+ channelId);
        //channelId = "CH58dbcf535f0b4a3c826f38023b314e47";
        if (channelId.startsWith("FCHAI")) {
            btnCallAgain.setVisibility(View.GONE);
        }
        String astrologerProfileUrl = "";
        if (astrologerProfileImage != null && astrologerProfileImage.length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileImage;
            // astrologer_profile_pic.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(ChatHistoryNew.this).getImageLoader());
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(astrologer_profile_pic);
        }
        title_text.setText(astrologerName);
        //channelId = "CH58dbcf535f0b4a3c826f38023b314e47";
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(ChatHistoryNew.this));
        messageChatHistoryAdapter = new MessageChatHistoryAdapter(ChatHistoryNew.this, messageHistoryArrayList);
        recyclerview.setAdapter(messageChatHistoryAdapter);
        getChatHistory(channelId, pageNo, isShowProgressBar);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
                if (!recyclerView.canScrollVertically(1)) {
                    if (isPagingCalled) {
                        isPagingCalled = false;
                        if (CUtils.isConnectedWithInternet(ChatHistoryNew.this)) {
                            pageNo++;
                            isShowProgressBar = false;
                            getChatHistory(channelId, pageNo, isShowProgressBar);
                            //Log.d("LoadMore ", " START_PAGE: " + pageNo);
                        } else {
                            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), ChatHistoryNew.this);
                        }
                    }
                }
            }
        });

        btnChatAgain.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents("chat_again_chat_history_detail", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if(chatHistoryBean != null){
                processChatAgainByChatBean();
            } else if(callHistoryBean != null){
                processChatAgainByCallBean();
            } else {
                openAstrologerDetail();
            }
        });

        btnCallAgain.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents("call_again_chat_history_detail", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            openAstrologerDetail();
        });

        CUtils.updateChatBackgroundBasedOnTheme(this);
    }

    private void openAstrologerDetail() {
        Bundle bundle = new Bundle();
        try {
            bundle.putString("phoneNumber", astrologerPhone);
            bundle.putString("urlText", urlText);
            Intent intent = new Intent(ChatHistoryNew.this, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    private void getChatHistory(String channelID, int pageNoo, boolean isShowProgressBarr) {
        if (CUtils.isConnectedWithInternet(ChatHistoryNew.this)) {
            if (isShowProgressBarr) {
                showProgressBar();
            }

            /*String Url = "";
            if(channelID != null){
                if(channelID.startsWith("FCH")){
                    Url = CGlobalVariables.CHAT_HISTORY_URL_V2;
                }else{
                    Url = CGlobalVariables.CHAT_HISTORY_URL;
                }
            }*/
            //Log.e("SAN CHAT Hist url " ,  Url);
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, Url,
                    ChatHistoryNew.this, false, getChatParams(channelID, pageNoo), 1).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);*/


            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getChatHistoryV2(getChatParams(channelID, pageNoo));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        parseChatHistoryData(myResponse);
                    } catch (Exception e) {
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error_msg), ChatHistoryNew.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error_msg), ChatHistoryNew.this);
                }
            });


        } else {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), ChatHistoryNew.this);
        }
    }

    public Map<String, String> getChatParams(String channelID, int pageNumber) {
        String key = CUtils.getApplicationSignatureHashCode(ChatHistoryNew.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.APP_KEY, key);
        params.put(CGlobalVariables.CHAT_CHANNEL_ID, channelID);
        if (channelID.startsWith("FCH")) {
            params.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(ChatHistoryNew.this));
            params.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(ChatHistoryNew.this));
        } else {
            params.put(CGlobalVariables.CHAT_PAGE_NO, String.valueOf(pageNumber));
        }
        params.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("SAN CHAT params", params.toString());
        return CUtils.setRequiredParams(params);
    }


    /**
     * Snack Bar to Show Error Messages to user
     *
     * @param view
     * @param text
     */
    /*public void showSnackbar(View view, String text) {
        try {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.dark_purple));
            TextView tv = snackbar.getView().findViewById(R.id.snackbar_text); //snackbar_text
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(16);
            FontUtils.changeFont(ChatHistoryNew.this, tv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            snackbar.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * show Progress Bar
     */
    public void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(ChatHistoryNew.this);
        }
        pd.show();
        pd.setCancelable(false);
    }

    /**
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, int method) {
        /*hideProgressBar();
        if(response != null && response.length()>0){
            try {
                String sResponse = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                parseChatHistoryData(sResponse);
            } catch (Exception e) {

            }
        }else{
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error_msg), ChatHistoryNew.this);
        }*/
    }

    private void parseChatHistoryData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String statuss = jsonObject.getString("status");
            if (statuss.equalsIgnoreCase("1")) {
                try {
                    if (jsonObject.has("totalpages")) {
                        totalPages = jsonObject.getString("totalpages");
                    }
                    if (totalPages != null && totalPages.length() > 0) {
                        int pageCount = Integer.parseInt(totalPages);
                        isPagingCalled = pageNo < pageCount;
                    } else {
                        isPagingCalled = false;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    isPagingCalled = false;
                }
                JSONArray msgJsonArray = jsonObject.getJSONArray("messages");
                if (msgJsonArray != null && msgJsonArray.length() > 0) {
                    if (messageHistoryArrayList == null) {
                        messageHistoryArrayList = new ArrayList<>();
                    }
                    for (int i = 0; i < msgJsonArray.length(); i++) {
                        JSONObject msgObject = msgJsonArray.getJSONObject(i);

                        MessageHistory messageHistory = new MessageHistory();
                        messageHistory.setFrom(msgObject.getString("from"));
                        messageHistory.setMsgBody(msgObject.getString("body"));
                        messageHistory.setMsgDate(msgObject.getString("time"));
                        messageHistoryArrayList.add(messageHistory);
                        // Log.e("SAN CHN", msgObject.getString("body") );
                    }

                    //Log.e("LoadMore ListSize ", "" + messageHistoryArrayList.size());
                    if (messageHistoryArrayList != null && messageHistoryArrayList.size() > 0) {
                        infoTextView.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.VISIBLE);
                        if (messageChatHistoryAdapter == null) {
                            messageChatHistoryAdapter = new MessageChatHistoryAdapter(ChatHistoryNew.this, messageHistoryArrayList);
                            recyclerview.setAdapter(messageChatHistoryAdapter);
                        } else {
                            messageChatHistoryAdapter.UpdateMessageChatHistoryAdapter(messageHistoryArrayList);
                        }
                    }
                }
                if (messageHistoryArrayList == null || messageHistoryArrayList.isEmpty()) {
                    showErrorMessage(getResources().getString(R.string.no_chat_found));
                }
            } else if (statuss.equalsIgnoreCase("2")) {
                showErrorMessage(getResources().getString(R.string.consultation_history_note));
            } else if (statuss.equals("100")) {
                LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                startBackgroundLoginService();
            } else {
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error_msg), ChatHistoryNew.this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMessage(getResources().getString(R.string.no_chat_found));

        }
    }

    @Override
    public void onError(VolleyError error) {
        /*CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error_msg), ChatHistoryNew.this);*/

    }

    private void showErrorMessage(String msg) {
        infoTextView.setVisibility(View.VISIBLE);
        infoTextView.setText(msg);
        recyclerview.setVisibility(View.GONE);
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(this)) {
                Intent intent = new Intent(this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {/**/}
    }

    private void processChatAgainByChatBean() {
        try {
            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
            astrologerDetailBean.setAiAstrologerId(chatHistoryBean.getAiAstroId());
            if (CUtils.isAiAstrologer(astrologerDetailBean)) {
                astrologerDetailBean.setName(chatHistoryBean.getAstrologerName());
                astrologerDetailBean.setImageFile(chatHistoryBean.getAstrologerImageFile());
                astrologerDetailBean.setImageFileLarge(chatHistoryBean.getAstroImageFileLarge());
                astrologerDetailBean.setAstroWalletId(chatHistoryBean.getAstroWalletId());
                astrologerDetailBean.setUrlText(chatHistoryBean.getUrlText());
                astrologerDetailBean.setDesignation(chatHistoryBean.getAstroExpertise());
                String className = CUtils.getActivityName(ChatHistoryNew.this);
                if (TextUtils.isEmpty(className)) {
                    className = "";
                }
                astrologerDetailBean.setCallSource(className);
                astrologerDetailBean.setAiAstrologerId(chatHistoryBean.getAiAstroId());
                astrologerDetailBean.setAstrologerId(chatHistoryBean.getAstroWalletId());
                String offerType = CUtils.getCallChatOfferType(this);
                if(chatHistoryBean.isFreeForChat() && !TextUtils.isEmpty(offerType)) {
                    astrologerDetailBean.setFreeForChat(true);
                    astrologerDetailBean.setUseIntroOffer(true);
                }
                if (com.ojassoft.astrosage.varta.utils.CUtils.isChatNotInitiated() && !TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId())) {
                    ChatUtils.getInstance(ChatHistoryNew.this).initAIChat(astrologerDetailBean);
                } else {
                    joinChatDialog = new JoinChatDialog();
                    joinChatDialog.show(getSupportFragmentManager(), "ChatHistoryNewJoinChat");
                    //Toast.makeText(this, getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
                }
                //finish();
            } else{
                openAstrologerDetail();
            }
        } catch (Exception e) {
            openAstrologerDetail();
        }
    }

    private void processChatAgainByCallBean() {
        try {
            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
            astrologerDetailBean.setAiAstrologerId(callHistoryBean.getAiAstroId());
            if (CUtils.isAiAstrologer(astrologerDetailBean)) {
                astrologerDetailBean.setName(callHistoryBean.getAstrologerName());
                astrologerDetailBean.setImageFile(callHistoryBean.getAstrologerImageFile());
                astrologerDetailBean.setImageFileLarge(callHistoryBean.getAstroImageFileLarge());
                astrologerDetailBean.setAstroWalletId(callHistoryBean.getAstroWalletId());
                astrologerDetailBean.setUrlText(callHistoryBean.getUrlText());
                astrologerDetailBean.setDesignation(callHistoryBean.getAstroExpertise());
                String className = CUtils.getActivityName(ChatHistoryNew.this);
                if (TextUtils.isEmpty(className)) {
                    className = "";
                }
                astrologerDetailBean.setCallSource(className);
                astrologerDetailBean.setAiAstrologerId(callHistoryBean.getAiAstroId());
                astrologerDetailBean.setAstrologerId(callHistoryBean.getAstroWalletId());
                String offerType = CUtils.getCallChatOfferType(this);
                if(callHistoryBean.isFreeForChat() && !TextUtils.isEmpty(offerType)) {
                    astrologerDetailBean.setFreeForChat(true);
                    astrologerDetailBean.setUseIntroOffer(true);
                }
                if (com.ojassoft.astrosage.varta.utils.CUtils.isChatNotInitiated() && !TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId())) {
                    ChatUtils.getInstance(ChatHistoryNew.this).initAIChat(astrologerDetailBean);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
                }
                //finish();
            } else{
                openAstrologerDetail();
            }
        } catch (Exception e) {
            openAstrologerDetail();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        CUtils.updateChatBackgroundBasedOnTheme(this);
    }
}
