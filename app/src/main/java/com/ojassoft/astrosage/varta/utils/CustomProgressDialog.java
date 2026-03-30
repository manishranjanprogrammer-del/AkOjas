package com.ojassoft.astrosage.varta.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;


public class CustomProgressDialog extends ProgressDialog {

    Context context;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
        try {
            if (context != null) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                getWindow().requestFeature(Window.FEATURE_PROGRESS);
                getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
                setIndeterminate(true);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void show() {
        try {
            if (context != null) {
                super.show();
                setContentView(R.layout.progress_dialog_layout_varta);
                ImageView custImageView = findViewById(R.id.cust_progress_gif);
                Glide.with(context.getApplicationContext()).load(R.drawable.new_ai_loader).into(custImageView);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        try {
            if (context != null && isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {

        }
    }
}
