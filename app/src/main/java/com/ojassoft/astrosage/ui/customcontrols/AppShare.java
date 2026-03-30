package com.ojassoft.astrosage.ui.customcontrols;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.fragments.AstroSageParentDialogFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class AppShare {

    public static final int SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE = 1007;

    private static Typeface mediumtTypeface, regularTypeface;
    public static boolean isRateDisplayed = false; // ADDED BY DEEPAK ON
    // 24-12-2014
    public static Dialog alertDialogBuilder;
    static AstrosageKundliApplication checkratedialog;
    private static String Headingtext = "";
    private static String Subheadingtext = "";
    //public static final int RATE_DIALOG_THREAD_SLEEP_TIME = 60000;

    // ADDED BY SHELENDRA ON 12.05.2015 FOR RATING BAR DIALOG

    public static void app_launched(Context mContext, String headingtxt,
                                    String subheading, String subchild) {
        checkratedialog = new AstrosageKundliApplication();


        SharedPreferences prefs = mContext.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        if (prefs.getBoolean(CGlobalVariables.APP_PREFS_Plan_for_Shareapp,
                false) || AstrosageKundliApplication.getAppContext().isIsShare_dialogChecked() == true
                || AstrosageKundliApplication.getAppContext().IsRateing_dialogChecked == true) {
            /*
             * if (!CUtils.GetSavePlanForPopupPreference(mContext)) { //
			 * CUtils.gotoProductPlanListUpdated((Activity) mContext, 1, //
			 * AppRater.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE);
			 * 
			 * }
			 */

            return;
        }
        isRateDisplayed = false;
        mediumtTypeface = CUtils.getRobotoFont(mContext,
                CUtils.getLanguageCodeFromPreference(mContext), CGlobalVariables.medium);
        regularTypeface = CUtils.getRobotoFont(mContext,
                CUtils.getLanguageCodeFromPreference(mContext), CGlobalVariables.regular);

       // SharedPreferences.Editor editor = prefs.edit();

        showShareDialog(mContext, headingtxt, subheading, subchild);
       // editor.commit();
    }

    // ADDED BY SHELENDRA ON 12.05.2015
    public static void showShareDialog(final Context mContext, final String heading,
                                       final String subheading, final String subchild) {
        FragmentManager fm = ((BaseInputActivity) mContext).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("HOME_INPUT_LANGUAGE");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ShowShareFragmentDialog showShareFragmentDialog = ShowShareFragmentDialog.getInstance(heading,subheading,subchild);
        showShareFragmentDialog.show(fm, "HOME_INPUT_LANGUAGE");
        ft.commit();
        //ft.commitAllowingStateLoss();


    }

    public static class ShowShareFragmentDialog extends AstroSageParentDialogFragment {

        String heading,subheading,subchild;
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
            View view = inflater.inflate(R.layout.share_app_dialog, container);
            TextView textViewHeading = (TextView) view
                    .findViewById(R.id.textViewHeading);
            textViewHeading.setTypeface(mediumtTypeface);
            textViewHeading.setText(heading);


            TextView textViewsubheading = (TextView) view
                    .findViewById(R.id.textViewsubheading);
            textViewsubheading.setTypeface(regularTypeface);
            textViewsubheading.setText(subheading);


            TextView textViewsubchild = (TextView) view
                    .findViewById(R.id.textViewsubchildheading);
            textViewsubchild.setText(Html.fromHtml(subchild));
            textViewsubchild.setTypeface(regularTypeface);


            TextView ratenowtxt = (TextView) view
                    .findViewById(R.id.textViewRatenow);
            //ratenowtxt.setTypeface(typeface, Typeface.BOLD);
            ratenowtxt.setTypeface(mediumtTypeface);
            ratenowtxt.setText(activity.getString(R.string.share_app));
            // ratenowtxt.setTypeface(typeface);
            TextView ratenotnowtxt = (TextView) view
                    .findViewById(R.id.textViewNotNowrating);
            ratenotnowtxt.setTypeface(mediumtTypeface);
            ratenotnowtxt.setText(activity.getString(R.string.app_donot_rate_now));

            ratenowtxt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setNotShowingInterestOnRateApp(activity);
                    AstrosageKundliApplication.getAppContext().setIsShare_dialogChecked(true);
                    CUtils.SaveShareAppDialogPreference(activity, true);
                    CUtils.shareToFriendMail(activity);

                    dialog.dismiss();

                }
            });
            ratenotnowtxt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    setNotShowingInterestOnRateApp(activity);
                    AstrosageKundliApplication.getAppContext().setIsShare_dialogChecked(true);
                    CUtils.SaveShareAppDialogPreference(activity, false);
                    if (!CUtils.GetSavePlanForPopupPreference(activity)) {
                        // StartTimerForPopup(mContext);
                    }
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

        public static ShowShareFragmentDialog getInstance(String heading,String subheading,String subchild){

            Bundle bundle = new Bundle();
            bundle.putString("heading",heading);
            bundle.putString("subheading",subheading);
            bundle.putString("subchild",subchild);

            ShowShareFragmentDialog showShareFragmentDialog = new ShowShareFragmentDialog();
            showShareFragmentDialog.setArguments(bundle);
            return showShareFragmentDialog;
        }
    }

    private static void setNotShowingInterestOnRateApp(final Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("rate_app_new",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = prefs.edit();
        editor1.putBoolean("Not_Showing_Interest_To_Rate_App", false);
        editor1.commit();
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
					

					}
				});
			}
		};
		myCountDownTimer.start();

	}*/

}
