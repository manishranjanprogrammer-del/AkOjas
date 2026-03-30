package com.ojassoft.astrosage.ui.customcontrols;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;

public class MyCustomToast {
    Context _context;
    LayoutInflater _inflater;
    View layout;
    TextView textMsg;
    Activity _activity;
    Typeface typeface;
    Toast toast;


    public MyCustomToast(Context context, LayoutInflater inflater,
                         Activity activity, Typeface typeface) {
        _context = context;
        _inflater = inflater;
        _activity = activity;
        this.typeface = typeface;
        layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) _activity.findViewById(R.id.toast_layout_root));
        textMsg = (TextView) layout.findViewById(R.id.textMsg);
        textMsg.setTypeface(typeface);
        toast = new Toast(_context);
    }

    public void show(String strMsg) {
        int Y = _context.getResources().getDimensionPixelSize(
                R.dimen.abs__action_bar_default_height);
        textMsg.setText(strMsg);
        textMsg.setTypeface(typeface);
        // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 75);
        toast.setGravity(Gravity.TOP, 0, Y);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
