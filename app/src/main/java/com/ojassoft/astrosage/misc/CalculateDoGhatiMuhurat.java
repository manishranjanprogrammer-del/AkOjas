package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.util.Log;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.HoraMetadata;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PanchangUtil;
import com.ojassoft.panchang.Masa;
import com.ojassoft.panchang.Muhurta;
import com.ojassoft.panchang.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ojas-08 on 10/3/17.
 */
public class CalculateDoGhatiMuhurat {
    Context context;


    /*private double lat = 28.36;
    private double lng = 77.12;
    private double tzone = +5.5;*/

    public CalculateDoGhatiMuhurat(Context context) {
        this.context = context;
    }

    public ArrayList<HoraMetadata> HoraLordName(int day_of_month) {
        int i;
        int dayLordForDayHora[] = new int[30];
        ArrayList<HoraMetadata> data = new ArrayList<HoraMetadata>();
        String PlanetName[] = context.getResources().getStringArray(
                R.array.do_ghati_name_list);
        String PlanetNameMeaning[] = context.getResources().getStringArray(
                R.array.do_ghati_name_list_Muhurat);
        String PlanetNameMeaningforcurrentHora[] = context.getResources()
                .getStringArray(R.array.do_ghati_muhurut_meaning);
        String doghatiSecondMeaning[] = context.getResources().getStringArray(
                R.array.do_ghati_name_list_meaning_second);
        String doghatiSecondMeaningwikipedia[] = context.getResources().getStringArray(
                R.array.do_ghati_name_list_meaning);
        String doghatimuhurat[] = context.getResources().getStringArray(
                R.array.do_ghati_muhurat);
        for (i = 0; i < 30; i++) {
            HoraMetadata hora = new HoraMetadata();
            if (i == 0 || i <= 2) {
                hora.setPlanetdata(PlanetName[0]);
            } else if (i == 3 || i <= 5) {
                hora.setPlanetdata(PlanetName[1]);
            } else if (i == 6 || i <= 8) {
                hora.setPlanetdata(PlanetName[2]);
            } else if (i == 9 || i <= 11) {
                hora.setPlanetdata(PlanetName[3]);
            } else if (i == 12 || i <= 14) {
                hora.setPlanetdata(PlanetName[4]);
            } else if (i == 15 || i <= 17) {
                hora.setPlanetdata(PlanetName[5]);
            } else if (i == 18 || i <= 21) {
                hora.setPlanetdata(PlanetName[6]);
            } else if (i == 22 || i <= 22) {
                hora.setPlanetdata(PlanetName[7]);
            } else if (i == 23 || i <= 27) {
                hora.setPlanetdata(PlanetName[8]);
            } else if (i == 28 || i <= 29) {
                hora.setPlanetdata(PlanetName[9]);
            }
            // hora.setPlanetdata(PlanetName[dayLordForDayHora[i]]);
            hora.setPlanetmeaning(PlanetNameMeaning[i]);
            // hora.setPlanetmeaning(PlanetName[0]);
            hora.setDoghatiSecondMeaning(doghatiSecondMeaning[i]);
            hora.setDoghatiSecondMeaningwikipedia(doghatiSecondMeaningwikipedia[i]);
            hora.setPlanetCurrentHorameaning(PlanetNameMeaningforcurrentHora[i]);
            hora.setDoghatimuhurat(doghatimuhurat[i]);
            data.add(hora);
        }
        return data;
    }

    public ArrayList<HoraMetadata> HoraEntryTime(Calendar calendar, int year, int month, int day, String latitude,
                                                 String longitude, String timezone, String timeZoneString) {

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

        //Check timeZone for DST if applicable then set automatically correction
        PanchangUtil objPanchangUtil = new PanchangUtil();
        Date date = calendar.getTime();
        if (objPanchangUtil.isDst(timeZoneString, date)) {
            tzone = tzone + 1.0;
        }
        // System.out.println("LAT LNG TMZNE" + lat + lng + tzone);
        Place place = new Place(lat, lng, tzone);
        for (int i = 0; i <= 14; i++) {
            HoraMetadata hora = new HoraMetadata();
            if (i <= 14) {
                hora.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        Masa.getSunRise(jd, place), 15)[i], 0));
                Muhurta.getDayDivisons(jd, place, Masa.getSunRise(jd, place), 8);
                datatime.add(hora);
            }
        }
        for (int j = 0; j <= 14; j++) {
            HoraMetadata hora1 = new HoraMetadata();
            if (j <= 14) {
                hora1.setEntertimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        Masa.getSunSet(jd, place), 15)[j], 0));
                datatime.add(hora1);
            }
        }
        return datatime;
    }

    public ArrayList<HoraMetadata> HoraExitTime(Calendar calendar, int year, int month, int day, String latitude,
                                                String longitude, String timezone, String timeZoneString) {

        double lat = 0;
        double lng = 0;
        double tzone = 0;

        ArrayList<HoraMetadata> datatimeexit = new ArrayList<HoraMetadata>();
        int jd = (int) Masa.toJulian(year, month + 1, day);
        try {
            lat = Double.parseDouble(latitude);
            lng = Double.parseDouble(longitude);
            tzone = Double.parseDouble(timezone);
        } catch (Exception e) {
        }

        //Check timeZone for DST if applicable then set automatically correction
        PanchangUtil objPanchangUtil = new PanchangUtil();
        Date date = calendar.getTime();
        if (objPanchangUtil.isDst(timeZoneString, date)) {
            tzone = tzone + 1.0;
        }
        // System.out.println("LAT LNG TMZNE" + lat + lng + tzone);
        Place place = new Place(lat, lng, tzone);
        for (int i = 0; i < 16; i++) {
            HoraMetadata hora = new HoraMetadata();
            if (i > 0) {
                hora.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getDayDivisons(jd, place,
                                        Masa.getSunRise(jd, place), 15)[i], 0));
                datatimeexit.add(hora);
            }
        }
        for (int j = 0; j < 16; j++) {
            HoraMetadata hora1 = new HoraMetadata();
            if (j > 0) {
                hora1.setExittimedata(CUtils
                        .FormatDMSIn2DigitStringWithSignForhora(
                                Muhurta.getNightDivisons(jd, place,
                                        Masa.getSunSet(jd, place), 15)[j], 0));
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
            /*
             * System.out .println("vvvv" + datatime2.size() +
             * datatimeexit2.size());
             */
            for (int i = 0; i <= datatime2.size(); i++) {
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
            Log.e("Exception ", e.getMessage().toString());
        }

        return currentHoraNumber;
    }
}
