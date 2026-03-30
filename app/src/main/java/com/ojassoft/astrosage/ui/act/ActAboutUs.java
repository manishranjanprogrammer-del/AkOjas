package com.ojassoft.astrosage.ui.act;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.SendFeedback;
import com.ojassoft.astrosage.networkcall.RetrofitResponses;
import com.ojassoft.astrosage.notification.ActShowOjasSoftArticles;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.model.RechargeHistoryBean;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.ui.fragments.CallHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.ChatHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.RechargeHistoryFragment;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.ui.fragments.AIHoroscopeFragment.aiHoroscopeLogData;
import static com.ojassoft.astrosage.utils.CGlobalVariables.REPORT_ERROR_PREF;
import static com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity.logMsgForFacebookLogin;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_RECHARG;
import static com.ojassoft.astrosage.varta.utils.CUtils.updateContactNumber;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

//import com.google.analytics.tracking.android.EasyTracker;

public class ActAboutUs extends BaseInputActivity implements SendDataBackToComponent,VolleyResponse, RetrofitResponses {
    //PlusOneButton plusOneButton;
    final int shareApp_text = 1,
            likeUsOnFacebook_text = 2,
            followUsOnGooglePlus_text = 3,
            followUsOnTwitter_text = 4,
            shareApp_img = 5,
            likeUsOnFacebook_img = 6,
            followUsOnTwitter_img = 8,
            firstNumber = 9,
            secondNumber = 10,
            eMail = 11,
            webPage = 12,
            insta_img = 13,
            linkedin_img = 14;
    int language_code;
    Typeface typeFace;
    TextView titleBar, app_name;
    TextView firstContactNumber, composeEMail, openWebPage;
    TextView shareAppWithOthers, likeUsOnFacebook, followUsOnTwitter, instaTV, linkedInTV;
    ImageView shareAppIcon, facebookLikeIcon, twitterFollowIcon, instaIcon, linkedInIcon;
    //ImageView imgBackButton;
    //RelativeLayout sharelLayout, fbLayout, gplusLayout, twitterlayout;
    //private static final int PLUS_ONE_REQUEST_CODE = 0;
    //private static final String URL = CGlobalVariables.gPlusUrl;
    Toolbar toolbar;
    TabLayout tabs;
    ActionBar actionBar;
    /*sharelLayout_t=13,
    fbLayout_t=14,
    gplusLayout_t=15,
    twitterlayout_t=16;*/
    private CustomProgressDialog pd;
    int SEND_FEEDBACK = 0;
    LinearLayout linLayoutPhone;

    public ActAboutUs() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //language_code = getIntent().getIntExtra("language_code", 0);
        language_code = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        //Log.e("SAN AAU ", "LC=>" + language_code);
        //typeFace = CUtils.getRobotoFont(getApplicationContext(), language_code, CGlobalVariables.regular);
        typeFace = CUtils.getRobotoFont(
                ActAboutUs.this.getApplicationContext(), language_code, CGlobalVariables.regular);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.lay_aboutus);

        setContentView(R.layout.about_us_new_screen);
        writeApplicationVersion();

        toolbar = (Toolbar) findViewById(R.id.tool_barAppModule);
// Get the navigation icon drawable
        Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);

