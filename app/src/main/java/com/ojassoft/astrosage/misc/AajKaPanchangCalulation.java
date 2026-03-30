package com.ojassoft.astrosage.misc;

////import com.google.analytics.tracking.android.Log;

import android.util.Log;

import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.jinterface.IConstants;
import com.ojassoft.astrosage.utils.PanchangUtil;
import com.ojassoft.panchang.CMoon;
import com.ojassoft.panchang.Masa;
import com.ojassoft.panchang.Muhurta;
import com.ojassoft.panchang.Place;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.ojassoft.panchang.cloudsql.service.FindPlaces;
//import com.ojassoft.panchang.dao.Language;
//import com.ojassoft.panchang.dao.MyPlace;
//import com.ojassoft.panchang.utils.PanchangUtil;

public class AajKaPanchangCalulation {

    public AajKaPanchangCalulation(Date _datePan, String lang, String lat, String lng, String tz, String timeZoneString) {
        datePan = _datePan;
        //placeID = _placeID;

        this.timezoneString = timeZoneString;

        calendarPan = Calendar.getInstance();
        calendarPan.setTime(datePan);
        datePanJd = (int) Masa.toJulian(calendarPan.get(Calendar.YEAR),
                calendarPan.get(Calendar.MONTH) + 1,
                calendarPan.get(Calendar.DAY_OF_MONTH));
        objMasa = new Masa();
        objCMoon = new CMoon();
        objPanchangUtil = new PanchangUtil();
        objAajKaPanchangModel = new AajKaPanchangModel();
        languageCode = Language.getLanguage(lang);
        constants = objPanchangUtil.getIConstantsObj(languageCode);


        // ***************** CODE to fetch place info from id ***************

        latitude = Float.valueOf(lat);
        longitude = Float.valueOf(lng);
        this.timezone = Float.valueOf(tz);
        objAajKaPanchangModel.setIsPlaceValid(true);

        if (objPanchangUtil.isDst(this.timezoneString, datePan))
            timezone = timezone + 1.0;
        place = new Place(latitude, longitude, timezone);
        // }

        // ************************* END **********************************

    }


    public AajKaPanchangCalulation(Date _datePan, String _placeID, String lang, String lat, String lng, String tz, String timeZoneString) {
        datePan = _datePan;
        placeID = _placeID;

        this.timezoneString = timeZoneString;

        calendarPan = Calendar.getInstance();
        calendarPan.setTime(datePan);
        datePanJd = (int) Masa.toJulian(calendarPan.get(Calendar.YEAR),
                calendarPan.get(Calendar.MONTH) + 1,
                calendarPan.get(Calendar.DAY_OF_MONTH));
        objMasa = new Masa();
        objCMoon = new CMoon();
        objPanchangUtil = new PanchangUtil();
        objAajKaPanchangModel = new AajKaPanchangModel();
        languageCode = Language.getLanguage(lang);
        constants = objPanchangUtil.getIConstantsObj(languageCode);


        // ***************** CODE to fetch place info from id ***************
        if (placeID.equals("1261481")) {
            latitude = 28.6139;
            longitude = 77.2090;
            timezone = +5.5;
            strPlace = "New Delhi";
            strCountry = "India";
            strState = "NCT";
            timezoneString = "Asia/Kolkata";
            if (objPanchangUtil.isDst(timezoneString, datePan)) {
                timezone = timezone + 1.0;
            }
            place = new Place(latitude, longitude, timezone);
            //place = new Place(28.6139, 77.2090, +5.5); // defualt new Delhi
            objAajKaPanchangModel.setIsPlaceValid(false);
        } else {/*
            FindPlaces findplace = new FindPlaces();
			String JSONplace = findplace.findPlaceInfoById(placeID);
			Gson gsonService = new Gson();
			List<MyPlace> currePlace = gsonService.fromJson(JSONplace,
					new TypeToken<List<MyPlace>>() {
					}.getType());

			if (currePlace.size() != 0)
				for (MyPlace mplace : currePlace) {
					latitude = Double.parseDouble(mplace.getLatitude());
					longitude = Double.parseDouble(mplace.getLongitude());
					timezone = Double.parseDouble(mplace.getTimezone());
					strPlace = mplace.getPlace();
					strCountry = mplace.getCountry();
					strState = mplace.getState();
					timezoneString = mplace.getTimezonestring();
			         objAajKaPanchangModel.setIsPlaceValid(true);
				}
			else {
				latitude = 28.6139;
				longitude = 77.2090;
				timezone = +5.5;
				strPlace = "New Delhi";
				strCountry = "India";
				strState = "NCT";
				timezoneString = "Asia/Kolkata";
				place = new Place(28.6139, 77.2090, +5.5);  // defualt new Delhi
				objAajKaPanchangModel.setIsPlaceValid(false);
			}
			 if(objPanchangUtil.isDst(timezoneString, datePan)){
	        	 timezone =timezone+1.0;}
	         place = new Place(latitude, longitude, timezone);

		*/
            if (!lat.equals("0") & !lng.equals("0")) {
                latitude = Float.valueOf(lat);
                longitude = Float.valueOf(lng);
                this.timezone = Float.valueOf(tz);
                objAajKaPanchangModel.setIsPlaceValid(true);
                //Log.e("tag","1" + lat);
                //Log.e("tag","1" + lng);
                //Log.e("tag","1" + tz);

                //Log.e("tag","2" + latitude);
                //Log.e("tag","2" + longitude);
                //Log.e("tag","2" + timezone);

            } else {
                latitude = 28.6139;
                longitude = 77.2090;
                timezone = +5.5;
                strPlace = "New Delhi";
                strCountry = "India";
                strState = "NCT";
                timezoneString = "Asia/Kolkata";
                //place = new Place(28.6139, 77.2090, +5.5); // defualt new Delhi
                objAajKaPanchangModel.setIsPlaceValid(false);
            }
            if (objPanchangUtil.isDst(this.timezoneString, datePan))
                timezone = timezone + 1.0;
            place = new Place(latitude, longitude, timezone);
        }

        // ************************* END **********************************

    }

