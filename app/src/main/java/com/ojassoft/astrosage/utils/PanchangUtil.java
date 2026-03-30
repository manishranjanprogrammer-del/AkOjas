package com.ojassoft.astrosage.utils;

import com.ojassoft.astrosage.constants.Constants;
import com.ojassoft.astrosage.constants.ConstantsAs;
import com.ojassoft.astrosage.constants.ConstantsBn;
import com.ojassoft.astrosage.constants.ConstantsGu;
import com.ojassoft.astrosage.constants.ConstantsHi;
import com.ojassoft.astrosage.constants.ConstantsKa;
import com.ojassoft.astrosage.constants.ConstantsOr;
import com.ojassoft.astrosage.constants.LocalizedLagnaConstants;
import com.ojassoft.astrosage.constants.ConstantsMl;
import com.ojassoft.astrosage.constants.ConstantsMr;
import com.ojassoft.astrosage.constants.ConstantsTa;
import com.ojassoft.astrosage.constants.ConstantsTe;
import com.ojassoft.astrosage.jinterface.IConstants;
import com.ojassoft.astrosage.misc.Language;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PanchangUtil {
    private static final String[] RASHI_NAMES_ODIA = {
            "NA", "ମେଷ", "ବୃଷ", "ମିଥୁନ", "କର୍କଟ", "ସିଂହ", "କନ୍ୟା", "ତୁଳା", "ବିଛା", "ଧନୁ", "ମକର", "କୁମ୍ଭ", "ମୀନ"
    };
    private static final String[] LAGNA_NATURES_ODIA = {
            "NA", "ଚର", "ସ୍ଥିର", "ଦ୍ୱିସ୍ୱଭାବ", "ଚର", "ସ୍ଥିର", "ଦ୍ୱିସ୍ୱଭାବ", "ଚର", "ସ୍ଥିର", "ଦ୍ୱିସ୍ୱଭାବ", "ଚର", "ସ୍ଥିର", "ଦ୍ୱିସ୍ୱଭାବ"
    };
    private static final String[] RASHI_NAMES_ASSAMESE = {
            "NA", "মেষ", "বৃষ", "মিথুন", "কৰ্কট", "সিংহ", "কন্যা", "তুলা", "বৃশ্চিক", "ধনু", "মকৰ", "কুম্ভ", "মীন"
    };
    private static final String[] LAGNA_NATURES_ASSAMESE = {
            "NA", "চৰ", "স্থিৰ", "দ্বিস্বভাৱ", "চৰ", "স্থিৰ", "দ্বিস্বভাৱ", "চৰ", "স্থিৰ", "দ্বিস্বভাৱ", "চৰ", "স্থিৰ", "দ্বিস্বভাৱ"
    };
    private static final String[] RASHI_NAMES_SPANISH = {
            "NA", "Aries", "Tauro", "Géminis", "Cáncer", "Leo", "Virgo", "Libra", "Escorpión", "Sagitario", "Capricornio", "Acuario", "Piscis"
    };
    private static final String[] LAGNA_NATURES_SPANISH = {
            "NA", "Cardinal", "Fijo", "Mutable", "Cardinal", "Fijo", "Mutable", "Cardinal", "Fijo", "Mutable", "Cardinal", "Fijo", "Mutable"
    };
    private static final String[] RASHI_NAMES_CHINESE = {
            "NA", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"
    };
    private static final String[] LAGNA_NATURES_CHINESE = {
            "NA", "本位", "固定", "变动", "本位", "固定", "变动", "本位", "固定", "变动", "本位", "固定", "变动"
    };
    private static final String[] RASHI_NAMES_JAPANESE = {
            "NA", "牡羊座", "牡牛座", "双子座", "蟹座", "獅子座", "乙女座", "天秤座", "蠍座", "射手座", "山羊座", "水瓶座", "魚座"
    };
    private static final String[] LAGNA_NATURES_JAPANESE = {
            "NA", "活動宮", "不動宮", "柔軟宮", "活動宮", "不動宮", "柔軟宮", "活動宮", "不動宮", "柔軟宮", "活動宮", "不動宮", "柔軟宮"
    };
    private static final String[] RASHI_NAMES_PORTUGUESE = {
            "NA", "Áries", "Touro", "Gêmeos", "Câncer", "Leão", "Virgem", "Libra", "Escorpião", "Sagitário", "Capricórnio", "Aquário", "Peixes"
    };
    private static final String[] LAGNA_NATURES_PORTUGUESE = {
            "NA", "Cardinal", "Fixo", "Mutável", "Cardinal", "Fixo", "Mutável", "Cardinal", "Fixo", "Mutável", "Cardinal", "Fixo", "Mutável"
    };
    private static final String[] RASHI_NAMES_GERMAN = {
            "NA", "Widder", "Stier", "Zwillinge", "Krebs", "Löwe", "Jungfrau", "Waage", "Skorpion", "Schütze", "Steinbock", "Wassermann", "Fische"
    };
    private static final String[] LAGNA_NATURES_GERMAN = {
            "NA", "Kardinal", "Fix", "Veränderlich", "Kardinal", "Fix", "Veränderlich", "Kardinal", "Fix", "Veränderlich", "Kardinal", "Fix", "Veränderlich"
    };
    private static final String[] RASHI_NAMES_ITALIAN = {
            "NA", "Ariete", "Toro", "Gemelli", "Cancro", "Leone", "Vergine", "Bilancia", "Scorpione", "Sagittario", "Capricorno", "Acquario", "Pesci"
    };
    private static final String[] LAGNA_NATURES_ITALIAN = {
            "NA", "Cardinale", "Fisso", "Mutevole", "Cardinale", "Fisso", "Mutevole", "Cardinale", "Fisso", "Mutevole", "Cardinale", "Fisso", "Mutevole"
    };
    private static final String[] RASHI_NAMES_FRENCH = {
            "NA", "Bélier", "Taureau", "Gémeaux", "Cancer", "Lion", "Vierge", "Balance", "Scorpion", "Sagittaire", "Capricorne", "Verseau", "Poissons"
    };
    private static final String[] LAGNA_NATURES_FRENCH = {
            "NA", "Cardinal", "Fixe", "Mutable", "Cardinal", "Fixe", "Mutable", "Cardinal", "Fixe", "Mutable", "Cardinal", "Fixe", "Mutable"
    };

    public static double fract(double x) {
        long i;
        double y;
        i = (long) x;
        y = x - (double) i;
        return y;
    }

    public static String makelength(String x, int y) {
        int diff = y - x.length();
        for (int i = 0; i < diff; i++)
            x = "0" + x;
        return x;
    }

    public String dms(double x) {
        String parts[] = new String[3];
        double temp;
        String sdms;
        int deg = (int) x;
        parts[0] = makelength(String.valueOf(deg), 2);
        temp = (x - (double) ((int) x));
        int min = (int) (temp * 60);
        parts[1] = makelength(String.valueOf(min), 2);
        temp = temp * 60;
        temp = (temp - (double) ((int) temp));
        int sec = (int) (temp * 60);
        //System.out.println("temp " + temp + " sec " + sec);
        parts[2] = makelength(String.valueOf(sec), 2);
        sdms = parts[0] + getDashString(1) + parts[1] + getDashString(1) + parts[2];
        return sdms;
    }

    public String getDashString(int noOfDash) {
        String DASH = "";
        if (getLanguageCode().equalsIgnoreCase("0")) {
            DASH = ":";
        } else if (getLanguageCode().equalsIgnoreCase("1")) {
            DASH = "&";
        }
        String dash = "";
        for (int i = 0; i < noOfDash; i++) {
            dash = dash + DASH;
        }
        return dash;
    }

    public String getLanguageCode() {
        return "0";
    }

    //*********** Increment in Date Days ***************
    public Date getAddDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    // ***************** fetch DST or not from Time_Zone_String ***************
    public boolean isDst(String timezoneString, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
       /* cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);*/
        Date dateTime = cal.getTime();
        boolean inDs;
        try {
            TimeZone tz = TimeZone.getTimeZone(timezoneString);
            inDs = tz.inDaylightTime(dateTime);
        } catch (Exception ex) {
            inDs = false;
        }
        return inDs;
    }

    //********* Get Constant Class Object **********
    public IConstants getIConstantsObj(Language la) {
        switch (la) {

            case hi:
                return (new ConstantsHi());
            case ta:
                return (new ConstantsTa());
            case te:
                return (new ConstantsTe());
            case ka:
                return (new ConstantsKa());
            case ml:
                return (new ConstantsMl());
            case gu:
                return (new ConstantsGu());
            case mr:
                return (new ConstantsMr());
            case bn:
                return (new ConstantsBn());
            case or:
                return new ConstantsOr();
            case as:
                return new ConstantsAs();
            case es:
                return new LocalizedLagnaConstants(RASHI_NAMES_SPANISH, LAGNA_NATURES_SPANISH);
            case zh:
                return new LocalizedLagnaConstants(RASHI_NAMES_CHINESE, LAGNA_NATURES_CHINESE);
            case ja:
                return new LocalizedLagnaConstants(RASHI_NAMES_JAPANESE, LAGNA_NATURES_JAPANESE);
            case pt:
                return new LocalizedLagnaConstants(RASHI_NAMES_PORTUGUESE, LAGNA_NATURES_PORTUGUESE);
            case de:
                return new LocalizedLagnaConstants(RASHI_NAMES_GERMAN, LAGNA_NATURES_GERMAN);
            case it:
                return new LocalizedLagnaConstants(RASHI_NAMES_ITALIAN, LAGNA_NATURES_ITALIAN);
            case fr:
                return new LocalizedLagnaConstants(RASHI_NAMES_FRENCH, LAGNA_NATURES_FRENCH);
            case en:
            default:
                return (new Constants());
        }
    }


}