// Check if the drawable is not null
        if (navIcon != null) {
            // Tint the drawable with the desired color
            navIcon.setTint(ContextCompat.getColor(this, R.color.black));

            // Set the tinted drawable as the navigation icon
            toolbar.setNavigationIcon(navIcon);
        }
        linLayoutPhone = (LinearLayout) findViewById(R.id.linLayoutPhone);
        if (CUtils.getBooleanData(this, CGlobalVariables.IS_PURCHASE,false)){
            linLayoutPhone.setVisibility(View.VISIBLE);
        }else {
            com.ojassoft.astrosage.varta.utils.CUtils.getConsultationHistoryViaRetrofit(this,CONSULT_HISTORY_RECHARG, "0", "RechargeHistoryFragment");
        }

        shareAppWithOthers = (TextView) findViewById(R.id.shareApp).findViewById(R.id.textAboutUs);
        shareAppWithOthers.setOnClickListener(new ButtonClick(shareApp_text));
        likeUsOnFacebook = (TextView) findViewById(R.id.likeUsOnFacebook).findViewById(R.id.textAboutUs);
        likeUsOnFacebook.setOnClickListener(new ButtonClick(likeUsOnFacebook_text));
        followUsOnTwitter = (TextView) findViewById(R.id.followUsOnTwitter).findViewById(R.id.textAboutUs);
        followUsOnTwitter.setOnClickListener(new ButtonClick(followUsOnTwitter_text));
        instaTV = (TextView) findViewById(R.id.followUsOnInstagram).findViewById(R.id.textAboutUs);
        instaTV.setOnClickListener(new ButtonClick(insta_img));
        linkedInTV = (TextView) findViewById(R.id.followUsOnLinkedIn).findViewById(R.id.textAboutUs);
        linkedInTV.setOnClickListener(new ButtonClick(linkedin_img));


        shareAppIcon = (ImageView) findViewById(R.id.shareApp).findViewById(R.id.imageAboutUs);
        findViewById(R.id.shareApp).setBackgroundResource(R.drawable.drawable_share_app);
        shareAppIcon.setOnClickListener(new ButtonClick(shareApp_img));
        facebookLikeIcon = (ImageView) findViewById(R.id.likeUsOnFacebook).findViewById(R.id.imageAboutUs);
        facebookLikeIcon.setOnClickListener(new ButtonClick(likeUsOnFacebook_img));
        twitterFollowIcon = (ImageView) findViewById(R.id.followUsOnTwitter).findViewById(R.id.imageAboutUs);
        twitterFollowIcon.setOnClickListener(new ButtonClick(followUsOnTwitter_img));
        instaIcon = (ImageView) findViewById(R.id.followUsOnInstagram).findViewById(R.id.imageAboutUs);
        instaIcon.setOnClickListener(new ButtonClick(insta_img));
        linkedInIcon = (ImageView) findViewById(R.id.followUsOnLinkedIn).findViewById(R.id.imageAboutUs);
        findViewById(R.id.followUsOnLinkedIn).setBackgroundResource(R.drawable.drawable_linkedin);
        linkedInIcon.setOnClickListener(new ButtonClick(linkedin_img));

        /*sharelLayout=(RelativeLayout)findViewById(R.id.shareApp).findViewById(R.id.aboutUsLayout);
        sharelLayout.setOnClickListener(new ButtonClick(sharelLayout_t));
        fbLayout=(LinearLayout)findViewById(R.id.likeUsOnFacebook).findViewById(R.id.aboutUsLayout);
        fbLayout.setOnClickListener(new ButtonClick(fbLayout_t));
        gplusLayout=(LinearLayout)findViewById(R.id.followUsOnGooglePlus).findViewById(R.id.aboutUsLayout);
        gplusLayout.setOnClickListener(new ButtonClick(gplusLayout_t));
        twitterlayout=(LinearLayout) findViewById(R.id.followUsOnTwitter).findViewById(R.id.aboutUsLayout);
        twitterlayout.setOnClickListener(new ButtonClick(twitterlayout_t));*/

        //plusOneButton=(PlusOneButton)findViewById(R.id.plusOneFollowButton);

        //shareAppWithOthers.setTypeface(typeFace, Typeface.BOLD);
        shareAppWithOthers.setText(getString(R.string.share_this_app));
        //likeUsOnFacebook.setTypeface(typeFace, Typeface.BOLD);
        likeUsOnFacebook.setText(getString(R.string.like_us_on_facebook));
        //followUsOnTwitter.setTypeface(typeFace, Typeface.BOLD);
        followUsOnTwitter.setText(getString(R.string.follow_on_twitter));
        //instaTV.setTypeface(typeFace, Typeface.BOLD);
        instaTV.setText(getString(R.string.follow_on_instagram));
        //linkedInTV.setTypeface(typeFace, Typeface.BOLD);
        linkedInTV.setText(getString(R.string.follow_on_linkedin));


        shareAppIcon.setImageResource(R.drawable.ic_whatsapp);
        facebookLikeIcon.setImageResource(R.drawable.ic_facebook);
        twitterFollowIcon.setImageResource(R.drawable.ic_twitter);
        instaIcon.setImageResource(R.drawable.ic_instagram);
        linkedInIcon.setImageResource(R.drawable.linkedin);

        shareAppWithOthers.setTextColor(getResources().getColor(R.color.share_app_text_color));
        likeUsOnFacebook.setTextColor(getResources().getColor(R.color.facebook_text_color));
        followUsOnTwitter.setTextColor(getResources().getColor(R.color.twitter_text_color));
        instaTV.setTextColor(getResources().getColor(R.color.insta_text_color));
        linkedInTV.setTextColor(getResources().getColor(R.color.linkedin_text_color));

        firstContactNumber = (TextView) findViewById(R.id.firstNum);
        firstContactNumber.setOnClickListener(new ButtonClick(firstNumber));
