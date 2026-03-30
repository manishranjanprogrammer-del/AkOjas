package com.ojassoft.astrosage.ui.customcontrols;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * This is custom time input control
 *
 * @author Bijendra
 * @updated date 8-nov-2012
 */
public class MyNewCustomTimePicker extends Activity {

    /**
     * Local variables
     */
    EditText _etHour, _etMinute, _etSecond;
    int _hour = 0, _minute = 0, _second = 0;
    TextView _tvTimePickerHeading;
    Button _butHP, _butHN, _butMP, _butMN, _butSP, _butSN;
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    private Handler repeatHourUpdateHandler = new Handler();
    private Handler repeatMinuteUpdateHandler = new Handler();
    private Handler repeatSecondUpdateHandler = new Handler();
    private final static int REP_DELAY = 50;
    Button butTimeOK, butTimeCancel;
    //int SELECTED_MODULE;
    int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //SELECTED_MODULE = getIntent().getIntExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
        if (getApplication() != null) {
            LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.laycustomtimepicker);
        butTimeOK = (Button) findViewById(R.id.butTimeOK);
        butTimeCancel = (Button) findViewById(R.id.butTimeCancel);

        _butHP = (Button) findViewById(R.id.butHP);
        _butHN = (Button) findViewById(R.id.butHN);
        _butMP = (Button) findViewById(R.id.butMP);
        _butMN = (Button) findViewById(R.id.butMN);
        _butSP = (Button) findViewById(R.id.butSP);
        _butSN = (Button) findViewById(R.id.butSN);


//		ViewGroup gv = (ViewGroup) getWindow().getDecorView();
//		CUtils.changeAllViewsFonts(gv);

        _etHour = (EditText) findViewById(R.id.etHour);
        _etMinute = (EditText) findViewById(R.id.etMinute);
        _etSecond = (EditText) findViewById(R.id.etSecond);
        _tvTimePickerHeading = (TextView) findViewById(R.id.tvTimePickerHeading);

