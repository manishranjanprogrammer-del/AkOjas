package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.HoraMetadata;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.panchang.Masa;
import com.ojassoft.panchang.Muhurta;
import com.ojassoft.panchang.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by ojas-08 on 10/3/17.
 */
public class CalculateChogadiya {
    Context context;
    int startNight;
    int startDay;

    /*private double lat = 28.36;
    private double lng = 77.12;
    private double tzone = +5.5;*/

    public CalculateChogadiya(Context context) {
        this.context = context;
    }

    public ArrayList<HoraMetadata> HoraLordName(int year, int month, int day) {
        ArrayList<HoraMetadata> data = new ArrayList<HoraMetadata>();
        Calendar calendar = new GregorianCalendar(year, month, day);
        int reslut = calendar.get(Calendar.DAY_OF_WEEK);
        int valueforday = getStartChoghadiaForDay(reslut);
        int valuefornight = getStartChoghadiaForNight(reslut);

        String[] chogadiyanameforday = getChogadiaDayName(valueforday);
        String[] chogadiyanamefornight = getChogadiaNightName(valuefornight);
        String[] chogadiyanamefordaymeaning = getChogadiaDayNameMeaning(valueforday);
        String[] chogadiyanamefornightmeaning = getChogadiaNightNameMeaning(valuefornight);

        for (int i = 0; i < 8; i++) {

            HoraMetadata hora = new HoraMetadata();
            hora.setPlanetdata(chogadiyanameforday[i]);

            hora.setPlanetmeaning(chogadiyanamefordaymeaning[i]);
            data.add(hora);
        }
        for (int i = 0; i < 8; i++) {

            HoraMetadata hora1 = new HoraMetadata();
            hora1.setPlanetdata(chogadiyanamefornight[i]);

            hora1.setPlanetmeaning(chogadiyanamefornightmeaning[i]);
            data.add(hora1);
        }

        return data;

    }

