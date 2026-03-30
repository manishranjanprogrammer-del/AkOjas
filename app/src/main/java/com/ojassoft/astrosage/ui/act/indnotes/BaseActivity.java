package com.ojassoft.astrosage.ui.act.indnotes;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.interfaces.AlertClicked;
import com.ojassoft.astrosage.utils.indnotes.AnalyticsUtils;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;
import com.ojassoft.astrosage.utils.indnotes.SharedPreferenceUtils;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, AlertClicked, AppConstants {

    Activity currentActivity;
    public Bundle bundle;
    Context context;
    ProgressDialog pdialog;
    Dialog dialog;
    private static boolean isFirstInit = true;

    protected abstract void initViews();

    protected abstract void initContext();

    protected abstract void initListners();

    protected abstract boolean isActionBar();

    protected abstract boolean isHomeButton();

    Dialog customDialog;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    protected String remindarMessage;
    protected String remindarDate;
    protected boolean isRemindarNotification;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        CoordinatorLayout layout = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.activity_base_activity, null);
        LinearLayout activityLayout = layout.findViewById(R.id.activity_layout);
        getLayoutInflater().inflate(layoutResID, activityLayout, true);

        super.setContentView(layout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        if (isActionBar()) {
            setSupportActionBar(toolbar);
            applyFontForToolbarTitle();
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (isHomeButton()) {
            settingHomeButton();
        }
        initContext();
        initViews();
        initListners();

        if (isFirstInit) {
            initRatingDialog();
            isFirstInit = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //changeLocale();
    }

    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("event_app_reating"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReminderReceiver,
                new IntentFilter("event_reminder"));
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }
        super.onBackPressed();
    }

    public void startActivity(Activity activity, Class newclass, Bundle bundle, boolean isResult, int requestCode, boolean animationRequired, int animationType) {

        Intent intent = new Intent(activity, newclass);

        if (bundle != null)
            intent.putExtras(bundle);
        if (!isResult && !animationRequired)
            startActivity(intent);
        else if (!isResult && animationRequired) {
            startActivity(intent);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }

        } else if (isResult && animationRequired) {
            startActivityForResult(intent, requestCode);
            if (animationType == AppConstants.ANIMATION_SLIDE_LEFT) {
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else if (animationType == AppConstants.ANIMATION_SLIDE_UP) {
                overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_stopping_exiting_activity);
            }
        } else
            startActivityForResult(intent, requestCode);
    }

    public void progressDialog(Context context, String title, String message, boolean cancelable, boolean isTitle) {
        if (pdialog == null) {
            pdialog = new ProgressDialog(context);
        }


        if (isTitle) {
            pdialog.setTitle(title);
        }

        pdialog.setMessage(message);

        if (!cancelable) {
            pdialog.setCancelable(false);
        }

        if (!pdialog.isShowing()) {
            pdialog.show();

        }

    }

    public void cancelProgressDialog() {
        if (pdialog != null && pdialog.isShowing()) {
            pdialog.cancel();
            pdialog = null;
        }

    }


    public void toast(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }

    public void showSnackbar(View view, String text) {

        // added by ankit on 6/3/2019
        try {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getColor(R.color.colorPrimary_day_night));
            TextView tv = snackbar.getView().findViewById(R.id.snackbar_text); //snackbar_text
            tv.setTextColor(getColor(R.color.white));
            snackbar.show();
        }catch (Exception e){
            //
        }
        // commented by ankit due to text color issue.
//        Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    public void toastTesting(String text, boolean isLengthLong) {
        int time;
        if (isLengthLong) {
            time = Toast.LENGTH_LONG;
        } else {
            time = Toast.LENGTH_SHORT;
        }
        Toast.makeText(currentActivity, text, time).show();
    }


    public static void log(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }


    public void logTesting(String key, String value, int LogType) {
        if (LogType == Log.ASSERT) {
            Log.wtf(key, value);
        } else if (LogType == Log.DEBUG) {
            Log.d(key, value);
        } else if (LogType == Log.ERROR) {
            Log.e(key, value);
        } else if (LogType == Log.INFO) {
            Log.i(key, value);
        } else if (LogType == Log.VERBOSE) {
            Log.v(key, value);
        } else if (LogType == Log.WARN) {
            Log.w(key, value);
        }

    }

    protected void toHideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void toOpenKeyboard() {
        View view = this.getCurrentFocus();
        if (view == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 1);
    }

    public void settingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
    }

    public void settingTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void removingHomeButton() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void popBackStack(String tag) {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 0) {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    public void alert(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (isTitle) {
            builder.setTitle(title);
        }


        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onAlertClicked(alertType);
            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, null);
        }

        builder.show();


    }

    public void applyFontForToolbarTitle() {
        try {
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                View view = toolbar.getChildAt(i);
                if (view instanceof TextView) {
                    TextView titleTV = (TextView) view;
                    if (titleTV.getText().equals(getTitle())) {
                        //titleTV.setTextSize(getResources().getDimension(R.dimen.font_toolbar_title));
                        FontUtils.changeFont(this, titleTV, FONT_ROBOTO_MEDIUM);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void changeLocale() {

        int oldLanguageIndex = SharedPreferenceUtils.getInstance(BaseActivity.this).getInteger(AppConstants.app_language_key);
        Locale locale = new Locale("hi");
        switch (oldLanguageIndex) {
            case AppConstants.ENGLISH:
                locale = Locale.ENGLISH;
                break;
            case AppConstants.HINDI:
                locale = new Locale("hi");
                break;
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());
    }

    public void switchContent(Fragment fragment, boolean addToBackStack,
                              boolean add, String tag) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /*if (!add) {
            ft.replace(R.id.fragmentContainer, fragment, tag);
        } else {
            ft.add(R.id.fragmentContainer, fragment, tag);
        }
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commitAllowingStateLoss();*/
    }

    public void toOpenShareDialog() {
        AnalyticsUtils.setAnalytics(context, "Share It Clicked");
        String textToShare = "";//getString(R.string.text_share_app) + " - " + APP_URL;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textToShare);
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(Intent.createChooser(intent, getResources().getString(R.string.text_share_it)));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void setSelection(int position) {
        try {
            if (drawerLayout != null) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (position) {

        }
    }

    public void toOpenNotesActivity() {
        if (currentActivity == null) return;
        AnalyticsUtils.setAnalytics(currentActivity, "Date wise Notes Clicked");
        startActivity(currentActivity, NotesActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);
    }

    @Override
    protected void onStop() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReminderReceiver);
        super.onStop();
    }

    public void initRatingDialog() {
        boolean isAppRated = SharedPreferenceUtils.getInstance(context).getBoolean(IS_APP_RATED);
        if (!isAppRated) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent("event_app_reating");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }, APP_RATE_TIME);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //openCustomRateFragmentDialog();
        }
    };

    private BroadcastReceiver mReminderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            String remindarDate = intent.getStringExtra(KEY_REMIND_DATE);
            String remindarMessage = intent.getStringExtra(KEY_REMIND_TEXT);
            alert(context, remindarDate, remindarMessage, getString(R.string.ok), getString(R.string.cancel), false, true, ALERT_TYPE_NO_NETWORK);
        }
    };

    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffc107"));
        }
    }
}