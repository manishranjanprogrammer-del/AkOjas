package com.ojassoft.astrosage.misc;

import static com.ojassoft.astrosage.ui.act.BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.ui.act.ActUpdatePlanAfterTenCharts;
import com.ojassoft.astrosage.ui.act.ActUpgradeAfterTenChartDialog;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas-02 on 9/3/18.
 */

public class ShareBirthChart {
    String chartId;
    Activity context;
    int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Typeface typeface;
    private CustomProgressDialog pd = null;
    private RequestQueue queue;
    BeanHoroPersonalInfo _birthDetail;
    String userName, pwd;
    boolean isShare = false;

    public ShareBirthChart(OutputMasterActivity context, String chartId, int LANGUAGE_CODE) {
        this.chartId = chartId;
        this.context = context;
        this.LANGUAGE_CODE = LANGUAGE_CODE;
        typeface = CUtils.getRobotoFont(
                context, LANGUAGE_CODE, CGlobalVariables.regular);
        queue = VolleySingleton.getInstance(context).getRequestQueue();

        _birthDetail = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();

        userName = CUtils.getUserName(context);
        pwd = CUtils.getUserPassword(context);


        checkForShareUrlOnServer(chartId, isShare, userName, pwd);
    }


    private void checkForShareUrlOnServer(String chartId, boolean isShare, String userName, String pwd) {

        if (chartId != null && !chartId.isEmpty() && !chartId.equals("-1")) {
            isShare = true;
            getShareUrlFromServer(chartId, isShare, userName, pwd);
        } else {
            isShare = false;
            getShareUrlFromServer(chartId, isShare, userName, pwd);
        }

    }

    // this method is used to get share url from server
    private void getShareUrlFromServer(final String chartId, final boolean isShare, final String userName, final String pwd) {
        final String urlCheck = CGlobalVariables.saveAndShareChart;

        if (pd == null) {
            Typeface typeface = CUtils.getRobotoFont(
                    context, LANGUAGE_CODE, CGlobalVariables.regular);
            pd = new CustomProgressDialog(context, typeface);
        }
        pd.show();
        pd.setCancelable(false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //        //Log.e("Simple+" + response.toString());
                        if (response != null && !response.isEmpty()) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                            //Log.e("Element" + element.toString());
                            parseGsonData(response);
                           /* if (isFirstTime) {
                                isFirstTime = false;
                                CUtils.saveBooleanData(ActYearlyVrat.this, keyArray[visibleFragmentPosition] + LANGUAGE_CODE, isFirstTime);
                            }*/
                        }
                        dismissProgress();
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(context, context.getLayoutInflater(), context, typeface);
                mct.show(error.getMessage());

                //   mTextView.setText("That didn't work!");

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                dismissProgress();
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(context));
                // headers.put("language", String.valueOf(LANGUAGE_CODE));

                //headers.put("pwd", pwd);
                if (isShare) {
                    headers.put("chartid", chartId);
                    headers.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userName));
                    headers.put("saveandshare", "1");

                } else {
                    headers.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userName));
                    headers.put("saveandshare", "2");
                    headers = makeParameterForSave(headers);
                }

                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }
