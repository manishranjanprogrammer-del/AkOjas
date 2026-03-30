package com.ojassoft.astrosage.model;



import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.jinterface.ICalculationProxy;



public class CWebServiceTajikVarshaphal extends com.cslsoftware.enghoro.EngHoro implements ICalculationProxy {

    private PlanetData _planetDegree = null;
    private BeanHoroPersonalInfo _birthDetail = null;
    private String varshphalYear = null;

    public CWebServiceTajikVarshaphal(BeanHoroPersonalInfo objBirthDetail,
                                      String _varshphalYear) {
        varshphalYear = _varshphalYear;
        _birthDetail = objBirthDetail;
        _planetDegree = new PlanetData();
    }

    @Override
    public boolean calculate(ICalculationProxy _ICalculationProxy) throws Exception {
        return false;
    }

    @Override
    public boolean calculate() throws Exception {
        boolean isSuccess = true;
        try {
            //calculatePlanetAndDegree();
        } catch (Exception e) {
            isSuccess = false;
            throw e;

        }
        return isSuccess;
    }

   /* private void calculatePlanetAndDegree() throws Exception {
        try {


            executeHTTPGetRequest();
            *//*
     * CGlobal.getCGlobalObject().getTajikVarshaphal().setPlanetData(CGlobal
     * .getCGlobalObject().getPlanetDataObject());
     * CGlobal.getCGlobalObject().getTajikVarshaphal().setMuntha("1");
     *//*

        } catch (Exception e) {
            throw e;
        }

    }*/

   /* private HttpParams getHttpClientTimeoutParameter() {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 1000 * 60;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 1000 * 60;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;
    }*/

    /*private void executeHTTPGetRequest() throws Exception {

        //CGlobalVariables._isInternetSlow = true;

        String strReturn = "";
        BufferedReader in = null;

        HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());

        HttpPost hp = new HttpPost(CGlobalVariables.TAJIK_VARSHAPHAL_URL);

        try {

            hp.setEntity(new UrlEncodedFormEntity(getPrepareNameValuePairForTajikVarshaphal()));
            //Log.e("Varshphal::", "::" + getPrepareNameValuePairForTajikVarshaphal());

            HttpResponse response = hc.execute(hp);

            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);
            in.close();
            strReturn = sb.toString();
            ////Log.e("varshphalyear-ste", String.valueOf(strReturn));
            initValuesForMyKundli(strReturn);

        } catch (Exception e) {
            Log.d("Error_DB-1", e.getMessage());
            //throw (new CustomException(e, this.getClass().getName(),
            //	"executeHTTPGetRequest"));
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //Log.d("Error_DB", e.getMessage());
                }
            }
        }

    }*/

