package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;


public class ConstantsBn implements IConstants {
    @Override
    public String getMasas(int index) {
        // TODO Auto-generated method stub
        return masas[index];
    }

    @Override
    public String getVaras(int index) {
        // TODO Auto-generated method stub
        return varas[index];
    }

    @Override
    public String getVarasSwami(int index) {
        // TODO Auto-generated method stub
        return vaars_swami[index];
    }

    @Override
    public String getSamvats(int index) {
        // TODO Auto-generated method stub
        return samvats[index];
    }

    @Override
    public String getRitus(int index) {
        // TODO Auto-generated method stub
        return ritus[index];
    }

    @Override
    public String getKaranas(int index) {
        // TODO Auto-generated method stub
        return karanas[index];
    }

    @Override
    public String getPakshas(int index) {
        // TODO Auto-generated method stub
        return pakshas[index];
    }

    @Override
    public String getAyanas(int index) {
        // TODO Auto-generated method stub
        return ayanas[index];
    }

    @Override
    public String getDishas(int index) {
        // TODO Auto-generated method stub
        return dishas[index];
    }

    @Override
    public String getNakshatra(int index) {
        // TODO Auto-generated method stub
        return nakshatra[index];
    }

    @Override
    public String getYoga(int index) {
        // TODO Auto-generated method stub
        return yoga[index];
    }

    @Override
    public String getMoonSign(int index) {
        // TODO Auto-generated method stub
        return rashi[index];
    }

    @Override
    public String getTithi(int index) {
        // TODO Auto-generated method stub
        return tithi[index];
    }

    @Override
    public String getChandraBala(int index) {
        // TODO Auto-generated method stub
        return rashi[index];
    }

    @Override
    public String getTaraBala(int index) {
        // TODO Auto-generated method stub
        return nakshatra[index];
    }

    @Override
    public String[] getChandraBala() {
        // TODO Auto-generated method stub
        return rashi;
    }

    @Override
    public String[] getTaraBala() {
        // TODO Auto-generated method stub
        return nakshatra;
    }

    @Override
    public String getMonthName(int index) {
        // TODO Auto-generated method stub
        return monthname[index];
    }

    @Override
    public String getMonthNameSort(int index) {
        // TODO Auto-generated method stub
        return monthnameSort[index];
    }

    @Override
    public String getDayName(int index) {
        // TODO Auto-generated method stub
        return dayname[index];
    }

    @Override
    public String getChoghadiaDayName(int index) {
        return choghadiaDayName[index];
    }

    @Override
    public String getChoghadiaNightName(int index) {
        return choghadiaNightName[index];
    }

    @Override
    public String getExString(int index) {
        // TODO Auto-generated method stub
        return exstring[index];
    }

    @Override
    public String getLagna(int index) {
        return rashi2[index];
    }

    @Override
    public String getLagnaNature(int index) {
        return rashiNature[index];
    }

    @Override
    public String getRashi(int index) {
        // TODO Auto-generated method stub
        return rashi2[index];
    }

    public final String[] masas = {"চৈত্র", "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র", "অাশ্বিন", "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন"};

    // base 0
    public final String[] varas = {"রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার"};

    public final String[] vaars_swami = {"NA", "সূর্য", "চাঁদ", "মঙ্গল", "বুধ", "বৃহস্পতি", "শুক্র", "শনি"};

    // base 1
    public final String[] samvats = {"Na", "প্রভব", "বিভব", "শুক্ল", "প্রমোদ", "প্রজাপতি", "অঙ্গিরা", "শ্রীমুখ", "ভাব",
            "যুব", "ধাতা", "ঈশ্বর", "বহুধান্য", "প্রমাথী", "বিক্রম", "বৃষ", "চিত্রভানু",
            "সুভানু", "তারণ", "পার্থিব", "অব্যয়", "সর্বজিৎ", "সর্বধারী", "বিরোধী", "বিকৃতি",
            "খর", "নন্দন", "বিজয়", "জয়", "মন্মথ", "দুর্মুখ", "হেমলম্বী", "বিলম্বী",
            "বিকারী", "শর্বরী", "প্লব", "শুভকৃত", "শোভকৃত", "ক্রোধী", "বিশ্বাবসু", "পরাভব",
            "প্লবঙ্গ", "কীলক", "সৌম্য", "সাধারণ", "বিরোধকৃত", "পরিধাবী", "প্রসাদী", "আনন্দ", "রাক্ষস", "আনল",
            "পিঙ্গল", "কালযুক্ত", "সিদ্ধার্থী", "রৌদ্র", "দুর্মতি", "দুন্দুভি", "রুধিরোদ্গারী", "রক্তাক্ষি", "ক্রোধন", "অক্ষয়"
    };

    //0 base
    public final String[] ritus = {"বসন্ত", "গ্রীষ্ম", "বর্ষা", "শরৎ", "হেমন্ত", "শীত"};

