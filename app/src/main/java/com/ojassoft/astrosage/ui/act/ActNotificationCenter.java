package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.libojassoft.android.dao.NotificationDBManager;
import com.libojassoft.android.models.NotificationModel;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.NotificationAdapterNew;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.ojassoft.astrosage.jinterface.NotificationCenterCallback;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_ARTICLE;
import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_PUSH;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NOTIFICATION_COUNT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;


public class ActNotificationCenter extends BaseInputActivity {

    public static final String SAVED_NOTIFICATION_ID = "saved_notification_id";
    static NotificationCenterCallback actAppModuleCtx;
    public View appbarAppModule;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private TextView noNotificationTV;
    private RecyclerView recyclerView;
    private NotificationDBManager dbManager;
    private List<NotificationModel> notificationModels;
    private NotificationAdapterNew notificationAdapter;
    private String playUrl = CGlobalVariables.PLAY_URL;
    private LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    private String question, aiAstroId, revertQCount, title;
    private boolean isAIAstrologerOnline = false;


    public ActNotificationCenter() {
        super(R.string.app_name);
    }

    public static void setNotificationCenterCallback(NotificationCenterCallback context) {
        ActNotificationCenter.actAppModuleCtx = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);
        initLayoutValues();
    }

    private void initLayoutValues() {
        CUtils.saveIntData(ActNotificationCenter.this, KEY_NOTIFICATION_COUNT, 0);
        TextView notificationHeadingTV = findViewById(R.id.font_auto_activity_notification_center_1);
        noNotificationTV = findViewById(R.id.noNotificationTV);
        recyclerView = findViewById(R.id.notification_list);
        dbManager = new NotificationDBManager(ActNotificationCenter.this);
        notificationModels = dbManager.getNotificationList(0, 10);
        if (notificationModels == null) {
            notificationModels = new ArrayList<>();
        }
        if (notificationModels.isEmpty()) {
            noNotificationTV.setVisibility(View.VISIBLE);
        } else {
            noNotificationTV.setVisibility(View.GONE);
        }
        notificationAdapter = new NotificationAdapterNew(ActNotificationCenter.this, notificationModels);
        mLayoutManager = new LinearLayoutManager(ActNotificationCenter.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notificationAdapter);
        FontUtils.changeFont(this, notificationHeadingTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        ImageView backBtn = findViewById(R.id.back_arrow);
        backBtn.setOnClickListener(v -> finish());

        notificationAdapter.setOnClickListner(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (notificationModels != null && notificationModels.size() > position) {
                    NotificationModel notificationModel = notificationModels.get(position);
                    redirectToActivity(notificationModel);
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            notificationModels.addAll(dbManager.getNotificationList(notificationModels.size(), 10));
                            notificationAdapter.notifyDataSetChanged();
                            //Toast.makeText(ActNotificationCenter.this, "loading", Toast.LENGTH_LONG).show();
                            loading = true;
                        }
                    }
                }
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void preparedListItem() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ActNotificationCenter.this, "preparedListItem", Toast.LENGTH_SHORT).show();
                notificationAdapter.notifyDataSetChanged();

            }
        }, 200);
    }

    /**
     * This method is used to redirect notification to particular page
     *
     * @param notificationModel
     */

    private void redirectToActivity(NotificationModel notificationModel) {
        if (notificationModel == null) {
            return;
        }
        CUtils.googleAnalyticSendWitPlayServie(ActNotificationCenter.this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_READ_NOTIFICATION, null);

        //Log.e("SAN ", "NOtification clicked link => " + notificationModel.getLink() );

        if ( (com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls+"/rechargenow").equalsIgnoreCase(notificationModel.getLink()) ) {
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.NEW_USER_RECHARGE_NOTIFICATION, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_NOTIFICATION_CLICKED_EVENT,"");
        }


        Intent resultIntent = null;

        String mss = notificationModel.getMessage();
        String tit = notificationModel.getTitle();
        String link = notificationModel.getLink();
        String ntID = notificationModel.getNtId();
        String extra = notificationModel.getExtra();
        String imgurl = notificationModel.getImgUrl();

        int notificationType = notificationModel.getNotificationType();
        // push notification deep linking
        if (notificationType == NOTIFICATION_PUSH) {
            try {
                if (link != null && link.trim().equalsIgnoreCase("CHAT_WITH_ASTROLOGER_KUNDLI".trim())) {
                    //Log.e("!!!!...Extra Data..!!!!",""+extra);
                    JSONObject object = new JSONObject(extra);
                    String msgUserType = object.getString("PropertyUserType");
                    String msgColor = object.getString("PropertyColorOfMsg");
                    String msgRatingOption = object.getString("PropertyRatingToShow");
                    String msgShareOption = object.getString("PropertyShareLinkToShow");
                    String msgNotificationOption = object.getString("PropertyShowOnNotificationBar");
                    String msgOrderId = object.getString("OrderId");
                    String msgAstrologerName = object.getString("AstrologerName");
                    String msgAstrologerImagePath = object.getString("AstrologerImagePath");
                    String msgChatId = object.getString("ChatId");

                    MessageDecode messageDecode = new MessageDecode();
                    messageDecode.setMessageText(mss);
                    messageDecode.setUserType(msgUserType);
                    messageDecode.setDateTimeShow(CUtils.getCurrentDateTime());
                    messageDecode.setAstrologerName(msgAstrologerName);
                    messageDecode.setOrderId(msgOrderId);
                    messageDecode.setAstrologerImagePath(msgAstrologerImagePath);
                    messageDecode.setColorOfMessage(msgColor);
                    messageDecode.setMessageTextTitle(tit);
                    messageDecode.setChatId(msgChatId);


                    if (msgRatingOption.equalsIgnoreCase("true")) {
                        messageDecode.setRateShow("true");
                    } else {
                        messageDecode.setRateShow("false");
                    }
                    if (msgShareOption.equalsIgnoreCase("true")) {
                        messageDecode.setShareLinkShow("true");
                    } else {
                        messageDecode.setShareLinkShow("false");
                    }

                    if (msgNotificationOption.equalsIgnoreCase("true")) {
                        //sendCustomPushNotiAstroChat(tit, mss, link);
                        resultIntent = new Intent(this, ActNotificationLanding.class);
                        resultIntent.putExtra("isNotification", true);
                        startActivity(resultIntent);
                    }

                } else if (tit.equals(getResources().getString(R.string.call_completed)) ||
                        tit.equals(getResources().getString(R.string.chat_completed))) {
                    Uri linkData = Uri.parse(link);
                    if (linkData != null) {
                        String articleIdLastPathSegment = linkData.getLastPathSegment();
                        Intent intent1 = new Intent(ActNotificationCenter.this, AstrologerDescriptionActivity.class);
                        intent1.putExtra("phoneNumber", com.ojassoft.astrosage.varta.utils.CUtils.getUserID(ActNotificationCenter.this));
                        intent1.putExtra("urlText", articleIdLastPathSegment);
                        intent1.putExtra("msg", notificationModel.getMessage());
                        intent1.putExtra("title", tit);
                        intent1.putExtra("from_notification_center", true);
                        startActivity(intent1);
                        finish();
                    }
                } else if (tit.equals(getResources().getString(R.string.call_failed_user)) ||
                        tit.equals(getResources().getString(R.string.chat_failed_user)) ||
                        tit.equals(getResources().getString(R.string.chat_not_completed))) {
                    Uri linkData = Uri.parse(link);
                    String articleIdLastPathSegment = linkData.getLastPathSegment();
                    if (articleIdLastPathSegment == null) articleIdLastPathSegment = "";
                    String articleId = linkData.toString();
                    if (articleId.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls) || articleId.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_url)) {
                        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActNotificationCenter.this);
                        if (articleIdLastPathSegment.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.consultationHistory) && isLogin) {
                            openWalletScreen();
                        } else {
                            goToDashBoard();
                        }

                    } else {
                        com.ojassoft.astrosage.varta.utils.CUtils.openWebBrowser(this, linkData);
                    }
                } else if (link != null && link.trim().equalsIgnoreCase("CHAT_WITH_ASTROLOGER_FREE_QUESTION".trim())) {
                    //Log.e("!!!!...Extra Data..!!!!",""+extra);
                    JSONObject object = new JSONObject(extra);
                    String msgUserType = object.getString("PropertyUserType");
                    String msgColor = object.getString("PropertyColorOfMsg");
                    String msgRatingOption = object.getString("PropertyRatingToShow");
                    String msgShareOption = object.getString("PropertyShareLinkToShow");
                    String msgNotificationOption = object.getString("PropertyShowOnNotificationBar");
                    String msgOrderId = object.getString("OrderId");
                    String msgAstrologerName = object.getString("AstrologerName");
                    String msgAstrologerImagePath = object.getString("AstrologerImagePath");
                    String msgChatId = object.getString("ChatId");

                    MessageDecode messageDecode = new MessageDecode();
                    messageDecode.setMessageText(mss);
                    messageDecode.setUserType(msgUserType);
                    messageDecode.setDateTimeShow(CUtils.getCurrentDateTime());
                    messageDecode.setAstrologerName(msgAstrologerName);
                    messageDecode.setOrderId(msgOrderId);
                    messageDecode.setAstrologerImagePath(msgAstrologerImagePath);
                    messageDecode.setColorOfMessage(msgColor);
                    messageDecode.setMessageTextTitle(tit);
                    messageDecode.setChatId(msgChatId);

                    if (msgRatingOption.equalsIgnoreCase("true")) {
                        messageDecode.setRateShow("true");
                    } else {
                        messageDecode.setRateShow("false");
                    }
                    if (msgShareOption.equalsIgnoreCase("true")) {
                        messageDecode.setShareLinkShow("true");
                    } else {
                        messageDecode.setShareLinkShow("false");
                    }

                    if (msgNotificationOption.equalsIgnoreCase("true")) {
                        //sendCustomPushNotiAstroChat(tit, mss, link);
                        resultIntent = new Intent(this, ActNotificationLanding.class);
                        resultIntent.putExtra("isNotification", true);
                        startActivity(resultIntent);
                    }

                }else if(link != null && link.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_LIVE) || link.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LINK_HAS_LIVE_ASTRO)){
                    Intent allLiveIntent = new Intent(this, DashBoardActivity.class);
                    allLiveIntent.setAction(Intent.ACTION_VIEW);
                    allLiveIntent.setData(Uri.parse(link));
                    //Log.d("testXyz","Called");
                    //Intent allLiveIntent = new Intent(ActNotificationCenter.this, AllLiveAstrologerActivity.class);
                    //allLiveIntent.putExtra("fromLiveIcon", true);
                    startActivity(allLiveIntent);

                } else {
                    if (!ntID.trim().isEmpty()) {
                        // Commented by ankit as discussed with Abhishek on 28-8-2019
                        /*if (ntID.equalsIgnoreCase(CUtils.getSavedNotificationID(this, SAVED_NOTIFICATION_ID))) {
                            return;
                        } else {*/
                        if (link.contains(playUrl)) {
                            resultIntent = new Intent(Intent.ACTION_VIEW);
                            resultIntent.setData(Uri.parse(link));
                            startActivity(resultIntent);
                        } else {
                            resultIntent = new Intent();
                            resultIntent.setAction(Intent.ACTION_VIEW);
                            resultIntent.setData(Uri.parse(link));
                            if (actAppModuleCtx instanceof ActAppModule) {
                                actAppModuleCtx.redirectToLink(resultIntent);
                            } else {
                                CUtils.getUrlLink(link, ActNotificationCenter.this, LANGUAGE_CODE, 0);
                            }
                        }
                        CUtils.setSavedNotificationID(this, ntID, SAVED_NOTIFICATION_ID);
                        //}
                    } else {
                        if (link.contains(playUrl)) {
                            resultIntent = new Intent(Intent.ACTION_VIEW);
                            resultIntent.setData(Uri.parse(link));
                            startActivity(resultIntent);
                        } else {
                            resultIntent = new Intent();
                            resultIntent.setAction(Intent.ACTION_VIEW);
                            resultIntent.setData(Uri.parse(link));
                            //actAppModuleCtx.redirectToLink(resultIntent);
                            if (actAppModuleCtx instanceof ActAppModule) {
                                actAppModuleCtx.redirectToLink(resultIntent);
                            } else {
                                CUtils.getUrlLink(link, ActNotificationCenter.this, LANGUAGE_CODE, 0);
                            }
                        }
                    }

                    if (link.contains(CGlobalVariables.chat_with_ai_astrologers)) {
                        AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n ActNotificationCenter extra : "+extra;
                        JSONObject object = new JSONObject(extra);
                        question = object.optString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION_NEW);
                        revertQCount = object.optString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REVERT_QUESTION_COUNT);
                        aiAstroId = object.optString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID);
                        title = object.optString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_NOTIFICATION_TITLE);
                        isAIAstrologerOnline = object.optBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE);
                        //if (!TextUtils.isEmpty(question)) {
                        checkAIAstrologerInLocalList(link);
                        //}
                    }else if (link.contains(CGlobalVariables.chat_with_kundli_ai)) {
                       Intent  intent = new Intent(this, ActAppModule.class);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(link));
                            try {
                                if (extra != null && !extra.isEmpty()) {
                                    JSONObject obj = new JSONObject(extra);
                                  //  Log.e("testNotification", "notification center= json: " + obj);
                                    String categoryId = obj.getString("category");
                                   // Log.e("testNotification", "notification center= categoryId: " + categoryId);
                                    intent.putExtra(CGlobalVariables.KEY_FOR_CATEGORY_ID, categoryId);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
//
                            }
                    }else if(link.contains(CGlobalVariables.talk_to_astrologers)) {
                        finish();
                        if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActNotificationCenter.this)) {
                            try{
                                FragmentActivity activity = (FragmentActivity)(ActNotificationCenter.this);
                                FragmentManager fm = activity.getSupportFragmentManager();
                                PopUpLogin popUpLogin = new PopUpLogin
                                        ("notification_center",
                                                "NOTIFICATIONCENTER");
                                popUpLogin.show(fm, "PopUpFreeCall");
                            }catch (Exception e){
                                //
                            }

                        } else {
                            String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(ActNotificationCenter.this);
                            if (!TextUtils.isEmpty(offerType) && offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                                //eventsFreeCallChat();
                                if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(ActNotificationCenter.this).equals("91")) {
                                    com.ojassoft.astrosage.varta.utils.CUtils.popUpLoginFreeChatClicked = true;
                                } else {
                                    com.ojassoft.astrosage.varta.utils.CUtils.popUpLoginFreeCallClicked = true;
                                }
                            }
                            //CUtils.popUpLoginFreeCallClicked = true;

                            Intent i = new Intent(ActNotificationCenter.this, DashBoardActivity.class);
                            i.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN, "AskQuestionFragment");
                            startActivity(i);
                        }

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } //artical notification deep linking
        else if (notificationType == NOTIFICATION_ARTICLE) {
            Intent intent = new Intent(ActNotificationCenter.this, ActShowOjasSoftArticlesWithTabs.class);
            intent.putExtra("BLOG_LINK_TO_SHOW", link);
            intent.putExtra("TITLE_TO_SHOW", tit);
            startActivity(intent);
        }
    }

    private void goToDashBoard() {
        CUtils.openVartaTabActivity(ActNotificationCenter.this, FILTER_TYPE_CHAT);
        finish();
    }

    public void openWalletScreen() {
        Intent intent = new Intent(ActNotificationCenter.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "ActNotificationCenter");
        startActivity(intent);
    }

    private void checkAIAstrologerInLocalList(String link) {
        try {
            AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n ActNotificationCenter aiAstroId : "+aiAstroId;

            Intent resultIntent = new Intent(this, ActAppModule.class);
            resultIntent.setAction(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));
            resultIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION, question);
            resultIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REVERT_QUESTION_COUNT, revertQCount);
            resultIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID, aiAstroId);
            resultIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_NOTIFICATION_TITLE, title);
            resultIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE, isAIAstrologerOnline);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(resultIntent);
        } catch (Exception e) {
            Log.d("aiListService", "checkAIAstrologerInLocalList e: " + e);
        }
    }

}
