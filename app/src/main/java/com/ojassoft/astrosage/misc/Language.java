package com.ojassoft.astrosage.misc;

public enum Language {
    // [Description("English")]
    en,

    // [Description("Hindi")]
    hi,

    // [Description("Tamil")]
    ta,

    // [Description("Telugu")]
    te,

    // [Description("Kannada")]
    ka,

    //[Description("Malayalam")]
    ml,

    // [Description("Gujarati")]
    gu,

    // [Description("Marathi")]
    mr,

    // [Description("Bengali")]
    bn,

    // [Description("Odia")]
    or,
    as,// [Description("Assammese")]

    es,// [Description("Spanish")]
    zh,// [Description("Chinese")]
    ja,// [Description("Japanese")]
    pt,// [Description("Portuguese")]
    de,// [Description("German")]
    it,// [Description("Italian")]
    fr;// [Description("French")]

    public static Language getLanguage(String lang) {
        switch (lang) {
            case "hi":
                return hi;
            case "ta":
                return ta;
            case "te":
                return te;
            case "ka":
                return ka;
            case "ml":
                return ml;
            case "gu":
                return gu;
            case "mr":
                return mr;
            case "bn":
                return bn;
            case "or":
                return or;
            case "as":
                return as;
            case "es":
                return es;
            case "zh":
                return zh;
            case "ja":
                return ja;
            case "pt":
                return pt;
            case "de":
                return de;
            case "it":
                return it;
            case "fr":
                return fr;
            case "en":
            default:
                return en;
        }
    }
}
 
 
