package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GENERATE_TICKET_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GENERATE_TICKET_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GENERATE_TICKET_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GENERATE_TICKET_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;
import static com.ojassoft.astrosage.varta.utils.CUtils.updateContactNumber;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAboutUs;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.varta.ui.fragments.ProfileFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class GenerateTicketActivity extends BaseActivity implements View.OnClickListener {

    Context context;
    TextView tvTitle, tvGenerateTicket, tvCustomerCarePhone,tvCustomerCareEmail;
    LinearLayout llTicketGenerate,phoneLayout;
    LinearLayout navView;
    ImageView ivBack;
    FloatingActionButton fabGenerateTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ticket);
        context = GenerateTicketActivity.this;
        tvTitle = findViewById(R.id.tvTitle);
        tvGenerateTicket = findViewById(R.id.tvGenerateTicket);
        llTicketGenerate = findViewById(R.id.llTicketGenerate);
        navView = findViewById(R.id.nav_view);
        ivBack = findViewById(R.id.ivBack);
        phoneLayout = findViewById(R.id.phone_layout);
        tvCustomerCarePhone = findViewById(R.id.tvCustomerCarePhone);
        tvCustomerCareEmail = findViewById(R.id.tvCustomerCareEmail);
        fabGenerateTicket = findViewById(R.id.fabGenerateTicket);

        tvCustomerCareEmail.setText(Html.fromHtml(getResources().getString(R.string.customer_care_email)));
        FontUtils.changeFont(context, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvGenerateTicket, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvCustomerCarePhone, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvCustomerCareEmail, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        boolean isNumVisible = com.ojassoft.astrosage.varta.utils.CUtils.checkForCsNumberVisibility(this);
        if(isNumVisible){
            phoneLayout.setVisibility(View.VISIBLE);
            updateContactNumber(this, tvCustomerCarePhone);
        }else{
            phoneLayout.setVisibility(View.GONE);
        }
        tvTitle.setText(getResources().getString(R.string.support));
        llTicketGenerate.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvCustomerCarePhone.setOnClickListener(this);
        tvCustomerCareEmail.setOnClickListener(this);
        fabGenerateTicket.setOnClickListener(this);

        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        //navView.getMenu().setGroupCheckable(0, false, true);
        setBottomNavigationText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llTicketGenerate:
                startActivity(new Intent(context, FeedbackActivity.class));
                break;
            case R.id.tvCustomerCarePhone:
                makeCallOn(tvCustomerCarePhone.getText().toString());
                break;
            case R.id.tvCustomerCareEmail:
                composeNewEmail();
                break;
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.fabGenerateTicket:
                fabActions();
                break;
        }
    }

    private void makeCallOn(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void composeNewEmail() {
        String email = getResources().getString(R.string.customer_care_email_id),
                subject = "Feedback And Queries";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        emailIntent.setType("text/plain");
        emailIntent.setData(Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //emailIntent.putExtra(Intent.EXTRA_EMAIL, email);

        startActivity(Intent.createChooser(emailIntent, "Send email.."));
    }

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
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_RECHARGE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                openWalletScreen();
            } else {
                //changeToolbar(CGlobalVariables.PROFILE_FRAGMENT);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_PROFILE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(context, MyAccountActivity.class);
                startActivity(intent);
            }
        }
    }

    public void openWalletScreen() {
        Intent intent = new Intent(context, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        startActivity(intent);
    }

    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            CUtils.openAstroSageHomeActivity(GenerateTicketActivity.this);
                            return true;
                        case R.id.navigation_recharge:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(GenerateTicketActivity.this, GENERATE_TICKET_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, GenerateTicketActivity.this);
                            return true;
                        case R.id.navigation_share:
                            fabActions();
                            return true;
                        case R.id.navigation_notification:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(GenerateTicketActivity.this, GENERATE_TICKET_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, GenerateTicketActivity.this);
                            return true;
                        case R.id.navigation_profile:
                            isUserLogin(new ProfileFragment(), CGlobalVariables.PROFILE_SCRREN);
                            return true;
                    }
                    return false;
                }
            };*/

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
        TextView navHomeTxt = navView.findViewById(R.id.txtViewHome);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);
        TextView navLive = navView.findViewById(R.id.txtViewLive);
        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if (isAIChatDisplayed) {
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
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this, fabGenerateTicket, navLive);

        if (CUtils.getUserLoginStatus(context)) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }
        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }
    private void fabActions(){
        try {
            //boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if(!liveStreamingEnabledForAstrosage){ //fetch data according to tagmanag
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(this, GENERATE_TICKET_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            }
            else {
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(this, GENERATE_TICKET_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(this);
            }
        }catch (Exception e){
            //
        }
    }

    @Override
    public void clickCall() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(GenerateTicketActivity.this, GENERATE_TICKET_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(GenerateTicketActivity.this, GENERATE_TICKET_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

}