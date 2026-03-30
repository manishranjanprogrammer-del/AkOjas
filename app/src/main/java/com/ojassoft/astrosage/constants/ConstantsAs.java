package com.ojassoft.astrosage.constants;

import com.ojassoft.astrosage.jinterface.IConstants;

/**
 * Supplies full Assamese panchang labels so local Panchang calculations do not fall back to English.
 */
public class ConstantsAs implements IConstants {
    @Override
    public String getMasas(int index) {
        return masas[index];
    }

    @Override
    public String getVaras(int index) {
        return varas[index];
    }

    @Override
    public String getVarasSwami(int index) {
        return vaarsSwami[index];
    }

    @Override
    public String getSamvats(int index) {
        return samvats[index];
    }

    @Override
    public String getRitus(int index) {
        return ritus[index];
    }

    @Override
    public String getKaranas(int index) {
        return karanas[index];
    }

    @Override
    public String getPakshas(int index) {
        return pakshas[index];
    }

    @Override
    public String getAyanas(int index) {
        return ayanas[index];
    }

    @Override
    public String getDishas(int index) {
        return dishas[index];
    }

    @Override
    public String getNakshatra(int index) {
        return nakshatra[index];
    }

    @Override
    public String getYoga(int index) {
        return yoga[index];
    }

    @Override
    public String getMoonSign(int index) {
        return rashi[index];
    }

    @Override
    public String getTithi(int index) {
        return tithi[index];
    }

    @Override
    public String getChandraBala(int index) {
        return rashi[index];
    }

    @Override
    public String getTaraBala(int index) {
        return nakshatra[index];
    }

    @Override
    public String[] getChandraBala() {
        return rashi;
    }

    @Override
    public String[] getTaraBala() {
        return nakshatra;
    }

    @Override
    public String getMonthName(int index) {
        return monthname[index];
    }

    @Override
    public String getMonthNameSort(int index) {
        return monthnameSort[index];
    }

    @Override
    public String getDayName(int index) {
        return dayname[index];
    }

