package com.ojassoft.astrosage.varta.ui.activity;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROLOGER_DESCRIPTION_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROLOGER_DESCRIPTION_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROLOGER_DESCRIPTION_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROLOGER_DESCRIPTION_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_SEND_GIFT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_SPEED;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.ApiRepository;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.fragments.FullProfileFragment;

import com.ojassoft.astrosage.ui.fragments.ShareAstroReviewFragment;
import com.ojassoft.astrosage.utils.OnlineAstrologerAdapter;
import com.ojassoft.astrosage.varta.adapters.AstrologerBioAdapter;
import com.ojassoft.astrosage.varta.adapters.AstrologerUserReciewAdapter;
import com.ojassoft.astrosage.varta.adapters.LiveGiftAdapter;
import com.ojassoft.astrosage.varta.customwidgets.TopCropImageView;
import com.ojassoft.astrosage.varta.dialog.AstroBusyAlertDialog;
import com.ojassoft.astrosage.varta.dialog.CallInitiatedDialog;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.dialog.FeedbackDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteMinimizeDialog;
import com.ojassoft.astrosage.varta.dialog.InSufficientBalanceDialog;
import com.ojassoft.astrosage.varta.dialog.NotificationAlertDialog;
import com.ojassoft.astrosage.varta.dialog.QuickRechargeBottomSheet;
import com.ojassoft.astrosage.varta.dialog.RatingAndDakshinaDialog;
import com.ojassoft.astrosage.varta.dialog.RechargeSuggestionBottomSheet;
import com.ojassoft.astrosage.varta.model.AstrologerBioModel;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.GiftModel;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.model.UserReviewBean;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.receiver.AstroLiveDataManager;
import com.ojassoft.astrosage.varta.receiver.OngoingCallChatManager;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AgoraCallOngoingService;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.service.CallStatusCheckService;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.utils.AppDataSingleton;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AstrologerDescriptionActivity extends BaseActivity implements View.OnClickListener, VolleyResponse, GlobalRetrofitResponse {

    private static final String TAG = "AstrologerDescriptionActivity";
    RelativeLayout vartaIconLayout, callNowLayout;
    LinearLayout backTitleLayout, walletLayout;
    ImageView ivBack, onlineOfflineImg;
    ImageView verifiedImg;
    CircularNetworkImageView profileImg, ongoingChatAstroProfileImage;
    private static int END_CHAT_VALUE = 105;
    TextView tvTitle, walletPriceTxt, vedicAstrologerTxt, languageTxt,
            experienceTxt, astrologerNameTxt, totalRatingTxt, ratingTxt, consultaionValueTxt,
            aboutAstrologerHeadingTxt, expertiseTxt, astrologerDescription,
             callNowBtnTxt, chatNowBtnTxt, allReviewTxt,
            expertiseValueTxt, consultaionTxt, actualConsultaionValueTxt, followCount,
            tvSeeAllReviews, tvSeeSimilarAstrologers, tvAstroDescWaitTime,share_your_experience_notes;
    String followCountVal = "0", followStatus = "";
    LinearLayout llSeeAllAstrologers;
    FrameLayout giveFeedbackBtn;
    ImageView rating_tag_img;
    TextView rating_tag_open, rating_tag_close;

    RecyclerView recyclerview, rvSimilarAstrologers;
    public RelativeLayout containerLayout, callNowBtn, chatNowBtn;
    static String readMore = "";
    static String readLess = "";
    Context context;
    AstrologerUserReciewAdapter astrologerUserReciewAdapter;
    CustomProgressDialog pd;
    RequestQueue queue;
    AppCompatRatingBar ratingStar;
    LinearLayout expertiseLayout, feedbackLayout, llBottomLayout;
    RelativeLayout mainlayout;
    int ASTROLOGER_DETAIL_METHOD = 44;
    int ASTROLOGER_FEEDBACK_DATA_METHOD = 45;
    private int SEND_GIFT_REQ = 100;
    int REVIEW_ACTION_METHOD = 46;
    static AstrologerDetailBean astrologerDetailBeanData = null;
    private BroadcastReceiver receiver, call_receiver;
    String callStatus = "";
    private ImageView logoutBtn, ivChatNow, ivCallNow;

    String consultationType = "";
    String phoneNumber;
    String urlText;
    boolean fromDashboard, fromLiveActivity, isAIAstrologer;
    RecyclerView bio_recyclerview;
    AstrologerBioAdapter astrologerBioAdapter;
    ArrayList<AstrologerBioModel> astrologerBioModelArrayList;
    TextView astrologer_description_dummy, read_more_txt;
    LinearLayout astrologer_description_dummy_layout;
    String message = "", title = "";
    private boolean isShowRatingDialogAfterCall;
    FreeMinuteDialog freeMinutedialog;
    private FreeMinuteMinimizeDialog freeMinuteMinimizeDialog;
    boolean from_notification_center = false;
    // ChatCallbackListener mChatCallbackListener;
    NetworkImageView offerImg;
    Button loadMoreBtn, joinOngoingChat;
    TextView followAstrologer, videoCallText;
    ArrayList<UserReviewBean> userFeedbackList = new ArrayList<>();
    RelativeLayout ratingLayout, rlLive;
    boolean isShowFeedbackDialog = false;
    private final int ASTRO_STATUS_UPDATE = 102;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String astroId = "";
    private ImageView logoGif, loadmore_gif, cross_btn, videoCallIcon, videoCallArrow;
    private TextView new_user, ruppee_sign, ongoingChatAstroName, ongoingChatRemTime;
    private NestedScrollView scrollview_layout;
    FeedbackDialog feedbackDialog;
    private RatingAndDakshinaDialog ratingAndDakshinaDialog;
    private static final int FOLLOW_ASTROLOGER_REQ = 6;
    private LinearLayout mFollowLayout, mUnFollowLayout;
    boolean isdisable;
    LinearLayout navView;
    FloatingActionButton fabAstrologerDescription;
    private ArrayList<AstrologerDetailBean> astrologerDetailBeanArrayList;
    private OnlineAstrologerAdapter onlineAstrologerAdapter;
    private ArrayList<String> astrologerLanguageList;
    public ImageView ivAstroDescShare, online_offline_img1, ivAstroDescGift;
    private ArrayList<GiftModel> giftModelArrayList;
    private Dialog giftDialog;
    private Button send;
    private GiftModel giftModel;
    LinearLayout ll_video_busy;
    private boolean isFreeConsultation;
    private int useIntroOffer;
    private int isFreeForChat;
    private int isFreeForCall;
    private int offerFromNotification = 0;
    private boolean isReviewBlocked,isFromNotification,isFollowDialogShown,isFreechat;
    private boolean astrologerIsBusy;
    public boolean isStartedFromNotificationDialog = false;
    final int ADD_TO_QUEUE_METHOD = 40;
    boolean onResumeFlag = false;
    private View ongoingChatInfoLayout, chatInitiateInfoLayout;
    private FrameLayout videoCallView;
    private static final int PERMISSION_REQ_ID = 27;
    private static final int VOICE_CALL_PERMISSION_REQ_ID = 28;
    private int WALLET_PRICE_API = 101;
    private boolean isStartedForWallet = false;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private static final String[] REQUESTED_PERMISSIONS_VOICE_CALL = {Manifest.permission.RECORD_AUDIO};
    private ProgressDialog installLiveProgressDialog;
    private android.app.AlertDialog alert;
    private boolean isFeedbackLoading = false;
    public boolean followAstroResp100 = false;
    private ApiRepository apiRepository;
    public void isUserLogin(Fragment fragment, String whichScreen) {
        boolean isLogin = false;
        isLogin = CUtils.getUserLoginStatus(context);
        if (!isLogin) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(context, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, whichScreen);
            startActivity(intent);
        } else {
            if (whichScreen.equals(CGlobalVariables.RECHARGE_SCRREN)) {
                openWalletScreen("astro_desc");
            } else {
                //changeToolbar(CGlobalVariables.PROFILE_FRAGMENT);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_PROFILE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(context, MyAccountActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.astrologer_description_layout);
        //manager = SplitInstallManagerFactory.create(this);
        context = AstrologerDescriptionActivity.this;
        queue = VolleySingleton.getInstance(context).getRequestQueue();
        //mChatCallbackListener = (ChatCallbackListener) DashBoardActivity.getMainActivity();
        try {
            initView();
            getIntentData(getIntent());
        } catch (Exception e){
            //
        }

    }

    private boolean flag;
    private final Observer<String> callChatObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            try {
                if (s.contains(CGlobalVariables.ASTRO_NO_ANSWER) && flag) {
                    flag = false;
                    chatInitiateInfoLayout.setVisibility(View.GONE);
                    String callSource = "";
                    AstrologerDetailBean astroBean = AstrosageKundliApplication.selectedAstrologerDetailBean;
                    if(astroBean != null){
                        callSource = astroBean.getCallSource();
                        if(callSource == null) callSource = "";
                    }
                    String isFreeHumanRandomChat = s.split("@")[1];
                    if(isFreeHumanRandomChat.equals("true")) {
                        com.ojassoft.astrosage.varta.utils.CUtils.startRandomAIChatAfterAstroNoAnswer(AstrologerDescriptionActivity.this);
                    } else if(callSource.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.HUMAN_CONTINUE_CHAT_DIALOG)){
                        //connect chat to AI astro
                        if(AstrosageKundliApplication.lastChatAIAstrologerDetailBean!=null ) {
                            AstrosageKundliApplication.lastChatAIAstrologerDetailBean.setCallSource(CGlobalVariables.HUMAN_CONTINUE_CHAT_FALLBACK_TO_SAME_AI);
                            ChatUtils.getInstance(AstrologerDescriptionActivity.this).initAIChat(AstrosageKundliApplication.lastChatAIAstrologerDetailBean);
                        }
                    } else {
                        AstroBusyAlertDialog astroBusyAlertDialog = AstroBusyAlertDialog.newInstance(AstrosageKundliApplication.astrologerDetailBeanForBusyDialog, s);
                        astroBusyAlertDialog.show(AstrologerDescriptionActivity.this.getSupportFragmentManager(), "astroBusyAlertDialog");

                        //    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.astrologer_not_answer), AstrologerDescriptionActivity.this);
                    }
                } else if (s.equals("-1") && flag) {
                    flag = false;
                    chatInitiateInfoLayout.setVisibility(View.GONE);
                } else {
                    String internationalCharges = "0.0";
                    if (s.contains("###")) {
                        String str[] = s.split("###");
                        s = str[0];
                        internationalCharges = str[1];
                    }
                    long remTime = Long.parseLong(s);
                    String leftTime = "" + remTime;
                    if (remTime > 60) {
                        leftTime = getResources().getString(R.string.wait_time) + " " + leftTime + " " + getResources().getString(R.string.sec);
                    } else {
                        leftTime = getResources().getString(R.string.wait_time_almost);
                    }
                    if (remTime > 0) {
                        flag = true;
                        if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                            CUtils.setChatInitiateLayout(AstrologerDescriptionActivity.this, AstrosageKundliApplication.selectedAstrologerDetailBean, leftTime, internationalCharges,remTime);
                            chatInitiateInfoLayout.setVisibility(View.VISIBLE);
                        } else {
                            chatInitiateInfoLayout.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (Exception e) {
                //
            }

        }
    };

    private void initView() {

        //Log.e("SAN ADA ", "initview");

        readMore = getResources().getString(R.string.read_more);
        readLess = getResources().getString(R.string.read_less);
        offerImg = findViewById(R.id.offer_img);
        actualConsultaionValueTxt = findViewById(R.id.actual_consultaion_value_txt);
        vartaIconLayout = findViewById(R.id.varta_icon_layout);
        backTitleLayout = findViewById(R.id.back_title_layout);
        walletLayout = findViewById(R.id.wallet_layout);
        walletPriceTxt = findViewById(R.id.wallet_price_txt);
        vedicAstrologerTxt = findViewById(R.id.vedic_astrologer_txt);
        languageTxt = findViewById(R.id.language_txt);
        experienceTxt = findViewById(R.id.experience_txt);
        astrologerNameTxt = findViewById(R.id.astrologer_name_txt);
        totalRatingTxt = findViewById(R.id.rating_txt_total);
        rating_tag_open = findViewById(R.id.rating_tag_open);
        rating_tag_close = findViewById(R.id.rating_tag_close);
        rating_tag_img = findViewById(R.id.rating_tag);

        ratingTxt = findViewById(R.id.rating_txt);
        consultaionValueTxt = findViewById(R.id.consultaion_value_txt);
        aboutAstrologerHeadingTxt = findViewById(R.id.about_astrologer_heading_txt);
        astrologerDescription = findViewById(R.id.astrologer_description);
        expertiseTxt = findViewById(R.id.expertise_txt);
        giveFeedbackBtn = findViewById(R.id.fr_give_feedback_btn);
        callNowBtn = findViewById(R.id.call_now_btn_ad);
        chatNowBtn = findViewById(R.id.chat_now_btn);
        callNowBtnTxt = findViewById(R.id.call_now_btn_txt);
        chatNowBtnTxt = findViewById(R.id.chat_now_btn_txt);
        allReviewTxt = findViewById(R.id.all_review_txt);
        bio_recyclerview = findViewById(R.id.bio_recyclerview);
        expertiseValueTxt = findViewById(R.id.expertise_value_txt);
        consultaionTxt = findViewById(R.id.consultaion_txt);
        containerLayout = findViewById(R.id.container_layout);
        llBottomLayout = findViewById(R.id.llBottomLayout);
        ratingStar = findViewById(R.id.rating_star);
        expertiseLayout = findViewById(R.id.expertise_layout);
        ratingLayout = findViewById(R.id.rating_layout);
        rlLive = findViewById(R.id.rl_live);
        astrologer_description_dummy_layout = findViewById(R.id.astrologer_description_dummy_layout);
        mainlayout = findViewById(R.id.mainlayout);
        logoutBtn = findViewById(R.id.logout_btn);
        feedbackLayout = findViewById(R.id.feedback_layout);
        callNowLayout = findViewById(R.id.call_now_layout);
        read_more_txt = findViewById(R.id.read_more_txt);
        astrologer_description_dummy = findViewById(R.id.astrologer_description_dummy);
        loadMoreBtn = findViewById(R.id.load_more_btn);
        followAstrologer = findViewById(R.id.followAstrologer);
        followCount = findViewById(R.id.follower_count);
        tvSeeAllReviews = findViewById(R.id.tvSeeAllReviews);
        tvSeeSimilarAstrologers = findViewById(R.id.tvSeeSimilarAstrologers);
        llSeeAllAstrologers = findViewById(R.id.llSeeAllAstrologers);
        tvAstroDescWaitTime = findViewById(R.id.tvAstroDescWaitTime);
        share_your_experience_notes = findViewById(R.id.share_your_experience_notes);
        ivChatNow = findViewById(R.id.chat_now_img);
        ivCallNow = findViewById(R.id.call_now_img);

        logoutBtn.setVisibility(View.GONE);
        recyclerview = findViewById(R.id.recyclerview);
        rvSimilarAstrologers = findViewById(R.id.rvSimilarAstrologers);
        rvSimilarAstrologers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        bio_recyclerview.setLayoutManager(new LinearLayoutManager(context));

        verifiedImg = findViewById(R.id.verified_img);
        onlineOfflineImg = findViewById(R.id.online_offline_img);
        profileImg = findViewById(R.id.profile_img);

        logoGif = (ImageView) findViewById(R.id.logo_gif);
        loadmore_gif = (ImageView) findViewById(R.id.loadmore_gif);
        new_user = findViewById(R.id.new_user);
        ruppee_sign = findViewById(R.id.ruppee_sign);
        scrollview_layout = findViewById(R.id.scrollview_layout);

        //giveFeedbackBtn.setText(Html.fromHtml(getResources().getString(R.string.rate_this_astrologer)));

        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        vartaIconLayout.setVisibility(View.GONE);
        backTitleLayout.setVisibility(View.VISIBLE);
        read_more_txt.setOnClickListener(this);
        mFollowLayout = findViewById(R.id.layout_live_follow);
        navView = findViewById(R.id.nav_view);
        fabAstrologerDescription = findViewById(R.id.fabAstrologerDescription);
        ivAstroDescShare = findViewById(R.id.ivAstroDescShare);
        online_offline_img1 = findViewById(R.id.online_offline_img1);
        ivAstroDescGift = findViewById(R.id.ivAstroDescGift);

        fabAstrologerDescription.setOnClickListener(v -> {
            fabActions();
        });

        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();

        mFollowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFollowLayout.getVisibility() == View.VISIBLE) {
                    close_layout(mFollowLayout);
                }
            }
        });
        mUnFollowLayout = findViewById(R.id.layout_live_unfollow);
        mUnFollowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
                    close_layout(mUnFollowLayout);
                }
            }
        });
        ll_video_busy = findViewById(R.id.ll_video_busy);
        videoCallView = findViewById(R.id.videoCallView);
        videoCallText = findViewById(R.id.videoCallText);
        videoCallIcon = findViewById(R.id.video_calls_icon);
        videoCallArrow = findViewById(R.id.video_call_arrow);
        TextView txt_video_busy = findViewById(R.id.txt_video_busy);
        FontUtils.changeFont(context, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        //FontUtils.changeFont(context, giveFeedbackBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, callNowBtnTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, chatNowBtnTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        FontUtils.changeFont(context, astrologerNameTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, totalRatingTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, aboutAstrologerHeadingTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, allReviewTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvSeeAllReviews, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, tvSeeSimilarAstrologers, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, expertiseTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, read_more_txt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, consultaionTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, ratingTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        //FontUtils.changeFont(context, walletPriceTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, loadMoreBtn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, expertiseValueTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, consultaionValueTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, actualConsultaionValueTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, vedicAstrologerTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, languageTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, experienceTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, astrologerDescription, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, astrologer_description_dummy, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, tvAstroDescWaitTime, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, new_user, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, ruppee_sign, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, videoCallText, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, txt_video_busy, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        read_more_txt.setText(Html.fromHtml(getResources().getString(R.string.read_more)));

        initSwipToRefresh();
        initReceiver();

        //containerLayout.setVisibility(View.GONE);
        llBottomLayout.setVisibility(View.GONE);
        callNowLayout.setVisibility(View.GONE);

        if (CUtils.getGiftModelArrayList() != null && !CUtils.getGiftModelArrayList().isEmpty()) {
            giftModelArrayList = CUtils.getGiftModelArrayList();
            ivAstroDescGift.setVisibility(View.VISIBLE);
        } else {
           try {
               apiRepository = new ApiRepository(this);
               Map<String, String> stringMap = com.ojassoft.astrosage.varta.utils.CUtils.getLiveAstroParams(AstrologerDescriptionActivity.this, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(AstrologerDescriptionActivity.this));
               apiRepository.getGiftDataFromServer(stringMap, new ApiRepository.ApiCallback() {
                   @Override
                   public void onSuccess() {
                       giftModelArrayList = CUtils.getGiftModelArrayList();
                       ivAstroDescGift.setVisibility(View.VISIBLE);
                   }

                   @Override
                   public void onError(String errorMessage) {

                   }
               });
           }catch (Exception e){

           }

        }

//        getIntentData();

        walletLayout.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        callNowBtn.setOnClickListener(this);
        chatNowBtn.setOnClickListener(this);
        giveFeedbackBtn.setOnClickListener(this);
        loadMoreBtn.setOnClickListener(this);
        followAstrologer.setOnClickListener(this);
        tvSeeAllReviews.setOnClickListener(this);
        ivAstroDescGift.setOnClickListener(this);
        followAstrologer.setEnabled(false);
        followAstrologer.setClickable(false);
        videoCallView.setOnClickListener(this);
        /*scrollview_layout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                    //Log.e("SAN ", "Load more...");
                    if (tvSeeAllReviews.getVisibility() == View.GONE) {
                        //loadMore();
                    }
                }
            }
        });*/

        scrollview_layout.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                if (tvSeeAllReviews.getVisibility() == View.GONE) {
                    if (!isFeedbackLoading) {
                        isFeedbackLoading = true;
                        loadMore();
                    }
                }
            }
        });

        ivAstroDescShare.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_SHARE,
                    FIREBASE_EVENT_ITEM_CLICK, "");
            prepareSharingCard();
            String astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBeanData.getImageFileLarge();
            findViewById(R.id.aslLinearLayout).setVisibility(View.VISIBLE);
            TopCropImageView aslProfile = findViewById(R.id.iv_profile_img);
            Glide.with(aslProfile)
                    .addDefaultRequestListener(new RequestListener<Object>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Object> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Object resource, @NonNull Object model, Target<Object> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            try {
                                new Handler().postDelayed(() -> {
                                    CUtils.shareAstrologer(context, astrologerDetailBeanData,
                                            getScreenShot(findViewById(R.id.aslLinearLayout)));
                                    findViewById(R.id.aslLinearLayout).setVisibility(View.GONE);
                                }, 100);
                            }catch (Exception ignore){}
                            return false;
                        }
                    })
                    .load(astrologerProfileUrl)
                    .into(aslProfile);

        });

        chatInitiateInfoLayout = findViewById(R.id.chat_initiate_info_layout);
        cross_btn = chatInitiateInfoLayout.findViewById(R.id.cross_chat_initiate_view);
        cross_btn.setEnabled(true);
        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cross_btn.setEnabled(false);
                openChatRequestCancel();
            }
        });
        setJoinChatView();
    }

    public Uri getScreenShot(ScrollView view) {
        try {
            int totalHeight = view.getChildAt(0).getHeight();
            int totalWidth = view.getChildAt(0).getWidth();
            Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            view.getChildAt(0).draw(canvas);

            File file = new File(getExternalCacheDir(), "share_media.png");
            FileOutputStream fOut = new FileOutputStream(file);
            returnedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return getUriForFile(context, "com.ojassoft.astrosage", file);
        }catch (Exception e){
            return null;
        }
    }
    
    private void openChatRequestCancel() {

        Log.e("SAN ", "Astro Desc openChatRequestCancel() X click ");

        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_button_click", AstrosageKundliApplication.currentEventType, "");
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.end_chat_confirm_dialog, null);
        builder.setView(dialogView);

        TextView end_chat_confirm_text = dialogView.findViewById(R.id.end_chat_confirm_text);
        FontUtils.changeFont(AstrologerDescriptionActivity.this, end_chat_confirm_text, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        TextView end_chat_yes = dialogView.findViewById(R.id.end_chat_yes);
        TextView end_chat_no = dialogView.findViewById(R.id.end_chat_no);
        alert = builder.create();

        end_chat_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_btn_yes_click", AstrosageKundliApplication.currentEventType, "");
                alert.dismiss();
                Log.e("SAN ", "Astro Desc openChatRequestCancel() X click yes ");
                clickCrossChatRequest();
            }
        });

        end_chat_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("SAN ", "Astro Desc openChatRequestCancel() X click no ");
                cross_btn.setEnabled(true);
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_btn_no_click", com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                alert.dismiss();
            }
        });
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("request_chat_cross_dialog_show", AstrosageKundliApplication.currentEventType, "");
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        if (alert.getWindow() != null) {
            alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

    }

    private void clickCrossChatRequest(){

        if (CUtils.getSelectedAstrologerID(getApplicationContext()) != null && CUtils.getSelectedAstrologerID(getApplicationContext()).length() > 0 &&
                CUtils.getSelectedChannelID(getApplicationContext()) != null && CUtils.getSelectedChannelID(getApplicationContext()).length() > 0) {
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_CANCELED;
            CUtils.changeFirebaseKeyStatus(CUtils.getSelectedChannelID(getApplicationContext()), CGlobalVariables.CANCELLED, true, CGlobalVariables.USER_CANCELLED);
            chatCompleted(CGlobalVariables.END_CHAT_URL, CUtils.getSelectedChannelID(getApplicationContext()), CGlobalVariables.USER_CANCELLED, CGlobalVariables.USER_BUSY, CUtils.getSelectedAstrologerID(getApplicationContext()));
        }

        if (CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
            if (CUtils.connectAgoraCallBean != null && AstrosageKundliApplication.selectedAstrologerDetailBean != null)
                CUtils.changeFirebaseKeyStatus(CUtils.connectAgoraCallBean.getCallsid(), CGlobalVariables.CANCELLED, true, CGlobalVariables.USER_CANCELLED);
            chatCompleted(CGlobalVariables.END_CALL_URL, CUtils.connectAgoraCallBean.getCallsid(), CGlobalVariables.USER_CANCELLED, CGlobalVariables.USER_BUSY, AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId());
        }

    }


    private void prepareSharingCard() {

        TextView aslName = findViewById(R.id.tv_astrologer_name);
        TextView aboutAstro = findViewById(R.id.tv_about);
        TextView tvDescription = findViewById(R.id.tv_astro_disc);
        FontUtils.changeFont(context, aslName, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        TextView aslExpertise = findViewById(R.id.tv_astrologer_exp);
        FontUtils.changeFont(context, aslExpertise, CGlobalVariables.FONTS_OPEN_SANS_LIGHT);
        FontUtils.changeFont(context, aboutAstro, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvDescription, CGlobalVariables.FONTS_OPEN_SANS_LIGHT);
        tvDescription.setText(HtmlCompat.fromHtml(astrologerDetailBeanData.getDescription(),HtmlCompat.FROM_HTML_MODE_LEGACY));
        aslName.setText(astrologerDetailBeanData.getName());
        aslExpertise.setText(astrologerDetailBeanData.getPrimaryExpertise());

    }

    public void customBottomNavigationFont(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    customBottomNavigationFont(child);
                }
            } else if (v instanceof TextView) {
                FontUtils.changeFont(context, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    private void setBottomNavigationText() {

        // find MenuItem you want to change
        ImageView navHomeImg = navView.findViewById(R.id.imgViewHome);
        TextView navHomeTxt = navView.findViewById(R.id.txtViewHome);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);
        TextView navLive = navView.findViewById(R.id.txtViewLive);

        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if(isAIChatDisplayed){
            navChatTxt.setText(getResources().getString(R.string.text_ask));
            navCallTxt.setText(getResources().getString(R.string.ai_astrologer));
            Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_unselected).into(navCallImg);
        } else {
            navChatTxt.setText(getResources().getString(R.string.chat_now));
            navCallTxt.setText(getResources().getString(R.string.call));
            navCallImg.setImageResource(R.drawable.nav_call_icons);
        }

        // set new title to the MenuItem
        navHomeTxt.setText(getResources().getString(R.string.title_home));
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this, fabAstrologerDescription, navLive);

        if (CUtils.getUserLoginStatus(context)) {
                navHisTxt.setText(getResources().getString(R.string.history));
                navHisImg.setImageResource(R.drawable.nav_more_icons);
            } else {
                navHisTxt.setText(getResources().getString(R.string.sign_up));
                navHisImg.setImageResource(R.drawable.nav_profile_icons);
            }
        //navView.getMenu().setGroupCheckable(0, false, true);
        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    private void fabActions() {
        try {
            //boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if (!liveStreamingEnabledForAstrosage) { //fetch data according to tagmanag
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(AstrologerDescriptionActivity.this, ASTROLOGER_DESCRIPTION_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(context);
            } else {
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(AstrologerDescriptionActivity.this, ASTROLOGER_DESCRIPTION_BOTTOM_BAR_LIVE_PARTNER_ID);
                if (fromLiveActivity) {
                    finish();
                } else {
                    gotoAllLiveActivityFromLiveIcon(this);
                }
            }
        } catch (Exception e) {
            //
        }
    }

    private void getAstrologerList() {
        String response = CUtils.getAstroList();
        if (!TextUtils.isEmpty(response)) {
            parseAstrologerList(response);
        } else {
            llSeeAllAstrologers.setVisibility(View.GONE);
        }
    }

    private void parseAstrologerList(String responseData) {
        //Log.d("TestAstroOnHome",responseData);
        try {
            if (astrologerDetailBeanArrayList == null) {
                astrologerDetailBeanArrayList = new ArrayList<>();
            } else {
                astrologerDetailBeanArrayList.clear();
            }
            JSONObject jsonObject = new JSONObject(responseData);
            if (jsonObject.length() > 0) {

                if (jsonObject.has("verifiedicon")) {
                    try {
                        CUtils.setVerifiedAndOfferImage(URLDecoder.decode(jsonObject.getString("verifiedicon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("verifiedicondetail"), "UTF-8"),
                                URLDecoder.decode(jsonObject.getString("offericon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("offericondetail"), "UTF-8"));
                    } catch (Exception ex) {

                    }
                }

                JSONArray jsonArray = jsonObject.optJSONArray("astrologers");
                if (jsonArray == null || jsonArray.length() == 0) {
                    llSeeAllAstrologers.setVisibility(View.GONE);
                    return;
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.optJSONObject(i);
                    if (object == null) {
                        continue;
                    }
                    AstrologerDetailBean astrologerDetailBean = CUtils.parseAstrologerObject(object);
                    if (astrologerDetailBean == null) continue;
                    astrologerDetailBeanArrayList.add(astrologerDetailBean);
                }

                if (astrologerDetailBeanArrayList.isEmpty()) {
                    llSeeAllAstrologers.setVisibility(View.GONE);
                    return;
                }

                ArrayList<AstrologerDetailBean> filteredAstrologerList = new ArrayList<>();
                if (astrologerLanguageList != null && !astrologerLanguageList.isEmpty()) {
                    for (AstrologerDetailBean astrologerDetailBean : astrologerDetailBeanArrayList) {
                        String language = astrologerDetailBean.getLanguage();
                        if (TextUtils.isEmpty(language)) {
                            continue;
                        }
                        for (int i = 0; i < astrologerLanguageList.size(); i++) {
                            if (language.contains(astrologerLanguageList.get(i))) {
                                filteredAstrologerList.add(astrologerDetailBean);
                                break;
                            }
                        }
                    }
                }

                if (filteredAstrologerList.isEmpty()) {
                    filteredAstrologerList.addAll(astrologerDetailBeanArrayList);
                }

                Collections.shuffle(filteredAstrologerList, new Random());
                ArrayList<AstrologerDetailBean> randomAstrologerList = new ArrayList<>();
                int maxAstrologersToShow = Math.min(3, filteredAstrologerList.size());
                for (int i = 0; i < maxAstrologersToShow; i++) {
                    randomAstrologerList.add(filteredAstrologerList.get(i));
                }

                if (!randomAstrologerList.isEmpty()) {
                    onlineAstrologerAdapter = new OnlineAstrologerAdapter(this, randomAstrologerList);
                    rvSimilarAstrologers.setAdapter(onlineAstrologerAdapter);
                } else {
                    llSeeAllAstrologers.setVisibility(View.GONE);
                }

            } else {
                llSeeAllAstrologers.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadMore() {

        if (astrologerUserReciewAdapter != null) {
            if (userFeedbackList != null && (userFeedbackList.size() >= 10 || isReviewBlocked)) {
                fetchAstrologerFeedback(true);
            } else {
                loadMoreBtn.setVisibility(View.INVISIBLE);
            }
        } else {
            loadMoreBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void initSwipToRefresh() {

        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh_astro_des);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //to refresh data on swipe refresh
                        try {
                            onResume();
                        }catch (Exception e){
                            //
                        }
                    }
                }, 500);
            }
        });

    }

    boolean hasFollow = false;

    private void getIntentData(Intent intent) {

        //Log.e("SAN ADA ", "getIntentData");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //Log.e("SAN ADA ", "getIntentData != null");
            consultationType = bundle.getString(CGlobalVariables.KEY_CONSULTATION_TYPE);
            phoneNumber = bundle.getString("phoneNumber");
            urlText = bundle.getString("urlText");
            fromDashboard = bundle.getBoolean("fromDashboard");
            isAIAstrologer = bundle.getBoolean("isAIAstrologer");
            fromLiveActivity = bundle.getBoolean("isFromLiveActivity");
            useIntroOffer = bundle.getInt("useIntroOffer", 0);
            isFreeForChat = bundle.getInt("isFreeForChat", 0);
            isFreeForCall = bundle.getInt("isFreeForCall", 0);
            isFromNotification = bundle.getBoolean("isFromNotification", false);
            isFreechat = bundle.getBoolean("isFreechat", false);
            if (bundle.containsKey("hasFollow")) {
                hasFollow = bundle.getBoolean("hasFollow");
            }
            String offerType = bundle.getString("offerType");
            if(offerType != null && !offerType.isEmpty()){
                offerFromNotification = 1;
            }else {
                offerFromNotification = 0;
            }
            //Log.e("actionOnIntent", "offerType= " + offerType);

            String userOfferType = CUtils.getCallChatOfferType(this);
            //Log.e("TestAI", "userOfferType= " + userOfferType);
            //in case of user has FIRSTSESSIONFREE or REDUCEDPRICE offer and got free chat to show from notification then set useIntroOffer to true
            if(!TextUtils.isEmpty(userOfferType) && isFreechat){// in this case show free or new user price according to user offer type
                useIntroOffer = 1; //true
            }
            if (urlText == null || urlText.length() == 0) {
                actionOnIntent(intent);
            } else {
                if (bundle.containsKey("msg") && bundle.containsKey("title")) {
                    message = bundle.getString("msg");
                    title = bundle.getString("title");
                }
                if (bundle.containsKey("from_notification_center")) {
                    from_notification_center = bundle.getBoolean("from_notification_center");
                }
                fetchAstrologerDetail(phoneNumber, urlText);
            }
        } else {
            //Log.e("SAN ADA ", "getIntentData == null");
        }

        registerReceiverRefundFreeChat();


    }

    private void initReceiver() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                callStatus = intent.getStringExtra(CallStatusCheckService.BROAD_RESULT);
                // Log.e("LoadMore callstatus ", callStatus);
                if (callStatus != null && callStatus.length() > 0) {
                    //{"status":"1","msg":"The Call is in-progress."}
                    try {
                        JSONObject jsonObject = new JSONObject(callStatus);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("msg");
                        if (status.equals("0")) {
                            try {
                                callMsgDialogData(getResources().getString(R.string.call_not_connected), getResources().getString(R.string.sorry), true, CALL_CLICK);
                                /*CallMsgDialog dialog = new CallMsgDialog( getResources().getString(R.string.call_not_connected), getResources().getString(R.string.sorry), true, CGlobalVariables.CALL_CLICK);
                                dialog.show(getSupportFragmentManager(), "Dialog");*/
                            } catch (Exception e) {
                                CUtils.showSnackbar(containerLayout, msg, context);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        call_receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String messagee = intent.getStringExtra(CGlobalVariables.BROAD_MSG_RESULT);
                String titlee = intent.getStringExtra(CGlobalVariables.BROAD_TITLE_RESULT);
                String urlText = intent.getStringExtra(CGlobalVariables.BROAD_LINK_RESULT);
                //Log.e("TestCall","urlText="+urlText);
                consultationType = intent.getStringExtra(CGlobalVariables.KEY_CONSULTATION_TYPE);
                try {
                    if (urlText != null && urlText.length() > 0) {
                        AstrologerDescriptionActivity.this.urlText = urlText;
                        isShowRatingDialogAfterCall = true;
                        //getAstrologerStatusPrice(astroId);
                        if (TextUtils.isEmpty(astroId)) {
                            getAstrologerDetatils(urlText);
                        } else {
                            getAstrologerStatusPrice(astroId);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


    }

    private void getAstrologerDetatils(String urlText) {

        //Log.e("SAN ADA ", "Astro URL Status N Price " + CGlobalVariables.ASTROLOGER_STATUS_N_PRICE_URL + " Time=> " + System.currentTimeMillis());
        showProgressBar();
        /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ASTROLOGER_DESCRIPTION_URL,
                AstrologerDescriptionActivity.this, false, getAstroDetailsParams(urlText), ASTROLOGER_DETAIL_METHOD).getMyStringRequest();
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);*/


        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerDetails(getAstroDetailsParams(urlText));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideProgressBar();
                try {
                    String myResponse = response.body().string();
                    JSONObject astrologerObj = new JSONObject(myResponse);
                    parseAstrologerDetail(astrologerObj);
                    if (isdisable) {
                        Toast.makeText(context, getResources().getString(R.string.astrologer_no_lon_avail), Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    getAstrologerStatusPrice(astroId);
                    fetchAstrologerFeedback(false);
                } catch (Exception e) {
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
            }
        });
    }

    public Map<String, String> getAstroDetailsParams(String urlText) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.URL_TEXT, urlText);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));
        //Log.e("SAN ADA ", "Astro URL Status N Price params " + headers.toString() );
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        String offerType = CUtils.getCallChatOfferType(this);
        if (offerType == null) offerType = "";
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(CGlobalVariables.USE_INTRO_OFFER, String.valueOf(useIntroOffer));
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(this));
        return CUtils.setRequiredParams(headers);
    }

    private void getAstrologerStatusPrice(String astroid) {
        //Log.e("SAN ADA ", "Astro URL Status N Price " + CGlobalVariables.ASTROLOGER_STATUS_N_PRICE_URL + " Time=> " + System.currentTimeMillis());
        /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ASTROLOGER_STATUS_N_PRICE_URL,
                AstrologerDescriptionActivity.this, false, getAstroStatusParams(astroid), ASTRO_STATUS_UPDATE).getMyStringRequest();
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);*/

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerStatusPrice(getAstroStatusParams(astroid));
        Log.e("TestNewUser", call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideSwipToRefresh();
                try {
                    String myResponse = response.body().string();
                    //Log.e("TestNewUser", myResponse);

                    parseAstrologerStatus(myResponse);
                    ivAstroDescShare.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideSwipToRefresh();
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
            }
        });
    }

    public Map<String, String> getAstroStatusParams(String astroId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        //Log.e("SAN ADA ", "Astro URL Status N Price params " + headers.toString() );
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));

        String offerType = CUtils.getCallChatOfferType(this);
        if (offerType == null) offerType = "";
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        //Log.e("TestNewUser", "offer= " + offerType);

        headers.put(CGlobalVariables.USE_INTRO_OFFER, String.valueOf(useIntroOffer));
        headers.put(CGlobalVariables.OFFER_FROM_NOTIFICATION,offerFromNotification+"");
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(this));
        //Log.e("TestNewUser", "headers= " + headers);
        return headers;
    }

    private void updateWalletData() {
        boolean isLogin = CUtils.getUserLoginStatus(AstrologerDescriptionActivity.this);
        if (isLogin) {
            walletLayout.setVisibility(View.VISIBLE);
        } else {
            walletLayout.setVisibility(View.GONE);
        }
        walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat(CUtils.getWalletRs(AstrologerDescriptionActivity.this)));
    }

    @Override
    public void onClick(View v) {
        if (astrologerDetailBeanData == null) return;
        switch (v.getId()) {
            case R.id.wallet_layout:
                openWalletScreen("astro_desc");
                break;

            case R.id.ivBack:
                if (message != null && message.length() > 0 && title != null && title.length() > 0) {
                    CGlobalVariables.isChatCompleteRefreshHome = true;
                }
                finish();
                break;

            case R.id.fr_give_feedback_btn:
                //Log.e("MOBILE NO ", astrologerDetailBeanData.getPhoneNumber() + "  " + astrologerDetailBeanData.getAstroWalletId());
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                String phoneNo = CUtils.getUserID(context);
                /*FeedbackDialog dialog = new FeedbackDialog(phoneNo, astrologerDetailBeanData.getAstrologerId(),
                        astrologerDetailBeanData.getUserOwnReviewModel(), consultationType, "");*/
                FeedbackDialog dialog = new FeedbackDialog(consultationType, "", getSupportFragmentManager(), astrologerDetailBeanData);
                consultationType = "";
                dialog.show(getSupportFragmentManager(), "Dialog");
                /*ratingAndDakshinaDialog = new RatingAndDakshinaDialog(context, this, phoneNo, astrologerDetailBeanData.getAstrologerId(), astrologerDetailBeanData.getUserOwnReviewModel());
                ratingAndDakshinaDialog.show(getSupportFragmentManager(), "FeedbackDialog");*/
                break;

            case R.id.call_now_btn_ad:
                errorLogs = errorLogs + "call now button clicked\n";
                if (CUtils.isChatNotInitiated()) {
                    AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
                    String aiai = astrologerDetailBeanData.getAiAstrologerId();
                    if(!TextUtils.isEmpty(aiai) && !aiai.equals("0")){
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_DESC_AI_AUDIO_CALL_BTN, AstrosageKundliApplication.currentEventType, "");
                    }else {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_DESC_AUDIO_CALL_BTN, AstrosageKundliApplication.currentEventType, "");
                    }
                    boolean isLogin = CUtils.getUserLoginStatus(context);
                    if (isLogin) {
                        if (astrologerDetailBeanData.getIsOnline().equalsIgnoreCase("true")) {
                            if (astrologerDetailBeanData.getIsBusy().equalsIgnoreCase("true")) {
                            /*String msg = getResources().getString(R.string.astrologer_busy_msg);
                            //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
                            callMsgDialogData(msg, "", true, CGlobalVariables.CALL_CLICK);*/
                            /*CallMsgDialog dialog1 = new CallMsgDialog(getResources().getString(R.string.astrologer_busy_msg), "", true, CGlobalVariables.CALL_CLICK);
                            dialog1.show(getSupportFragmentManager(), "Dialog");*/
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_NOTI_FREE,
                                        AstrosageKundliApplication.currentEventType, "");
                                CUtils.showNotificationAlertDialog(this, astrologerDetailBeanData, getSupportFragmentManager());
                            } else {
                                if (/*astrologerDetailBeanData.getPhoneNumber().length() > 0 && */astrologerDetailBeanData.getUrlText().length() > 0) {
                                    com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.ASTRO_DETAIL_CALL_BTN_PARTNER_ID);
                                    if (isFreeForCall == 1) { //true
                                        isFreeConsultation = true;
                                        astrologerDetailBeanData.setFreeForCall(true);
                                    } else if (isFreeForChat == 2) { //false
                                        astrologerDetailBeanData.setFreeForCall(false);

                                        isFreeConsultation = false;
                                    } else {
                                        isFreeConsultation = astrologerDetailBeanData.getUseIntroOffer();
                                        astrologerDetailBeanData.setFreeForCall(astrologerDetailBeanData.getUseIntroOffer());
                                    }
                                     if (!TextUtils.isEmpty(aiai) && !aiai.equals("0")) {
                                        astrologerDetailBeanData.setCallSource(CGlobalVariables.ASTROLOGERS_DETAILS_ACTIVITY_CALL_BTN);
                                    }else{
                                         astrologerDetailBeanData.setCallSource(TAG);
                                    }
                                    callNowBtn.setEnabled(false);
                                    boolean isAgoraCallEnabled = com.ojassoft.astrosage.utils.CUtils.getBooleanData(AstrologerDescriptionActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.ISAGORACALLENABLED, false);
                                    //Log.d("testcallAstrodeatils","astrologerDetailBeanData.getAcceptInternetCall()==>"+astrologerDetailBeanData.getAcceptInternetCall());
                                    if (isAgoraCallEnabled && astrologerDetailBeanData.getAcceptInternetCall()  != CGlobalVariables.NETWORK_CALL) {
                                        //checkCallMedium();
                                        checkVoiceCallPermission();
                                    } else if(!TextUtils.isEmpty(aiai) && !aiai.equals("0")) {
                                        checkVoiceCallPermission();
                                    } else {
                                        checkCallMedium();
                                        //CUtils.openProfileOrKundliAct(this, astrologerDetailBeanData.getUrlText(), "call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                                    }


                                } else {
                                    CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_IN_ANOTHER_CALL,
                                            AstrosageKundliApplication.currentEventType, "");
                                    String msg = getResources().getString(R.string.astrologer_busy_msg);
                                    //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
                                    callMsgDialogData(msg, "", true, CALL_CLICK);
                                /*CallMsgDialog dialog1 = new CallMsgDialog(getResources().getString(R.string.astrologer_busy_msg), "", true, CGlobalVariables.CALL_CLICK);
                                dialog1.show(getSupportFragmentManager(), "Dialog");*/
                                    //CUtils.showSnackbar(mainlayout, getResources().getString(R.string.astrologer_busy_msg), context);
                                }
                            }
                        } else {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_IN_ANOTHER_CALL,
                                    AstrosageKundliApplication.currentEventType, "");
                            showCallChatStatusDialog(CALL_CLICK);
                            /*if (astrologerDetailBeanData.getIsBusy().equalsIgnoreCase("true")) {
                                String msg = getResources().getString(R.string.astrologer_busy_msg);
                                //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
                                callMsgDialogData(msg, "", true, CALL_CLICK);
                            } else {
                                String stringMsg = "";
                                if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                                    stringMsg = getResources().getString(R.string.astrologer_offline_call);
                                } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                                    stringMsg = getResources().getString(R.string.astrologer_offline_chat);
                                } else {
                                    stringMsg = getResources().getString(R.string.astrologer_offline_msg);
                                }
                                callMsgDialogData(stringMsg, "", true, CALL_CLICK);
                            }*/
                        /* CallMsgDialog dialog2 = new CallMsgDialog(getResources().getString(R.string.astrologer_offline_msg), "", true, CGlobalVariables.CALL_CLICK);
                        dialog2.show(getSupportFragmentManager(), "Dialog");*/
                            // CUtils.showSnackbar(mainlayout, getResources().getString(R.string.astrologer_offline_msg), context);
                        }
                    } else {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.AUDIO_CALL_USER_NOT_LOGIN,
                                AstrosageKundliApplication.currentEventType, "");
                        Intent intent1 = new Intent(context, LoginSignUpActivity.class);
                        intent1.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.ASTROLOGER_DETAIL_CALL_NOW_SCREEN);
                        context.startActivity(intent1);
                    }
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), AstrologerDescriptionActivity.this);
                }

                break;
            case R.id.videoCallView:
                errorLogs = errorLogs + "video call now button clicked\n";
                checkPermissionVideoCall();


                break;
            case R.id.chat_now_btn:
                AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                CUtils.fcmAnalyticsEvents("chat_btn_click_astro_detail", AstrosageKundliApplication.currentEventType, "");
                if (CUtils.getUserLoginStatus(context)) {
                    CUtils.saveStringData(context, CGlobalVariables.PREF_CHAT_BUTTON_CLICK, "chat");
                    //CGlobalVariables.isChatCompleteRefreshHome = false;
                    if (astrologerDetailBeanData != null) {
                        if (astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("false") && astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("false")) {
                            CUtils.fcmAnalyticsEvents("astro_offline_chat_when_online_user_receive_notification",
                                    AstrosageKundliApplication.currentEventType, "");

                            NotificationAlertDialog notificationAlertDialog = new NotificationAlertDialog(this, astrologerDetailBeanData, 1);
                            notificationAlertDialog.show(getSupportFragmentManager(), "NotificationAlertDialog");
                        } else if (astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("true")) {


                            if (astrologerDetailBeanData.getIsBusy().equalsIgnoreCase("true")) {
                                /*String msg = getResources().getString(R.string.astrologer_busy_msg_chat);
                                //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
                                callMsgDialogData(msg, "", true, CGlobalVariables.CHAT_CLICK);*/
                                CUtils.fcmAnalyticsEvents("astro_busy_chat_when_free_user_receive_notification",
                                        AstrosageKundliApplication.currentEventType, "");
                                CUtils.showNotificationAlertDialog(this, astrologerDetailBeanData, getSupportFragmentManager());
                            } else {
                                astrologerDetailBeanData.setCallSource(TAG);
                                CUtils.fcmAnalyticsEvents("astro_chat_method_called",
                                        AstrosageKundliApplication.currentEventType, "");

                                com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.ASTRO_DETAIL_CHAT_BTN_PARTNER_ID);

                                if (CUtils.isChatNotInitiated()) {
                                    chatNowBtn.setEnabled(false);

                                    if (isFreeForChat == 1) { //true
                                        astrologerDetailBeanData.setFreeForChat(true);
                                    } else if (isFreeForChat == 2) { //false
                                        astrologerDetailBeanData.setFreeForChat(false);
                                    } else {
                                        astrologerDetailBeanData.setFreeForChat(astrologerDetailBeanData.getUseIntroOffer());
                                    }
                                    if (isAIAstrologer) {
                                        ChatUtils.getInstance(AstrologerDescriptionActivity.this).initAIChat(astrologerDetailBeanData);
                                    } else {
                                        ChatUtils.getInstance(AstrologerDescriptionActivity.this).initChat(astrologerDetailBeanData);
                                    }
                                }else {
                                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.allready_in_chat), context);
                                }
                            }
                        } else {
                            CUtils.fcmAnalyticsEvents("astro_busy_in_another_chat",
                                    AstrosageKundliApplication.currentEventType, "");
                            showCallChatStatusDialog(CGlobalVariables.CHAT_CLICK);
//                            if (astrologerDetailBeanData.getIsBusy().equalsIgnoreCase("true")) {
//                                String msg = getResources().getString(R.string.astrologer_busy_msg_chat);
//                                //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
//                                callMsgDialogData(msg, "", true, CGlobalVariables.CHAT_CLICK);
//                            } else {
//                                String stringMsg = "";
//                                if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("false"))) {
//                                    stringMsg = getResources().getString(R.string.astrologer_offline_call);
//                                } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("true"))) {
//                                    stringMsg = getResources().getString(R.string.astrologer_offline_chat);
//                                } else {
//                                    stringMsg = getResources().getString(R.string.astrologer_offline_msg);
//                                }
//
//                                callMsgDialogData(stringMsg, "", true, CGlobalVariables.CHAT_CLICK);
//                            }
                        }
                    } else {
                        CUtils.fcmAnalyticsEvents("astro_busy_in_another_chat",
                                AstrosageKundliApplication.currentEventType, "");
                        String msg = getResources().getString(R.string.astrologer_busy_msg_chat);
                        //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
                        callMsgDialogData(msg, "", true, CGlobalVariables.CHAT_CLICK);
                    }

                } else {
                    CUtils.fcmAnalyticsEvents("chat_user_not_login",
                            AstrosageKundliApplication.currentEventType, "");
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    Intent intent = new Intent(context, LoginSignUpActivity.class);
                    intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.ASTROLOGER_DETAIL_CALL_NOW_SCREEN);
                    context.startActivity(intent);

                }

                break;

            case R.id.read_more_txt:
                astrologer_description_dummy.setVisibility(View.GONE);
                astrologerDescription.setVisibility(View.VISIBLE);
                bio_recyclerview.setVisibility(View.VISIBLE);
                read_more_txt.setVisibility(View.GONE);
                break;


            case R.id.load_more_btn:
                if (astrologerUserReciewAdapter != null) {
                    if (userFeedbackList != null && userFeedbackList.size() > 0 && userFeedbackList.size() >= 10) {
                        fetchAstrologerFeedback(true);
                    } else {
                        loadMoreBtn.setVisibility(View.INVISIBLE);
                    }
                } else {
                    loadMoreBtn.setVisibility(View.INVISIBLE);
                }
                break;

            case R.id.followAstrologer:
                followUnfollowAstrolgoer();
                break;

            case R.id.tvSeeAllReviews:
                tvSeeAllReviews.setVisibility(View.GONE);
                setFeedbackAdapter();
                break;

            case R.id.ivAstroDescGift:
                if (CUtils.getUserLoginStatus(context)) {
                    initGiftDialog();
                } else {
                    Intent intent1 = new Intent(context, LoginSignUpActivity.class);
                    intent1.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.ASTROLOGER_DETAIL_CALL_NOW_SCREEN);
                    context.startActivity(intent1);
                }
                /*feedbackDialog = new FeedbackDialog("", astrologerDetailBeanData.getAstrologerId(),
                        astrologerDetailBeanData.getUserOwnReviewModel(), consultationType,"",
                        astrologerDetailBeanData.getName(), astrologerDetailBeanData.getImageFile(), getSupportFragmentManager());
                consultationType = "";
                feedbackDialog.show(getSupportFragmentManager(), "Dialog");*/
                break;
        }
    }
    private void checkPermissionVideoCall() {
        boolean granted = true;
        for (String per : REQUESTED_PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            errorLogs = errorLogs + "video call granted permission initiateVideoCall() \n";
            initiateVideoCall();
        } else {
            requestPermissions();
        }
    }

    private void initiateVideoCall() {
        if (CUtils.isChatNotInitiated()) {
            AstrosageKundliApplication.currentEventType = CGlobalVariables.VIDEO_CALL_BTN_CLICKED;
            CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_DESC_VIDEO_CALL_BTN, AstrosageKundliApplication.currentEventType, "");
            boolean isLogin = CUtils.getUserLoginStatus(context);
            if (isLogin) {
                if (astrologerDetailBeanData.getIsOnline().equalsIgnoreCase("true")) {
                    if (astrologerDetailBeanData.getIsBusy().equalsIgnoreCase("true")) {

                        CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_NOTI_FREE,
                                AstrosageKundliApplication.currentEventType, "");
                        CUtils.showNotificationAlertDialog(this, astrologerDetailBeanData, getSupportFragmentManager());
                    } else {
                        if (astrologerDetailBeanData.getUrlText().length() > 0) {
                            com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.ASTRO_DETAIL_CALL_BTN_PARTNER_ID);

                            if (isFreeForCall == 1) { //true
                                isFreeConsultation = true;
                                astrologerDetailBeanData.setFreeForCall(true);
                            } else if (isFreeForCall == 2) { //false
                                astrologerDetailBeanData.setFreeForCall(false);

                                isFreeConsultation = false;
                            } else {
                                isFreeConsultation = astrologerDetailBeanData.getUseIntroOffer();
                                astrologerDetailBeanData.setFreeForCall(astrologerDetailBeanData.getUseIntroOffer());
                            }

                            astrologerDetailBeanData.setCallSource(TAG);
                            checkVartaLiveModule();
                        } else {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_IN_ANOTHER_CALL,
                                    AstrosageKundliApplication.currentEventType, "");
                            String msg = getResources().getString(R.string.astrologer_busy_msg);
                            //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
                            callMsgDialogData(msg, "", true, CGlobalVariables.CALL_TYPE_VIDEO);
                        }
                    }
                } else {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_IN_ANOTHER_CALL,
                            AstrosageKundliApplication.currentEventType, "");
                    showCallChatStatusDialog(CGlobalVariables.CALL_TYPE_VIDEO);