    String strPlace, strState, strCountry, timeZoneString;
    Calendar calendarPan;
    Date datePan;
    String placeID;
    int datePanJd;
    Masa objMasa;
    Place place;
    CMoon objCMoon;
    PanchangUtil objPanchangUtil;
    AajKaPanchangModel objAajKaPanchangModel;
    Language languageCode;
    IConstants constants;
    double latitude;
    double longitude;
    double timezone;
    String timezoneString;
    final int fetchCompleteData = 1;
    final int fetchOnlyValue = 2;
    final int fetchOnlyTime = 3;

    public AajKaPanchangModel getPanchang() {
        //getPageTitleHeadinginfo();
        getPanchangMasa();
        getPanchangMuhurat();
        return objAajKaPanchangModel;
    }

    public void getPanchangMasa() {
        double sunriseval = Masa.getSunRise(datePanJd, place);
        double sunsetval = Masa.getSunSet(datePanJd, place);
        double nextdaysunrise = Masa.getSunRise(datePanJd + 1, place);
        //int[] masa_moon = objMasa.masa(datePanJd, place);
        int[] masa_moon = objMasa.masaPurnimanta(datePanJd, place);
        int[] kalishakavikramsamvat = objMasa.elapsed_year(datePanJd, place, masa_moon[0]);
        double[] moonrisesetval = objCMoon.getMoonRiseSetTime(datePanJd, place);
        double[] MoonSign = objMasa.moonsign(datePanJd, place);
        // double[] Tith = objMasa.tithi(datePanJd, place);
        // double[] Naksh = objMasa.nakshatra(datePanJd, place);
        // double[] Yog = objMasa.yoga(datePanJd, place);
        // double[] Karan = objMasa.karana(datePanJd, place);
        objAajKaPanchangModel.setSunRiseDouble(sunriseval);
        objAajKaPanchangModel.setSunSetDouble(sunsetval);
        objAajKaPanchangModel.setSunRise(objPanchangUtil.dms(sunriseval));
        objAajKaPanchangModel.setSunSet(objPanchangUtil.dms(sunsetval));
        objAajKaPanchangModel
                .setMoonRise(moonrisesetval[0] != 0.0 ? GetHoverString(moonrisesetval[0])
                        : constants.getExString(1));
        objAajKaPanchangModel
                .setMoonSet(moonrisesetval[2] != 0.0 ? GetHoverString(moonrisesetval[2])
                        : constants.getExString(2));
        objAajKaPanchangModel.setRitu(constants.getRitus(objMasa.ritu_drik(datePanJd, place)));

        objAajKaPanchangModel.setShakaSamvatName(constants.getSamvats(objMasa.samvatsara(datePanJd, place, masa_moon[0], false)));

        if (masa_moon[1] == 1) {
            objAajKaPanchangModel.setMonthAmanta(constants.getMasas(masa_moon[0] - 1)
                    + " " + constants.getExString(3));
            objAajKaPanchangModel.setMonthPurnimanta(constants.getMasas(masa_moon[2] - 1)
                    + " " + constants.getExString(3));
        } else {
            objAajKaPanchangModel
                    .setMonthAmanta(constants.getMasas(masa_moon[0] - 1));
            objAajKaPanchangModel
                    .setMonthPurnimanta(constants.getMasas(masa_moon[2] - 1));
        }

        objAajKaPanchangModel.setPakshaName(constants.getPakshas((objMasa
                .getPaksha(datePanJd, place))));
        objAajKaPanchangModel
                .setVaara(constants.getVaras(Masa.vaara(datePanJd)));
        objAajKaPanchangModel.setKaliSamvat(kalishakavikramsamvat[3] + "");
        objAajKaPanchangModel.setShakaSamvatYear(kalishakavikramsamvat[1] + "");
        objAajKaPanchangModel.setVikramSamvat(kalishakavikramsamvat[2] + "");

        /*
         * Date : 25-Sep-2015
         * Getting MoonSign
         * */
        if (MoonSign[1] > (sunriseval + 24.00000)) {
            String nameMoonSign = GetDay(MoonSign[0], "MoonSign");
            objAajKaPanchangModel.setMoonSign(nameMoonSign);
            objAajKaPanchangModel.setMoonSignValue(nameMoonSign);
        } else {
            objAajKaPanchangModel.setMoonSign(GetString(MoonSign, "MoonSign", nextdaysunrise, fetchCompleteData));
            objAajKaPanchangModel.setMoonSignValue(GetString(MoonSign, "MoonSign", nextdaysunrise, fetchOnlyValue));
            objAajKaPanchangModel.setMoonSignTime(GetString(MoonSign, "MoonSign", nextdaysunrise, fetchOnlyTime));
        }

        /*
         * Date : 25-Sep-2015
         * Getting Tithi
         * */
        double[] tithiData = objMasa.tithi(datePanJd, place);
        objAajKaPanchangModel.setTithiInt(tithiData);
        objAajKaPanchangModel.setTithi(GetString(tithiData, "Tith", nextdaysunrise, fetchCompleteData));
        objAajKaPanchangModel.setTithiValue(GetString(tithiData, "Tith", nextdaysunrise, fetchOnlyValue));
        objAajKaPanchangModel.setTithiTime(GetString(tithiData, "Tith", nextdaysunrise, fetchOnlyTime));
        /*
         * Date : 25-Sep-2015
         * Getting Nakshatra
         * */
        double[] nakshatraData = objMasa.nakshatra(datePanJd, place);
        objAajKaPanchangModel.setNakshatra(GetString(nakshatraData, "Naksh", nextdaysunrise, fetchCompleteData));
        objAajKaPanchangModel.setNakshatraValue(GetString(nakshatraData, "Naksh", nextdaysunrise, fetchOnlyValue));
        objAajKaPanchangModel.setNakshatraTime(GetString(nakshatraData, "Naksh", nextdaysunrise, fetchOnlyTime));
        /*
         * Date : 25-Sep-2015
         * Getting Karana
         * */
        double[] karanaData = objMasa.karana(datePanJd, place);
        objAajKaPanchangModel.setKarana(GetString(karanaData, "Karan", nextdaysunrise, fetchCompleteData));
        objAajKaPanchangModel.setKaranaValue(GetString(karanaData, "Karan", nextdaysunrise, fetchOnlyValue));
        objAajKaPanchangModel.setKaranaTime(GetString(karanaData, "Karan", nextdaysunrise, fetchOnlyTime));
        /*
         * Date : 25-Sep-2015
         * Getting Yoga
         * */
        double[] yogaData = objMasa.yoga(datePanJd, place);
        objAajKaPanchangModel.setYoga(GetString(yogaData, "Yog", nextdaysunrise, fetchCompleteData));
        objAajKaPanchangModel.setYogaValue(GetString(yogaData, "Yog", nextdaysunrise, fetchOnlyValue));
        objAajKaPanchangModel.setYogaTime(GetString(yogaData, "Yog", nextdaysunrise, fetchOnlyTime));
    }

