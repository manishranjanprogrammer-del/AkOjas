package com.ojassoft.astrosage.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;

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
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.CScreenHistoryItemCollectionStack;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.NoInternetException;
import com.ojassoft.astrosage.customexceptions.UICOnlineChartOperationException;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.FirstTimeProfileDetailsActivity;
import com.ojassoft.astrosage.varta.ui.activity.ProfileForChat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amit RAutela on 29/2/16.
 * this Class is used to calculate the kundli
 */
public class CalculateKundli {

    public static final int SUB_ACTIVITY_UPGRADE_PLAN_DIALOG = 1005;
    public static boolean isPersonalKundli = false;
    private final boolean IS_APP_ONLINE = true;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    public int SELECTED_MODULE;
    BeanHoroPersonalInfo beanHoroPersonalInfo;
    boolean isSaveDetail;
    Activity activity;
    int ScreenId = CGlobalVariables.HOME_INPUT_SCREEN;
    boolean is_bookMarkSelectFromAppModule;
    int group_id;
    int child_id;
    int SELECTED_SUB_SCREEN;
    CustomProgressDialog pd;

    //by abhishek
    long kundliId = 0;
    boolean isKundliSaved = true;
    long[] onlineServerArray = new long[2];
    String msg = "";
    boolean isUserLoggedIn = false;
    private String updatedOnlineChartId = null;