// end method getShareUrlFromServer


    private HashMap<String, String> makeParameterForSave(HashMap<String, String> headers) {
        if (_birthDetail != null) {
            String _name = _birthDetail.getName().trim();
            String _city = _birthDetail.getPlace().getCityName().trim();
            headers.put("name", _name);

            headers.put("sex", _birthDetail.getGender().trim());

            headers.put("day", String.valueOf(
                    _birthDetail.getDateTime().getDay()).trim());
            headers.put("month", String.valueOf(
                    _birthDetail.getDateTime().getMonth() + 1).trim());
            headers.put("year", String.valueOf(
                    _birthDetail.getDateTime().getYear()).trim());
            headers.put("hrs", String.valueOf(
                    _birthDetail.getDateTime().getHour()).trim());
            headers.put("min", String.valueOf(
                    _birthDetail.getDateTime().getMin()).trim());
            headers.put("sec", String.valueOf(
                    _birthDetail.getDateTime().getSecond()).trim());
            headers.put("place", _city);
            headers.put("longdeg", _birthDetail.getPlace().getLongDeg().trim());
            headers.put("longmin", _birthDetail.getPlace().getLongMin().trim());
            headers.put("longeW", _birthDetail.getPlace().getLongDir().trim());
            headers.put("latdeg", _birthDetail.getPlace().getLatDeg().trim());
            headers.put("latmin", _birthDetail.getPlace().getLatMin().trim());
            headers.put("latns", _birthDetail.getPlace().getLatDir().trim());

            headers.put("ayanamsa", String.valueOf(
                    _birthDetail.getAyanIndex()).trim());
            headers.put("timezone", String.valueOf(_birthDetail.getPlace().getTimeZoneValue()).trim());
            headers.put("dst", String.valueOf(
                    _birthDetail.getDST()).trim());
            if (_birthDetail.getOnlineChartId().trim().equals("-1")) {
                headers.put("chartid", "");
            } else {
                headers.put("chartid", _birthDetail.getOnlineChartId().trim());
            }
            headers.put("kphn", String.valueOf(_birthDetail.getHoraryNumber()));
        } else {
            Toast.makeText(context, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
        }

        return headers;
    }

    private void parseGsonData(String response) {
        int resultId;
        try {
            JSONObject jsonObject = new JSONObject(response);
            String result = jsonObject.getString("Result");
            if (result.equals("1")) {
                String chartIdFromServer = "";
                if (chartId != null && !chartId.isEmpty() && !chartId.equals("-1")) {
                    chartIdFromServer = "";

                } else {
                    chartIdFromServer = jsonObject.getString("ChartId");
                }

                if (chartIdFromServer != null && !chartIdFromServer.isEmpty() && !chartIdFromServer.equals("-1")) {
                    long localChartId = CGlobal.getCGlobalObject()
                            .getHoroPersonalInfoObject().getLocalChartId();
                    ArrayList<BeanHoroPersonalInfo> recentSearchKundli = (ArrayList) CUtils.getRecentSearchKundli(context);
                    for (int i = 0; i < recentSearchKundli.size(); i++) {
                        if (recentSearchKundli.get(i).getLocalChartId() == localChartId) {
                            recentSearchKundli.get(i).setOnlineChartId(chartIdFromServer);
                            break;
                        }

                    }
                    CUtils.saveRecentSearchKundli(context, recentSearchKundli);

                    try {
                        CGlobal
                                .getCGlobalObject()
                                .getHoroPersonalInfoObject().setOnlineChartId(chartIdFromServer);
                        new ControllerManager()
                                .addEditHoroPersonalInfoOperation(context, CGlobal
                                        .getCGlobalObject()
                                        .getHoroPersonalInfoObject());
                    } catch (UIDataOperationException e) {
                        e.printStackTrace();
                    }


                }
                shareOnSocialMedia(jsonObject.getString("url"));
            } else if (result.equals("-2")) {
                resultId = -132;
                openTheDialog(resultId);
            } else if (result.equals("-1")) {
                resultId = -133;
                openTheDialog(resultId);
            } else if (result.equals("4")) {

                Toast.makeText(context, "Chart id and user id not matched", Toast.LENGTH_SHORT).show();

            } else if (result.equals("5")) {
                Toast.makeText(context, "Chart name not updated", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        // shareOnSocialMedia(response);

    }

    private void openTheDialog(int resultId) {
        try {
            context.startActivityForResult(new Intent(context,
                    ActUpgradeAfterTenChartDialog.class).putExtra("language_code",
                    LANGUAGE_CODE).putExtra("resultId",
                    resultId).putExtra(SOURCE_OF_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_SHARE_BIRTH_CHART), SUB_ACTIVITY_UPGRADE_PLAN_DIALOG);
        } catch (Exception e) {
            //ignore
        }
    }

    private void shareOnSocialMedia(String urlToShare) {

        String name = "",sharedName="";

        BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo)CUtils.getCustomObject(context);
        if(beanHoroPersonalInfo != null){
            if(beanHoroPersonalInfo.getName()!=null && !beanHoroPersonalInfo.getName().equals("")){
                name = beanHoroPersonalInfo.getName();
            }else{
                name = CUtils.getUserName(context);
            }
        }else{
            name = CUtils.getUserName(context);
        }

        if(_birthDetail!=null && _birthDetail.getName()!=null){
            sharedName = _birthDetail.getName();
        }

        String data = context.getString(R.string.share_chart_data);

        data = data.replace("$",name);
        data = data.replace("#",sharedName);

        //data = data;
        try {
            if (urlToShare != null && !urlToShare.isEmpty()) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, data+" - "+urlToShare);
                intent.putExtra(Intent.EXTRA_SUBJECT, data);
                context.startActivity(Intent.createChooser(intent, "Share"));
            } else {
                Toast.makeText(context, "Bad url", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }

    private void dismissProgress() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
}
