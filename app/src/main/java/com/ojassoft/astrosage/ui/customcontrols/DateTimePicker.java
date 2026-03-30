package com.ojassoft.astrosage.ui.customcontrols;

import java.util.Calendar;

import com.ojassoft.astrosage.R;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class DateTimePicker extends RelativeLayout {
    // DatePicker reference
    private int startYear = 1600;
    private int endYear = 2400;

    private View myPickerView;

    private Button month_plus;
    private EditText month_display;
    private Button month_minus;

    private Button date_plus;
    private EditText date_display;
    private Button date_minus;

    private Button year_plus;
    private EditText year_display;
    private Button year_minus;

    private Button hour_plus;
    private EditText hour_display;
    private Button hour_minus;

    private Button min_plus;
    private EditText min_display;
    private Button min_minus;

    private Calendar cal;

    // for handling long press events
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;

    private final static int REP_DELAY = 50;

    private Handler repeatYearUpdateHandler = new Handler();
    private Handler repeatMonthUpdateHandler = new Handler();
    private Handler repeatdayUpdateHandler = new Handler();


    // Constructor start
    public DateTimePicker(Context context) {
        this(context, null);

        init(context);
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myPickerView = inflator.inflate(R.layout.datetimepicker, null);
        this.addView(myPickerView);

        initializeReference();

    }

    private void init(Context mContext) {
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myPickerView = inflator.inflate(R.layout.datetimepicker, null);
        this.addView(myPickerView);

        initializeReference();
    }

    private void initializeReference() {

        month_plus = (Button) myPickerView.findViewById(R.id.month_plus);
        month_plus.setOnClickListener(month_plus_listener);
        month_plus.setOnLongClickListener(month_long_plus_listener);
        month_plus.setOnTouchListener(month_touch_plus_listener);

        month_display = (EditText) myPickerView.findViewById(R.id.month_display);
        month_minus = (Button) myPickerView.findViewById(R.id.month_minus);
        month_minus.setOnClickListener(month_minus_listener);
        month_minus.setOnLongClickListener(month_long_minus_listener);
        month_minus.setOnTouchListener(month_touch_minus_listener);

        date_plus = (Button) myPickerView.findViewById(R.id.date_plus);
        date_plus.setOnClickListener(date_plus_listener);
        date_plus.setOnLongClickListener(day_long_plus_listener);
        date_plus.setOnTouchListener(day_touch_plus_listener);

        date_display = (EditText) myPickerView.findViewById(R.id.date_display);
        date_display.addTextChangedListener(date_watcher);
        date_minus = (Button) myPickerView.findViewById(R.id.date_minus);
        date_minus.setOnClickListener(date_minus_listener);
        date_minus.setOnLongClickListener(day_long_minus_listener);
        date_minus.setOnTouchListener(day_touch_minus_listener);

        year_plus = (Button) myPickerView.findViewById(R.id.year_plus);
        year_plus.setOnClickListener(year_plus_listener);
        year_plus.setOnLongClickListener(year_long_plus_listener);
        year_plus.setOnTouchListener(year_touch_plus_listener);

        year_display = (EditText) myPickerView.findViewById(R.id.year_display);
        year_display.setOnFocusChangeListener(mLostFocusYear);
        year_display.addTextChangedListener(year_watcher);
        year_minus = (Button) myPickerView.findViewById(R.id.year_minus);
        year_minus.setOnClickListener(year_minus_listener);
        year_minus.setOnLongClickListener(year_long_minus_listener);
        year_minus.setOnTouchListener(year_touch_minus_listener);


        hour_plus = (Button) myPickerView.findViewById(R.id.hour_plus);
        hour_plus.setOnClickListener(hour_plus_listener);
        hour_display = (EditText) myPickerView.findViewById(R.id.hour_display);
        hour_display.addTextChangedListener(hour_watcher);
        hour_minus = (Button) myPickerView.findViewById(R.id.hour_minus);
        hour_minus.setOnClickListener(hour_minus_listener);

        min_plus = (Button) myPickerView.findViewById(R.id.min_plus);
        min_plus.setOnClickListener(min_plus_listener);
        min_display = (EditText) myPickerView.findViewById(R.id.min_display);
        min_display.addTextChangedListener(min_watcher);
        min_minus = (Button) myPickerView.findViewById(R.id.min_minus);
        min_minus.setOnClickListener(min_minus_listener);

        initData();
        initFilterNumericDigit();

    }

    public void initData() {
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);

        month_display.setText(months[cal.get(Calendar.MONTH)]);
        date_display.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
        hour_display.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        min_display.setText(String.valueOf(cal.get(Calendar.MINUTE)));
    }

    private void initFilterNumericDigit() {

        try {
            date_display.setFilters(new InputFilter[]{new InputFilterMinMax(1, cal.getActualMaximum(Calendar.DAY_OF_MONTH))});
            InputFilter[] filterArray_year = new InputFilter[1];
            filterArray_year[0] = new InputFilter.LengthFilter(4);
            year_display.setFilters(filterArray_year);
            hour_display.setFilters(new InputFilter[]{new InputFilterMinMax(0, 23)});
            min_display.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeFilter() {
        try {
            date_display.setFilters(new InputFilter[]{new InputFilterMinMax(1, cal.getActualMaximum(Calendar.DAY_OF_MONTH))});
        } catch (Exception e) {
            date_display.setText("" + cal.get(Calendar.DAY_OF_MONTH));
            e.printStackTrace();
        }
    }

    public void setTimeChangedListener(TimeWatcher listener) {
        this.mTimeWatcher = listener;
    }

    public void removeTimeChangedListener() {
        this.mTimeWatcher = null;
    }

    View.OnClickListener hour_plus_listener = new View.OnClickListener() {

        public void onClick(View v) {
            hour_display.requestFocus();

            try {
                cal.add(Calendar.HOUR_OF_DAY, 1);
                sendToDisplay();
            } catch (Exception e) {
                //Log.e("", e.toString());

            }
        }
    };
    View.OnClickListener hour_minus_listener = new View.OnClickListener() {


        public void onClick(View v) {
            hour_display.requestFocus();

            try {
                cal.add(Calendar.HOUR_OF_DAY, -1);
                sendToDisplay();
            } catch (Exception e) {
                //Log.e("", e.toString());
            }
        }
    };

    View.OnClickListener min_plus_listener = new View.OnClickListener() {


        public void onClick(View v) {
            min_display.requestFocus();

            try {
                cal.add(Calendar.MINUTE, 1);
                sendToDisplay();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    View.OnClickListener min_minus_listener = new View.OnClickListener() {


        public void onClick(View v) {
            min_display.requestFocus();

            try {
                cal.add(Calendar.MINUTE, -1);
                sendToDisplay();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};

    View.OnClickListener month_plus_listener = new View.OnClickListener() {
        public void onClick(View v) {
            month_plus();
        }
    };

    View.OnLongClickListener month_long_plus_listener = new View.OnLongClickListener() {
        public boolean onLongClick(View arg0) {
//				_hour = Integer.parseInt(_etHour.getText().toString());
            mAutoIncrement = true;
            repeatMonthUpdateHandler.post(new MonthUpdater());
            return false;
        }
    };

    View.OnTouchListener month_touch_plus_listener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            if ((event.getAction() == MotionEvent.ACTION_UP || event
                    .getAction() == MotionEvent.ACTION_CANCEL)
                    && mAutoIncrement) {

                mAutoIncrement = false;
            }
            return false;
        }
    };

    View.OnLongClickListener month_long_minus_listener = new View.OnLongClickListener() {
        public boolean onLongClick(View arg0) {
//				_hour = Integer.parseInt(_etHour.getText().toString());
            mAutoDecrement = true;
            repeatMonthUpdateHandler.post(new MonthUpdater());
            return false;
        }
    };

    View.OnTouchListener month_touch_minus_listener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            if ((event.getAction() == MotionEvent.ACTION_UP || event
                    .getAction() == MotionEvent.ACTION_CANCEL)
                    && mAutoDecrement) {

                mAutoDecrement = false;
            }
            return false;
        }
    };


    class MonthUpdater implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                month_plus();
                repeatMonthUpdateHandler.postDelayed(new MonthUpdater(),
                        REP_DELAY);
            } else if (mAutoDecrement) {
                month_minus();
                repeatMonthUpdateHandler.postDelayed(new MonthUpdater(),
                        REP_DELAY);
            }
        }
    }

    View.OnClickListener month_minus_listener = new View.OnClickListener() {
        public void onClick(View v) {
            month_minus();
        }
    };
    View.OnClickListener date_plus_listener = new View.OnClickListener() {
        public void onClick(View v) {
            date_plus();
        }
    };

    View.OnLongClickListener day_long_plus_listener = new View.OnLongClickListener() {
        public boolean onLongClick(View arg0) {
//				_hour = Integer.parseInt(_etHour.getText().toString());
            mAutoIncrement = true;
            repeatdayUpdateHandler.post(new DayUpdater());
            return false;
        }
    };

    View.OnTouchListener day_touch_plus_listener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            if ((event.getAction() == MotionEvent.ACTION_UP || event
                    .getAction() == MotionEvent.ACTION_CANCEL)
                    && mAutoIncrement) {
                mAutoIncrement = false;
            }
            return false;
        }
    };

    View.OnLongClickListener day_long_minus_listener = new View.OnLongClickListener() {
        public boolean onLongClick(View arg0) {
//				_hour = Integer.parseInt(_etHour.getText().toString());
            mAutoDecrement = true;
            repeatdayUpdateHandler.post(new DayUpdater());
            return false;
        }
    };

    View.OnTouchListener day_touch_minus_listener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            if ((event.getAction() == MotionEvent.ACTION_UP || event
                    .getAction() == MotionEvent.ACTION_CANCEL)
                    && mAutoDecrement) {

                mAutoDecrement = false;
            }
            return false;
        }
    };


    class DayUpdater implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                date_plus();
                repeatdayUpdateHandler.postDelayed(new DayUpdater(),
                        REP_DELAY);
            } else if (mAutoDecrement) {
                date_minus();
                repeatdayUpdateHandler.postDelayed(new DayUpdater(),
                        REP_DELAY);
            }
        }
    }

    View.OnClickListener date_minus_listener = new View.OnClickListener() {
        public void onClick(View v) {
            date_minus();
        }
    };
    View.OnClickListener year_plus_listener = new View.OnClickListener() {


        public void onClick(View v) {

            year_plus();

        }
    };


    View.OnLongClickListener year_long_plus_listener = new View.OnLongClickListener() {
        public boolean onLongClick(View arg0) {
//				_hour = Integer.parseInt(_etHour.getText().toString());
            mAutoIncrement = true;
            repeatYearUpdateHandler.post(new YearUpdater());
            return false;
        }
    };

    View.OnTouchListener year_touch_plus_listener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            if ((event.getAction() == MotionEvent.ACTION_UP || event
                    .getAction() == MotionEvent.ACTION_CANCEL)
                    && mAutoIncrement) {
                mAutoIncrement = false;
            }
            return false;
        }
    };

    View.OnLongClickListener year_long_minus_listener = new View.OnLongClickListener() {
        public boolean onLongClick(View arg0) {
//				_hour = Integer.parseInt(_etHour.getText().toString());
            mAutoDecrement = true;
            repeatYearUpdateHandler.post(new YearUpdater());
            return false;
        }
    };

    View.OnTouchListener year_touch_minus_listener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            if ((event.getAction() == MotionEvent.ACTION_UP || event
                    .getAction() == MotionEvent.ACTION_CANCEL)
                    && mAutoDecrement) {

                mAutoDecrement = false;
            }
            return false;
        }
    };


    class YearUpdater implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                year_plus();
                repeatYearUpdateHandler.postDelayed(new YearUpdater(),
                        REP_DELAY);
            } else if (mAutoDecrement) {
                year_minus();
                repeatYearUpdateHandler.postDelayed(new YearUpdater(),
                        REP_DELAY);
            }
        }
    }


    View.OnClickListener year_minus_listener = new View.OnClickListener() {


        public void onClick(View v) {

            year_minus();

        }
    };


    class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }


        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString()
                        + source.toString());
                if (isInRange(min, max, input)) {
                    return null;
                }
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    public void reset() {
        cal = Calendar.getInstance();
        initFilterNumericDigit();
        initData();
        sendToDisplay();
    }

    protected void year_minus() {
        // TODO Auto-generated method stub
        try {
            year_display.requestFocus();

            if (cal.get(Calendar.YEAR) <= startYear) {
                cal.set(Calendar.YEAR, endYear);

            } else {
                cal.add(Calendar.YEAR, -1);

            }

            month_display.setText(months[cal.get(Calendar.MONTH)]);
            year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
            date_display.setText(String.valueOf(cal
                    .get(Calendar.DAY_OF_MONTH)));

            changeFilter();
            sendToListener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void year_plus() {
        // TODO Auto-generated method stub
        try {
            year_display.requestFocus();

            if (cal.get(Calendar.YEAR) >= endYear) {

                cal.set(Calendar.YEAR, startYear);

            } else {
                cal.add(Calendar.YEAR, +1);

            }

            month_display.setText(months[cal.get(Calendar.MONTH)]);
            year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
            date_display.setText(String.valueOf(cal
                    .get(Calendar.DAY_OF_MONTH)));

            changeFilter();
            sendToListener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void date_minus() {
        try {
            date_display.requestFocus();
            cal.add(Calendar.DAY_OF_MONTH, -1);

            month_display.setText(months[cal.get(Calendar.MONTH)]);
            year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
            date_display.setText(String.valueOf(cal
                    .get(Calendar.DAY_OF_MONTH)));

            sendToListener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void date_plus() {
        try {
            date_display.requestFocus();
            cal.add(Calendar.DAY_OF_MONTH, 1);

            month_display.setText(months[cal.get(Calendar.MONTH)]);
            year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
            date_display.setText(String.valueOf(cal
                    .get(Calendar.DAY_OF_MONTH)));

            sendToListener();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void month_minus() {
        try {
            cal.add(Calendar.MONTH, -1);

            month_display.setText(months[cal.get(Calendar.MONTH)]);
            year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
            date_display.setText(String.valueOf(cal
                    .get(Calendar.DAY_OF_MONTH)));

            changeFilter();
            sendToListener();
        } catch (Exception e) {
            //Log.e("", e.toString());
        }
    }

    protected void month_plus() {
        try {
            cal.add(Calendar.MONTH, 1);

            month_display.setText(months[cal.get(Calendar.MONTH)]);
            year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
            date_display.setText(String.valueOf(cal
                    .get(Calendar.DAY_OF_MONTH)));

            changeFilter();
            sendToListener();
        } catch (Exception e) {
            //Log.e("", e.toString());
        }
    }

    synchronized private void sendToListener() {

        if (mTimeWatcher != null) {
            mTimeWatcher.onTimeChanged(cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE), -1);
        }
        if (mDateWatcher != null) {
            mDateWatcher.onDateChanged(cal);
        }
    }

    private void sendToDisplay() {

        hour_display.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        min_display.setText(String.valueOf(cal.get(Calendar.MINUTE)));
    }

    TimeWatcher mTimeWatcher = null;

    public interface TimeWatcher {
        void onTimeChanged(int h, int m, int am_pm);
    }

    TextWatcher hour_watcher = new TextWatcher() {


        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }


        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }


        public void afterTextChanged(Editable s) {
            try {
                if (s.toString().length() > 0) {
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s.toString()));
                    sendToListener();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    TextWatcher min_watcher = new TextWatcher() {


        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }


        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }


        public void afterTextChanged(Editable s) {
            try {
                if (s.toString().length() > 0) {
                    cal.set(Calendar.MINUTE, Integer.parseInt(s.toString()));
                    sendToListener();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public int getYear() {
        return Integer.parseInt(year_display.getText().toString());
    }

    public int getDay() {
        return Integer.parseInt(date_display.getText().toString());
    }

    public String getMonth() {
        return month_display.getText().toString();
    }

    public int getHour() {
        return Integer.parseInt(hour_display.getText().toString());
    }

    public int getMinute() {
        return Integer.parseInt(min_display.getText().toString());
    }

    public void setDateChangedListener(DateWatcher listener) {
        this.mDateWatcher = listener;
    }

    public void removeDateChangedListener() {
        this.mDateWatcher = null;
    }


    View.OnFocusChangeListener mLostFocusYear = new OnFocusChangeListener() {


        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {

                year_display.setText(String.valueOf(cal.get(Calendar.YEAR)));
            }
        }
    };


    TextWatcher date_watcher = new TextWatcher() {


        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }


        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }


        public void afterTextChanged(Editable s) {

            try {
                if (s.toString().length() > 0) {
                    // //Log.e("", "afterTextChanged : " + s.toString());
                    cal.set(Calendar.DAY_OF_MONTH,
                            Integer.parseInt(s.toString()));

                    month_display.setText(months[cal.get(Calendar.MONTH)]);

                    sendToListener();
                }
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    TextWatcher year_watcher = new TextWatcher() {


        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }


        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }


        public void afterTextChanged(Editable s) {
            try {
                if (s.toString().length() == 4) {
                    int year = Integer.parseInt(s.toString());

                    if (year > endYear) {
                        cal.set(Calendar.YEAR, endYear);
                    } else if (year < startYear) {
                        cal.set(Calendar.YEAR, startYear);
                    } else {
                        cal.set(Calendar.YEAR, year);
                    }
                }

                sendToListener();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    DateWatcher mDateWatcher = null;
    //Added by hukum to init date
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int hour = 0;
    private int min = 0;
    private int sec = 0;

    //end
    public interface DateWatcher {
        void onDateChanged(Calendar c);
    }

    //Added by hukum to init date
    public void initDateElements(int year, int month, int day, int hour, int min, int sec) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }
    //end


}
