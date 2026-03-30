package com.ojassoft.astrosage.model;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.jinterface.matching.IProxyKundliMatchingNorth;
import com.ojassoft.astrosage.utils.CGlobalVariables;



/**
 * this is north matching class. this class parse XML.
 *
 * @author Hukum
 */
public class OnlineKundliMatchingNorth implements IProxyKundliMatchingNorth {

    String _varna = "", _vasya = "", _tara = "", _yoni = "", _maitri = "",
            _gana = "", _bhakoot = "", _nadi = "", _totalGuna = "",
            _boyMangalDosha = "", _girlMangalDosha = "", _conclusion = "";
    String _varnaPrediction, _vasyaPrediction, _taraPrediction,
            _yoniPrediction, _maitriPrediction, _ganaPrediction,
            _bhakootPrediction, _nadiPrediction;

    String mmURI = "";
    BeanHoroPersonalInfo _boy, _girl;
    //int languageCode=CGlobalVariables.MM_ENGLISH;
    int languageCode = CGlobalVariables.MM_ENGLISH;

    String _boyRasiNumber = "", _girlRasiNumber = "";
    String _moonDegreeOfBoy = "", _moonDegreeOfGirl = "";//ADDED BY BIJENDRA ON 28-04-15

    /**
     * this is constructor
     *
     * @param boy
     * @param girl
     */
    public OnlineKundliMatchingNorth(BeanHoroPersonalInfo boy, BeanHoroPersonalInfo girl, int languageCode) {

        // mmURI=CUtils.getPreparedURLForMatchMaking(boy,girl);
        //Log.d("OnlineKundliMatchingNorth_Constructor", mmURI);
        _boy = boy;
        _girl = girl;
        this.languageCode = languageCode;

    }

    /**
     * this methods post call to web server.
     */
    /*public boolean initialize() throws Exception {
        boolean isSuccess = false;
        *//* String mmURI=CUtils.getPreparedURLForMatchMaking(boy,girl); *//*
        try {
            // String matchResult=CUtils.executeOnlineRequest(mmURI);
			*//*
     * Log.d("OnlineKundliMatchingNorth_result", matchResult);
     * separatOutoutString(matchResult);
     *//*
            //getPreparedURL();//use for testing url
            isSuccess = postMatchMakingValues();
            //isSuccess = true;
        } catch (Exception e) {
            //Log.d("OnlineKundliMatchingNorth_Error", e.getMessage());
            isSuccess = false;
            throw e;

        }
        return isSuccess;
    }*/

