package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAlertDialog extends DialogFragment implements View.OnClickListener, VolleyResponse {
    Activity activity;
    AstrologerDetailBean astrologerDetailBean;
    TextView tvNotifAlertMsg;
    ImageView ivNotifAlertClose;
    Button btnNotifAlertYes,btnNotifAlertNo;
    RequestQueue queue;
    final int ADD_TO_QUEUE_METHOD = 40;

    NotificationAlertDialog(){}
    int isofflinenoti = 0;

    public NotificationAlertDialog(Activity activity, AstrologerDetailBean astrologerDetailBean){
        this.activity = activity;
        this.astrologerDetailBean = astrologerDetailBean;
    }
    public NotificationAlertDialog(Activity activity, AstrologerDetailBean astrologerDetailBean,int isofflinenoti){
        this.activity = activity;
        this.astrologerDetailBean = astrologerDetailBean;
        this.isofflinenoti = isofflinenoti;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
        View view = inflater.inflate(R.layout.notification_alert_layout,container);
        if (getDialog() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        tvNotifAlertMsg = view.findViewById(R.id.tvNotifAlertMsg);
        ivNotifAlertClose = view.findViewById(R.id.ivNotifAlertClose);
        btnNotifAlertYes = view.findViewById(R.id.btnNotifAlertYes);
        btnNotifAlertNo = view.findViewById(R.id.btnNotifAlertNo);

        String noBtnText = activity.getString(R.string.no) + ", " + activity.getString(R.string.button_thanks);
        btnNotifAlertNo.setText(noBtnText);
        if (isofflinenoti == 1){
            tvNotifAlertMsg.setText(getString(R.string.do_you_want_to_get_notified_when_astrologer_gets_online));
        }
        FontUtils.changeFont(activity, tvNotifAlertMsg, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(activity, btnNotifAlertYes, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(activity, btnNotifAlertNo, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        ivNotifAlertClose.setOnClickListener(this);
        btnNotifAlertYes.setOnClickListener(this);
        btnNotifAlertNo.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("notification_alert_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNotifAlertYes){
            sendNotification(astrologerDetailBean);
        } else if (v.getId() == R.id.btnNotifAlertNo){
            this.dismiss();
        } else if (v.getId() == R.id.ivNotifAlertClose){
            this.dismiss();
        }
    }

    private void sendNotification(AstrologerDetailBean astrologerDetailBean) {
        if (CUtils.isConnectedWithInternet(activity)) {

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getNotificationIfAstroBusy(getQueueParams(astrologerDetailBean));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if (status.equals("100")) {
                            if (activity instanceof AstrologerDescriptionActivity){
                                ((AstrologerDescriptionActivity)activity).startBackgroundLoginServiceFromDialog();
                            }
                        } else {
                            CUtils.showSnackbar(activity.findViewById(android.R.id.content),message,activity);
                        }
                        NotificationAlertDialog.this.dismiss();
                    } catch (Exception e) {
                        try {
                            CUtils.showSnackbar(activity.findViewById(android.R.id.content), getResources().getString(R.string.something_wrong_error), activity);
                        } catch (Exception e1){
                            //
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        NotificationAlertDialog.this.dismiss();
                        CUtils.showSnackbar(activity.findViewById(android.R.id.content), getResources().getString(R.string.something_wrong_error), activity);
                    } catch (Exception e){
                        //
                    }
                }
            });

            /*StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ADD_TO_QUEUE,
                    this, false, getQueueParams(astrologerDetailBean), ADD_TO_QUEUE_METHOD).getMyStringRequest();
            queue.add(stringRequest);*/
        }
    }

    public Map<String, String> getQueueParams(AstrologerDetailBean astrologerDetailBean) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(activity));
        headers.put(CGlobalVariables.URL_TEXT, astrologerDetailBean.getUrlText());
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(activity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, "" + CUtils.getMyAndroidId(activity));
        headers.put(CGlobalVariables.ASTROLOGER_ID, "" + astrologerDetailBean.getAstrologerId());
        headers.put(CGlobalVariables.KEY_USER_ID, "" + CUtils.getUserIdForBlock(activity));
        headers.put(CGlobalVariables.IS_OFFLINE_NOTI, "" + isofflinenoti);

        Log.d("queueParams", "getQueueParams: "+headers);
        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
        if (!TextUtils.isEmpty(response)){
            Log.d("queueParams","queue response:- "+response);
            if (method == ADD_TO_QUEUE_METHOD){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("100")){
                        if (activity instanceof AstrologerDescriptionActivity){
                            ((AstrologerDescriptionActivity)activity).startBackgroundLoginServiceFromDialog();
                        }
                    } else {
                        CUtils.showSnackbar(activity.findViewById(android.R.id.content),message,activity);
                    }
                    this.dismiss();
                } catch (Exception e){
                    //
                }
            }
        }
    }

    @Override
    public void onError(VolleyError error) {

    }
}
