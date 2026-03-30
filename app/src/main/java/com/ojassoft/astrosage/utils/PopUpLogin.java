package com.ojassoft.astrosage.utils;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_USER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NEW_USER_OTP_SEND;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.facebook.appevents.AppEventsConstants;
import com.google.gson.Gson;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.CGCMRegistrationInfoSaveOnOjas;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.varta.adapters.CountryListAdapter;
import com.ojassoft.astrosage.varta.customwidgets.CustomEditText;
import com.ojassoft.astrosage.varta.interfacefile.CustomEditTextListener;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopUpLogin extends DialogFragment implements View.OnClickListener, VolleyResponse {
    CountryListAdapter adapter;
    ArrayList<CountryBean> countryBeanList = null;
    RequestQueue queue;
    boolean isShowProgressBar = false;
    CustomProgressDialog pd;
    TextView countryCodeText;
    EditText mobileNumberTxt;
    private Context mContext;
    RelativeLayout country_code_layout;
    Button getOtpBtn, btnCallNow;
    boolean isClickGetOtpBtn = false;
    String mobileNumber = "";
    LinearLayout ll_otp;
    CustomEditText otpEdt1, otpEdt2, otpEdt3, otpEdt4 /*,otpEdt5, otpEdt6*/;
    int RESEND_OTP_METHOD = 33;
    int RESEND_OTP_VIA_PHONE_METHOD = 44;
    Button verifyBtn;
    TextView resendOtp, otpHeadingTxt, otpMsgTxt;
    TextView getOtpViaPhone;
    private TextView textViewTimer;
    private CountDownTimer countDownTimer;
    private static final String FORMAT = "%02d:%02d";
    private static final long longTimeAfterWhichButtonEnable = 91000;
    private static final long longTotalVerificationTime = 91000;
    private static final long longOneSecond = 1000;
    String newUser = "0";
    private String astroSageUserId = "";
    LinearLayout mainViewDialoag, enterOtpLayout, enterPhoneNumberLayout;
    public static final int FETCH_OTP_METHOD = 1, VERIFY_OTP_METHOD = 5;
    private int intNumberOfTimesResendPress = 0;
    static int resendOtpCount = 1;
    private String viewType;
    TextView txt_heading_pop_up, txt_sub_heading_pop_up, edit_number;
    String headingText, subHeadingText;
    ImageView imageViewExpressionIcon;
    LinearLayout ll_change_mobile_no;
    int drawableId;
    Drawable drawable;
    String popUpCalledFrom = "ACTIVITY";
    Typeface typeface;
    private int LANGUAGE_CODE = com.ojassoft.astrosage.utils.CGlobalVariables.ENGLISH;
    private String consulationType = CGlobalVariables.TYPE_CALL;
    boolean enabledAIFreeChatPopup;

    public PopUpLogin() {

    }

    public PopUpLogin(String viewType, String popUpCalledFrom) {
        this.viewType = viewType;
        this.popUpCalledFrom = popUpCalledFrom;
        dialogEnterMobileAppearEvent();
    }

    public PopUpLogin(String viewType, String headingText, String subHeadingText, int drawableId) {
        this.viewType = viewType;
        this.headingText = headingText;
        this.subHeadingText = subHeadingText;
        this.drawableId = drawableId;
        CUtils.isPopUpLoginShowing = true;
        dialogAppearEvent();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("viewType", viewType);
        outState.putString("headingText", headingText);
        outState.putString("subHeadingText", subHeadingText);
        outState.putInt("drawableId", drawableId);
        outState.putString("popUpCalledFrom", popUpCalledFrom);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            if (savedInstanceState != null) {
                viewType = savedInstanceState.getString("viewType");
                headingText = savedInstanceState.getString("headingText");
                subHeadingText = savedInstanceState.getString("subHeadingText");
                drawableId = savedInstanceState.getInt("drawableId");
                popUpCalledFrom = savedInstanceState.getString("popUpCalledFrom");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            drawable = ContextCompat.getDrawable(mContext, drawableId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LANGUAGE_CODE = com.ojassoft.astrosage.utils.CUtils.getLanguageCodeFromPreference(mContext);
        typeface = com.ojassoft.astrosage.utils.CUtils.getRobotoFont(
                mContext, LANGUAGE_CODE, com.ojassoft.astrosage.utils.CGlobalVariables.regular);
        View view = inflater.inflate(R.layout.layout_pop_up_login, container);
        try {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            //getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            // getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(getActivity());
            init(view);
            initDialogView(view);
            initEnterMobileView(view);
            initOtpView(view);
            updateDialogStatus();
        } catch (Exception e) {
            //
        }
        return view;
    }
    int astroImageIdRes;
    private void updateDialogStatus() {
        try {
            if (popUpCalledFrom != null && popUpCalledFrom.equals("ACTIVITY")) {
                // txt_heading_pop_up.setText(headingText);
                //  txt_sub_heading_pop_up.setText(subHeadingText);

                //imageViewExpressionIcon.setImageDrawable(drawable);
            } else {
                enterPhoneNumberLayout.setVisibility(View.VISIBLE);
                enterOtpLayout.setVisibility(View.GONE);
                mainViewDialoag.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            //
        }
    }

    private void initDialogView(View view) {
        try {
            mainViewDialoag = view.findViewById(R.id.mainViewDialoag);
            // txt_heading_pop_up = view.findViewById(R.id.txt_heading_pop_up);
            TextView consultPremiumAstrologerTV = view.findViewById(R.id.consult_premium_astrologers_tv);
            TextView first_call_in_1rs_tv = view.findViewById(R.id.first_call_in_1rs_tv);
            // txt_sub_heading_pop_up = view.findViewById(R.id.txt_sub_heading_pop_up);
            imageViewExpressionIcon = view.findViewById(R.id.iv_astrologer_image);
            ArrayList<Integer> my_list = new ArrayList<Integer>();
//            my_list.add(R.drawable.astro_dr_raman);
//            my_list.add(R.drawable.astro_anita_jha);
//            my_list.add(R.drawable.astro_acharya_joshi);
            my_list.add(R.drawable.astro_mr_krishnamurti);
//            my_list.add(R.drawable.mom_the_astrologer);
            my_list.add(R.drawable.astro_rahasya_veda);
            for (int i = 0; i < my_list.size(); i++) {
                int index = (int) (Math.random() * my_list.size());
                astroImageIdRes = my_list.get(index);
                Glide.with(getContext())
                        .load(astroImageIdRes)
                        .into(imageViewExpressionIcon);
            }
            btnCallNow = view.findViewById(R.id.btnCallNow);
            boolean isSecondFreeChat = CUtils.isSecondFreeChat(mContext);
            //String firstFreeChatType = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE,"");
            String secondFreeChatType = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE,"");
            String firstConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_CONSULT_TYPE,"");
            String offerType = CUtils.getCallChatOfferType(mContext);
            String callChatText = mContext.getResources().getString(R.string.first_chat_call_free);
            if (CUtils.getUserLoginStatus(mContext)) {

                if(!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    if(isSecondFreeChat && secondFreeChatType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)){
                        consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_ai_astrologers));
                        consulationType = CGlobalVariables.TYPE_CHAT;
                    }else if (firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT)) {
                        consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_ai_astrologers));
                        consulationType = CGlobalVariables.TYPE_CHAT;
                    } else if (firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)) {
                        consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_ai_astrologers));
                        first_call_in_1rs_tv.setText(mContext.getResources().getText(R.string.get_your_guidance));
                        btnCallNow.setText(callChatText.replace("#", mContext.getResources().getString(R.string.call)));
                        consulationType = CGlobalVariables.TYPE_CALL;
                    } else if (firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT)) {
                        first_call_in_1rs_tv.setText(callChatText.replace("#", mContext.getResources().getString(R.string.chat_now)));
                        btnCallNow.setText(mContext.getResources().getString(R.string.free_chat));
                        consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_astrologers));
                        consulationType = CGlobalVariables.TYPE_CHAT;
                    } else if (firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CALL)) {
                        consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_ai_astrologers));
                        first_call_in_1rs_tv.setText(callChatText.replace("#", mContext.getResources().getString(R.string.call)));
                        btnCallNow.setText(mContext.getResources().getString(R.string.call_now));
                        consulationType = CGlobalVariables.TYPE_CALL;
                    }else {
                        consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_ai_astrologers));
                        consulationType = CGlobalVariables.TYPE_CHAT;
                    }
                }else{

                    switch (firstConsultType) {
                        case com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL:
                            consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_ai_astrologers));
                            first_call_in_1rs_tv.setVisibility(View.GONE);
                            btnCallNow.setText(mContext.getResources().getString(R.string.call_now));
                            consulationType = CGlobalVariables.TYPE_CALL;
                            break;
                        case com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CALL:
                            consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_astrologers));
                            first_call_in_1rs_tv.setVisibility(View.GONE);
                            btnCallNow.setText(mContext.getResources().getString(R.string.call_now));
                            consulationType = CGlobalVariables.TYPE_CALL;
                            break;
                        case com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT:
                            consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_astrologers));
                            first_call_in_1rs_tv.setVisibility(View.GONE);
                            btnCallNow.setText(mContext.getResources().getString(R.string.txt_chat_now));
                            consulationType = CGlobalVariables.TYPE_CHAT;
                            break;
                        default:
                            consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.consult_premium_ai_astrologers));
                            first_call_in_1rs_tv.setVisibility(View.GONE);
                            btnCallNow.setText(mContext.getResources().getString(R.string.txt_chat_now));
                            consulationType = CGlobalVariables.TYPE_CHAT;
                            break;
                    }
                }

            } else {
                if(firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT) || firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT)){
                    first_call_in_1rs_tv.setText(mContext.getResources().getString(R.string.first_chat_free));
                    btnCallNow.setText(mContext.getResources().getString(R.string.free_chat));
                    consulationType = CGlobalVariables.TYPE_CHAT;
                } else {
                    first_call_in_1rs_tv.setText(mContext.getResources().getString(R.string.first_calL_free));
                    btnCallNow.setText(mContext.getResources().getString(R.string.get_free_call_now));
                    consulationType = CGlobalVariables.TYPE_CALL;
                }
            }


            //  FontUtils.changeFont(mContext, confuse_about_txt, "fonts/Roboto-Regular.ttf");
            FontUtils.changeFont(mContext, consultPremiumAstrologerTV, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(mContext, first_call_in_1rs_tv, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BLACK);
            FontUtils.changeFont(mContext, btnCallNow, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD);

            //FontUtils.changeFont(mContext, txt_heading_pop_up, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            //FontUtils.changeFont(mContext, txt_sub_heading_pop_up, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            if (viewType.equals(CGlobalVariables.NUMEROLOGY)) {
                consultPremiumAstrologerTV.setText(mContext.getResources().getString(R.string.pop_up_2sub_heading_num));
            }
        } catch (Exception e) {
            //
        }
    }

    private void updateViewTypeEventsCallNow() {
        try {
            switch (viewType) {
                case CGlobalVariables.HOROSCOPE_MATCHING:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_HM_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
                case CGlobalVariables.KUNDALI:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_K_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
                case CGlobalVariables.NUMEROLOGY:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_N_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
                case CGlobalVariables.PANCHNAG:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_P_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
                case CGlobalVariables.HOROSCOPE:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_H_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
            }
        } catch (Exception e) {
            //
        }
    }

    private void initEnterMobileView(View view) {
        enterPhoneNumberLayout = view.findViewById(R.id.enterPhoneNumberLayout);
        mobileNumberTxt = view.findViewById(R.id.mobile_number_txt);
        country_code_layout = view.findViewById(R.id.country_code_layout);
        getOtpBtn = view.findViewById(R.id.get_otp_btn);
        countryCodeText = view.findViewById(R.id.country_code);
        TextView login_heading_txt = view.findViewById(R.id.login_heading_txt);
        if (!CUtils.getCountryCode(mContext).equals("91") || popUpCalledFrom.equals("ADAPTER")) {
            login_heading_txt.setText(mContext.getResources().getString(R.string.enter_mobile_number));
        } else if(popUpCalledFrom.equals("ONLY_LOGIN")) {
            login_heading_txt.setText(mContext.getResources().getString(R.string.enter_mobile_number));
        }
    }

    private void initOtpView(View view) {
        enterOtpLayout = view.findViewById(R.id.enterOtpLayout);
        ll_otp = view.findViewById(R.id.ll_otp);
        otpEdt1 = view.findViewById(R.id.otp_edt1);
        otpEdt2 = view.findViewById(R.id.otp_edt2);
        otpEdt3 = view.findViewById(R.id.otp_edt3);
        otpEdt4 = view.findViewById(R.id.otp_edt4);
        verifyBtn = view.findViewById(R.id.verify_btn);
        resendOtp = view.findViewById(R.id.resend_otp);
        getOtpViaPhone = view.findViewById(R.id.get_otp_via_phone);
        textViewTimer = view.findViewById(R.id.textViewTimer);
        edit_number = view.findViewById(R.id.edit_number);
        edit_number.setPaintFlags(edit_number.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        resendOtp.setText(Html.fromHtml(mContext.getResources().getString(R.string.resend_otp)));
        getOtpViaPhone.setText(Html.fromHtml(mContext.getResources().getString(R.string.get_otp_via_phone)));
        ll_change_mobile_no = view.findViewById(R.id.ll_change_mobile_no);
        FontUtils.changeFont(mContext, countryCodeText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext, getOtpBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(mContext, mobileNumberTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext, verifyBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(mContext, resendOtp, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext, getOtpViaPhone, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext, textViewTimer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(mContext, otpEdt1, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext, otpEdt2, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext, otpEdt3, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext, otpEdt4, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        cursorMoveOtpAutomatically();
        if(!CUtils.getUserLoginStatus(mContext)&& !popUpCalledFrom.equals("ONLY_LOGIN")) {
            if (!CUtils.getCountryCode(mContext).equals("91") || popUpCalledFrom.equals("ADAPTER")) {
                verifyBtn.setText(mContext.getResources().getString(R.string.connect_free_chat_now));
                consulationType = CGlobalVariables.TYPE_CHAT;
            } else {
                if (enabledAIFreeChatPopup) {
                    verifyBtn.setText(mContext.getResources().getString(R.string.connect_free_chat_now));
                    consulationType = CGlobalVariables.TYPE_CHAT;
                } else {
                    verifyBtn.setText(mContext.getResources().getString(R.string.get_free_call_now));
                    consulationType = CGlobalVariables.TYPE_CALL;
                }
            }
        } else {
            verifyBtn.setText(mContext.getResources().getString(R.string.login));
        }
        setOnClickListeners();
        checkCacheCountryListData();
        initEditTextPasteListner();
        cursorMoveOtpAutomatically();
    }

    private void init(View view) {
        queue = VolleySingleton.getInstance(mContext).getRequestQueue();
        ImageView dismissDialog = view.findViewById(R.id.dismissDialog);
        dismissDialog.setOnClickListener(view1 -> {
            CUtils.isPopUpLoginShowing = false;
            dismiss();

        });
    }

    private void setOnClickListeners() {
        btnCallNow.setOnClickListener(this);
        country_code_layout.setOnClickListener(this);
        getOtpBtn.setOnClickListener(this);
        verifyBtn.setOnClickListener(this);
        resendOtp.setOnClickListener(this);
        getOtpViaPhone.setOnClickListener(this);
        ll_change_mobile_no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCallNow:
                updateViewTypeEventsCallNow();
                com.ojassoft.astrosage.utils.CUtils.addFcmAnalytics(astroImageIdRes);
                if (!CUtils.getUserLoginStatus(mContext)) {
                    enterPhoneNumberLayout.setVisibility(View.VISIBLE);
                    enterOtpLayout.setVisibility(View.GONE);
                    mainViewDialoag.setVisibility(View.GONE);
                } else {
                    CUtils.popUpLoginFreeChatClicked = false;
                    CUtils.popUpLoginFreeCallClicked = false;
                    String firstConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_CONSULT_TYPE,"");
                    String secondFreeChatType = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE,"");
                    boolean isSecondFreeChat = CUtils.isSecondFreeChat(mContext);
                    String offerType = CUtils.getCallChatOfferType(getActivity());

                    if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                        eventsFreeCallChat();
                        if(isSecondFreeChat && secondFreeChatType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)){
                            CUtils.popUpLoginFreeChatClicked = true;
                            AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                            CUtils.fcmAnalyticsEvents("domestic_free_chat_pop_up", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                        }else if(firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL) || firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CALL)){
                            CUtils.popUpLoginFreeCallClicked = true;
                            AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
                            CUtils.fcmAnalyticsEvents("domestic_free_call_pop_up", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                        }else{
                            CUtils.popUpLoginFreeChatClicked = true;
                            AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                            CUtils.fcmAnalyticsEvents("domestic_free_chat_pop_up", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                        }

                    }

                    if(firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT)
                            || firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)){
                        CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CALL, mContext);//redirect to AI list
                    }else{
                        CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CHAT, mContext);//redirect to Human list
                    }

                    CUtils.isPopUpLoginShowing = false;
                    if (!(getActivity() instanceof DetailedHoroscope)) {
                        dismiss();
                    }
                }
                break;
            case R.id.country_code_layout:
                showDialog();
                break;
            case R.id.get_otp_btn:
                mobileNumber = mobileNumberTxt.getText().toString().trim();
                if (!isClickGetOtpBtn) {
                    if (isValidData(mobileNumber)) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_LOGIN_GET_OTP,
                                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                        if (!CUtils.checkReceiveSmsPermission(mContext)) {
                            CUtils.requestReceiveSmsPermission(getActivity());
                            isClickGetOtpBtn = false;
                        } else {
                            isClickGetOtpBtn = true;
                            sendOtpToMobile(mobileNumber);
                        }
                    } else {
                        isClickGetOtpBtn = false;
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.enter_valid_mobile_no), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.verify_btn:
                String otpNumberStr = otpEdt1.getText().toString().trim() + otpEdt2.getText().toString().trim() +
                        otpEdt3.getText().toString().trim() + otpEdt4.getText().toString().trim();
                if (isValidDataOtp(otpNumberStr)) {
                    verifyOtp(otpNumberStr);
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.enter_valid_otp_no), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.resend_otp:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_RESEND_OTP_BTN,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
                intNumberOfTimesResendPress = intNumberOfTimesResendPress + 1;
                if (intNumberOfTimesResendPress <= CGlobalVariables.RESET_LIMIT) {
//                    if (istimerForUnregisteringBroadcastRecieverrunning) {
//                        timerForUnregisteringBroadcastReciever.cancel();
//                        istimerForUnregisteringBroadcastRecieverrunning = false;
//                    }
//                    functionForUnregisteringBroadcastReceiever();
                    setOtpTimer();
                    callLoginReSendOtpApi();
                    //resendOtp(CGlobalVariables.REGISTRATION_URL, RESEND_OTP_METHOD);
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.resend_otp_max_limit), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.get_otp_via_phone:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_RESEND_OTP_VIA_PHONE_BTN,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                intNumberOfTimesResendPress = intNumberOfTimesResendPress + 1;
                if (intNumberOfTimesResendPress <= CGlobalVariables.RESET_LIMIT) {
                    setOtpTimer();
                    callLoginReSendViaCallOtpApi();
                    //resendOtp(CGlobalVariables.CALL_OVER_OTP_URL, RESEND_OTP_VIA_PHONE_METHOD);
                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.resend_otp_max_limit), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_change_mobile_no:
                enterPhoneNumberLayout.setVisibility(View.VISIBLE);
                enterOtpLayout.setVisibility(View.GONE);
                mainViewDialoag.setVisibility(View.GONE);
                resetOtpTimerAndViews();
                break;
        }
    }


    private void verifyOtp(String otpData) {
        CUtils.hideMyKeyboard(getActivity());
        if (!CUtils.isConnectedWithInternet(mContext)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(mContext);
            pd.show();
            pd.setCancelable(false);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.verifyOtp(getParamsOTp(otpData));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        isClickGetOtpBtn = false;
                        if (mContext == null) {
                            return;
                        }
                        hideProgressBar();
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String errorCode = mContext.getResources().getString(R.string.error_code).replace("#", status);
                        if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
                            if(newUser != null && newUser.equals(NEW_USER_OTP_SEND)){//new registration
                                CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, "new_register", "PopUpLogin");
                            }
                            CUtils.saveBannerData("");
                            CUtils.startFollowerSubscriptionService(mContext);

                            com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_NOT_LOGGEDIN, mContext);
                            com.ojassoft.astrosage.varta.utils.CUtils.subscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_LOGGEDIN, mContext);

                            UserProfileData userProfileDataBean = parseJsonObject(myResponse);
                            // CUtils.saveBooleanData(mContext, CGlobalVariables.PREF_FIRST_INSTALL_APP_KEY, true);
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SIGNUP_LOGIN_SUCCESS,
                                    CGlobalVariables.FIREBASE_EVENT_SIGNUP_LOGIN, "");
                            //save password in preference
                            CUtils.setUserLoginPassword(mContext, jsonObject.getString(KEY_PASSWORD));
                            CUtils.setUserOffers(mContext, userProfileDataBean.isLiveintrooffer(), userProfileDataBean.getPrivateintrooffertype());
                            CUtils.setSecondFreeChat(mContext, userProfileDataBean.isSecondFreeChat());

                            String bonusStatus = "0", isShowOneRsPopup = "0", amountOnPopup = "0", freeSessionPopUp = "0";
                            if (jsonObject.has("eligibleforsignupbonus")) {
                                bonusStatus = jsonObject.getString("eligibleforsignupbonus");
                            }
                            if (jsonObject.has("showonerepopup")) {
                                isShowOneRsPopup = jsonObject.getString("showonerepopup");
                                CUtils.setDataForOneRsDialog(mContext, isShowOneRsPopup);
                            }
                            if (jsonObject.has("showfreesessionpopup")) {
                                freeSessionPopUp = jsonObject.getString("showfreesessionpopup");
                                CUtils.setDataForFreeSessionDialog(mContext, freeSessionPopUp);
                            }
                            if (jsonObject.has("amountonpopup")) {
                                amountOnPopup = jsonObject.getString("amountonpopup");
                                CUtils.setAmountOnDialog(mContext, amountOnPopup);
                            }

                            try {
                                CUtils.setUserIdForBlock(mContext, jsonObject.getString("userid"));
                                String blockedby = jsonObject.getString("blockedby");
                                String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
                                CUtils.setblockByAstrologerList(blockByAstrologer);
                            } catch (Exception e) {
                            }

                            CUtils.saveLoginDetailInPrefs(mContext, mobileNumber, true, userProfileDataBean.getWalletbalance(), bonusStatus);
                            try {
//                                if(!TextUtils.isEmpty(userProfileDataBean.getPrivateintrooffertype()) && userProfileDataBean.getPrivateintrooffertype().equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
//                                    CUtils.saveAiRandomChatAstroDeatils("");
//                                    CUtils.getAiRandomChatAstroDetail(getActivity());
//                                }
                                String regid = CUtils.getRegistrationId(mContext.getApplicationContext());
                                new CGCMRegistrationInfoSaveOnOjas().saveUserGCMRegistrationInformationOnOjasServer(AstrosageKundliApplication.getAppContext(),
                                        regid, 0, mobileNumber);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(!popUpCalledFrom.equals("ONLY_LOGIN")){
                                String offerType = CUtils.getCallChatOfferType(getActivity());

                                boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(getActivity());
                                if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                                    eventsFreeCallChat();
                                    if (!TextUtils.isEmpty(mobileNumber)) {
                                        if (!CUtils.getCountryCode(mContext).equals("91")) {
                                            CUtils.popUpLoginFreeChatClicked = true;
                                            AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                                            CUtils.fcmAnalyticsEvents("international_free_chat_pop_up_verify_otp", AstrosageKundliApplication.currentEventType, "");

                                        } else {
                                            if(enabledAIFreeChatPopup) {
                                                CUtils.popUpLoginFreeChatClicked = true;
                                                AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                                                CUtils.fcmAnalyticsEvents("pop_up_login_free_chat_verify_otp", AstrosageKundliApplication.currentEventType, "");
                                            } else {
                                                CUtils.popUpLoginFreeCallClicked = true;
                                                AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
                                                CUtils.fcmAnalyticsEvents("pop_up_login_free_call_verify_otp", AstrosageKundliApplication.currentEventType, "");
                                            }
                                        }
                                    }
                                } else {
                                    if (mContext != null)
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.avail_all_ready_offer), Toast.LENGTH_SHORT).show();
                                }
                                /*if(consulationType.equals(CGlobalVariables.TYPE_CALL)){
                                    CUtils.openCallList(getActivity());
                                }else {//type chat
                                    boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(getActivity());
                                    if (isAIChatDisplayed && enabledAIFreeChatPopup) {
                                        CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CALL, getActivity());//redirect to AI list
                                    } else {
                                        CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CHAT,getActivity());
                                    }
                                }*/

                                CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CALL, getActivity());//redirect to AI list
                            }
                            handleAstroSageLogin(jsonObject);
                            CUtils.isPopUpLoginShowing = false;
                            dismiss();
                        } else if (status.equals(CGlobalVariables.OTP_EXPIRED)) {
                            String msg = jsonObject.getString("msg");

                            if (msg.equalsIgnoreCase(CGlobalVariables.FBA_OTP_EXPIRED)) {
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_EXPIRED,
                                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.otp_expired) + " " + errorCode, Toast.LENGTH_SHORT).show();
                            } else {
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_INCORRECT,
                                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.otp_is_wrong) + " " + errorCode, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
                        }
                        CUtils.saveAstroList("");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
