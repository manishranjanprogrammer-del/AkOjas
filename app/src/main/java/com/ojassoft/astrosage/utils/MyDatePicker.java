

package com.ojassoft.astrosage.utils;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;

import com.ojassoft.astrosage.R;

//this one because we make another methoid for device below honeycomb so line added tested
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyDatePicker extends FrameLayout {
      int maxValue=0;

    private static final OnDateChangedListener NO_OP_CHANGE_LISTENER = new OnDateChangedListener() {
        public void onDateChanged(MyDatePicker view, int hourOfDay, int minute, int seconds) {
        }
    };

    public static final Formatter TWO_DIGIT_FORMATTER =
            new Formatter() {

                @Override
                public String format(int value) {
                    // TODO Auto-generated method stub
                    return String.format(Locale.ENGLISH,"%02d", value);
                }
            };

    // state
    private int mCurrentMonth = 0; // 0-23
    private int mCurrentDay = 0; // 0-59
    private int mCurrentYear = 0; // 0-59
    private Boolean mIs24HourView = false;
    private boolean mIsAm;

    // ui components
    private final NumberPicker mMonthPicker;
    private final NumberPicker mDayPicker;
    private final NumberPicker mYearPicker;



    // callbacks
    private OnDateChangedListener mOnDateChangedListener;

    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    public interface OnDateChangedListener {

        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The current hour.
         * @param minute The current minute.
         * @param seconds The current second.
         */
        void onDateChanged(MyDatePicker view, int hourOfDay, int minute, int seconds);
    }

    public MyDatePicker(Context context) {
        this(context, null);
    }

    public MyDatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.date_picker_widget,
                this, // we are the parent
                true);

        mMonthPicker = (NumberPicker) findViewById(R.id.month);

        setDividerColor(mMonthPicker);



        mDayPicker = (NumberPicker) findViewById(R.id.date);
        setDividerColor(mDayPicker);

        mDayPicker.setMinValue(1);
          maxValue=getMaxValue(mCurrentMonth,mCurrentYear);
        mDayPicker.setMaxValue(maxValue);
        mDayPicker.setFormatter(TWO_DIGIT_FORMATTER);


        // digits of seconds
        mYearPicker = (NumberPicker) findViewById(R.id.year);
        setDividerColor(mYearPicker);
        mYearPicker.setMinValue(1600);
        mYearPicker.setMaxValue(2400);
        mYearPicker.setFormatter( TWO_DIGIT_FORMATTER);
        mDayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                mCurrentDay = newVal;
                if(newVal==1&&maxValue==oldVal)
                {
                    if (mCurrentMonth == 11) {
                        mCurrentMonth = 0;
                        mMonthPicker.setValue(mCurrentMonth);
                        mCurrentYear = mCurrentYear + 1;
                        mYearPicker.setValue(mCurrentYear);
                    }
                    else {
                        mCurrentMonth = mCurrentMonth + 1;
                        mMonthPicker.setValue(mCurrentMonth);
                        maxValue = getMaxValue(mCurrentMonth, mCurrentYear);
                        mDayPicker.setMaxValue(maxValue);
                    }
                }
                if(newVal==maxValue&&oldVal==1) {
                    if (mCurrentMonth == 0) {
                        mCurrentMonth = 11;
                        mMonthPicker.setValue(mCurrentMonth);
                        mCurrentYear = mCurrentYear - 1;
                        mYearPicker.setValue(mCurrentYear);
                    } else
                        if (mCurrentMonth == 11) {
                            mCurrentMonth = 0;
                            mMonthPicker.setValue(mCurrentMonth);
                            mCurrentYear = mCurrentYear + 1;
                            mYearPicker.setValue(mCurrentYear);
                        } else {
                            mCurrentMonth = mCurrentMonth - 1;
                            mMonthPicker.setValue(mCurrentMonth);
                        }


                }

                    maxValue= getMaxValue(mCurrentMonth,mCurrentYear);
                    mDayPicker.setMaxValue(maxValue);


                onTimeChanged();
            }
        });
        mYearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCurrentYear = newVal;
               maxValue= getMaxValue(mCurrentMonth,newVal);
                mDayPicker.setMaxValue(maxValue);
                onTimeChanged();
            }
        });

        mMonthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                mCurrentMonth = newVal;
                maxValue=getMaxValue(newVal,mCurrentYear);
                mDayPicker.setMaxValue(maxValue);
                if(oldVal==11&&newVal==0)
                {
                    mCurrentYear=mCurrentYear+1;
                    mYearPicker.setValue(mCurrentYear);
                }
                if(oldVal==0&&newVal==11)
                {
                    mCurrentYear=mCurrentYear-1;
                    mYearPicker.setValue(mCurrentYear);
                }
                onTimeChanged();
            }
        });

        configurePickerRanges();


        Calendar cal = Calendar.getInstance();
        setOnDateChangedListener(NO_OP_CHANGE_LISTENER);

        setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        setCurrentMinute(cal.get(Calendar.MINUTE));
        setCurrentSecond(cal.get(Calendar.SECOND));

        mIsAm = (mCurrentMonth < 12);



        if (!isEnabled()) {
            setEnabled(false);
        }
    }


    public int getMaxValue(int currentMonth,int currentYear)
    {
        int maxValue=0;
         if(currentMonth==0)
         {
             maxValue=31;
         }
        else if(currentMonth==1)
         {
             if(isLeapYear(currentYear))
             {
                 maxValue=29;
             }
             else {
                 maxValue=28;
             }
         }
        else if(currentMonth==2)
         {
             maxValue=31;
         }
         else if(currentMonth==3)
         {
             maxValue=30;
         }
         else if(currentMonth==4)
         {
             maxValue=31;
         }
         else if(currentMonth==5)
         {
             maxValue=30;
         }
         else if(currentMonth==6)
         {
             maxValue=31;
         }
         else if(currentMonth==7)
         {
             maxValue=31;
         }
         else if(currentMonth==8)
         {
             maxValue=30;
         }
         else if(currentMonth==9)
         {
             maxValue=31;
         }
         else if(currentMonth==10)
         {
             maxValue=30;
         }
         else if(currentMonth==11)
         {
             maxValue=31;
         }
        return maxValue;
    }
    public static boolean isLeapYear(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mDayPicker.setEnabled(enabled);
        mMonthPicker.setEnabled(enabled);
        // mAmPmButton.setEnabled(enabled);
    }

    private void setDividerColor(NumberPicker picker) {
        Field[] numberPickerFields = NumberPicker.class.getDeclaredFields();
        for (Field field : numberPickerFields) {
            if (field.getName().equals("mSelectionDivider")) {
                field.setAccessible(true);
                try {
                    field.set(picker, getResources().getDrawable(R.drawable.number_picker_divider));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private static class SavedState extends BaseSavedState {

        private final int mHour;
        private final int mMinute;

        private SavedState(Parcelable superState, int hour, int minute) {
            super(superState);
            mHour = hour;
            mMinute = minute;
        }

        private SavedState(Parcel in) {
            super(in);
            mHour = in.readInt();
            mMinute = in.readInt();
        }

        public int getHour() {
            return mHour;
        }

        public int getMinute() {
            return mMinute;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mHour);
            dest.writeInt(mMinute);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mCurrentMonth, mCurrentDay);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentHour(ss.getHour());
        setCurrentMinute(ss.getMinute());
    }

    /**
     * Set the callback that indicates the time has been adjusted by the user.
     * @param onTimeChangedListener the callback, should not be null.
     */
    public void setOnDateChangedListener(OnDateChangedListener onTimeChangedListener) {
        mOnDateChangedListener = onTimeChangedListener;
    }

    /**
     * @return The current hour (0-23).
     */
    public Integer getCurrentHour() {
        return mCurrentMonth;
    }

    /**
     * Set the current hour.
     */
    public void setCurrentHour(Integer currentHour) {
        this.mCurrentMonth = currentHour;
        updateHourDisplay();
    }

    /**
     * Set whether in 24 hour or AM/PM mode.
     * @param is24HourView True = 24 hour mode. False = AM/PM.
     */
    public void setIs24HourView(Boolean is24HourView) {
        if (mIs24HourView != is24HourView) {
            mIs24HourView = is24HourView;
            configurePickerRanges();
            updateHourDisplay();
        }
    }

    /**
     * @return true if this is in 24 hour view else false.
     */
    public boolean is24HourView() {
        return mIs24HourView;
    }

    /**
     * @return The current minute.
     */
    public Integer getCurrentMinute() {
        return mCurrentDay;
    }

    /**
     * Set the current minute (0-59).
     */
    public void setCurrentMinute(Integer currentMinute) {
        this.mCurrentDay = currentMinute;
        updateMinuteDisplay();
    }

    /**
     * @return The current minute.
     */
    public Integer getCurrentSeconds() {
        return mCurrentYear;
    }

    /**
     * Set the current second (0-59).
     */
    public void setCurrentSecond(Integer currentSecond) {
        this.mCurrentYear = currentSecond;
        updateSecondsDisplay();
    }

    @Override
    public int getBaseline() {
        return mMonthPicker.getBaseline();
    }

    /**
     * Set the state of the spinners appropriate to the current hour.
     */
    private void updateHourDisplay() {
        int currentHour = mCurrentMonth;
        if (!mIs24HourView) {
            // convert [0,23] ordinal to wall clock display
            if (currentHour > 12) currentHour -= 12;
            else if (currentHour == 0) currentHour = 12;
        }
        mMonthPicker.setValue(currentHour);
        mIsAm = mCurrentMonth < 12;
        //  mAmPmButton.setText(mIsAm ? mAmText : mPmText);
        onTimeChanged();
    }

    private void configurePickerRanges() {

            mMonthPicker.setMinValue(0);
            mMonthPicker.setMaxValue(11);
        mMonthPicker.setDisplayedValues( new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"} );
            mMonthPicker.setFormatter(null);
    }

    private void onTimeChanged() {
        mOnDateChangedListener.onDateChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
    }

    /**
     * Set the state of the spinners appropriate to the current minute.
     */
    private void updateMinuteDisplay() {
        mDayPicker.setValue(mCurrentDay);
        mOnDateChangedListener.onDateChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
    }

    /**
     * Set the state of the spinners appropriate to the current second.
     */
    private void updateSecondsDisplay() {
        mYearPicker.setValue(mCurrentYear);
        mOnDateChangedListener.onDateChanged(this, getCurrentHour(), getCurrentMinute(), getCurrentSeconds());
    }
}