    private int getStartChoghadiaForDay(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                startDay = 0;
                break;
            case Calendar.MONDAY:
                startDay = 3;
                break;
            case Calendar.TUESDAY:
                startDay = 6;
                break;
            case Calendar.WEDNESDAY:
                startDay = 2;
                break;
            case Calendar.THURSDAY:
                startDay = 5;
                break;
            case Calendar.FRIDAY:
                startDay = 1;
                break;
            case Calendar.SATURDAY:
                startDay = 4;
                break;
        }
        return startDay;
    }

    private int getStartChoghadiaForNight(int day) {
        switch (day) {
            case Calendar.SUNDAY:
                startNight = 0;
                break;
            case Calendar.MONDAY:
                startNight = 2;
                break;
            case Calendar.TUESDAY:
                startNight = 4;
                break;
            case Calendar.WEDNESDAY:
                startNight = 6;
                break;
            case Calendar.THURSDAY:
                startNight = 1;
                break;
            case Calendar.FRIDAY:
                startNight = 3;
                break;
            case Calendar.SATURDAY:
                startNight = 5;
                break;
        }
        return startNight;
    }

    private String[] getChogadiaDayName(int startDay) {
        String dayChoghadiaName[] = new String[8];
        String chogadiyadayName[] = context.getResources().getStringArray(
                R.array.chogadiya_day_names_list);
        int j = 0;
        for (int i = startDay; i < 7; i++, j++) {
            dayChoghadiaName[j] = chogadiyadayName[i];
        }
        for (int l = 0; l <= startDay; l++) {
            dayChoghadiaName[j] = chogadiyadayName[l];
            j++;
        }
        return dayChoghadiaName;
    }

    private String[] getChogadiaNightName(int startDay) {
        String nightChoghadiaName[] = new String[8];
        String chogadiyaNightName[] = context.getResources().getStringArray(
                R.array.chogadiya_night_names_list);
        int j = 0;
        for (int i = startDay; i < 7; i++, j++) {
            nightChoghadiaName[j] = chogadiyaNightName[i];
        }
        for (int l = 0; l <= startDay; l++) {
            nightChoghadiaName[j] = chogadiyaNightName[l];
            j++;
        }
        return nightChoghadiaName;
    }

    private String[] getChogadiaDayNameMeaning(int startDay) {
        String dayChoghadiaNameMeaning[] = new String[8];
        String chogadiyadayName[] = context.getResources().getStringArray(
                R.array.chogadiya_day_names_list_meaning);
        int j = 0;
        for (int i = startDay; i < 7; i++, j++) {
            dayChoghadiaNameMeaning[j] = chogadiyadayName[i];
        }
        for (int l = 0; l <= startDay; l++) {
            dayChoghadiaNameMeaning[j] = chogadiyadayName[l];
            j++;
        }
        return dayChoghadiaNameMeaning;
    }

    private String[] getChogadiaNightNameMeaning(int startDay) {
        String nightChoghadiaNameMeaning[] = new String[8];
        String chogadiyaNightNameMeaning[] = context.getResources().getStringArray(
                R.array.chogadiya_night_names_list_meaning);
        int j = 0;
        for (int i = startDay; i < 7; i++, j++) {
            nightChoghadiaNameMeaning[j] = chogadiyaNightNameMeaning[i];
        }
        for (int l = 0; l <= startDay; l++) {
            nightChoghadiaNameMeaning[j] = chogadiyaNightNameMeaning[l];
            j++;
        }
        return nightChoghadiaNameMeaning;
    }

    public ArrayList<HoraMetadata> HoraEntryTime(int year, int month, int day, String latitude,
                                                 String longitude, String timezone) {

         double lat = 0;
         double lng = 0;
         double tzone = 0;

        ArrayList<HoraMetadata> datatime = new ArrayList<HoraMetadata>();
        int jd = (int) Masa.toJulian(year, month + 1, day);
        try {
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            tzone = Double.parseDouble(timezone);
        } catch (Exception e) {
        }

        Place place = new Place(lat, lng, tzone);
        for (int i = 0; i < 8; i++) {
            HoraMetadata hora = new HoraMetadata();
            if (i <= 7) {
                hora.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        Masa.getSunRise(jd, place), 8)[i], 0));
                datatime.add(hora);
            }
        }
        for (int j = 0; j < 8; j++) {
            HoraMetadata hora1 = new HoraMetadata();
            if (j <= 7) {
                hora1.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        Masa.getSunSet(jd, place), 8)[j], 0));
                datatime.add(hora1);
            }
        }
        return datatime;
    }

    public ArrayList<HoraMetadata> HoraExitTime(int year, int month, int day, String latitude,
                                                String longitude, String timezone) {

        double lat = 0;
        double lng = 0;
        double tzone = 0;

        ArrayList<HoraMetadata> datatimeexit = new ArrayList<HoraMetadata>();
        int jd = (int) Masa.toJulian(year, month + 1, day);
        try {
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            tzone = Double.parseDouble(timezone);
        } catch (NumberFormatException ex) {

        } catch (Exception e) {
            // TODO: handle exception
        }

        Place place = new Place(lat, lng, tzone);
        for (int i = 0; i < 9; i++) {
            HoraMetadata hora = new HoraMetadata();
            if (i > 0) {
                hora.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        Masa.getSunRise(jd, place), 8)[i], 0));
                datatimeexit.add(hora);
            }
        }
        for (int j = 0; j < 9; j++) {
            HoraMetadata hora1 = new HoraMetadata();
            if (j > 0) {
                hora1.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        Masa.getSunSet(jd, place), 8)[j], 0));
                datatimeexit.add(hora1);
            }
        }
        return datatimeexit;

    }

    public int getCurrentHoraNumber(List<HoraMetadata> datatime2,
                                    List<HoraMetadata> datatimeexit2) {
        String[] resultEntryTime = null;
        String[] resultExitTime = null;

        int currentHoraNumber = 0;
        try {
            Calendar calendar = Calendar.getInstance();

            Calendar horaEntryTime = Calendar.getInstance();
            Calendar horaExitTime = Calendar.getInstance();

            for (int i = 0; i <= 15; i++) {
                resultEntryTime = datatime2.get(i).getEntertimedata()
                        .split(":");
                resultExitTime = datatimeexit2.get(i).getExittimedata()
                        .split(":");

                if (Integer.parseInt(resultExitTime[0]) >= 24) {
                    if (Integer.parseInt(resultExitTime[0]) == 24) {
                        resultExitTime[0] = "00";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 25) {
                        resultExitTime[0] = "01";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 26) {
                        resultExitTime[0] = "02";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 27) {
                        resultExitTime[0] = "03";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 28) {
                        resultExitTime[0] = "04";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 29) {
                        resultExitTime[0] = "05";
                    }
                    if (Integer.parseInt(resultExitTime[0]) == 30) {
                        resultExitTime[0] = "06";
                    }
                }
                if (Integer.parseInt(resultEntryTime[0]) >= 24) {
                    if (Integer.parseInt(resultEntryTime[0]) == 24) {
                        resultEntryTime[0] = "00";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 25) {
                        resultEntryTime[0] = "01";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 26) {
                        resultEntryTime[0] = "02";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 27) {
                        resultEntryTime[0] = "03";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 28) {
                        resultEntryTime[0] = "04";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 29) {
                        resultEntryTime[0] = "05";
                    }
                    if (Integer.parseInt(resultEntryTime[0]) == 30) {
                        resultEntryTime[0] = "06";
                    }
                }

                long currentTimeMilliSeconds = calendar.getTimeInMillis();

                horaEntryTime.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(resultEntryTime[0]));

                horaEntryTime.set(Calendar.MINUTE,
                        Integer.parseInt(resultEntryTime[1]));
                horaEntryTime.set(Calendar.SECOND,
                        Integer.parseInt(resultEntryTime[2]));
                long horaEntryTimeMilliSeconds = horaEntryTime
                        .getTimeInMillis();

                horaExitTime.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(resultExitTime[0]));

                horaExitTime.set(Calendar.MINUTE,
                        Integer.parseInt(resultExitTime[1]));
                horaExitTime.set(Calendar.SECOND,
                        Integer.parseInt(resultExitTime[2]));
                long horaExitTimeMilliSeconds = horaExitTime.getTimeInMillis();
                if (horaEntryTimeMilliSeconds > horaExitTimeMilliSeconds) {
                    horaExitTimeMilliSeconds = horaExitTimeMilliSeconds + 24
                            * 60 * 60 * 1000;
                }
                if (currentTimeMilliSeconds < horaEntryTimeMilliSeconds) {
                    currentTimeMilliSeconds = currentTimeMilliSeconds + 24 * 60
                            * 60 * 1000;
                }

                if (currentTimeMilliSeconds >= horaEntryTimeMilliSeconds
                        && currentTimeMilliSeconds <= horaExitTimeMilliSeconds) {

                    currentHoraNumber = i;
                    break;
                }

            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        // System.out.println(currentHoraNumber);
        return currentHoraNumber;
    }
}
