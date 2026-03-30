package com.ojassoft.astrosage.varta.ui.activity;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.IPermissionCallback;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.ui.fragments.ProfileFragment;
import com.ojassoft.astrosage.varta.ui.fragments.ReadFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.UserEmailFetcher;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener, VolleyResponse, IPermissionCallback {

    EditText _etName, _etEmailId, _etPhone, _etMsg;
    TextView tvName, tvEmailId, tvPhone, tv_message, tvTitle, tv_feedback,
            tv_customer_care_email, tv_customer_care_phone1, tv_customer_care_phone2, tv_customer_care_phone3, tv_emailid, tv_email_msg, tv_phone, tv_phone_msg;
    Button butSend;
    ImageView ivBack;
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    LinearLayout scrollView;
    CustomProgressDialog pd;
    RequestQueue queue;
    BottomNavigationView navView;
    private CredentialManager credentialManager;
    private static final int RC_HINT_EMAIL = 2;
    private boolean isGoogleApiClientConnected;
    private boolean isEmailDialogueOpened;
    private String selectedEmailId;
    private FeedbackActivity activity;
    public static final int PERMISSION_CONTACTS = 2503;

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            CUtils.openAstroSageHomeActivity(FeedbackActivity.this);
                            return true;
                        case R.id.navigation_recharge:
                            /*Intent intent2 = new Intent(FeedbackActivity.this, WalletActivity.class);
                            intent2.putExtra(CGlobalVariables.CALLING_ACTIVITY, "FeedbackActivity");
                            startActivity(intent2);*/
                            isUserLogin(new ReadFragment(), CGlobalVariables.RECHARGE_SCRREN);

                            return true;
                        case R.id.navigation_share:
                            /*CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SUPPORT,
                                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");*/
                            //CUtils.sendFeedbackActivity(FeedbackActivity.this);
                            return true;
                        case R.id.navigation_notification:
                            /*Intent intent1 = new Intent(FeedbackActivity.this, NotificationCenterActivity.class);
                            startActivity(intent1);*/
                            //Intent intentShop = new Intent(FeedbackActivity.this, ActAstroShopCategories.class);
                            //startActivity(intentShop);
                            return true;
                        case R.id.navigation_profile:
                            /*Intent intent3 = new Intent(FeedbackActivity.this, ProfileActivity.class);
                            startActivity(intent3);*/
                            isUserLogin(new ProfileFragment(), CGlobalVariables.PROFILE_SCRREN);
                            return true;
                    }
                    return false;
                }
            };

    public void isUserLogin(Fragment fragment, String whichScreen) {
        boolean isLogin = false;
        isLogin = CUtils.getUserLoginStatus(FeedbackActivity.this);
        if (!isLogin) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(FeedbackActivity.this, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, whichScreen);
            startActivity(intent);
        } else {
            if (whichScreen.equals(CGlobalVariables.RECHARGE_SCRREN)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_RECHARGE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                openWalletScreen();
            } else {
                tvTitle.setText(getResources().getString(R.string.my_account));
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_PROFILE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                //openFragment(fragment, "PROFILEFRAGMENT");
                Intent intent = new Intent(FeedbackActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        }
    }

    public void openWalletScreen() {
        Intent intent = new Intent(FeedbackActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.varta_lay_feedback);
        activity = FeedbackActivity.this;
        initView();
    }

    private void initView() {
        scrollView = (LinearLayout) findViewById(R.id.scrollView);
        //navView = findViewById(R.id.nav_view);
        _etName = (EditText) findViewById(R.id.etName);
        _etEmailId = (EditText) findViewById(R.id.etEmailId);
        _etPhone = (EditText) findViewById(R.id.etPhone);
        _etMsg = (EditText) findViewById(R.id.etMsg);

        tv_phone_msg = (TextView) findViewById(R.id.tv_phone_msg);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_email_msg = (TextView) findViewById(R.id.tv_email_msg);
        tv_emailid = (TextView) findViewById(R.id.tv_emailid);

        tv_customer_care_phone1 = (TextView) findViewById(R.id.tv_customer_care_phone1);
        tv_customer_care_phone2 = (TextView) findViewById(R.id.tv_customer_care_phone2);
        tv_customer_care_phone3 = (TextView) findViewById(R.id.tv_customer_care_phone3);
        tv_customer_care_email = (TextView) findViewById(R.id.tv_customer_care_email);
        tvName = (TextView) findViewById(R.id.tvName);
        tvEmailId = (TextView) findViewById(R.id.tvEmailId);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tv_feedback = (TextView) findViewById(R.id.tv_feedback);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        butSend = (Button) findViewById(R.id.butSend);

        tvTitle.setText(getResources().getString(R.string.title_share));
        queue = VolleySingleton.getInstance(FeedbackActivity.this).getRequestQueue();
        tv_customer_care_email.setText(Html.fromHtml(getResources().getString(R.string.customer_care_email)));

        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //customBottomNavigationFont(navView);
        //navView.setSelectedItemId(R.id.navigation_share);
        //setBottomNavigationText();
        FontUtils.changeFont(FeedbackActivity.this, tv_customer_care_email, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FeedbackActivity.this, tv_customer_care_phone1, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FeedbackActivity.this, tv_customer_care_phone2, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FeedbackActivity.this, tv_customer_care_phone3, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FeedbackActivity.this, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FeedbackActivity.this, tv_feedback, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(FeedbackActivity.this, tv_emailid, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(FeedbackActivity.this, tv_phone, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(FeedbackActivity.this, butSend, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(FeedbackActivity.this, tvName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FeedbackActivity.this, tvEmailId, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FeedbackActivity.this, tvPhone, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FeedbackActivity.this, tv_message, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        FontUtils.changeFont(FeedbackActivity.this, _etName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FeedbackActivity.this, _etEmailId, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FeedbackActivity.this, _etPhone, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FeedbackActivity.this, _etMsg, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FeedbackActivity.this, tv_email_msg, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FeedbackActivity.this, tv_phone_msg, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        /*_etEmailId.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        _etMsg.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_FILTER);*/

        credentialManager = CredentialManager.create(this);
        setEmailIdFromUserAccount();

        butSend.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tv_customer_care_email.setOnClickListener(this);
        tv_customer_care_phone1.setOnClickListener(this);
        tv_customer_care_phone2.setOnClickListener(this);
        tv_customer_care_phone3.setOnClickListener(this);
    }


    private void setEmailIdFromUserAccount() {
        String email = UserEmailFetcher.getEmail(this);
        if (!TextUtils.isEmpty(email)) {
            _etEmailId.setText(email);
        } else {
            //_etEmailId.setFocusable(false);
            _etEmailId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (isGoogleApiClientConnected) {
                        requestCredentials();
                    } else {
                        setFocusOnEmail();
                    }*/

                    //requestCredentials();
                }
            });
        }
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
                FontUtils.changeFont(FeedbackActivity.this, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
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
        // get menu from navigationView
        Menu menu = navView.getMenu();
        // find MenuItem you want to change
        MenuItem navHome = menu.findItem(R.id.navigation_home);
        MenuItem navRead = menu.findItem(R.id.navigation_recharge);
        MenuItem navNotificaton = menu.findItem(R.id.navigation_share);
        MenuItem navMyaccount = menu.findItem(R.id.navigation_profile);

        MenuItem navCall = menu.findItem(R.id.bottom_nav_call);
        MenuItem navChat = menu.findItem(R.id.bottom_nav_chat);
        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if(isAIChatDisplayed){
            navChat.setTitle(getResources().getString(R.string.text_ask));
            navCall.setTitle(getResources().getString(R.string.ai_astrologer));
            navCall.setIcon(R.drawable.nav_ai_icons);
        } else {
            navChat.setTitle(getResources().getString(R.string.chat_now));
            navCall.setTitle(getResources().getString(R.string.call));
            navCall.setIcon(R.drawable.nav_call_icons);
        }

        // set new title to the MenuItem
        navHome.setTitle(getResources().getString(R.string.title_home));
        navRead.setTitle(getResources().getString(R.string.wallet));
        navNotificaton.setTitle(getResources().getString(R.string.support));
        if (CUtils.getUserLoginStatus(FeedbackActivity.this)) {
            navMyaccount.setTitle(getResources().getString(R.string.history));
            navMyaccount.setIcon(R.drawable.nav_more_icons);
        } else {
            navMyaccount.setTitle(getResources().getString(R.string.sign_up));
            navMyaccount.setIcon(R.drawable.nav_profile_icons);;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.butSend:
                if (validateData()) {
                    try {
                        CUtils.hideMyKeyboard(this);
                        sendFeedbackToServer();
                    }catch (Exception e){
                        //
                    }
                }
                break;

            case R.id.ivBack:
                finish();
                break;

            case R.id.tv_customer_care_email:
                composeNewEmail();
                break;

            case R.id.tv_customer_care_phone1:
                makeCallOn(CGlobalVariables.call_on__01143145870);
                break;
            case R.id.tv_customer_care_phone2:
                makeCallOn(CGlobalVariables.call_on__07948058177);
                break;
            case R.id.tv_customer_care_phone3:
                makeCallOn(CGlobalVariables.call_on__01135645085);
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

    private boolean validateData() {
        boolean flag = false;
        if (validateName(_etName, getString(R.string.please_enter_name_v))
                && validateName(_etEmailId, getString(R.string.email_one_v))
                && validateName(_etMsg, getString(R.string.feedback_message_one_v))
                && validateName(_etPhone, getString(R.string.mandatory_fields)))
            flag = true;

        return flag;
    }

    private boolean validateName(EditText name, String message) {
        boolean value = true;
        if (name == _etName) {
            if (name.getText().toString().trim().length() < 1) {
                name.setError(message);
                name.requestFocus();
                CUtils.showSnackbar(_etName, message, FeedbackActivity.this);
                value = false;
            } else if (name.getText().toString().trim().length() > 50) {
                name.setError(getResources().getString(R.string.name_limit_v));
                name.requestFocus();
                CUtils.showSnackbar(_etName, getResources().getString(R.string.name_limit_v), FeedbackActivity.this);
                value = false;
            }
        }

        if (name == _etEmailId) {
            if (name.getText().toString().trim().length() < 1) {
                name.setError(message);
                name.requestFocus();
                CUtils.showSnackbar(_etEmailId, message, FeedbackActivity.this);
                value = false;
            } else if (name.getText().toString().trim().length() > 70) {
                name.setError(getResources().getString(R.string.email_two_v));
                name.requestFocus();
                CUtils.showSnackbar(_etEmailId, getResources().getString(R.string.email_two_v), FeedbackActivity.this);
                value = false;
            } else if (!checkEmail(name.getText().toString().trim())) {
                name.setError(getResources().getString(R.string.email_three_v));
                name.requestFocus();
                CUtils.showSnackbar(_etEmailId, getResources().getString(R.string.email_three_v), FeedbackActivity.this);
                value = false;
            }
        }

        if (name == _etMsg) {
            if (name.getText().toString().trim().length() < 1) {
                name.setError(message);
                name.requestFocus();
                CUtils.showSnackbar(_etMsg, message, FeedbackActivity.this);
                value = false;
            } else if (name.getText().toString().trim().length() > 500) {
                name.setError(getResources().getString(R.string.feedback_message_two_v));
                name.requestFocus();
                CUtils.showSnackbar(_etMsg, getResources().getString(R.string.feedback_message_two_v), FeedbackActivity.this);
                value = false;
            }
        }

        if (name == _etPhone) {
            if (name.getText().toString().trim().length() > 0 && name.getText().toString().trim().length() < 10) {
                name.setError(getResources().getString(R.string.phone_validation_v));
                name.requestFocus();
                CUtils.showSnackbar(_etPhone, getResources().getString(R.string.phone_validation_v), FeedbackActivity.this);
                value = false;
            }
        }
        return value;
    }

    private boolean checkEmail(String email) {
        if (email == null) {
            return false;
        } else if (email.equals("")) {
            return true;
        } else {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        }
    }

    private void sendFeedbackToServer() {

        if (!CUtils.isConnectedWithInternet(FeedbackActivity.this)) {
            CUtils.showSnackbar(scrollView, getResources().getString(R.string.no_internet), FeedbackActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(FeedbackActivity.this);
            pd.show();
            pd.setCancelable(false);
            //Log.e("LoadMore url ", CGlobalVariables.FEED_BACK_URL);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.FEED_BACK_URL,
                    FeedbackActivity.this, false, getparams(FeedbackActivity.this), 1).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
        }
    }

    private String getMyApplicationName() {
        ApplicationInfo info = getApplicationInfo();
        PackageManager p = this.getPackageManager();
        String appName = p.getApplicationLabel(info).toString();
        getApplicationContext().getPackageName();
        return appName + " ( " + getApplicationContext().getPackageName() + " )";
    }

    private Map<String, String> getparams(Context ctx) {

        String language = "ENGLISH";

        String appVerName = CUtils.getApplicationVersionToShow(FeedbackActivity.this);
        String name = _etName.getText().toString();
        String emailId = _etEmailId.getText().toString();
        String phone = _etPhone.getText().toString();
        String message = _etMsg.getText().toString();
        String apppName = getMyApplicationName();

        Map<String, String> params = new HashMap<>();
        params.put("feedbackfrom", apppName);
        ;
        params.put("appvesrion", appVerName);//ADDED BY BIJENDRA ON 01-MAY-15nameValuePairs.add(new BasicNameValuePair("appvesrion", appVerName));//ADDED BY BIJENDRA ON 01-MAY-15
        params.put("key", CUtils.getApplicationSignatureHashCode(ctx));
        params.put("feedbackpersonname", name);
        params.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(emailId));
        params.put("phoneno", phone);
        params.put("message", message);
        params.put("modelname", android.os.Build.MODEL);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("brandname", android.os.Build.BRAND);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("osversion", android.os.Build.VERSION.RELEASE);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("sdkversion", "" + android.os.Build.VERSION.SDK_INT);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("languagecode", language);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("activityname", "FeedbackActivity");

        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(FeedbackActivity.this);
        if (userProfileData != null) {
            params.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.getUserID(FeedbackActivity.this));
            params.put("name", name);
            params.put("day", userProfileData.getDay());
            params.put("month", userProfileData.getMonth());
            params.put("year", userProfileData.getYear());
            params.put("hour", userProfileData.getHour());
            params.put("min", userProfileData.getMinute());
            params.put("sec", userProfileData.getSecond());
            params.put("place", userProfileData.getPlace());
            params.put("timezone", userProfileData.getTimezone());
            params.put("longdeg", userProfileData.getLongdeg());
            params.put("longmin", userProfileData.getLongmin());
            params.put("longew", userProfileData.getLongew());
            params.put("latdeg", userProfileData.getLatdeg() == null?"":userProfileData.getLatdeg());
            params.put("latmin", userProfileData.getLatmin());
            params.put("latns", userProfileData.getLatns());
            params.put("dst", "");
            params.put("ayanamsa", "");
        }
        return params;
    }

    @Override
    public void onError(VolleyError error) {
        try {
            hideProgressBar();
            if (error != null) {
                CUtils.showSnackbar(scrollView, error.toString(), FeedbackActivity.this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvTitle.setText(getResources().getString(R.string.support));
        //navView.setSelectedItemId(R.id.navigation_share);
        desableColorFilter(_etName);
        desableColorFilter(_etEmailId);
        desableColorFilter(_etPhone);
        desableColorFilter(_etMsg);
    }

    @Override
    public void onResponse(String response, int method) {
        //super.onResponse(response, method);
        hideProgressBar();
        //Log.e("LoadMore response ", response + " method=" + method);

        if (response != null && response.length() > 0) {

            if (!TextUtils.isEmpty(response)) {
                int resultCode = 0;
                try {
                    resultCode = parseResponse(response);
                    switch (resultCode) {
                        case 0:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.feedback_sent), FeedbackActivity.this);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    _etName.setText("");
                                    _etEmailId.setText("");
                                    _etPhone.setText("");
                                    _etMsg.setText("");
//                                    FeedbackActivity.this.finish();
                                }
                            }, 2000); // Millisecond 1000 = 1 sec
                            break;
                        case 1:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_enter_name), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                        case 2:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.name_limit), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                        case 3:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.email_one), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                        case 4:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.email_two), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                        case 5:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.email_three), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                        case 6:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.phone_validation), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                        case 7:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.phone_should_be_numeric), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                        case 8:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.feedback_message_one), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                        case 9:
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.feedback_message_two), FeedbackActivity.this);
                            CUtils.showSnackbar(scrollView, getResources().getString(R.string.please_try_again), FeedbackActivity.this);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            CUtils.showSnackbar(scrollView, getResources().getString(R.string.server_error), FeedbackActivity.this);
        }
    }

    public static int parseResponse(String strData) throws Exception {
        int returnStr = -1;
        if (strData.contains("<msgcode>")) {
            returnStr = Integer.valueOf(strData.substring(strData.indexOf("<msgcode>") + "<msgcode>".length(), strData.indexOf("</msgcode>")).trim());
        }
        return returnStr;
    }

    /***************************** Gmail choose dialogue start **********************/
    public void requestCredentials() {
        if (!isEmailDialogueOpened) {
            showGmailAccountPicker();
        }
    }

    public void showGmailAccountPicker() {
        // Configure Google ID option
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Show all accounts
                .setServerClientId(getString(R.string.default_web_client_id)) // Required
                .build();

        // Build the request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // Launch Credential Manager UI
        credentialManager.getCredentialAsync(
                this,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback() {
                    @Override
                    public void onResult(Object responseObj) {
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                        handleSignIn((GetCredentialResponse) responseObj);
                    }

                    @Override
                    public void onError(@NonNull Object o) {
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                    }
                }
        );
    }

    private void handleSignIn(GetCredentialResponse credentialResponse) {
        try {
            Credential credential = credentialResponse.getCredential();
            if (credential instanceof CustomCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                // Create Google ID Token
                Bundle credentialData = credential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
                selectedEmailId = googleIdTokenCredential.getId();
                runOnUiThread(() -> {
                    try {
                        com.ojassoft.astrosage.utils.CUtils.saveAstroshopUserEmail(activity, selectedEmailId);
                        _etEmailId.setText(selectedEmailId);
                    } catch (Exception e) {
                        //
                    }
                });
            }
        }catch (Exception e){
            //
        }
    }

    private void setFocusOnEmail() {
        runOnUiThread(() -> {
            _etEmailId.setFocusableInTouchMode(true);
            _etEmailId.setFocusable(true);
            _etEmailId.requestFocus();
        });
    }


    private void desableColorFilter(EditText editText) {
        editText.getBackground().setColorFilter(null);
    }

    @Override
    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        //setEmailIdFromUserAccount(true);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* switch (requestCode) {
            case PERMISSION_CONTACTS:
                setEmailIdFromUserAccount(false);
                break;
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //setEmailIdFromUserAccount(false);
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(this, CGlobalVariables.PERMISSION_KEY_CONTACTS, true);
                }
            }
        }
    }
}