package com.ojassoft.astrosage.ui.act;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.Iterator;
import java.util.List;

/*import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;*/

public class ActAstroold extends BaseInputActivity {

    String TAG = "ActAstroShop";
    private String title = "AstroSage.com : All Articles";
    WebView webView = null;
    private ProgressBar progressBar;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout relativeView;
    private Button textShowStatus;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private int type=0;
    private TextView tvTitle;


    Typeface typeface;
    int SCREEN_TYPE = -1;

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

  //  private Toolbar toolbar_magazine;
    private TextView tvToolBarTitle;


    public ActAstroold() {
        super(R.string.app_name);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();// ADDED BY HEVENDRA ON 24-12-2014
        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.astroweb);
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        webView = (WebView) findViewById(R.id.webView);


        tvTitle = (TextView) findViewById(R.id.tvTitle);

        tvTitle.setTypeface(typeface);
        type=getIntent().getExtras().getInt("TYPE", 0);
        if(type==0)
        {
            tvTitle.setText(getResources().getString(R.string.astro_services));
        }
        else
        {
            tvTitle.setText(getResources().getString(R.string.astro_astrologer));
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initProgressBar();
        initWebView();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();


       /* typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astroshop_main);
        try {
            //DISABLED BY BIJENDRA ON 17-08-15
            *//*CUtils.applyTypeFaceOnActionBarTitle(
					ActAstroShop.this,
					typeface,
					getResources().getStringArray(
							R.array.module_names_for_title_and_home_screen)[CGlobalVariables.MODULE_ASTRO_SHOP]);*//*
            //ADDED BY BIJENDRA ON 17-08-15
            showToolBarTitle(
                    typeface,
                    getResources().getStringArray(
                            R.array.module_names_for_title_and_home_screen)[CGlobalVariables.MODULE_ASTRO_SHOP]);
        } catch (Exception e) {

        }*/

    }

    /**
     * This function is used to show tool bar title
     *
     * @param typeface
     * @param titleToshow 17-Aug-2015
     */
    private void showToolBarTitle(Typeface typeface, String titleToshow) {
        tvToolBarTitle.setTypeface(typeface);
        if (titleToshow != null)
            tvToolBarTitle.setText(titleToshow);
        else
            // ADDED BY BIJENDRA ON 17-06-14
            tvToolBarTitle.setText("");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CUtils.hideMyKeyboard(this);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initProgressBar() {
        progressBar.setVisibility(View.VISIBLE);

        textShowStatus = new Button(this, null, android.R.attr.buttonStyle);

        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeView = new RelativeLayout(this);
        relativeView.setLayoutParams(params);
        relativeView.setGravity(Gravity.CENTER);
    }





    private void initWebView() {
        //Log.e("Loading web view", "******");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(getAstroShopUrl());
        webView.setWebViewClient(new myWebClient());
        webView.setWebChromeClient(new WebChromeClient() {

            // For Android 3.0+
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;

                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                ActAstroold.this.startActivityForResult(
                        Intent.createChooser(i, "Select Image"),
                        FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback uploadMsg,
                                        String acceptType) {
                mUploadMessage = uploadMsg;

                Intent i = new Intent(Intent.ACTION_PICK);
                //i.setType("*/*");
                i.setType("image/*");
                ActAstroold.this.startActivityForResult(
                        Intent.createChooser(i, "Select Image"),
                        FILECHOOSER_RESULTCODE);
            }

            // For Android 4.1
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                mUploadMessage = uploadMsg;

                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                ActAstroold.this.startActivityForResult(
                        Intent.createChooser(i, "Select Image"),
                        ActAstroold.FILECHOOSER_RESULTCODE);

            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, progress);
                try {
                    showProgressBar(true);

                    if (progress == 100)
                        showProgressBar(false);

                } catch (Exception e) {
                    //Log.e("ActAstroShop", e.getMessage());
                }
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    private void showProgressBar(boolean show) {
        try {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
            webView.removeAllViews();
        } catch (Exception e) {
            //Log.e("ActAstroShop", e.getMessage());
        }
    }

    private void loadUrl() {
        if (CUtils.isConnectedWithInternet(this)) {
            webView.loadUrl(getAstroShopUrl());
        } else {
            showProgressBar(false);
            showWarning(getResources().getString(R.string.no_internet_tap_to_retry));
        }
    }

    private String getAstroShopUrl() {
        //Log.e("type","hj"+type);
        String astroShopUrl = CGlobalVariables.ASTRO_SHOP_URL;
        if(type==0)
            astroShopUrl=CGlobalVariables.ASTRO_SHOP_SERVICES;
        else if(type==1)
        {
            astroShopUrl=CGlobalVariables.ASTRO_SHOP_ASTROLOGER;

        }


     //   String astroShopUrl="https://www.google.co.in/?gfe_rd=cr&ei=ph00V6CCD8WL8QfksI34Bg".trim();
        return astroShopUrl;
    }

    private void showWarning(String message) {
        textShowStatus.setText(message);
        Drawable icon = getResources().getDrawable(R.drawable.ic_av_replay);
        textShowStatus.setCompoundDrawablesWithIntrinsicBounds(null, icon,
                null, null);
        textShowStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl();
            }
        });
        relativeView.removeAllViews();
        relativeView.addView(textShowStatus);
        webView.removeAllViews();
        webView.addView(relativeView, params);
    }

    /*
     * private void setBlogTitle(String title) {
     * CUtils.applyTypeFaceOnActionBarTitle(ActAstroShop.this,
     * Typeface.DEFAULT,title); }
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                } else {
                    // changes by abhishek bcz GET_TASKS permission is depricated
                    if (isTaskRoot())
                        CUtils.restartApplication(ActAstroold.this);
                    ActAstroold.this.finish();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isApplicatonOpened() {
        boolean haveActivitiesInTask = false;
        ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(20);
        Iterator<RunningTaskInfo> itr = runningTaskInfoList.iterator();
        while (itr.hasNext()) {
            RunningTaskInfo runningTaskInfo = (RunningTaskInfo) itr.next();
            if (runningTaskInfo.baseActivity.getPackageName().equalsIgnoreCase(
                    "com.ojassoft.astrosage")) {
                int numOfActivities = runningTaskInfo.numActivities;
                if (numOfActivities > 1)
                    haveActivitiesInTask = true;
            }

        }
        return haveActivitiesInTask;
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                view.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            // progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    final String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.loadUrl("about:blank");
            if (!CUtils.isConnectedWithInternet(ActAstroold.this)) {
                view.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        showWarning(getResources().getString(
                                R.string.no_internet_tap_to_retry));
                    }
                }, 1000);
            } else {
                view.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        showWarning(description);
                    }
                }, 1000);
            }
        }
    }
}