    public CalculateKundli(BeanHoroPersonalInfo beanHoroPersonalInfo, boolean isSaveDetail, Activity activity, Typeface typeface, int SELECTED_MODULE, int ScreenId, boolean is_bookMarkSelectFromAppModule, int group_id, int child_id, int SELECTED_SUB_SCREEN) {
        this.beanHoroPersonalInfo = beanHoroPersonalInfo;
        this.isSaveDetail = isSaveDetail;
        this.activity = activity;
        this.typeface = typeface;
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();
        this.SELECTED_MODULE = SELECTED_MODULE;
        this.ScreenId = ScreenId;
        this.is_bookMarkSelectFromAppModule = is_bookMarkSelectFromAppModule;
        this.group_id = group_id;
        this.child_id = child_id;
        // this.isDisplayKundli = isDisplayKundli;
        this.SELECTED_SUB_SCREEN = SELECTED_SUB_SCREEN;
        try {
            CScreenHistoryItemCollectionStack.getScreenHistoryItemCollection(
                    activity).clearHistoryCollectionStack();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * This method check user loggedin or not. If user is loggedin then save kundali on server
     * otherwise save kundali into local db.
     */
    public void calculate() {

        CGlobal.getCGlobalObject().setHoroPersonalInfoObject(
                beanHoroPersonalInfo);
        BeanHoroPersonalInfo defaultKundliInfo = (BeanHoroPersonalInfo) CUtils
                .getCustomObject(activity.getApplication());
        if(defaultKundliInfo==null){
            CUtils.setKundliAsDefault(activity, beanHoroPersonalInfo);
        }

        CUtils.googleAnalyticSendWitPlayServie(activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHOW_KUNDLI, null);
        //CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHOW_KUNDLI, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,""); // now trigger from addFacebookAndFirebaseEvent method
        com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHOW_KUNDLI,"CalculateKundli");
        if (isSaveDetail) {
            try {

                //by abhishek raj -- change asynctask to volley
                isUserLoggedIn = CUtils.isUserLogedIn(activity);
                if (!isUserLoggedIn) {
                    try {
                        // save kundali into local db
                        kundliId = new ControllerManager()
                                .addEditHoroPersonalInfoOperation(activity, CGlobal
                                        .getCGlobalObject()
                                        .getHoroPersonalInfoObject());

                    } catch (Exception e) {
                        isKundliSaved = false;
                        msg = e.getMessage();
                    } finally {

                    }
                    handleSaveKundaliResponce();

                } else {
                    //save kundali on server
                    saveKundliPersonalDetails(CGlobal.getCGlobalObject()
                            .getHoroPersonalInfoObject(), CUtils
                            .getUserName(activity), CUtils
                            .getUserPassword(activity));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try{
                kundliId = new ControllerManager()
                        .addEditHoroPersonalInfoOperation(activity, CGlobal
                                .getCGlobalObject()
                                .getHoroPersonalInfoObject());
            }catch (Exception ignore){}
            calculateOnlineData();
        }

    }

    private void calculateOnlineData() {
        CUtils.saveKundliInPreference(activity, beanHoroPersonalInfo);
        ControllerManager _controllerManager = new ControllerManager(CalculateKundli.this, activity);
        try {
            showProgressbar();

            _controllerManager.calculateKundliData(beanHoroPersonalInfo,
                    IS_APP_ONLINE, CUtils.isConnectedWithInternet(activity));
        } catch (UICOnlineChartOperationException e) {
               /* e.printStackTrace();
                isSuccessCalculation = false;
                exceptionMessage = e.getMessage();*/

        } catch (NoInternetException nIntrntExc) {
            //isSuccessCalculation = false;
            nIntrntExc.printStackTrace();
        }
    }

    public void displayKundli() {
        //Log.e("KundliOutput","local_chat_id = " + this.beanHoroPersonalInfo.getLocalChartId());
        if(this.beanHoroPersonalInfo.getLocalChartId() == -1){
            this.beanHoroPersonalInfo.setLocalChartId(kundliId);
        }
        CUtils.saveKundliInPreference(activity, beanHoroPersonalInfo);
        if (CalculateKundli.isPersonalKundli) {
            CUtils.saveCustomObject(activity, this.beanHoroPersonalInfo);
            CalculateKundli.isPersonalKundli = false;
        }
        if (activity instanceof FirstTimeProfileDetailsActivity || activity instanceof ProfileForChat) {
            //do nothing
        } else if ((activity instanceof HomeInputScreen) && (((HomeInputScreen) activity).ASK_QUESTION_QUERY_DATA)) {
            ((HomeInputScreen) activity).sendDataToActAstroPaymentOptionsForAskAQuestion(this.beanHoroPersonalInfo);
        } else if ((activity instanceof HomeInputScreen) && (((HomeInputScreen) activity).ASTROSAGE_CHAT_QUERY_DATA)) {
            ((HomeInputScreen) activity).sendDataToActAstroPaymentOptionsForAstroChat(this.beanHoroPersonalInfo);
        }else if ((activity instanceof HomeInputScreen) && (((HomeInputScreen) activity).NUMROLOGY_QUERY_DATA)) {
            ((HomeInputScreen) activity).sendDataForProfileUse(this.beanHoroPersonalInfo);
        }else if ((activity instanceof HomeInputScreen) && (((HomeInputScreen) activity).VARTA_PROFILE_QUERY_DATA)) {
            ((HomeInputScreen) activity).sendDataForProfileUse(this.beanHoroPersonalInfo);
        }else if ((activity instanceof HomeInputScreen) && (((HomeInputScreen) activity).BHRIGOO_QUERY_DATA)) {
            ((HomeInputScreen) activity).sendDataForBhrigoo(this.beanHoroPersonalInfo);
        }

        //mahtab
        else if ((activity instanceof HomeInputScreen) && (((HomeInputScreen) activity).ASTRO_SERVICE_QUERY_DATA)) {
            ((HomeInputScreen) activity).sendDataToActAstroService(this.beanHoroPersonalInfo);
        } else if ((activity instanceof HomeInputScreen) && (((HomeInputScreen) activity).ASTRO_PRODUCT_DATA)) {
            ((HomeInputScreen) activity).sendDataToActAstroProduct(this.beanHoroPersonalInfo);
        }

        //end
        else {
            if (SELECTED_MODULE == CGlobalVariables.MODULE_VARSHAPHAL && !CUtils.isConnectedWithInternet(activity)) {
                MyCustomToast mct2 = new MyCustomToast(
                        activity,
                        activity.getLayoutInflater(),
                        activity, typeface);
                mct2.show(activity.getResources().getString(R.string.no_internet));
            } else {
                Intent intent = new Intent(activity, OutputMasterActivity.class);
                intent.putExtra(CGlobalVariables.LANGUAGE_CODE, LANGUAGE_CODE);
                intent.putExtra("ScreenId", ScreenId);
                intent.putExtra("is_bookMarkSelectFromAppModule", is_bookMarkSelectFromAppModule);
                intent.putExtra("group_id", group_id);
                intent.putExtra("child_id", child_id);
                SharedPreferences sharedPreferences = activity.getSharedPreferences(
                        CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
                intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
                intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, SELECTED_SUB_SCREEN);
                intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN, sharedPreferences
                        .getInt(CGlobalVariables.APP_PREFS_ChartStyle,
                                CGlobalVariables.CHART_NORTH_STYLE));
                intent.putExtra("calculateKundli", true);
                activity.startActivity(intent);
            }

        }
    }

    public void dismissProgressbar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {

        }
    }

    public void showProgressbar() {
        try {
            pd = new CustomProgressDialog(activity, typeface);
            pd.setCancelable(false);
            pd.show();
        } catch (Exception ex) {

        }
    }

    public void saveKundliPersonalDetails(final BeanHoroPersonalInfo _birthDetail, final String userId, final String pwd) {
        showProgressbar();

        String orderUrl = CGlobalVariables.ONLINE_CHART_SAVE_URL;
        //AstrosageKundliApplication.cloudChartLogs = "\n"+AstrosageKundliApplication.cloudChartLogs+"\nUrl="+orderUrl;
        System.out.println("URLLLLLLLLL "+ orderUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, orderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("URLLLLLLLLL "+ response);
                            //AstrosageKundliApplication.cloudChartLogs = "\n"+AstrosageKundliApplication.cloudChartLogs+"\nResponse="+response;
                            onlineServerArray = new ControllerManager().getSaveOnlineChartId(response, updatedOnlineChartId);
                        } catch (Exception e) {
                            e.printStackTrace();
                            isKundliSaved = false;
                            msg = e.getMessage();
                        }
                        handleSaveKundaliResponce();
                        dismissProgressbar();
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isKundliSaved = false;
                msg = activity.getString(R.string.chart_not_saved_on_server);
                handleSaveKundaliResponce();
                dismissProgressbar();
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("VolleyError: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
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
            }
        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> nameValuePairs = new HashMap();

                String _name = _birthDetail.getName().trim();
                String _city = _birthDetail.getPlace().getCityName().trim();
                String _state = _birthDetail.getPlace().getState().trim();
                String _country = _birthDetail.getPlace().getCountryName().trim();
                if (_state.length()>0){
                    _state = ", "+_state;
                }
                if (_country.length()>0){
                    _country = ", "+_country;
                }
                String _place = _city+_state+_country;
                //_city=_city.replaceAll("_","");

                nameValuePairs.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userId));
                nameValuePairs.put(CGlobalVariables.KEY_PASSWORD, pwd);


