package com.ojassoft.astrosage.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class GetDefaultKundliDataService extends IntentService implements VolleyResponse {
    //AlarmManager alarmManager;
    int LANGUAGE_CODE = 0;

    public GetDefaultKundliDataService() {
        super("GetDefaultKundliDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*
         * BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo)
         * CUtils .getCustomObject(getApplicationContext());
         */
        // LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();

        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) intent
                .getExtras().get("beanHoroPersonalInfo");

        try {


            CUtils.vollyPostRequest(this, CGlobalVariables.USER_DASHA_DETAILS, getParams(beanHoroPersonalInfo), 0);

            Log.e("LoadMore URL ", CGlobalVariables.USER_DASHA_DETAILS);
           /* String defaultKundliData = executeHTTPGetRequest(beanHoroPersonalInfo,
                    CGlobalVariables.USER_DASHA_DETAILS);


            if (!defaultKundliData.equals("") && defaultKundliData.contains("ShaniSadeSati")
                    && defaultKundliData.contains("Mahadsha") && defaultKundliData.contains("AnterDasha")) {
                //save default kundli data in preference
                CUtils.saveStringData(getApplicationContext(), "defaultKundliData", defaultKundliData);
                //save current mahadasha info
                saveCurrentMahadashaIndex(defaultKundliData);
                //save current sadesati info
                saveCurrentSadesatiIndex(defaultKundliData);
                //save current natardasa info
                saveCurrentAnterdashaIndex(defaultKundliData);
            }*/


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

   /* private String executeHTTPGetRequest(
            BeanHoroPersonalInfo beanHoroPersonalInfo, String url)
            throws Exception {
        // String strReturn = CGlobalVariables.OUT_PUT_DATA;
        HttpResponse response = null;// ADDED BY BIJENDRA ON 05-MAY-15
        String strReturn = "";
        BufferedReader in = null;
        HttpClient hc = CUtils.getNewHttpClient();
        // HttpClient hc = new DefaultHttpClient(getHttpClientTimeoutParameter());

        HttpPost hp = new HttpPost(url);
        try {

            hp.setEntity(new UrlEncodedFormEntity(
                    getPrepareNameValuePairForKundli(beanHoroPersonalInfo)));
            response = hc.execute(hp);

            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String data = "";
            while ((data = in.readLine()) != null)
                sb.append(data);
            in.close();
            strReturn = sb.toString();

        } catch (Exception e) {
            strReturn = "";
            throw e;

        } finally {
            if (in != null) {
                try {

                    in.close();
                } catch (Exception e) {

                }
            }
            // ADDED BY BIJENDRA ON 05-MAY-15
            try {
                if (response.getEntity() != null) {
                    response.getEntity().consumeContent();
                }// if
            } catch (Exception e) {

            }
            // END
        }
        return strReturn;
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

   /* private List<NameValuePair> getPrepareNameValuePairForKundli(
            BeanHoroPersonalInfo _birthDetail) {

        String _name = _birthDetail.getName().trim().replace(' ', '_');
        String _mycity = _birthDetail.getPlace().getCityName().trim();
        _mycity = _mycity.substring(0, 3);
        String key = CUtils.getApplicationSignatureHashCode(this);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(22);
        nameValuePairs.add(new BasicNameValuePair("key", key));
        nameValuePairs.add(new BasicNameValuePair("name", _name));
        nameValuePairs.add(new BasicNameValuePair("sex", _birthDetail
                .getGender().trim()));
        nameValuePairs.add(new BasicNameValuePair("day", String.valueOf(
                _birthDetail.getDateTime().getDay()).trim()));
        nameValuePairs.add(new BasicNameValuePair("month", String.valueOf(
                _birthDetail.getDateTime().getMonth() + 1).trim()));
        nameValuePairs.add(new BasicNameValuePair("year", String.valueOf(
                _birthDetail.getDateTime().getYear()).trim()));
        nameValuePairs.add(new BasicNameValuePair("hrs", String.valueOf(
                _birthDetail.getDateTime().getHour()).trim()));
        nameValuePairs.add(new BasicNameValuePair("min", String.valueOf(
                _birthDetail.getDateTime().getMin()).trim()));
        nameValuePairs.add(new BasicNameValuePair("sec", String.valueOf(
                _birthDetail.getDateTime().getSecond()).trim()));
        nameValuePairs.add(new BasicNameValuePair("dst", String
                .valueOf(_birthDetail.getDST())));
        nameValuePairs.add(new BasicNameValuePair("place", _birthDetail
                .getPlace().getCityName().trim()));
        nameValuePairs.add(new BasicNameValuePair("longdeg", _birthDetail
                .getPlace().getLongDeg().trim()));
        nameValuePairs.add(new BasicNameValuePair("longmin", _birthDetail
                .getPlace().getLongMin().trim()));
        nameValuePairs.add(new BasicNameValuePair("longew", _birthDetail
                .getPlace().getLongDir().trim()));
        nameValuePairs.add(new BasicNameValuePair("latdeg", _birthDetail
                .getPlace().getLatDeg().trim()));
        nameValuePairs.add(new BasicNameValuePair("latmin", _birthDetail
                .getPlace().getLatMin().trim()));
        nameValuePairs.add(new BasicNameValuePair("latns", _birthDetail
                .getPlace().getLatDir().trim()));
        nameValuePairs.add(new BasicNameValuePair("timezone", String
                .valueOf(_birthDetail.getPlace().getTimeZoneValue())));
        nameValuePairs.add(new BasicNameValuePair("ayanamsa", String.valueOf(
                _birthDetail.getAyanIndex()).trim()));
        nameValuePairs.add(new BasicNameValuePair("kphn", String
                .valueOf(_birthDetail.getHoraryNumber())));
        nameValuePairs.add(new BasicNameValuePair("langcode", String
                .valueOf(LANGUAGE_CODE)));

        String detail =
                "name=" + _name +
                        "sex=" + _birthDetail.getGender().trim() +
                        "day=" + String.valueOf(_birthDetail.getDateTime().getDay()).trim() +
                        "month=" + String.valueOf(_birthDetail.getDateTime().getMonth() + 1).trim() +
                        "year=" + String.valueOf(_birthDetail.getDateTime().getYear()).trim() +
                        "hrs=" + String.valueOf(_birthDetail.getDateTime().getHour()).trim() +
                        "min=" + String.valueOf(_birthDetail.getDateTime().getMin()).trim() +
                        "sec=" + String.valueOf(_birthDetail.getDateTime().getSecond()).trim() +
                        "dst=" + String.valueOf(_birthDetail.getDST()) +
                        "place=" + _birthDetail.getPlace().getCityName().trim() +
                        "longdeg=" + _birthDetail.getPlace().getLongDeg().trim() +
                        "longmin=" + _birthDetail.getPlace().getLongMin().trim() +
                        "longew=" + _birthDetail.getPlace().getLongDir().trim() +
                        "latdeg=" + _birthDetail.getPlace().getLatDeg().trim() +
                        "latmin=" + _birthDetail.getPlace().getLatMin().trim() +
                        "latns=" + _birthDetail.getPlace().getLatDir().trim() +
                        "timezone=" + String.valueOf(_birthDetail.getPlace().getTimeZoneValue()) +
                        "ayanamsa=" + String.valueOf(_birthDetail.getAyanIndex()).trim() +
                        "kphn=" + String.valueOf(_birthDetail.getHoraryNumber());
        return nameValuePairs;
    }*/


    private void saveCurrentMahadashaIndex(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("Mahadsha");
                if (jsonArray != null) {

                    //if (currentDasha < jsonArray.length()) {
                    CUtils.saveIntData(getApplicationContext(),
                            "currentMahadasha", 1);
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(1);
                    CUtils.saveStringData(getApplicationContext(),
                            "MahadashaStartDate", jsonObject1.getString("MahadashaStartDate"));
                    CUtils.saveStringData(getApplicationContext(),
                            "MahadashaEndDate", jsonObject1.getString("MahadashaEndDate"));
                    CUtils.saveStringData(getApplicationContext(),
                            "PlanetName", jsonObject1.getString("PlanetName"));
                    CUtils.saveStringData(getApplicationContext(),
                            "DateBeforeStartDate", CUtils.getFortyDaysBeforeDate(jsonObject1.getString("MahadashaStartDate"), "dd/MM/yy"));
                    //  }
                }
            }
        } catch (Exception e) {

        }

    }

    private void saveCurrentSadesatiIndex(String jsonString) {
        try {
            int index = 0;
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("ShaniSadeSati");
                if (jsonArray != null) {
                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
                    String startDate = jsonObject1.getString("Start Date");
                    String endDate = jsonObject1.getString("End Date");
                    if (CUtils.checkDate(Calendar.getInstance().getTime(), CUtils.convertStringToDate(startDate, "MMMM dd, yyyy"), CUtils.convertStringToDate(endDate, "MMMM dd, yyyy"))) {
                        index = 1;
                    } else {
                        index = 0;
                    }
                    jsonObject1 = (JSONObject) jsonArray.get(index);
                    startDate = jsonObject1.getString("Start Date");
                    endDate = jsonObject1.getString("End Date");
                    CUtils.saveStringData(getApplicationContext(), "Sadesati", jsonObject1.getString("Sade Sati"));
                    CUtils.saveStringData(getApplicationContext(), "Shanirashi", jsonObject1.getString("Shani Rashi"));
                    CUtils.saveStringData(getApplicationContext(), "SadesatiStartDate", startDate);
                    CUtils.saveStringData(getApplicationContext(), "SadesatiEndDate", endDate);
                    CUtils.saveStringData(getApplicationContext(), "SadestiPhase", jsonObject1.getString("Phase"));
                    String startDateBefore = CUtils.getFortyDaysBeforeDate(jsonObject1.getString("Start Date"), "MMMM dd, yyyy");
                    String endDateBefore = CUtils.getFortyDaysBeforeDate(jsonObject1.getString("End Date"), "MMMM dd, yyyy");
                    CUtils.saveStringData(getApplicationContext(), "SadestiDateBeforeStartDate", startDateBefore);
                    CUtils.saveStringData(getApplicationContext(), "SadestiDateBeforeEndDate", endDateBefore);
                    CUtils.saveIntData(this, "sadesatidashaarrayindex1", index);
                    CUtils.saveIntData(this, "sadesatidashaarrayindex2", index);
                    CUtils.saveIntData(this, "sadesatidashaarrayindex3", index);
                    CUtils.saveIntData(this, "sadesatidashaarrayindex4", index);
                }
            }
        } catch (Exception e) {

        }
    }

    private void saveCurrentAnterdashaIndex(String jsonString) {
        try {
            int arraySize = 0;
            int currentAnterDasha = 0;
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("AnterDasha");
                if (jsonArray != null) {
                    JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                    JSONArray jsonArray2 = jsonArray1.getJSONArray(1);
                    arraySize = jsonArray2.length();
                    for (int i = 0; i < arraySize; i++) {
                        JSONObject innerJsonObject = (JSONObject) jsonArray2.get(i);
                        String sDate = innerJsonObject
                                .getString("AnterDashaStartDate").substring(0, 5);
                        String eDate = innerJsonObject
                                .getString("AnterDashaEndDate").substring(0, 5);
                        boolean bool1 = sDate.contains("00");
                        boolean bool2 = eDate.contains("00");
                        if (bool1) {
                            if (bool2) {
                                continue;
                            } else {
                                Date endDate = CUtils.convertStringToDate(innerJsonObject
                                                .getString("AnterDashaEndDate"),
                                        "dd/MM/yy");
                                boolean bool = CUtils.checkDate(Calendar.getInstance().getTime(), endDate);
                                if (bool) {
                                    continue;
                                } else {
                                    currentAnterDasha = i;
                                    saveCurrentAnterdashaState(currentAnterDasha, arraySize);
                                    break;
                                }
                            }

                        }
                        Date startDate = CUtils.convertStringToDate(
                                innerJsonObject
                                        .getString("AnterDashaStartDate"), "dd/MM/yy");
                        Date endDate = CUtils.convertStringToDate(innerJsonObject
                                        .getString("AnterDashaEndDate"),
                                "dd/MM/yy");

                        if (CUtils.checkDate(Calendar.getInstance().getTime(), startDate, endDate)) {
                            currentAnterDasha = i;
                            break;
                        }
                    }
                    saveCurrentAnterdashaState(currentAnterDasha, arraySize);

                }
            }
        } catch (Exception e) {
            //Log.i("Error", e.getMessage());
        }

    }

    private void saveCurrentAnterdashaState(int currentAnterDasha, int size) {
        if (currentAnterDasha == size - 1) {
            CUtils.saveIntData(getApplicationContext(),
                    "currentPlanet1", 1);
            CUtils.saveIntData(getApplicationContext(),
                    "currentPlanet2", 1);
            CUtils.saveIntData(getApplicationContext(),
                    "currentAnterDashaIndex1", 0);
            CUtils.saveIntData(getApplicationContext(),
                    "currentAnterDashaIndex2", 0);
        } else {
            CUtils.saveIntData(getApplicationContext(),
                    "currentPlanet1", 0);
            CUtils.saveIntData(getApplicationContext(),
                    "currentPlanet2", 0);
            CUtils.saveIntData(getApplicationContext(),
                    "currentAnterDashaIndex1", currentAnterDasha + 1);
            CUtils.saveIntData(getApplicationContext(),
                    "currentAnterDashaIndex2", currentAnterDasha + 1);
        }
    }


    @Override
    public void onResponse(String response, int method) {
        try {
            Log.i("", response);
            Log.e("LoadMore URL ", response);
            if (!response.equals("") && response.contains("ShaniSadeSati")
                    && response.contains("Mahadsha") && response.contains("AnterDasha")) {
                //save default kundli data in preference
                CUtils.saveStringData(getApplicationContext(), "defaultKundliData", response);
                //save current mahadasha info
                saveCurrentMahadashaIndex(response);
                //save current sadesati info
                saveCurrentSadesatiIndex(response);
                //save current natardasa info
                saveCurrentAnterdashaIndex(response);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onError(VolleyError error) {

    }

    private Map<String, String> getParams(BeanHoroPersonalInfo _birthDetail) {
        String _name = _birthDetail.getName().trim().replace(' ', '_');
        String _mycity = _birthDetail.getPlace().getCityName().trim();
        _mycity = _mycity.substring(0, 3);
        String key = CUtils.getApplicationSignatureHashCode(this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key", key);
        params.put("name", _name);
        params.put("sex", _birthDetail.getGender().trim());
        params.put("day", String.valueOf(_birthDetail.getDateTime().getDay()).trim());
        params.put("month", String.valueOf(_birthDetail.getDateTime().getMonth() + 1).trim());
        params.put("year", String.valueOf(_birthDetail.getDateTime().getYear()).trim());
        params.put("hrs", String.valueOf(_birthDetail.getDateTime().getHour()).trim());
        params.put("min", String.valueOf(_birthDetail.getDateTime().getMin()).trim());
        params.put("sec", String.valueOf(_birthDetail.getDateTime().getSecond()).trim());
        params.put("dst", String.valueOf(_birthDetail.getDST()));
        params.put("place", _birthDetail.getPlace().getCityName().trim());
        params.put("longmin", _birthDetail.getPlace().getLongMin().trim());
        params.put("longew", _birthDetail.getPlace().getLongDir().trim());
        params.put("latdeg", _birthDetail.getPlace().getLatDeg().trim());
        params.put("longdeg", _birthDetail.getPlace().getLongDeg().trim());
        params.put("latns", _birthDetail.getPlace().getLatDir().trim());
        params.put("timezone", String.valueOf(_birthDetail.getPlace().getTimeZoneValue()));
        params.put("latmin", _birthDetail.getPlace().getLatMin().trim());
        params.put("ayanamsa", String.valueOf(_birthDetail.getAyanIndex()).trim());
        params.put("kphn", String.valueOf(_birthDetail.getHoraryNumber()));
        params.put("langcode", String.valueOf(LANGUAGE_CODE));
        return params;
    }
}