    /**
     * this method separates xml data in local properties.
     *
     * @param strData
     */
    /*private void separatOutoutString(String strData) throws Exception {
        try {

            _varna = (strData.substring(
                    strData.indexOf("<Varna>") + "<Varna>".length(),
                    strData.indexOf("</Varna>")));
            _vasya = (strData.substring(
                    strData.indexOf("<Vasya>") + "<Vasya>".length(),
                    strData.indexOf("</Vasya>")));
            _tara = (strData.substring(
                    strData.indexOf("<Tara>") + "<Tara>".length(),
                    strData.indexOf("</Tara>")));
            _yoni = (strData.substring(
                    strData.indexOf("<Yoni>") + "<Yoni>".length(),
                    strData.indexOf("</Yoni>")));
            _maitri = (strData.substring(strData.indexOf("<Maitri>")
                    + "<Maitri>".length(), strData.indexOf("</Maitri>")));
            _gana = (strData.substring(
                    strData.indexOf("<Gana>") + "<Gana>".length(),
                    strData.indexOf("</Gana>")));
            _bhakoot = (strData.substring(strData.indexOf("<Bhakoot>")
                    + "<Bhakoot>".length(), strData.indexOf("</Bhakoot>")));
            _nadi = (strData.substring(
                    strData.indexOf("<Nadi>") + "<Nadi>".length(),
                    strData.indexOf("</Nadi>")));
            _totalGuna = (strData.substring(strData.indexOf("<TotalGuna>")
                    + "<TotalGuna>".length(), strData.indexOf("</TotalGuna>")));
            _boyMangalDosha = (strData.substring(
                    strData.indexOf("<BoyMangalDosha>")
                            + "<BoyMangalDosha>".length(),
                    strData.indexOf("</BoyMangalDosha>")));
            _girlMangalDosha = (strData.substring(
                    strData.indexOf("<GirlMangalDosha>")
                            + "<GirlMangalDosha>".length(),
                    strData.indexOf("</GirlMangalDosha>")));
            _conclusion = (strData
                    .substring(
                            strData.indexOf("<Conclusion>")
                                    + "<Conclusion>".length(),
                            strData.indexOf("</Conclusion>")));
            _varnaPrediction = (strData.substring(
                    strData.indexOf("<Varna-Prediction>")
                            + "<Varna-Prediction>".length(),
                    strData.indexOf("</Varna-Prediction>")));
            _vasyaPrediction = (strData.substring(
                    strData.indexOf("<Vasya-Prediction>")
                            + "<Vasya-Prediction>".length(),
                    strData.indexOf("</Vasya-Prediction>")));
            _taraPrediction = (strData.substring(
                    strData.indexOf("<Tara-Prediction>")
                            + "<Tara-Prediction>".length(),
                    strData.indexOf("</Tara-Prediction>")));
            _yoniPrediction = (strData.substring(
                    strData.indexOf("<Yoni-Prediction>")
                            + "<Yoni-Prediction>".length(),
                    strData.indexOf("</Yoni-Prediction>")));
            _maitriPrediction = (strData.substring(
                    strData.indexOf("<Maitri-Prediction>")
                            + "<Maitri-Prediction>".length(),
                    strData.indexOf("</Maitri-Prediction>")));
            _ganaPrediction = (strData.substring(
                    strData.indexOf("<Gana-Prediction>")
                            + "<Gana-Prediction>".length(),
                    strData.indexOf("</Gana-Prediction>")));
            _bhakootPrediction = (strData.substring(
                    strData.indexOf("<Bhakoot-Prediction>")
                            + "<Bhakoot-Prediction>".length(),
                    strData.indexOf("</Bhakoot-Prediction>")));
            _nadiPrediction = (strData.substring(
                    strData.indexOf("<Nadi-Prediction>")
                            + "<Nadi-Prediction>".length(),
                    strData.indexOf("</Nadi-Prediction>")));

            //ADDED BY BIJENDRA ON 19-06-14
            _boyRasiNumber = (strData.substring(
                    strData.indexOf("<BoyRasiNumber>")
                            + "<BoyRasiNumber>".length(),
                    strData.indexOf("</BoyRasiNumber>")));
            ////Log.e("_boyRasiNumber", _boyRasiNumber);

            _girlRasiNumber = (strData.substring(
                    strData.indexOf("<GirlRasiNumber>")
                            + "<GirlRasiNumber>".length(),
                    strData.indexOf("</GirlRasiNumber>")));
            ////Log.e("_girlRasiNumber", _girlRasiNumber);

            //ADDED BY BIJENDRA ON 28-04-15
            _moonDegreeOfBoy = (strData.substring(
                    strData.indexOf("<MoonDegreeOfBoy>")
                            + "<MoonDegreeOfBoy>".length(),
                    strData.indexOf("</MoonDegreeOfBoy>")));

            ////Log.e("_moonDegreeOfBoy", _moonDegreeOfBoy);

            _moonDegreeOfGirl = (strData.substring(
                    strData.indexOf("<MoonDegreeOfGirl>")
                            + "<MoonDegreeOfGirl>".length(),
                    strData.indexOf("</MoonDegreeOfGirl>")));

            ////Log.e("_moonDegreeOfGirl", _moonDegreeOfGirl);

            //END

        } catch (Exception e) {
            //Log.d("Error_separatOutoutString", e.getMessage());
            throw e;
        }
    }*/
    public String subGetMatchMakingInterpretation(String strCategory,
                                                  int intForBoy, int intForGirl) {
        // TODO Auto-generated method stub
        String _prediction = "";
        if (CGlobalVariables.MM_PREDICTION_VARNA.equalsIgnoreCase(strCategory
                .trim()))
            _prediction = _varnaPrediction;
        if (CGlobalVariables.MM_PREDICTION_VASYA.equalsIgnoreCase(strCategory
                .trim()))
            _prediction = _vasyaPrediction;

        if (CGlobalVariables.MM_PREDICTION_TARA.equalsIgnoreCase(strCategory
                .trim()))
            _prediction = _taraPrediction;

        if (CGlobalVariables.MM_PREDICTION_YONI.equalsIgnoreCase(strCategory
                .trim()))
            _prediction = _yoniPrediction;

        if (CGlobalVariables.MM_PREDICTION_MAITRI.equalsIgnoreCase(strCategory
                .trim()))
            _prediction = _maitriPrediction;

        if (CGlobalVariables.MM_PREDICTION_GANA.equalsIgnoreCase(strCategory
                .trim()))
            _prediction = _ganaPrediction;

        if (CGlobalVariables.MM_PREDICTION_BHAKUT.equalsIgnoreCase(strCategory
                .trim()))
            _prediction = _bhakootPrediction;

        if (CGlobalVariables.MM_PREDICTION_NADI.equalsIgnoreCase(strCategory
                .trim()))
            _prediction = _nadiPrediction;

        return _prediction;
    }

