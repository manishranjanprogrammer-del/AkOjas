package com.ojassoft.astrosage.model;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.cslsoftware.enghoro.EngHoro;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.InKPPlanetsAndCusp;
import com.ojassoft.astrosage.beans.InKpPlanetSignification;
import com.ojassoft.astrosage.beans.OutPanchang;
import com.ojassoft.astrosage.beans.OutShodashvarga;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.jinterface.ICalculationProxy;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.TransitFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

public class TransitOnlineDataCalculation extends EngHoro implements ICalculationProxy {
    private Context t = null;

    private BeanHoroPersonalInfo _birthDetail = null;
    private long _chartId = -1;
    private InputStream inStream;


    int ayanamsaType; // need to implement
    double[] kPCuspLongitude = new double[12];//size should be 12
    double ayanamsa;
    double lagna;
    double sun;
    double moon;
    double mars;
    double mercury;
    double jupitor;
    double venus;
    double saturn;
    double rahu;
    double ketu;
    double uranus;
    double neptune;
    double pluto;
    double kPAyanamsaLongitude;
    double fortuna;
    int[][] kPPlanetSignification = new int[10][13];
    int[][] positionForShodasvarg = new int[17][14];
    int[][] ashtakvargaBinduForSignAndPlanet = new int[8][13];
    int[] totalAshtakVargaValue = new int[13];
    int hinduWeekday;
    boolean[] combust = new boolean[12];
    boolean[] planetDirect = new boolean[13];
    String[] planetRelation = new String[12];

    String pakshaName;
    String yoganame;
    String karanName;
    String sunRiseTimeHms;
    String sunSetTimeHms;
    String kPDayLordName;
    String tithiName;
    Context context;
    // int seconds, min, hrs, month, year, day;
    TransitFragment transitFragment;
    private int moduleId = 0;
    private Object subModuleId = 13;

    public TransitOnlineDataCalculation(TransitFragment transitFragment, Context context, BeanHoroPersonalInfo objBirthDetail) {
        _birthDetail = objBirthDetail;
        this.context = context;
        this.transitFragment = transitFragment;

    }

    public boolean calculate(ICalculationProxy _ICalculationProxy) throws Exception {
        boolean isSuccess = true;
        calculatePlanetAndDegree(_ICalculationProxy);
        return isSuccess;
    }

    @Override
    public boolean calculate() throws Exception {
        return false;
    }

    private void calculatePlanetAndDegree(ICalculationProxy _ICalculationProxy) throws Exception {
        doActionOnClick(_ICalculationProxy);
        //executeHTTPGetRequest();
    }


    public int getAyanamsaType() {
        // TODO Auto-generated method stub
        return 0;
    }

    public double getKPCuspLongitude(int cuspNo) {
        // TODO Auto-generated method stub
        return kPCuspLongitude[cuspNo - 1];//ADDED BY BIJENDRA ON 03-MAY-2013
        /*return kPCuspLongitude[cuspNo];*/
    }

    public double getAyanamsa() {
        // TODO Auto-generated method stub
        return ayanamsa;
    }

    public double getSun() {
        // TODO Auto-generated method stub
        return sun;
    }

    public double getMoon() {
        // TODO Auto-generated method stub
        return moon;
    }

    public double getMars() {
        // TODO Auto-generated method stub
        return mars;
    }

    public double getMercury() {
        // TODO Auto-generated method stub
        return mercury;
    }

    public double getJupitor() {
        // TODO Auto-generated method stub
        return jupitor;
    }

    public double getVenus() {
        // TODO Auto-generated method stub
        return venus;
    }

    public double getSaturn() {
        // TODO Auto-generated method stub
        return saturn;
    }

    public double getRahu() {
        // TODO Auto-generated method stub
        return rahu;
    }

    public double getKetu() {
        // TODO Auto-generated method stub
        return ketu;
    }

    public double getUranus() {
        // TODO Auto-generated method stub
        return uranus;
    }

    public double getNeptune() {
        // TODO Auto-generated method stub
        return neptune;
    }

    public double getPluto() {
        // TODO Auto-generated method stub
        return pluto;
    }

    public double getKPAyanamsaLongitude() {
        // TODO Auto-generated method stub
        return kPAyanamsaLongitude;
    }

    public double getFortuna() {
        // TODO Auto-generated method stub
        return fortuna;
    }

    public int[] getKPPlanetSignification(int planetNo) {
        // TODO Auto-generated method stub
        return kPPlanetSignification[planetNo - 1];//ADDED BY BIJENDRA ON 03-MAY-2013
        /*return kPPlanetSignification[planetNo];*/
    }

    public int[] getPositionForShodasvarg(int position) {
        // TODO Auto-generated method stub
        return positionForShodasvarg[position];
    }

    public int getAshtakvargaBinduForSignAndPlanet(int signNo, int planetNo) {
        // TODO Auto-generated method stub
        return ashtakvargaBinduForSignAndPlanet[signNo][planetNo];
    }

    public int[] getTotalAshtakVargaValue() {
        // TODO Auto-generated method stub
        return totalAshtakVargaValue;
    }

    public int getHinduWeekday() {
        // TODO Auto-generated method stub
        return hinduWeekday;
    }

    public boolean isCombust(int planetNumber) {
        // TODO Auto-generated method stub
        return combust[planetNumber];
    }

    public String getPakshaName() {
        // TODO Auto-generated method stub
        return pakshaName;
    }

    public String getYoganame() {
        // TODO Auto-generated method stub
        return yoganame;
    }

    public String getKaranName() {
        // TODO Auto-generated method stub
        return karanName;
    }

    public String getSunRiseTimeHms() {
        // TODO Auto-generated method stub
        return sunRiseTimeHms;
    }

    public String getSunSetTimeHms() {
        // TODO Auto-generated method stub
        return sunSetTimeHms;
    }

    public String getKPDayLordName() {
        // TODO Auto-generated method stub
        return kPDayLordName;
    }

    public String getTithiName() {
        // TODO Auto-generated method stub
        return tithiName;
    }