    public void getPanchangMuhurat() {
        double[] fifteenMuhurtas = Muhurta.getFifteenMuhurtaForDay(datePanJd,
                place);
        int[] kulikaetc = Muhurta
                .getKulikaKantakaKalavelaYama(datePanJd, place);
        int[] dushtaMuhurtas = Muhurta.getDushtaMuhurta(datePanJd, place);
        double[] eightDivisions = Muhurta.getDayDivisons(datePanJd, place,
                Muhurta.getSunRise(datePanJd, place), 8);
        int[] rahuetc = Muhurta.getRahuYamaGulikaKaal(datePanJd, place);

        objAajKaPanchangModel.setDayDuration(objPanchangUtil.dms(Muhurta
                .dayDuration(datePanJd, place)));
        if (Masa.vaara(datePanJd) != 3) // For Wednesday
        {
            objAajKaPanchangModel.setAbhijit(GetFromToString(
                    objPanchangUtil.dms(fifteenMuhurtas[8 - 1]),
                    objPanchangUtil.dms(fifteenMuhurtas[8])));
            /*
             * Date : 25-Sep-2015
             * Getting Abhijit
             * */
            objAajKaPanchangModel.setAbhijitFrom(objPanchangUtil.dms(fifteenMuhurtas[8 - 1]));
            objAajKaPanchangModel.setAbhijitTo(objPanchangUtil.dms(fifteenMuhurtas[8]));
        } else {
            objAajKaPanchangModel.setAbhijit(constants.getExString(0));
            /*
             * Date : 25-Sep-2015
             * Getting Abhijit
             * */
            objAajKaPanchangModel.setAbhijitFrom(constants.getExString(0));
            objAajKaPanchangModel.setAbhijitTo("");
        }
        /*
         * Date : 25-Sep-2015
         * Getting Kulika
         * */
        String fromKulika = objPanchangUtil.dms(fifteenMuhurtas[kulikaetc[0] - 1]);
        String toKulika = objPanchangUtil.dms(fifteenMuhurtas[kulikaetc[0]]);
        objAajKaPanchangModel.setKulika(GetFromToString(fromKulika, toKulika));
        objAajKaPanchangModel.setKulikaFrom(fromKulika);
        objAajKaPanchangModel.setKulikaTo(toKulika);

        /*
         * Date : 25-Sep-2015
         * Getting Kantaka_Mrityu
         * */
        String fromKantaka_Mrityu = objPanchangUtil.dms(fifteenMuhurtas[kulikaetc[1] - 1]);
        String toKantaka_Mrityu = objPanchangUtil.dms(fifteenMuhurtas[kulikaetc[1]]);
        objAajKaPanchangModel.setKantaka_Mrityu(GetFromToString(fromKantaka_Mrityu, toKantaka_Mrityu));
        objAajKaPanchangModel.setKantaka_MrityuFrom(fromKantaka_Mrityu);
        objAajKaPanchangModel.setKantaka_MrityuTo(toKantaka_Mrityu);

        /*
         * Date : 25-Sep-2015
         * Getting Kalavela_Ardhayaam
         * */
        String fromKalavela_Ardhayaam = objPanchangUtil.dms(fifteenMuhurtas[kulikaetc[2] - 1]);
        String toKalavela_Ardhayaam = objPanchangUtil.dms(fifteenMuhurtas[kulikaetc[2]]);
        objAajKaPanchangModel.setKalavela_Ardhayaam(GetFromToString(fromKalavela_Ardhayaam, toKalavela_Ardhayaam));
        objAajKaPanchangModel.setKalavela_ArdhayaamFrom(fromKalavela_Ardhayaam);
        objAajKaPanchangModel.setKalavela_ArdhayaamTo(toKalavela_Ardhayaam);

        /*
         * Date : 25-Sep-2015
         * Getting Yamaghanta
         * */
        String fromYamaghanta = objPanchangUtil.dms(fifteenMuhurtas[kulikaetc[3] - 1]);
        String toYamaghanta = objPanchangUtil.dms(fifteenMuhurtas[kulikaetc[3]]);
        objAajKaPanchangModel.setYamaghanta(GetFromToString(fromYamaghanta, toYamaghanta));
        objAajKaPanchangModel.setYamaghantaFrom(fromYamaghanta);
        objAajKaPanchangModel.setYamaghantaTo(toYamaghanta);

        if (dushtaMuhurtas.length > 1) {
            /*
             * Date : 25-Sep-2015
             * Getting DushtaMuhurtas
             * */
            String from1 = objPanchangUtil.dms(fifteenMuhurtas[dushtaMuhurtas[0] - 1]);
            String to1 = objPanchangUtil.dms(fifteenMuhurtas[dushtaMuhurtas[0]]);
            String from2 = objPanchangUtil.dms(fifteenMuhurtas[dushtaMuhurtas[1] - 1]);
            String to2 = objPanchangUtil.dms(fifteenMuhurtas[dushtaMuhurtas[1]]);

            objAajKaPanchangModel
                    .setDushtaMuhurtas(GetFromToString(from1, to1)
                            + ", "
                            + GetFromToString(from2, to2));

            objAajKaPanchangModel.setDushtaMuhurtasFrom(from1 + "\n" + from2);
            objAajKaPanchangModel.setDushtaMuhurtasTo(to1 + ",\n" + to2);

        } else {

            /*
             * Date : 25-Sep-2015
             * Getting DushtaMuhurtas
             * */
            String from1 = objPanchangUtil.dms(fifteenMuhurtas[dushtaMuhurtas[0] - 1]);
            String to1 = objPanchangUtil.dms(fifteenMuhurtas[dushtaMuhurtas[0]]);

            objAajKaPanchangModel.setDushtaMuhurtas(GetFromToString(from1, to1));
            objAajKaPanchangModel.setDushtaMuhurtasFrom(from1);
            objAajKaPanchangModel.setDushtaMuhurtasTo(to1);
        }
        /*
         * Date : 25-Sep-2015
         * Getting RahuKaalVela
         * */
        String fromRahuKaalVela = objPanchangUtil.dms(eightDivisions[rahuetc[0] - 1]);
        String toRahuKaalVela = objPanchangUtil.dms(eightDivisions[rahuetc[0]]);
        objAajKaPanchangModel.setRahuKaalVela(GetFromToString(fromRahuKaalVela, toRahuKaalVela));
        objAajKaPanchangModel.setRahuKaalVelaFrom(fromRahuKaalVela);
        objAajKaPanchangModel.setRahuKaalVelaTo(toRahuKaalVela);

        /*
         * Date : 25-Sep-2015
         * Getting GulikaKaalVela
         * */
        String fromGulikaKaalVela = objPanchangUtil.dms(eightDivisions[rahuetc[1] - 1]);
        String toGulikaKaalVela = objPanchangUtil.dms(eightDivisions[rahuetc[1]]);
        objAajKaPanchangModel.setGulikaKaalVela(GetFromToString(fromGulikaKaalVela, toGulikaKaalVela));
        objAajKaPanchangModel.setGulikaKaalVelaFrom(fromGulikaKaalVela);
        objAajKaPanchangModel.setGulikaKaalVelaTo(toGulikaKaalVela);

        /*
         * Date : 25-Sep-2015
         * Getting YamagandaVela
         * */
        String fromYamagandaVela = objPanchangUtil.dms(eightDivisions[rahuetc[2] - 1]);
        String toYamagandaVela = objPanchangUtil.dms(eightDivisions[rahuetc[2]]);
        objAajKaPanchangModel.setYamagandaVela(GetFromToString(fromYamagandaVela, toYamagandaVela));
        objAajKaPanchangModel.setYamagandaVelaFrom(fromYamagandaVela);
        objAajKaPanchangModel.setYamagandaVelaTo(toYamagandaVela);


        objAajKaPanchangModel.setDishaShoola(constants.getDishas(Muhurta
                .getDishaShoola(datePanJd) - 1));
        objAajKaPanchangModel.setTaraBala(GetMergeString(
                Muhurta.getTaraBaliNakshatra((int) objMasa.nakshatra(datePanJd,
                        place)[0]), constants.getTaraBala()));
        objAajKaPanchangModel.setChandraBala(GetMergeString(
                Muhurta.getChandraBaliRasi((int) objMasa.moonsign(datePanJd,
                        place)[0]), constants.getChandraBala()));

        // this.Muhurtas_15_1 =
        // GetMergeString(Muhurta.getFifteenMuhurtaForDay(datePanJd, place));
        // this.Muhurtas_15_2 = GetMergeString(Muhurta.getDayDivisons(datePanJd,
        // place, Muhurta.getSunRise(datePanJd, place), 15));
        // this.Horas_8 = GetMergeString(Muhurta.getDayDivisons(datePanJd,
        // place, Muhurta.getSunRise(datePanJd, place), 8));

    }

