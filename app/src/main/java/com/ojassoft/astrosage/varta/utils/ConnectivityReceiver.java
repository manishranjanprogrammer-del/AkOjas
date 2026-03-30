package com.ojassoft.astrosage.varta.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

public class ConnectivityReceiver extends BroadcastReceiver {

    private Snackbar snackbar;
    private Activity currentActivity;

    @Override
    public void onReceive(Context context, Intent arg1) {

        try {
            currentActivity = ((AstrosageKundliApplication) context.getApplicationContext()).getCurrentActivity();
            if (currentActivity == null) return;
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            String className = currentActivity.getComponentName().getClassName();
            if (className.contains("LanguageSelectionActivity") || className.contains("AppIntroActivity")) {
                return;
            }
            if (!CUtils.isConnectedWithInternet(currentActivity)) {

                BottomNavigationView bottomNavigationView = currentActivity.findViewById(R.id.nav_view);
                if (bottomNavigationView != null) {
                    showSnackbarNetworkConnection(context, bottomNavigationView, true);
                } else {
                    Toolbar toolbarView = currentActivity.findViewById(R.id.toolbar);
                    if (toolbarView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        showSnackbarOverToolBar(context, toolbarView);
                    } else {
                        ScrollView scrollView = currentActivity.findViewById(R.id.container_layout);
                        if (scrollView != null) {
                            showSnackbarNetworkConnection(context, scrollView, false);
                        } else {
                            showSnackBarOverRootView();
                        }
                    }
                }
            }
        } catch (Exception e) {
            //Log.e("ConnectivityReceiver", e.toString());
            showSnackBarOverRootView();
        }

    }

    private void showSnackBarOverRootView() {
        try {
            View rootView = currentActivity.getWindow().getDecorView().getRootView();
            showSnackbarNetworkConnection(currentActivity, rootView, false);
        } catch (Exception e) {
            //
        }
    }

    public void showSnackbarNetworkConnection(Context context, View view, boolean isNavView) {
        try {
            String text = context.getResources().getString(R.string.nw_conn_lost);
            snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimary_day_night));
            TextView tv = snackbar.getView().findViewById(R.id.snackbar_text); //snackbar_text
            tv.setTextColor(context.getResources().getColor(R.color.white));
            tv.setTextSize(16);
            FontUtils.changeFont(context, tv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            if (isNavView) {
                //snackbar.setAnchorView(view);
            }
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showSnackbarOverToolBar(Context context, Toolbar view) {
        try {
            String text = context.getResources().getString(R.string.nw_conn_lost);
            snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.Orangecolor));
            TextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text); //snackbar_text
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(16);
            FontUtils.changeFont(context, tv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}