    public boolean isPlanetDirect(int planetNo) {
        // TODO Auto-generated method stub
        return planetDirect[planetNo];
    }

    public double getLagna2() {
        // TODO Auto-generated method stub
        return lagna;
    }


    boolean isShowToast = true;

    private String getParameterString() {
        String paramStr = "";
        String _name = _birthDetail.getName().trim().replace(' ', '_');
        String _mycity = _birthDetail.getPlace().getCityName().trim();
        _mycity = _mycity.substring(0, 3);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(22);
        paramStr =
                "name" + "=" + _name + "&" +
                        "sex" + "=" + _birthDetail.getGender().trim() + "&" +
                        "day" + "=" + String.valueOf(_birthDetail.getDateTime().getDay()).trim() + "&" +
                        "month" + "=" + String.valueOf(_birthDetail.getDateTime().getMonth() + 1).trim() + "&" +
                        "year" + "=" + String.valueOf(_birthDetail.getDateTime().getYear()).trim() + "&" +
                        "hrs" + "=" + String.valueOf(_birthDetail.getDateTime().getHour()).trim() + "&" +
                        "min" + "=" + String.valueOf(_birthDetail.getDateTime().getMin()).trim() + "&" +
                        "sec" + "=" + String.valueOf(_birthDetail.getDateTime().getSecond()).trim() + "&" +
                        "dst" + "=" + String.valueOf(_birthDetail.getDST()) + "&" +
                        "place" + "=" + _birthDetail.getPlace().getCityName().trim().replace(" ", "") + "&" +
                        "LongDeg" + "=" + _birthDetail.getPlace().getLongDeg().trim() + "&" +
                        "LongMin" + "=" + _birthDetail.getPlace().getLongMin().trim() + "&" +
                        "LongEW" + "=" + _birthDetail.getPlace().getLongDir().trim() + "&" +
                        "LatDeg" + "=" + _birthDetail.getPlace().getLatDeg().trim() + "&" +
                        "LatMin" + "=" + _birthDetail.getPlace().getLatMin().trim() + "&" +
                        "LatNS" + "=" + _birthDetail.getPlace().getLatDir().trim() + "&" +
                        "timeZone" + "=" + String.valueOf(_birthDetail.getPlace().getTimeZoneValue()) + "&" +
                        "ayanamsa" + "=" + String.valueOf(_birthDetail.getAyanIndex()).trim() + "&" +
                        "charting" + "=" + "0" + "&" +
                        "kphn" + "=" + String.valueOf(_birthDetail.getHoraryNumber()) + "&" +
                        "button1" + "=" + "Get+Kundali" + "&" +
                        "languageCode" + "=" + "0";
        return paramStr;
    }