//                            if (astrologerDetailBeanData.getIsBusy().equalsIgnoreCase("true")) {
//                                String msg = getResources().getString(R.string.astrologer_busy_msg);
//                                //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
//                                callMsgDialogData(msg, "", true, CGlobalVariables.CALL_TYPE_VIDEO);
//                            } else {
//                                String stringMsg = "";
//                                if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("false"))) {
//                                    stringMsg = getResources().getString(R.string.astrologer_offline_call);
//                                } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("true"))) {
//                                    stringMsg = getResources().getString(R.string.astrologer_offline_chat);
//                                } else {
//                                    stringMsg = getResources().getString(R.string.astrologer_offline_msg);
//                                }
//
//                                callMsgDialogData(stringMsg, "", true, CALL_CLICK);
//                            }
                        /* CallMsgDialog dialog2 = new CallMsgDialog(getResources().getString(R.string.astrologer_offline_msg), "", true, CGlobalVariables.CALL_CLICK);
                        dialog2.show(getSupportFragmentManager(), "Dialog");*/
                    // CUtils.showSnackbar(mainlayout, getResources().getString(R.string.astrologer_offline_msg), context);
                }
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent1 = new Intent(context, LoginSignUpActivity.class);
                intent1.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.ASTROLOGER_DETAIL_CALL_NOW_SCREEN);
                context.startActivity(intent1);
            }
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), AstrologerDescriptionActivity.this);
        }
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                initiateVideoCall();

            } else {
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.need_necessary_permissions),context);
            }
        } else if(requestCode == VOICE_CALL_PERMISSION_REQ_ID){
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }
            if (granted) {
                CUtils.setHavePermissionRecordAudio("1");
                checkCallMedium();
            } else {
                CUtils.setHavePermissionRecordAudio("0");
//                if (astrologerDetailBeanData != null && astrologerDetailBeanData.getAcceptInternetCall() == CGlobalVariables.NETWORK_AND_INTERNET_CALL) {
//                    CUtils.openProfileOrKundliAct(this, astrologerDetailBeanData.getUrlText(), "call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
//                } else {
//                    CUtils.displayRequiredPermissionDialog(this);
//                }
                String aiai = astrologerDetailBeanData.getAiAstrologerId();
                if(TextUtils.isEmpty(aiai) || aiai.equals("0")) {
                    checkCallMedium();
                }else{
                    CUtils.showPermissionDeniedDialog(this);
                }
            }


        }
    }
    private void checkCallMedium() {
        try {
            ChatUtils.getInstance(AstrologerDescriptionActivity.this).initVoiceCall(astrologerDetailBeanData);
        } catch (Exception e){
            //
        }
        /*if (CUtils.isConnectedMobile(context)) {
            if (CUtils.isNetworkSpeedSlow(context)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.NETWORK_CALL_CONNECT_REASON_SPEED_SLOW,
                        AstrosageKundliApplication.currentEventType, "");
                CUtils.openProfileOrKundliAct(this, astrologerDetailBeanData.getUrlText(), "call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
            } else {
                errorLogs = errorLogs + "profile dialog open 1\n";
                ChatUtils.getInstance(AstrologerDescriptionActivity.this).initVoiceCall(astrologerDetailBeanData);
            }
        } else {
            errorLogs = errorLogs + "profile dialog open 2\n";
            ChatUtils.getInstance(AstrologerDescriptionActivity.this).initVoiceCall(astrologerDetailBeanData);
        }*/
    }

    private void showCallChatStatusDialog(String fromWhich) {
        if (astrologerDetailBeanData.getIsBusy().equalsIgnoreCase("true")) {
            String msg = getResources().getString(R.string.astrologer_busy_msg_chat);
            //msg = msg.replace("#",astrologerDetailBeanData.getWaitTimeRemaining());
            callMsgDialogData(msg, "", true, fromWhich);
        } else {
            String stringMsg = "";
            if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForVideoCall().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                stringMsg = getResources().getString(R.string.astrologer_offline_chat_video_call_av_voice_call);
            } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForVideoCall().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                stringMsg = getResources().getString(R.string.astrologer_offline_video_call_av_chat_voice_call);
            } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForVideoCall().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                stringMsg = getResources().getString(R.string.astrologer_offline_chat_av_voice_call_video_call);
            } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForVideoCall().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                stringMsg = getResources().getString(R.string.astrologer_offline_voice_call_av_chat_video_call);
            } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForVideoCall().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                stringMsg = getResources().getString(R.string.astrologer_offline_voice_call_video_call_av_chat);
            } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForVideoCall().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                stringMsg = getResources().getString(R.string.astrologer_offline_voice_call_chat_av_video_call);
            } else {
                stringMsg = getResources().getString(R.string.astrologer_offline_msg);
            }