                nameValuePairs.put("Name", _name);

                nameValuePairs.put("Sex", _birthDetail.getGender().trim());

                nameValuePairs.put("Day", String.valueOf(
                        _birthDetail.getDateTime().getDay()).trim());
                nameValuePairs.put("Month", String.valueOf(
                        _birthDetail.getDateTime().getMonth() + 1).trim());
                nameValuePairs.put("Year", String.valueOf(
                        _birthDetail.getDateTime().getYear()).trim());
                nameValuePairs.put("Hrs", String.valueOf(
                        _birthDetail.getDateTime().getHour()).trim());
                nameValuePairs.put("Min", String.valueOf(
                        _birthDetail.getDateTime().getMin()).trim());
                nameValuePairs.put("Sec", String.valueOf(
                        _birthDetail.getDateTime().getSecond()).trim());
                nameValuePairs.put("Place", _place);
                nameValuePairs.put("LongDeg", _birthDetail.getPlace().getLongDeg().trim());
                nameValuePairs.put("LongMin", _birthDetail.getPlace().getLongMin().trim());
                nameValuePairs.put("LongEW", _birthDetail.getPlace().getLongDir().trim());
                nameValuePairs.put("LatDeg", _birthDetail.getPlace().getLatDeg().trim());
                nameValuePairs.put("LatMin", _birthDetail.getPlace().getLatMin().trim());
                nameValuePairs.put("LatNS", _birthDetail.getPlace().getLatDir().trim());

