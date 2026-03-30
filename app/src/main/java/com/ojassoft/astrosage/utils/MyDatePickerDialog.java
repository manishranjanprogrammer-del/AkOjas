

package com.ojassoft.astrosage.utils;


import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ojassoft.astrosage.R;

/**
 * A dialog that prompts the user for the time of day using a {@link TimePicker}.
 */
public class MyDatePickerDialog extends AlertDialog implements OnClickListener,
        MyDatePicker.OnDateChangedListener {

    @Override
    public void onDateChanged(MyDatePicker view, int hourOfDay, int minute, int seconds) {
        updateTitle(hourOfDay, minute, seconds);
    }

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnDateChangedListener {

        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The hour that was set.
         * @param minute The minute that was set.
         */
        void onDateSet(MyDatePicker view, int hourOfDay, int minute, int seconds);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";
    private static final String IS_24_HOUR = "is24hour";

    private final MyDatePicker mDateDPicker;
    private final OnDateChangedListener mCallback;
    private final Calendar mCalendar;
    private final java.text.DateFormat mDateFormat;
    private Context context;

    int mInitialMonth;
    int mInitialDay;
    int mInitialYear;
    boolean mIs24HourView;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public MyDatePickerDialog(Context context,
                              OnDateChangedListener callBack,
                              int hourOfDay, int minute, int seconds, boolean is24HourView) {

        this(context, 0, callBack, hourOfDay, minute, seconds, is24HourView);
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     * @param month The initial month.
     * @param year The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public MyDatePickerDialog(Context context,
                              int theme,
                              OnDateChangedListener callBack,
                              int month, int day, int year, boolean is24HourView) {
        super(context, resolveDialogTheme(context, theme));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context=context;
        final Context themeContext = getContext();

        mCallback = callBack;
        mInitialMonth = month;
        mInitialDay = day;
        mInitialYear = year;
        mIs24HourView = is24HourView;

        mDateFormat = DateFormat.getLongDateFormat(context);
        mCalendar = Calendar.getInstance();
         updateTitle(mInitialMonth, mInitialDay, mInitialYear);



        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_picker_dialog, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
        //setButtonPanelLayoutHint(LAYOUT_HINT_SIDE);
        mDateDPicker = (MyDatePicker) view.findViewById(R.id.datepicker);

        // initialize state
        mDateDPicker.setCurrentHour(mInitialMonth);
        mDateDPicker.setCurrentMinute(mInitialDay);
        mDateDPicker.setCurrentSecond(mInitialYear);
        mDateDPicker.setIs24HourView(mIs24HourView);
        mDateDPicker.setOnDateChangedListener(this);
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
                    mDateDPicker.clearFocus();
                    mCallback.onDateSet(mDateDPicker, mDateDPicker.getCurrentSeconds(),mDateDPicker.getCurrentHour(),
                            mDateDPicker.getCurrentMinute());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    /*public void onDate(TimePicker view, int hourOfDay, int minute, int seconds) {
        updateTitle(hourOfDay, minute, seconds);
    }*/

    public void updateTime(int hourOfDay, int minutOfHour, int seconds) {
        mDateDPicker.setCurrentHour(hourOfDay);
        mDateDPicker.setCurrentMinute(minutOfHour);
        mDateDPicker.setCurrentSecond(seconds);
    }

    private void updateTitle(int hour, int minute, int seconds) {
        mCalendar.set(Calendar.MONTH, hour);
        mCalendar.set(Calendar.DAY_OF_MONTH, minute);
        mCalendar.set(Calendar.YEAR, seconds);
        TextView textView=new TextView(getContext());
        textView.setBackgroundColor(context.getResources().getColor(R.color.white));
        //   setCustomTitle(textView);
         // setTitle("HELLO");
         setTitle(mDateFormat.format(mCalendar.getTime()));

    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mDateDPicker.getCurrentHour());
        state.putInt(MINUTE, mDateDPicker.getCurrentMinute());
        state.putInt(SECONDS, mDateDPicker.getCurrentSeconds());
        state.putBoolean(IS_24_HOUR, mDateDPicker.is24HourView());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        mDateDPicker.setCurrentHour(hour);
        mDateDPicker.setCurrentMinute(minute);
        mDateDPicker.setCurrentSecond(seconds);
        mDateDPicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mDateDPicker.setOnDateChangedListener(this);
        updateTitle(hour, minute, seconds);
    }


}