//                    CUtils.showSnackbar(containerLayout, e.toString(), mContext);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isClickGetOtpBtn = false;
                    hideProgressBar();

                }
            });
        }
    }

    private void callLoginReSendOtpApi() {
        if (!CUtils.isConnectedWithInternet(mContext)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(mContext);
            pd.show();
            pd.setCancelable(false);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.reSendOtp(getParamsForResend());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        isClickGetOtpBtn = false;
                        if (mContext == null) {
                            return;
                        }
                        hideProgressBar();
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String errorCode = mContext.getResources().getString(R.string.error_code).replace("#", status);
                        String msg = jsonObject.optString("msg");
                        if (status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_EXISTING_USER) || status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_NEW_USER)) {
                            if (TextUtils.isEmpty(msg)) {
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.resend_otp_success_msg), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error) + " " + errorCode, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isClickGetOtpBtn = false;
                    hideProgressBar();

                }
            });
        }

    }

    private void callLoginReSendViaCallOtpApi() {
        if (!CUtils.isConnectedWithInternet(mContext)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(mContext);
            pd.show();
            pd.setCancelable(false);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.reSendViaCallOtp(getParamsForResend());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        isClickGetOtpBtn = false;
                        if (mContext == null) {
                            return;
                        }
                        hideProgressBar();
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String errorCode = mContext.getResources().getString(R.string.error_code).replace("#", status);

                        if (status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_NEW_USER)) {
                            //CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_success_msg), OtpVerifyActivity.this);
                        } else {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.resend_otp_failed_msg) + " " + errorCode, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    isClickGetOtpBtn = false;
                    hideProgressBar();
                }
            });
        }

    }