    /*
    * Fetch data from server
    * parse the data
    * change and set data in planet degree
    * data display on layout
    *
    * */
    private void doActionOnClick(final ICalculationProxy _ICalculationProxy) {
  /*      Calendar c = Calendar.getInstance();
        seconds = c.get(Calendar.SECOND);
        min = c.get(Calendar.MINUTE);
        hrs = c.get(Calendar.HOUR_OF_DAY);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);*/
        String city = _birthDetail.getPlace().getCityName();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = CGlobalVariables.CALCULATION_WEB_SERVICE_URL + "?" + getParameterString();
        Log.e("Url for tramsit", "" + url);
        //String url = " http://akxml.astrosage.com/freekphorary/chartxmlv2.asp?name=test1&sex=M&day=15&month=8&year=1984&hrs=2&min=29&sec=11& dst=0&place=Agra&LongDeg=078&LongMin=00&LongEW=E&LatDeg=027&LatMin=09&LatNS=N&timeZone=5.5& ayanamsa=0&kphn=0&button1=Get+Kundali&languageCode=0&charting=0";
        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    String responseStr = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    responseStr = responseStr.replace("\n", "").replace("\r", "");
                    Log.e("TEST_KUNDLI", "transit resp = " + responseStr);
                    initValuesForTransitPlanet(responseStr);
                    setTransitPlanetData(_ICalculationProxy);
                    transitFragment.displayTransitKundli(1);
                    transitFragment.dismissProgressbar();
                    isShowToast = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                transitFragment.dismissProgressbar();
                if (isShowToast) {
                    if (error instanceof NoConnectionError) {
                        MyCustomToast mct2 = new MyCustomToast(
                                context,
                                ((Activity) context).getLayoutInflater(),
                                ((Activity) context), Typeface.DEFAULT);
                        mct2.show(context.getResources().getString(R.string.no_internet));
                    } else {
                        MyCustomToast mct1 = new MyCustomToast(
                                context,
                                ((Activity) context).getLayoutInflater(),
                                ((Activity) context), Typeface.DEFAULT);
                        mct1.show("Error on server");
                    }
                }
                Log.i("error", error.toString());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);
    }

    private class CacheRequest extends Request<NetworkResponse> {
        private final Response.Listener<NetworkResponse> mListener;
        private final Response.ErrorListener mErrorListener;

        public CacheRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
        }


        @Override
        protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
            if (cacheEntry == null) {
                cacheEntry = new Cache.Entry();
            }
            final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
            final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
            long now = System.currentTimeMillis();
            final long softExpire = now + cacheHitButRefreshed;
            final long ttl = now + cacheExpired;
            cacheEntry.data = response.data;
            cacheEntry.softTtl = softExpire;
            cacheEntry.ttl = ttl;
            String headerValue;
            Map<String, String> map = response.headers;
            headerValue = response.headers.get("Date");
            if (headerValue != null) {
                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }
            headerValue = response.headers.get("Last-Modified");
            if (headerValue != null) {
                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }
            cacheEntry.responseHeaders = response.headers;
            Response<NetworkResponse> responseResponse = Response.success(response, cacheEntry);
            return responseResponse;
        }

        @Override
        protected void deliverResponse(NetworkResponse response) {
            mListener.onResponse(response);
        }

        @Override
        protected VolleyError parseNetworkError(VolleyError volleyError) {
            return super.parseNetworkError(volleyError);
        }

        @Override
        public void deliverError(VolleyError error) {
            mErrorListener.onErrorResponse(error);
        }
    }


    /*
     * Parsing the transit data and store the values in respective variables
     * */
    private void initValuesForTransitPlanet(String strData) {
        double _dValues = 0.0;
        long _Id = 0;
        String _sValues = "", _sDirect = "", _sCombust = "", _sRelation = "";

        //PUT THESE VALU IN PREFERENCE

        //new CGlobalObjectMiscOperation(t).saveCGlobalObjectValuesInPreference(strData);//ADDED BY BIJENDRA ON 20-JULY-13

        lagna = Double.valueOf(strData.substring(strData.indexOf("<lagna>") + "<lagna>".length(), strData.indexOf("</lagna>")));
        if (lagna >= 360.00)
            lagna -= 360.00;

		 /*  _inKPPlanetsAndCusp.setLagna(Double.valueOf(_dValues));
            _planetDegree.setLagna(Double.valueOf(_dValues));*/

        sun = Double.valueOf(strData.substring(strData.indexOf("<sun>") + "<sun>".length(), strData.indexOf("</sun>")));
            /*_inKPPlanetsAndCusp.setSun(Double.valueOf(_dValues));
            _planetDegree.setSun(Double.valueOf(_dValues));*/

        moon = Double.valueOf(strData.substring(strData.indexOf("<moon>") + "<moon>".length(), strData.indexOf("</moon>")));
        /*	_inKPPlanetsAndCusp.setMoon(Double.valueOf(_dValues));
            _planetDegree.setMoon(Double.valueOf(_dValues));*/

        mars = Double.valueOf(strData.substring(strData.indexOf("<mars>") + "<mars>".length(), strData.indexOf("</mars>")));
            /*_inKPPlanetsAndCusp.setMarsh(Double.valueOf(_dValues));
            _planetDegree.setMarsh(Double.valueOf(_dValues));*/

        mercury = Double.valueOf(strData.substring(strData.indexOf("<mercury>") + "<mercury>".length(), strData.indexOf("</mercury>")));
            /*_inKPPlanetsAndCusp.setMercury(Double.valueOf(_dValues));
            _planetDegree.setMercury(Double.valueOf(_dValues));*/

        jupitor = Double.valueOf(strData.substring(strData.indexOf("<jupiter>") + "<jupiter>".length(), strData.indexOf("</jupiter>")));
            /*_inKPPlanetsAndCusp.setJup(Double.valueOf(_dValues));
            _planetDegree.setJup(Double.valueOf(_dValues));*/

        venus = Double.valueOf(strData.substring(strData.indexOf("<venus>") + "<venus>".length(), strData.indexOf("</venus>")));
            /*_inKPPlanetsAndCusp.setVenus(Double.valueOf(_dValues));
            _planetDegree.setVenus(Double.valueOf(_dValues))*/
        ;

        saturn = Double.valueOf(strData.substring(strData.indexOf("<saturn>") + "<saturn>".length(), strData.indexOf("</saturn>")));
            /*_inKPPlanetsAndCusp.setSat(Double.valueOf(_dValues));
            _planetDegree.setSat(Double.valueOf(_dValues));*/

        rahu = Double.valueOf(strData.substring(strData.indexOf("<rahu>") + "<rahu>".length(), strData.indexOf("</rahu>")));
        /*	_inKPPlanetsAndCusp.setRahu(Double.valueOf(_dValues));
            _planetDegree.setRahu(Double.valueOf(_dValues));*/

        ketu = Double.valueOf(strData.substring(strData.indexOf("<ketu>") + "<ketu>".length(), strData.indexOf("</ketu>")));
/*			_inKPPlanetsAndCusp.setKetu(Double.valueOf(_dValues));
            _planetDegree.setKetu(Double.valueOf(_dValues));*/

        uranus = Double.valueOf(strData.substring(strData.indexOf("<uranus>") + "<uranus>".length(), strData.indexOf("</uranus>")));

			/*_inKPPlanetsAndCusp.setUranus(Double.valueOf(_dValues));
            _planetDegree.setUranus(Double.valueOf(_dValues));*/

        neptune = Double.valueOf(strData.substring(strData.indexOf("<neptune>") + "<neptune>".length(), strData.indexOf("</neptune>")));

			/*_inKPPlanetsAndCusp.setNeptune(Double.valueOf(_dValues));
            _planetDegree.setNeptune(Double.valueOf(_dValues));*/

        pluto = Double.valueOf(strData.substring(strData.indexOf("<pluto>") + "<pluto>".length(), strData.indexOf("</pluto>")));
            /*_inKPPlanetsAndCusp.setPluto(Double.valueOf(_dValues));
            _planetDegree.setPluto(Double.valueOf(_dValues));*/


        kPCuspLongitude[0] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp1>") + "<kpcusp1>".length(), strData.indexOf("</kpcusp1>")));
            /*_inKPPlanetsAndCusp.setAscedent(Double.valueOf(_dValues));
            _inKPPlanetsAndCusp.setKpCusp1(Double.valueOf(_dValues));*/


        kPCuspLongitude[1] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp2>") + "<kpcusp2>".length(), strData.indexOf("</kpcusp2>")));
        /*_inKPPlanetsAndCusp.setKpCusp2(Double.valueOf(_dValues));*/


        kPCuspLongitude[2] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp3>") + "<kpcusp3>".length(), strData.indexOf("</kpcusp3>")));
//			_inKPPlanetsAndCusp.setKpCusp3(Double.valueOf(_dValues));

        kPCuspLongitude[3] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp4>") + "<kpcusp4>".length(), strData.indexOf("</kpcusp4>")));
//			_inKPPlanetsAndCusp.setKpCusp4(Double.valueOf(_dValues));

        kPCuspLongitude[4] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp5>") + "<kpcusp5>".length(), strData.indexOf("</kpcusp5>")));
//			_inKPPlanetsAndCusp.setKpCusp5(Double.valueOf(_dValues));


        kPCuspLongitude[5] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp6>") + "<kpcusp6>".length(), strData.indexOf("</kpcusp6>")));
//			_inKPPlanetsAndCusp.setKpCusp6(Double.valueOf(_dValues));


        kPCuspLongitude[6] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp7>") + "<kpcusp7>".length(), strData.indexOf("</kpcusp7>")));
//			_inKPPlanetsAndCusp.setKpCusp7(Double.valueOf(_dValues));


        kPCuspLongitude[7] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp8>") + "<kpcusp8>".length(), strData.indexOf("</kpcusp8>")));
//			_inKPPlanetsAndCusp.setKpCusp8(Double.valueOf(_dValues));


        kPCuspLongitude[8] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp9>") + "<kpcusp9>".length(), strData.indexOf("</kpcusp9>")));
//			_inKPPlanetsAndCusp.setKpCusp9(Double.valueOf(_dValues));


        kPCuspLongitude[9] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp10>") + "<kpcusp10>".length(), strData.indexOf("</kpcusp10>")));
//			_inKPPlanetsAndCusp.setKpCusp10(Double.valueOf(_dValues));


        kPCuspLongitude[10] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp11>") + "<kpcusp11>".length(), strData.indexOf("</kpcusp11>")));
//			_inKPPlanetsAndCusp.setKpCusp11(Double.valueOf(_dValues));


        kPCuspLongitude[11] = Double.valueOf(strData.substring(strData.indexOf("<kpcusp12>") + "<kpcusp12>".length(), strData.indexOf("</kpcusp12>")));
//			_inKPPlanetsAndCusp.setKpCusp12(Double.valueOf(_dValues));


        _sValues = strData.substring(strData.indexOf("<planetsignification1>") + "<planetsignification1>".length(), strData.indexOf("</planetsignification1>"));
        String[] temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[0][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setSunSignification(_sValues);


        _sValues = strData.substring(strData.indexOf("<planetsignification2>") + "<planetsignification2>".length(), strData.indexOf("</planetsignification2>"));
        temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[1][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setMoonSignification(_sValues);


        _sValues = strData.substring(strData.indexOf("<planetsignification3>") + "<planetsignification3>".length(), strData.indexOf("</planetsignification3>"));
        temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[2][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setMarsSignification(_sValues);


        _sValues = strData.substring(strData.indexOf("<planetsignification4>") + "<planetsignification4>".length(), strData.indexOf("</planetsignification4>"));
        temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[3][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setMercurySignification(_sValues);


        _sValues = strData.substring(strData.indexOf("<planetsignification5>") + "<planetsignification5>".length(), strData.indexOf("</planetsignification5>"));
        temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[4][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setJupiterSignification(_sValues);


        _sValues = strData.substring(strData.indexOf("<planetsignification6>") + "<planetsignification6>".length(), strData.indexOf("</planetsignification6>"));
        temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[5][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setVenusSignification(_sValues);


        _sValues = strData.substring(strData.indexOf("<planetsignification7>") + "<planetsignification7>".length(), strData.indexOf("</planetsignification7>"));
        temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[6][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setSaturnSignification(_sValues);


        _sValues = strData.substring(strData.indexOf("<planetsignification8>") + "<planetsignification8>".length(), strData.indexOf("</planetsignification8>"));
        temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[7][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setRahuSignification(_sValues);


        _sValues = strData.substring(strData.indexOf("<planetsignification9>") + "<planetsignification9>".length(), strData.indexOf("</planetsignification9>"));
        temp = _sValues.split(" ");

        for (int i = 0; i < temp.length; i++) {
            kPPlanetSignification[8][i] = Integer.valueOf(temp[i]);
        }
//			_inKpPlanetSignification.setKetuSignification(_sValues);


        kPAyanamsaLongitude = Double.valueOf(strData.substring(strData.indexOf("<kpayan>") + "<kpayan>".length(), strData.indexOf("</kpayan>")));
//			_inKPPlanetsAndCusp.setKpAyan(_dValues);


        double _dFortuna = Double.valueOf(strData.substring(strData.indexOf("<fortuna>") + "<fortuna>".length(), strData.indexOf("</fortuna>")));
        if (_dFortuna >= 360.00)
            _dFortuna = _dFortuna - 360.00;
        fortuna = _dFortuna;

//			_inKPPlanetsAndCusp.setKpFortuna(_dFortuna);

        _sValues = strData.substring(strData.indexOf("<RPDayLord>") + "<RPDayLord>".length(), strData.indexOf("</RPDayLord>"));
        kPDayLordName = _sValues;
        //Log.e("Bijendra_DayLoard-1", _sValues);
//			_inKPPlanetsAndCusp.setDayLord(CUtils.getFullNameofDayLord(t,_sValues));
//			Log.d("DayLoard-2", CUtils.getFullNameofDayLord(t,_sValues));


        //SHODASHVARGA
//			 OutShodashvarga _outShodashvarga=new OutShodashvarga();
        _sValues = strData.substring(strData.indexOf("<shodashvarga_lagna>") + "<shodashvarga_lagna>".length(), strData.indexOf("</shodashvarga_lagna>"));
        String[] temp2 = new String[13];
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[0][i] = Integer.valueOf(temp2[i]);
        }

//			_outShodashvarga.set_lagna(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_hora>") + "<shodashvarga_hora>".length(), strData.indexOf("</shodashvarga_hora>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[1][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_hora(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_drekkana>") + "<shodashvarga_drekkana>".length(), strData.indexOf("</shodashvarga_drekkana>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[2][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_drekkana(_sValues);


        _sValues = strData.substring(strData.indexOf("<shodashvarga_chaturthamsha>") + "<shodashvarga_chaturthamsha>".length(), strData.indexOf("</shodashvarga_chaturthamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[3][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_chaturthamsha(_sValues);


        _sValues = strData.substring(strData.indexOf("<shodashvarga_saptamamsha>") + "<shodashvarga_saptamamsha>".length(), strData.indexOf("</shodashvarga_saptamamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[4][i] = Integer.valueOf(temp2[i]);
        }
//						_outShodashvarga.set_saptamamsha(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_navamsha>") + "<shodashvarga_navamsha>".length(), strData.indexOf("</shodashvarga_navamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[5][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_navamsha(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_dashamamsha>") + "<shodashvarga_dashamamsha>".length(), strData.indexOf("</shodashvarga_dashamamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[6][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_dashamamsha(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_dwadashamamsha>") + "<shodashvarga_dwadashamamsha>".length(), strData.indexOf("</shodashvarga_dwadashamamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[7][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_dwadashamamsha(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_shodashamsha>") + "<shodashvarga_shodashamsha>".length(), strData.indexOf("</shodashvarga_shodashamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[8][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_shodashamsha(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_vimshamsha>") + "<shodashvarga_vimshamsha>".length(), strData.indexOf("</shodashvarga_vimshamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[9][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_vimshamsha(_sValues);

        //ADDED BY BIJENDRA ON 4-JULY-13,INTERCHANGED WITH D-27
        //_sValues=strData.substring(strData.indexOf("<shodashvarga_chaturvimshamsha>")+"<shodashvarga_chaturvimshamsha>".length(), strData.indexOf("</shodashvarga_chaturvimshamsha>"));
        _sValues = strData.substring(strData.indexOf("<shodashvarga_saptavimshamsha>") + "<shodashvarga_saptavimshamsha>".length(), strData.indexOf("</shodashvarga_saptavimshamsha>"));
        //END
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[10][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_chaturvimshamsha(_sValues);


        //ADDED BY BIJENDRA ON 4-JULY-13,INTERCHANGED WITH D-24
        //_sValues=strData.substring(strData.indexOf("<shodashvarga_saptavimshamsha>")+"<shodashvarga_saptavimshamsha>".length(), strData.indexOf("</shodashvarga_saptavimshamsha>"));
        _sValues = strData.substring(strData.indexOf("<shodashvarga_chaturvimshamsha>") + "<shodashvarga_chaturvimshamsha>".length(), strData.indexOf("</shodashvarga_chaturvimshamsha>"));
        //END
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[11][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_saptavimshamsha(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_trimshamsha>") + "<shodashvarga_trimshamsha>".length(), strData.indexOf("</shodashvarga_trimshamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[12][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_trimshamsha(_sValues);


        _sValues = strData.substring(strData.indexOf("<shodashvarga_khavedamsha>") + "<shodashvarga_khavedamsha>".length(), strData.indexOf("</shodashvarga_khavedamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[13][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_khavedamsha(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_akshvedamsha>") + "<shodashvarga_akshvedamsha>".length(), strData.indexOf("</shodashvarga_akshvedamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[14][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_akshvedamsha(_sValues);

        _sValues = strData.substring(strData.indexOf("<shodashvarga_shastiamsha>") + "<shodashvarga_shastiamsha>".length(), strData.indexOf("</shodashvarga_shastiamsha>"));
        temp2 = _sValues.split(",");

        for (int i = 0; i < temp2.length; i++) {
            positionForShodasvarg[15][i] = Integer.valueOf(temp2[i]);
        }
//			_outShodashvarga.set_shastiamsha(_sValues);

//			CGlobal.getCGlobalObject().setOutShodashvargaObject(_outShodashvarga);
        //END SHODASHVARGA

        //ASTAKVARGA
        _sValues = strData.substring(strData.indexOf("<ashtakvarga_sun>") + "<ashtakvarga_sun>".length(), strData.indexOf("</ashtakvarga_sun>"));

        String[] temp3 = new String[13];
        temp3 = _sValues.split(",");

        for (int i = 0; i < temp3.length; i++) {
            ashtakvargaBinduForSignAndPlanet[0][i] = Integer.valueOf(temp3[i]);
        }

//			CGlobal.getCGlobalObject().getOutAstakvargaTableObject().setSun(_sValues);

        _sValues = strData.substring(strData.indexOf("<ashtakvarga_moon>") + "<ashtakvarga_moon>".length(), strData.indexOf("</ashtakvarga_moon>"));
        temp3 = _sValues.split(",");

        for (int i = 0; i < temp3.length; i++) {
            ashtakvargaBinduForSignAndPlanet[1][i] = Integer.valueOf(temp3[i]);
        }
//			CGlobal.getCGlobalObject().getOutAstakvargaTableObject().setMoon(_sValues);


        _sValues = strData.substring(strData.indexOf("<ashtakvarga_mars>") + "<ashtakvarga_mars>".length(), strData.indexOf("</ashtakvarga_mars>"));
        temp3 = _sValues.split(",");

        for (int i = 0; i < temp3.length; i++) {
            ashtakvargaBinduForSignAndPlanet[2][i] = Integer.valueOf(temp3[i]);
        }
//			CGlobal.getCGlobalObject().getOutAstakvargaTableObject().setMars(_sValues);


        _sValues = strData.substring(strData.indexOf("<ashtakvarga_mercury>") + "<ashtakvarga_mercury>".length(), strData.indexOf("</ashtakvarga_mercury>"));
        temp3 = _sValues.split(",");

        for (int i = 0; i < temp3.length; i++) {
            ashtakvargaBinduForSignAndPlanet[3][i] = Integer.valueOf(temp3[i]);
        }
//			CGlobal.getCGlobalObject().getOutAstakvargaTableObject().setMercury(_sValues);


        _sValues = strData.substring(strData.indexOf("<ashtakvarga_jupiter>") + "<ashtakvarga_jupiter>".length(), strData.indexOf("</ashtakvarga_jupiter>"));
        temp3 = _sValues.split(",");

        for (int i = 0; i < temp3.length; i++) {
            ashtakvargaBinduForSignAndPlanet[4][i] = Integer.valueOf(temp3[i]);
        }
//			CGlobal.getCGlobalObject().getOutAstakvargaTableObject().setJupiter(_sValues);


        _sValues = strData.substring(strData.indexOf("<ashtakvarga_venus>") + "<ashtakvarga_venus>".length(), strData.indexOf("</ashtakvarga_venus>"));
        temp3 = _sValues.split(",");

        for (int i = 0; i < temp3.length; i++) {
            ashtakvargaBinduForSignAndPlanet[5][i] = Integer.valueOf(temp3[i]);
        }
//			CGlobal.getCGlobalObject().getOutAstakvargaTableObject().setVenus(_sValues);


        _sValues = strData.substring(strData.indexOf("<ashtakvarga_saturn>") + "<ashtakvarga_saturn>".length(), strData.indexOf("</ashtakvarga_saturn>"));
        temp3 = _sValues.split(",");

        for (int i = 0; i < temp3.length; i++) {
            ashtakvargaBinduForSignAndPlanet[6][i] = Integer.valueOf(temp3[i]);
        }
//			CGlobal.getCGlobalObject().getOutAstakvargaTableObject().setSat(_sValues);

        _sValues = strData.substring(strData.indexOf("<ashtakvarga_total>") + "<ashtakvarga_total>".length(), strData.indexOf("</ashtakvarga_total>"));
        temp3 = _sValues.split(",");

        for (int i = 0; i < temp3.length; i++) {
            totalAshtakVargaValue[i] = Integer.valueOf(temp3[i]);
        }
//			CGlobal.getCGlobalObject().getOutAstakvargaTableObject().setTotal(_sValues);


        //END ASTAKVARGA

        setPanchangValues(strData);

        ayanamsa = Double.valueOf(strData.substring(strData.indexOf("<ayan>") + "<ayan>".length(), strData.indexOf("</ayan>")));

//		_planetDegree.setAyan(Double.valueOf(_dValues));

        _sCombust = strData.substring(strData.indexOf("<combust>") + "<combust>".length(), strData.indexOf("</combust>"));
        String[] temp4 = new String[13];
        temp4 = _sCombust.split(",");
        String[] s2 = new String[13];
        for (int i = 0; i <= 10; i++) {
            temp4[i] = temp4[i].replace(" ", "");
            s2[i + 1] = temp4[i];
        }
        for (int i = 1; i <= 11; i++) {
            int x = Integer.valueOf(s2[i]);
            if (x == 1)
                combust[i] = true;
            else
                combust[i] = false;
        }

        _sDirect = strData.substring(strData.indexOf("<Diret>") + "<Diret>".length(), strData.indexOf("</Diret>"));
        temp4 = _sDirect.split(",");
        String[] s = new String[13];
        for (int i = 0; i <= 11; i++) {
            temp4[i] = temp4[i].replace(" ", "");
            s[i + 1] = temp4[i];
        }
        for (int i = 1; i <= 12; i++) {
            if (Integer.valueOf(s[i]) == 1)
                planetDirect[i] = true;
            else
                planetDirect[i] = false;
        }

        _sRelation = strData.substring(strData.indexOf("<relation>") + "<relation>".length(), strData.indexOf("</relation>"));
        temp4 = _sRelation.split(",");
        String[] s3 = new String[12];
        for (int i = 0; i <= 11; i++) {
            temp4[i] = temp4[i].replace(" ", "");
            s3[i] = temp4[i];
        }
        for (int i = 0; i <= 11; i++) {
            planetRelation[i] = s3[i];
        }

        // CGlobalVariables._isInternetSlow=false;


    }


    /**
     * This function extract panchang values from the passed string
     * and fill in the object.
     *
     * @param strData
     * @return void
     */
    private void setPanchangValues(String strData) {
        double _dValues = 0.0;
        long _Id = 0;
        String _sValues = "";


        //Toast.makeText(t,"LANG-Code="+String.valueOf(CGlobalVariables.LANGUAGE_CODE), Toast.LENGTH_LONG).show();
        // GET PANCHANG
//		OutPanchang _outOutPanchang=new OutPanchang();
        _sValues = strData.substring(strData.indexOf("<sunrise>") + "<sunrise>".length(), strData.indexOf("</sunrise>"));
        sunRiseTimeHms = _sValues;
//		_outOutPanchang.set_SunRise(_sValues);


        _sValues = strData.substring(strData.indexOf("<sunset>") + "<sunset>".length(), strData.indexOf("</sunset>"));
        sunSetTimeHms = _sValues;
//		_outOutPanchang.set_SunSet(_sValues);


        _sValues = strData.substring(strData.indexOf("<tithi>") + "<tithi>".length(), strData.indexOf("</tithi>"));
        tithiName = _sValues;
//		_outOutPanchang.setTitAtBirth(_sValues);


        _sValues = strData.substring(strData.indexOf("<hindu_week_day>") + "<hindu_week_day>".length(), strData.indexOf("</hindu_week_day>"));

        String[] Weekdays = new String[7];
        Weekdays[0] = "Monday";
        Weekdays[1] = "Tuesday";
        Weekdays[2] = "Wednesday";
        Weekdays[3] = "Thursday";
        Weekdays[4] = "Friday";
        Weekdays[5] = "Saturday";
        Weekdays[6] = "Sunday";

        for (int i = 0; i <= 6; i++) {
            if (Weekdays[i].equalsIgnoreCase(_sValues)) {
                hinduWeekday = i;
                break;
            }
        }
//		hinduWeekday = Integer.valueOf(_sValues);
//		_outOutPanchang.set_HinduWeekDay(_sValues);


        _sValues = strData.substring(strData.indexOf("<paksha>") + "<paksha>".length(), strData.indexOf("</paksha>"));
        pakshaName = _sValues;
//		_outOutPanchang.setPaksha(_sValues);


        _sValues = strData.substring(strData.indexOf("<yoga>") + "<yoga>".length(), strData.indexOf("</yoga>"));
        yoganame = _sValues;
//		_outOutPanchang.setYoga(_sValues);

        _sValues = strData.substring(strData.indexOf("<Karan>") + "<Karan>".length(), strData.indexOf("</Karan>"));
        karanName = _sValues;
//		_outOutPanchang.setKaran(_sValues);


//		CGlobal.getCGlobalObject().setOutPanchangObject(_outOutPanchang);
        //END PANCHANG
    }


    // set planets for transit
    private void setTransitPlanetData(ICalculationProxy _ICalculationProxy) throws Exception {

        //InKpPlanetSignification _inKpPlanetSignification = new InKpPlanetSignification();
        //InKPPlanetsAndCusp _inKPPlanetsAndCusp = new InKPPlanetsAndCusp();
        PlanetData _planetDegree = new PlanetData();
        //OutShodashvarga _outShodashvarga = new OutShodashvarga();
        //OutPanchang _outOutPanchang = new OutPanchang();
        _planetDegree.setLagna(_ICalculationProxy.getLagna2());
        _planetDegree.setSun(_ICalculationProxy.getSun());
        _planetDegree.setMoon(_ICalculationProxy.getMoon());
        _planetDegree.setMarsh(_ICalculationProxy.getMars());
        _planetDegree.setMercury(_ICalculationProxy.getMercury());
        _planetDegree.setJup(_ICalculationProxy.getJupitor());
        _planetDegree.setVenus(_ICalculationProxy.getVenus());
        _planetDegree.setSat(_ICalculationProxy.getSaturn());
        _planetDegree.setRahu(_ICalculationProxy.getRahu());
        _planetDegree.setKetu(_ICalculationProxy.getKetu());
        _planetDegree.setUranus(_ICalculationProxy.getUranus());
        _planetDegree.setNeptune(_ICalculationProxy.getNeptune());
        _planetDegree.setPluto(_ICalculationProxy.getPluto());
        /*_inKPPlanetsAndCusp.setLagna(_ICalculationProxy.getLagna2());


        _inKPPlanetsAndCusp.setSun(_ICalculationProxy.getSun());


        _inKPPlanetsAndCusp.setMoon(_ICalculationProxy.getMoon());


        _inKPPlanetsAndCusp.setMarsh(_ICalculationProxy.getMars());


        _inKPPlanetsAndCusp.setMercury(_ICalculationProxy.getMercury());


        _inKPPlanetsAndCusp.setJup(_ICalculationProxy.getJupitor());


        _inKPPlanetsAndCusp.setVenus(_ICalculationProxy.getVenus());


        _inKPPlanetsAndCusp.setSat(_ICalculationProxy.getSaturn());


        _inKPPlanetsAndCusp.setRahu(_ICalculationProxy.getRahu());


        _inKPPlanetsAndCusp.setKetu(_ICalculationProxy.getKetu());


        _inKPPlanetsAndCusp.setUranus(_ICalculationProxy.getUranus());


        _inKPPlanetsAndCusp.setNeptune(_ICalculationProxy.getNeptune());


        _inKPPlanetsAndCusp.setPluto(_ICalculationProxy.getPluto());


        _inKPPlanetsAndCusp.setAscedent(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.ASCEDENT + 1));

        _inKPPlanetsAndCusp.setKpCusp1(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP1 + 1));

        _inKPPlanetsAndCusp.setKpCusp2(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP2 + 1));

        _inKPPlanetsAndCusp.setKpCusp3(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP3 + 1));

        _inKPPlanetsAndCusp.setKpCusp4(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP4 + 1));

        _inKPPlanetsAndCusp.setKpCusp5(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP5 + 1));

        _inKPPlanetsAndCusp.setKpCusp6(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP6 + 1));

        _inKPPlanetsAndCusp.setKpCusp7(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP7 + 1));

        _inKPPlanetsAndCusp.setKpCusp8(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP8 + 1));

        _inKPPlanetsAndCusp.setKpCusp9(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP9 + 1));

        _inKPPlanetsAndCusp.setKpCusp10(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP10 + 1));

        _inKPPlanetsAndCusp.setKpCusp11(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP11 + 1));

        _inKPPlanetsAndCusp.setKpCusp12(_ICalculationProxy
                .getKPCuspLongitude(CGlobalVariables.KPCUSP12 + 1));

        _inKpPlanetSignification
                .setSunSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.SUN_SIG + 1)));

        _inKpPlanetSignification
                .setMoonSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.Moon_SIG + 1)));

        _inKpPlanetSignification
                .setMarsSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.MARS_SIG + 1)));

        _inKpPlanetSignification
                .setMercurySignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.MERCURY_SIG + 1)));

        _inKpPlanetSignification
                .setJupiterSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.JUPITER_SIG + 1)));

        _inKpPlanetSignification
                .setVenusSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.VENUS_SIG + 1)));

        _inKpPlanetSignification
                .setSaturnSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.SATURN_SIG + 1)));

        _inKpPlanetSignification
                .setRahuSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.RAHU_SIG + 1)));

        _inKpPlanetSignification
                .setKetuSignification(CUtils.getFormattedSignification(_ICalculationProxy
                        .getKPPlanetSignification(CGlobalVariables.KETU_SIG + 1)));

        _inKPPlanetsAndCusp.setKpAyan(_ICalculationProxy
                .getKPAyanamsaLongitude());

        _inKPPlanetsAndCusp.setKpFortuna(_ICalculationProxy.getFortuna());

        // CODE UPDATED ON 16-SEP-13 (BIJENDRA)
        // _inKPPlanetsAndCusp.setDayLord(CUtils.getFullNameofDayLord(CGlobal.getCGlobalObject().getContext(),
        // _ICalculationProxy.getKPDayLordName()));
        _inKPPlanetsAndCusp.setDayLord(_ICalculationProxy.getKPDayLordName());*/
        // END

        /*_outShodashvarga.set_lagna(CUtils
                .getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.LAGNA)));
        _outShodashvarga.set_hora(CUtils
                .getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.HORA)));
        _outShodashvarga.set_drekkana(CUtils
                .getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.DREKKANA)));
        _outShodashvarga
                .set_chaturthamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.CHATURTHAMSHA)));
        _outShodashvarga
                .set_saptamamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.SAPTAMAMSHA)));
        _outShodashvarga.set_navamsha(CUtils
                .getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.NAVAMSHA)));
        _outShodashvarga
                .set_dashamamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.DASHAMAMSHA)));
        _outShodashvarga
                .set_dwadashamamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.DWADASHAMAMSHA)));
        _outShodashvarga
                .set_shodashamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.SHODASHAMSHA)));
        _outShodashvarga
                .set_vimshamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.VIMSHAMSHA)));
        _outShodashvarga
                .set_chaturvimshamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.CHATURVIMSHAMSHA)));
        _outShodashvarga
                .set_saptavimshamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.SAPTAVIMSHAMSHA)));
        _outShodashvarga
                .set_trimshamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.TRIMSHAMSHA)));
        _outShodashvarga
                .set_khavedamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.KHAVEDAMSHA)));
        _outShodashvarga
                .set_akshvedamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.AKSHVEDAMSHA)));
        _outShodashvarga
                .set_shastiamsha(CUtils.getFormattedShodashAndAshtak(_ICalculationProxy
                        .getPositionForShodasvarg(CGlobalVariables.SHASTIAMSHA)));
*/
        //CGlobal.getCGlobalObject().setOutShodashvargaObject(_outShodashvarga);

       /* int[][] astak = new int[7][12];

        for (int j = 0; j <= 6; j++) {
            for (int i = 0; i <= 11; i++) {
                astak[j][i] = _ICalculationProxy
                        .getAshtakvargaBinduForSignAndPlanet(j, i);
            }
        }*/

        /*CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setSun(CUtils
                        .getFormattedShodashAndAshtak(astak[CGlobalVariables.SUN_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setMoon(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.Moon_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setMars(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.MARS_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setMercury(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.MERCURY_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setJupiter(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.JUPITER_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setVenus(
                        CUtils.getFormattedShodashAndAshtak(astak[CGlobalVariables.VENUS_ASTAK]));
        CGlobal.getCGlobalObject()
                .getOutAstakvargaTableObject()
                .setSat(CUtils
                        .getFormattedShodashAndAshtak(astak[CGlobalVariables.SATURN_ASTAK]));
*/
        //int[] total = _ICalculationProxy.getTotalAshtakVargaValue();
       /* CGlobal.getCGlobalObject().getOutAstakvargaTableObject()
                .setTotal(CUtils.getFormattedShodashAndAshtak(total));*/

       /* _outOutPanchang.set_SunRise(_ICalculationProxy.getSunRiseTimeHms());
        _outOutPanchang.set_SunSet(_ICalculationProxy.getSunSetTimeHms());
        _outOutPanchang.setTitAtBirth(_ICalculationProxy.getTithiName());
        _outOutPanchang.set_HinduWeekDay(String.valueOf(_ICalculationProxy.getHinduWeekday()));
        _outOutPanchang.setPaksha(_ICalculationProxy.getPakshaName());
        _outOutPanchang.setYoga(_ICalculationProxy.getYoganame());
        _outOutPanchang.setKaran(_ICalculationProxy.getKaranName());*/
        //CGlobal.getCGlobalObject().setOutPanchangObject(_outOutPanchang);

        _planetDegree.setAyan(Double.valueOf(_ICalculationProxy.getAyanamsa()));

        String isCum = "";
        for (int i = 1; i < 12; i++) {
            if (_ICalculationProxy.isCombust(i)) {
                if (i == 1)
                    isCum += "1";
                else
                    isCum += ",1";
            } else {
                if (i == 1)
                    isCum += "0";
                else
                    isCum += ",0";
            }
        }

        _planetDegree.setCombust(isCum);

        String isDir = "";
        for (int i = 1; i < 13; i++) {
            if (_ICalculationProxy.isPlanetDirect(i)) {
                if (i == 1)
                    isDir += "1";
                else
                    isDir += ",1";
            } else {
                if (i == 1)
                    isDir += "0";
                else
                    isDir += ",0";
            }
        }

        _planetDegree.setDirect(isDir);

        // Abhishek for Relation
        String relation = "";
        for (int i = 0; i <= 11; i++) {
            if (i == 0)
                relation = relation + planetRelation[i];
            else
                relation = relation +","+ planetRelation[i];

        }

        _planetDegree.setRelation(relation);
        // fillKPPlanets(_planetDegree, _inKPPlanetsAndCusp);

        CGlobal.getCGlobalObject().setPlanetDataObjectTransit(_planetDegree);
       /* CGlobal.getCGlobalObject().setInKPPlanetsAndCuspObject(
                fillKPPlanets(_planetDegree, _inKPPlanetsAndCusp));
        CGlobal.getCGlobalObject().setInKpPlanetSignificationObject(
                _inKpPlanetSignification);*/
    }

    private InKPPlanetsAndCusp fillKPPlanets(PlanetData planetDegree, InKPPlanetsAndCusp inKPPlanetsAndCusp) {
        PlanetData _planetDegree = planetDegree;
        InKPPlanetsAndCusp _inKPPlanetsAndCusp = inKPPlanetsAndCusp;
        double tempCalculation = 0;
        double ayanDiff = _planetDegree.getAyan() - _inKPPlanetsAndCusp.getKpAyan();

        // for sun
        tempCalculation = _planetDegree.getSun() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setSun(tempCalculation);


        // for moon
        tempCalculation = _planetDegree.getMoon() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setMoon(tempCalculation);


        //for marsh
        tempCalculation = _planetDegree.getMarsh() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setMarsh(tempCalculation);

        //for mercury
        tempCalculation = _planetDegree.getMercury() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setMercury(tempCalculation);


        //for jupiter
        tempCalculation = _planetDegree.getJup() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setJup(tempCalculation);

        // for venus
        tempCalculation = _planetDegree.getVenus() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setVenus(tempCalculation);

        //for sat
        tempCalculation = _planetDegree.getSat() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setSat(tempCalculation);

        // for rahu
        tempCalculation = _planetDegree.getRahu() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setRahu(tempCalculation);

        // for ketu
        tempCalculation = _planetDegree.getKetu() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setKetu(tempCalculation);

        // for nept
        tempCalculation = _planetDegree.getNeptune() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setNeptune(tempCalculation);

        //for pluto
        tempCalculation = _planetDegree.getPluto() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setPluto(_planetDegree.getPluto() + ayanDiff);

        //for uranus
        tempCalculation = _planetDegree.getUranus() + ayanDiff;
        if (tempCalculation < 0)
            tempCalculation += 360.00;
        _inKPPlanetsAndCusp.setUranus(tempCalculation);

        return _inKPPlanetsAndCusp;

    }

}