    public double matchVarnaGuna(int maleRasiNumber, int femaleRasiNumber) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(_varna.trim());
        } catch (Exception e) {

        }
        return 0;
    }

    public double matchVashyaGuna(double degOfBoy, double degOfGirl) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(_vasya.trim());
        } catch (Exception e) {

        }
        return 0;
    }

    public double matchTaraGuna(double degOfBoy, double degOfGirl) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(_tara.trim());
        } catch (Exception e) {

        }
        return 0;
    }

    public double matchYoniGuna(double degOfBoy, double degOfGirl) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(_yoni.trim());
        } catch (Exception e) {

        }
        return 0;
    }

    public double matchGrahaMitraGuna(int maleRasiNumber, int femaleRasiNumber) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(_maitri.trim());
        } catch (Exception e) {

        }
        return 0;
    }

    public double matchGanaGuna(double degOfBoy, double degOfGirl) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(_gana.trim());
        } catch (Exception e) {

        }
        return 0;
    }

    public double matchBhakutGuna(int maleRasiNumber, int femaleRasiNumber) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(_bhakoot.trim());
        } catch (Exception e) {

        }
        return 0;
    }

    public double matchNadiGuna(double degOfBoy, double degOfGirl) {
        // TODO Auto-generated method stub
        try {
            return Double.valueOf(_nadi.trim());
        } catch (Exception e) {

        }
        return 0;
    }

    public String boyMangalDosh() {
        // TODO Auto-generated method stub
        return _boyMangalDosha;
    }

    public String girlMangalDosh() {
        // TODO Auto-generated method stub
        return _girlMangalDosha;
    }

    public String getConclusion() {
        // TODO Auto-generated method stub
        return _conclusion;
    }

    /*private boolean postMatchMakingValues() throws ClientProtocolException,
            IOException {
        boolean _success = false;
        String strReturn = "";
        BufferedReader in = null;
        String url = CGlobalVariables.MM_MATCH_MAKING_URL_NORTH;
        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());
        HttpPost hp = new HttpPost(url);
        getPreparedURL();

        try {
            // CGlobalVariables.MM_outBeanNorthMatchMaking=new
            // BeanOutMatchmakingNorth();
            hp.setEntity(new UrlEncodedFormEntity(
                    getPrepareNameValuePairForMatchMaking()));

            HttpResponse response = hc.execute(hp);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);
            in.close();
            strReturn = sb.toString();
            in.close();
            // Log.d("north-calculation-result",strReturn);
            separatOutoutString(strReturn);
            _success = true;
        } catch (Exception e) {
            _success = false;
            //Log.d("north-calculation-error", e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    _success = false;

                }
            }
        }

        return _success;
    }*/

    /*private void getPreparedURL() {

        StringBuilder sb = new StringBuilder();
        sb.append(CGlobalVariables.MM_MATCH_MAKING_URL_NORTH + "?");
		*//*
     * sb.append("name="+_name);
     * //sb.append("https://www.astrosage.com/freekphorary/chartxml.asp?name='"
     * +CGlobalVariables._PersonInfo.getName().trim().replace(' ','%' ));
     * sb.append("&sex="+_birthDetail.getSex().trim());
     *//*
        sb.append("name1=" + _boy.getName());
        sb.append("&day1=" + String.valueOf(_boy.getDateTime().getDay()));
        sb.append("&month1="
                + String.valueOf(_boy.getDateTime().getMonth() + 1));
        sb.append("&year1=" + String.valueOf(_boy.getDateTime().getYear()));
        sb.append("&hrs1=" + String.valueOf(_boy.getDateTime().getHour()));
        sb.append("&min1=" + String.valueOf(_boy.getDateTime().getMin()));
        sb.append("&sec1=" + String.valueOf(_boy.getDateTime().getSecond()));
        sb.append("&dst1=" + _boy.getDST());
        // FOR BOY CITY DATA

        sb.append("&place1=" + _boy.getPlace().getCityName());
        sb.append("&LongDeg1=" + _boy.getPlace().getLongDeg());
        sb.append("&LongMin1=" + _boy.getPlace().getLongMin());
        sb.append("&LongSec1=" + _boy.getPlace().getLongSec());
        sb.append("&LongEW1=" + _boy.getPlace().getLongDir());
        sb.append("&LatDeg1=" + _boy.getPlace().getLatDeg());
        sb.append("&LatMin1=" + _boy.getPlace().getLatMin());
        sb.append("&LatSec1=" + _boy.getPlace().getLatSec());
        sb.append("&LatNS1=" + _boy.getPlace().getLatDir());
        sb.append("&timeZone1="
                + String.valueOf(_boy.getPlace().getTimeZoneValue()));

        sb.append("&name2=" + _girl.getName());
        sb.append("&day2=" + String.valueOf(_girl.getDateTime().getDay()));
        sb.append("&month2="
                + String.valueOf(_girl.getDateTime().getMonth() + 1));
        sb.append("&year2=" + String.valueOf(_girl.getDateTime().getYear()));
        sb.append("&hrs2=" + String.valueOf(_girl.getDateTime().getHour()));
        sb.append("&min2=" + String.valueOf(_girl.getDateTime().getMin()));
        sb.append("&sec2=" + String.valueOf(_girl.getDateTime().getSecond()));
        sb.append("&dst2=" + _girl.getDST());
        // FOR GIRL CITY DATA

        sb.append("&place2=" + _girl.getPlace().getCityName());
        sb.append("&LongDeg2=" + _girl.getPlace().getLongDeg());
        sb.append("&LongMin2=" + _girl.getPlace().getLongMin());
        sb.append("&LongSec2=" + _girl.getPlace().getLongSec());
        sb.append("&LongEW2=" + _girl.getPlace().getLongDir());
        sb.append("&LatDeg2=" + _girl.getPlace().getLatDeg());
        sb.append("&LatMin2=" + _girl.getPlace().getLatMin());
        sb.append("&LatSec2=" + _girl.getPlace().getLatSec());
        sb.append("&LatNS2=" + _girl.getPlace().getLatDir());
		*//*
     * nameValuePairs.add(new
     * BasicNameValuePair("LatSec2",_girl.getPlace().getLatSec()));
     *//*
        sb.append("&timeZone2="
                + String.valueOf(_girl.getPlace().getTimeZoneValue()));

        sb.append("&LanguageCode=" + String.valueOf(languageCode));
        //sb.append("&LanguageCode="+ String.valueOf(CGlobalVariables.LANGUAGE_CODE));

        ////Log.e("URL_TO_GET_DATA", sb.toString());

    }*/

    /*private List<NameValuePair> getPrepareNameValuePairForMatchMaking()
            throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(33);
        try {

            // FOR BOY
            nameValuePairs.add(new BasicNameValuePair("name1", _boy.getName().trim()));
            nameValuePairs.add(new BasicNameValuePair("day1", String.valueOf(_boy.getDateTime().getDay()).trim()));
            nameValuePairs.add(new BasicNameValuePair("month1", String.valueOf(_boy.getDateTime().getMonth() + 1).trim()));
            nameValuePairs.add(new BasicNameValuePair("year1", String.valueOf(_boy.getDateTime().getYear()).trim()));
            nameValuePairs.add(new BasicNameValuePair("hrs1", String.valueOf(_boy.getDateTime().getHour()).trim()));
            nameValuePairs.add(new BasicNameValuePair("min1", String.valueOf(_boy.getDateTime().getMin()).trim()));
            nameValuePairs.add(new BasicNameValuePair("sec1", String.valueOf(_boy.getDateTime().getSecond()).trim()));
            nameValuePairs.add(new BasicNameValuePair("dst1", String.valueOf(_boy.getDST())));
            // nameValuePairs.add(new BasicNameValuePair("dst1","0"));
            // FOR BOY CITY DATA

            nameValuePairs.add(new BasicNameValuePair("place1", _boy.getPlace().getCityName().trim()));
            nameValuePairs.add(new BasicNameValuePair("LongDeg1", _boy.getPlace().getLongDeg().trim()));
            nameValuePairs.add(new BasicNameValuePair("LongMin1", _boy.getPlace().getLongMin().trim()));
            nameValuePairs.add(new BasicNameValuePair("LongSec1", _boy.getPlace().getLongSec().trim()));
            nameValuePairs.add(new BasicNameValuePair("LongEW1", _boy.getPlace().getLongDir().trim()));
            nameValuePairs.add(new BasicNameValuePair("LatDeg1", _boy.getPlace().getLatDeg().trim()));
            nameValuePairs.add(new BasicNameValuePair("LatMin1", _boy.getPlace().getLatMin().trim()));
            nameValuePairs.add(new BasicNameValuePair("LatSec1", _boy.getPlace().getLatSec().trim()));
            nameValuePairs.add(new BasicNameValuePair("LatNS1", _boy.getPlace().getLatDir().trim()));
            nameValuePairs.add(new BasicNameValuePair("timeZone1", String.valueOf(_boy.getPlace().getTimeZoneValue()).trim()));

            // END BOY

            // FOR GIRL

            nameValuePairs.add(new BasicNameValuePair("name2", _girl.getName().trim()));
            nameValuePairs.add(new BasicNameValuePair("day2", String.valueOf(_girl.getDateTime().getDay()).trim()));
            nameValuePairs.add(new BasicNameValuePair("month2", String.valueOf(_girl.getDateTime().getMonth() + 1).trim()));
            nameValuePairs.add(new BasicNameValuePair("year2", String.valueOf(_girl.getDateTime().getYear()).trim()));
            nameValuePairs.add(new BasicNameValuePair("hrs2", String.valueOf(_girl.getDateTime().getHour()).trim()));
            nameValuePairs.add(new BasicNameValuePair("min2", String.valueOf(_girl.getDateTime().getMin()).trim()));
            nameValuePairs.add(new BasicNameValuePair("sec2", String.valueOf(_girl.getDateTime().getSecond()).trim()));
            nameValuePairs.add(new BasicNameValuePair("dst2", String.valueOf(_girl.getDST())));
            // nameValuePairs.add(new BasicNameValuePair("dst2","0"));
            // FOR GIRL CITY DATA

            nameValuePairs.add(new BasicNameValuePair("place2", _girl.getPlace().getCityName().trim()));
            nameValuePairs.add(new BasicNameValuePair("LongDeg2", _girl.getPlace().getLongDeg().trim()));
            nameValuePairs.add(new BasicNameValuePair("LongMin2", _girl.getPlace().getLongMin().trim()));
            nameValuePairs.add(new BasicNameValuePair("LongSec2", _girl.getPlace().getLongSec().trim()));
            nameValuePairs.add(new BasicNameValuePair("LongEW2", _girl.getPlace().getLongDir().trim()));
            nameValuePairs.add(new BasicNameValuePair("LatDeg2", _girl.getPlace().getLatDeg().trim()));
            nameValuePairs.add(new BasicNameValuePair("LatMin2", _girl.getPlace().getLatMin().trim()));
            nameValuePairs.add(new BasicNameValuePair("LatSec2", _girl.getPlace().getLatSec().trim()));
            nameValuePairs.add(new BasicNameValuePair("LatNS2", _girl.getPlace().getLatDir().trim()));
            nameValuePairs.add(new BasicNameValuePair("timeZone2", String.valueOf(_girl.getPlace().getTimeZoneValue()).trim()));

            nameValuePairs.add(new BasicNameValuePair("LanguageCode", String.valueOf(languageCode)));//ADDED BY BIJENDRA ON 2-02-FEB-2013


            // END GIRL

        } catch (Exception e) {
            throw e;

        }
        return nameValuePairs;

    }*/

   /* private HttpParams getHttpClientTimeoutParameter() {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 1000 * 45;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 1000 * 45;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;
    }*/

    @Override
    public String getBoyRashiNumber() {
        // TODO Auto-generated method stub
        return _boyRasiNumber;
    }

    @Override
    public String getGirlRashiNumber() {
        // TODO Auto-generated method stub
        return _girlRasiNumber;
    }

    @Override
    public String getBoyMoonDegree() {
        // TODO Auto-generated method stub
        return _moonDegreeOfBoy;
    }

    @Override
    public String getGirlMoonDegree() {
        // TODO Auto-generated method stub
        return _moonDegreeOfGirl;
    }

    /*
     * private String formatDegree(String sDeg) { String fDeg="";
     * if(sDeg.length()==0) fDeg="000"; if(sDeg.length()==1) fDeg="00"+sDeg;
     * if(sDeg.length()==2) fDeg="0"+sDeg;
     *
     *
     * return fDeg; }
     *
     * private String formatMin(String sMin) { String fMin="";
     * if(sMin.length()==1) fMin="0"+sMin;
     *
     *
     * return fMin; }
     */

}
