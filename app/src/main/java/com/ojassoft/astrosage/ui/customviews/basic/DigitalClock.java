package com.ojassoft.astrosage.ui.customviews.basic;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Like AnalogClock, but digital.  Shows seconds.
 * <p>
 * FIXME: implement separate views for hours/minutes/seconds, so
 * proportional fonts don't shake rendering
 */

public class DigitalClock extends AppCompatTextView {

    private Calendar mCalendar;
    private final static String m12 = "hh:mm:ss a";
    private final static String m24 = "k:mm:ss";
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    String mFormat;

    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped) return;
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                StringBuilder sb = new StringBuilder();
                String dateStr = (String) DateFormat.format(mFormat, mCalendar);
                /*sb.append(dateStr);
                if (mCalendar.get(Calendar.AM_PM) == Calendar.AM) {
                    sb.append(" " + getContext().getString(R.string.text_am));
                } else {
                    sb.append(" " + getContext().getString(R.string.text_pm));
                }*/
                setText(dateStr);
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        /*if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m24;
        }*/
        mFormat = m12;
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }

    public void setTimeZone(TimeZone timeZone){
        mCalendar.setTimeZone(timeZone);
    }
}

