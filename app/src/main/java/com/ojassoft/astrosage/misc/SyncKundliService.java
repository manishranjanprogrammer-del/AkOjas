package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.SyncDataModel;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.model.CDatabaseHelperOperations;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This service is used to sync offline kundli data to server in two cases
 * 1. When user login then kundli data will sync automatically
 * 2. When user click on upload chart button then kundli data will be sync
 */

/**
 * Created by ojas-08 on 4/11/16.
 */
public class SyncKundliService extends IntentService implements VolleyResponse {
    ArrayList<BeanHoroPersonalInfo> localChartArray;
    public static final String ACTION_SYNC_SERVICE = "com.ojassoft.syncchart";
    boolean isHitagain;
    ControllerManager controllerManager;
    String userName = "";
    String password = "";
    String jsonString1;
    String result;
    Gson gson;
    JsonArray myCustomArray;
    ArrayList<SyncDataModel> localChartList;
    boolean isSyncStarted = false;
    CDatabaseHelperOperations cDatabaseHelperOperations;
    boolean isAutoSyncd;

    public SyncKundliService() {
        super("GetDefaultKundliDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Log.e("TestSyncChart", "onHandleIntent:  started");
        controllerManager = new ControllerManager();
        cDatabaseHelperOperations = new CDatabaseHelperOperations(this);
        gson = new GsonBuilder().create();
        userName = CUtils.getUserName(this);
        password = CUtils.getUserPassword(this);
        isHitagain = true;
        if (intent != null && intent.hasExtra("isAutoSync")) {
            isAutoSyncd = intent.getBooleanExtra("isAutoSync", false);
        }

        if (!cDatabaseHelperOperations.getOfflineKundliForSync().isEmpty()) {
            //Log.e("TestSyncChart", "onHandleIntent:  if there is offline kundli to sync = true");
            sendBroadCast(0, getResources().getString(R.string.chart_upoloading));
            syncDatabase();
        } else {
            if(!isAutoSyncd) {// in case of auto sync do not send message if there is no kundli to sync
                sendBroadCast(0, getResources().getString(R.string.chart_upoload_error));
            } else { // in case of auto sync mark kundli synced to avoid repeated attempts
                CUtils.saveKundliSyncedStatus(this, CUtils.getUserName(this), true);
            }
            //Log.e("TestSyncChart", "onHandleIntent:  if there is offline kundli to sync = false");
        }

    }

    private void syncDatabase() {


        localChartArray = cDatabaseHelperOperations.getOfflineKundliForSync();
        localChartList = new ArrayList<SyncDataModel>();
        for (int i = 0; i < localChartArray.size(); i++) {
            localChartList.add(createSyncDataModelObject(localChartArray.get(i)));
        }
        if (localChartList != null && localChartList.size() > 0 && isHitagain) {
            //jsonString = gson.toJson(localChartArray);

            Gson gson = new GsonBuilder().create();
            myCustomArray = gson.toJsonTree(localChartList).getAsJsonArray();
            String url = CGlobalVariables.SYNCCHARTURL;
            CUtils.vollyPostRequest(SyncKundliService.this, url, getParams(SyncKundliService.this,
                    userName, password, myCustomArray.toString()), 0);
            /*result = new ControllerManager().syncChartOnServer(SyncKundliService.this, userName, password, myCustomArray.toString());
            parseResult(result);
            syncDatabase();*/
        } else if (localChartList != null && localChartList.size() == 0 && isSyncStarted) {

            sendBroadCast(0, getResources().getString(R.string.chart_upoloaded));
            isSyncStarted = false;
        }

    }

    private Map<String, String> getParams(Context context, String userId, String pwd, String chartDetail) {
        HashMap<String, String> params = new HashMap<String, String>();
        String key = CUtils.getApplicationSignatureHashCode(context);
        params.put("key", key);
        params.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(userId));
        params.put(CGlobalVariables.KEY_PASSWORD, pwd);
        params.put("isAutoSynch", isAutoSyncd ? "1" : "0");
        params.put("jsonInput", chartDetail);
        //Log.e("TestSyncChart", "params=>" + params);
        return params;
    }

