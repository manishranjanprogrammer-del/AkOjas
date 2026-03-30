package com.ojassoft.astrosage.varta.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {

    TextView version_tv, version_value, textViewDisclaimer,
            textViewPrivacyPolicy, textViewTermsAndConditions, tvTitle, copyright_tv;
    ImageView ivBack;
    TextView shareAppWithOthers, likeUsOnFacebook, followUsOnTwitter, instaTV;
    ImageView shareAppIcon, facebookLikeIcon, twitterFollowIcon, instaIcon;
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
            insta_img = 13;
    TextView firstContactNumber,  composeEMail, openWebPage, helpIV;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen_layout);
        initView();
    }

    private void initView() {

        version_tv = findViewById(R.id.version_tv);
        version_value = findViewById(R.id.version_value);
        copyright_tv = findViewById(R.id.copyright_tv);
        textViewDisclaimer = findViewById(R.id.textViewDisclaimer);
        textViewPrivacyPolicy = findViewById(R.id.textViewPrivacyPolicy);
        textViewTermsAndConditions = findViewById(R.id.textViewTermsAndConditions);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        helpIV = findViewById(R.id.helpIV);

        shareAppWithOthers = (TextView) findViewById(R.id.shareApp).findViewById(R.id.textAboutUs);
        shareAppWithOthers.setOnClickListener(new ButtonClick(shareApp_text));
        likeUsOnFacebook = (TextView) findViewById(R.id.likeUsOnFacebook).findViewById(R.id.textAboutUs);
        likeUsOnFacebook.setOnClickListener(new ButtonClick(likeUsOnFacebook_text));
        followUsOnTwitter = (TextView) findViewById(R.id.followUsOnTwitter).findViewById(R.id.textAboutUs);
        followUsOnTwitter.setOnClickListener(new ButtonClick(followUsOnTwitter_text));
        instaTV = (TextView) findViewById(R.id.followUsOnInstagram).findViewById(R.id.textAboutUs);
        instaTV.setOnClickListener(new ButtonClick(insta_img));

        shareAppIcon = (ImageView) findViewById(R.id.shareApp).findViewById(R.id.imageAboutUs);
        shareAppIcon.setOnClickListener(new ButtonClick(shareApp_img));
        facebookLikeIcon = (ImageView) findViewById(R.id.likeUsOnFacebook).findViewById(R.id.imageAboutUs);
        facebookLikeIcon.setOnClickListener(new ButtonClick(likeUsOnFacebook_img));
        twitterFollowIcon = (ImageView) findViewById(R.id.followUsOnTwitter).findViewById(R.id.imageAboutUs);
        twitterFollowIcon.setOnClickListener(new ButtonClick(followUsOnTwitter_img));
        instaIcon = (ImageView) findViewById(R.id.followUsOnInstagram).findViewById(R.id.imageAboutUs);
        instaIcon.setOnClickListener(new ButtonClick(insta_img));

        shareAppWithOthers.setText(getString(R.string.share_this_app));
        likeUsOnFacebook.setText(getString(R.string.like_us_on_facebook));
        followUsOnTwitter.setText(getString(R.string.follow_on_twitter));
        instaTV.setText(getString(R.string.follow_on_instagram));

        shareAppIcon.setImageResource(R.drawable.sharing_new_icon);
        facebookLikeIcon.setImageResource(R.drawable.facebook_new_icon);
        twitterFollowIcon.setImageResource(R.drawable.twitter);
        instaIcon.setImageResource(R.drawable.insta_icon);

        shareAppWithOthers.setTextColor(getResources().getColor(R.color.share_app_text_color));
        likeUsOnFacebook.setTextColor(getResources().getColor(R.color.facebook_text_color));
        followUsOnTwitter.setTextColor(getResources().getColor(R.color.twitter_text_color));
        instaTV.setTextColor(getResources().getColor(R.color.insta_text_color));

        firstContactNumber = (TextView) findViewById(R.id.firstNum);
        firstContactNumber.setOnClickListener(new ButtonClick(firstNumber));
//        secondContactNumber = (TextView) findViewById(R.id.secondNum);
//        secondContactNumber.setOnClickListener(new ButtonClick(secondNumber));
        composeEMail = (TextView) findViewById(R.id.composeEmail);
        composeEMail.setOnClickListener(new ButtonClick(eMail));
        openWebPage = (TextView) findViewById(R.id.openWebPage);
        openWebPage.setOnClickListener(new ButtonClick(webPage));

        tvTitle.setText(getResources().getString(R.string.about_us));
        textViewPrivacyPolicy.setOnClickListener(this);
        textViewDisclaimer.setOnClickListener(this);
        textViewTermsAndConditions.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        FontUtils.changeFont(AboutUsActivity.this, shareAppWithOthers, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(AboutUsActivity.this, likeUsOnFacebook, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(AboutUsActivity.this, followUsOnTwitter, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(AboutUsActivity.this, instaTV, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        FontUtils.changeFont(AboutUsActivity.this, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(AboutUsActivity.this, version_tv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(AboutUsActivity.this, version_value, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(AboutUsActivity.this, textViewDisclaimer, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(AboutUsActivity.this, textViewPrivacyPolicy, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(AboutUsActivity.this, textViewTermsAndConditions, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(AboutUsActivity.this, copyright_tv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        try {
            String appVerName = CUtils.getApplicationVersionToShow(getApplicationContext());
            int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            version_value.setText(appVerName +" ("+versionCode+")");
        } catch (Exception e) {
            e.printStackTrace();
        }

        helpIV.setOnClickListener(v -> {
            String logData = AstrosageKundliApplication.runningCallsIdLog;
            if(!TextUtils.isEmpty(logData)){
                composeLiveStreamingEmail(logData);
            }else {
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void composeLiveStreamingEmail(String logStr) {

        String email = getResources().getString(R.string.customer_care_email_id),
                subject = "Varta User Logs";

        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT,logStr);
            startActivity(intent);
        }catch (Exception e){
            //Log.e("SAN FCM  ", "composeNewEmail exception" + e.toString() );
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WebViewActivity.class);
        switch (v.getId()) {
            case R.id.textViewDisclaimer:
                intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", CGlobalVariables.VARTA_DISCLAIMER_URL);
                intent.putExtra("TITLE_TO_SHOW", "Disclaimer");
                startActivity(intent);
                break;
            case R.id.textViewPrivacyPolicy:
                intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", CGlobalVariables.VARTA_PRIVACY_POLICY_URL);
                intent.putExtra("TITLE_TO_SHOW", "Privacy Policy");
                startActivity(intent);
                break;
            case R.id.textViewTermsAndConditions:
                intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", CGlobalVariables.VARTA_TERMS_CONDITIONS_URL);
                intent.putExtra("TITLE_TO_SHOW", "Terms and Conditions");
                startActivity(intent);
                break;

            case R.id.ivBack:
                finish();
                break;
        }

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
                    shareApplicationWithOthers();
                    break;

                case likeUsOnFacebook_text:
                case likeUsOnFacebook_img:
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
            }
        }
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


    private void shareApplicationWithOthers() {
        CUtils.shareToFriendMail(AboutUsActivity.this);
    }

    private void likeUsOnFacebook() {
        Uri uri = Uri.parse(CGlobalVariables.Astrosage_Url_For_Facebook);
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                //"fb://page/426253597411506"
                // http://stackoverflow.com/a/24547437/1048340
//                uri = Uri.parse("fb://facewebmodal/f?href=" + CGlobalVariables.Astrosage_Url_For_Facebook);
                String str = "fb://facewebmodal/f?href="+CGlobalVariables.Astrosage_Url_For_Facebook;
                uri = Uri.parse(str);
            }
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {// or else open in browser instead//https://www.facebook.com/contasty
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CGlobalVariables.Astrosage_Url_For_Facebook)));
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

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        emailIntent.setType("text/plain");
        emailIntent.setData(Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //emailIntent.putExtra(Intent.EXTRA_EMAIL, email);

        startActivity(Intent.createChooser(emailIntent, "Send email.."));
    }

    private void openAstrosageWeb() {
        String url = CGlobalVariables.VARTA_BASE_URL;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
