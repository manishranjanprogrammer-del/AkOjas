package com.ojassoft.astrosage.beans;

public class AajKaPanchangModel {

    public AajKaPanchangModel() {

    }

    //	public static AajKaPanchangModel getPanchang(Date _datePan, String _placeID ) {
//		AajKaPanchangModel objAajKaPanchangModel = new AajKaPanchangModel();
//		
//		return objAajKaPanchangModel;
//	}
    private boolean isPlaceValid;
    //************* Masa Properties Start ***************
    private double sunRiseDouble;
    private double sunSetDouble;
    private String sunRise;
    private String sunSet;
    private String moonRise;
    private String moonSet;
    private String moonSign;
    private String moonSignValue;
    private String moonSignTime;
    private String ritu;
    private String tithi;
    private double[] tithiInt;
    private String tithiValue;
    private String tithiTime;
    private String nakshatra;
    private String nakshatraValue;
    private String nakshatraTime;
    private String karana;
    private String karanaValue;
    private String karanaTime;
    private String pakshaName;
    private String yoga;
    private String yogaValue;
    private String yogaTime;
    private String vaara;
    private String shakaSamvat;
    private String shakaSamvatName;
    private String shakaSamvatYear;
    private String vikramSamvat;
    private String kaliSamvat;
    private String monthAmanta;
    private String monthPurnimanta;
    private String panchang;
    //************* Masa Properties End***************

    //************* Muhurta Properties Start***************.

    private String dayDuration;
    private String abhijit;
    private String abhijitFrom;
    private String abhijitTo;
    private String kulika;
    private String kulikaFrom;
    private String kulikaTo;
    private String kantaka_Mrityu;
    private String kantaka_MrityuFrom;
    private String kantaka_MrityuTo;
    private String kalavela_Ardhayaam;
    private String kalavela_ArdhayaamFrom;
    private String kalavela_ArdhayaamTo;
    private String yamaghanta;
    private String yamaghantaFrom;
    private String yamaghantaTo;
    private String dushtaMuhurtas;
    private String dushtaMuhurtasFrom;
    private String dushtaMuhurtasTo;
    private String rahuKaalVela;
    private String rahuKaalVelaFrom;
    private String rahuKaalVelaTo;
    private String gulikaKaalVela;
    private String gulikaKaalVelaFrom;
    private String gulikaKaalVelaTo;

    public String getKulikaFrom() {
        return kulikaFrom;
    }

    public void setKulikaFrom(String kulikaFrom) {
        this.kulikaFrom = kulikaFrom;
    }

    public String getKulikaTo() {
        return kulikaTo;
    }

    public void setKulikaTo(String kulikaTo) {
        this.kulikaTo = kulikaTo;
    }

    public String getKalavela_ArdhayaamFrom() {
        return kalavela_ArdhayaamFrom;
    }

    public void setKalavela_ArdhayaamFrom(String kalavela_ArdhayaamFrom) {
        this.kalavela_ArdhayaamFrom = kalavela_ArdhayaamFrom;
    }

    public String getKalavela_ArdhayaamTo() {
        return kalavela_ArdhayaamTo;
    }

    public void setKalavela_ArdhayaamTo(String kalavela_ArdhayaamTo) {
        this.kalavela_ArdhayaamTo = kalavela_ArdhayaamTo;
    }

    public String getRahuKaalVelaFrom() {
        return rahuKaalVelaFrom;
    }

    public void setRahuKaalVelaFrom(String rahuKaalVelaFrom) {
        this.rahuKaalVelaFrom = rahuKaalVelaFrom;
    }

    public String getRahuKaalVelaTo() {
        return rahuKaalVelaTo;
    }

    public void setRahuKaalVelaTo(String rahuKaalVelaTo) {
        this.rahuKaalVelaTo = rahuKaalVelaTo;
    }

    public String getGulikaKaalVelaFrom() {
        return gulikaKaalVelaFrom;
    }

