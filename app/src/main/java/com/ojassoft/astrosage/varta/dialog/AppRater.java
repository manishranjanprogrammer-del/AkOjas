package com.ojassoft.astrosage.varta.dialog;

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
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import static com.ojassoft.astrosage.utils.CUtils.rateAppication;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;


public class AppRater {
    public static final int SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE = 1007;

    private static Typeface ralwayBold, ralewayReguler;
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

        showRateDialog(mContext, headingtxt, subheading, subchild);
    }

    // ADDED BY SHELENDRA ON 12.05.2015
    public static void showRateDialog(final Context mContext, final String heading,
                                      final String subheading, final String subchild) {
        try {
            FragmentManager fm = ((BaseActivity) mContext).getSupportFragmentManager();
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

        }

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
            LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
            CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
            final Dialog dialog = getDialog();
            Button imageyes, imageno;
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            View view = inflater.inflate(R.layout.rate_app_dialog, container);
            TextView textViewHeading = (TextView) view.findViewById(R.id.textViewHeading);
            textViewHeading.setTypeface(ralwayBold);
            textViewHeading.setText(heading);

            TextView textViewsubheading = (TextView) view
                    .findViewById(R.id.textViewsubheading);
            if (subheading.equals("")) {
                textViewsubheading.setVisibility(View.GONE);
            } else {
                FontUtils.changeFont(context, textViewsubheading, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                //textViewsubheading.setTypeface(ralewayReguler);
                //textViewsubheading.setText(subheading);
                textViewsubheading.setText(Html.fromHtml(subheading));
            }

            TextView textViewsubchild = (TextView) view.findViewById(R.id.textViewsubchildheading);
            textViewsubchild.setText(Html.fromHtml(subchild));
            FontUtils.changeFont(context, textViewsubchild, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            //textViewsubchild.setTypeface(ralewayReguler);

            TextView ratenowtxt = (TextView) view.findViewById(R.id.textViewRatenow);
            FontUtils.changeFont(context, ratenowtxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            //ratenowtxt.setTypeface(ralwayBold);
            ratenowtxt.setText(activity.getString(R.string.app_ratenow));
            TextView ratenotnowtxt = (TextView) view.findViewById(R.id.textViewNotNowrating);
            FontUtils.changeFont(context, ratenotnowtxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            //ratenotnowtxt.setTypeface(ralwayBold);
            ratenotnowtxt.setText(activity.getString(R.string.app_donot_rate_now));

            ratenowtxt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_RATE_NOW_BTN_CLICK,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    SharedPreferences prefs = activity.getSharedPreferences("rate_app_new",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("HAS_GIVEN_RATE", true);
                    editor.commit();


                    //rateAppication(activity);

                    /*final Uri marketUri = Uri.parse("market://details?id="
                            + activity.getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);*/

                    final Uri marketUri = Uri.parse(com.ojassoft.astrosage.utils.CGlobalVariables.RATE_APP_URL);
                    Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    dialog.dismiss();

                }
            });
            ratenotnowtxt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_RATE_NOT_NOW_BTN_CLICK,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    SharedPreferences prefs = activity.getSharedPreferences("rate_app_new",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("Not_Showing_Interest_To_Rate_App", true);
                    editor.commit(); // StartTimerForPopup(mContext);

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

    static private int getImageSize() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

       /* Point size = new Point();
        display.getSize(size);*/
        int width = display.getWidth();
        //  int height = display.getHeight();
        return width;
    }


}
