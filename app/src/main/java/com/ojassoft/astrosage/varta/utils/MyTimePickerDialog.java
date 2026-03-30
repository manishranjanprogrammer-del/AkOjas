package com.ojassoft.astrosage.varta.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import androidx.core.content.ContextCompat;

import com.ojassoft.astrosage.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A dialog that prompts the user for the time of day using a {@link TimePicker}.
 */
public class MyTimePickerDialog extends AlertDialog implements OnClickListener,
        TimePicker.OnTimeChangedListener {


    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view      The view associated with this listener.
         * @param hourOfDay The hour that was set.
         * @param minute    The minute that was set.
         */
        void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";
    private static final String AM_PM = "am_pm";

    private final TimePicker mTimePicker;
    private final OnTimeSetListener mCallback;
    private final Calendar mCalendar;
    private final SimpleDateFormat mDateFormat;
    private Context context;

    int mInitialHourOfDay;
    int mInitialMinute;
    int mInitialSeconds;
    int mInitialAmPm;

    /**
     * @param context      Parent.
     * @param theme        the theme to apply to this dialog
     * @param callBack     How parent is notified.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     */
    public MyTimePickerDialog(Context context,
                              int theme,
                              OnTimeSetListener callBack,
                              int hourOfDay, int minute, int seconds) {
        super(context, resolveDialogTheme(context, theme));
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        final Context themeContext = context;

        mCallback = callBack;
        convertTo12HourFormat(hourOfDay);//setting 24 hour in 12 hour just to use in dialog
        mInitialMinute = minute;
        mInitialSeconds = seconds;

        mDateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
        mCalendar = Calendar.getInstance();
        updateTitle(mInitialHourOfDay, mInitialMinute, mInitialSeconds,mInitialAmPm);


        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker_dialog_varta, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
        //setButtonPanelLayoutHint(LAYOUT_HINT_SIDE);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // initialize state
        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setCurrentSecond(mInitialSeconds);
        mTimePicker.setCurrentAm_pm(mInitialAmPm);
        mTimePicker.setOnTimeChangedListener(this);
    }

    static int resolveDialogTheme(Context context, int resid) {
        if (resid == 0) {
            final TypedValue outValue = new TypedValue();
            //  context.getTheme().resolveAttribute(R.attr.datePickerDialogTheme, outValue, true);
            return outValue.resourceId;
        } else {
            return resid;
        }
    }

    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case BUTTON_POSITIVE:
                if (mCallback != null) {
                    mTimePicker.clearFocus();
                    int hour24 = CUtils.getHourIn24HourFormat(mTimePicker.getCurrentHour(),mTimePicker.getCurrentAm_pm());
                    mCallback.onTimeSet(mTimePicker, hour24,
                            mTimePicker.getCurrentMinute(), mTimePicker.getCurrentSeconds());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute, int seconds, int am_pm) {
        updateTitle(hourOfDay, minute, seconds,am_pm);
    }
//
//    public void updateTime(int hourOfDay, int minutOfHour, int seconds) {
//        mTimePicker.setCurrentHour(hourOfDay);
//        mTimePicker.setCurrentMinute(minutOfHour);
//        mTimePicker.setCurrentSecond(seconds);
//    }

    private void updateTitle(int hour, int minute, int seconds,int am_pm) {
        if (hour == 12) {
            am_pm = (am_pm == Calendar.AM) ? Calendar.PM : Calendar.AM;
        }
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, seconds);
        mCalendar.set(Calendar.AM_PM, am_pm);
        String dateString = mDateFormat.format(mCalendar.getTime());
        setTitle(dateString);
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putInt(SECONDS, mTimePicker.getCurrentSeconds());
        state.putInt(AM_PM, mTimePicker.getCurrentAm_pm());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        int amPm = savedInstanceState.getInt(AM_PM);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setCurrentSecond(seconds);
        mTimePicker.setOnTimeChangedListener(this);
        updateTitle(hour, minute, seconds,amPm);
    }

    public void convertTo12HourFormat(int hour24) {
        mInitialAmPm = (hour24 < 12) ? 0 : 1;
        mInitialHourOfDay = hour24 % 12;
        if (mInitialHourOfDay == 0) {
            mInitialHourOfDay = 12;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if(window != null) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            ColorDrawable bgColor = new ColorDrawable(ContextCompat.getColor(context, R.color.bg_card_view_color));
            InsetDrawable inset = new InsetDrawable(bgColor, 25);
            window.setBackgroundDrawable(inset);
        }
    }
}