//        secondContactNumber = (TextView) findViewById(R.id.secondNum);
//        secondContactNumber.setOnClickListener(new ButtonClick(secondNumber));
        composeEMail = (TextView) findViewById(R.id.composeEmail);
        composeEMail.setOnClickListener(new ButtonClick(eMail));
        openWebPage = (TextView) findViewById(R.id.openWebPage);
        openWebPage.setOnClickListener(new ButtonClick(webPage));

        boolean isNumVisible = com.ojassoft.astrosage.varta.utils.CUtils.checkForCsNumberVisibility(this);
        if(isNumVisible){
            linLayoutPhone.setVisibility(View.VISIBLE);
            updateContactNumber(this, firstContactNumber);
        }else{
            linLayoutPhone.setVisibility(View.GONE);
        }

        titleBar = (TextView) findViewById(R.id.toolBar_InputKundli).findViewById(R.id.tvTitle);
        titleBar.setTypeface(typeFace);
        titleBar.setText(R.string.About_us);

        app_name = (TextView) findViewById(R.id.app_name);
      //  app_name.setTypeface(typeFace, Typeface.ITALIC);
        String appName = getString(R.string.app_name);
        app_name.setText(appName + " ");

        tabs = (TabLayout) findViewById(R.id.toolBar_InputKundli).findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*imgBackButton = (ImageView) findViewById(R.id.imgBackButton);
        imgBackButton.setVisibility(View.GONE);
        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });*/

    }
    private final int DATA_RECHARGE = 2;

    @Override
    public void onResponse(String response, int method) {
        //Log.e("SAN ", " RHF response " + response );
        //errorLogsConsultation = errorLogsConsultation + " RHF response " + response +"\n";
      //  hideProgressBar();
        if (method == DATA_RECHARGE ) {
            //Log.e("SAN ", " CHF response " + response );
            parseConsulList(response);
        }

    }

    @Override
    public void onError(VolleyError error) {
       // hideProgressBar();
        //SAN CHA response ", " error " + error.toString());

    }

    private void parseConsulList(String response){
        //hideProgressBar();
        try {
            if (!TextUtils.isEmpty(response)) {

                //ArrayList<RechargeHistoryBean> rechargeHistoryList = new ArrayList();
                //RechargeHistoryBean rechargeHistoryBean;
                JSONArray recharges = null;

                JSONObject jsonObject = new JSONObject(response);

                String walletBalance = jsonObject.getString("walletbalance");

                if(jsonObject.has("recharges")) {
                    recharges = jsonObject.getJSONArray("recharges");
                    if(recharges != null && recharges.length()>0) {
                        CUtils.saveBooleanData(this, CGlobalVariables.IS_PURCHASE,true);
                        linLayoutPhone.setVisibility(View.VISIBLE);

                      /*  for (int i = 0; i < recharges.length(); i++) {
                            rechargeHistoryBean = new RechargeHistoryBean();
                            String rechargeType = recharges.getJSONObject(i).getString("rechargeType");
                            String rechargeAmount = recharges.getJSONObject(i).getString("rechargeAmount");
                            String rechargeDateTime = recharges.getJSONObject(i).getString("rechargeDateTime");
                            String displayMessage = recharges.getJSONObject(i).getString("displayMsg");
                            String orderId = recharges.getJSONObject(i).getString("orderId");
                            String rechargeId = recharges.getJSONObject(i).getString("rechargeId");

                            //Log.e("SAN ", "CHA response parse consultationId " + rechargeId );

                            rechargeHistoryBean.setRechargeType(rechargeType);
                            rechargeHistoryBean.setRechargeAmount(rechargeAmount);
                            rechargeHistoryBean.setRechargeDateTime(rechargeDateTime);
                            rechargeHistoryBean.setDisplayMsg(displayMessage);
                            rechargeHistoryBean.setOrderId(orderId);
                            rechargeHistoryBean.setRechargeId(rechargeId);
                            rechargeHistoryList.add(rechargeHistoryBean);
                        }*/
                    }


                }

            }
        } catch (Exception e) {
            //Log.e("SAN ", "RHF response parse exp " + e.toString());
            //errorLogsConsultation = errorLogsConsultation + "RHF response parse exp " + e.toString() +"\n";

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    /*private void setClickListener(final View parentView, View view, final int id) {
        if (view instanceof TextView) {
            ((TextView) view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doActionOnRadioBtnSelect(parentView, id);
                }
            });
        } else if (view instanceof ImageView) {
            ((ImageView) view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doActionOnRadioBtnSelect(parentView, id);
                }
            });
        }
    }*/

    private void shareApplicationWithOthers() {
        CUtils.shareToFriendMail(ActAboutUs.this);
    }

    private void likeUsOnFacebook() {
        Uri uri = Uri.parse(CGlobalVariables.Astrosage_Url_For_Facebook);
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                String str = "fb://facewebmodal/f?href=" + CGlobalVariables.Astrosage_Url_For_Facebook;
                uri = Uri.parse(str);
            }
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {// or else open in browser instead//https://www.facebook.com/contasty
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CGlobalVariables.Astrosage_Url_For_Facebook)));
            CUtils.openWebBrowserOnlyChrome(this, Uri.parse(CGlobalVariables.Astrosage_Url_For_Facebook));
        }
    }

    private void followUsOnGooglePlus() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CGlobalVariables.Astrosage_Url_For_GooglePlus)).setPackage("com.google.android.apps.plus"));
        } catch (Exception e) {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CGlobalVariables.Astrosage_Url_For_GooglePlus)));
        }
    }

    private void followUsOnTwitter() {
        Intent intent = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            //https://twitter.com/Astrosage.com
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(CGlobalVariables.Astrosage_Url_For_Twitter)).setPackage("com.twitter.android");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(CGlobalVariables.Astrosage_Url_For_Twitter));
        }
        this.startActivity(intent);

    }

    private void followUsOnInstagram() {
        Uri uri = Uri.parse(CGlobalVariables.Astrosage_Url_For_Instagram);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CGlobalVariables.Astrosage_Url_For_Instagram)));
        }

    }

    private void followUsOnLinkedIn() {
        Uri uri = Uri.parse(CGlobalVariables.Astrosage_Url_For_LinkedIn);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.linkedin.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CGlobalVariables.Astrosage_Url_For_LinkedIn)));
        }

    }

    private void makeCallOn(int id) {
        String number = "";
        if (id == firstNumber)
            number = CGlobalVariables.call_on_first_number_9560267006;
        else // if(id==secondNumber)
            number = CGlobalVariables.call_on_second_number_120_4138503;

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void composeNewEmail() {
        String email = "query@astrosage.com",
                subject = "Feedback And Queries";
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            //intent.putExtra(Intent.EXTRA_TEXT,aiHoroscopeLogData);
            //intent.putExtra(Intent.EXTRA_TEXT, com.ojassoft.astrosage.ui.act.AstrosageKundliApplication.paymentScreenLogs);
            startActivity(intent);

            pd.dismiss();
        } catch (Exception e) {
            //Log.e("SAN ", "composeNewEmail exception" + e.toString() );
        }

    }

    private void openAstrosageWeb() {
        String url = CGlobalVariables.ASTROSAGE_BASE_URL;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void gotoSelectedScreen(View textView) {
        Intent intent = new Intent(this, ActShowOjasSoftArticles.class);
        String url ="";
        switch (textView.getId()) {
            case R.id.textViewDisclaimer:
               url = CGlobalVariables.ASTROSAGE_DISCLAIMER_URL;
                intent.putExtra("TITLE_TO_SHOW", "Disclaimer");
                break;
            case R.id.textViewPrivacyPolicy:
                  intent.putExtra("TITLE_TO_SHOW", "Privacy Policy");
                url = CGlobalVariables.ASTROSAGE_PRIVACY_POLICY_URL;
                break;
            case R.id.textViewTermsAndConditions:
                url = CGlobalVariables.ASTROSAGE_TERMS_CONDITIONS_URL;
                intent.putExtra("TITLE_TO_SHOW", "Terms and Conditions");
                break;
        }


        url = CUtils.addParamsForDarkInURL(this,url);
       // Log.e("urlcheck", "gotoSelectedScreen:url =  "+url );
        intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
        intent.putExtra("URL", url);
        startActivity(intent);
    }



    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("About Us","AppSignature = " + getAppSignature());
        //typeFace = CUtils.getRobotoFont(getApplicationContext(), CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.regular);
    }

    /**
     * This function write the Version code of the application
     */
    private void writeApplicationVersion() {
        TextView _tvAppVersion = (TextView) findViewById(R.id.tvAppVersion1);
       // _tvAppVersion.setTypeface(typeFace, Typeface.ITALIC);
        try {
            String appVerName = LibCUtils.getApplicationVersionToShow(getApplicationContext());
            //if (language_code == CGlobalVariables.HINDI)
            //appVerName = appVerName.replace(".", "-");
            int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            String verText = getResources().getString(R.string.lib_app_version_text) + " " + appVerName + " (" + versionCode + ")";
            _tvAppVersion.setText(verText);


        } catch (Exception e) {
            Log.d("VERSION-Error", e.getMessage());
        }

    }

    public void goToSend(View view) {
        if (LibCUtils.isConnectedWithInternet(ActAboutUs.this)) {
            String etMsg = CUtils.getStringData(ActAboutUs.this, REPORT_ERROR_PREF, "");
            if (TextUtils.isEmpty(etMsg)) {
                showCustomisedToastMessage(getResources().getString(R.string.no_report_to_send));
            } else {
                pd = new CustomProgressDialog(ActAboutUs.this, typeFace);
                pd.show();
                //composeNewEmail();
                new SendFeedback(ActAboutUs.this).sendFeedbackToServer(getparams(this), SEND_FEEDBACK);

//                 new SendFeedBackAsync().execute();
                CUtils.hideMyKeyboard(ActAboutUs.this);
            }
        } else {
            showCustomisedToastMessage(getResources().getString(R.string.internet_is_not_working));
        }

//        composeNewEmail();
    }
    /*private void finishActivity() {
        this.finish();
    }*/

    private void showCustomisedToastMessage(String msg) {
        MyCustomToast mct = new MyCustomToast(ActAboutUs.this, getLayoutInflater(), ActAboutUs.this, typeFace);
        mct.show(msg);
    }

    /**
     * This function return the application name from which this activity is called
     *
     * @return String
     * @author Bijendra(02 - nov - 13)
     */
    private String getMyApplicationName() {
        ApplicationInfo info = getApplicationInfo();
        PackageManager p = getPackageManager();
        String appName = p.getApplicationLabel(info).toString();
        getApplicationContext().getPackageName();
        return appName + " ( " + getApplicationContext().getPackageName() + " )";
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.body() != null) {
            try {
                String myResponse = response.body().string();
                //Log.d("TestHistoryResponse","TestHistory==>>"+myResponse);
                parseConsulList(myResponse);
            }catch (Exception e){

            }

        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
    }


    private class ButtonClick implements View.OnClickListener {

        int id;

        ButtonClick(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {

            switch (this.id) {
                case shareApp_text:
                case shareApp_img:
                    //case sharelLayout_t:
                    shareApplicationWithOthers();
                    break;

                case likeUsOnFacebook_text:
                case likeUsOnFacebook_img:
                    //case fbLayout_t:
                    likeUsOnFacebook();
                    break;

                case followUsOnTwitter_text:
                case followUsOnTwitter_img:
                    //case twitterlayout_t:
                    followUsOnTwitter();
                    break;

                case firstNumber:
                case secondNumber:
                    makeCallOn(this.id);
                    break;

                case eMail:
                    composeNewEmail();
                    break;
                case webPage:
                    openAstrosageWeb();
                    break;
                case insta_img:
                    followUsOnInstagram();
                    break;
                case linkedin_img:
                    followUsOnLinkedIn();
                    break;
            }
        }
    }

    private Map<String, String> getparams(Context ctx) {
        SharedPreferences sharedPreferencesForLang = getSharedPreferences(
                CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                Context.MODE_PRIVATE);
        int selectedLanguage = sharedPreferencesForLang.getInt(
                CGlobalVariables.APP_PREFS_AppLanguage, 0);
        String language = "";
        if (selectedLanguage == 0) {
            language = "ENGLISH";
        } else if (selectedLanguage == 1) {
            language = "HINDI";
        } else if (selectedLanguage == 2) {
            language = "TAMIL";
        } else {
            language = "";
        }
        String appVerName = LibCUtils.getApplicationVersionToShow(ActAboutUs.this);
        String name = "Error";
        String emailId = "error@ojassoft.com";
        String phone = "";
        String message = CUtils.getStringData(ActAboutUs.this, REPORT_ERROR_PREF, "");
        String apppName = getMyApplicationName();

        Map<String, String> params = new HashMap<>();
        params.put("feedbackfrom", apppName);
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
        params.put("activityname", "ui.act.ActAboutUs");

        if (LibCGlobalVariables.userDetailAvailble) {
            params.put(CGlobalVariables.KEY_AS_USER_ID, LibCGlobalVariables.useridsession);
            params.put("name", LibCGlobalVariables.name);
            params.put("day", LibCGlobalVariables.day);
            params.put("month", LibCGlobalVariables.month);
            params.put("year", LibCGlobalVariables.year);
            params.put("hour", LibCGlobalVariables.hour);
            params.put("min", LibCGlobalVariables.min);
            params.put("sec", LibCGlobalVariables.sec);
            params.put("place", LibCGlobalVariables.place);
            params.put("timezone", LibCGlobalVariables.timezone);
            params.put("longdeg", LibCGlobalVariables.longdeg);
            params.put("longmin", LibCGlobalVariables.longmin);
            params.put("longew", LibCGlobalVariables.longew);
            params.put("latdeg", LibCGlobalVariables.latdeg);
            params.put("latmin", LibCGlobalVariables.latmin);
            params.put("latns", LibCGlobalVariables.latns);
            params.put("dst", LibCGlobalVariables.dst);
            params.put("ayanamsa", LibCGlobalVariables.ayanamsa);
        }
        return params;
    }

    /* private class SendFeedBackAsync extends AsyncTask<String, Long, Void> {

         String etName = "", etEmailId = "", etPhone = "", etMsg = "";
         String _exceptionMessage = "", appVerName = "";
         boolean isSuccess = true;
         int resultCode = -1;

         @Override
         protected Void doInBackground(String... arg0) {
             try {
                 SharedPreferences sharedPreferencesForLang = getSharedPreferences(
                         CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                         Context.MODE_PRIVATE);
                 int selectedLanguage = sharedPreferencesForLang.getInt(
                         CGlobalVariables.APP_PREFS_AppLanguage, 0);
                 String language = "";
                 if (selectedLanguage == 0) {
                     language = "ENGLISH";
                 } else if (selectedLanguage == 1) {
                     language = "HINDI";
                 } else if (selectedLanguage == 2) {
                     language = "TAMIL";
                 } else {
                     language = "";
                 }
                 resultCode = LibCUtils.sendFeedbackToAstroSageWithVesrion(ActAboutUs.this, getMyApplicationName(), etName,
                         etEmailId, etPhone, etMsg, appVerName, language, "ui.act.ActAboutUs");
             } catch (Exception e) {
                 isSuccess = false;
                 _exceptionMessage = e.getMessage();
             }
             return null;
         }

         protected void onPreExecute() {
             appVerName = LibCUtils.getApplicationVersionToShow(ActAboutUs.this);
             etName = "Error";
             etEmailId = "error@ojassoft.com";
             etPhone = "";
             etMsg = CUtils.getStringData(ActAboutUs.this, REPORT_ERROR_PREF, "");

             pd = new CustomProgressDialog(ActAboutUs.this, typeFace);
             pd.show();
         }

         protected void onPostExecute(final Void unused) {
             try {
                 if (pd != null && pd.isShowing()) {
                     pd.dismiss();
                     pd = null;
                 }

                 if (isSuccess) {

                     switch (resultCode) {
                         case 0:
                             showCustomisedToastMessage(getResources().getString(R.string.feedback_sent));
                             break;
                         case 1:
                             showCustomisedToastMessage(getResources().getString(R.string.please_enter_name));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                         case 2:
                             showCustomisedToastMessage(getResources().getString(R.string.name_limit));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                         case 3:
                             showCustomisedToastMessage(getResources().getString(R.string.email_one));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                         case 4:
                             showCustomisedToastMessage(getResources().getString(R.string.email_two));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                         case 5:
                             showCustomisedToastMessage(getResources().getString(R.string.email_three));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                         case 6:
                             showCustomisedToastMessage(getResources().getString(R.string.phone_validation));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                         case 7:
                             showCustomisedToastMessage(getResources().getString(R.string.phone_should_be_numeric));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                         case 8:
                             showCustomisedToastMessage(getResources().getString(R.string.feedback_message_one));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                         case 9:
                             showCustomisedToastMessage(getResources().getString(R.string.feedback_message_two));
                             showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                             break;
                     }
                 }

                 if (_exceptionMessage.trim().length() > 0) {
                     showCustomisedToastMessage(_exceptionMessage);
                 }
             } catch (Exception e) {
                 //showCustomisedToastMessage(e.getMessage());
             }

         }
     }*/
    @Override
    public void doActionAfterGetResult(String response, int method) {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }

            if (!TextUtils.isEmpty(response)) {
                int resultCode = LibCUtils.parseResponse(response);
                switch (resultCode) {
                    case 0:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_sent));
                        break;
                    case 1:
                        showCustomisedToastMessage(getResources().getString(R.string.please_enter_name));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 2:
                        showCustomisedToastMessage(getResources().getString(R.string.name_limit));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 3:
                        showCustomisedToastMessage(getResources().getString(R.string.email_one));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 4:
                        showCustomisedToastMessage(getResources().getString(R.string.email_two));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 5:
                        showCustomisedToastMessage(getResources().getString(R.string.email_three));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 6:
                        showCustomisedToastMessage(getResources().getString(R.string.phone_validation));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 7:
                        showCustomisedToastMessage(getResources().getString(R.string.phone_should_be_numeric));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 8:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_message_one));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 9:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_message_two));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                }
            }


        } catch (Exception e) {
            if (e != null && e.getMessage() != null) {
                showCustomisedToastMessage(e.getMessage());
            }
        }
    }

    @Override
    public void doActionOnError(String error) {
        if (!TextUtils.isEmpty(error)) {
            showCustomisedToastMessage(error);
        }
    }

    public  String getAppSignature() {
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hash = new String(Base64.encode(md.digest(), Base64.NO_WRAP)).substring(0, 11);
                return hash;
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}
