package com.ojassoft.astrosage.misc;

import com.ojassoft.astrosage.beans.LagnaTableModel;
import com.ojassoft.astrosage.jinterface.IConstants;
import com.ojassoft.astrosage.misc.Language;
import com.ojassoft.astrosage.utils.PanchangUtil;
import com.ojassoft.panchang.AscTable;
import com.ojassoft.panchang.Masa;
import com.ojassoft.panchang.Place;

import java.util.Calendar;
import java.util.Date;

public class LagnaTableCalculation {

    int datePanJd;
    Calendar calendarPan;
    IConstants constants;
    double latitude;
    double longitude;
    double timezone;
    String timezoneString;
    Place place;
    String placeID;
    Language languageCode;
    PanchangUtil objPanchangUtil;
    Date datePan;
    LagnaTableModel objLagnaTableModel = new LagnaTableModel();

    public LagnaTableCalculation(Date _datePan, String lang, String lat, String lng, String tz, String timeZoneString) {


        datePan = _datePan;
        //placeID = _placeID;

        objPanchangUtil = new PanchangUtil();
        calendarPan = Calendar.getInstance();
        calendarPan.setTime(datePan);
        datePanJd = (int) Masa.toJulian(calendarPan.get(Calendar.YEAR),
                calendarPan.get(Calendar.MONTH) + 1,
                calendarPan.get(Calendar.DAY_OF_MONTH));

        languageCode = Language.getLanguage(lang);
        constants = objPanchangUtil.getIConstantsObj(languageCode);
        // ***************** CODE to fetch place info from id ***************

        latitude = Float.valueOf(lat);
        longitude = Float.valueOf(lng);
        this.timezone = Float.valueOf(tz);

        if (objPanchangUtil.isDst(this.timezoneString, datePan))
            timezone = timezone + 1.0;
        place = new Place(latitude, longitude, timezone);

        /*if (placeID.equals("1261481")) {
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
        } else {
            if (!lat.equals("0") & !lng.equals("0")) {
                latitude = Float.valueOf(lat);
                longitude = Float.valueOf(lng);
                this.timezone = Float.valueOf(tz);
                objAajKaPanchangModel.setIsPlaceValid(true);

            } else {
                latitude = 28.6139;
                longitude = 77.2090;
                timezone = +5.5;
                strPlace = "New Delhi";
                strCountry = "India";
                strState = "NCT";
                timezoneString = "Asia/Kolkata";
                objAajKaPanchangModel.setIsPlaceValid(false);
            }
            if (objPanchangUtil.isDst(this.timezoneString, datePan))
                timezone = timezone + 1.0;
            place = new Place(latitude, longitude, timezone);
        }*/
    }

    public LagnaTableModel getLagnaTable() {

        lagnaValueTime();
        return objLagnaTableModel;
    }


