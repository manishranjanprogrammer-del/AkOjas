package com.ojassoft.astrosage.varta.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomDialog extends Dialog {
    private Activity activity;
    private TextView myView;
    private ImageView giftImageView;
    public CustomDialog(@NonNull Activity activity, TextView myView, ImageView giftImageView) {
        super(activity);
        this.activity = activity;
        this.myView = myView;
        this.giftImageView = giftImageView;
        showKeyboard();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            myView.setVisibility(View.VISIBLE);
            giftImageView.setVisibility(View.VISIBLE);
            closeOnlyKeyboard();
            this.cancel();
            return true;
        }
        return false;
    }
    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onBackPressed() {
        myView.setVisibility(View.VISIBLE);
        giftImageView.setVisibility(View.VISIBLE);
        closeOnlyKeyboard();
        this.cancel();
        super.onBackPressed();
    }

    public void closeOnlyKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager
                    = (InputMethodManager)
                    activity.getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }

}
