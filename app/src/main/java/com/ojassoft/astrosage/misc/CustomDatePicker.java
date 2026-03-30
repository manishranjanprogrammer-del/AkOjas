package com.ojassoft.astrosage.misc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.Calendar;
import java.util.Date;

public class CustomDatePicker extends Dialog {
    private NumberPicker dayPicker;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private final DatePickerDialog.OnDateSetListener listener;
    int year,month,day;

    private TextView tvSelected;
    java.text.DateFormat mDateFormat;

    public CustomDatePicker(Context context, DatePickerDialog.OnDateSetListener listener, int year, int month, int day) {
        super(context);
        this.listener = listener;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_date_picker, null);
        setContentView(R.layout.layout_custom_date_picker);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            ColorDrawable bgColor = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.bg_card_view_color));
            InsetDrawable inset = new InsetDrawable(bgColor, 50);
            window.setBackgroundDrawable(inset);
        }


        dayPicker = findViewById(R.id.picker_day);
        monthPicker =findViewById(R.id.picker_month);
        yearPicker = findViewById(R.id.picker_year);
        tvSelected = findViewById(R.id.tv_selected_date);
        Button btnSet = findViewById(R.id.button_set);
        Button btnCancel = findViewById(R.id.button_cancel);
        FontUtils.changeFont(getContext(),btnCancel, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getContext(),btnSet, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getContext(),tvSelected, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        String[] months = getContext().getResources().getStringArray(R.array.month_short_name_list);
        monthPicker.setDisplayedValues(months);
        dayPicker.setFormatter(value -> String.format("%02d", value));
        mDateFormat = DateFormat.getLongDateFormat(getContext());
        setupPickers();
        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(yearPicker.getValue(),newVal,1);
            calendar.set(Calendar.MONTH,newVal);
            //Log.d("Datepicker","Month = "+newVal+" DAYs in month = "+calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            int maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if(maxDayInMonth < dayPicker.getValue()){
                dayPicker.setValue(maxDayInMonth);
            }
            dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            updateDate();
        });

        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(newVal,monthPicker.getValue(),1);
            int maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            //Log.d("Datepicker","Month = "+newVal+" DAYs in month = "+calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            if(maxDayInMonth < dayPicker.getValue()){
                dayPicker.setValue(maxDayInMonth);
            }
            dayPicker.setMaxValue(maxDayInMonth);
            updateDate();
        });
        dayPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            updateDate();
        });

        btnCancel.setOnClickListener((v)->{
            dismiss();
        });
        btnSet.setOnClickListener(v -> {
            yearPicker.clearFocus();
            monthPicker.clearFocus();
            dayPicker.clearFocus();
            if(listener != null){
                listener.onDateSet(null,yearPicker.getValue(),monthPicker.getValue(),dayPicker.getValue());
            }
            dismiss();
        });
        TextView tvDay = findViewById(R.id.tv_day);
        FontUtils.changeFont(getContext(),tvDay, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        TextView tvMonth = findViewById(R.id.tv_month);
        FontUtils.changeFont(getContext(),tvMonth, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        TextView tvYear = findViewById(R.id.tv_year);
        FontUtils.changeFont(getContext(),tvYear, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

    }

    private void setupPickers() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);

        // Day Picker
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));

        // Month Picker
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setValue(calendar.get(Calendar.MONTH));

        // Year Picker
        yearPicker.setMinValue(currentYear - 200);
        yearPicker.setMaxValue(currentYear + 100);
        yearPicker.setValue(year);
        updateDate();
    }
    private void updateDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearPicker.getValue(),monthPicker.getValue(),dayPicker.getValue());
        tvSelected.setText(mDateFormat.format(calendar.getTime()));
    }

    public void updateDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        yearPicker.setValue(year);
        monthPicker.setValue(calendar.get(Calendar.MONTH));
        dayPicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));
    }
}