    private void lagnaValueTime() {
        String sunrise = objPanchangUtil.dms(Masa.getSunRise(datePanJd, place));
        double[] lagnaAtSunRiseArray = AscTable.ascAtSunRise(datePanJd, place);
        String lagnaAtSunRise = constants.getLagna((int) lagnaAtSunRiseArray[0]);
        String lagnaDegAtSunRise = objPanchangUtil.dms(lagnaAtSunRiseArray[2]);
        String[] degMinSecArr = lagnaDegAtSunRise.split(":");
        String degMinSec = degMinSecArr[0] + "&#176 " + degMinSecArr[1] + "&#180 " + degMinSecArr[2] + "&#8221";
        int[] lagnaNatureNum = {0, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3};

        int[] jdDateArray = Masa.fromJulian(datePanJd);
        objLagnaTableModel.setLagnaDateApi(addLeadingZero(jdDateArray[2]) + "/" + addLeadingZero(jdDateArray[1]) + "/" + jdDateArray[0]);
        objLagnaTableModel.setSunrise(sunrise);
        objLagnaTableModel.setLagnaValue(lagnaAtSunRise);
        objLagnaTableModel.setLagnaDegre(degMinSec);

        objLagnaTableModel.setLagnaDegApi(degMinSecArr[0]);
        objLagnaTableModel.setLagnaMinApi(degMinSecArr[1]);
        objLagnaTableModel.setLagnaSecApi(degMinSecArr[2]);

        double[] allLagnaValueTime = AscTable.ascInADay(datePanJd, place);

        objLagnaTableModel.setLagna1(constants.getLagna((int) allLagnaValueTime[0]));
        objLagnaTableModel.setLagna1StartTime(objPanchangUtil.dms(allLagnaValueTime[23] - 24.0));
        objLagnaTableModel.setLagna1EndTime(objPanchangUtil.dms(allLagnaValueTime[1]));
        objLagnaTableModel.setLagna1Nature(constants.getLagnaNature((int) allLagnaValueTime[0]));
        objLagnaTableModel.setLagna1NatureNum(lagnaNatureNum[(int) allLagnaValueTime[0]] + "");

        objLagnaTableModel.setLagna2(constants.getLagna((int) allLagnaValueTime[2]));
        objLagnaTableModel.setLagna2StartTime(objPanchangUtil.dms(allLagnaValueTime[1]));
        objLagnaTableModel.setLagna2EndTime(objPanchangUtil.dms(allLagnaValueTime[3]));
        objLagnaTableModel.setLagna2Nature(constants.getLagnaNature((int) allLagnaValueTime[2]));
        objLagnaTableModel.setLagna2NatureNum(lagnaNatureNum[(int) allLagnaValueTime[2]] + "");

        objLagnaTableModel.setLagna3(constants.getLagna((int) allLagnaValueTime[4]));
        objLagnaTableModel.setLagna3StartTime(objPanchangUtil.dms(allLagnaValueTime[3]));
        objLagnaTableModel.setLagna3EndTime(objPanchangUtil.dms(allLagnaValueTime[5]));
        objLagnaTableModel.setLagna3Nature(constants.getLagnaNature((int) allLagnaValueTime[4]));
        objLagnaTableModel.setLagna3NatureNum(lagnaNatureNum[(int) allLagnaValueTime[4]] + "");

        objLagnaTableModel.setLagna4(constants.getLagna((int) allLagnaValueTime[6]));
        objLagnaTableModel.setLagna4StartTime(objPanchangUtil.dms(allLagnaValueTime[5]));
        objLagnaTableModel.setLagna4EndTime(objPanchangUtil.dms(allLagnaValueTime[7]));
        objLagnaTableModel.setLagna4Nature(constants.getLagnaNature((int) allLagnaValueTime[6]));
        objLagnaTableModel.setLagna4NatureNum(lagnaNatureNum[(int) allLagnaValueTime[6]] + "");

        objLagnaTableModel.setLagna5(constants.getLagna((int) allLagnaValueTime[8]));
        objLagnaTableModel.setLagna5StartTime(objPanchangUtil.dms(allLagnaValueTime[7]));
        objLagnaTableModel.setLagna5EndTime(objPanchangUtil.dms(allLagnaValueTime[9]));
        objLagnaTableModel.setLagna5Nature(constants.getLagnaNature((int) allLagnaValueTime[8]));
        objLagnaTableModel.setLagna5NatureNum(lagnaNatureNum[(int) allLagnaValueTime[8]] + "");

        objLagnaTableModel.setLagna6(constants.getLagna((int) allLagnaValueTime[10]));
        objLagnaTableModel.setLagna6StartTime(objPanchangUtil.dms(allLagnaValueTime[9]));
        objLagnaTableModel.setLagna6EndTime(objPanchangUtil.dms(allLagnaValueTime[11]));
        objLagnaTableModel.setLagna6Nature(constants.getLagnaNature((int) allLagnaValueTime[10]));
        objLagnaTableModel.setLagna6NatureNum(lagnaNatureNum[(int) allLagnaValueTime[10]] + "");

        objLagnaTableModel.setLagna7(constants.getLagna((int) allLagnaValueTime[12]));
        objLagnaTableModel.setLagna7StartTime(objPanchangUtil.dms(allLagnaValueTime[11]));
        objLagnaTableModel.setLagna7EndTime(objPanchangUtil.dms(allLagnaValueTime[13]));
        objLagnaTableModel.setLagna7Nature(constants.getLagnaNature((int) allLagnaValueTime[12]));
        objLagnaTableModel.setLagna7NatureNum(lagnaNatureNum[(int) allLagnaValueTime[12]] + "");

        objLagnaTableModel.setLagna8(constants.getLagna((int) allLagnaValueTime[14]));
        objLagnaTableModel.setLagna8StartTime(objPanchangUtil.dms(allLagnaValueTime[13]));
        objLagnaTableModel.setLagna8EndTime(objPanchangUtil.dms(allLagnaValueTime[15]));
        objLagnaTableModel.setLagna8Nature(constants.getLagnaNature((int) allLagnaValueTime[14]));
        objLagnaTableModel.setLagna8NatureNum(lagnaNatureNum[(int) allLagnaValueTime[14]] + "");

        objLagnaTableModel.setLagna9(constants.getLagna((int) allLagnaValueTime[16]));
        objLagnaTableModel.setLagna9StartTime(objPanchangUtil.dms(allLagnaValueTime[15]));
        objLagnaTableModel.setLagna9EndTime(objPanchangUtil.dms(allLagnaValueTime[17]));
        objLagnaTableModel.setLagna9Nature(constants.getLagnaNature((int) allLagnaValueTime[16]));
        objLagnaTableModel.setLagna9NatureNum(lagnaNatureNum[(int) allLagnaValueTime[16]] + "");

        objLagnaTableModel.setLagna10(constants.getLagna((int) allLagnaValueTime[18]));
        objLagnaTableModel.setLagna10StartTime(objPanchangUtil.dms(allLagnaValueTime[17]));
        objLagnaTableModel.setLagna10EndTime(objPanchangUtil.dms(allLagnaValueTime[19]));
        objLagnaTableModel.setLagna10Nature(constants.getLagnaNature((int) allLagnaValueTime[18]));
        objLagnaTableModel.setLagna10NatureNum(lagnaNatureNum[(int) allLagnaValueTime[18]] + "");

        objLagnaTableModel.setLagna11(constants.getLagna((int) allLagnaValueTime[20]));
        objLagnaTableModel.setLagna11StartTime(objPanchangUtil.dms(allLagnaValueTime[19]));
        objLagnaTableModel.setLagna11EndTime(objPanchangUtil.dms(allLagnaValueTime[21]));
        objLagnaTableModel.setLagna11Nature(constants.getLagnaNature((int) allLagnaValueTime[20]));
        objLagnaTableModel.setLagna11NatureNum(lagnaNatureNum[(int) allLagnaValueTime[20]] + "");

        objLagnaTableModel.setLagna12(constants.getLagna((int) allLagnaValueTime[22]));
        objLagnaTableModel.setLagna12StartTime(objPanchangUtil.dms(allLagnaValueTime[21]));
        objLagnaTableModel.setLagna12EndTime(objPanchangUtil.dms(allLagnaValueTime[23]));
        objLagnaTableModel.setLagna12Nature(constants.getLagnaNature((int) allLagnaValueTime[22]));
        objLagnaTableModel.setLagna12NatureNum(lagnaNatureNum[(int) allLagnaValueTime[22]] + "");

    }