    public void getPageTitleHeadinginfo() {

        Calendar calnPan = Calendar.getInstance();
        calnPan.setTime(datePan);

        String month, date, year, day, pageTitle, DescriptionContent, keywordContent, dailypanchangh1;
        day = constants.getDayName(calnPan.get(Calendar.DAY_OF_WEEK));
        month = constants.getMonthName(calnPan.get(Calendar.MONTH) + 1);
        year = calnPan.get(Calendar.YEAR) + "";
        date = calnPan.get(Calendar.DAY_OF_MONTH) + "";
        pageTitle = "";
        DescriptionContent = "";
        keywordContent = "";
        dailypanchangh1 = "";

        /*
         * strPlace = "Delhi"; strState = "New Delhi"; strCountry ="India";
         */

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date curentDate = new Date();
        String panDate = formatter.format(datePan);
        String todayDate = formatter.format(curentDate);

        if (panDate.equals(todayDate)) {

            try {
                switch (languageCode) {
                    case hi:

                        pageTitle = "दैनिक पंचांग - आज का पंचांग" + " " + day
                                + ",  " + month + " " + date + ", " + year + " को "
                                + strPlace + ", " + strState + ", " + strCountry
                                + " के लिए ";
                        DescriptionContent = " आज का पंचांग और दैनिक हिन्‍दू कैलेंडर भारत की नं. 1 वेबसाइट एस्‍ट्रोसेज द्वारा निर्मित "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " का "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry + " के लिए";
                        keywordContent = "पंचांग, पञ्चाङ्ग, पञ्चाङ्गम, हिन्‍दी पंचांग "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " का पंचांग";
                        dailypanchangh1 = "<h1 align='center' > आज का पंचांग  </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " का पंचांग  "
                                + strPlace + ", " + strCountry + " के लिए</h2>";
                        break;

                    case ta:
                        pageTitle = "Aaj Ka Panchang: " + day + ",  " + month + " "
                                + date + ", " + year + " Panchangam today for "
                                + strPlace + ", " + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang or Aaj Ka Panchang  for "
                                + " "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchangam today in [English] for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " and also showing muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center' > Aaj Ka Panchang - Panchangam Today </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchang for "
                                + strPlace + ", " + strCountry + "</h2>";
                        break;
                    case te:
                        pageTitle = "Aaj Ka Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang or Aaj Ka Panchang  for "
                                + " "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchangam today in [English] for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " and also showing muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center' > Aaj Ka Panchang - Panchangam Today </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchang for "
                                + strPlace + ", " + strCountry + "</h2>";
                        break;
                    case ka:
                        pageTitle = "Aaj Ka Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang or Aaj Ka Panchang  for "
                                + " "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchangam today in [English] for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " and also showing muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center' > Aaj Ka Panchang - Panchangam Today </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchang for "
                                + strPlace + ", " + strCountry + "</h2>";
                        break;
                    case ml:
                        pageTitle = "Aaj Ka Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang or Aaj Ka Panchang  for "
                                + " "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchangam today in [English] for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " and also showing muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center' > Aaj Ka Panchang - Panchangam Today </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchang for "
                                + strPlace + ", " + strCountry + "</h2>";
                        break;
                    case gu:
                        pageTitle = "Aaj Ka Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang or Aaj Ka Panchang  for "
                                + " "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchangam today in [English] for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " and also showing muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center' > Aaj Ka Panchang - Panchangam Today </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchang for "
                                + strPlace + ", " + strCountry + "</h2>";
                        break;
                    case mr:
                        pageTitle = "Aaj Ka Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang or Aaj Ka Panchang  for "
                                + " "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchangam today in [English] for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " and also showing muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center' > Aaj Ka Panchang - Panchangam Today </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchang for "
                                + strPlace + ", " + strCountry + "</h2>";
                        break;
                    case bn:
                        pageTitle = "Aaj Ka Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang or Aaj Ka Panchang  for "
                                + " "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchangam today in [English] for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " and also showing muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center' > Aaj Ka Panchang - Panchangam Today </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchang for "
                                + strPlace + ", " + strCountry + "</h2>";
                        break;
                    case en:
                    default:

                        pageTitle = "Aaj Ka Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang or Aaj Ka Panchang  for "
                                + " "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchangam today in [English] for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " and also showing muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center' > Aaj Ka Panchang - Panchangam Today </h1> "
                                + "<h2 align='center'> "
                                + day
                                + ",  "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " Panchang for "
                                + strPlace + ", " + strCountry + "</h2>";
                        break;
                }

                objAajKaPanchangModel.setPageTitleContent(pageTitle);
                objAajKaPanchangModel
                        .setPageDescriptionContent(DescriptionContent);
                objAajKaPanchangModel.setPageKeywordContent(keywordContent);
                objAajKaPanchangModel.setPageDailypanchangH1(dailypanchangh1);

            } catch (Exception e) {
            }
        } else {
            try {
                switch (languageCode) {
                    case hi:

                        pageTitle = "दैनिक पंचांग" + " " + day + ",  " + month
                                + " " + date + ", " + year + " को " + strPlace + ", "
                                + strState + ", " + strCountry + " के लिए ";
                        DescriptionContent = "  दैनिक पंचांग और दैनिक हिन्‍दू कैलेंडर "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " के लिए भारत की नं. 1 वेबसाइट एस्‍ट्रोसेज द्वारा निर्मित ।";
                        keywordContent = "पंचांग, पञ्चाङ्ग, पञ्चाङ्गम, हिन्‍दी पंचांग "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " का पंचांग";
                        dailypanchangh1 = "<h1 align='center'>  दैनिक पंचांग - "
                                + day + ", " + month + " " + date + ", " + year
                                + " for " + strPlace + ", " + strCountry
                                + " के लिए" + "</h1>";
                        break;
                    case ta:
                        pageTitle = "Daily Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang for " + " " + day
                                + ",  " + month + " " + date + ", " + year
                                + " Panchangam today in English for " + strPlace
                                + ", " + strState + ", " + strCountry
                                + " and also shows muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center'> Daily Panchang: Panchang of "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " for " + strPlace + ", " + strCountry + "</h1>";
                        break;
                    case te:
                        pageTitle = "Daily Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang for " + " " + day
                                + ",  " + month + " " + date + ", " + year
                                + " Panchangam today in English for " + strPlace
                                + ", " + strState + ", " + strCountry
                                + " and also shows muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center'> Daily Panchang: Panchang of "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " for " + strPlace + ", " + strCountry + "</h1>";
                        break;
                    case ka:
                        pageTitle = "Daily Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang for " + " " + day
                                + ",  " + month + " " + date + ", " + year
                                + " Panchangam today in English for " + strPlace
                                + ", " + strState + ", " + strCountry
                                + " and also shows muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center'> Daily Panchang: Panchang of "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " for " + strPlace + ", " + strCountry + "</h1>";
                        break;
                    case ml:
                        pageTitle = "Daily Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang for " + " " + day
                                + ",  " + month + " " + date + ", " + year
                                + " Panchangam today in English for " + strPlace
                                + ", " + strState + ", " + strCountry
                                + " and also shows muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center'> Daily Panchang: Panchang of "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " for " + strPlace + ", " + strCountry + "</h1>";
                        break;
                    case gu:
                        pageTitle = "Daily Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang for " + " " + day
                                + ",  " + month + " " + date + ", " + year
                                + " Panchangam today in English for " + strPlace
                                + ", " + strState + ", " + strCountry
                                + " and also shows muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center'> Daily Panchang: Panchang of "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " for " + strPlace + ", " + strCountry + "</h1>";
                        break;
                    case mr:
                        pageTitle = "Daily Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang for " + " " + day
                                + ",  " + month + " " + date + ", " + year
                                + " Panchangam today in English for " + strPlace
                                + ", " + strState + ", " + strCountry
                                + " and also shows muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center'> Daily Panchang: Panchang of "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " for " + strPlace + ", " + strCountry + "</h1>";
                        break;
                    case bn:
                        pageTitle = "Daily Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang for " + " " + day
                                + ",  " + month + " " + date + ", " + year
                                + " Panchangam today in English for " + strPlace
                                + ", " + strState + ", " + strCountry
                                + " and also shows muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center'> Daily Panchang: Panchang of "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " for " + strPlace + ", " + strCountry + "</h1>";
                        break;
                    case en:
                    default:

                        pageTitle = "Daily Panchang: " + " " + day + ",  " + month
                                + " " + date + ", " + year
                                + " Panchangam today for " + strPlace + ", "
                                + strState + ", " + strCountry;
                        DescriptionContent = "Get daily Panchang for " + " " + day
                                + ",  " + month + " " + date + ", " + year
                                + " Panchangam today in English for " + strPlace
                                + ", " + strState + ", " + strCountry
                                + " and also shows muhurat.";
                        keywordContent = "Panchang, Panchanga, Panchangam, Aaj Ka Panchang, Today Panchang, Panchangam Today, English Panchang, Panchang for "
                                + strPlace
                                + ", "
                                + strState
                                + ", "
                                + strCountry
                                + " Hindu Panchang";
                        dailypanchangh1 = "<h1 align='center'> Daily Panchang: Panchang of "
                                + day
                                + ", "
                                + month
                                + " "
                                + date
                                + ", "
                                + year
                                + " for " + strPlace + ", " + strCountry + "</h1>";
                        break;
                }

                objAajKaPanchangModel.setPageTitleContent(pageTitle);
                objAajKaPanchangModel
                        .setPageDescriptionContent(DescriptionContent);
                objAajKaPanchangModel.setPageKeywordContent(keywordContent);
                objAajKaPanchangModel.setPageDailypanchangH1(dailypanchangh1);

            } catch (Exception e) {
            }

        }
    }