    public void setGulikaKaalVelaFrom(String gulikaKaalVelaFrom) {
        this.gulikaKaalVelaFrom = gulikaKaalVelaFrom;
    }

    public String getGulikaKaalVelaTo() {
        return gulikaKaalVelaTo;
    }

    public void setGulikaKaalVelaTo(String gulikaKaalVelaTo) {
        this.gulikaKaalVelaTo = gulikaKaalVelaTo;
    }

    public String getYamagandaVelaFrom() {
        return yamagandaVelaFrom;
    }

    public void setYamagandaVelaFrom(String yamagandaVelaFrom) {
        this.yamagandaVelaFrom = yamagandaVelaFrom;
    }

    public String getYamagandaVelaTo() {
        return yamagandaVelaTo;
    }

    public void setYamagandaVelaTo(String yamagandaVelaTo) {
        this.yamagandaVelaTo = yamagandaVelaTo;
    }

    private String yamagandaVela;
    private String yamagandaVelaFrom;
    private String yamagandaVelaTo;
    private String dishaShoola;
    private String taraBala;
    private String chandraBala;

    //************* Muhurta Properties Start***************

    //*********** Aaj ka panchang Properties Start **********
    private String pageDailypanchangH1;
    private String pageTitleContent;
    private String pageDescriptionContent;
    private String pageKeywordContent;


    public boolean getIsPlaceValid() {
        return isPlaceValid;
    }

    public void setIsPlaceValid(boolean isPlaceValid) {
        this.isPlaceValid = isPlaceValid;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }

    public String getMoonRise() {
        return moonRise;
    }

    public void setMoonRise(String moonRise) {
        this.moonRise = moonRise;
    }

    public String getMoonSet() {
        return moonSet;
    }

    public void setMoonSet(String moonSet) {
        this.moonSet = moonSet;
    }

    public String getMoonSign() {
        return moonSign;
    }

    public void setMoonSign(String moonSign) {
        this.moonSign = moonSign;
    }

    public String getMoonSignValue() {
        return moonSignValue;
    }

    public void setMoonSignValue(String moonSignValue) {
        this.moonSignValue = moonSignValue;
    }

    public String getMoonSignTime() {
        return moonSignTime;
    }

    public void setMoonSignTime(String moonSignTime) {
        this.moonSignTime = moonSignTime;
    }

    public String getRitu() {
        return ritu;
    }

    public void setRitu(String ritu) {
        this.ritu = ritu;
    }

    public String getTithi() {
        return tithi;
    }

    public void setTithi(String tithi) {
        this.tithi = tithi;
    }

    public String getTithiValue() {
        return tithiValue;
    }

    public void setTithiValue(String tithiValue) {
        this.tithiValue = tithiValue;
    }

    public double[] getTithiInt() {
        return tithiInt;
    }

    public void setTithiInt(double[] tithiInt) {
        this.tithiInt = tithiInt;
    }

    public String getTithiTime() {
        return tithiTime;
    }

    public void setTithiTime(String tithiTime) {
        this.tithiTime = tithiTime;
    }

    public String getNakshatra() {
        return nakshatra;
    }

    public void setNakshatra(String nakshatra) {
        this.nakshatra = nakshatra;
    }

    public String getNakshatraValue() {
        return nakshatraValue;
    }

    public void setNakshatraValue(String nakshatraValue) {
        this.nakshatraValue = nakshatraValue;
    }

    public String getNakshatraTime() {
        return nakshatraTime;
    }

    public void setNakshatraTime(String nakshatraTime) {
        this.nakshatraTime = nakshatraTime;
    }

    public String getKarana() {
        return karana;
    }

    public void setKarana(String karana) {
        this.karana = karana;
    }

    public String getKaranaValue() {
        return karanaValue;
    }

    public void setKaranaValue(String karanaValue) {
        this.karanaValue = karanaValue;
    }