//                if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("true")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("false"))) {
//                stringMsg = getResources().getString(R.string.astrologer_offline_call);
//            } else if ((astrologerDetailBeanData.getIsAvailableForChat().equalsIgnoreCase("false")) && (astrologerDetailBeanData.getIsAvailableForCall().equalsIgnoreCase("true"))) {
//                stringMsg = getResources().getString(R.string.astrologer_offline_chat);
//            } else {
//                stringMsg = getResources().getString(R.string.astrologer_offline_msg);
//            }
            callMsgDialogData(stringMsg, "", true, fromWhich);
        }
    }

    public void followUnfollowAstrolgoer() {
        if (followStatus != null && followStatus.equals("0")) {
            if (CUtils.getUserLoginStatus(AstrologerDescriptionActivity.this)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_FOLLOW_ASTROLOGER_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                followAstrologerRequest("1");
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent1 = new Intent(context, LoginSignUpActivity.class);
                intent1.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.ASTROLOGER_DETAIL_CALL_NOW_SCREEN);
                context.startActivity(intent1);
            }
        } else {
            openUnFollowDialog();
        }

    }

    private void openFollowDialog() {
        isFollowDialogShown = true;
        initAstrologerFollowDialog();
        if (mFollowLayout.getVisibility() == View.VISIBLE) {
            close_layout(mFollowLayout);
        }
        if (followStatus != null && followStatus.equals("0")) {
            open_layout(mFollowLayout);
        }
    }

    private void openUnFollowDialog() {
        try {
            initAstrologerUnFollowDialog();
            if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
                close_layout(mUnFollowLayout);
            }
            if (followStatus != null && followStatus.equals("1")) {
                open_layout(mUnFollowLayout);
            }
        }catch (Exception e){
            //
        }
    }

    private void initAstrologerFollowDialog() {
        AstrologerDetailBean astrologerDetailBean = astrologerDetailBeanData;

        TextView live_follow_textTv = mFollowLayout.findViewById(R.id.live_login_text);
        FontUtils.changeFont(AstrologerDescriptionActivity.this, live_follow_textTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        live_follow_textTv.setText(getResources().getString(R.string.astro_follow_txt).replace("#", astrologerDetailBean.getName()));

        TextView live_astrologer_name_follow = mFollowLayout.findViewById(R.id.live_astrologer_name_follow);
        FontUtils.changeFont(AstrologerDescriptionActivity.this, live_astrologer_name_follow, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        live_astrologer_name_follow.setText(astrologerDetailBean.getName());

        CircularNetworkImageView live_astrologer_image_follow = findViewById(R.id.live_astrologer_image_follow);
        String astrologerProfileUrl = "";
        if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
           // live_astrologer_image_follow.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(live_astrologer_image_follow);

        }

        Button live_follow_ok_btn = mFollowLayout.findViewById(R.id.live_follow_ok_btn);
        FontUtils.changeFont(AstrologerDescriptionActivity.this, live_follow_ok_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        ImageView follow_cancel = mFollowLayout.findViewById(R.id.follow_cancel);
        follow_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_layout(mFollowLayout);
            }
        });

        live_follow_ok_btn.setOnClickListener(view -> {
            close_layout(mFollowLayout);
            if (CUtils.getUserLoginStatus(AstrologerDescriptionActivity.this)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_FOLLOW_ASTROLOGER_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                followAstrologerRequest("1");
            } else {
//                openLoginDialog();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent1 = new Intent(context, LoginSignUpActivity.class);
                intent1.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.ASTROLOGER_DETAIL_CALL_NOW_SCREEN);
                context.startActivity(intent1);

            }
        });

    }

    private void initAstrologerUnFollowDialog() {
        try {
            AstrologerDetailBean astrologerDetailBean = astrologerDetailBeanData;

            TextView live_follow_textTv = mUnFollowLayout.findViewById(R.id.live_login_text);
            FontUtils.changeFont(AstrologerDescriptionActivity.this, live_follow_textTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            TextView live_astrologer_name_follow = mUnFollowLayout.findViewById(R.id.live_astrologer_name_follow);
            FontUtils.changeFont(AstrologerDescriptionActivity.this, live_astrologer_name_follow, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            live_astrologer_name_follow.setText(astrologerDetailBean.getName());

            CircularNetworkImageView live_astrologer_image_follow = findViewById(R.id.live_astrologer_image_unfollow);
            String astrologerProfileUrl = "";
            if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
                astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
                //live_astrologer_image_follow.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
                Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(live_astrologer_image_follow);
            }


            Button live_follow_ok_btn = mUnFollowLayout.findViewById(R.id.live_follow_ok_btn);
            FontUtils.changeFont(AstrologerDescriptionActivity.this, live_follow_ok_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            ImageView follow_cancel = mUnFollowLayout.findViewById(R.id.unfollow_cancel);
            follow_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close_layout(mUnFollowLayout);
                }
            });

            live_follow_ok_btn.setOnClickListener(view -> {
                close_layout(mUnFollowLayout);
                if (CUtils.getUserLoginStatus(AstrologerDescriptionActivity.this)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_UNFOLLOW_ASTROLOGER_BTN_CLICK,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    followAstrologerRequest("0");
                    CUtils.unSubscribeFollowTopic(context, astroId);
                } else {
//                openLoginDialog();
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    Intent intent1 = new Intent(context, LoginSignUpActivity.class);
                    intent1.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.ASTROLOGER_DETAIL_CALL_NOW_SCREEN);
                    context.startActivity(intent1);

                }
            });
        }catch (Exception e){
            //
        }
    }

    public void open_layout(View linearLayout) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", linearLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }

    public void close_layout(View linearLayout) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, linearLayout.getHeight());
        animator.setDuration(200L);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                linearLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                linearLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    public void followAstrologerRequest(String followVal) {
        if(followVal !=  null && followVal.equals("1")) {
            CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_WISHLIST, "follow_astrologer", "AstrologerDescriptionActivity");
            CUtils.addFollowEventForAstrologer(astrologerDetailBeanData.getAstrologerId(),"AstrologerDescriptionActivity");
        }
        if (CUtils.isConnectedWithInternet(AstrologerDescriptionActivity.this)) {

            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.followAstrologer(getfollowAstrologerParams(followVal));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        parseFollowAstrologerStatus(myResponse, followVal);
                    } catch (Exception e) {
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                }
            });


        } else {
            //CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.no_internet), AstrologerDescriptionActivity.this);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public Map<String, String> getfollowAstrologerParams(String followVal) {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(AstrologerDescriptionActivity.this));
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerDetailBeanData.getAstrologerId());
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(AstrologerDescriptionActivity.this));
        headers.put(CGlobalVariables.KEY_FOLLOW, followVal);
        headers.put(CGlobalVariables.KEY_IAPI, "1");
        headers.put(CGlobalVariables.ACTIONSOURCE, CUtils.getActivityName(AstrologerDescriptionActivity.this));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(AstrologerDescriptionActivity.this));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(AstrologerDescriptionActivity.this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        return headers;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
            close_layout(mUnFollowLayout);
        } else if (mFollowLayout.getVisibility() == View.VISIBLE) {
            close_layout(mFollowLayout);
        } else {
            if (message != null && message.length() > 0 && title != null && title.length() > 0) {
                CGlobalVariables.isChatCompleteRefreshHome = true;
            }
            finish();
        }
    }

    private void fetchAstrologerFeedback(boolean isLoadMore) {
        if (!CUtils.isConnectedWithInternet(context)) {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), context);
        } else {
            if (isLoadMore) {
                showLogoProgressbar(loadmore_gif);
            } else {
                showLogoProgressbar(logoGif);
            }
            //Log.e("SAN ADA ", " feedback url " + CGlobalVariables.LOAD_MORE_ASTROLOGER_FEEDBACK_URL);
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.LOAD_MORE_ASTROLOGER_FEEDBACK_URL,
                    AstrologerDescriptionActivity.this, false, getAstroFeedbackParams(), ASTROLOGER_FEEDBACK_DATA_METHOD).getMyStringRequest();
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getAstrologerFeedback(getAstroFeedbackParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        isFeedbackLoading = false;
                        hideLogoProgressbar(logoGif);
                        hideLogoProgressbar(loadmore_gif);
                        String myResponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(myResponse);
                        ArrayList<UserReviewBean> userReviewBeanArrayList = null;
                        if (jsonArray.length() > 0) {
                            userReviewBeanArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject reviewObject = jsonArray.getJSONObject(i);
                                UserReviewBean userReviewBean = new UserReviewBean();
                                if (reviewObject.has("countrycode")) {
                                    userReviewBean.setCountrycode(reviewObject.getString("countrycode"));
                                }

                                userReviewBean.setUsername(reviewObject.getString("username"));
                                userReviewBean.setMaskedusername(reviewObject.getString("maskedusername"));
                                userReviewBean.setComment(reviewObject.getString("comment"));
                                userReviewBean.setUserRatingType(reviewObject.optString("ratingname"));
                                userReviewBean.setUserRatingTypeValue(reviewObject.optString("ratingtype"));
                                userReviewBean.setRate(reviewObject.getString("rate"));
                                userReviewBean.setDate(reviewObject.getString("date"));
                                userReviewBean.setFeedbackId(reviewObject.getString("feedbackId"));
                                userReviewBean.setActualFeedbackId(reviewObject.getString("actualfeedbackid"));
                                userReviewBeanArrayList.add(userReviewBean);
                            }
                            if (userReviewBeanArrayList != null && userReviewBeanArrayList.size() > 0) {
                                userFeedbackList.addAll(userReviewBeanArrayList);
                                astrologerDetailBeanData.setUserReviewBeanArrayList(userFeedbackList);
                                if (astrologerUserReciewAdapter != null) {
                                    if (userReviewBeanArrayList.size() < 10) {
                                        loadMoreBtn.setVisibility(View.INVISIBLE);
                                    }
                                    astrologerUserReciewAdapter.AstrologerUserFeedbackUpdate(userFeedbackList);
                                } else {
                                    setFeedbackAdapter();
                                }
                            } else {
                                loadMoreBtn.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            tvSeeAllReviews.setVisibility(View.GONE);
                            loadMoreBtn.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideLogoProgressbar(logoGif);
                    hideLogoProgressbar(loadmore_gif);
                }
            });
        }
    }

    public Map<String, String> getAstroFeedbackParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.ASROLOGER_IDD, astrologerDetailBeanData.getAstrologerId());
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));
        if (userFeedbackList.isEmpty())
            headers.put(CGlobalVariables.ASTROLOGER_LAST_ID, "0");
        else
            headers.put(CGlobalVariables.ASTROLOGER_LAST_ID, userFeedbackList.get(userFeedbackList.size() - 1).getFeedbackId());

        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        return CUtils.setRequiredParams(headers);
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine,
                                             final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    tv.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                }
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);

            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tv.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                    }
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();

                    if (viewMore) {
                        makeTextViewResizable(tv, -1, readLess, false);
                    } else {
                        makeTextViewResizable(tv, 6, readMore, true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }

    private void callSelectedAstrologer(String mobileNo, String urlTextt, boolean isFreeConsultation) {

        if (!CUtils.isConnectedWithInternet(context)) {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), context);
        } else {
            showProgressBar();
            //Log.e("SAN FEEDBACK SIZE ", "" + urlTextt + " " + mobileNo + " url " + CGlobalVariables.CALL_ASTROLOGER_URL );
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CALL_ASTROLOGER_URL,
                    AstrologerDescriptionActivity.this, false, getCallParams(mobileNo, urlTextt, isFreeConsultation), 1).getMyStringRequest();
            queue.add(stringRequest);*/

            CUtils.fcmAnalyticsEvents("connect_call_api_called", AstrosageKundliApplication.currentEventType, "");

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.connectNetworkCall(getCallParams(mobileNo, urlTextt, isFreeConsultation));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        parseConnectCallResponse(myResponse);
                    } catch (Exception e) {
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                }
            });
        }
    }

    public Map<String, String> getCallParams(String phoneNo, String urlText, boolean isFreeConsultation) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(context));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
                headers.put(KEY_PASSWORD, CUtils.getUserLoginPassword(context));
            } else {
                headers.put(CGlobalVariables.USER_PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.URL_TEXT, urlText);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.CALL_SOURCE, TAG);
        String currentOfferType = CUtils.getUserIntroOfferType(this);
        headers.put(CGlobalVariables.ISFREE_CONSULTATION, String.valueOf(isFreeConsultation));
        headers.put(CGlobalVariables.OFFER_TYPE, currentOfferType);

        headers.put(USER_SPEED, String.valueOf(CUtils.getNetworkSpeed(context)));
        //Log.e("SAN TestStatus", "params2="+headers.toString());
        return headers;
    }

    private void hideSwipToRefresh() {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }

    }

    private void parseFollowAstrologerStatus(String response, String followVal) {
        Log.d("unfolist", "parseFollowAstrologerStatus: "+response);
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");
                if (status.equals("1")) {
                    followCountVal = jsonObject.getString("astrofollowcount");
                    followCount.setText(followCountVal);
                    if (followVal != null && followVal.equals("1")) {
                        followStatus = "1";
                        handleFollowUnfollowAstrologer(true);
                    } else {
                        followStatus = "0";
                        handleFollowUnfollowAstrologer(false);
                    }
                } else if (status.equals("100")) {
                    followAstroResp100 = true;
                    LocalBroadcastManager.getInstance(AstrologerDescriptionActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                    startBackgroundLoginService();
                } else {
                    CUtils.showSnackbar(mainlayout, message, AstrologerDescriptionActivity.this);
                }
            } catch (Exception e) {
            }
        }
    }

    public void startBackgroundLoginServiceFromDialog() {
        isStartedFromNotificationDialog = true;
        LocalBroadcastManager.getInstance(AstrologerDescriptionActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
        startBackgroundLoginService();
    }


    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                if(followAstroResp100){
                    followAstroResp100 = false;
                    followUnfollowAstrolgoer();
                }

                if (isStartedFromNotificationDialog) {
                    isStartedFromNotificationDialog = false;
                    sendNotification(astrologerDetailBeanData);
                }
                if (isStartedForWallet){
                    isStartedForWallet = false;
                    getWalletPriceData();
                }
            } else {
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(AstrologerDescriptionActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };


    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(AstrologerDescriptionActivity.this)) {
                Intent intent = new Intent(AstrologerDescriptionActivity.this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onResponse(String response, int method) {
        Log.e("ASTROLOGER_DEonResponse", method + " res=> " + response);
        if (method == METHOD_RECHARGE) {
            handleWalletRechargeResponse(response);
        } else if (method == ADD_TO_QUEUE_METHOD) {
            /*try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");
                if (status.equals("100")) {
                    startBackgroundLoginServiceFromDialog();
                } else {
                    CUtils.showSnackbar(findViewById(android.R.id.content), message, this);
                }
            } catch (Exception e) {
                //
            }*/
        } else if (method == REVIEW_ACTION_METHOD) {
            //do nothing
            // Log.e("ASTROLOGER_DE",  " res=> " + response );
        } else if (method == ASTROLOGER_DETAIL_METHOD) {
            //Log.e("ASTROLOGER_DE", " onresponse" + response);
            /*try {
                hideProgressBar();
                JSONObject astrologerObj = new JSONObject(response);
                parseAstrologerDetail(astrologerObj);
                if (isdisable) {
                    Toast.makeText(context, getResources().getString(R.string.astrologer_no_lon_avail), Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                getAstrologerStatusPrice(astroId);
                fetchAstrologerFeedback(false);
            } catch (Exception e) {
                //
            }*/
        } else if (method == ASTRO_STATUS_UPDATE) {
           /* hideSwipToRefresh();
            parseAstrologerStatus(response);*/
        } else if (method == FOLLOW_ASTROLOGER_REQ) {
            /*if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            parseFollowAstrologerStatus(response);*/
        } else if (method == SEND_GIFT_REQ) {
            /*try {
                if(send != null){
                    send.setEnabled(true);
                }
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    updateWalletBalance();
                    makeGiftUnSelected();
                    CUtils.showSnackbar(findViewById(android.R.id.content), getResources().getString(R.string.gift_sucessful), this);
                    giftDialog.dismiss();
                } else {
                    CUtils.showSnackbar(giftDialog.findViewById(R.id.rlAstroDescGiftParent), getResources().getString(R.string.gift_unsucessful), context);
                }
            } catch (Exception e) {
                CUtils.showSnackbar(giftDialog.findViewById(R.id.rlAstroDescGiftParent), getResources().getString(R.string.gift_unsucessful), context);
            }
*/
        } else if (method == END_CHAT_VALUE) {
            //hideProgressBar();
            /*CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
            //Log.e("SAN CI DA ", " onResponse method == END_CHAT_VALUE "  );
            if (response != null && response.length() > 0) {
                CGlobalVariables.chatTimerTime = 0;
                AstrosageKundliApplication.chatTimerRemainingTime = 0;
                AstrosageKundliApplication.selectedAstrologerDetailBean = null;
                AstrosageKundliApplication.chatJsonObject = "";
                AstrosageKundliApplication.channelIdTempStore = "";
                CUtils.saveAstrologerIDAndChannelID(AstrologerDescriptionActivity.this, "", "");
                if (AstrosageKundliApplication.currentConsultType != null) {
                    if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.CHAT_TEXT)) {
                        stopService(new Intent(context, AstroAcceptRejectService.class));
                    } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.AUDIO_CALL_TEXT)) {
                        stopService(new Intent(context, AgoraCallInitiateService.class));
                    } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.VIDEO_CALL_TEXT)) {
                        stopService(new Intent(context, AgoraCallInitiateService.class));
                    }
                }
                chatInitiateInfoLayout.setVisibility(View.GONE);
            }
            onResume(); //to refresh busy status if call is cancelled*/
        } else {

            if (response != null && response.length() > 0) {
                try {
                    if (method == WALLET_PRICE_API) {

                        /*try {
                            JSONObject object = new JSONObject(response);
                            String status = "";
                            if (object.has("status")){
                                status = object.getString("status");
                            }
                            if (status.equalsIgnoreCase("100")){
                                startBackgroundLoginServiceForWallet();
                            } else {
                                String walletBal = object.getString("userbalancce");
                                if (walletBal != null && walletBal.length() > 0) {
                                    CUtils.setWalletRs(AstrologerDescriptionActivity.this, walletBal);
                                    walletLayout.setVisibility(View.VISIBLE);
                                    walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat(walletBal));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    } else if (method == ASTROLOGER_FEEDBACK_DATA_METHOD) {
                        Log.d("textFeedBack","fetchAstrologerFeedback Response"+response);
                        /*hideLogoProgressbar(logoGif);
                        hideLogoProgressbar(loadmore_gif);
                        // Log.e("SAN ADA Res ",  " ASTROLOGER_FEEDBACK_DATA_METHOD =>" + response);
                        JSONArray jsonArray = new JSONArray(response);
                        ArrayList<UserReviewBean> userReviewBeanArrayList = null;
                        if (jsonArray != null && jsonArray.length() > 0) {
                            userReviewBeanArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject reviewObject = jsonArray.getJSONObject(i);
                                UserReviewBean userReviewBean = new UserReviewBean();
                                if (reviewObject.has("countrycode")) {
                                    userReviewBean.setCountrycode(reviewObject.getString("countrycode"));
                                }

                                userReviewBean.setUsername(reviewObject.getString("username"));
                                userReviewBean.setMaskedusername(reviewObject.getString("maskedusername"));
                                userReviewBean.setComment(reviewObject.getString("comment"));
                                userReviewBean.setRate(reviewObject.getString("rate"));
                                userReviewBean.setDate(reviewObject.getString("date"));
                                userReviewBean.setFeedbackId(reviewObject.getString("feedbackId"));

                                userReviewBeanArrayList.add(userReviewBean);
                            }
                            if (userReviewBeanArrayList != null && userReviewBeanArrayList.size() > 0) {
                                userFeedbackList.addAll(userReviewBeanArrayList);
                                astrologerDetailBeanData.setUserReviewBeanArrayList(userFeedbackList);
                                if (astrologerUserReciewAdapter != null) {
                                    if (userReviewBeanArrayList.size() < 10) {
                                        loadMoreBtn.setVisibility(View.INVISIBLE);
                                    }
                                    astrologerUserReciewAdapter.AstrologerUserFeedbackUpdate(userFeedbackList);
                                } else {
                                    setFeedbackAdapter();
                                }
                            } else {
                                loadMoreBtn.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            tvSeeAllReviews.setVisibility(View.GONE);
                            loadMoreBtn.setVisibility(View.INVISIBLE);
                        }*/
                    } else {
                        /*hideProgressBar();
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                            final String callsId = jsonObject.getString("callsid");
                            String talkTime = jsonObject.getString("talktime");
                            final String exophoneNo = jsonObject.getString("exophone");
                            String internationalCharges = jsonObject.getString("callcharge");

                            if (freeMinutedialog != null && freeMinutedialog.isVisible()) {
                                //freeMinutedialog.dismiss();
                                CUtils.fcmAnalyticsEvents("show_call_thank_you_dialog", AstrosageKundliApplication.currentEventType, "");
                                openFreeMinDialogBox();
                            }
                            String astroName = "", profileUrl = "";
                            if (astrologerDetailBeanData != null) {
                                profileUrl = astrologerDetailBeanData.getImageFile();
                                astroName = astrologerDetailBeanData.getName();
                            }
                            CUtils.fcmAnalyticsEvents("show_call_initiate_dialog", AstrosageKundliApplication.currentEventType, "");

                            CallInitiatedDialog dialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CALL_CLICK, internationalCharges, astroName, profileUrl);
                            dialog.show(getSupportFragmentManager(), "Dialog");
                            //astrologerDetailBeanData.setIsBusy(jsonObject.getString("isBusy"));
                            astrologerIsBusy = true;
                            setAstroStatusData();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        CUtils.fcmAnalyticsEvents("start_call_status_service", AstrosageKundliApplication.currentEventType, "");
                                        Intent intent = new Intent(AstrologerDescriptionActivity.this, CallStatusCheckService.class);
                                        intent.putExtra(CGlobalVariables.CALLS_ID, callsId);
                                        context.startService(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 60000);

                        } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                            CUtils.fcmAnalyticsEvents("call_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = getResources().getString(R.string.unable_to_connect_free_consultation);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");
                            CUtils.fcmAnalyticsEvents("call_insufficient_bal_dialog", AstrosageKundliApplication.currentEventType, "");
                            InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, CALL_CLICK);
                            dialog.show(getSupportFragmentManager(), "Dialog");

                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                            String fcm_label = "call_astrologer_" + message;

                            CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");

                            String dialogMsg = "";
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                dialogMsg = getResources().getString(R.string.user_blocked_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                dialogMsg = getResources().getString(R.string.astrologer_busy_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                String stringMsg = getResources().getString(R.string.astrologer_offline_msg);
                                dialogMsg = stringMsg;
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                dialogMsg = getResources().getString(R.string.astrologer_status_disable);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                dialogMsg = getResources().getString(R.string.call_api_failed_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                dialogMsg = getResources().getString(R.string.existing_call_msg);
                            } else {
                                dialogMsg = getResources().getString(R.string.something_wrong_error);
                            }
                            callMsgDialogData(dialogMsg, "", true, CALL_CLICK);
                        } else {
                            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), context);
                        }*/

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error), context);
            }
        }
        ivAstroDescShare.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(VolleyError error) {
        handleWalletRechargeError();
        hideSwipToRefresh();
        hideProgressBar();
        CUtils.showVolleyErrorRespMessage(containerLayout, AstrologerDescriptionActivity.this, error,"9");
    }

    private void openFreeMinDialogBox() {
        try {
            //Log.e("SAN ADA", " openFreeMinDialogBox call");
            freeMinutedialog.dismiss();
            if (freeMinuteMinimizeDialog == null) {
                freeMinuteMinimizeDialog = new FreeMinuteMinimizeDialog();
            }
            freeMinuteMinimizeDialog.show(getSupportFragmentManager(), "MiniDialog");

        } catch (Exception e) {
            //Log.e("SAN ADA", " openFreeMinDialogBox exp " + e.toString());
        }
    }

    public void popUpOverPopUp() {
        try {
            if (freeMinuteMinimizeDialog != null && freeMinuteMinimizeDialog.isVisible()) {
                freeMinuteMinimizeDialog.dismiss();
//                freeMinutedialog = new FreeMinuteDialog("");
//                freeMinutedialog.show(getSupportFragmentManager(), "Dialog");
            }
        } catch (Exception e) {

        }

    }

    private void showFreeOneMinDialog() {
        try {
            String offerType = CUtils.getCallChatOfferType(this);
            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE) && CUtils.getCountryCode(AstrologerDescriptionActivity.this).equals("91")) {
                freeMinutedialog = new FreeMinuteDialog(CGlobalVariables.INTRO_OFFER_TYPE_FREE);
                freeMinutedialog.show(getSupportFragmentManager(), "Dialog");
                CUtils.fcmAnalyticsEvents("first_min_free_dialog_free_call", AstrosageKundliApplication.currentEventType, "");

            }
            String wallet = CUtils.getWalletRs(this);
            //Log.e("LoadMore wallet ", wallet);
            if ((wallet != null) && !wallet.equals("0.0")) {
                CUtils.fcmAnalyticsEvents("first_min_free_dialog", AstrosageKundliApplication.currentEventType, "");
                freeMinutedialog = new FreeMinuteDialog("");
                freeMinutedialog.show(getSupportFragmentManager(), "Dialog");
            } else {

            }
        } catch (Exception e) {
            //
        }

    }

    private void fetchAstrologerDetail(String mobileNo, String astroUrlText) {
        urlText = astroUrlText;
        try {
            JSONObject astrologerObj = CUtils.getAstrologerDetail(context,isAIAstrologer,astroUrlText);
            if (astrologerObj != null) {

                parseAstrologerDetail(astrologerObj);
                fetchAstrologerFeedback(false);
            }
        } catch (Exception e) {
            // Log.e("SAN ", " inside fetchAstrologerDetail exception " + e.toString() );
        }
    }

    private void parseAstrologerDetail(JSONObject object) {
        try {
            AstrologerDetailBean cacheAstrologerDetailBeanData = new AstrologerDetailBean();
            astroId = object.getString("ai");
            isAIAstrologer = object.optBoolean("iai");
            if (object.has("isdisable")) {
                isdisable = object.getBoolean("isdisable");
            }
            cacheAstrologerDetailBeanData.setName(object.getString("n"));
            cacheAstrologerDetailBeanData.setExperience(object.getString("ex"));
            cacheAstrologerDetailBeanData.setLanguage(object.getString("ln"));
            parseAstrologerLanguages(object.getString("ln"));
            cacheAstrologerDetailBeanData.setImageFile(object.getString("if"));
            cacheAstrologerDetailBeanData.setDesignation(object.getString("dg"));
            cacheAstrologerDetailBeanData.setServicePrice(object.getString("sp"));
            cacheAstrologerDetailBeanData.setRating(object.getString("rc"));
            cacheAstrologerDetailBeanData.setEmail(object.getString("e"));
            cacheAstrologerDetailBeanData.setUrlText(object.getString("urlText"));
           // Log.d("sdsadsadasdasd","urltext==>"+object.getString("urlText"));
            cacheAstrologerDetailBeanData.setPhoneNumber(object.getString("ph"));
            cacheAstrologerDetailBeanData.setIsOnline(object.getString("io"));
            cacheAstrologerDetailBeanData.setIsBusy(object.getString("ib"));
            cacheAstrologerDetailBeanData.setDoubleRating(object.getString("r"));
            cacheAstrologerDetailBeanData.setAstroWalletId(object.getString("wi"));
            cacheAstrologerDetailBeanData.setAstrologerId(object.getString("ai"));
            cacheAstrologerDetailBeanData.setTotalRating(object.getString("tr"));
            cacheAstrologerDetailBeanData.setRatingNote(object.optString("ratingnote"));

            if (useIntroOffer == 1) { //true
                cacheAstrologerDetailBeanData.setUseIntroOffer(true);
            } else if (useIntroOffer == 2) { //false
                cacheAstrologerDetailBeanData.setUseIntroOffer(false);
            } else { //direct open
                cacheAstrologerDetailBeanData.setUseIntroOffer(object.optBoolean("iof"));
            }

            cacheAstrologerDetailBeanData.setIntroOfferType(CUtils.getUserIntroOfferType(this));

            if (object.has("iv")) {
                cacheAstrologerDetailBeanData.setIsVerified(object.getString("iv"));
               // Log.d("setIsVerified","setIsVerified==>>>"+object.getString("iv"));
            }

            if (object.has("asp")) {
                cacheAstrologerDetailBeanData.setActualServicePriceInt(object.getString("asp"));
            }

            if (object.has("iavc")) {
                cacheAstrologerDetailBeanData.setIsAvailableForChat(object.getString("iavc"));
            }

            if (object.has("iavp")) {
                cacheAstrologerDetailBeanData.setIsAvailableForCall(object.getString("iavp"));
            }
            if (object.has("iav")) {
                cacheAstrologerDetailBeanData.setIsAvailableForVideoCall(object.getString("iav"));
               // Log.d("asdadsasd", object.getString("iav"));
                //cacheAstrologerDetailBeanData.setIsAvailableForVideoCall("true");
            }

            cacheAstrologerDetailBeanData.setAiAstrologerId(object.optString("aiai"));

            astrologerBioModelArrayList = new ArrayList<>();

            if (object.has("et")) {
                if (object.getString("et") != null &&
                        object.getString("et").length() > 0) {
                    cacheAstrologerDetailBeanData.setExpertise(object.getString("et"));
                    AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                    astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.expertise));
                    astrologerBioModel.setAstrologerBioDescription(object.getString("et"));
                    astrologerBioModel.setImageFile(R.drawable.ic_expertise);
                    astrologerBioModelArrayList.add(astrologerBioModel);
                }
            }
            if(object.has("pet")){
                cacheAstrologerDetailBeanData.setPrimaryExpertise(object.getString("pet"));
            }
            if (object.has("education")) {
                if (object.getString("education") != null &&
                        object.getString("education").length() > 0) {
                    cacheAstrologerDetailBeanData.setEducation(object.getString("education"));
                    AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                    astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.education));
                    astrologerBioModel.setAstrologerBioDescription(object.getString("education"));
                    astrologerBioModel.setImageFile(R.drawable.ic_graduation);
                    astrologerBioModelArrayList.add(astrologerBioModel);
                }
            }

            if (object.has("focusareas")) {
                if (object.getString("focusareas") != null &&
                        object.getString("focusareas").length() > 0) {
                    cacheAstrologerDetailBeanData.setFocusareas(object.getString("focusareas"));
                    AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                    astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.focus_area));
                    astrologerBioModel.setAstrologerBioDescription(object.getString("focusareas"));
                    astrologerBioModel.setImageFile(R.drawable.ic_focus_large);
                    astrologerBioModelArrayList.add(astrologerBioModel);
                }
            }

            if (object.has("background")) {
                if (object.getString("background") != null &&
                        object.getString("background").length() > 0) {
                    cacheAstrologerDetailBeanData.setBackground(object.getString("background"));
                    AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                    astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.background));
                    astrologerBioModel.setAstrologerBioDescription(object.getString("background"));
                    astrologerBioModel.setImageFile(R.drawable.ic_background);
                    astrologerBioModelArrayList.add(astrologerBioModel);
                }
            }

            if (object.has("career")) {
                if (object.getString("career") != null &&
                        object.getString("career").length() > 0) {
                    cacheAstrologerDetailBeanData.setCareer(object.getString("career"));
                    AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                    astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.career));
                    astrologerBioModel.setAstrologerBioDescription(object.getString("career"));
                    astrologerBioModel.setImageFile(R.drawable.ic_career);
                    astrologerBioModelArrayList.add(astrologerBioModel);
                }
            }

            if (object.has("hobbies")) {
                if (object.getString("hobbies") != null &&
                        object.getString("hobbies").length() > 0) {
                    cacheAstrologerDetailBeanData.setHobbies(object.getString("hobbies"));
                    AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                    astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.hobbies));
                    astrologerBioModel.setAstrologerBioDescription(object.getString("hobbies"));
                    astrologerBioModel.setImageFile(R.drawable.ic_hobbies);
                    astrologerBioModelArrayList.add(astrologerBioModel);
                }
            }

            if (object.has("achievements")) {
                if (object.getString("achievements") != null &&
                        object.getString("achievements").length() > 0) {
                    cacheAstrologerDetailBeanData.setAchievements(object.getString("achievements"));
                    AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                    astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.achievements));
                    astrologerBioModel.setAstrologerBioDescription(object.getString("achievements"));
                    astrologerBioModel.setImageFile(R.drawable.ic_achievement);
                    astrologerBioModelArrayList.add(astrologerBioModel);
                }
            }

            if (object.has("abilities")) {
                if (object.getString("abilities") != null &&
                        object.getString("abilities").length() > 0) {
                    cacheAstrologerDetailBeanData.setAbilities(object.getString("abilities"));
                    AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                    astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.abilities));
                    astrologerBioModel.setAstrologerBioDescription(object.getString("abilities"));
                    astrologerBioModel.setImageFile(R.drawable.ic_abilities);
                    astrologerBioModelArrayList.add(astrologerBioModel);
                }
            }

            if (object.has("enablefeedbacks")) {
                cacheAstrologerDetailBeanData.setEnablefeedbacks(object.getString("enablefeedbacks"));
            } else {
                cacheAstrologerDetailBeanData.setEnablefeedbacks("false");
            }

            AstrologerDetailBean detailBean = null;
            ArrayList<AstrologerDetailBean> astrologerDetailBeans = AppDataSingleton.getInstance().getAllAstrologerList();
            if (astrologerDetailBeans != null) {
                String astroUrlText1 = cacheAstrologerDetailBeanData.getUrlText();
                for (int i = 0; i < astrologerDetailBeans.size(); i++) {
                    detailBean = astrologerDetailBeans.get(i);
                    String astroUrlText2 = detailBean.getUrlText();
                    if (astroUrlText2 == null) continue;
                    if (astroUrlText1.equalsIgnoreCase(astroUrlText2)) {
                        break;
                    }
                }
            }
            if (detailBean != null) {//update info from main data
                cacheAstrologerDetailBeanData.setActualServicePriceInt(detailBean.getActualServicePriceInt());
                cacheAstrologerDetailBeanData.setIsAvailableForCall(detailBean.getIsAvailableForCall());
                cacheAstrologerDetailBeanData.setIsAvailableForVideoCall(detailBean.getIsAvailableForVideoCall());
                cacheAstrologerDetailBeanData.setIsAvailableForChat(detailBean.getIsAvailableForChat());
                cacheAstrologerDetailBeanData.setIsBusy(detailBean.getIsBusy());
                cacheAstrologerDetailBeanData.setServicePrice(detailBean.getServicePrice());
                cacheAstrologerDetailBeanData.setUseIntroOffer(detailBean.getUseIntroOffer());
                cacheAstrologerDetailBeanData.setIntroOfferType(detailBean.getIntroOfferType());
                cacheAstrologerDetailBeanData.setIsOnline(detailBean.getIsOnline());
            }

            astrologerDetailBeanData = cacheAstrologerDetailBeanData;

            setData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ASTROLOGER_DE", "exp1=" + e);
        }
    }

    // added by shreyans to show similar astrologers filtered by language
    private void parseAstrologerLanguages(String languages) {
        astrologerLanguageList = new ArrayList<>();
        languages = languages.replace(" ", "");
        if (!languages.contains(",")) {
            astrologerLanguageList.add(languages);
        } else {
            int length = languages.length();
            String newLang = "";
            for (int i = 0; i < length; i++) {
                String nextChar = String.valueOf(languages.charAt(i));
                if (!nextChar.equals(",")) {
                    newLang += String.valueOf(languages.charAt(i));
                } else {
                    astrologerLanguageList.add(newLang);
                    System.out.println(newLang);
                    newLang = "";
                }

                if (i == length - 1) {
                    astrologerLanguageList.add(newLang);
                    newLang = "";
                }
            }

        }
    }

    private void parseAstrologerStatus(String response) {
        try {
            Log.e("TestAI","response="+response);
            JSONObject jsonObject = new JSONObject(response);
            isAIAstrologer = jsonObject.optBoolean("isaiastro");
            Log.e("TestAI","isAIAstrologer="+isAIAstrologer);
            if (jsonObject.has("astrofollowcount")) {
                followCountVal = jsonObject.getString("astrofollowcount");
                followCount.setText(followCountVal);
            }
            if (jsonObject.has("followbyuser")) {
                followStatus = jsonObject.getString("followbyuser");
                if (followStatus != null && followStatus.equals("0")) {
                    handleFollowUnfollowAstrologer(false);
                    if(isFromNotification){
                        CUtils.unSubscribeFollowTopic(context, astroId);
                        isFromNotification = false;
                    }
                    if(!isFollowDialogShown){
                        try {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    openFollowDialog();
                                }
                            },CGlobalVariables.SHOW_FOLLOW_DIALOG_TIME_MS);
                        } catch (Exception e) {
                            //
                        }
                    }
                } else {
                    handleFollowUnfollowAstrologer(true);
                }
            }
            followAstrologer.setEnabled(true);
            followAstrologer.setClickable(true);

            if (!onResumeFlag) {
                if (astrologerBioModelArrayList != null) {
                    if (astrologerBioModelArrayList.size() > 0) {
                        astrologerBioModelArrayList.clear();
                    }
                }

                if (jsonObject.has("education")) {
                    if (jsonObject.getString("education") != null &&
                            jsonObject.getString("education").length() > 0) {
                        astrologerDetailBeanData.setEducation(jsonObject.getString("education"));
                        AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                        astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.education));
                        astrologerBioModel.setAstrologerBioDescription(jsonObject.getString("education"));
                        astrologerBioModel.setImageFile(R.drawable.ic_graduation);
                        astrologerBioModelArrayList.add(astrologerBioModel);
                    }
                }

                if (jsonObject.has("focusareas")) {
                    if (jsonObject.getString("focusareas") != null &&
                            jsonObject.getString("focusareas").length() > 0) {
                        astrologerDetailBeanData.setFocusareas(jsonObject.getString("focusareas"));
                        AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                        astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.focus_area));
                        astrologerBioModel.setAstrologerBioDescription(jsonObject.getString("focusareas"));
                        astrologerBioModel.setImageFile(R.drawable.ic_focus_large);
                        astrologerBioModelArrayList.add(astrologerBioModel);
                    }
                }

                if (jsonObject.has("background")) {
                    if (jsonObject.getString("background") != null &&
                            jsonObject.getString("background").length() > 0) {
                        astrologerDetailBeanData.setBackground(jsonObject.getString("background"));
                        AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                        astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.background));
                        astrologerBioModel.setAstrologerBioDescription(jsonObject.getString("background"));
                        astrologerBioModel.setImageFile(R.drawable.ic_background);
                        astrologerBioModelArrayList.add(astrologerBioModel);
                    }
                }

                if (jsonObject.has("career")) {
                    if (jsonObject.getString("career") != null &&
                            jsonObject.getString("career").length() > 0) {
                        astrologerDetailBeanData.setCareer(jsonObject.getString("career"));
                        AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                        astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.career));
                        astrologerBioModel.setAstrologerBioDescription(jsonObject.getString("career"));
                        astrologerBioModel.setImageFile(R.drawable.ic_career);
                        astrologerBioModelArrayList.add(astrologerBioModel);
                    }
                }

                if (jsonObject.has("hobbies")) {
                    if (jsonObject.getString("hobbies") != null &&
                            jsonObject.getString("hobbies").length() > 0) {
                        astrologerDetailBeanData.setHobbies(jsonObject.getString("hobbies"));
                        AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                        astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.hobbies));
                        astrologerBioModel.setAstrologerBioDescription(jsonObject.getString("hobbies"));
                        astrologerBioModel.setImageFile(R.drawable.ic_hobbies);
                        astrologerBioModelArrayList.add(astrologerBioModel);
                    }
                }

                if (jsonObject.has("achievements")) {
                    if (jsonObject.getString("achievements") != null &&
                            jsonObject.getString("achievements").length() > 0) {
                        astrologerDetailBeanData.setAchievements(jsonObject.getString("achievements"));
                        AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                        astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.achievements));
                        astrologerBioModel.setAstrologerBioDescription(jsonObject.getString("achievements"));
                        astrologerBioModel.setImageFile(R.drawable.ic_achievement);
                        astrologerBioModelArrayList.add(astrologerBioModel);
                    }
                }

                if (jsonObject.has("abilities")) {
                    if (jsonObject.getString("abilities") != null &&
                            jsonObject.getString("abilities").length() > 0) {
                        astrologerDetailBeanData.setAbilities(jsonObject.getString("abilities"));
                        AstrologerBioModel astrologerBioModel = new AstrologerBioModel();
                        astrologerBioModel.setAstrologerBioName(getResources().getString(R.string.abilities));
                        astrologerBioModel.setAstrologerBioDescription(jsonObject.getString("abilities"));
                        astrologerBioModel.setImageFile(R.drawable.ic_abilities);
                        astrologerBioModelArrayList.add(astrologerBioModel);
                    }
                }
            }

            if (jsonObject.has("enablefeedbacks")) {
                astrologerDetailBeanData.setEnablefeedbacks(jsonObject.getString("enablefeedbacks"));
            } else {
                astrologerDetailBeanData.setEnablefeedbacks("false");
            }

            if (jsonObject.has("actualServicePriceInt")) {
                astrologerDetailBeanData.setActualServicePriceInt(jsonObject.getString("actualServicePriceInt"));
            }

            astrologerDetailBeanData.setServicePrice(jsonObject.getString("price"));
            astrologerDetailBeanData.setIsAvailableForChat(jsonObject.getString("isAvailableForChat"));
            astrologerDetailBeanData.setIsAvailableForCall(jsonObject.getString("isAvailableForCall"));
            astrologerDetailBeanData.setIsAvailableForVideoCall(jsonObject.getString("isAvailableForVideo"));
            //Log.d("asdadsasd", jsonObject.getString("isAvailableForVideo"));
            // astrologerDetailBeanData.setIsAvailableForVideoCall("true");
            astrologerDetailBeanData.setIsBusy(jsonObject.getString("isBusy"));
            if (jsonObject.has("waitTime")) {
                astrologerDetailBeanData.setWaitTime(jsonObject.getString("waitTime"));
            }
            astrologerDetailBeanData.setIsOnline(jsonObject.optString("isOnline"));

            if (useIntroOffer == 1) {//1 means true
                astrologerDetailBeanData.setUseIntroOffer(true);
            } else if (useIntroOffer == 2) {// 2 means false
                astrologerDetailBeanData.setUseIntroOffer(false);
            }

                astrologerDetailBeanData.setUseIntroOffer(jsonObject.optBoolean("useIntroOffer"));
                astrologerDetailBeanData.setIntroOfferType(jsonObject.optString("offertype"));

            astrologerDetailBeanData.setAstroSingleTimeOffer(jsonObject.optBoolean("astrosingletimeoffer"));
            astrologerDetailBeanData.setDesignation(jsonObject.getString("designation"));
            astrologerDetailBeanData.setDescription(jsonObject.getString("description"));
            astrologerDetailBeanData.setCity(jsonObject.getString("city"));
            astrologerDetailBeanData.setState(jsonObject.getString("state"));
            astrologerDetailBeanData.setCountry(jsonObject.getString("country"));
            astrologerDetailBeanData.setAvailableTimeForCall(jsonObject.getString("availableTimeForCall"));
            astrologerDetailBeanData.setImageFileLarge(jsonObject.getString("imageFileLarge"));
            astrologerDetailBeanData.setIsastrologerlive(jsonObject.getString("isastrologerlive"));
            astrologerDetailBeanData.setAcceptInternetCall(jsonObject.optInt("acceptinternetcall"));
            if (jsonObject.has("currentuserfeedback")) {
                JSONArray jsonArray = null;
                JSONObject reviewObject = null;
                UserReviewBean userOwnReviewBean = null;
                try {

                    jsonArray = jsonObject.getJSONArray("currentuserfeedback");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        userOwnReviewBean = new UserReviewBean();
                        reviewObject = jsonArray.getJSONObject(0);
                        userOwnReviewBean.setUsername(reviewObject.getString("username"));
                        userOwnReviewBean.setMaskedusername(reviewObject.getString("maskedusername"));
                        userOwnReviewBean.setComment(reviewObject.getString("comment"));
                        //userOwnReviewBean.setUserRatingType(reviewObject.getString("ratingname"));
                        userOwnReviewBean.setRate(reviewObject.getString("rate"));
                        userOwnReviewBean.setDate(reviewObject.getString("date"));
                        userOwnReviewBean.setFeedbackId(reviewObject.getString("feedbackId"));

                        astrologerDetailBeanData.setUserOwnReviewModel(userOwnReviewBean);
                    } else {
                        astrologerDetailBeanData.setUserOwnReviewModel(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    astrologerDetailBeanData.setUserOwnReviewModel(null);
                }
            }
            setAstroStatusData();
            setData();

            if (followStatus != null && followStatus.equals("0")) {
                handleFollowUnfollowAstrologer(false);
                if (hasFollow && getUserLoginStatus(this)) {
                    followAstrologerRequest("1");
                }
            } else {
                handleFollowUnfollowAstrologer(true);
            }

        } catch (Exception e) {
            Log.e("ASTROLOGER_DE", "exp2=" + e);
        }
    }

    private void handleFollowUnfollowAstrologer(boolean doFollowAndSubscribe){
        if(doFollowAndSubscribe){
            followAstrologer.setText(getResources().getString(R.string.following));
            CUtils.subscribeFollowTopic(context, astroId);
        } else {
            followAstrologer.setText(getResources().getString(R.string.follow));
            CUtils.unSubscribeFollowTopic(context, astroId);
        }
    }

    private void feedbackAstro(JSONObject object) {

        //

        if (object.has("feedbacks")) {
            JSONArray jsonArray = null;
            ArrayList<UserReviewBean> userReviewBeanArrayList = null;
            try {
                jsonArray = object.getJSONArray("feedbacks");

                if (jsonArray != null && jsonArray.length() > 0) {
                    userReviewBeanArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject reviewObject = jsonArray.getJSONObject(i);
                        UserReviewBean userReviewBean = new UserReviewBean();
                        if (reviewObject.has("countrycode")) {
                            userReviewBean.setCountrycode(reviewObject.getString("countrycode"));
                        }
                        userReviewBean.setUsername(reviewObject.getString("username"));
                        userReviewBean.setMaskedusername(reviewObject.getString("maskedusername"));
                        userReviewBean.setComment(reviewObject.getString("comment"));
                        userReviewBean.setRate(reviewObject.getString("rate"));
                        userReviewBean.setDate(reviewObject.getString("date"));
                        userReviewBean.setFeedbackId(reviewObject.getString("feedbackId"));

                        userReviewBeanArrayList.add(userReviewBean);
                    }
                    astrologerDetailBeanData.setUserReviewBeanArrayList(userReviewBeanArrayList);
                } else {
                    astrologerDetailBeanData.setUserReviewBeanArrayList(null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setData() {

        AstrologerDetailBean astrologerDetailBean = astrologerDetailBeanData;

        if (astrologerDetailBean == null) {
            return;
        }

        llBottomLayout.setVisibility(View.VISIBLE);
        containerLayout.setVisibility(View.VISIBLE);
        callNowLayout.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(astrologerDetailBean.getRatingNote())){
            share_your_experience_notes.setText(astrologerDetailBean.getRatingNote());
        }
        if (astrologerDetailBean.getIsVerified().equalsIgnoreCase("true")) {
            String imageUrl = CGlobalVariables.VERIFIED_IMAGE_URL + CUtils.getLanguageKey(LANGUAGE_CODE) + ".png";
            verifiedImg.setVisibility(View.VISIBLE);
        } else {
            verifiedImg.setVisibility(View.GONE);
        }
        String is_astrologerBusy = astrologerDetailBean.getIsBusy();
        setCallChatButtonStatus(astrologerDetailBean, is_astrologerBusy);

        if (astrologerDetailBean.getDoubleRating() != null && astrologerDetailBean.getDoubleRating().length() > 0) {
            if (astrologerDetailBean.getDoubleRating().equalsIgnoreCase("0.0") ||
                    astrologerDetailBean.getRating().equalsIgnoreCase("0")) {
                ratingLayout.setVisibility(View.INVISIBLE);
            } else {
                ratingLayout.setVisibility(View.VISIBLE);
                ratingTxt.setText(astrologerDetailBean.getDoubleRating());
                ratingStar.setRating(Float.parseFloat(astrologerDetailBean.getDoubleRating()));
            }
        }

        //LayerDrawable stars = (LayerDrawable) ratingStar.getProgressDrawable();
        //stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);


        String expStr = getResources().getString(R.string.year_of_experiance_full).replace("#", astrologerDetailBean.getExperience());
        experienceTxt.setText(expStr);

        /*String quePrice = getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
        consultaionValueTxt.setText(quePrice);*/


        int actualPrice = 0;
        if (astrologerDetailBean.getActualServicePriceInt() != null && astrologerDetailBean.getActualServicePriceInt().length() > 0) {
            actualPrice = Integer.parseInt(astrologerDetailBean.getActualServicePriceInt());
        }

        int servicePrice = 0;
        if (astrologerDetailBean.getServicePrice() != null && astrologerDetailBean.getServicePrice().length() > 0) {
            servicePrice = Integer.parseInt(astrologerDetailBean.getServicePrice());
        }

        // actualConsultaionValueTxt.setText(actualPrice);
        if (actualPrice > servicePrice) {
            //show both prices
            actualConsultaionValueTxt.setVisibility(View.VISIBLE);
            String price = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getActualServicePriceInt());
            CUtils.setStrikeOnTextView(actualConsultaionValueTxt, price);
            //actualConsultaionValueTxt.setText(price);
            //actualConsultaionValueTxt.setPaintFlags(actualConsultaionValueTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            consultaionValueTxt.setVisibility(View.VISIBLE);
            String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
            consultaionValueTxt.setText(quePrice);
            String imageUrl = CGlobalVariables.OFFER_IMAGE_URL + CUtils.getLanguageKey(LANGUAGE_CODE) + ".png";
            offerImg.setImageUrl(CUtils.getOfferImageLarge(), VolleySingleton.getInstance(AstrologerDescriptionActivity.this).getImageLoader());
            offerImg.setVisibility(View.VISIBLE);
        } else {
            // show only one price(servicePriceInt)
            String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
            /*try {
                Drawable img = context.getResources().getDrawable(R.drawable.rupee_icon);
                consultaionValueTxt.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }*/
            consultaionValueTxt.setText(quePrice);
            actualConsultaionValueTxt.setVisibility(View.GONE);
            consultaionValueTxt.setVisibility(View.VISIBLE);
            offerImg.setVisibility(View.GONE);
        }
        if(isAIAstrologer && CUtils.isKundliAiProPlan(context)){
//            new_user.setVisibility(View.VISIBLE);
//            new_user.setText(context.getResources().getString(R.string.text_free));
//            String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
//            CUtils.setStrikeOnTextView(consultaionValueTxt, quePrice);
//            //consultaionValueTxt.setText(quePrice);
//            //consultaionValueTxt.setPaintFlags(consultaionValueTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            consultaionValueTxt.setVisibility(View.VISIBLE);
//            actualConsultaionValueTxt.setVisibility(View.GONE);
            chatNowBtnTxt.setText(context.getResources().getString(R.string.free_chat_now));
            FontUtils.changeFont(context, chatNowBtnTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, callNowBtnTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            chatNowBtnTxt.setTextSize(18);
            callNowBtnTxt.setTextSize(18);
        }

            if (astrologerDetailBean.getUseIntroOffer()) {
                new_user.setVisibility(View.VISIBLE);
                String offerType = astrologerDetailBean.getIntroOfferType();
                //Log.d("AstroData", "offerType2="+offerType);
                if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    new_user.setText(context.getResources().getString(R.string.text_free));
                    String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                    CUtils.setStrikeOnTextView(consultaionValueTxt, quePrice);
                    //consultaionValueTxt.setText(quePrice);
                    //consultaionValueTxt.setPaintFlags(consultaionValueTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    consultaionValueTxt.setVisibility(View.VISIBLE);
                    actualConsultaionValueTxt.setVisibility(View.GONE);
                    chatNowBtnTxt.setText(context.getResources().getString(R.string.chat_now));
                } else if (!TextUtils.isEmpty(offerType) && offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {
                    CUtils.removeStrikeOnTextView(consultaionValueTxt);
                    //consultaionValueTxt.setPaintFlags(consultaionValueTxt.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    new_user.setText(context.getResources().getString(R.string.new_user));
                }
                offerImg.setImageUrl(CUtils.getOfferImageLarge(), VolleySingleton.getInstance(AstrologerDescriptionActivity.this).getImageLoader());
                offerImg.setVisibility(View.VISIBLE);
            } else if (astrologerDetailBean.isAstroSingleTimeOffer()) {
                new_user.setVisibility(View.VISIBLE);
                new_user.setText(context.getResources().getString(R.string.text_free));
                String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                CUtils.setStrikeOnTextView(consultaionValueTxt, quePrice);
                consultaionValueTxt.setVisibility(View.VISIBLE);
                actualConsultaionValueTxt.setVisibility(View.GONE);
                offerImg.setImageUrl(CUtils.getOfferImageLarge(), VolleySingleton.getInstance(AstrologerDescriptionActivity.this).getImageLoader());
                offerImg.setVisibility(View.VISIBLE);
                if (!astrologerDetailBean.isBusyBool()) chatNowBtnTxt.setText(R.string.free_chat);
            } else {
                new_user.setVisibility(View.GONE);
                CUtils.removeStrikeOnTextView(consultaionValueTxt);
                //consultaionValueTxt.setPaintFlags(consultaionValueTxt.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                //offerImg.setImageResource(R.drawable.icon_offer);
            }

        if (new_user.getVisibility() == View.VISIBLE) {
            if (!new_user.getText().toString().trim().equals(context.getResources().getString(R.string.text_free))) {
                videoCallText.setText(context.getResources().getString(R.string.video_call_at) + " " + new_user.getText().toString().trim() + " " + consultaionValueTxt.getText().toString().trim());
            } else {

                videoCallText.setText(context.getResources().getString(R.string.video_call_at) + " " + new_user.getText().toString().trim());
            }
        } else {
            videoCallText.setText(context.getResources().getString(R.string.video_call_at) + " " + consultaionValueTxt.getText().toString().trim());
        }
        //videoCallText.setText(context.getResources().getString(R.string.video_call_at));
        vedicAstrologerTxt.setText(astrologerDetailBean.getDesignation());
        languageTxt.setText(astrologerDetailBean.getLanguage());
        astrologerNameTxt.setText(astrologerDetailBean.getName());

        String waitTime = context.getResources().getString(R.string.waitTimeText).replace("#", astrologerDetailBean.getWaitTime());

        /*if (!astrologerDetailBean.getWaitTime().equals("0")) {
            tvAstroDescWaitTime.setText(waitTime);
            tvAstroDescWaitTime.setVisibility(View.VISIBLE);
        } else {
            tvAstroDescWaitTime.setVisibility(View.GONE);
        }*/

        if (astrologerDetailBean.getTotalRating().equalsIgnoreCase("0")) {
            rating_tag_open.setVisibility(View.GONE);
            rating_tag_close.setVisibility(View.GONE);
            rating_tag_img.setVisibility(View.GONE);
            totalRatingTxt.setVisibility(View.GONE);
        } else {
            rating_tag_open.setVisibility(View.VISIBLE);
            rating_tag_close.setVisibility(View.VISIBLE);
            rating_tag_img.setVisibility(View.VISIBLE);
            totalRatingTxt.setVisibility(View.VISIBLE);
            totalRatingTxt.setText(astrologerDetailBean.getTotalRating());
        }

        String astrologerProfileUrl = "";
        if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
            //Picasso.with(context).load(astrologerProfileUrl).into(profileImg);
           // profileImg.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(profileImg);

           // Glide.with(context).
        }
        if(!astrologerDetailBean.getImageFileLarge().isEmpty()){

            final String astrologerProfileLargeUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFileLarge();
            Glide.with(getApplicationContext()).load(astrologerProfileLargeUrl).diskCacheStrategy(DiskCacheStrategy.ALL).preload();
            profileImg.setOnClickListener(v -> {
                FullProfileFragment fragment = FullProfileFragment.getInstant(astrologerDetailBean.getName(),astrologerProfileLargeUrl);
                fragment.show(getSupportFragmentManager(),FullProfileFragment.TAG);
            });
        }

        if (astrologerDetailBean.getDescription() != null && astrologerDetailBean.getDescription().trim().length() > 0) {
            aboutAstrologerHeadingTxt.setVisibility(View.VISIBLE);
            astrologer_description_dummy_layout.setVisibility(View.VISIBLE);
            if (astrologer_description_dummy.getVisibility() != View.VISIBLE) {
                astrologerDescription.setVisibility(View.VISIBLE);
                bio_recyclerview.setVisibility(View.VISIBLE);
            } else {
                astrologerDescription.setVisibility(View.GONE);
                bio_recyclerview.setVisibility(View.GONE);
            }
            astrologerDescription.setText(Html.fromHtml(astrologerDetailBean.getDescription()));
            astrologer_description_dummy.setText(Html.fromHtml(astrologerDetailBean.getDescription()));
            if (astrologerBioModelArrayList != null && astrologerBioModelArrayList.size() > 0) {
                astrologerBioAdapter = new AstrologerBioAdapter(context, astrologerBioModelArrayList);
                bio_recyclerview.setAdapter(astrologerBioAdapter);
            }

            //makeTextViewResizable(astrologerDescription, 6, readMore, true);
        } else {
            aboutAstrologerHeadingTxt.setVisibility(View.GONE);
            astrologerDescription.setVisibility(View.GONE);
            astrologer_description_dummy_layout.setVisibility(View.GONE);
        }

        /*if (astrologerDetailBean.getExpertise() != null && astrologerDetailBean.getExpertise().length() > 0) {
            expertiseTxt.setText(getResources().getString(R.string.expertise) + " ");
            expertiseValueTxt.setText(astrologerDetailBean.getExpertise());
            expertiseLayout.setVisibility(View.VISIBLE);
        } else {
            expertiseLayout.setVisibility(View.GONE);
        }*/

        expertiseLayout.setVisibility(View.GONE);

        boolean isLogin = CUtils.getUserLoginStatus(context);
        if (astrologerDetailBean.getEnablefeedbacks() != null && astrologerDetailBean.getEnablefeedbacks().length() > 0) {
            if (astrologerDetailBean.getEnablefeedbacks().equalsIgnoreCase("true") && isLogin) {
                isShowFeedbackDialog = true;
                feedbackLayout.setVisibility(View.VISIBLE);
            } else {
                feedbackLayout.setVisibility(View.GONE);
            }
        } else {
            feedbackLayout.setVisibility(View.GONE);
        }

        /*if (message != null && message.length() > 0 && title != null && title.length() > 0) {

            if (isShowFeedbackDialog) {
                showRatingDialogToUser();
            }
        }*/

        getAstrologerList();
        try {
            if (astrologerDetailBean.getIsastrologerlive().equals("true")) {
                rlLive.setVisibility(View.VISIBLE);
                Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.blinking_animation);
                online_offline_img1.startAnimation(startAnimation);
            } else {
                rlLive.setVisibility(View.GONE);
            }
            rlLive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fromLiveActivity) {
                        finish();
                    } else {
                        Intent intent = new Intent(context, AllLiveAstrologerActivity.class);
                        intent.putExtra("astroProfileUrl", urlText);
                        startActivity(intent);
                    }
                }
            });

        } catch (Exception e) {
            //
        }

    }

    private void setCallChatButtonStatus(AstrologerDetailBean astrologerDetailBean, String is_astrologerBusy) {
        callNowBtn.setEnabled(true);
        String is_astrologerAvailabaleForCall = astrologerDetailBean.getIsAvailableForCall();
        String is_astrologerAvailabaleForChat = astrologerDetailBean.getIsAvailableForChat();
        String is_astrologerAvailabaleForVideoCall = astrologerDetailBean.getIsAvailableForVideoCall();
        if (is_astrologerBusy.equalsIgnoreCase("true")) {

            if (is_astrologerAvailabaleForCall.equalsIgnoreCase("true")
                    && is_astrologerAvailabaleForChat.equalsIgnoreCase("true")) {

                callNowBtnTxt.setText(context.getResources().getString(R.string.busy));
                callNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                callNowBtnTxt.setTextColor(Color.WHITE);
                setBottomActionIcon(ivCallNow, R.drawable.call_white_logo_large, R.color.white);


                chatNowBtnTxt.setText(context.getResources().getString(R.string.busy));
                chatNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                chatNowBtnTxt.setTextColor(Color.WHITE);
                setBottomActionIcon(ivChatNow, R.drawable.chat_white_logo_large, R.color.white);
                callNowBtn.setVisibility(View.VISIBLE);
                chatNowBtn.setVisibility(View.VISIBLE);
                hideCallBtn();
            } else if (is_astrologerAvailabaleForCall.equalsIgnoreCase("true")
                    && is_astrologerAvailabaleForChat.equalsIgnoreCase("false")) {

                callNowBtnTxt.setText(context.getResources().getString(R.string.busy));
                callNowBtn.setBackgroundResource(R.drawable.bg_button_red);


                callNowBtnTxt.setTextColor(Color.WHITE);
                setBottomActionIcon(ivCallNow, R.drawable.call_white_logo_large, R.color.white);
                chatNowBtn.setVisibility(View.GONE);
                hideCallBtn();
            } else if (is_astrologerAvailabaleForCall.equalsIgnoreCase("false")
                    && is_astrologerAvailabaleForChat.equalsIgnoreCase("true")) {

                chatNowBtnTxt.setText(context.getResources().getString(R.string.busy));
                chatNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                chatNowBtnTxt.setTextColor(Color.WHITE);
                setBottomActionIcon(ivChatNow, R.drawable.chat_white_logo_large, R.color.white);
                chatNowBtn.setVisibility(View.VISIBLE);
                callNowBtn.setVisibility(View.GONE);
                chatNowBtn.setVisibility(View.VISIBLE);
                hideCallBtn();
            } else if (is_astrologerAvailabaleForCall.equalsIgnoreCase("false")
                    && is_astrologerAvailabaleForChat.equalsIgnoreCase("false")) {

                //chatNowBtnTxt.setText(context.getResources().getString(R.string.offline));
                chatNowBtnTxt.setText(context.getResources().getString(R.string.busy));
                chatNowBtn.setVisibility(View.VISIBLE);
                chatNowBtn.setBackgroundResource(R.drawable.bg_button_gray);
                callNowBtn.setVisibility(View.GONE);
                chatNowBtnTxt.setTextColor(Color.WHITE);
                setBottomActionIcon(ivChatNow, R.drawable.chat_white_logo_large, R.color.white);
                if (is_astrologerAvailabaleForVideoCall.equalsIgnoreCase("true")) {
                    chatNowBtn.setVisibility(View.GONE);
                }

            }
            /*callNowBtnTxt.setText("  "+context.getResources().getString(R.string.call_now)+"  ");
            callNowBtn.setBackgroundResource(R.drawable.bg_button_red);
            chatNowBtnTxt.setText("  "+context.getResources().getString(R.string.chat_now)+"  ");
            chatNowBtn.setBackgroundResource(R.drawable.bg_button_red);
            callNowBtn.setVisibility(View.VISIBLE);
            chatNowBtn.setVisibility(View.VISIBLE);*/

            if (is_astrologerAvailabaleForVideoCall.equalsIgnoreCase("true")) {
                videoCallView.setVisibility(View.VISIBLE);
                videoCallView.setBackground(ContextCompat.getDrawable(context, R.drawable.shadow_red_outline));
                videoCallText.setTextColor(ContextCompat.getColor(context, R.color.no_change_black));
                ll_video_busy.setVisibility(View.VISIBLE);
                //videoCallIcon.setColorFilter(Color.rgb(255, 255, 255));
                videoCallArrow.setVisibility(View.GONE);
            } else {
                videoCallView.setVisibility(View.GONE);
            }
        } else {
            if (is_astrologerAvailabaleForVideoCall.equalsIgnoreCase("true")) {
                videoCallView.setVisibility(View.VISIBLE);
                videoCallView.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_rect_with_border));
                videoCallText.setTextColor(ContextCompat.getColor(context, R.color.astro_desc_video_call_text));
                videoCallArrow.setColorFilter(ContextCompat.getColor(context, R.color.astro_desc_video_call_text));
                //videoCallIcon.setColorFilter(Color.rgb(0, 0, 0));
                ll_video_busy.setVisibility(View.GONE);
                videoCallArrow.setVisibility(View.VISIBLE);
            } else {
                videoCallView.setVisibility(View.GONE);
            }
            if (is_astrologerAvailabaleForCall.equalsIgnoreCase("true")
                    && is_astrologerAvailabaleForChat.equalsIgnoreCase("true")) {

                callNowBtnTxt.setText(context.getResources().getString(R.string.call));
                callNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                callNowBtnTxt.setTextColor(context.getColor(R.color.white));
                chatNowBtnTxt.setText(context.getResources().getString(R.string.chat_now));
                chatNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                callNowBtn.setVisibility(View.VISIBLE);
                chatNowBtnTxt.setTextColor(context.getColor(R.color.white));
                chatNowBtn.setVisibility(View.VISIBLE);
                setBottomActionIcon(ivCallNow, R.drawable.call_white_logo_large, R.color.white);
                setBottomActionIcon(ivChatNow, R.drawable.chat_white_logo_large, R.color.white);
                hideCallBtn();
            } else if (is_astrologerAvailabaleForCall.equalsIgnoreCase("true")
                    && is_astrologerAvailabaleForChat.equalsIgnoreCase("false")) {

                callNowBtnTxt.setText(context.getResources().getString(R.string.call));
                callNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                callNowBtnTxt.setTextColor(context.getColor(R.color.white));
                chatNowBtn.setVisibility(View.GONE);

                setBottomActionIcon(ivCallNow, R.drawable.call_white_logo_large, R.color.white);
                hideCallBtn();
            } else if (is_astrologerAvailabaleForCall.equalsIgnoreCase("false")
                    && is_astrologerAvailabaleForChat.equalsIgnoreCase("true")) {
                chatNowBtnTxt.setText(context.getResources().getString(R.string.chat_now));
                chatNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                chatNowBtnTxt.setTextColor(context.getColor(R.color.white));
                callNowBtn.setVisibility(View.GONE);
                chatNowBtn.setVisibility(View.VISIBLE);
                setBottomActionIcon(ivChatNow, R.drawable.chat_white_logo_large, R.color.white);
                hideCallBtn();
            } else if (is_astrologerAvailabaleForCall.equalsIgnoreCase("false")
                    && is_astrologerAvailabaleForChat.equalsIgnoreCase("false")) {

                //chatNowBtnTxt.setText(context.getResources().getString(R.string.offline));
                chatNowBtnTxt.setText(context.getResources().getString(R.string.busy));
                chatNowBtn.setVisibility(View.VISIBLE);
                chatNowBtn.setBackgroundResource(R.drawable.bg_button_gray);
                callNowBtn.setVisibility(View.GONE);
                chatNowBtnTxt.setTextColor(Color.WHITE);
                setBottomActionIcon(ivChatNow, R.drawable.chat_white_logo_large, R.color.white);
                if (is_astrologerAvailabaleForVideoCall.equalsIgnoreCase("true")) {
                    chatNowBtn.setVisibility(View.GONE);
                }
            }
        }

        if (astrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("true") ||
                astrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")||
                astrologerDetailBean.getIsAvailableForVideoCall().equalsIgnoreCase("true")) {
            if (astrologerDetailBean.getIsBusy().equalsIgnoreCase("true")) {
                //onlineTxt.setText(context.getResources().getString(R.string.busy));
                onlineOfflineImg.setImageResource(R.drawable.red);
            } else {
                //onlineTxt.setText(context.getResources().getString(R.string.online));
                onlineOfflineImg.setImageResource(R.drawable.green);
            }
        } else {
            //onlineTxt.setText(context.getResources().getString(R.string.offline));
            onlineOfflineImg.setImageResource(R.drawable.grey);
        }
    }

    private void setBottomActionIcon(ImageView imageView, int drawableRes, int tintColorRes) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableRes);
        if (drawable != null) {
            drawable = drawable.mutate();
            drawable.setTint(ContextCompat.getColor(this, tintColorRes));
            imageView.setImageDrawable(drawable);
            return;
        }
        imageView.setImageResource(drawableRes);
        imageView.setColorFilter(ContextCompat.getColor(this, tintColorRes));
    }

    private void setFeedbackAdapter() {

        allReviewTxt.setVisibility(View.VISIBLE);
        if (userFeedbackList.size() > 2 && tvSeeAllReviews.getVisibility() == View.VISIBLE) {
            ArrayList<UserReviewBean> list = new ArrayList<>();
            list.add(userFeedbackList.get(0));
            list.add(userFeedbackList.get(1));
            astrologerUserReciewAdapter = new AstrologerUserReciewAdapter(context, list);
            loadMoreBtn.setVisibility(View.INVISIBLE);
        } else {
            astrologerUserReciewAdapter = new AstrologerUserReciewAdapter(context, userFeedbackList);
            tvSeeAllReviews.setVisibility(View.GONE);
            loadMoreBtn.setVisibility(View.INVISIBLE);
        }
        //astrologerUserReciewAdapter = new AstrologerUserReciewAdapter(context, userFeedbackList);
        recyclerview.setAdapter(astrologerUserReciewAdapter);

    }

    private void setAstroStatusData() {
        String is_astrologerBusy;
        if (!astrologerIsBusy) {
            is_astrologerBusy = astrologerDetailBeanData.getIsBusy();
        } else {
            is_astrologerBusy = "true";

        }
        setCallChatButtonStatus(astrologerDetailBeanData, is_astrologerBusy);

        int actualPrice = 0;
        if (astrologerDetailBeanData.getActualServicePriceInt() != null && astrologerDetailBeanData.getActualServicePriceInt().length() > 0) {
            actualPrice = Integer.parseInt(astrologerDetailBeanData.getActualServicePriceInt());
        }

        int servicePrice = 0;
        if (astrologerDetailBeanData.getServicePrice() != null && astrologerDetailBeanData.getServicePrice().length() > 0) {
            servicePrice = Integer.parseInt(astrologerDetailBeanData.getServicePrice());
        }

        if (actualPrice > servicePrice) {
            actualConsultaionValueTxt.setVisibility(View.VISIBLE);
            String price = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBeanData.getActualServicePriceInt());
            CUtils.setStrikeOnTextView(actualConsultaionValueTxt, price);
            //actualConsultaionValueTxt.setText(price);
            //actualConsultaionValueTxt.setPaintFlags(actualConsultaionValueTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            consultaionValueTxt.setVisibility(View.VISIBLE);
            String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBeanData.getServicePrice());
            consultaionValueTxt.setText(quePrice);
            String imageUrl = CGlobalVariables.OFFER_IMAGE_URL + CUtils.getLanguageKey(LANGUAGE_CODE) + ".png";
            offerImg.setImageUrl(CUtils.getOfferImageLarge(), VolleySingleton.getInstance(AstrologerDescriptionActivity.this).getImageLoader());
            offerImg.setVisibility(View.VISIBLE);
        } else {
            String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBeanData.getServicePrice());
            /*try {
                Drawable img = context.getResources().getDrawable(R.drawable.rupee_icon);
                consultaionValueTxt.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }*/
            consultaionValueTxt.setText(quePrice);
            actualConsultaionValueTxt.setVisibility(View.GONE);
            consultaionValueTxt.setVisibility(View.VISIBLE);
            offerImg.setVisibility(View.GONE);
        }
        if (astrologerDetailBeanData.getUseIntroOffer()) {
            new_user.setVisibility(View.VISIBLE);
            String offerType = astrologerDetailBeanData.getIntroOfferType();
            //Log.d("AstroData", "offerType1="+offerType);
            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                new_user.setText(context.getResources().getString(R.string.text_free));

                String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBeanData.getServicePrice());
                CUtils.setStrikeOnTextView(consultaionValueTxt, quePrice);
                //consultaionValueTxt.setText(quePrice);
                //consultaionValueTxt.setPaintFlags(consultaionValueTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                consultaionValueTxt.setVisibility(View.VISIBLE);
                actualConsultaionValueTxt.setVisibility(View.GONE);
            } else {
                new_user.setText(context.getResources().getString(R.string.new_user));
            }
            //String imageUrl = CGlobalVariables.NEW_USER_OFFER_IMAGE_URL + CUtils.getLanguageKey(LANGUAGE_CODE) + ".png";
            offerImg.setImageUrl(CUtils.getOfferImageLarge(), VolleySingleton.getInstance(AstrologerDescriptionActivity.this).getImageLoader());
            offerImg.setVisibility(View.VISIBLE);
        } else {
            new_user.setVisibility(View.GONE);
            CUtils.removeStrikeOnTextView(consultaionValueTxt);
            //consultaionValueTxt.setPaintFlags(consultaionValueTxt.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            //String imageUrl = CGlobalVariables.OFFER_IMAGE_URL + CUtils.getLanguageKey(LANGUAGE_CODE) + ".png";
            offerImg.setImageUrl(CUtils.getOfferImageLarge(), VolleySingleton.getInstance(AstrologerDescriptionActivity.this).getImageLoader());
        }
        if (new_user.getVisibility() == View.VISIBLE) {
            if (!new_user.getText().toString().trim().equals(context.getResources().getString(R.string.text_free))) {
                videoCallText.setText(context.getResources().getString(R.string.video_call_at) + " " + new_user.getText().toString().trim() + " ₹" + consultaionValueTxt.getText().toString().trim());
            } else {

                videoCallText.setText(context.getResources().getString(R.string.video_call_at) + " " + new_user.getText().toString().trim());
            }
        } else {
            videoCallText.setText(context.getResources().getString(R.string.video_call_at) + " ₹" + consultaionValueTxt.getText().toString().trim());
        }
        boolean isLogin = CUtils.getUserLoginStatus(context);
        if (astrologerDetailBeanData.getEnablefeedbacks() != null && astrologerDetailBeanData.getEnablefeedbacks().length() > 0) {
            if (astrologerDetailBeanData.getEnablefeedbacks().equalsIgnoreCase("true") && isLogin) {
                isShowFeedbackDialog = true;
                feedbackLayout.setVisibility(View.VISIBLE);
            } else {
                feedbackLayout.setVisibility(View.GONE);
            }
        } else {
            feedbackLayout.setVisibility(View.GONE);
        }

        if (isShowRatingDialogAfterCall && isShowFeedbackDialog) {
            isShowRatingDialogAfterCall = false;
            showRatingDialogToUser();
        } else if ( message != null && message.length() > 0 && title != null && title.length() > 0) {
            message = null;
            if (isShowFeedbackDialog) {
                showRatingDialogToUser();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.e("SAN ADA ", "onstart");

        LocalBroadcastManager.getInstance(context).registerReceiver((receiver),
                new IntentFilter(CallStatusCheckService.BROAD_ACTION));
        LocalBroadcastManager.getInstance(context).registerReceiver((call_receiver),
                new IntentFilter(CGlobalVariables.CALL_BROAD_ACTION));
    }

    @Override
    public void onStop() {
        try {
            if (alert != null && alert.isShowing()) {
                alert.dismiss();
            }
        } catch (Exception e) {
            //
        }
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(call_receiver);
        // LocalBroadcastManager.getInstance(AstrologerDescriptionActivity.this).unregisterReceiver(waitingChatTimerView);
        // LocalBroadcastManager.getInstance(AstrologerDescriptionActivity.this).unregisterReceiver(ongoingChatTimerView);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Log.e("CCAvenuePmt", "onNewIntent()");
        getIntentData(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            fromDashboard = bundle.getBoolean("fromDashboard");
            isAIAstrologer = bundle.getBoolean("isAIAstrologer");
            boolean isRecharged = bundle.getBoolean(CGlobalVariables.IS_RECHARGED);
            if (isRecharged) {
                try {
                    if(RechargeSuggestionBottomSheet.getInstance()!=null){
                        RechargeSuggestionBottomSheet.getInstance().dismiss();
                    }
                }catch (Exception e){
                    //
                }

                String orderID = bundle.getString(CGlobalVariables.ORDER_ID);
                String orderStatus = bundle.getString(CGlobalVariables.ORDER_STATUS);
                String rechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT);
                String paymentMode = bundle.getString(CGlobalVariables.PAYMENT_MODE);
                String razorpayid = bundle.getString("razorpayid");
                //udatePaymentStatusOnserver(containerLayout, orderStatus, orderID, rechargeAmount, queue);
                if (orderStatus.equals("0")) {
                    udatePaymentStatusOnserveronFailed(containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode);
                } else {
                    udatePaymentStatusOnserver(this,containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode, razorpayid);
                }
            } else {
                Bundle extras = intent.getExtras();
                Intent intent1 = new Intent(this, AstrologerDescriptionActivity.class);
                intent1.putExtras(extras);
                startActivity(intent1);
                finish();
            }
        }
    }
    private  String bottomServiceListUsedFor;
    public void openWalletScreen(String openFrom) {
        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_astro_desc_low_balance_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "ADREC");
        }else  if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE)){
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_astro_desc_low_balance_subscribe_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "ASREC");

        }else {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("fba_astro_desc_low_balance_page_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "APREC");
        }
        openWalletScreen();
//        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)||openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT_SUBSCRIBE)){
//            if (CUtils.getCountryCode(AstrologerDescriptionActivity.this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
//                //intent = new Intent(AIChatWindowActivity.this, MiniPaymentInformationActivity.class);
//                bottomServiceListUsedFor = com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT;
//                openQuickRechargeSheet();
//            }else {
//                openWalletScreen();
//            }
//        }else {
//            openWalletScreen();
//        }
    }

    public void openWalletScreen() {
        Intent intent = new Intent(context, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "AstrologerDescriptionActivity");
        intent.putExtra(CGlobalVariables.ASTROLOGER_MOB_NUMBER, phoneNumber);
        if(urlText!=null){
            intent.putExtra(CGlobalVariables.ASTROLGER_URL_TEXT, urlText);
        }
        startActivity(intent);
        //finish();
    }
    private void openQuickRechargeSheet() {
        try {
            QuickRechargeBottomSheet quickRechargeBottomSheet = QuickRechargeBottomSheet.getInstance();
            Bundle bundle = new Bundle();
            //bundle.putString("mResponse", response);
            bundle.putString("astrologerUrlText", AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
            bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR,bottomServiceListUsedFor);
            bundle.putString("minBalanceNeededText","");
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "AstrologerDescriptionActivity");

            quickRechargeBottomSheet.setArguments(bundle);
            quickRechargeBottomSheet.show(getSupportFragmentManager(), QuickRechargeBottomSheet.TITLE);
        } catch (Exception e) {
            //
        }
    }
    public void gotoMiniPaymentInfoActivity(int mSelectedPosition, WalletAmountBean walletAmountBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
        bundle.putInt(CGlobalVariables.SELECTED_POSITION, mSelectedPosition);
        bundle.putString(CGlobalVariables.CALLING_ACTIVITY, "AstrologerDescriptionActivity");
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
        Intent intent = new Intent(context, MiniPaymentInformationActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            /*if (manager != null) {
                manager.registerListener(listener);
            }*/
            updateWalletData();
            actionOnResume();
            if (TextUtils.isEmpty(astroId)) {
                onResumeFlag = true;
                getAstrologerDetatils(urlText);
            } else {
                getAstrologerStatusPrice(astroId);
                if (astrologerIsBusy) {
                    astrologerIsBusy = false;
                }
            }
            showOrHideCallChatInitiate();
            showOrHideOngoingChat();
            showOrHideOngoingCall();
        }catch (Exception e){
            //
        }
    }

    //added by vishal :  hide call button in case of user is international and useIntroOffer is true.
    private void hideCallBtn() {
        errorLogs = errorLogs + useIntroOffer + " <====  useIntroOffer \n";

        if (useIntroOffer == 1) {
            if (!CUtils.getCountryCode(this).isEmpty() && !CUtils.getCountryCode(this).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
                callNowBtn.setVisibility(View.GONE);
            }
        }
    }

    public void callMsgDialogData(String message, String title, boolean isShowOkBtn, String fromWhich) {
        try {
            CallMsgDialog dialog = new CallMsgDialog(message, title, isShowOkBtn, fromWhich);
            dialog.show(getSupportFragmentManager(), "Dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actionOnIntent(Intent intent) {
        try {
            if (intent != null) {


                Bundle extras = intent.getExtras();
                String messagee = "", titlee = "", urlText = "";
                if (extras != null) {
                    if (extras.containsKey("title")) {
                        titlee = extras.getString("title");
                        //Log.e("LoadMore ", "Extra:" + extras.getString("title"));
                    }
                    if (extras.containsKey("msg")) {
                        messagee = extras.getString("msg");
                        //  Log.e("LoadMore ", "Extra:" + extras.getString("msg"));
                    }
                    if (extras.containsKey("astrologerUrl")) {
                        urlText = extras.getString("astrologerUrl");
                        if (urlText != null && urlText.length() > 0) {
                            String[] astroUrl = urlText.split("\\/");
                            if (astroUrl != null && astroUrl.length > 0) {
                                message = messagee;
                                title = titlee;
                                fetchAstrologerDetail(CUtils.getUserID(AstrologerDescriptionActivity.this), astroUrl[astroUrl.length - 1]);
                            }
                        }
                    } else {
                        if (message != null && message.length() > 0 && title != null && title.length() > 0) {
                            callMsgDialogData(message, title, true, CALL_CLICK);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actionOnResume() {
        boolean isLogin = CUtils.getUserLoginStatus(AstrologerDescriptionActivity.this);
        try {
            if (isLogin) {
                getWalletPriceData();
            }
        } catch (Exception e) {
        }
    }

    private void getWalletPriceData() {

        if (!CUtils.isConnectedWithInternet(AstrologerDescriptionActivity.this)) {
            //CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), DashBoardActivity.this);
        } else {
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_WALLET_PRICE_URL,
                    AstrologerDescriptionActivity.this, false, getWalletParams(), WALLET_PRICE_API).getMyStringRequest();
            queue.add(stringRequest);*/


            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getWalletBalance(getWalletParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String myResponse = response.body().string();
                        JSONObject object = new JSONObject(myResponse);
                        String status = "";
                        if (object.has("status")){
                            status = object.getString("status");
                        }
                        if (status.equalsIgnoreCase("100")){
                            startBackgroundLoginServiceForWallet();
                        } else {
                            String walletBal = object.getString("userbalancce");
                            if (walletBal.length() > 0) {
                                CUtils.setWalletRs(AstrologerDescriptionActivity.this, walletBal);
                                walletLayout.setVisibility(View.VISIBLE);
                                walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat(walletBal));
                            }
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //
                }
            });
        }
    }

    public Map<String, String> getWalletParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(AstrologerDescriptionActivity.this));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(AstrologerDescriptionActivity.this));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(AstrologerDescriptionActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        return CUtils.setRequiredParams(headers);
    }

    public void callToAstrologer(String phoneNo, String urlText, boolean isFreeConsultation) {
        showFreeOneMinDialog();
        callSelectedAstrologer(phoneNo, urlText, isFreeConsultation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DashBoardActivity.BACK_FROM_PROFILECHATDIALOG) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                if (isProceed) {
                    String phoneNo = data.getStringExtra("phoneNo");
                    String urlText = data.getStringExtra("urlText");
                    callToAstrologer(phoneNo, urlText, isFreeConsultation);
                } else if (data.getExtras().containsKey("openKundliList")) {
                    CUtils.openSavedKundliList(this, urlText, "call", DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                } else if (data.getExtras().containsKey("openProfileForChat")) {
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")) {
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    CUtils.openProfileForChat(this, urlText, "call", bundle, prefillData, DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
                }
            }
        } else if (requestCode == BACK_FROM_PROFILE_CHAT_DIALOG) {
            chatNowBtn.setEnabled(true);
            videoCallView.setEnabled(true);
            callNowBtn.setEnabled(true);
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                UserProfileData userProfileDataBean = (UserProfileData) data.getExtras().get("USER_DETAIL");
                String fromWhere = data.getStringExtra("fromWhere");
                if (isProceed) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.START_FROM_PROFILE_DIALOG,
                            AstrosageKundliApplication.currentEventType, "");
                    AstrosageKundliApplication.backgroundLoginCountForChat = 0;
                    if (fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_CHAT)) {
                        ChatUtils.getInstance(AstrologerDescriptionActivity.this).startChat(userProfileDataBean);
                    } else if (isProceed && fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_AI_CHAT)) {
                        ChatUtils.getInstance(AstrologerDescriptionActivity.this).startAIChat(userProfileDataBean);
                    } else if (fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_VIDEO_CALL)) {
                        errorLogs = errorLogs + "video call startVideoCall api Hit \n";
                        ChatUtils.getInstance(AstrologerDescriptionActivity.this).startVideoCall(userProfileDataBean);
                    } else if (fromWhere.equalsIgnoreCase(CGlobalVariables.TYPE_VOICE_CALL)) {
                        errorLogs = errorLogs + "goto api hit\n";
                        ChatUtils.getInstance(AstrologerDescriptionActivity.this).startAudioCall(userProfileDataBean);
                    }
                } else if (!isProceed && data.getExtras().containsKey("openKundliList")) {
                    CUtils.openSavedKundliList(this, astrologerDetailBeanData.getUrlText(), fromWhere, BACK_FROM_PROFILE_CHAT_DIALOG);
                } else if (!isProceed && data.getExtras().containsKey("openProfileForChat")) {
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")) {
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    CUtils.openProfileForChat(this, astrologerDetailBeanData.getUrlText(), fromWhere, bundle, prefillData, BACK_FROM_PROFILE_CHAT_DIALOG);
                } else {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.CANCEL_FROM_PROFILE_DIALOG,
                            AstrosageKundliApplication.currentEventType, "");
                    AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_CANCELED;
                }
            }

        }
    }

    public void enableChatBtn() {
        chatNowBtn.setEnabled(true);
        callNowBtn.setEnabled(true);
    }
    public void showRatingDialogToUser() {
        try {

            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_BTN,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            String phoneNo1 = CUtils.getUserID(context);
            /*if (ratingAndDakshinaDialog != null && ratingAndDakshinaDialog.isVisible()) {
                return;
            }
            ratingAndDakshinaDialog = new RatingAndDakshinaDialog(context, this, phoneNo1, astrologerDetailBeanData.getAstrologerId(), astrologerDetailBeanData.getUserOwnReviewModel());
            ratingAndDakshinaDialog.show(getSupportFragmentManager(), "FeedbackDialog");*/

            if (feedbackDialog != null && feedbackDialog.isVisible()) {
                return;
            }
            /*feedbackDialog = new FeedbackDialog(phoneNo1, astrologerDetailBeanData.getAstrologerId(),
                    astrologerDetailBeanData.getUserOwnReviewModel(), consultationType,"");*/
            feedbackDialog = new FeedbackDialog(consultationType,"", getSupportFragmentManager(), astrologerDetailBeanData);
            consultationType = "";
            feedbackDialog.show(getSupportFragmentManager(), "Dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    LiveGiftAdapter liveGiftAdapter;

    private void initGiftDialog() {
        giftDialog = new Dialog(context);
        giftDialog.setCancelable(false);
        giftDialog.setCanceledOnTouchOutside(false);
        giftDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        giftDialog.setContentView(R.layout.astrologer_description_gift_layout);
        giftDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView rvAstroDescGift = giftDialog.findViewById(R.id.rvAstroDescGift);
        ImageView closeView = giftDialog.findViewById(R.id.ivAstroDescGiftClose);
        Button recharge = giftDialog.findViewById(R.id.btnAstroDescGiftRecharge);
         send = giftDialog.findViewById(R.id.btnAstroDescGiftSend);
        TextView walletBalance = giftDialog.findViewById(R.id.tvAstroDescGiftBalance);
        String balance = getString(R.string.balance);
         balance = balance+ ": " + getResources().getString(R.string.astroshop_rupees_sign) +
                CUtils.convertAmtIntoIndianFormat(CUtils.getWalletRs(AstrologerDescriptionActivity.this));
        walletBalance.setText(balance);
        rvAstroDescGift.setLayoutManager(new GridLayoutManager(context, 3));
       try {
           for (int i = 0; i < giftModelArrayList.size(); i++) {
               GiftModel giftModel = giftModelArrayList.get(i);
               giftModel.setSelected(false);
           }
       }catch (Exception e){
           //
       }

        liveGiftAdapter = new LiveGiftAdapter(this, giftModelArrayList);
        rvAstroDescGift.setAdapter(liveGiftAdapter);

        liveGiftAdapter.onItemClick = (view, position) -> {
            try {
                for (int i = 0; i < giftModelArrayList.size(); i++) {
                    GiftModel giftModel = giftModelArrayList.get(i);
                    giftModel.setSelected(false);
                }
                GiftModel giftModel = giftModelArrayList.get(position);
                giftModel.setSelected(true);
                liveGiftAdapter.notifyDataSetChanged();
                send.setBackgroundResource(R.drawable.bg_button_orange);
                send.setBackgroundTintList(
                        ContextCompat.getColorStateList(this, R.color.colorPrimary_day_night)
                );
                send.setTextColor(ContextCompat.getColor(this, R.color.white));

            } catch (Exception e) {
                //
            }
        };

        closeView.setOnClickListener(v -> giftDialog.dismiss());
        recharge.setOnClickListener(v -> {
            Intent intent = new Intent(context, WalletActivity.class);
            intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "AstrologerDescription");
            startActivity(intent);
        });
        send.setOnClickListener(v -> {
            try {
                send.setEnabled(false);
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_SEND_GIFT, FIREBASE_EVENT_ITEM_CLICK, "");
                giftModel = null;
                for (int i = 0; i < giftModelArrayList.size(); i++) {
                    giftModel = giftModelArrayList.get(i);
                    if (giftModel.isSelected()) {
                        giftModel.setSelected(false);
                        break;
                    } else {
                        giftModel = null;
                    }
                }
                if (giftModel == null) {
                    CUtils.showSnackbar(giftDialog.findViewById(R.id.rlAstroDescGiftParent), getResources().getString(R.string.select_send_gift_error), context);
                } else {
                    if (Float.parseFloat(giftModel.getActualraters()) > Float.parseFloat(CUtils.getWalletRs(context))) {
                        CUtils.showSnackbar(giftDialog.findViewById(R.id.rlAstroDescGiftParent), getResources().getString(R.string.insufficient_wallet_balance), context);
                    } else {
                        sendGiftRequest(giftModel.getServiceid());
                    }
                }
            } catch (Exception e) {
                //
            }
        });

        giftDialog.show();

    }

    public void sendGiftRequest(String giftId) {

        if (CUtils.isConnectedWithInternet(this)) {
            //String url = CGlobalVariables.SEND_GIFT_URL;
            //Log.e("SAN ", " gift channel name " + channelName );
            //Log.e("SAN ", " gift url " + url );
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    AstrologerDescriptionActivity.this, false, sendGiftParams(giftId), SEND_GIFT_REQ).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.sendGift(sendGiftParams(giftId));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String myResponse = response.body().string();
                        if(send != null){
                            send.setEnabled(true);
                        }
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            updateWalletBalance();
                            makeGiftUnSelected();
                            CUtils.showSnackbar(findViewById(android.R.id.content), getResources().getString(R.string.gift_sucessful), AstrologerDescriptionActivity.this);
                            giftDialog.dismiss();
                        } else {
                            CUtils.showSnackbar(giftDialog.findViewById(R.id.rlAstroDescGiftParent), getResources().getString(R.string.gift_unsucessful), context);
                        }
                    } catch (Exception e) {
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if(send != null){
                        send.setEnabled(true);
                    }
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                }
            });
        } else {
            CUtils.showSnackbar(giftDialog.findViewById(R.id.rlAstroDescGiftParent), getResources().getString(R.string.no_internet), this);
        }
    }

    public Map<String, String> sendGiftParams(String giftId) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this));
        boolean isLogin = CUtils.getUserLoginStatus(this);
        try {
            headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(this));
            headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(this));
            headers.put(CGlobalVariables.CHANNEL_NAME, CUtils.getCountryCode(this) + "-" + CUtils.getUserID(this));
            /*if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(this));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(this));
                headers.put(CGlobalVariables.CHANNEL_NAME, CUtils.getCountryCode(this)+"-"+CUtils.getUserID(this));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }*/
        } catch (Exception e) {
            //
        }
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(this));
        headers.put(CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(this));
        headers.put(CGlobalVariables.KEY_GIFT_ID, giftId);

        //Log.e("SAN ", " gift params " + headers.toString() );

        return headers;
    }

    private void updateWalletBalance() {
        try {
            if (giftModel == null)
                return;
            float currWalletBal = Float.parseFloat(CUtils.getWalletRs(context));
            currWalletBal = currWalletBal - Float.parseFloat(giftModel.getActualraters());
            CUtils.setWalletRs(context, String.valueOf(currWalletBal));
            updateWalletData();

        } catch (Exception e) {
            //
        }
    }

    public void makeGiftUnSelected() {
        try {
            giftModel = null;
            if (giftModelArrayList == null)
                return;
            for (int i = 0; i < giftModelArrayList.size(); i++) {
                GiftModel giftModel = giftModelArrayList.get(i);
                giftModel.setSelected(false);
            }
            liveGiftAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }

    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(context);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportOrBlockReview(int actionType, String reviewId) {
        try {
            if (actionType == CGlobalVariables.BLOCK_REVIEW) {
                for (int i = 0; i < userFeedbackList.size(); i++) {
                    UserReviewBean userReviewBean = userFeedbackList.get(i);
                    if (userReviewBean.getActualFeedbackId().equals(reviewId)) {
                        userFeedbackList.remove(userReviewBean);
                        isReviewBlocked = true;
                        break;
                    }
                }
                setFeedbackAdapter();
                Toast.makeText(context, context.getResources().getString(R.string.msg_block_review), Toast.LENGTH_LONG).show();
            } else if (actionType == CGlobalVariables.REPORT_REVIEW) {
                Toast.makeText(context, context.getResources().getString(R.string.msg_report_review), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //
        }
        try {
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.REPORT_FEEDBACK_URL,
                    AstrologerDescriptionActivity.this, false, getReviewActionParams(actionType, reviewId), REVIEW_ACTION_METHOD).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            //Log.d("reviewReportApi", "ASTROLOGER_DE"+getReviewActionParams(actionType, reviewId));
            Call<ResponseBody> call = api.reviewReport(getReviewActionParams(actionType, reviewId));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {

                        String myResponse = response.body().string();
                        //Log.d("reviewReportApi","reviewReportApi Response+==="+myResponse);
                        // do nothing
                    } catch (Exception e) {
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                }
            });
        } catch (Exception e) {
            //
        }
    }

    public Map<String, String> getReviewActionParams(int actionType, String reviewId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(context));
        headers.put("reporttype", String.valueOf(actionType));
        headers.put("feedbackid", reviewId);

        return CUtils.setRequiredParams(headers);
    }

    private void sendNotification(AstrologerDetailBean astrologerDetailBean) {
        if (CUtils.isConnectedWithInternet(this)) {
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ADD_TO_QUEUE,
                    this, false, getQueueParams(astrologerDetailBean), ADD_TO_QUEUE_METHOD).getMyStringRequest();
            queue.add(stringRequest);*/

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getNotificationIfAstroBusy(getQueueParams(astrologerDetailBean));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if (status.equals("100")) {
                            startBackgroundLoginServiceFromDialog();
                        } else {
                            CUtils.showSnackbar(findViewById(android.R.id.content), message, AstrologerDescriptionActivity.this);
                        }
                    } catch (Exception e) {
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
                }
            });
        }
    }

    public Map<String, String> getQueueParams(AstrologerDetailBean astrologerDetailBean) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this));
        headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(this));
        headers.put(CGlobalVariables.URL_TEXT, astrologerDetailBean.getUrlText());
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(this));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, "" + CUtils.getMyAndroidId(this));
        headers.put(CGlobalVariables.ASTROLOGER_ID, "" + astrologerDetailBean.getAstrologerId());
        headers.put(CGlobalVariables.KEY_USER_ID, "" + CUtils.getUserIdForBlock(this));
        return headers;
    }

    private void setJoinChatView() {
        ongoingChatInfoLayout = findViewById(R.id.join_ongoing_chat_layout);
        ongoingChatAstroProfileImage = ongoingChatInfoLayout.findViewById(R.id.chatAstroProfileImage);
        ongoingChatAstroName = ongoingChatInfoLayout.findViewById(R.id.chatAstroName);
        FontUtils.changeFont(context, ongoingChatAstroName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        ongoingChatRemTime = ongoingChatInfoLayout.findViewById(R.id.chatRemTime);
        joinOngoingChat = ongoingChatInfoLayout.findViewById(R.id.join_ongoing_chat);
        FontUtils.changeFont(context, joinOngoingChat, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
    }

    /* private final BroadcastReceiver ongoingChatTimerView = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             String remTime = intent.getStringExtra("rem_time");
             String CHANNEL_ID = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
             String chatJsonObject = intent.getStringExtra("connect_chat_bean");
             String astrologerName = intent.getStringExtra("astrologer_name");
             String astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
             String astrologerId = intent.getStringExtra("astrologer_id");
             String userChatTime = intent.getStringExtra("userChatTime");
             if (!remTime.equals("00:00:00")) {
                 if (AstrosageKundliApplication.chatInitiateAstrologerDetailBean != null) {
                     CUtils.joinOngoingChatLayoutView(AstrologerDescriptionActivity.this, remTime,CHANNEL_ID,chatJsonObject,astrologerName,astrologerProfileUrl,astrologerId,userChatTime);
                     ongoingChatInfoLayout.setVisibility(View.VISIBLE);
                 }
             } else {
                 ongoingChatInfoLayout.setVisibility(View.GONE);
             }

         }
     };*/
    public void showOrHideOngoingChat() {
        if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
            OngoingCallChatManager.getOngoingChatLiveData().observe(this, ongoingChatObserver);
        } else {
            if (ongoingChatObserver != null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
            }
            ongoingChatInfoLayout.setVisibility(View.GONE);
        }

    }

    public void showOrHideOngoingCall() {
        Log.d("agoraCallOngoing", "showOrHideOngoingCall == > called Des");
        if (CUtils.checkServiceRunning(AgoraCallOngoingService.class)) {
            Log.d("agoraCallOngoing", "showOrHideOngoingCall == > called showOrHideOngoingCall Des");

            OngoingCallChatManager.getOngoingChatLiveData().observe(this, ongoingCallObserver);
        } else {
            if (ongoingCallObserver != null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingCallObserver);
            }
            ongoingChatInfoLayout.setVisibility(View.GONE);
        }

    }

    public void showOrHideCallChatInitiate() {
        flag = false;
        cross_btn.setEnabled(true);

        if (CUtils.checkServiceRunning(AstroAcceptRejectService.class) || CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
            AstroLiveDataManager.getAstroAcceptLiveData().observe(this, callChatObserver);
        } else {
            if (callChatObserver != null) {
                AstroLiveDataManager.getAstroAcceptLiveData().removeObserver(callChatObserver);
            }
            chatInitiateInfoLayout.setVisibility(View.GONE);
        }

    }

    public void chatCompleted(String URL, String channelID, String remarks, String status, String astroId) {
        CUtils.sendLogDataRequest(astroId, channelID, "DashboardActivity chatCompleted() onChildAdded() remarks="+remarks);
        CUtils.cancelNotification(AstrologerDescriptionActivity.this);
        CGlobalVariables.chatTimerTime = 0;
        if (!CUtils.isConnectedWithInternet(AstrologerDescriptionActivity.this)) {
            CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), AstrologerDescriptionActivity.this);
        } else {
            showProgressBar();
            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, URL,
                    AstrologerDescriptionActivity.this, false, getChatCompleteParams(channelID, remarks, status, astroId), END_CHAT_VALUE).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);*/
            if(URL.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.END_CHAT_URL)){
                callEndChatApi(channelID,remarks,status,astroId);
            }else {
                callEndCallApi(channelID,remarks,status,astroId);
            }