        initValues();
        setButtonListener();

    }

    private void reInitializeHourEditTextVariable() {
        _etHour = (EditText) findViewById(R.id.etHour);
        _etHour.setFilters(new InputFilter[]{new MyCustomInputFilterMinMax(0, 23)});
    }

    private void reInitializeMinuteEditTextVariable() {
        _etMinute = (EditText) findViewById(R.id.etMinute);
        _etMinute.setFilters(new InputFilter[]{new MyCustomInputFilterMinMax(
                0, 59)});
    }

    private void reInitializeSecondEditTextVariable() {
        _etSecond = (EditText) findViewById(R.id.etSecond);
        _etSecond.setFilters(new InputFilter[]{new MyCustomInputFilterMinMax(
                0, 59)});
    }

    /**
     * This function is used to set Button listener
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void setButtonListener() {
        setHourButtonListener();
        setMinuteButtonListener();
        setSecondButtonListener();
    }

    /**
     * This function set hour button listener
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void setHourButtonListener() {
        // HOUR INCREMENT
        _butHP.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                //added by Bijendra(21-oct-13)
                if (_etHour == null)
                    reInitializeHourEditTextVariable();//end

                try {
                    _hour = Integer.parseInt(_etHour.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _hour = 0;
                }
                incrementHour();
            }
        });
        _butHP.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                //added by Bijendra(21-oct-13)
                if (_etHour == null)
                    reInitializeHourEditTextVariable();//end

                try {
                    _hour = Integer.parseInt(_etHour.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _hour = 0;
                }
                mAutoIncrement = true;
                repeatHourUpdateHandler.post(new HourUpdater());
                return false;
            }
        });
        _butHP.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement) {

                    mAutoIncrement = false;
                }
                return false;
            }
        });
        // HOUR DECREMENT

        _butHN.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                //added by Bijendra(21-oct-13)
                if (_etHour == null)
                    reInitializeHourEditTextVariable();//end

                try {
                    _hour = Integer.parseInt(_etHour.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _hour = 0;
                }
                decrementHour();
            }
        });
        _butHN.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {

                //added by Bijendra(21-oct-13)
                if (_etHour == null)
                    reInitializeHourEditTextVariable();//end

                try {
                    _hour = Integer.parseInt(_etHour.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _hour = 0;
                }
                mAutoDecrement = true;
                repeatHourUpdateHandler.post(new HourUpdater());
                return false;
            }
        });
        _butHN.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement) {
                    mAutoDecrement = false;
                }
                return false;
            }
        });
    }

    /**
     * This function set minute button listener
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void setMinuteButtonListener() {
        // MINUTE INCREMENT
        _butMP.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //added by Bijendra(21-oct-13)
                if (_etMinute == null)
                    reInitializeMinuteEditTextVariable();//end

                try {
                    _minute = Integer.parseInt(_etMinute.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _minute = 0;
                }
                incrementMinute();
            }
        });
        _butMP.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                //added by Bijendra(21-oct-13)
                if (_etMinute == null)
                    reInitializeMinuteEditTextVariable();//end

                try {
                    _minute = Integer.parseInt(_etMinute.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _minute = 0;
                }
                mAutoIncrement = true;
                repeatMinuteUpdateHandler.post(new MinuteUpdater());
                return false;
            }
        });
        _butMP.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement) {

                    mAutoIncrement = false;
                }
                return false;
            }
        });
        // MINUTE DECREMENT

        _butMN.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                //added by Bijendra(21-oct-13)
                if (_etMinute == null)
                    reInitializeMinuteEditTextVariable();//end

                try {
                    _minute = Integer.parseInt(_etMinute.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _minute = 0;
                }
                decrementMinute();
            }
        });
        _butMN.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                //added by Bijendra(21-oct-13)
                if (_etMinute == null)
                    reInitializeMinuteEditTextVariable();//end

                try {
                    _minute = Integer.parseInt(_etMinute.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _minute = 0;
                }
                mAutoDecrement = true;
                repeatMinuteUpdateHandler.post(new MinuteUpdater());
                return false;
            }
        });
        _butMN.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement) {

                    mAutoDecrement = false;
                }
                return false;
            }
        });
    }

    /**
     * This function set second button listener
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void setSecondButtonListener() {
        // SECOND INCREMENT
        _butSP.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //added by Bijendra(21-oct-13)
                if (_etSecond == null)
                    reInitializeSecondEditTextVariable();//end

                try {
                    _second = Integer.parseInt(_etSecond.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _second = 0;
                }
                incrementSecond();
            }
        });
        _butSP.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                //added by Bijendra(21-oct-13)
                if (_etSecond == null)
                    reInitializeSecondEditTextVariable();//end

                try {
                    _second = Integer.parseInt(_etSecond.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _second = 0;
                }
                mAutoIncrement = true;
                repeatSecondUpdateHandler.post(new SecondUpdater());
                return false;
            }
        });
        _butSP.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement) {
                    mAutoIncrement = false;
                }
                return false;
            }
        });
        // SECOND DECREMENT

        _butSN.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //added by Bijendra(21-oct-13)
                if (_etSecond == null)
                    reInitializeSecondEditTextVariable();//end

                try {
                    _second = Integer.parseInt(_etSecond.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _second = 0;
                }
                decrementSecond();
            }
        });
        _butSN.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                //added by Bijendra(21-oct-13)
                if (_etSecond == null)
                    reInitializeSecondEditTextVariable();//end

                try {
                    _second = Integer.parseInt(_etSecond.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    _second = 0;
                }
                mAutoDecrement = true;
                repeatSecondUpdateHandler.post(new SecondUpdater());
                return false;
            }
        });
        _butSN.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement) {

                    mAutoDecrement = false;
                }
                return false;
            }
        });
    }

    /**
     * This function is used to set initial values and other misc
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void initValues() {
        Typeface typeface;
        //LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        typeface = CUtils.getRobotoFont(this, LANGUAGE_CODE, CGlobalVariables.regular);

        butTimeCancel.setTypeface(typeface);
        butTimeOK.setTypeface(typeface);
        if (getIntent() != null && getIntent().getExtras() != null) {
            _hour = getIntent().getExtras().getInt("H");
            _minute = getIntent().getExtras().getInt("M");
            _second = getIntent().getExtras().getInt("S");
        }

        _etHour.setText(String.valueOf(_hour));
        _etMinute.setText(String.valueOf(_minute));
        _etSecond.setText(String.valueOf(_second));

	/*_tvTimePickerHeading.setTypeface(CGlobalVariables.tfFoEnglish);
    _etHour.setTypeface(CGlobalVariables.tfFoEnglish);
	_etMinute.setTypeface(CGlobalVariables.tfFoEnglish);
	_etSecond.setTypeface(CGlobalVariables.tfFoEnglish);

	// BUTTONS
	_butHP.setTypeface(CGlobalVariables.tfFoEnglish);
	_butHN.setTypeface(CGlobalVariables.tfFoEnglish);
	_butMP.setTypeface(CGlobalVariables.tfFoEnglish);
	_butMN.setTypeface(CGlobalVariables.tfFoEnglish);
	_butSP.setTypeface(CGlobalVariables.tfFoEnglish);
	_butSN.setTypeface(CGlobalVariables.tfFoEnglish);*/

        _etHour.setFilters(new InputFilter[]{new MyCustomInputFilterMinMax(0,
                23)});
        _etMinute.setFilters(new InputFilter[]{new MyCustomInputFilterMinMax(
                0, 59)});
        _etSecond.setFilters(new InputFilter[]{new MyCustomInputFilterMinMax(
                0, 59)});

        setTimeHeading();

    }

    /**
     * This function is sued to set Time heading on time input control
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void setTimeHeading() {

        String strTitle = CUtils.pad(_hour) + ":" + CUtils.pad(_minute) + ":"
                + CUtils.pad(_second);
        _tvTimePickerHeading.setText(strTitle);
    }

    /**
     * This function validate the input values
     *
     * @return boolean
     * @author Bijendra
     * @date 8nov-2012
     */
    private boolean validateForm() {
        boolean _success = true;
        try {
            _hour = Integer.parseInt(_etHour.getText().toString());
        } catch (Exception e) {

            _etHour.setError("");
            _success = false;
        }
        try {
            _minute = Integer.parseInt(_etMinute.getText().toString());
        } catch (Exception e) {

            _etMinute.setError("");
            _success = false;
        }

        try {
            _second = Integer.parseInt(_etSecond.getText().toString());
        } catch (Exception e) {

            _etSecond.setError("");
            _success = false;
        }
        return _success;

    }

    /**
     * This is  a OK (Set) button listener
     *
     * @param v
     * @author Bijendra
     * @date 8nov-2012
     */
    public void goToOk(View v)// goToExit
    {
        if (validateForm()) {
            Intent intent = new Intent();
            Bundle b = new Bundle();
            b.putInt("H", _hour);
            b.putInt("M", _minute);
            b.putInt("S", _second);

            intent.putExtras(b);
            setResult(RESULT_OK, intent);

            this.finish();
        }

    }

    /**
     * This is  a cancel button  listener
     *
     * @param v
     * @author Bijendra
     * @date 8nov-2012
     */
    public void goToExit(View v)//
    {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    /**
     * This function is used to increment hour values
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void incrementHour() {

        _hour++;
        if (_hour > 23)
            _hour = 0;

        _etHour.setText(String.valueOf(_hour));

        setTimeHeading();
    }

    /**
     * This function is used to decrease hour values
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void decrementHour() {

        _hour--;
        if (_hour < 0)
            _hour = 23;

        _etHour.setText(String.valueOf(_hour));

        setTimeHeading();
    }

    /**
     * This function is used to increment minute values
     *
     * @author Bijendra
     * @date 8nov-2012
     */

    private void incrementMinute() {

        _minute++;
        if (_minute > 59)
            _minute = 0;

        _etMinute.setText(String.valueOf(_minute));

        setTimeHeading();

    }

    /**
     * This function is used to decrease minute values
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void decrementMinute() {

        _minute--;
        if (_minute < 0)
            _minute = 59;

        _etMinute.setText(String.valueOf(_minute));

        setTimeHeading();

    }

    /**
     * This function is used to increase second values
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void incrementSecond() {

        _second++;
        if (_second > 59)
            _second = 0;

        _etSecond.setText(String.valueOf(_second));

        setTimeHeading();

    }

    /**
     * This function is used to decrease second values
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    private void decrementSecond() {

        _second--;
        if (_second < 0)
            _second = 59;

        _etSecond.setText(String.valueOf(_second));

        setTimeHeading();

    }

    /**
     * This is a Thread class for Hour
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    class HourUpdater implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                incrementHour();
                repeatHourUpdateHandler.postDelayed(new HourUpdater(),
                        REP_DELAY);
            } else if (mAutoDecrement) {
                decrementHour();
                repeatHourUpdateHandler.postDelayed(new HourUpdater(),
                        REP_DELAY);
            }
        }
    }

    /**
     * This is a Thread class for minute
     *
     * @author Bijendra
     * @date 8nov-2012
     */

    class MinuteUpdater implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                incrementMinute();
                repeatMinuteUpdateHandler.postDelayed(new MinuteUpdater(),
                        REP_DELAY);
            } else if (mAutoDecrement) {
                decrementMinute();
                repeatMinuteUpdateHandler.postDelayed(new MinuteUpdater(),
                        REP_DELAY);
            }
        }
    }


    /**
     * This is a Thread class for Second
     *
     * @author Bijendra
     * @date 8nov-2012
     */
    class SecondUpdater implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                incrementSecond();
                repeatSecondUpdateHandler.postDelayed(new SecondUpdater(),
                        REP_DELAY);
            } else if (mAutoDecrement) {
                decrementSecond();
                repeatSecondUpdateHandler.postDelayed(new SecondUpdater(),
                        REP_DELAY);
            }
        }
    }

    public class MyCustomInputFilterMinMax implements InputFilter {

        private int min, max;

        public MyCustomInputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public MyCustomInputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }


        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }


    }
}
