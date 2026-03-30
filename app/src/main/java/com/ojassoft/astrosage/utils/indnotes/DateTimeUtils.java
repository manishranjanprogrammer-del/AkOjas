package com.ojassoft.astrosage.utils.indnotes;

import android.content.Context;
import android.util.Log;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class DateTimeUtils {

    public static String GenerateRandomDate(int minAge, int maxAge) {

        Calendar calendarMinDate = Calendar.getInstance();

        calendarMinDate.add(Calendar.YEAR, -maxAge);


        Calendar calendarMaxDate = Calendar.getInstance();

        calendarMaxDate.add(Calendar.YEAR, -minAge);

        int year = randomNo(calendarMinDate.get(Calendar.YEAR) + 1, calendarMaxDate.get(Calendar.YEAR) - 1);

        int month = randomNo(1, 12);

        int day = randomNo(10, 28);

        Log.e("gen birth date", day + "/" + month + "/" + year);


        return day + "/" + month + "/" + year;


    }


    private static int randomNo(int low, int high) {
        Random r = new Random();

        return r.nextInt(high - low) + low;
    }


    public static long currentTimeMills() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();

    }

    public static String currentDate() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }

    public static Calendar calendarByDate(String format, String stringDate) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    public static String dateInParticularFormat(String format, String requiredFormat, String stringDate) {
        Date date = null;
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(requiredFormat, Locale.US);
        return sdf.format(calendar.getTime());

    }


    public static int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }

    public static String getDateByTimestamp(String sTime) {
        String date = "";
        try {
            long timestamp = Long.valueOf(sTime);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            date = android.text.format.DateFormat.format("dd MMM, yyyy", cal).toString();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTimeByTimestamp(String sTime) {
        String time = "";
        try {
            long timestamp = Long.valueOf(sTime);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            time = android.text.format.DateFormat.format("hh:mm a", cal).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time.toUpperCase();
    }

    public static String getDateInFormat(String sTime) {
        String formattedDate = "";
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = originalFormat.parse(sTime);
            formattedDate = targetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String getMonthString(Context context, int currentMonth, int day) {
        StringBuilder sb = new StringBuilder();
        String title = "";
        switch (currentMonth) {
            case AppConstants.January: {
                title = context.getString(R.string.tab_month_jan);
                break;
            }
            case AppConstants.February: {
                title = context.getString(R.string.tab_month_feb);
                break;
            }
            case AppConstants.March: {
                title = context.getString(R.string.tab_month_mar);
                break;
            }
            case AppConstants.April: {
                title = context.getString(R.string.tab_month_apr);
                break;
            }
            case AppConstants.May: {
                title = context.getString(R.string.tab_month_may);
                break;
            }
            case AppConstants.June: {
                title = context.getString(R.string.tab_month_jun);
                break;
            }
            case AppConstants.July: {
                title = context.getString(R.string.tab_month_july);
                break;
            }
            case AppConstants.August: {
                title = context.getString(R.string.tab_month_aug);
                break;
            }
            case AppConstants.September: {
                title = context.getString(R.string.tab_month_sep);
                break;
            }
            case AppConstants.October: {
                title = context.getString(R.string.tab_month_oct);
                break;
            }
            case AppConstants.November: {
                title = context.getString(R.string.tab_month_nov);
                break;
            }
            case AppConstants.December: {
                title = context.getString(R.string.tab_month_dec);
                break;
            }

        }
        sb.append(title);
        if (day < 10) {
            sb.append(" 0" + day);
        } else {
            sb.append(" " + day);
        }
        return sb.toString();
    }

    public static String getMonthTitle(Context context, int month) {
        String title = "";
        switch (month) {
            case AppConstants.December18: {
                title = context.getString(R.string.tab_month_dec);
                break;
            }
            case AppConstants.January: {
                title = context.getString(R.string.tab_month_jan);
                break;
            }
            case AppConstants.February: {
                title = context.getString(R.string.tab_month_feb);
                break;
            }
            case AppConstants.March: {
                title = context.getString(R.string.tab_month_mar);
                break;
            }
            case AppConstants.April: {
                title = context.getString(R.string.tab_month_apr);
                break;
            }
            case AppConstants.May: {
                title = context.getString(R.string.tab_month_may);
                break;
            }
            case AppConstants.June: {
                title = context.getString(R.string.tab_month_jun);
                break;
            }
            case AppConstants.July: {
                title = context.getString(R.string.tab_month_july);
                break;
            }
            case AppConstants.August: {
                title = context.getString(R.string.tab_month_aug);
                break;
            }
            case AppConstants.September: {
                title = context.getString(R.string.tab_month_sep);
                break;
            }
            case AppConstants.October: {
                title = context.getString(R.string.tab_month_oct);
                break;
            }
            case AppConstants.November: {
                title = context.getString(R.string.tab_month_nov);
                break;
            }
            case AppConstants.December: {
                title = context.getString(R.string.tab_month_dec);
                break;
            }
        }
        return title;
    }
}