   /* private List<NameValuePair> getPrepareNameValuePairForTajikVarshaphal() {
        String _name = _birthDetail.getName().trim().replace(' ', '_');
        String _mycity = _birthDetail.getPlace().getCityName().trim();
        _mycity = _mycity.substring(0, 3);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(22);

        nameValuePairs.add(new BasicNameValuePair("name", _name));
        nameValuePairs.add(new BasicNameValuePair("sex", _birthDetail.getGender().trim()));
        nameValuePairs.add(new BasicNameValuePair("day", String.valueOf(_birthDetail.getDateTime().getDay()).trim()));
        nameValuePairs.add(new BasicNameValuePair("month", String.valueOf(_birthDetail.getDateTime().getMonth() + 1).trim()));
        nameValuePairs.add(new BasicNameValuePair("year", String.valueOf(_birthDetail.getDateTime().getYear()).trim()));
        nameValuePairs.add(new BasicNameValuePair("hrs", String.valueOf(_birthDetail.getDateTime().getHour()).trim()));
        nameValuePairs.add(new BasicNameValuePair("min", String.valueOf(_birthDetail.getDateTime().getMin()).trim()));
        nameValuePairs.add(new BasicNameValuePair("sec", String.valueOf(_birthDetail.getDateTime().getSecond())));
        nameValuePairs.add(new BasicNameValuePair("dst", String.valueOf(_birthDetail.getDST())));
        nameValuePairs.add(new BasicNameValuePair("place", _birthDetail.getPlace().getCityName().trim()));
        nameValuePairs.add(new BasicNameValuePair("longdeg", _birthDetail.getPlace().getLongDeg().trim()));
        nameValuePairs.add(new BasicNameValuePair("longmin", _birthDetail.getPlace().getLongMin().trim()));
        nameValuePairs.add(new BasicNameValuePair("longew", _birthDetail.getPlace().getLongDir().trim()));
        nameValuePairs.add(new BasicNameValuePair("latdeg", _birthDetail.getPlace().getLatDeg().trim()));
        nameValuePairs.add(new BasicNameValuePair("latmin", _birthDetail.getPlace().getLatMin().trim()));
        nameValuePairs.add(new BasicNameValuePair("latns", _birthDetail.getPlace().getLatDir().trim()));
        nameValuePairs.add(new BasicNameValuePair("timezone", String.valueOf(_birthDetail.getPlace().getTimeZoneValue())));
        nameValuePairs.add(new BasicNameValuePair("ayanamsa", String.valueOf(_birthDetail.getAyanIndex()).trim()));
        nameValuePairs.add(new BasicNameValuePair("kphn", String.valueOf(_birthDetail.getHoraryNumber())));

        int calVarshphalYear = _birthDetail.getDateTime().getYear() + Integer.valueOf(varshphalYear);
        nameValuePairs.add(new BasicNameValuePair("varshphalyear", String.valueOf(calVarshphalYear)));


        nameValuePairs.add(new BasicNameValuePair("charting", "0"));
        nameValuePairs.add(new BasicNameValuePair("button1", "Get+Kundali"));
        nameValuePairs.add(new BasicNameValuePair("languageCode", "0"));
        return nameValuePairs;
    }*/

   /* private void initValuesForMyKundli(String strData) {
        double _dValues = 0.0;
        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Lagna>")
                + "<Lagna>".length(), strData.indexOf("</Lagna>")));
        if (_dValues >= 360.00)
            _dValues -= 360.00;
        _planetDegree.setLagna(_dValues);

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Sun>")
                + "<Sun>".length(), strData.indexOf("</Sun>")));
        _planetDegree.setSun(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Moon>")
                + "<Moon>".length(), strData.indexOf("</Moon>")));
        _planetDegree.setMoon(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Mars>")
                + "<Mars>".length(), strData.indexOf("</Mars>")));
        _planetDegree.setMarsh(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(
                strData.indexOf("<Mercury>") + "<Mercury>".length(),
                strData.indexOf("</Mercury>")));
        _planetDegree.setMercury(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(
                strData.indexOf("<Jupiter>") + "<Jupiter>".length(),
                strData.indexOf("</Jupiter>")));
        _planetDegree.setJup(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Venus>")
                + "<Venus>".length(), strData.indexOf("</Venus>")));
        _planetDegree.setVenus(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Saturn>")
                + "<Saturn>".length(), strData.indexOf("</Saturn>")));
        _planetDegree.setSat(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Rahu>")
                + "<Rahu>".length(), strData.indexOf("</Rahu>")));
        _planetDegree.setRahu(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Ketu>")
                + "<Ketu>".length(), strData.indexOf("</Ketu>")));
        _planetDegree.setKetu(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Uranus>")
                + "<Uranus>".length(), strData.indexOf("</Uranus>")));
        _planetDegree.setUranus(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(
                strData.indexOf("<Neptune>") + "<Neptune>".length(),
                strData.indexOf("</Neptune>")));
        _planetDegree.setNeptune(Double.valueOf(_dValues));

        _dValues = Double.valueOf(strData.substring(strData.indexOf("<Pluto>")
                + "<Pluto>".length(), strData.indexOf("</Pluto>")));
        _planetDegree.setPluto(Double.valueOf(_dValues));
        _dValues = Double.valueOf(strData.substring(strData.indexOf("<MunthaInBhav>")
                + "<MunthaInBhav>".length(), strData.indexOf("</MunthaInBhav>")));

        //muntha=(int) _dValues;
        CGlobal.getCGlobalObject().getBeanTajikVarshaphal().setMuntha(String.valueOf((int) _dValues));

        CGlobal.getCGlobalObject().getBeanTajikVarshaphal().setPlanetData(_planetDegree);

    }*/


    public double getLagna2() {
        // TODO Auto-generated method stub
        return _planetDegree.getLagna();

    }


}