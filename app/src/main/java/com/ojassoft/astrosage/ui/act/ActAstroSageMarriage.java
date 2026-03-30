package com.ojassoft.astrosage.ui.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.Iterator;
import java.util.List;

/*import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;*/

public class ActAstroSageMarriage extends BaseInputActivity {
    String TAG = "ActAstroSageMarriage";
    private String title = "AstroSage.com : All Articles";
    WebView webView = null;
    private ProgressBar progressBar;
    private RelativeLayout.LayoutParams params;
    private RelativeLayout relativeView;
    private Button textShowStatus;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    public Typeface mediumTypeface;
    Typeface typeface;
    int SCREEN_TYPE = -1;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int FILECHOOSER_RESULTCODE_FORLOLLIPOP = 2;

    private Toolbar toolbar_magazine;
    private TextView tvToolBarTitle;

    public ActAstroSageMarriage() {
        super(R.string.astrosage_name);
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_astrosage_matrimony);
        webView = (WebView) findViewById(R.id.webView);
        toolbar_magazine = (Toolbar) findViewById(R.id.toolbar_magazine);
        tvToolBarTitle = (TextView) toolbar_magazine.findViewById(R.id.tvToolBarTitle);
        setSupportActionBar(toolbar_magazine);

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();// ADDED BY HEVENDRA ON 24-12-2014
        mediumTypeface = CUtils.getRobotoFont(
                getApplicationContext(),
                CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.medium);
        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        SCREEN_TYPE = getIntent().getIntExtra(
                CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                CGlobalVariables.MODULE_ASTROSAGE_ARTICLES);
        initProgressBar();
        initWebView();

        // Set up action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
      /*  getSupportActionBar().setLogo(
                getResources().getDrawable(R.drawable.astrosage_logo));*/
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(),
                CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.regular);
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_MARRIAGE) {
            /*CUtils.applyTypeFaceOnActionBarTitle(ActAstroSageMarriage.this,
                    typeface, getResources().getString(R.string.marriage_title));*/
            showToolBarTitle(
                    mediumTypeface, getResources().getString(R.string.marriage_title));
        } // END
        else if (title == null) {
			/*CUtils.applyTypeFaceOnActionBarTitle(
					ActAstroSageMarriage.this,
					typeface,
					getResources().getStringArray(
							R.array.module_names_for_title_and_home_screen)[SCREEN_TYPE]);*/
            showToolBarTitle(
                    mediumTypeface,
                    getResources().getStringArray(
                            R.array.module_names_for_title_and_home_screen)[SCREEN_TYPE]);
        }

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
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);

        textShowStatus = new Button(this, null, android.R.attr.buttonStyle);

        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeView = new RelativeLayout(this);
        relativeView.setLayoutParams(params);
        relativeView.setGravity(Gravity.CENTER);
    }

    @SuppressLint("NewApi")
    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        webView.loadUrl(getMarriageUrl());

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                // handle download, here we use brower to download, also you can try other approach.
                if(url != null) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        webView.setWebViewClient(new myWebClient());
        webView.setWebChromeClient(new WebChromeClient() {

            // For Android 3.0+
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;

                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                ActAstroSageMarriage.this.startActivityForResult(
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
                ActAstroSageMarriage.this.startActivityForResult(
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
                ActAstroSageMarriage.this.startActivityForResult(
                        Intent.createChooser(i, "Select Image"),
                        ActAstroSageMarriage.FILECHOOSER_RESULTCODE);

            }


            //For Android 5 and more(Monika)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {

                Log.e("onReceivedError ","onReceivedError Error "+ mUploadMessageForAndroid5);
                if (mUploadMessageForAndroid5 != null) {
                    mUploadMessageForAndroid5.onReceiveValue(null);
                    mUploadMessageForAndroid5 = null;
                }

                mUploadMessageForAndroid5 = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try
                {
                    startActivityForResult(intent, FILECHOOSER_RESULTCODE_FORLOLLIPOP);
                } catch (ActivityNotFoundException e)
                {
                    mUploadMessageForAndroid5 = null;
                    Toast.makeText(ActAstroSageMarriage.this.getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
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
                    //Log.e("ActAstroSageMarriage", e.getMessage());
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

        }else if(requestCode == FILECHOOSER_RESULTCODE_FORLOLLIPOP)
        {
            try{
                Log.e("onReceivedError ","onReceivedError Error onAct "+ mUploadMessageForAndroid5);
                Log.e("onReceivedError ","onReceivedError Error intent "+ intent.getData());
                Uri result;
                if (null == mUploadMessageForAndroid5)
                    return;

                if (intent == null || resultCode != Activity.RESULT_OK) {
                    result = null;
                } else {
                    result = intent.getData();
                }
                if (result != null) {
                    Log.v("", "" + " # result.getPath()=" + result.getPath());
                    //  mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                    mUploadMessageForAndroid5.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                } else {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                }

                mUploadMessageForAndroid5 = null;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
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
            //Log.e("ActAstroSageMarriage", e.getMessage());
        }
    }

    private void loadUrl() {
        if (CUtils.isConnectedWithInternet(this)) {
            webView.loadUrl(getMarriageUrl());
        } else {
            showProgressBar(false);
            showWarning(getResources().getString(R.string.no_internet_tap_to_retry));
        }
    }

    private String getMarriageUrl() {
        String blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_EN;

        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH)
            blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_EN;
        else if (LANGUAGE_CODE == CGlobalVariables.HINDI)
            blogUrl = CGlobalVariables.ASTROSAGE_MARRIAGE_URL_HI;

        return blogUrl;
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
     * CUtils.applyTypeFaceOnActionBarTitle(ActAstroSageMarriage.this,
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
                        CUtils.restartApplication(ActAstroSageMarriage.this);
                    ActAstroSageMarriage.this.finish();
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
            if (!CUtils.isConnectedWithInternet(ActAstroSageMarriage.this)) {
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
}