    // ********************** String Formatter Start ************************

    public String GetString(double[] data, String stringtitle, double nextdaysunrise, int id) {
        String result = "";

        for (int i = 0; i < data.length; ) {
            switch (languageCode) {
                case hi:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " - " + this.dms(data[i + 1]) + " तक";
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='अगला  "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " - "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise)
						+ " तक";*/
                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " तक";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        //+ " तक";
                    }
                    break;

                case ta:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " - " + this.dms(data[i + 1]) + " வரை";
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='அடுத்த  "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " - "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise)
						+ " வரை";*/

                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " வரை";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        //+ " வரை";
                    }
                    break;
                case te:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " - " + this.dms(data[i + 1]) + " వరకు";
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='తరువాత   "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " - "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise)
						+ " వరకు";*/
                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " వరకు";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        //+ " வரை";
                    }
                    break;
                case ka:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " - " + this.dms(data[i + 1]) + " ವರೆಗೆ";
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='ಮುಂದಿನ "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " - "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise)
						+ " ವರೆಗೆ";*/

                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " ವರೆಗೆ";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        //+ " வரை";
                    }
                    break;
                case ml:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " - " + this.dms(data[i + 1]) + " കൊണ്ട്";
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='അടുത്ത  "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " - "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise)
						+ " കൊണ്ട്";*/

                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " കൊണ്ട്";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        //+ " வரை";
                    }

                    break;
                case gu:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " - " + this.dms(data[i + 1]) + " સુધી";
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='આગલા "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " - "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise)
						+ " સુધી";*/
                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " સુધી";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        //+ " வரை";
                    }
                    break;
                case mr:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " - " + this.dms(data[i + 1]) + " तक";
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='पुढील  "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " - "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise)
						+ " तक";*/
                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " तक";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        //+ " তক";
                    }


                    break;
                case bn:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " - " + this.dms(data[i + 1]) + " তক";
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='পরের  "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " - "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise)
						+ " তক";*/


                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " তক";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        //+ " তক";
                    }
                    break;
                case or:
                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " ପର୍ଯ୍ୟନ୍ତ";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                    }
                    break;
                case as:
                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " - "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise)
                                + " পৰ্য্যন্ত";
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle);
                    } else {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                    }
                    break;
                case en:
                default:
                    // result = (i == 0 ? "" : result + ", ") + GetDay(data[i],
                    // stringtitle) + " upto " + this.dms(data[i + 1]);
                    // result = (i == 0 ? "" : result + ", ") +
                    // "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='next "
                    // + GethoverDay(data[i] + 1.0, stringtitle) + "'><u>" +
                    // GetDay(data[i], stringtitle) + "</u></a>" + " upto " +
                    // GetHoverString(data[i + 1]);
				/*result = (i == 0 ? "" : result + ", ")
						+ "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title='next "
						+ GethoverDay(data[i] + 1.0, stringtitle) + "'><u>"
						+ GetDay(data[i], stringtitle) + "</u></a>" + " upto "
						+ GetHoverStringFullNight(data[i + 1], nextdaysunrise);*/
                    if (id == fetchCompleteData) {
                        result = (i == 0 ? "" : result + ",\n")
                                + GetDay(data[i], stringtitle) + " upto "
                                + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                    } else if (id == fetchOnlyValue) {
                        result = (i == 0 ? "" : result + ",\n") + GetDay(data[i], stringtitle);
                    } else {
                        //result = (i == 0 ? "" : result + ",\n")+" upto "+ GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                        result = (i == 0 ? "" : result + ",\n") + GetHoverStringFullNight(data[i + 1], nextdaysunrise);
                    }
                    break;
            }

            i += 2;
        }
        return result;
    }

    public String GetHoverString(double actualtime) {

        String result = "";
        if (actualtime < 24.0) {
            result = objPanchangUtil.dms(actualtime);
        } else {
            Date nextdate = objPanchangUtil.getAddDays(datePan, 1);
            Calendar calnPan = Calendar.getInstance();
            calnPan.setTime(nextdate);

            double actual12htime = actualtime - 24.0;
            String month, date, year, day;
            day = constants.getDayName(calnPan.get(Calendar.DAY_OF_WEEK));
            month = constants.getMonthName(calnPan.get(Calendar.MONTH) + 1);
            year = calnPan.get(Calendar.YEAR) + "";
            date = calnPan.get(Calendar.DAY_OF_MONTH) + "";

            switch (languageCode) {
                case hi:

				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> को "
						+ objPanchangUtil.dms(actual12htime)
						+ " बजे</p> '><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
				/*result = " "
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ " को "
						+ objPanchangUtil.dms(actual12htime)
						+ " बजे"
						+ objPanchangUtil.dms(actualtime) + "";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;

                case ta:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> செய்ய "
						+ objPanchangUtil.dms(actual12htime)
						+ " நேரம்</p> '><u>"
						+ objPanchangUtil.dms(actualtime)
						+ "</u></a>";*/
				
				/*result = " "
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ " செய்ய "
						+ objPanchangUtil.dms(actual12htime)
						+ " நேரம்"
						+ objPanchangUtil.dms(actualtime) + "";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case te:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> కు "
						+ objPanchangUtil.dms(actual12htime)
						+ " గంట</p> '><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case ka:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> ಗೆ "
						+ objPanchangUtil.dms(actual12htime)
						+ " ಗಂಟೆ</p> '><u>"
						+ objPanchangUtil.dms(actualtime)
						+ "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case ml:
			/*	result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> ലേക്ക് "
						+ objPanchangUtil.dms(actual12htime)
						+ " സമയ</p> '><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case gu:
			/*	result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> ના રોજ "
						+ objPanchangUtil.dms(actual12htime)
						+ " વાગ્યે</p> '><u>"
						+ objPanchangUtil.dms(actualtime)
						+ "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case mr:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> को "
						+ objPanchangUtil.dms(actual12htime)
						+ " बजे</p> '><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case bn:
			/*	result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> করার "
						+ objPanchangUtil.dms(actual12htime)
						+ " সময়</p> '><u>"
						+ objPanchangUtil.dms(actualtime)
						+ "</u></a>";*/

                    result = objPanchangUtil.dms(actualtime);
                    break;
                case en:
                default:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ objPanchangUtil.dms(actual12htime)
						+ "</p> <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p>'  ><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
            }
        }
        return result;

    }

    public String GetHoverStringFullNight(double actualtime,
                                          double nextdaysunrise) {
        String result = "";
        if (actualtime < 24.0) {
            result = objPanchangUtil.dms(actualtime);
        } else if (actualtime > nextdaysunrise + 24.00) {
            switch (languageCode) {
                case hi:
                    result = "पूर्ण रात्रि";
                    break;

                case ta:
                    result = "முழு இரவு";
                    break;
                case te:
                    result = "నిండా రాత్రి";
                    break;
                case ka:
                    result = "ಪೂರ್ಣ ರಾತ್ರಿ";
                    break;
                case ml:
                    result = "പൂർണ്ണ രാത്രി";
                    break;
                case gu:
                    result = "પૂર્ણ રાત્રિ";
                    break;
                case mr:
                    result = "पूर्ण रात्र";
                    break;
                case bn:
                    result = "পূর্ণ রাতে";
                    break;
                case or:
                    result = "ପୂର୍ଣ୍ଣ ରାତ୍ରି";
                    break;
                case as:
                    result = "সম্পূৰ্ণ ৰাতি";
                    break;
                case en:
                default:
                    result = "Full Night";
                    break;
            }
        } else {
            double actual12htime = actualtime - 24.0;
            String month, date, year, day;

            Date nextdate = objPanchangUtil.getAddDays(datePan, 1);
            Calendar calnPan = Calendar.getInstance();
            calnPan.setTime(nextdate);

            day = constants.getDayName(calnPan.get(Calendar.DAY_OF_WEEK));
            month = constants.getMonthName(calnPan.get(Calendar.MONTH) + 1);
            year = calnPan.get(Calendar.YEAR) + "";
            date = calnPan.get(Calendar.DAY_OF_MONTH) + "";

            switch (languageCode) {
                case hi:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> को "
						+ objPanchangUtil.dms(actual12htime)
						+ " बजे</p> '><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;

                case ta:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> செய்ய "
						+ objPanchangUtil.dms(actual12htime)
						+ " நேரம்</p> '><u>"
						+ objPanchangUtil.dms(actualtime)
						+ "</u></a>";*/

                    result = objPanchangUtil.dms(actualtime);
                    break;
                case te:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> కు "
						+ objPanchangUtil.dms(actual12htime)
						+ " గంట</p> '><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case ka:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> ಗೆ "
						+ objPanchangUtil.dms(actual12htime)
						+ " ಗಂಟೆ</p> '><u>"
						+ objPanchangUtil.dms(actualtime)
						+ "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case ml:
			/*	result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> ലേക്ക് "
						+ objPanchangUtil.dms(actual12htime)
						+ " സമയ</p> '><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case gu:
			/*	result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> ના રોજ "
						+ objPanchangUtil.dms(actual12htime)
						+ " વાગ્યે</p> '><u>"
						+ objPanchangUtil.dms(actualtime)
						+ "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case mr:
			/*	result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> को "
						+ objPanchangUtil.dms(actual12htime)
						+ " बजे</p> '><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/
                    result = objPanchangUtil.dms(actualtime);
                    break;
                case bn:
				/*result = "<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p> <p> করার "
						+ objPanchangUtil.dms(actual12htime)
						+ " সময়</p> '><u>"
						+ objPanchangUtil.dms(actualtime)
						+ "</u></a>";*/

                    result = objPanchangUtil.dms(actualtime);
                    break;
                case en:
                default:
				/*result ="<a href='javascript:void(0)' data-toggle='tooltip' data-html='true' title=' <p>"
						+ objPanchangUtil.dms(actual12htime)
						+ "</p> <p>"
						+ month
						+ " "
						+ date
						+ ", "
						+ year
						+ "</p>'  ><u>"
						+ objPanchangUtil.dms(actualtime) + "</u></a>";*/

                    result = objPanchangUtil.dms(actualtime);
                    break;
            }
        }
        return result;
    }

    public String GetDay(double dayValue, String title) {
        int day = (int) dayValue;
        String result = day + "";
        switch (title) {
            case "MoonSign":
                result = constants.getMoonSign(day);
                break;
            case "Tith":
                result = constants.getTithi(day);
                break;
            case "Naksh":
                result = constants.getNakshatra(day);
                break;
            case "Yog":
                result = constants.getYoga(day);
                break;
            case "Karan":
                result = constants.getKaranas(day);
                break;
        }
        return result;
    }

    public String GethoverDay(double dayValue, String title) {
        int day = (int) dayValue;
        String result = day + "";
        switch (title) {
            case "MoonSign":
                result = (day == 13 ? constants.getMoonSign(1) : constants
                        .getMoonSign(day));
                break;
            case "Tith":
                result = (day == 31 ? constants.getTithi(1) : constants
                        .getTithi(day)); // constants.Tithi[day];
                break;
            case "Naksh":
                result = (day == 28 ? constants.getNakshatra(1) : constants
                        .getNakshatra(day)); // constants.Nakshatra[day];
                break;
            case "Yog":
                result = (day == 28 ? constants.getYoga(1) : constants.getYoga(day)); // constants.Yoga[day];
                break;
            case "Karan":
                result = (day == 61 ? constants.getKaranas(1) : constants
                        .getKaranas(day)); // constants.Karanas[day];
                break;
        }
        return result;
    }

    public String GetFromToString(String from, String to) {
        String strFormat = "";

        switch (languageCode) {
            case hi:
                strFormat = from + " से  " + to + " तक";
                break;

            case ta:
                strFormat = from + " " + to + ", இருந்து";
                break;

            case te:
                strFormat = from + " నుండి " + to;
                break;

            case ka:
                strFormat = from + " ರಿಂದ " + to;
                break;

            case ml:
                strFormat = from + " മുതൽ " + to + " വരെ";
                break;

            case gu:
                strFormat = from + " થી " + to + " ના";
                break;

            case mr:
                strFormat = from + " से " + to + " तक";
                break;

            case bn:
                strFormat = "ফরম " + from + " থেকে " + to;
                break;
            case or:
                strFormat = from + " ରୁ " + to + " ପର୍ଯ୍ୟନ୍ତ";
                break;
            case as:
                strFormat = from + " পৰা " + to + " পৰ্য্যন্ত";
                break;

            case en:
            default:
                strFormat = "From " + from + " To " + to;
                break;
        }

        return strFormat;
    }

    public String GetMergeString(int[] data, String[] list) {
        String str = "";
        for (int item : data) {
            str += (str == "" ? "" : ", ") + list[item];
        }
        return str;
    }

    public String GetMergeString(double[] data) {
        String str = "";
        for (double item : data) {
            str += (str == "" ? "" : ", ") + objPanchangUtil.dms(item);
        }

        return str;
    }

}
