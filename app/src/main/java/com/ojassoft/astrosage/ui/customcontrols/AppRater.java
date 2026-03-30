package com.ojassoft.astrosage.ui.customcontrols;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.fragments.AstroSageParentDialogFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class AppRater {
    public static final int SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE = 1007;

    private static Typeface mediumtTypeface, regularTypeface;
    public static boolean isRateDisplayed = false; // ADDED BY DEEPAK ON
    // 24-12-2014
    //public static Dialog alertDialogBuilder;

    private static String Headingtext = "";
    private static String Subheadingtext = "";
    // public static final int RATE_DIALOG_THREAD_SLEEP_TIME = 60000;
    public static final int RATE_DIALOG_THREAD_SLEEP_TIME = 1000;

    public static CountDownTimer myCountDownTimer;
    // ADDED BY SHELENDRA ON 12.05.2015 FOR RATING BAR DIALOG
    static Context context;

    public static void app_launched(Context mContext, String headingtxt,
                                    String subheading, String subchild) {
        Subheadingtext = subheading;
        context = mContext;

        SharedPreferences prefs = mContext.getSharedPreferences("rate_app_new",
                Context.MODE_PRIVATE);

        // SharedPreferences.Editor editor = prefs.edit();

       /* if (prefs.getBoolean("HAS_GIVEN_RATE", false)
                || prefs.getBoolean("Not_Showing_Interest_To_Rate_App", false)
                || AstrosageKundliApplication.IsRateing_dialogChecked == true
                || AstrosageKundliApplication.isIsShare_dialogChecked() == true) {
            StartTimerForPopup(mContext);
            if (!CUtils.GetSavePlanForPopupPreference(mContext)) {
                // CUtils.gotoProductPlanListUpdated((Activity) mContext, 1,
                // AppRater.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE);

            }

            return;
        }*/
        if (prefs.getBoolean("HAS_GIVEN_RATE", false)
                || AstrosageKundliApplication.getAppContext().IsRateing_dialogChecked == true) {
           /* StartTimerForPopup(mContext);
            if (!CUtils.GetSavePlanForPopupPreference(mContext)) {
                // CUtils.gotoProductPlanListUpdated((Activity) mContext, 1,
                // AppRater.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE);

            }*/

            return;
        }

        isRateDisplayed = false;
        mediumtTypeface = CUtils.getRobotoFont(mContext,
                CUtils.getLanguageCodeFromPreference(mContext), CGlobalVariables.medium);
        regularTypeface = CUtils.getRobotoFont(mContext,
                CUtils.getLanguageCodeFromPreference(mContext), CGlobalVariables.regular);

        showRateDialog(mContext, headingtxt, subheading, subchild);
        // editor.commit();
    }

    // ADDED BY SHELENDRA ON 12.05.2015
    public static void showRateDialog(final Context mContext, final String heading,
                                      final String subheading, final String subchild) {

        if (mContext != null)
            CUtils.rateAppication(mContext, false);

        /*try {
            FragmentManager fm = ((BaseInputActivity) mContext).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("APP_RATER_DIALOG");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            ShowCustomRateFragmentDialog showCustomRateFragmentDialog = ShowCustomRateFragmentDialog.getInstance(heading, subheading, subchild);
            showCustomRateFragmentDialog.show(fm, "APP_RATER_DIALOG");
            //ft.commit();
            ft.commitAllowingStateLoss();
        } catch (Exception ex) {

            //Log.e("Exception",ex.getMessage());
        }*/

    }

    public static class ShowCustomRateFragmentDialog extends AstroSageParentDialogFragment {

        String heading, subheading, subchild;
        Activity activity;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            heading = getArguments().getString("heading");
            subheading = getArguments().getString("subheading");
            subchild = getArguments().getString("subchild");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final Dialog dialog = getDialog();
            Button imageyes, imageno;
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            View view = inflater.inflate(R.layout.rate_app_dialog, container);
            TextView textViewHeading = (TextView) view
                    .findViewById(R.id.textViewHeading);
            textViewHeading.setTypeface(mediumtTypeface);
            textViewHeading.setText(heading);

            TextView textViewsubheading = (TextView) view
                    .findViewById(R.id.textViewsubheading);
            if (subheading.equals("")) {
                textViewsubheading.setVisibility(View.GONE);
            } else {
                textViewsubheading.setTypeface(regularTypeface);
                textViewsubheading.setText(subheading);
            }

            TextView textViewsubchild = (TextView) view
                    .findViewById(R.id.textViewsubchildheading);
            textViewsubchild.setText(Html.fromHtml(subchild));
            textViewsubchild.setTypeface(regularTypeface);

            TextView ratenowtxt = (TextView) view
                    .findViewById(R.id.textViewRatenow);
            // ratenowtxt.setTypeface(typeface, Typeface.BOLD);
            ratenowtxt.setTypeface(mediumtTypeface);
            ratenowtxt.setText(activity.getString(R.string.app_ratenow));
            // ratenowtxt.setTypeface(typeface);
            TextView ratenotnowtxt = (TextView) view
                    .findViewById(R.id.textViewNotNowrating);
            ratenotnowtxt.setTypeface(mediumtTypeface);
            ratenotnowtxt.setText(activity.getString(R.string.app_donot_rate_now));
//            if (CUtils.isTablet(activity)) {
//                ImageView ganeshaImage = (ImageView) view.findViewById(R.id.ganesha_image);
//                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ganeshaImage.getLayoutParams();
//                layoutParams.width = getImageSize() / 6;
//                layoutParams.height = getImageSize() / 6;
//                ganeshaImage.setLayoutParams(layoutParams);
//
//            }
//

            ratenowtxt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    SharedPreferences prefs = activity.getSharedPreferences("rate_app_new",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("HAS_GIVEN_RATE", true);
                    editor.commit();

                    if (getContext() != null)
                        CUtils.rateAppication(getContext(), false);

                    /*AstrosageKundliApplication.getAppContext().setIsRateing_dialogChecked(true);
                    final Uri marketUri = Uri.parse("market://details?id="
                            + activity.getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);*/

                    // StartTimerForPopup(mContext);

                   /* if (!CUtils.GetSavePlanForPopupPreference(getActivity())) {
                        ;
                    }*/

                    dialog.dismiss();

                }
            });
            ratenotnowtxt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    SharedPreferences prefs = activity.getSharedPreferences("rate_app_new",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("Not_Showing_Interest_To_Rate_App", true);
                    editor.commit(); // StartTimerForPopup(mContext);
                    AstrosageKundliApplication.getAppContext().setIsRateing_dialogChecked(true);
                   /* if (!CUtils.GetSavePlanForPopupPreference(getActivity())) { //
                        StartTimerForPopup(getActivity());
                    }*/

                    dialog.dismiss();

                }
            });


            return view;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            this.activity = activity;
        }

        @Override
        public void onDetach() {
            super.onDetach();
            activity = null;
        }

        public static ShowCustomRateFragmentDialog getInstance(String heading, String subheading, String subchild) {

            Bundle bundle = new Bundle();
            bundle.putString("heading", heading);
            bundle.putString("subheading", subheading);
            bundle.putString("subchild", subchild);

            ShowCustomRateFragmentDialog showCustomRateFragmentDialog = new ShowCustomRateFragmentDialog();
            showCustomRateFragmentDialog.setArguments(bundle);
            return showCustomRateFragmentDialog;
        }
    }

    /*public static void StartTimerForPopup(final Context mContext) {

        myCountDownTimer = new CountDownTimer(RATE_DIALOG_THREAD_SLEEP_TIME,
                1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        *//*
     * CUtils.gotoProductPlanListUpdated((Activity)
     * mContext, 1,
     * AppRater.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE);
     *//*

                        String Headingtext = mContext
                                .getString(R.string.app_mainheading_text_matching);
                        *//*
     * String SubHeadingtext = mContext
     * .getString(R.string.app_subheading_text_kundali);
     *//*
                        String Subchildheading = mContext
                                .getString(R.string.app_subchild_text_share);

                        AppShare.app_launched(mContext, Headingtext,
                                Subheadingtext, Subchildheading);
                    }
                });
            }
        };
        myCountDownTimer.start();

    }*/

    /**
     * This function is used to check that dialog is to show or not
     *
     * @param mContext
     * @return 12-May-2015
     */
    public static boolean istoShowRateDialog(Context mContext) {
        boolean isShow = true;
        if ((mContext
                .getSharedPreferences("rate_app_new", Context.MODE_PRIVATE))
                .getBoolean("HAS_GIVEN_RATE", false)
                || AstrosageKundliApplication.getAppContext().IsRateing_dialogChecked == true) {

            isShow = false;
        }
        return isShow;

    }

    static private int getImageSize() {
        if(context == null) return 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

       /* Point size = new Point();
        display.getSize(size);*/
        int width = display.getWidth();
        //  int height = display.getHeight();
        return width;
    }


}
