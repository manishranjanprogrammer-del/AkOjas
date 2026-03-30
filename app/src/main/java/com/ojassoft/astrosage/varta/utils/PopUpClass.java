package com.ojassoft.astrosage.varta.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.dao.NotificationDBManager;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.AboutUsActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;

import static android.content.Context.MODE_PRIVATE;

public class PopUpClass {

    Context context;

    public PopUpClass(Context context){
        this.context = context;
    }
    //PopupWindow display method

    public void showPopupWindow(final View view) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.show_logout_layout, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        //popupWindow.showAtLocation(view, Gravity.DISPLAY_CLIP_VERTICAL, 0, 0);
        popupWindow.showAtLocation(view, Gravity.TOP|Gravity.RIGHT, 0, 0);
       
        //Initialize the elements of our window, install the handler
        TextView welcometext = popupView.findViewById(R.id.welcometext);
        TextView userPhoneNoText = popupView.findViewById(R.id.user_phone_no_text);
        TextView aboutusTxt = popupView.findViewById(R.id.aboutus_txt);
        TextView shareTxt = popupView.findViewById(R.id.share_txt);
        userPhoneNoText.setText(CUtils.getCountryCode(context) + CUtils.getUserID(context));

        TextView logoutButton = popupView.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LOGOUT,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                onLogoutClicked();
            }
        });

        FontUtils.changeFont(context, aboutusTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, welcometext, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, userPhoneNoText, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, logoutButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, shareTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });

        aboutusTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AboutUsActivity.class);
                context.startActivity(intent);
                popupWindow.dismiss();
            }
        });

        shareTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SHARE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.shareWithFriends(context,context.getResources().getString( R.string.shareAppBody));
            }
        });
    }

    private void onLogoutClicked() {
        try {
            /* if (cDatabaseHelperOperations == null) {
                cDatabaseHelperOperations = new CDatabaseHelperOperations(context);
            }
            new DeleteRecords().execute();*/
            CUtils.clearAllSharedPreferences(context);
            CUtils.saveLoginDetailInPrefs(context, "", false, "", "0");
            CUtils.setUserLoginPassword(context, "");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(context, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.BASE_INPUT_SCREEN);
            context.startActivity(intent);
            ((Activity)context).finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}