//            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
//            Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, remarks, status, astroId));
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    hideProgressBar();
//                    try {
//                        String myResponse = response.body().string();
//                        CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
//                        CGlobalVariables.chatTimerTime = 0;
//                        AstrosageKundliApplication.chatTimerRemainingTime = 0;
//                        AstrosageKundliApplication.selectedAstrologerDetailBean = null;
//                        AstrosageKundliApplication.chatJsonObject = "";
//                        AstrosageKundliApplication.channelIdTempStore = "";
//                        CUtils.saveAstrologerIDAndChannelID(AstrologerDescriptionActivity.this, "", "");
//                        if (AstrosageKundliApplication.currentConsultType != null) {
//                            if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.CHAT_TEXT)) {
//                                stopService(new Intent(context, AstroAcceptRejectService.class));
//                            } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.AUDIO_CALL_TEXT)) {
//                                stopService(new Intent(context, AgoraCallInitiateService.class));
//                            } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.VIDEO_CALL_TEXT)) {
//                                stopService(new Intent(context, AgoraCallInitiateService.class));
//                            }
//                        }
//                        chatInitiateInfoLayout.setVisibility(View.GONE);
//                        onResume(); //to refresh busy status if call is cancelled
//                    } catch (Exception e) {
//                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    hideProgressBar();
//                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
//                }
//            });
        }
    }
    private void callEndChatApi(String channelID, String remarks, String status, String astroId) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endChat(getChatCompleteParams(channelID, remarks, status, astroId), channelID, getClass().getSimpleName());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.body()!=null) {
                    //Log.e("SAN CI DA ", " onResponse method == END_CHAT_VALUE "  );
                    try {
                        hideProgressBar();
                        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("chat_competed_api_response", AstrosageKundliApplication.currentEventType, "");
                        endCallChatActions();

                    } catch (Exception e) {
                        //
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                endCallChatActions();
            }
        });
    }
    private void callEndCallApi(String channelID, String remarks, String status, String astroId) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endInternetcall(getChatCompleteParams(channelID, remarks, status, astroId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    hideProgressBar();
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("internet_call_competed_api_response", AstrosageKundliApplication.currentEventType, "");
                    endCallChatActions();

                } catch (Exception e) {
                    //
                }            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                endCallChatActions();
            }
        });
    }
    private void endCallChatActions() {
        try {
            CGlobalVariables.chatTimerTime = 0;
            AstrosageKundliApplication.chatTimerRemainingTime = 0;
            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
            AstrosageKundliApplication.chatJsonObject = "";
            AstrosageKundliApplication.channelIdTempStore = "";
            CUtils.saveAstrologerIDAndChannelID(AstrologerDescriptionActivity.this, "", "");
            if (AstrosageKundliApplication.currentConsultType != null) {
                if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.CHAT_TEXT)) {
                    stopService(new Intent(context, AstroAcceptRejectService.class));
                } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.AUDIO_CALL_TEXT)) {
                    stopService(new Intent(context, AgoraCallInitiateService.class));
                } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.VIDEO_CALL_TEXT)) {
                    stopService(new Intent(context, AgoraCallInitiateService.class));
                }
            }
            chatInitiateInfoLayout.setVisibility(View.GONE);
            onResume(); //to refresh busy status if call is cancelled
        } catch (Exception e) {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), AstrologerDescriptionActivity.this);
        }

    }

    public Map<String, String> getChatCompleteParams(String channelID, String chatStatus, String remarks, String astroId) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, chatStatus);
        headers.put(CGlobalVariables.CHAT_DURATION, "00");
        headers.put(CGlobalVariables.CHANNEL_ID, channelID);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astroId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    private final Observer<Intent> ongoingChatObserver = new Observer<Intent>() {
        @Override
        public void onChanged(Intent intent) {
            try {
                String remTime = intent.getStringExtra("rem_time");
                String CHANNEL_ID = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
                String chatJsonObject = intent.getStringExtra("connect_chat_bean");
                String astrologerName = intent.getStringExtra("astrologer_name");
                String astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
                String astrologerId = intent.getStringExtra("astrologer_id");
                String userChatTime = intent.getStringExtra("userChatTime");
                String chatinitiatetype = intent.getStringExtra(CGlobalVariables.CHATINITIATETYPE);
                if (!remTime.equals("00:00:00")) {
                    if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                        CUtils.joinOngoingChatLayoutView(AstrologerDescriptionActivity.this, remTime, CHANNEL_ID, chatJsonObject, astrologerName, astrologerProfileUrl, astrologerId, userChatTime,chatinitiatetype);
                        ongoingChatInfoLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    // OngoingChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
                    ongoingChatInfoLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                //
            }

        }
    };
    private final Observer<Intent> ongoingCallObserver = new Observer<Intent>() {
        @Override
        public void onChanged(Intent intent) {
            try {
                String remTime = intent.getStringExtra("rem_time");
                boolean micStatus = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_MIC_STATUS, true);
                boolean videoStatus = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, true);
                String consultationType = intent.getStringExtra(CGlobalVariables.AGORA_CALL_TYPE);
                String agoraCallSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
                String agoraToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
                String agoraTokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
                String astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
                String astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
                String agoraCallDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
                String astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);
                if (!remTime.equals("00:00:00")) {
                    CUtils.joinOngoingCallLayoutView(AstrologerDescriptionActivity.this, remTime, agoraCallSId, agoraToken, agoraTokenId, astrologerName, astrologerProfileUrl, astrologerId, agoraCallDuration, consultationType, micStatus, videoStatus);
                    ongoingChatInfoLayout.setVisibility(View.VISIBLE);
                } else {
                    // OngoingChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
                    ongoingChatInfoLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                //
            }

        }
    };

    @Override
    protected void onDestroy() {
        try {
            if (callChatObserver != null) {
                AstroLiveDataManager.getAstroAcceptLiveData().removeObserver(callChatObserver);
            }
            if (ongoingChatObserver != null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
            }
            if (ongoingCallObserver != null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingCallObserver);
            }

            if (mReceiverRefundFreeChat != null) {
                LocalBroadcastManager.getInstance(AstrologerDescriptionActivity.this).unregisterReceiver(mReceiverRefundFreeChat);
            }

        } catch (Exception e) {
            //
        }
        super.onDestroy();
    }

    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    /*SplitInstallStateUpdatedListener listener =
            new SplitInstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(SplitInstallSessionState state) {

                    switch (state.status()) {
                        case SplitInstallSessionStatus.DOWNLOADING:
                            int totalBytes = (int) state.totalBytesToDownload();
                            int progress = (int) state.bytesDownloaded();
                            updateInstallLiveProgressBar(CUtils.convertBytesToMB(totalBytes), CUtils.convertBytesToMB(progress));
                            break;
                        case SplitInstallSessionStatus.INSTALLING:
                            break;
                        case SplitInstallSessionStatus.DOWNLOADED:
                            break;

                        case SplitInstallSessionStatus.INSTALLED:

                            hideInstallLiveProgressBar();
                            //init video call
                            if(AstrosageKundliApplication.currentEventType.equals(CGlobalVariables.VIDEO_CALL_BTN_CLICKED)){
                                ChatUtils.getInstance(AstrologerDescriptionActivity.this).initVideoCall(astrologerDetailBeanData);
                            }
                            break;

                        case SplitInstallSessionStatus.CANCELED:
                            hideInstallLiveProgressBar();
                            //callSnakbar("Live content installation cancelled");
                            break;

                        case SplitInstallSessionStatus.PENDING:
                            break;

                        case SplitInstallSessionStatus.FAILED:
                            hideInstallLiveProgressBar();
                            //callSnakbar("Live content installation failed. Error code: " + state.errorCode());
                            break;
                    }
                }
            };*/