    public final String[] karanas = {"NA", "কিন্তুঘ্ন", "বব", "বালব", "কৌলব", "তৈতিল", "গর", "বণিজ", "বিষ্টি", "বব", "বালব", "কৌলব", "তৈতিল", "গর", "বণিজ", "বিষ্টি", "বব", "বালব", "কৌলব", "তৈতিল", "গর", "বণিজ", "বিষ্টি", "বব", "বালব", "কৌলব", "তৈতিল", "গর", "বণিজ", "বিষ্টি", "বব", "বালব", "কৌলব", "তৈতিল", "গর", "বণিজ", "বিষ্টি", "বব", "বালব", "কৌলব", "তৈতিল", "গর", "বণিজ", "বিষ্টি", "বব", "বালব", "কৌলব", "তৈতিল", "গর", "বণিজ", "বিষ্টি", "বব", "বালব", "কৌলব", "তৈতিল", "গর", "বণিজ", "বিষ্টি", "শকুনি", "চতুষ্পাদ", "নাগ"};

    public final String[] pakshas = {"শুক্ল", "কৃষ্ণ"};

    public final String[] ayanas = {"উত্তরায়ণ", "দক্ষিণায়ণ"};

    public final String[] dishas = {"পূর্ব", "পশ্চিম", "উত্তর", "দক্ষিণ"};

    public final String[] nakshatra = {"NA", "অশ্বিনী", "ভরণী", "কৃত্তিকা", "রোহিণী", "মৃগশিরা", "আর্দ্রা", "পুনর্বসু", "পুষ্যা", "অশ্লেষা", "মঘা", "পূর্বফাল্গুনী", "উত্তরফাল্গুনী", "হস্তা", "চিত্রা", "স্বাতী", "বিশাখা", "অনুরাধা", "জ্যেষ্ঠা", "মূলা", "পূর্বাষাঢ়া", "উত্তরাষাঢ়া", "শ্রবণা", "ধনিষ্ঠা", "শতভিষা", "পূর্বভাদ্রপদ", "উত্তরভাদ্রপদ", "রেবতী"};

    public final String[] yoga = {"NA", "বিষ্কুম্ভ", "প্রীতি", "আয়ুষ্মান", "সৌভাগ্য", "শোভন", "অতিগণ্ড", "সুকর্মা", "ধৃতি", "শূল", "গণ্ড", "বৃদ্ধি", "ধ্রুব",
            "ব্যাঘাত", "হর্ষণ", "বজ্র", "সিদ্ধি", "ব্যাতিপাত", "বরীয়ান", "পরিঘ", "শিব", "সিদ্ধি", "সাধ্য", "শুভ", "শুক্ল", "ব্রহ্ম", "ইন্দ্র", "বৈধৃতি"};

    public final String[] tithi = {"NA", "প্রতিপদ", "দ্বিতীয়া", "তৃতীয়া", "চতুর্থী", "পঞ্চমী", "ষষ্ঠী", "সপ্তমী", "অষ্টমী", "নবমী", "দশমী", "একাদশী", "দ্বাদশী", "ত্রয়োদশী", "চতুর্দশী",
            "পূর্ণিমা", "প্রতিপদ", "দ্বিতীয়া", "তৃতীয়া", "চতুর্থী", "পঞ্চমী", "ষষ্ঠী", "সপ্তমী", "অষ্টমী", "নবমী", "দশমী", "একাদশী", "দ্বাদশী", "ত্রয়োদশী", "চতুর্দশী", "অমাবস্যা"};

    public final String[] rashi = {"NA", "মেষ", "বৃষ", "মিথুন", "কর্কট", "সিংহ", "কন্যা", "তুলা", "বৃশ্চিক", "ধনু", "মকর", "কুম্ভ", "মীন"};

    public final String[] rashi2 = {"NA", "মেষ", "বৃষ", "মিথুন", "কর্কট", "সিংহ", "কন্যা", "তুলা", "বৃশ্চিক", "ধনু", "মকর", "কুম্ভ", "মীন"};
    public final String[] rashiNature = {"NA", "অস্থাবর", "স্থাবর", "দ্বৈত", "অস্থাবর", "স্থাবর", "দ্বৈত", "অস্থাবর", "স্থাবর", "দ্বৈত", "অস্থাবর", "স্থাবর", "দ্বৈত"};
    public final String[] monthname = {"NA", "জানুয়ারী", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "অগাস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"};

    public final String[] monthnameSort = {"NA", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public final String[] dayname = {"NA", "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার"};

    public final String[] choghadiaDayName = {"Udveg", "Chal", "Laabh", "Amrut", "Kaal", "Shubh", "Rog"};

    public final String[] choghadiaNightName = {"Shubh", "Amrut", "Chal", "Rog", "Kaal", "Laabh", "Udveg"};

    public final String[] exstring = {"কেউ না", "চন্দ্রোদয় নেই", "চন্দ্রাস্ট নেই", "(অধিক)"};

}