    @Override
    public String getExString(int index) {
        return exstring[index];
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
    public String getLagna(int index) {
        return rashi2[index];
    }

    @Override
    public String getLagnaNature(int index) {
        return rashiNature[index];
    }

    @Override
    public String getRashi(int index) {
        return rashi2[index];
    }

    public final String[] masas = {"চৈত্ৰ", "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্ৰাৱণ", "ভাদ্ৰ", "আশ্বিন", "কাৰ্তিক", "অগ্ৰহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন"};
    public final String[] varas = {"ৰবিবাৰ", "সোমবাৰ", "মঙলবাৰ", "বুধবাৰ", "বৃহস্পতিবাৰ", "শুকুৰবাৰ", "শনিবাৰ"};
    public final String[] vaarsSwami = {"NA", "সূৰ্য", "চন্দ্ৰ", "মঙ্গল", "বুধ", "বৃহস্পতি", "শুক্ৰ", "শনি"};
    public final String[] samvats = {"না", "প্ৰভৱ", "বিভৱ", "শুক্ল", "প্ৰমোদ", "প্ৰজাপতি", "আঙ্গিৰস", "শ্ৰীমুখ", "ভাৱ",
            "যুৱ", "ধাতা", "ঈশ্বৰ", "বহুধান্য", "প্ৰমাথী", "বিক্ৰম", "বৃষ", "চিত্ৰভানু",
            "স্বভানু", "তাৰণ", "পাৰ্থিৱ", "ব্যয়", "সৰ্বজিত", "সৰ্বধাৰী", "বিৰোধী", "বিকৃতি",
            "খৰ", "নন্দন", "বিজয়", "জয়", "মন্মথ", "দুৰ্মুখ", "হেভিলম্বী", "বিলম্বী",
            "বিকাৰী", "শাৰ্বৰী", "প্লৱ", "শুভকৃত", "শোভকৃত", "ক্ৰোধী", "বিশ্বাৱসু", "পৰাভৱ",
            "প্লৱঙ্গ", "কীলক", "সৌম্য", "সাধাৰণ", "বিৰোধকৃত", "পৰিধাবী", "প্ৰমাদীচ", "আনন্দ", "ৰাক্ষস", "নল",
            "পিঙ্গল", "কালযুক্তি", "সিদ্ধাৰ্থী", "ৰৌদ্ৰ", "দুৰ্মতি", "দুন্দুভি", "ৰুধিৰোদ্গাৰী", "ৰক্তাক্ষী", "ক্ৰোধন", "অক্ষয়"};
    public final String[] ritus = {"বসন্ত", "গ্ৰীষ্ম", "বৰ্ষা", "শৰৎ", "হেমন্ত", "শীত"};
    public final String[] karanas = {"NA", "কিন্তুগ্ন", "বব", "বালৱ", "কৌলৱ", "তৈতিল", "গৰ", "বণিজ", "বিষ্টি", "বব", "বালৱ", "কৌলৱ", "তৈতিল", "গৰ", "বণিজ", "বিষ্টি", "বব", "বালৱ", "কৌলৱ", "তৈতিল", "গৰ", "বণিজ", "বিষ্টি", "বব", "বালৱ", "কৌলৱ", "তৈতিল", "গৰ", "বণিজ", "বিষ্টি", "বব", "বালৱ", "কৌলৱ", "তৈতিল", "গৰ", "বণিজ", "বিষ্টি", "বব", "বালৱ", "কৌলৱ", "তৈতিল", "গৰ", "বণিজ", "বিষ্টি", "বব", "বালৱ", "কৌলৱ", "তৈতিল", "গৰ", "বণিজ", "বিষ্টি", "বব", "বালৱ", "কৌলৱ", "তৈতিল", "গৰ", "বণিজ", "বিষ্টি", "শকুনি", "চতুষ্পদ", "নাগ"};
    public final String[] pakshas = {"শুক্ল", "কৃষ্ণ"};
    public final String[] ayanas = {"উত্তৰায়ণ", "দক্ষিণায়ণ"};
    public final String[] dishas = {"পূব", "পশ্চিম", "উত্তৰ", "দক্ষিণ"};
    public final String[] nakshatra = {"NA", "অশ্বিনী", "ভৰণী", "কৃত্তিকা", "ৰোহিণী", "মৃগশিৰা", "আৰ্দ্ৰা", "পুনৰ্বসু", "পুষ্যা", "অশ্লেষা", "মঘা", "পূৰ্বফাল্গুনী", "উত্তৰফাল্গুনী", "হস্তা", "চিত্ৰা", "স্বাতী", "বিশাখা", "অনুৰাধা", "জ্যেষ্ঠা", "মূলা", "পূৰ্বাষাঢ়া", "উত্তৰাষাঢ়া", "শ্ৰৱণা", "ধনিষ্ঠা", "শতভিষা", "পূৰ্বভাদ্ৰপদা", "উত্তৰভাদ্ৰপদা", "ৰেৱতী"};
    public final String[] yoga = {"NA", "বিষ্কুম্ভ", "প্ৰীতি", "আয়ুষ্মান", "সৌভাগ্য", "শোভন", "অতিগণ্ড", "সুকৰ্মা", "ধৃতি", "শূল", "গণ্ড", "বৃদ্ধি", "ধ্ৰুব",
            "ব্যাঘাত", "হৰ্ষণ", "বজ্ৰ", "সিদ্ধি", "ব্যতিপাত", "ৱৰীয়ান", "পৰিঘ", "শিৱ", "সিদ্ধ", "সাধ্য", "শুভ", "শুক্ল", "ব্ৰহ্ম", "ইন্দ্ৰ", "বৈধৃতি"};
    public final String[] tithi = {"NA", "প্ৰতিপদা", "দ্বিতীয়া", "তৃতীয়া", "চতুৰ্থী", "পঞ্চমী", "ষষ্ঠী", "সপ্তমী", "অষ্টমী", "নৱমী", "দশমী", "একাদশী", "দ্বাদশী", "ত্ৰয়োদশী", "চতুৰ্দশী",
            "পূৰ্ণিমা", "প্ৰতিপদা", "দ্বিতীয়া", "তৃতীয়া", "চতুৰ্থী", "পঞ্চমী", "ষষ্ঠী", "সপ্তমী", "অষ্টমী", "নৱমী", "দশমী", "একাদশী", "দ্বাদশী", "ত্ৰয়োদশী", "চতুৰ্দশী", "অমাৱস্যা"};
    public final String[] rashi = {"NA", "মেষ", "বৃষ", "মিথুন", "কৰ্কট", "সিংহ", "কন্যা", "তুলা", "বৃশ্চিক", "ধনু", "মকৰ", "কুম্ভ", "মীন"};
    public final String[] rashi2 = {"NA", "মেষ", "বৃষ", "মিথুন", "কৰ্কট", "সিংহ", "কন্যা", "তুলা", "বৃশ্চিক", "ধনু", "মকৰ", "কুম্ভ", "মীন"};
    public final String[] rashiNature = {"NA", "চৰ", "স্থিৰ", "দ্বিস্বভাৱ", "চৰ", "স্থিৰ", "দ্বিস্বভাৱ", "চৰ", "স্থিৰ", "দ্বিস্বভাৱ", "চৰ", "স্থিৰ", "দ্বিস্বভাৱ"};
    public final String[] monthname = {"NA", "জানুৱাৰী", "ফেব্ৰুৱাৰী", "মাৰ্চ", "এপ্ৰিল", "মে", "জুন", "জুলাই", "আগষ্ট", "ছেপ্টেম্বৰ", "অক্টোবৰ", "নৱেম্বৰ", "ডিচেম্বৰ"};
    public final String[] monthnameSort = {"NA", "জানু", "ফেব্ৰু", "মাৰ্চ", "এপ্ৰিল", "মে", "জুন", "জুলাই", "আগষ্ট", "ছেপ্টে", "অক্টো", "নৱে", "ডিচে"};
    public final String[] dayname = {"NA", "ৰবিবাৰ", "সোমবাৰ", "মঙলবাৰ", "বুধবাৰ", "বৃহস্পতিবাৰ", "শুকুৰবাৰ", "শনিবাৰ"};
    public final String[] choghadiaDayName = {"উদ্বেগ", "চল", "লাভ", "অমৃত", "কাল", "শুভ", "ৰোগ"};
    public final String[] choghadiaNightName = {"শুভ", "অমৃত", "চল", "ৰোগ", "কাল", "লাভ", "উদ্বেগ"};
    public final String[] exstring = {"কোনো নাই", "চন্দ্ৰোদয় নাই", "চন্দ্ৰাস্ত নাই", "(অধিক)"};
}