    private void parseResult(String resultStr) {
        try {
            ArrayList<BeanHoroPersonalInfo> recentViewed = (ArrayList<BeanHoroPersonalInfo>) CUtils.getRecentSearchKundli(SyncKundliService.this);
            if (!resultStr.equals("")) {
                JSONArray jsonArray = new JSONArray(resultStr);
                JSONObject jsonObject;
                BeanHoroPersonalInfo beanHoroPersonalInfo;

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    String result = jsonObject.getString("Result");
                    Log.i("result1", result);
                    if (result.equals("1")) {
                        isSyncStarted = true;
                        long localChartId = Long.parseLong(jsonObject.getString("LocalChartId"));
                        Log.i("result2", localChartId + "");
                        String onlineChartid = jsonObject.getString("OnlineChartId");
                        Log.i("result3", onlineChartid);
                        for (int j = 0; j < localChartArray.size(); j++) {
                            beanHoroPersonalInfo = localChartArray.get(j);
                            if (beanHoroPersonalInfo.getLocalChartId() == localChartId) {
                                beanHoroPersonalInfo.setOnlineChartId(onlineChartid);
                                controllerManager.addEditHoroPersonalInfoOperation(SyncKundliService.this, beanHoroPersonalInfo);
                                break;
                            }
                        }
                        for (int j = 0; j < recentViewed.size(); j++) {
                            if (recentViewed.get(j).getLocalChartId() == localChartId) {
                                recentViewed.get(j).setOnlineChartId(onlineChartid);
                                break;
                            }
                        }
                        CUtils.saveRecentSearchKundli(SyncKundliService.this, recentViewed);
                        if(isAutoSyncd){// in case of auto sync mark kundli synced to avoid repeated attempts
                            CUtils.saveKundliSyncedStatus(this, CUtils.getUserName(this), true);
                        }
                        Log.i("Kundli ", "saved");
                    } else if (result.equals("-1")) {//you can not save more than 5000 charts

                        if(isAutoSyncd){// in case of limit reached during auto sync mark kundli synced to avoid repeated attempts
                            CUtils.saveKundliSyncedStatus(this, CUtils.getUserName(this), true);
                        }

                        isHitagain = false;
                        isSyncStarted = false;
                        break;
                    } else if (result.equals("-2")) {// you can not save more than 10 Charts

                        sendBroadCast(1, "");
                        //startActivity(new Intent(SyncKundliService.this, PlanAlertActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        isHitagain = false;
                        isSyncStarted = false;
                        break;
                    } else {
                        sendBroadCast(0, getResources().getString(R.string.server_error));
                        Log.i("result>>", result);
                        isHitagain = false;
                        isSyncStarted = false;
                        break;
                    }
                }
            } else {

                sendBroadCast(0, getResources().getString(R.string.server_error));

                isHitagain = false;
                isSyncStarted = false;
            }

        } catch (Exception e) {
            sendBroadCast(0, e.getMessage());

            isHitagain = false;
            isSyncStarted = false;
            Log.d("mResponse", e.toString());
        }

    }

    private SyncDataModel createSyncDataModelObject(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        SyncDataModel syncDataModel = new SyncDataModel();
        BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
        BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();
        syncDataModel.setmDay(beanDateTime.getDay());
        syncDataModel.setmHour(beanDateTime.getHour());
        syncDataModel.setmMin(beanDateTime.getMin());
        syncDataModel.setmSecond(beanDateTime.getSecond());
        syncDataModel.setmYear(beanDateTime.getYear());
        syncDataModel.setmMonth(beanDateTime.getMonth() + 1);
        syncDataModel.setfTimeZoneValue(beanPlace.getTimeZoneValue());
        syncDataModel.setHoraryNumber(beanHoroPersonalInfo.getHoraryNumber());
        syncDataModel.setiCityId(beanHoroPersonalInfo.getCityID());
        syncDataModel.setsDst(beanHoroPersonalInfo.getDST());
        syncDataModel.setiAyanIndex(beanHoroPersonalInfo.getAyanIndex());
        syncDataModel.setRecentId(beanHoroPersonalInfo.getRecentId());
        syncDataModel.setiCountryId(beanPlace.getCountryId());
        syncDataModel.setiTimeZoneId(beanPlace.getTimeZoneId());
        syncDataModel.setName(beanHoroPersonalInfo.getName());
        syncDataModel.setsGender(beanHoroPersonalInfo.getGender());
        syncDataModel.setsAyan(beanHoroPersonalInfo.getAyan());
        syncDataModel.setOnlineChartId(beanHoroPersonalInfo.getOnlineChartId());
        syncDataModel.setsCountryName(beanPlace.getCountryName());
        syncDataModel.setsStateName(beanPlace.getState());
        syncDataModel.setsCityName(beanPlace.getCityName());
        syncDataModel.setsLatDeg(beanPlace.getLatDeg());
        syncDataModel.setsLatMin(beanPlace.getLatMin());
        syncDataModel.setsLongDeg(beanPlace.getLongDeg());
        syncDataModel.setsLongMin(beanPlace.getLongMin());
        syncDataModel.setsLatDir(beanPlace.getLatDir());
        syncDataModel.setsLongDir(beanPlace.getLongDir());
        syncDataModel.setsTimeZoneName(beanPlace.getTimeZoneName());
        syncDataModel.setLatSec(beanPlace.getLatSec());
        syncDataModel.setLongSec(beanPlace.getLongSec());
        syncDataModel.setLocalChartId(beanHoroPersonalInfo.getLocalChartId());
        syncDataModel.setSaveChart(beanHoroPersonalInfo.isSaveChart());
        return syncDataModel;
    }

    private void sendBroadCast(int resultCode, String msg) {
        if(isAutoSyncd) {// in case of auto sync do not send message
            return;
        }
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(ACTION_SYNC_SERVICE);
            Bundle bundle = new Bundle();
            bundle.putString("resultstr", msg);
            bundle.putInt("resultcode", resultCode);
            intent.putExtra("result", bundle);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            //Log.e("TestSyncChart", "response=>" + response);
            result = response;
            parseResult(response);
            syncDatabase();

        } catch (Exception e) {
            Log.i("", e.getMessage());
        }

    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("TestSyncChart", "onError=>" + error);
        if (error != null && error.getMessage() != null) {
            sendBroadCast(0, error.getMessage());
        }

        isHitagain = false;
        isSyncStarted = false;
    }
}
