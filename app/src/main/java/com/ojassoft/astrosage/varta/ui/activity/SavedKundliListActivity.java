package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_KUNDALI_DETAILS;
import static com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity.astrologerDetailBeanData;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REQUEST_CODE_SELECT_KUNDALI;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SELECTED_KUNDLI_PROFILE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.varta.adapters.ProfileListAdapter;
import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedKundliListActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponse {

    private RecyclerView rvKundliList;
    private Context context;
    private ArrayList<BeanHoroPersonalInfo> recentSearchKundli;
    private LinearLayout llShowAll, create_kundli, llKundliListSkip, llKundliListCancel;
    private int dataSize;
    private ImageView closeButton;
    private TextView tvKundliListCreateKundli,tvKundliListMyProfile,tvKundliListShowAll, tvKundliListSkip, tvKundliListCancel;
    private UserProfileData userProfileData = null;
    RequestQueue queue;
    CustomProgressDialog pd;
    ConstraintLayout rlSavedParentView;
    String phoneNo = "";
    String urlText = "", fromWhereData = "";
    ProfileListAdapter adapter;

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                profileDataSendToServer(null);
            } else {
                CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(SavedKundliListActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kundli_list);

        context = this;
        queue = VolleySingleton.getInstance(context).getRequestQueue();
        initViews();
        getIntentData();
    }

    private void initViews(){
        llShowAll = findViewById(R.id.llShowAll);
        create_kundli = findViewById(R.id.create_kundli);
        tvKundliListCreateKundli = findViewById(R.id.tvKundliListCreateKundli);
        tvKundliListMyProfile = findViewById(R.id.tvKundliListMyProfile);
        tvKundliListShowAll = findViewById(R.id.tvKundliListShowAll);
        rlSavedParentView = findViewById(R.id.rlSavedParentView);
        llKundliListSkip = findViewById(R.id.llKundliListSkip);
        tvKundliListSkip = findViewById(R.id.tvKundliListSkip);
        rvKundliList = findViewById(R.id.rvKundliList);
        llKundliListCancel = findViewById(R.id.llKundliListCancel);
        tvKundliListCancel = findViewById(R.id.tvKundliListCancel);
        closeButton = findViewById(R.id.btn_close);
        rvKundliList.setLayoutManager(new LinearLayoutManager(context));

        FontUtils.changeFont(context,tvKundliListMyProfile, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context,tvKundliListCreateKundli, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context,tvKundliListShowAll, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context,tvKundliListSkip, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context,tvKundliListCancel, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        llShowAll.setOnClickListener(this);
        create_kundli.setOnClickListener(this);
        llKundliListSkip.setOnClickListener(this);
        llKundliListCancel.setOnClickListener(this);
        closeButton.setOnClickListener(this);

        if (userProfileData == null)
            userProfileData = new UserProfileData();

        loadAllLocalKundli();

    }

    private void getIntentData(){
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("phoneNo")) {
                phoneNo = getIntent().getStringExtra("phoneNo");
            }
            if (getIntent().getExtras().containsKey("urlText")) {
                urlText = getIntent().getStringExtra("urlText");
            }
            if (getIntent().getExtras().containsKey("fromWhere")) {
                fromWhereData = getIntent().getStringExtra("fromWhere");
            }
            if (!TextUtils.isEmpty(fromWhereData) && fromWhereData.equals(CGlobalVariables.TYPE_AI_CHAT)) {
                llKundliListSkip.setVisibility(View.GONE);
            } else {
                llKundliListSkip.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            /*if (recentSearchKundli.size() > 5){
                llShowAll.setVisibility(View.VISIBLE);
            }*/
        }
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            // Set the height to 70% of the screen height
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            params.width = (int) (displayMetrics.widthPixels * 0.92);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void loadAllLocalKundli() {
        ArrayList<BeanHoroPersonalInfo> localSavedKundli = null;
        recentSearchKundli = new ArrayList<>();
        try {
            localSavedKundli = new ControllerManager().searchLocalKundliListOperation(
                    this, 0, 40);
        } catch (Exception e) {
            //
        }
        if (localSavedKundli == null) {
            localSavedKundli = new ArrayList<>();
        } else {
            dataSize = localSavedKundli.size();
        }

        /*String selectedKundli = CUtils.getStringData(context,SELECTED_KUNDLI_PROFILE,"");
        BeanHoroPersonalInfo beanHoroPersonalInfo = null;
         for (int i = 0; i<localSavedKundli.size(); i++){
             if (String.valueOf(localSavedKundli.get(i).getLocalChartId()).equals(selectedKundli)){
                 beanHoroPersonalInfo = localSavedKundli.get(i);
                 recentSearchKundli.add(beanHoroPersonalInfo);
                 break;
             }
         }
         localSavedKundli.remove(beanHoroPersonalInfo);*/
        recentSearchKundli.addAll(localSavedKundli);
        adapter = new ProfileListAdapter(this, context, recentSearchKundli);
        rvKundliList.setAdapter(adapter);
    }

    public void onKundliSelected(int position) {
        if (!CUtils.isConnectedWithInternet(SavedKundliListActivity.this)) {
            CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.no_internet), context);
            showToast(getResources().getString(R.string.no_internet));
        } else {
            //profileDataSendToServer(recentSearchKundli.get(position));
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_KUNDALI_DETAILS,recentSearchKundli.get(position));
            intent.putExtra("IS_PROCEED", false);
            intent.putExtra("openProfileForChat", false);
            intent.putExtra("phoneNo", phoneNo);
            intent.putExtra("urlText", urlText);
            intent.putExtra("fromWhere", fromWhereData);
            intent.putExtras(bundle);
            setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent);
            finish();
        }
    }

    private void profileDataSendToServer(BeanHoroPersonalInfo beanHoroPersonalInfo){
        try {
            com.ojassoft.astrosage.beans.BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            com.ojassoft.astrosage.beans.BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();

            userProfileData.setName(beanHoroPersonalInfo.getName());
            userProfileData.setUserPhoneNo(CUtils.getUserID(context));

            userProfileData.setGender(beanHoroPersonalInfo.getGender());

            userProfileData.setMaritalStatus(CGlobalVariables.NOT_SPECIFIED);

            userProfileData.setOccupation(CGlobalVariables.NOT_SPECIFIED);

            if (beanDateTime != null) {
                userProfileData.setDay("" + beanDateTime.getDay());
                userProfileData.setMonth("" + (beanDateTime.getMonth()+1));
                userProfileData.setYear("" + beanDateTime.getYear());
                userProfileData.setHour("" + beanDateTime.getHour());
                userProfileData.setMinute("" + beanDateTime.getMin());
                userProfileData.setSecond("" + beanDateTime.getSecond());
            }
            if (beanPlace != null) {
                String place = beanPlace.getCityName();
                if (beanPlace.getState() != null && !beanPlace.getState().trim().isEmpty()) {
                    if (!place.contains(",")) {
                        place = place + ", " + beanPlace.getState();
                    }
                }
                /*if (beanPlace.getState() != null && beanPlace.getState().trim().length() > 0) {
                    place = place + ", " + beanPlace.getState();
                }*/
                userProfileData.setPlace(place);
                userProfileData.setLatdeg(beanPlace.getLatDeg());
                userProfileData.setLongdeg(beanPlace.getLongDeg());
                userProfileData.setLongmin(beanPlace.getLongMin());
                userProfileData.setLatmin(beanPlace.getLatMin());
                userProfileData.setLongew(beanPlace.getLongDir());
                userProfileData.setLatns(beanPlace.getLatDir());
                userProfileData.setTimezone(beanPlace.getTimeZoneValue() + "");
            }

            userProfileData.setProfileSendToAstrologer(true);
            CUtils.saveUserSelectedProfileInPreference(context, userProfileData);
            CUtils.saveProfileForChatInPreference(context, userProfileData);
            sendToServer(userProfileData);
            proceedWithSelectedKundli(false);

        }catch (Exception e){
            Log.d("joinLiveAud",e.toString());
        }
    }

    private void proceedWithSelectedKundli(boolean isSkip){
        Intent intent1 = new Intent();
        intent1.putExtra("IS_PROCEED", true);
        intent1.putExtra("IS_SKIP", isSkip);
        if (userProfileData == null)
            userProfileData = new UserProfileData();
        intent1.putExtra("USER_DETAIL", userProfileData);
        if (phoneNo != null && phoneNo.length() > 0 && urlText != null && urlText.length() > 0) {
            intent1.putExtra("phoneNo", phoneNo);
            intent1.putExtra("urlText", urlText);
        }
        if (!TextUtils.isEmpty(urlText) && urlText.equals(CGlobalVariables.OPEN_DUMMY_CHAT_WINDOW)) {
            intent1.putExtra(CGlobalVariables.OPEN_DUMMY_CHAT_WINDOW,true);
        }
        intent1.putExtra("fromWhere", fromWhereData);

//        if(fromWhereData.equals("chat")){
//            setResult(2001, intent1);
//        }else if(fromWhereData.equals("call")){
            setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent1);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_KUNDALI){
            if (resultCode == RESULT_OK) {
                if (data == null) return;
                Bundle bundle = data.getExtras();
                if (bundle == null) return;
                Intent intent = new Intent();
                intent.putExtra("IS_PROCEED", false);
                intent.putExtra("openProfileForChat", false);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("urlText", urlText);
                intent.putExtra("fromWhere", fromWhereData);
                intent.putExtras(bundle);
                setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent);
                finish();
            }
        } else if (requestCode == DashBoardActivity.BACK_FROM_PROFILECHATDIALOG) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                boolean openKundli = false;
                if (data.getExtras().containsKey("openKundli")){
                    openKundli = data.getExtras().getBoolean("openKundli");
                }
                UserProfileData rUserProfileData = (UserProfileData) data.getExtras().get("USER_DETAIL");
                if (isProceed) {
                    Intent intent = new Intent();
                    intent.putExtra("IS_PROCEED", isProceed);
                    intent.putExtra("USER_DETAIL", rUserProfileData);
                    if (phoneNo != null && phoneNo.length() > 0 && urlText != null && urlText.length() > 0) {
                        intent.putExtra("phoneNo", phoneNo);
                        intent.putExtra("urlText", urlText);
                    }
                    intent.putExtra("fromWhere", fromWhereData);
                    setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent);
                    if (rUserProfileData.isProfileSendToAstrologer()) {
                        CUtils.saveUserSelectedProfileInPreference(context, rUserProfileData);
                        CUtils.saveProfileForChatInPreference(context, rUserProfileData);
                        sendToServer(rUserProfileData);
                    }
                    finish();
                } else {
                    if (openKundli){
                        openKundlis(0);
                    } else {
                        if (recentSearchKundli == null || recentSearchKundli.isEmpty()){
                            finish();
                        }
                    }
                }
            }
        }
    }

    private void sendToServer(UserProfileData userProfileData1) {
        try {
            CUtils.fcmAnalyticsEvents(userProfileData1.getMaritalStatus(), CGlobalVariables.EVENT_MARITAL_STATUS,"");
            CUtils.fcmAnalyticsEvents(userProfileData1.getOccupation(), CGlobalVariables.EVENT_OCCUPATION,"");

            int year = Integer.parseInt(userProfileData1.getYear());
            int month = Integer.parseInt(userProfileData1.getMonth());
            int day = Integer.parseInt(userProfileData1.getDay());
            CUtils.setFcmAnalyticsByAge(year, month, day);
        }catch (Exception e){
            //
        }
        if (!CUtils.isConnectedWithInternet(context)) {
            CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.no_internet), context);
            showToast(getResources().getString(R.string.no_internet));
        } else {
            if (pd == null) {
                pd = new CustomProgressDialog(context);
                pd.show();
                pd.setCancelable(false);
            }

//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.PROFILE_UPDATE_SUBMIT_URL,
//                    this, false, getParams(userProfileData1), 1).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.updateUserProfile(getParams(userProfileData1));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    //response = "{\"status\":\"2\", \"msg\":\"\"}";
                    // Log.e("LoadMore response ", response);
                    if (response.body() != null ) {
                            try {
                                String myResponse = response.body().string();
                                JSONObject jsonObject = new JSONObject(myResponse);
                                String status = jsonObject.getString("status");
                                if (status.equalsIgnoreCase("1")) {
                                    CUtils.saveUserSelectedProfileInPreference(context, userProfileData);
                                    CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.update_successfully), context);
                                    //showToast(getResources().getString(R.string.update_successfully));
                                } else if (status.equals("100")) {
                                    CUtils.fcmAnalyticsEvents("bg_login_from_profile_for_chat",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                                    LocalBroadcastManager.getInstance(context).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                                    startBackgroundLoginService();
                                } else {
                                    CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.update_not_successfully), context);
                                    showToast(getResources().getString(R.string.update_not_successfully));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                    } else {
                        CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.server_error), context);
                        showToast(getResources().getString(R.string.server_error));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }

    public Map<String, String> getParams(UserProfileData userProfileDataBean) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        headers.put(CGlobalVariables.USER_PHONE_NO, userProfileDataBean.getUserPhoneNo());
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put("name", userProfileDataBean.getName());
        headers.put("gender", userProfileDataBean.getGender());
        headers.put("place", userProfileDataBean.getPlace());
        headers.put("day", userProfileDataBean.getDay());
        headers.put("month", userProfileDataBean.getMonth());
        headers.put("year", userProfileDataBean.getYear());
        headers.put("hour", userProfileDataBean.getHour());
        headers.put("minute", userProfileDataBean.getMinute());
        headers.put("second", userProfileDataBean.getSecond());
        headers.put("longdeg", userProfileDataBean.getLongdeg());
        headers.put("longmin", userProfileDataBean.getLongmin());
        headers.put("longew", userProfileDataBean.getLongew());
        headers.put("latmin", userProfileDataBean.getLatmin());
        headers.put("latdeg", userProfileDataBean.getLatdeg());
        headers.put("latns", userProfileDataBean.getLatns());
        headers.put("timezone", userProfileDataBean.getTimezone());
        headers.put("maritalStatus", userProfileDataBean.getMaritalStatus());
        headers.put("occupation", userProfileDataBean.getOccupation());
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.d("TestLog", "headers="+headers);
        return CUtils.setRequiredParams(headers);
    }

    /*
     *pass 1 to create new kundli and 0 to select kundli from local database
     * */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llShowAll){
            CUtils.fcmAnalyticsEvents("kundli_list_dialog_show_all",AstrosageKundliApplication.currentEventType,"");
            openKundlis(0);
        } else if (v.getId() == R.id.create_kundli){
            createKundli();
        } else if (v.getId() == R.id.llKundliListSkip){
            if (!TextUtils.isEmpty(urlText) && urlText.equals(CGlobalVariables.TYPE_AI_CHAT)) {
                showToast(getString(R.string.please_enter_name_v));
                createKundli();
                return;
            }
            CUtils.fcmAnalyticsEvents("kundli_list_dialog_skip",AstrosageKundliApplication.currentEventType,"");
            proceedWithSelectedKundli(true);
        } else if (v.getId() == R.id.llKundliListCancel || v.getId() == R.id.btn_close){
            CUtils.fcmAnalyticsEvents("kundli_list_dialog_cancel",AstrosageKundliApplication.currentEventType,"");
            Intent intent = new Intent();
            intent.putExtra("IS_PROCEED", false);
            setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent);
            finish();
        }
    }

    private void createKundli(){
        Intent intent = new Intent();
        intent.putExtra("IS_PROCEED", false);
        intent.putExtra("openProfileForChat", false);
        intent.putExtra("prefillData", false);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("urlText", urlText);
        intent.putExtra("fromWhere", fromWhereData);
        setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent);
        finish();
    }

    private void openKundlis(int index){
        //CUtils.fcmAnalyticsEvents("user_profile_selected", AstrosageKundliApplication.currentEventType,"");
        Bundle bundle = new Bundle();
        bundle.putBoolean(com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_PROFILE_QUERY_DATA, true);
        bundle.putInt("PAGER_INDEX", index);
        Intent intent = new Intent(this, HomeInputScreen.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_SELECT_KUNDALI);
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        //response = "{\"status\":\"2\", \"msg\":\"\"}";
        // Log.e("LoadMore response ", response);
        if (response != null && response.length() > 0) {
            if (method == 1) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        CUtils.saveUserSelectedProfileInPreference(context, userProfileData);
                        CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.update_successfully), context);
                        //showToast(getResources().getString(R.string.update_successfully));
                    } else if (status.equals("100")) {
                        CUtils.fcmAnalyticsEvents("bg_login_from_profile_for_chat",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                        startBackgroundLoginService();
                    } else {
                        CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.update_not_successfully), context);
                        showToast(getResources().getString(R.string.update_not_successfully));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            CUtils.showSnackbar(rlSavedParentView, getResources().getString(R.string.server_error), context);
            showToast(getResources().getString(R.string.server_error));
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(context)) {
                Intent intent = new Intent(context, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
        }
    }

    public void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AstrosageKundliApplication.currentChatStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CANCELED;
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AstrosageKundliApplication.currentChatStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CANCELED;
    }
}