    private String addLeadingZero(int i) {
        String date = String.valueOf(i);
        if(date!= null && date.length()<2){
            date = "0"+date;
        }
        return date;
    }

    //return json of start and end muhurat date and timing
    public static String getStartEndMuhuratJson(LagnaTableModel objLagDO, String festivalName) {
        boolean flag = false;
        StringBuilder jsonObj = new StringBuilder();
        jsonObj.append("{\"status\":");
        if (objLagDO != null) {
            jsonObj.append("\"1\",");
            jsonObj.append("\"festivalapiname\":\"" + festivalName + "\",");
            jsonObj.append("\"festivalapidate\":\"" + objLagDO.getLagnaDateApi() + "\",");

            jsonObj.append("\"festivalapidata2\":{");
            jsonObj.append("\"sunrise\":\"" + objLagDO.getSunrise() + "\",");
            jsonObj.append("\"lagnavalue\":\"" + objLagDO.getLagnaValue() + "\",");
            jsonObj.append("\"lagnaDeg\":\"" + objLagDO.getLagnaDegApi() + "\",");
            jsonObj.append("\"lagnaMin\":\"" + objLagDO.getLagnaMinApi() + "\",");
            jsonObj.append("\"lagnaSec\":\"" + objLagDO.getLagnaSecApi() + "\"");
            jsonObj.append("},");

            jsonObj.append("\"festivalapidata\":[");
            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna1() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna1StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna1EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna1NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna1Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna2() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna2StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna2EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna2NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna2Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna3() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna3StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna3EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna3NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna3Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna4() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna4StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna4EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna4NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna4Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna5() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna5StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna5EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna5NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna5Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna6() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna6StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna6EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna6NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna6Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna7() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna7StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna7EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna7NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna7Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna8() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna8StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna8EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna8NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna8Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna9() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna9StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna9EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna9NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna9Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna10() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna10StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna10EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna10NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna10Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna11() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna11StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna11EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna11NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna11Nature() + "\"");
            jsonObj.append("},");

            jsonObj.append("{");
            jsonObj.append("\"lagnaName\":\"" + objLagDO.getLagna12() + "\",");
            jsonObj.append("\"lagnaStart\":\"" + objLagDO.getLagna12StartTime() + "\",");
            jsonObj.append("\"lagnaEnd\":\"" + objLagDO.getLagna12EndTime() + "\",");
            jsonObj.append("\"lagnaNatureNum\":\"" + objLagDO.getLagna12NatureNum() + "\",");
            jsonObj.append("\"lagnaNature\":\"" + objLagDO.getLagna12Nature() + "\"");
            jsonObj.append("}");
        } else {
            jsonObj.append("\"0\",");
            jsonObj.append("\"festivalapiname\":\"" + festivalName + "\",");
            jsonObj.append("\"festivalapidata2\":{},");
            jsonObj.append("\"festivalapidata\":[");
        }
        jsonObj.append("]");
        jsonObj.append("}");
        return jsonObj.toString();
    }
}