//    public void checkPermissions() {
//        boolean granted = true;
//        for (String per : PERMISSIONS) {
//            if (!permissionGranted(per)) {
//                granted = false;
//                break;
//            }
//        }
//
//        if (granted) {
//            Log.e("liveAstrologerModel", "checkPermissions2");
//            checkVartaLiveModule();
//        } else {
//            requestPermissions();
//        }
//    }
//
//    private boolean permissionGranted(String permission) {
//        return ContextCompat.checkSelfPermission(
//                this, permission) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
//    }

    private void toastNeedPermissions() {
        //callSnakbar(getString(R.string.need_necessary_permissions));
    }

    private void checkVartaLiveModule() {
        //if (manager == null) return;
        //if (manager.getInstalledModules().contains(CGlobalVariables.MODULE_VARTA_LIVE)) {
        //init video call
        videoCallView.setEnabled(false);
        ChatUtils.getInstance(AstrologerDescriptionActivity.this).initVideoCall(astrologerDetailBeanData);
        /*} else {
            installVartaLiveModule();
        }*/
    }

    /*public void installVartaLiveModule() {
        if (manager == null) return;
        SplitInstallRequest request = SplitInstallRequest
                .newBuilder()
                .addModule(CGlobalVariables.MODULE_VARTA_LIVE)
                .build();

        manager.startInstall(request)
                .addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer sessionId) {
                        showInstallLiveProgressBar();
                    }
                })
                .addOnFailureListener(new com.google.android.play.core.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        //callSnakbar("Live content installation failed");
                    }
                });
    }*/

    public void updateCallChatButton() {
        // astrologerDetailBeanData.setIsBusy(jsonObject.getString("isBusy"));
        astrologerIsBusy = true;
        setAstroStatusData();
    }

    @Override
    public void clickCall() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(AstrologerDescriptionActivity.this, ASTROLOGER_DESCRIPTION_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(AstrologerDescriptionActivity.this, ASTROLOGER_DESCRIPTION_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }



    public void responseNetworkCall(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("msg");
            if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                final String callsId = jsonObject.getString("callsid");
                String talkTime = jsonObject.getString("talktime");
                final String exophoneNo = jsonObject.getString("exophone");
                String internationalCharges = jsonObject.getString("callcharge");

                if (freeMinutedialog != null && freeMinutedialog.isVisible()) {
                    //freeMinutedialog.dismiss();
                    CUtils.fcmAnalyticsEvents("show_call_thank_you_dialog", AstrosageKundliApplication.currentEventType, "");
                    openFreeMinDialogBox();
                }
                String astroName = "", profileUrl = "";
                if (astrologerDetailBeanData != null) {
                    profileUrl = astrologerDetailBeanData.getImageFile();
                    astroName = astrologerDetailBeanData.getName();

                    // Free offer taken event
                                /*
                                if (astrologerDetailBeanData != null && astrologerDetailBeanData.getUseIntroOffer()) {
                                    String offerType = astrologerDetailBeanData.getIntroOfferType();
                                    if(!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
                                        CUtils.fcmAnalyticsEvents(FREE_OFFER_TAKEN,CALL_CLICK,"");
                                        CUtils.saveFirstFreeOfferTakenDate(context, DateTimeUtils.currentDate());

                                    }else if(!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.REDUCED_PRICE_OFFER)){
                                        CUtils.fcmAnalyticsEvents(REDUCE_PRICE_OFFER_TAKEN,CALL_CLICK,"");
                                        CUtils.saveReducePriceOfferTakenDate(context, DateTimeUtils.currentDate());
                                    }
                                }*/
                }
                CUtils.fcmAnalyticsEvents("show_call_initiate_dialog", AstrosageKundliApplication.currentEventType, "");

                CallInitiatedDialog dialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CALL_CLICK, internationalCharges, astroName, profileUrl);
                dialog.show(getSupportFragmentManager(), "Dialog");
                astrologerDetailBeanData.setIsBusy(jsonObject.getString("isBusy"));
                astrologerIsBusy = true;
                setAstroStatusData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CUtils.fcmAnalyticsEvents("start_call_status_service", AstrosageKundliApplication.currentEventType, "");
                            Intent intent = new Intent(AstrologerDescriptionActivity.this, CallStatusCheckService.class);
                            intent.putExtra(CGlobalVariables.CALLS_ID, callsId);
                            context.startService(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 60000);

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void clickLive () {
        fabActions();
    }

    public void startBackgroundLoginServiceForWallet() {
        isStartedForWallet = true;
        LocalBroadcastManager.getInstance(AstrologerDescriptionActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
        startBackgroundLoginService();
    }

    private void registerReceiverRefundFreeChat() {
        LocalBroadcastManager.getInstance(AstrologerDescriptionActivity.this).registerReceiver(mReceiverRefundFreeChat
                , new IntentFilter(CGlobalVariables.SEND_BROADCAST_REFUND_FREE_CHAT));
    }

    private final BroadcastReceiver mReceiverRefundFreeChat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.e("SAN Astro Desc ", "Refund Free Chat onRecieve()");
            onResume();
        }
    };


    private void checkVoiceCallPermission() {
        boolean granted = true;
        for (String per : REQUESTED_PERMISSIONS_VOICE_CALL) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            CUtils.setHavePermissionRecordAudio("1");
            checkCallMedium();
        } else {
            CUtils.showPreMicPermissionDialog(this, this::requestVoiceCallPermissions);
        }
    }

    private void requestVoiceCallPermissions() {
        ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS_VOICE_CALL, VOICE_CALL_PERMISSION_REQ_ID);
    }

    private void parseConnectCallResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("msg");
            if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                final String callsId = jsonObject.getString("callsid");
                String talkTime = jsonObject.getString("talktime");
                final String exophoneNo = jsonObject.getString("exophone");
                String internationalCharges = jsonObject.getString("callcharge");

                if (freeMinutedialog != null && freeMinutedialog.isVisible()) {
                    //freeMinutedialog.dismiss();
                    CUtils.fcmAnalyticsEvents("show_call_thank_you_dialog", AstrosageKundliApplication.currentEventType, "");
                    openFreeMinDialogBox();
                }
                String astroName = "", profileUrl = "";
                if (astrologerDetailBeanData != null) {
                    profileUrl = astrologerDetailBeanData.getImageFile();
                    astroName = astrologerDetailBeanData.getName();
                }
                CUtils.fcmAnalyticsEvents("show_call_initiate_dialog", AstrosageKundliApplication.currentEventType, "");

                CallInitiatedDialog dialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CALL_CLICK, internationalCharges, astroName, profileUrl);
                dialog.show(getSupportFragmentManager(), "Dialog");
                //astrologerDetailBeanData.setIsBusy(jsonObject.getString("isBusy"));
                astrologerIsBusy = true;
                setAstroStatusData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CUtils.fcmAnalyticsEvents("start_call_status_service", AstrosageKundliApplication.currentEventType, "");
                            Intent intent = new Intent(AstrologerDescriptionActivity.this, CallStatusCheckService.class);
                            intent.putExtra(CGlobalVariables.CALLS_ID, callsId);
                            context.startService(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 60000);

            } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                CUtils.fcmAnalyticsEvents("call_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                String dialogMsg = getResources().getString(R.string.unable_to_connect_free_consultation);
                callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
            } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                String astrologerName = jsonObject.getString("astrologername");
                String userbalance = jsonObject.getString("userbalance");
                String minBalance = jsonObject.getString("minbalance");
                CUtils.fcmAnalyticsEvents("call_insufficient_bal_dialog", AstrosageKundliApplication.currentEventType, "");
                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, CALL_CLICK);
                dialog.show(getSupportFragmentManager(), "Dialog");

            } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                String fcm_label = "call_astrologer_" + message;

                CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");

                String dialogMsg = "";
                if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                    dialogMsg = getResources().getString(R.string.user_blocked_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                    dialogMsg = getResources().getString(R.string.astrologer_busy_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                    String stringMsg = getResources().getString(R.string.astrologer_offline_msg);
                    dialogMsg = stringMsg;
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                    dialogMsg = getResources().getString(R.string.astrologer_status_disable);
                } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                    dialogMsg = getResources().getString(R.string.call_api_failed_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                    dialogMsg = getResources().getString(R.string.existing_call_msg);
                } else {
                    dialogMsg = getResources().getString(R.string.something_wrong_error);
                }
                callMsgDialogData(dialogMsg, "", true, CALL_CLICK);
            } else {
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), context);
            }
        } catch (Exception e){
            //
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response, int requestCode) {
        hideProgressBar();
        if (requestCode == METHOD_RECHARGE) {
            if(response.body()!=null) {
                try {
                    String myResponse = response.body().string();
                    handleWalletRechargeResponse(myResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        handleWalletRechargeError();
        hideSwipToRefresh();
        hideProgressBar();
        CUtils.showSnackbar(mainlayout, t.getMessage(), AstrologerDescriptionActivity.this);
    }

    public void shareReview(String reviewId) {
        for (int i = 0; i < userFeedbackList.size(); i++) {
            UserReviewBean userReviewBean = userFeedbackList.get(i);
            if (userReviewBean.getActualFeedbackId().equals(reviewId)) {
                ShareAstroReviewFragment.getInstance(userReviewBean,astrologerDetailBeanData).show(getSupportFragmentManager(),"shareAstrologerReviewTag");
                break;
            }
        }
    }

}