    public String getKaranaTime() {
        return karanaTime;
    }

    public void setKaranaTime(String karanaTime) {
        this.karanaTime = karanaTime;
    }

    public String getPakshaName() {
        return pakshaName;
    }

    public void setPakshaName(String pakshaName) {
        this.pakshaName = pakshaName;
    }

    public String getYoga() {
        return yoga;
    }

    public void setYoga(String yoga) {
        this.yoga = yoga;
    }

    public String getYogaValue() {
        return yogaValue;
    }

    public void setYogaValue(String yogaValue) {
        this.yogaValue = yogaValue;
    }

    public String getYogaTime() {
        return yogaTime;
    }

    public void setYogaTime(String yogaTime) {
        this.yogaTime = yogaTime;
    }

    public String getVaara() {
        return vaara;
    }

    public void setVaara(String vaara) {
        this.vaara = vaara;
    }

    public String getShakaSamvat() {
        return shakaSamvat;
    }

    public void setShakaSamvat(String shakaSamvat) {
        this.shakaSamvat = shakaSamvat;
    }

    public String getShakaSamvatName() {
        return shakaSamvatName;
    }

    public void setShakaSamvatName(String shakaSamvatName) {
        this.shakaSamvatName = shakaSamvatName;
    }

    public String getShakaSamvatYear() {
        return shakaSamvatYear;
    }

    public void setShakaSamvatYear(String shakaSamvatYear) {
        this.shakaSamvatYear = shakaSamvatYear;
    }

    public String getVikramSamvat() {
        return vikramSamvat;
    }

    public void setVikramSamvat(String vikramSamvat) {
        this.vikramSamvat = vikramSamvat;
    }

    public String getKaliSamvat() {
        return kaliSamvat;
    }

    public void setKaliSamvat(String kaliSamvat) {
        this.kaliSamvat = kaliSamvat;
    }

    public String getPanchang() {
        return panchang;
    }

    public void setPanchang(String panchang) {
        this.panchang = panchang;
    }

    public String getDayDuration() {
        return dayDuration;
    }

    public void setDayDuration(String dayDuration) {
        this.dayDuration = dayDuration;
    }

    public String getAbhijit() {
        return abhijit;
    }

    public void setAbhijit(String abhijit) {
        this.abhijit = abhijit;
    }

    public String getAbhijitFrom() {
        return abhijitFrom;
    }

    public void setAbhijitFrom(String abhijitFrom) {
        this.abhijitFrom = abhijitFrom;
    }

    public String getAbhijitTo() {
        return abhijitTo;
    }

    public void setAbhijitTo(String abhijitTo) {
        this.abhijitTo = abhijitTo;
    }

    public String getKulika() {
        return kulika;
    }

    public void setKulika(String kulika) {
        this.kulika = kulika;
    }

    public String getKantaka_Mrityu() {
        return kantaka_Mrityu;
    }

    public void setKantaka_Mrityu(String kantaka_Mrityu) {
        this.kantaka_Mrityu = kantaka_Mrityu;
    }

    public String getKantaka_MrityuFrom() {
        return kantaka_MrityuFrom;
    }

    public void setKantaka_MrityuFrom(String kantaka_MrityuFrom) {
        this.kantaka_MrityuFrom = kantaka_MrityuFrom;
    }

    public String getKantaka_MrityuTo() {
        return kantaka_MrityuTo;
    }

    public void setKantaka_MrityuTo(String kantaka_MrityuTo) {
        this.kantaka_MrityuTo = kantaka_MrityuTo;
    }

    public String getKalavela_Ardhayaam() {
        return kalavela_Ardhayaam;
    }

    public void setKalavela_Ardhayaam(String kalavela_Ardhayaam) {
        this.kalavela_Ardhayaam = kalavela_Ardhayaam;
    }

    public String getYamaghanta() {
        return yamaghanta;
    }

    public void setYamaghanta(String yamaghanta) {
        this.yamaghanta = yamaghanta;
    }

