package com.ojassoft.astrosage.varta.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class ExpandableTextView  extends TextView {

    private final String readMoreText = "...read more";
    private final int readMoreColor = Color.parseColor("#4A0281");

    private int _maxLines = 6;
    private CharSequence originalText;

    public ExpandableTextView(Context context) {
        super(context);
        init(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);

                truncateText();
            }
        });
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);

        if (originalText == null) {
            originalText = text;
        }
    }

    @Override
    public int getMaxLines() {
        return _maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        _maxLines = maxLines;
    }

    public void truncateText() {

        int maxLines = _maxLines;
        String text = getText().toString();

        if (getLineCount() >= maxLines) {

            int lineEndIndex = getLayout().getLineEnd(maxLines - 1);

            String truncatedText = getText().subSequence(0, lineEndIndex - readMoreText.length() + 1) + readMoreText;

            ///Log.e("READ MORE TEXT ", truncatedText);

            Spannable spannable = new SpannableString(truncatedText);

            spannable.setSpan(new ForegroundColorSpan(readMoreColor), truncatedText.length() - readMoreText.length(), truncatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannable, BufferType.SPANNABLE);

            super.setMaxLines(_maxLines);
        }
    }


    public void expandText() {
        setText(originalText);
        super.setMaxLines(1000);
    }

    public void reset() {
        originalText = null;
    }
}
