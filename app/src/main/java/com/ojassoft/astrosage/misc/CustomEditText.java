package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;

public class CustomEditText extends EditText {

    public CustomEditText(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        // TODO Auto-generated method stub
        super.setText(text, type);
    }

    @Override
    public void setError(CharSequence error) {
        // TODO Auto-generated method stub
        super.setError(error);
    }

    @Override
    public void setTypeface(Typeface tf) {
        // TODO Auto-generated method stub
        super.setTypeface(tf);
    }

}