    public String getYamaghantaFrom() {
        return yamaghantaFrom;
    }

    public void setYamaghantaFrom(String yamaghantaFrom) {
        this.yamaghantaFrom = yamaghantaFrom;
    }

    public String getYamaghantaTo() {
        return yamaghantaTo;
    }

    public void setYamaghantaTo(String yamaghantaTo) {
        this.yamaghantaTo = yamaghantaTo;
    }

    public String getDushtaMuhurtas() {
        return dushtaMuhurtas;
    }

    public void setDushtaMuhurtas(String dushtaMuhurtas) {
        this.dushtaMuhurtas = dushtaMuhurtas;
    }

    public String getDushtaMuhurtasFrom() {
        return dushtaMuhurtasFrom;
    }

    public void setDushtaMuhurtasFrom(String dushtaMuhurtasFrom) {
        this.dushtaMuhurtasFrom = dushtaMuhurtasFrom;
    }

    public String getDushtaMuhurtasTo() {
        return dushtaMuhurtasTo;
    }

    public void setDushtaMuhurtasTo(String dushtaMuhurtasTo) {
        this.dushtaMuhurtasTo = dushtaMuhurtasTo;
    }

    public String getRahuKaalVela() {
        return rahuKaalVela;
    }

    public void setRahuKaalVela(String rahuKaalVela) {
        this.rahuKaalVela = rahuKaalVela;
    }

    public String getGulikaKaalVela() {
        return gulikaKaalVela;
    }

    public void setGulikaKaalVela(String gulikaKaalVela) {
        this.gulikaKaalVela = gulikaKaalVela;
    }

    public String getYamagandaVela() {
        return yamagandaVela;
    }

    public void setYamagandaVela(String yamagandaVela) {
        this.yamagandaVela = yamagandaVela;
    }

    public String getDishaShoola() {
        return dishaShoola;
    }

    public void setDishaShoola(String dishaShoola) {
        this.dishaShoola = dishaShoola;
    }

    public String getTaraBala() {
        return taraBala;
    }

    public void setTaraBala(String taraBala) {
        this.taraBala = taraBala;
    }

    public String getChandraBala() {
        return chandraBala;
    }

    public void setChandraBala(String chandraBala) {
        this.chandraBala = chandraBala;
    }

    public String getPageDailypanchangH1() {
        return pageDailypanchangH1;
    }

    public void setPageDailypanchangH1(String pageDailypanchangH1) {
        this.pageDailypanchangH1 = pageDailypanchangH1;
    }

    public String getPageTitleContent() {
        return pageTitleContent;
    }

    public void setPageTitleContent(String pageTitleContent) {
        this.pageTitleContent = pageTitleContent;
    }

    public String getPageDescriptionContent() {
        return pageDescriptionContent;
    }

    public void setPageDescriptionContent(String pageDescriptionContent) {
        this.pageDescriptionContent = pageDescriptionContent;
    }

    public String getPageKeywordContent() {
        return pageKeywordContent;
    }

    public void setPageKeywordContent(String pageKeywordContent) {
        this.pageKeywordContent = pageKeywordContent;
    }

    public String getMonthAmanta() {
        return monthAmanta;
    }

    public void setMonthAmanta(String monthAmanta) {
        this.monthAmanta = monthAmanta;
    }

    public String getMonthPurnimanta() {
        return monthPurnimanta;
    }

    public void setMonthPurnimanta(String monthPurnimanta) {
        this.monthPurnimanta = monthPurnimanta;
    }

    public double getSunRiseDouble() {
        return sunRiseDouble;
    }

    public void setSunRiseDouble(double sunRiseDouble) {
        this.sunRiseDouble = sunRiseDouble;
    }

    public double getSunSetDouble() {
        return sunSetDouble;
    }

    public void setSunSetDouble(double sunSetDouble) {
        this.sunSetDouble = sunSetDouble;
    }
}