//    private void resendOtp(String otpUrl, int method) {
//        if (!CUtils.isConnectedWithInternet(mContext)) {
//            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//        } else {
//            if (pd == null)
//                pd = new CustomProgressDialog(mContext);
//            pd.show();
//            pd.setCancelable(false);
//
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, otpUrl,
//                    this, false, getParamsForResend(), method).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
//        }
//    }

    public Map<String, String> getParamsForResend() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(CGlobalVariables.PHONE_NO, mobileNumber);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(mContext));
        headers.put(CGlobalVariables.FROM_RESEND, String.valueOf(resendOtpCount));
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(mContext));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(mContext));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_SIGNUP);

        return headers;
    }

    /**
     * @param mobileNo
     * @return
     */
    private boolean isValidData(String mobileNo) {
        boolean isValid = mobileNo != null || mobileNo.trim().length() != 0;

        if (CUtils.getCountryCode(mContext).equals("91")) {
            if (mobileNo.trim().length() < 10) {
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * @param otpNo
     * @return
     */
    private boolean isValidDataOtp(String otpNo) {
        boolean isValid = otpNo != null || otpNo.trim().length() != 0;
        if (otpNo.trim().length() < 4) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * @param mobNo
     */
    private void sendOtpToMobile(String mobNo) {
        CUtils.hideMyKeyboard(getActivity());
        if (!CUtils.isConnectedWithInternet(mContext)) {
            isClickGetOtpBtn = false;
            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(mContext);
            pd.show();
            pd.setCancelable(false);

//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.REGISTRATION_URL,
//                    this, false, getParams(mobNo), FETCH_OTP_METHOD).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            callLoginSendOtpApi(mobNo);
        }
    }

    private void callLoginSendOtpApi(String mobNo) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.sendOtp(getParams(mobNo));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    isClickGetOtpBtn = false;
                    if (mContext == null) {
                        return;
                    }
                    hideProgressBar();
                    String myResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(myResponse);
                    String status = jsonObject.getString("status");

                    if (status.equals(CGlobalVariables.USER_ALREADY_EXIST_OTP_SEND) || status.equals(CGlobalVariables.NEW_USER_OTP_SEND)) {
                        if (status.equals(CGlobalVariables.NEW_USER_OTP_SEND)) {
                            newUser = status;
                        }
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("as");
                            JSONObject jsonObjectAS = jsonArray.getJSONObject(0);
                            astroSageUserId = jsonObjectAS.getString("userid");
                        } catch (Exception e) {
                            //
                        }
                        String msg = jsonObject.optString("msg");
                        if (!TextUtils.isEmpty(msg)) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                        // show otp views
                        setOtpTimer();

                    } else if (status.equals(CGlobalVariables.USER_OTP_MAX_LIMIT)) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.otp_max_limit), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.invalid_mobie_no), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isClickGetOtpBtn = false;
                hideProgressBar();
            }
        });
    }


    /**
     * @param mobileNo
     * @return
     */
    public Map<String, String> getParams(String mobileNo) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(CGlobalVariables.PHONE_NO, mobileNo);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(mContext));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(mContext));
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(mContext));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_SIGNUP);
        //Log.d("LoginFlow", " popup headers: " + headers);
        return headers;
    }

    public Map<String, String> getParamsOTp(String otpNumber) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(CGlobalVariables.PHONE_NO, mobileNumber);
        headers.put(CGlobalVariables.OTP_NO, otpNumber);
        headers.put(CGlobalVariables.NEW_USER, newUser);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(mContext));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        headers.put(CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_LOGIN);

        int kundliCount = com.ojassoft.astrosage.utils.CUtils.getKundliCount(mContext);
        String strFirstLoginAfterPlanPurchase = "";
        if (com.ojassoft.astrosage.utils.CUtils.getBooleanData(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false)) {
            strFirstLoginAfterPlanPurchase = com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_LOGIN_AFTER_PLAN_PURCHASED;
        }
        if (astroSageUserId == null) astroSageUserId = "";

        headers.put(KEY_USER_ID, astroSageUserId);
        headers.put("firstloginafterplanpurchase", strFirstLoginAfterPlanPurchase);
        headers.put("isverified", "1");
        headers.put("nocharts", String.valueOf(kundliCount));

        return CUtils.setRequiredParams(headers);
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.d("LoginFlow", " popup response: " + response);
        isClickGetOtpBtn = false;
        if (mContext == null) {
            return;
        }
        hideProgressBar();
        //Log.d("TestDialog", " Method = " + method + " Response = " + response);
        if (response != null && response.length() > 0) {
            if (method == 2) {
                parseCountryList(response);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
            if (method == FETCH_OTP_METHOD) {
                {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String status = jsonObject.getString("status");
//
//                        if (status.equals(CGlobalVariables.USER_ALREADY_EXIST_OTP_SEND) || status.equals(CGlobalVariables.NEW_USER_OTP_SEND)) {
//                            if (status.equals(CGlobalVariables.NEW_USER_OTP_SEND)) {
//                                newUser = status;
//                            }
//                            try {
//                                JSONArray jsonArray = jsonObject.getJSONArray("as");
//                                JSONObject jsonObjectAS = jsonArray.getJSONObject(0);
//                                astroSageUserId = jsonObjectAS.getString("userid");
//                            } catch (Exception e) {
//                                //
//                            }
//                            String msg = jsonObject.optString("msg");
//                            if(!TextUtils.isEmpty(msg)) {
//                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
//                            }
//                            // show otp views
//                            setOtpTimer();
//
//                        } else if (status.equals(CGlobalVariables.USER_OTP_MAX_LIMIT)) {
//                            Toast.makeText(mContext, mContext.getResources().getString(R.string.otp_max_limit), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(mContext, mContext.getResources().getString(R.string.invalid_mobie_no), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }
            if (method == VERIFY_OTP_METHOD) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    String errorCode = mContext.getResources().getString(R.string.error_code).replace("#", status);
//                    if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
//                        CUtils.saveBannerData("");
//                        //startPrefatchDataService();
//                        CUtils.startFollowerSubscriptionService(mContext);
//
//                        com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_NOT_LOGGEDIN, mContext);
//                        com.ojassoft.astrosage.varta.utils.CUtils.subscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_LOGGEDIN, mContext);
//
//                        UserProfileData userProfileDataBean = parseJsonObject(response);
//                        // CUtils.saveBooleanData(mContext, CGlobalVariables.PREF_FIRST_INSTALL_APP_KEY, true);
//                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SIGNUP_LOGIN_SUCCESS,
//                                CGlobalVariables.FIREBASE_EVENT_SIGNUP_LOGIN, "");
//                        //save password in preference
//                        CUtils.setUserLoginPassword(mContext, jsonObject.getString(KEY_PASSWORD));
//                        CUtils.setUserOffers(mContext, userProfileDataBean.isLiveintrooffer(), userProfileDataBean.getPrivateintrooffertype());
//
//                        String bonusStatus = "0", isShowOneRsPopup = "0", amountOnPopup = "0", freeSessionPopUp = "0";
//                        ;
//                        if (jsonObject.has("eligibleforsignupbonus")) {
//                            bonusStatus = jsonObject.getString("eligibleforsignupbonus");
//                        }
//                        if (jsonObject.has("showonerepopup")) {
//                            isShowOneRsPopup = jsonObject.getString("showonerepopup");
//                            CUtils.setDataForOneRsDialog(mContext, isShowOneRsPopup);
//                        }
//                        if (jsonObject.has("showfreesessionpopup")) {
//                            freeSessionPopUp = jsonObject.getString("showfreesessionpopup");
//                            CUtils.setDataForFreeSessionDialog(mContext, freeSessionPopUp);
//                        }
//                        if (jsonObject.has("amountonpopup")) {
//                            amountOnPopup = jsonObject.getString("amountonpopup");
//                            CUtils.setAmountOnDialog(mContext, amountOnPopup);
//                        }
//
//                        try {
//                            CUtils.setUserIdForBlock(mContext, jsonObject.getString("userid"));
//                            String blockedby = jsonObject.getString("blockedby");
//                            String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
//                            CUtils.setblockByAstrologerList(blockByAstrologer);
//                        } catch (Exception e) {
//                        }
//
//                        CUtils.saveLoginDetailInPrefs(mContext, mobileNumber, true, userProfileDataBean.getWalletbalance(), bonusStatus);
//                        try {
//                            String regid = CUtils.getRegistrationId(mContext.getApplicationContext());
//                            new CGCMRegistrationInfoSaveOnOjas().saveUserGCMRegistrationInformationOnOjasServer(AstrosageKundliApplication.getAppContext(),
//                                    regid, 0, mobileNumber);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
////                        if(isFromScreen != null && isFromScreen.equals(CGlobalVariables.PAYMENT_INFO_SCREEN)){
////                            setResult(RESULT_OK);
////                        }else {
//                        String offerType = CUtils.getCallChatOfferType(getActivity());
//                        if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
//                            eventsFreeCallChat();
//                            if (!TextUtils.isEmpty(mobileNumber)) {
//                                if (!CUtils.getCountryCode(mContext).equals("91")) {
//                                    CUtils.popUpLoginFreeChatClicked = true;
//                                    AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
//                                    CUtils.fcmAnalyticsEvents("international_free_chat_pop_up_verify_otp", AstrosageKundliApplication.currentEventType, "");
//
//                                } else {
//                                    CUtils.popUpLoginFreeCallClicked = true;
//                                    AstrosageKundliApplication.currentEventType  = CGlobalVariables.CALL_BTN_CLICKED;
//                                    CUtils.fcmAnalyticsEvents("pop_up_login_free_call_verify_otp",AstrosageKundliApplication.currentEventType,"");
//
//                                }
//                            }
//                        } else {
//                            if (mContext != null)
//                                Toast.makeText(mContext, mContext.getResources().getString(R.string.avail_all_ready_offer), Toast.LENGTH_SHORT).show();
//                        }
//
//                        handleAstroSageLogin(jsonObject);
//
//                        Intent i = new Intent(mContext, DashBoardActivity.class);
//                        i.putExtra(CGlobalVariables.USER_DATA, userProfileDataBean);
//                        i.putExtra(CGlobalVariables.IS_FROM_SCREEN, "popUpLogin");
//                        startActivity(i);
//                        CUtils.isPopUpLoginShowing = false;
//                        dismiss();
//                        // }
//                        //finish();
//
//                    } else if (status.equals(CGlobalVariables.OTP_EXPIRED)) {
//                        String msg = jsonObject.getString("msg");
//
//                        if (msg.equalsIgnoreCase(CGlobalVariables.FBA_OTP_EXPIRED)) {
//                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_EXPIRED,
//                                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//                            Toast.makeText(mContext, mContext.getResources().getString(R.string.otp_expired) + " " + errorCode, Toast.LENGTH_SHORT).show();
//                        } else {
//                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_INCORRECT,
//                                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//                            Toast.makeText(mContext, mContext.getResources().getString(R.string.otp_is_wrong) + " " + errorCode, Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
//                    }
//                    CUtils.saveAstroList("");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
////                    CUtils.showSnackbar(containerLayout, e.toString(), mContext);
//                }
            }
            if (method == RESEND_OTP_METHOD) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    String errorCode = mContext.getResources().getString(R.string.error_code).replace("#", status);
//                    String msg = jsonObject.optString("msg");
//                    if (status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_EXISTING_USER) || status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_NEW_USER)) {
//                        if(TextUtils.isEmpty(msg)) {
//                            Toast.makeText(mContext, mContext.getResources().getString(R.string.resend_otp_success_msg), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error) + " " + errorCode, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
//                }
            }
            if (method == RESEND_OTP_VIA_PHONE_METHOD) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    String errorCode = mContext.getResources().getString(R.string.error_code).replace("#", status);
//
//                    if (status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_NEW_USER)) {
//                        //CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_success_msg), OtpVerifyActivity.this);
//                    } else {
//                        Toast.makeText(mContext, mContext.getResources().getString(R.string.resend_otp_failed_msg) + " " + errorCode, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(mContext, mContext.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        //Log.d("LoginFlow", " popup error: " + error);
        isClickGetOtpBtn = false;
        hideProgressBar();
    }

    private void handleAstroSageLogin(JSONObject jsonObject) {
        try {
            String astroSageUserId = com.ojassoft.astrosage.utils.CUtils.getUserName(getActivity());
            if (com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(getActivity()) || !TextUtils.isEmpty(astroSageUserId)) {
                return; //if already loggedin with astrosage-id then return
            }
            //Log.d("LoginFlow", " handleAstroSageLogin()1");
            JSONArray jsonArray = jsonObject.getJSONArray("as");
            JSONObject respObj = jsonArray.getJSONObject(0);

            String respCode = respObj.getString("msgcode");
            if (respCode.equals("22")) {               //if plan activated successfully
                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false);
            } else if (respCode.equals("3")) {
                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false);
            }

            String _userId = "", _pwd = "";
            HashMap<String, String> jsonObjHash = com.ojassoft.astrosage.utils.CUtils.parseLoginSignupJson(respObj);
            if (jsonObjHash.size() > 0) {
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERID)) {
                    _userId = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERID);
                }
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD)) {
                    _pwd = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD);
                }
                CUtils.saveInformation(getActivity(), _userId, _pwd, jsonObjHash);
            }
            //Log.d("LoginFlow", " handleAstroSageLogin()2");
        } catch (Exception e) {
            //
        }
    }

    private UserProfileData parseJsonObject(String responseResult) {
        //  {"status":"1", "place":"Delhi","day":"02","month":"10","year":"1994","hour":"13","minute":"15","second":"00","longew":"E","latdeg":"28","longdeg":"77","latmin":"40","longmin":"13","latns":"N","timezone":"5.5"}
        UserProfileData userProfileDataBean;
        try {
            userProfileDataBean = new UserProfileData();
            JSONObject jsonObject = new JSONObject(responseResult);
            Gson gson = new Gson();
            userProfileDataBean = gson.fromJson(jsonObject.toString(), UserProfileData.class);

            CUtils.saveUserSelectedProfileInPreference(mContext, userProfileDataBean);
            CUtils.saveProfileForChatInPreference(mContext, userProfileDataBean);

        } catch (Exception e) {
            e.printStackTrace();
            userProfileDataBean = null;
        }

        return userProfileDataBean;
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

    private void checkCacheCountryListData() {
        String url = CGlobalVariables.COUNTRY_LIST_URL;
        Cache cache = VolleySingleton.getInstance(mContext).getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1-" + url);
        String saveData = "";
        // cache data
        try {
            if (entry != null) {
                saveData = new String(entry.data, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(saveData)) {
            saveData = "{\"countries\":[{\"name\":\"India\",\"code\":\"91\"},{\"name\":\"USA\",\"code\":\"1\"},{\"name\":\"UK\",\"code\":\"44\"},{\"name\":\"Australia\",\"code\":\"61\"},{\"name\":\"Brazil\",\"code\":\"55\"},{\"name\":\"Canada\",\"code\":\"1\"},{\"name\":\"France\",\"code\":\"33\"},{\"name\":\"Germany\",\"code\":\"49\"},{\"name\":\"Israel\",\"code\":\"972\"},{\"name\":\"Japan\",\"code\":\"81\"},{\"name\":\"New Zealand\",\"code\":\"64\"},{\"name\":\"Singapore\",\"code\":\"65\"},{\"name\":\"Saudi Arabia\",\"code\":\"966\"},{\"name\":\"United Arab Emirates\",\"code\":\"971\"}]}";
        }
        isShowProgressBar = false;
        parseCountryList(saveData);

        if (!CUtils.isConnectedWithInternet(mContext)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            if (isShowProgressBar) {
                if (pd == null)
                    pd = new CustomProgressDialog(mContext);
                pd.show();
                pd.setCancelable(false);
                isShowProgressBar = false;
            }
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.COUNTRY_LIST_URL,
//                    this, false, getParamsCountryList(), 2).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getCountryList(getParamsCountryList());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {
                            String myResponse = response.body().string();
                            parseCountryList(myResponse);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }

    public Map<String, String> getParamsCountryList() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    private void showDialog() {
        String strFilter;
        final EditText inputSearch;
        final Dialog dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(true);

        View view = getLayoutInflater().inflate(R.layout.lay_city_custompopup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);

        inputSearch = view.findViewById(R.id.edtcountry);
        strFilter = inputSearch.getText().toString();
        RecyclerView recyclerView = view.findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        adapter = new CountryListAdapter(mContext, countryBeanList);
        recyclerView.setAdapter(adapter);
        CUtils.setCountryCode(mContext, "+91");
        adapter.setOnClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (position != -1) {
                    CountryBean countryBean = countryBeanList.get(position);
                    if (countryBean != null) {
                        String countryCode = countryBean.getCountryCode();
                        countryCodeText.setText(countryBean.getCountryName() + " (+" + countryCode + ")");
                        CUtils.setCountryCode(mContext, countryCode);
                        if (countryCodeText.getText().toString().contains("+91")) {
                            mobileNumberTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        } else {
                            mobileNumberTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                        }
                    }
                    dialog.dismiss();
                    try {
                        if (adapter != null) {
                            countryBeanList = (ArrayList<CountryBean>) adapter.filter("");
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                countryBeanList = (ArrayList<CountryBean>) adapter.filter(text);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void parseCountryList(String saveData) {
        if (saveData != null && saveData.length() > 0) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(saveData);
                JSONArray jsonArray = jsonObject.getJSONArray("countries");
                if (jsonArray != null && jsonArray.length() > 0) {
                    countryBeanList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CountryBean countryBean = new CountryBean();
                        countryBean.setCountryName(object.getString("name"));
                        countryBean.setCountryCode(object.getString("code"));
                        countryBeanList.add(countryBean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void cursorMoveOtpAutomatically(){
        otpEdt1.addTextChangedListener(new PopUpLogin.GenericTextWatcher(otpEdt1, otpEdt2));
        otpEdt2.addTextChangedListener(new PopUpLogin.GenericTextWatcher(otpEdt2, otpEdt3));
        otpEdt3.addTextChangedListener(new PopUpLogin.GenericTextWatcher(otpEdt3, otpEdt4));
        otpEdt4.addTextChangedListener(new PopUpLogin.GenericTextWatcher(otpEdt4, null));

        otpEdt2.setOnKeyListener( new PopUpLogin.GenericKeyEvent( otpEdt2 , otpEdt1 ) );
        otpEdt3.setOnKeyListener( new PopUpLogin.GenericKeyEvent( otpEdt3 , otpEdt2 ) );
        otpEdt4.setOnKeyListener( new PopUpLogin.GenericKeyEvent( otpEdt4 , otpEdt3 ) );
    }

    /*private void cursorMoveOtpAutomatically() {

        otpEdt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    otpEdt2.requestFocus();
                }
            }
        });
        otpEdt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    otpEdt3.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    otpEdt1.requestFocus();
                }
            }
        });
        otpEdt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    otpEdt4.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    otpEdt2.requestFocus();
                }
            }
        });
        otpEdt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    otpEdt3.requestFocus();
                }
            }
        });

    }*/

    private void initEditTextPasteListner() {
        otpEdt1.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEdt2.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEdt3.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEdt4.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
    }

    private void onOtpPaste() {
        try {
            String otpStr = getCopiedOtpFromClipboard();
            autoFillOtp(otpStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCopiedOtpFromClipboard() {
        try {
            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null && clipboard.hasPrimaryClip()) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                String pasteOtp = item.getText().toString();
                if (!TextUtils.isEmpty(pasteOtp) && pasteOtp.length() == 4) {
                    return pasteOtp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void autoFillOtp(String otp) {
        try {
            otpEdt1.setText(String.valueOf(otp.charAt(0)));
            otpEdt2.setText(String.valueOf(otp.charAt(1)));
            otpEdt3.setText(String.valueOf(otp.charAt(2)));
            otpEdt4.setText(String.valueOf(otp.charAt(3)));
            verifyBtn.performClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } catch (Exception e) {
            //
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void setOtpTimer() {
        if (!TextUtils.isEmpty(mobileNumber)) {
            if (!CUtils.getCountryCode(mContext).equals("91") || popUpCalledFrom.equals("ADAPTER")) {
                verifyBtn.setText(mContext.getResources().getString(R.string.connect_free_chat_now));
                consulationType = CGlobalVariables.TYPE_CHAT;
            } else {
                boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(getActivity());
                if(enabledAIFreeChatPopup){
                    verifyBtn.setText(mContext.getResources().getString(R.string.connect_free_chat_now));
                    consulationType = CGlobalVariables.TYPE_CHAT;
                } else {
                    verifyBtn.setText(mContext.getResources().getString(R.string.get_free_call));
                    consulationType = CGlobalVariables.TYPE_CALL;
                }
            }
        }
        ll_change_mobile_no.setVisibility(View.VISIBLE);
        enterOtpLayout.setVisibility(View.VISIBLE);
        enterPhoneNumberLayout.setVisibility(View.GONE);
        mainViewDialoag.setVisibility(View.GONE);
        resendOtp.setOnClickListener(null);
        resendOtp.setClickable(false);
        resendOtp.setAlpha(0.5f);
        getOtpViaPhone.setOnClickListener(null);
        getOtpViaPhone.setClickable(false);
        getOtpViaPhone.setAlpha(0.5f);
        //functionEnablingButtons();
        functionInitializingCountDownTimer();
    }

    private void resetOtpTimerAndViews() {
        countDownTimer.cancel();
        textViewTimer.setVisibility(View.GONE);
        ll_change_mobile_no.setVisibility(View.GONE);
        isClickGetOtpBtn = false;
    }

    public void functionInitializingCountDownTimer() {
        textViewTimer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(longTotalVerificationTime, longOneSecond) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setVisibility(View.VISIBLE);

                String text = String.format(Locale.US, FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                textViewTimer.setText(text);
            }

            @Override
            public void onFinish() {
                textViewTimer.setVisibility(View.INVISIBLE);
                otpEdt1.setFocusable(true);
                otpEdt1.requestFocus();
                otpEdt1.setFocusableInTouchMode(true);
                otpEdt1.invalidate();
                resendOtp.setOnClickListener(PopUpLogin.this);
                resendOtp.setClickable(true);
                resendOtp.setAlpha(1);
                getOtpViaPhone.setOnClickListener(PopUpLogin.this);
                getOtpViaPhone.setClickable(true);
                getOtpViaPhone.setAlpha(1);
            }
        }.start();
    }

    private void dialogAppearEvent() {
        try {
            switch (viewType) {
                case CGlobalVariables.HOROSCOPE_MATCHING:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_HM, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case CGlobalVariables.KUNDALI:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_K, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case CGlobalVariables.NUMEROLOGY:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_N, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case CGlobalVariables.PANCHNAG:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_P, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case CGlobalVariables.HOROSCOPE:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_H, CGlobalVariables.DIALOG_SHOW, "");
                    break;
            }
        } catch (Exception e) {
            //
        }
    }

    private void dialogEnterMobileAppearEvent() {
        try {
            switch (viewType) {
                case CGlobalVariables.HOROSCOPE_MATCHING:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_HM_FROM_TAB, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case CGlobalVariables.KUNDALI:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_K_FROM_TAB, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case CGlobalVariables.NUMEROLOGY:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_N_FROM_TAB, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case CGlobalVariables.PANCHNAG:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_P_FROM_TAB, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case CGlobalVariables.HOROSCOPE:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_H_FROM_TAB, CGlobalVariables.DIALOG_SHOW, "");
                    break;
                case "notification_center":
                    CUtils.fcmAnalyticsEvents("pop_up_login_notification_center", CGlobalVariables.DIALOG_SHOW, "");
                    break;
            }
        } catch (Exception e) {
            //
        }
    }

    private void eventsFreeCallChat() {
        try {
            switch (viewType) {
                case CGlobalVariables.HOROSCOPE_MATCHING:
                    com.ojassoft.astrosage.utils.CUtils.createSession(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.DIALOG_HM_FREE_CALL_CHAT_PARTNER_ID);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_HM_FREE_CALL, CGlobalVariables.FIREBASE_EVENT_FREE_CALL_CHAT_DIALOG, "");
                    break;
                case CGlobalVariables.KUNDALI:
                    com.ojassoft.astrosage.utils.CUtils.createSession(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.DIALOG_K_FREE_CALL_CHAT_PARTNER_ID);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_K_FREE_CALL, CGlobalVariables.FIREBASE_EVENT_FREE_CALL_CHAT_DIALOG, "");
                    break;
                case CGlobalVariables.NUMEROLOGY:
                    com.ojassoft.astrosage.utils.CUtils.createSession(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.DIALOG_N_FREE_CALL_CHAT_PARTNER_ID);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_N_FREE_CALL, CGlobalVariables.FIREBASE_EVENT_FREE_CALL_CHAT_DIALOG, "");
                    break;
                case CGlobalVariables.PANCHNAG:
                    com.ojassoft.astrosage.utils.CUtils.createSession(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.DIALOG_P_FREE_CALL_CHAT_PARTNER_ID);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_P_FREE_CALL, CGlobalVariables.FIREBASE_EVENT_FREE_CALL_CHAT_DIALOG, "");
                    break;
                case CGlobalVariables.HOROSCOPE:
                    com.ojassoft.astrosage.utils.CUtils.createSession(mContext, com.ojassoft.astrosage.utils.CGlobalVariables.DIALOG_H_FREE_CALL_CHAT_PARTNER_ID);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_POP_UP_H_FREE_CALL, CGlobalVariables.FIREBASE_EVENT_FREE_CALL_CHAT_DIALOG, "");
                    break;
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("popup_login_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }

    private class GenericTextWatcher implements TextWatcher {
        private final EditText currentView;
        private final EditText nextView;

        private GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (currentView.getId ()) {
                case (R.id.otp_edt1) :

                case (R.id.otp_edt3) :

                case (R.id.otp_edt2) :
                    if (text.length () == 1) nextView.requestFocus();
                    break;

                case (R.id.otp_edt4) :
                    if (text.length () == 1) checkForAutoClick();
                    break;

                //You can use EditText4 same as above to hide the keyboard
            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    private  class GenericKeyEvent implements View.OnKeyListener {

        private final EditText currentView;
        private final EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction () == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getId () != R.id.otp_edt1 && currentView.getText ().toString ().isEmpty())
            {
                //If current is empty then previous EditText's number will also be deleted
                previousView.setText (null);
                previousView.requestFocus();
                return true;
            }
            return false;
        }
    }

    private void checkForAutoClick(){

        String otpNumberStr = otpEdt1.getText().toString().trim() + otpEdt2.getText().toString().trim() +
                otpEdt3.getText().toString().trim() + otpEdt4.getText().toString().trim();

        if (isValidateOTP(otpNumberStr)){
            verifyBtn.performClick();
        }

    }

    private boolean isValidateOTP(String otpNo) {
        boolean isValid = otpNo != null || otpNo.trim().length() != 0;
        if (otpNo.trim().length() < 4) {
            isValid = false;
        }
        return isValid;
    }

}