                nameValuePairs.put("Ayanamsa", String.valueOf(
                        _birthDetail.getAyanIndex()).trim());
                nameValuePairs.put("timezone", String.valueOf(_birthDetail.getPlace().getTimeZoneValue()).trim());
                nameValuePairs.put("DST", String.valueOf(
                        _birthDetail.getDST()).trim());
                nameValuePairs.put("Isapi", "1");
                if (_birthDetail.getOnlineChartId().trim().equals("-1")) {
                    nameValuePairs.put("ChartId", "");
                } else {
                    nameValuePairs.put("ChartId", _birthDetail.getOnlineChartId().trim());
                }
                nameValuePairs.put("kphn", String.valueOf(_birthDetail.getHoraryNumber()));
                if ((_birthDetail.getOnlineChartId() != null) && (_birthDetail.getOnlineChartId().trim().length() > 0)) {
                    updatedOnlineChartId = _birthDetail.getOnlineChartId().trim();
                }
                //AstrosageKundliApplication.cloudChartLogs = "\n"+AstrosageKundliApplication.cloudChartLogs+"\nParams="+nameValuePairs;
                System.out.println("URLLLLLLLLL "+ nameValuePairs.toString());
                return nameValuePairs;
            }

        };
        RequestQueue queue = VolleySingleton.getInstance(activity).getRequestQueue();
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        if (queue != null) {
            queue.add(stringRequest);
        }
    }

    /**
     * This method calculate online chart either user loggedin or not
     */
    private void handleSaveKundaliResponce() {

        if (!isUserLoggedIn)
            CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .setLocalChartId(kundliId);
        if (isUserLoggedIn) {
            if (isKundliSaved)
                CGlobal.getCGlobalObject()
                        .getHoroPersonalInfoObject()
                        .setOnlineChartId(
                                String.valueOf(onlineServerArray[0]));
            try {
                kundliId = new ControllerManager()
                        .addEditHoroPersonalInfoOperation(activity, CGlobal
                                .getCGlobalObject()
                                .getHoroPersonalInfoObject());
            } catch (Exception e) {
                isKundliSaved = false;
                msg = e.getMessage();
            } finally {
            }

            CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .setLocalChartId(kundliId);
            System.out.println("onlineServerArray" + onlineServerArray[1]);
            if ((onlineServerArray[1] == 2 || onlineServerArray[1] == 6)) {
                CUtils.saveKundliInPreference(activity, beanHoroPersonalInfo);
                if (!(activity instanceof OutputMasterActivity)) {
                    CUtils.gotoshowCompletedFreeChartPlanScreen(
                            activity, LANGUAGE_CODE,
                            SUB_ACTIVITY_UPGRADE_PLAN_DIALOG, (int) onlineServerArray[1]);
                } else {
                    calculateOnlineData();
                }
            } else if (onlineServerArray[1] == 4) {
                MyCustomToast mct = new MyCustomToast(activity,
                        activity.getLayoutInflater(),
                        activity, Typeface.DEFAULT);
                mct.show("Chart not updated on server");
                calculateOnlineData();
            } else {
                calculateOnlineData();
            }
        } else {
            calculateOnlineData();
        }
        if (!isKundliSaved) {
            MyCustomToast mct = new MyCustomToast(activity,
                    activity.getLayoutInflater(),
                    activity, Typeface.DEFAULT);
            mct.show(msg);
        }

    }

    public class CCalculateOnlineDataSync extends
            AsyncTask<String, Long, Void> {
        CustomProgressDialog pd = null;
        BeanHoroPersonalInfo beanHoroPersonalInfo;
        boolean internetStatus, isSuccessCalculation = true;
        String exceptionMessage = "";

        public CCalculateOnlineDataSync(
                BeanHoroPersonalInfo beanHoroPersonalInfo,
                boolean internetStatus) {
            this.beanHoroPersonalInfo = beanHoroPersonalInfo;
            this.internetStatus = internetStatus;
        }

        @Override
        protected Void doInBackground(String... arg0) {
            // calculate chart code
            ControllerManager _controllerManager = new ControllerManager(CalculateKundli.this, activity);
            try {
                _controllerManager.calculateKundliData(beanHoroPersonalInfo,
                        IS_APP_ONLINE, internetStatus);
            } catch (UICOnlineChartOperationException e) {
                e.printStackTrace();
                isSuccessCalculation = false;
                exceptionMessage = e.getMessage();

            } catch (NoInternetException nIntrntExc) {
                isSuccessCalculation = false;
                nIntrntExc.printStackTrace();
            }
            return null;
        }

        /*
         * (non-Javadoc
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {

            }
            if (isSuccessCalculation) {

                if (CalculateKundli.isPersonalKundli) {
                    CUtils.saveCustomObject(activity, this.beanHoroPersonalInfo);
                    CalculateKundli.isPersonalKundli = false;
                }
                // displayKundli();
            } else {
                if ((exceptionMessage != null)
                        && (exceptionMessage.length() > 0)) {
                    MyCustomToast mct = new MyCustomToast(activity,
                            activity.getLayoutInflater(),
                            activity, Typeface.DEFAULT);
                    mct.show(exceptionMessage);
                } else if ((exceptionMessage == null)) {
                    MyCustomToast mct1 = new MyCustomToast(
                            activity,
                            activity.getLayoutInflater(),
                            activity, Typeface.DEFAULT);
                    mct1.show("Error on server");
                } else {
                    MyCustomToast mct2 = new MyCustomToast(
                            activity,
                            activity.getLayoutInflater(),
                            activity, typeface);
                    mct2.show(activity.getResources().getString(R.string.no_internet));
                }
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exceptionMessage = "";
            pd = new CustomProgressDialog(activity, typeface);
            pd.show();

        }
    }


}