package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_SEND_GIFT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ask_A_Question_Android;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.adapters.LiveGiftAdapter;
import com.ojassoft.astrosage.varta.model.GiftModel;
import com.ojassoft.astrosage.varta.model.UserReviewBean;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.ojassoft.astrosage.vartalive.activities.LiveActivityNew;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingAndDakshinaDialog extends DialogFragment implements View.OnClickListener, VolleyResponse {

    private Context mContext;
    private View view;
    private TextView tvDRDBalance, tvDRDTitle, tvDRDHeading, tvDRDRecharge, tvDRDSubmit;
    private RecyclerView rvDRDGiftList;
    private AppCompatRatingBar starsDRD;
    private EditText etDRDFeedback;
    private ImageView ivDRDClose;
    private ArrayList<GiftModel> giftModelArrayList;
    private Activity mActivity;
    private RequestQueue queue;
    CustomProgressDialog pd;
    ProgressBar progressDRD;
    UserReviewBean userOwnReviewBean;
    String astrologerID, mobilePhoneNo;
    int ratingInt = 0;
    private GiftModel giftModel;
    private LiveGiftAdapter giftAdapter;
    private RelativeLayout snackbarView;
    private final int SUBMIT_FEEDBACK_REQ = 1, SEND_GIFT_REQ = 2, FETCH_LIVE_ASTROLOGER = 3;
    private boolean isStartedForWallet = false;

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                if (isStartedForWallet){
                    isStartedForWallet = false;
                    getWalletBalance();
                }
            } else {
                CUtils.showSnackbar(snackbarView, getResources().getString(R.string.something_wrong_error), mContext);
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    public RatingAndDakshinaDialog() {
    }

    public RatingAndDakshinaDialog(Context context, Activity activity, String mobilePhoneNo, String astrologerID, UserReviewBean userOwnReviewBean) {
        this.mContext = context;
        this.mActivity = activity;
        this.mobilePhoneNo = mobilePhoneNo;
        this.astrologerID = astrologerID;
        this.userOwnReviewBean = userOwnReviewBean;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.dialog_rating_dakshina, container);
        try {
            if(mContext == null)
                mContext = getActivity();
            queue = VolleySingleton.getInstance(mContext).getRequestQueue();

            tvDRDBalance = view.findViewById(R.id.tvDRDBalance);
            tvDRDTitle = view.findViewById(R.id.tvDRDTitle);
            tvDRDHeading = view.findViewById(R.id.tvDRDHeading);
            tvDRDRecharge = view.findViewById(R.id.tvDRDRecharge);
            tvDRDSubmit = view.findViewById(R.id.tvDRDSubmit);
            rvDRDGiftList = view.findViewById(R.id.rvDRDGiftList);
            starsDRD = view.findViewById(R.id.starsDRD);
            etDRDFeedback = view.findViewById(R.id.etDRDFeedback);
            ivDRDClose = view.findViewById(R.id.ivDRDClose);
            snackbarView = view.findViewById(R.id.rlDRDParent);
            progressDRD = view.findViewById(R.id.progressDRD);

            FontUtils.changeFont(mContext, tvDRDBalance, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(mContext, tvDRDTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(mContext, tvDRDHeading, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(mContext, tvDRDRecharge, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(mContext, tvDRDSubmit, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            ivDRDClose.setOnClickListener(this);
            tvDRDRecharge.setOnClickListener(this);
            tvDRDSubmit.setOnClickListener(this);

            if (CUtils.getGiftModelArrayList() != null && !CUtils.getGiftModelArrayList().isEmpty()) {
                giftModelArrayList = CUtils.getGiftModelArrayList();
                setupGiftAdapter();
            } else {
//                StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_LIVE_ASTRO_URL,
//                        this, false, CUtils.getLiveAstroParams(mContext,CUtils.getActivityName(mContext)), FETCH_LIVE_ASTROLOGER).getMyStringRequest();
//                queue.add(stringRequest);
                showProgressBar();
                ApiList api = RetrofitClient.getInstance().create(ApiList.class);
                Call<ResponseBody> call = api.liveAstroList(CUtils.getLiveAstroParams(mContext,CUtils.getActivityName(mContext)));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            hideProgressBar();
                            String myResponse = response.body().string();
                            CUtils.saveLiveAstroList(myResponse);
                            CUtils.parseGiftList(myResponse);
                            giftModelArrayList = CUtils.getGiftModelArrayList();
                            setupGiftAdapter();
                        }catch (Exception e){
                          //
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideProgressBar();
                        Toast.makeText(mActivity, mActivity.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (userOwnReviewBean != null) {
                if (userOwnReviewBean.getRate() != null && userOwnReviewBean.getRate().length() > 0) {
                    starsDRD.setRating(Float.parseFloat(userOwnReviewBean.getRate()));
                }

                if (userOwnReviewBean.getComment() != null && userOwnReviewBean.getComment().length() > 0) {
                    etDRDFeedback.setText(userOwnReviewBean.getComment());
                }
            }


        } catch (Exception e) {
            //
        }

        return view;
    }

    private void setupGiftAdapter(){
        rvDRDGiftList.setLayoutManager(new GridLayoutManager(mContext, 3));
        giftAdapter = new LiveGiftAdapter(mActivity, giftModelArrayList, true);
        rvDRDGiftList.setAdapter(giftAdapter);

        giftAdapter.onItemClick = (view, position) -> {
            try {
                int alreadySelected = -1;
                for (int i = 0; i < giftModelArrayList.size(); i++) {
                    GiftModel mGiftModel = giftModelArrayList.get(i);
                    if (mGiftModel.isSelected()) alreadySelected = i;
                    mGiftModel.setSelected(false);
                }
                GiftModel sGiftModel = giftModelArrayList.get(position);
                sGiftModel.setSelected(alreadySelected != position);
                giftAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                //
            }
        };
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("rating_dakshina_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }

    @Override
    public void onResume() {
        super.onResume();
        getWalletBalance();
    }

    private void getWalletBalance() {
        Call<ResponseBody> call = RetrofitClient.getInstance().create(ApiList.class).getWalletBalance(getWalletParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String myResponse = response.body().string();
                        JSONObject object = new JSONObject(myResponse);
                        String status = "";
                        if (object.has("status")) {
                            status = object.getString("status");
                        }
                        if (status.equalsIgnoreCase("100")) {
                            startBackgroundLoginServiceForWallet();
                        } else {
                            String walletBal = object.getString("userbalancce");
                            if (walletBal.length() > 0) {
                                CUtils.setWalletRs(mContext, walletBal);
                                String balance = mActivity.getString(R.string.balance);
                                balance = balance + ": " + getResources().getString(R.string.astroshop_rupees_sign) +
                                        CUtils.convertAmtIntoIndianFormat(walletBal);
                                tvDRDBalance.setText(balance);
                            }
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public Map<String, String> getWalletParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(mContext));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(mContext));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        return CUtils.setRequiredParams(headers);
    }

    public void startBackgroundLoginServiceForWallet() {
        isStartedForWallet = true;
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
        startBackgroundLoginService();
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(mContext)) {
                Intent intent = new Intent(mContext, Loginservice.class);
                mContext.startService(intent);
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            Dialog dialog = getDialog();
            if (dialog != null) {
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
            }
        }catch (Exception e){
            //
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDRDClose:
                dismiss();
                break;
            case R.id.tvDRDRecharge:
                Intent intent = new Intent(mContext, WalletActivity.class);
                intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "MainChatFragment");
                startActivity(intent);
                break;
            case R.id.tvDRDSubmit:
                /*if (validation()) {
                    String feedbackStr = etDRDFeedback.getText().toString();
                    float rating = starsDRD.getRating();
                     ratingInt = (int) rating;
                    submitFeedback(mobilePhoneNo, astrologerID, ratingInt, feedbackStr);
                }*/
                sendGift();
                break;
        }
    }

    public boolean validation() {
        boolean isValid = true;
        if (starsDRD.getRating() == 0.0) {
            CUtils.showSnackbar(snackbarView, getResources().getString(R.string.select_rating), mActivity);
            isValid = false;
        }
        if(!TextUtils.isEmpty(etDRDFeedback.getText().toString())){
            if (isContain(etDRDFeedback.getText().toString(), CGlobalVariables.abuseKeyword)) {
                CUtils.showSnackbar(snackbarView, getResources().getString(R.string.valid_word_in_feedback), mActivity);
                isValid = false;
            }
        }
        return isValid;
    }
    private  boolean isContain(String inputString, String[] items){
        String lowerCase = inputString.toLowerCase();
        boolean found = false;
        for (String item : items) {
            String pattern = "\\b"+item+"\\b";
            Pattern p=Pattern.compile(pattern);
            Matcher m=p.matcher(lowerCase);
            if(m.find()){
                found = true;
                break;
            }

        }
        return found;
    }
    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        //Log.e("LoadMore", "response="+response);
        if (response != null && response.length() > 0) {
            try {
                if (method == SUBMIT_FEEDBACK_REQ) {
//                    //{"status":"1","msg":"Feedback inserted successfully."}
//                    //Log.e("SAN RADD ", "response="+response);
//                    JSONObject jsonObject = new JSONObject(response);
//                    final String status = jsonObject.getString("status");
//                    if (status.contains("1")) {
//                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_SUBMIT_BTN,
//                                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//                        etDRDFeedback.setText("");
//                        CUtils.showSnackbar(snackbarView, getResources().getString(R.string.feedback_success), mContext);
//                    } else {
//                        CUtils.showSnackbar(snackbarView, getResources().getString(R.string.server_error), mContext);
//                    }
//                    try {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    if (status.contains("1")) {
//                                        if (mContext != null) {
//                                            if (ratingInt > 4) {
//                                                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_RATE_APP_DIALOG,
//                                                        CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
//                                               /* String Headingtext = mContext
//                                                        .getString(R.string.app_mainheading_text_kundali);
//                                                String SubHeadingtext = mContext
//                                                        .getString(R.string.app_subchild_text_ask_a_question);
//                                                String Subchildheading = mContext
//                                                        .getString(R.string.app_subchild_text_kundali);
//                                                AppRater.app_launched(mContext,
//                                                        Headingtext, SubHeadingtext, Subchildheading);*/
//
//                                                BottomSheetRatingDialog bottomSheet = new BottomSheetRatingDialog();
//                                                bottomSheet.show(getFragmentManager(),"ModalBottomSheet");
//
//                                            }
//                                        }
//                                    }
//                                    dismiss();
//
//                                } catch (Exception e) {
//
//                                }
//                            }
//                        }, 1000); // Millisecond 1000 = 1 sec
//                    }catch (Exception e){
//
//                    }
//


                } else if (method == SEND_GIFT_REQ) {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        CUtils.showSnackbar(snackbarView, getResources().getString(R.string.gift_sucessful), mContext);
                        updateWalletBalance();
                        makeGiftUnSelected();
                    }
                    new Handler().postDelayed(this::dismiss, 2000);

                } else if (method == FETCH_LIVE_ASTROLOGER){
//                    CUtils.saveLiveAstroList(response);
//                    CUtils.parseGiftList(response);
//                    giftModelArrayList = CUtils.getGiftModelArrayList();
//                    setupGiftAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CUtils.showSnackbar(snackbarView, getResources().getString(R.string.server_error), mContext);
        }
        enableDesableSubmitBtn(true);
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        CUtils.showVolleyErrorRespMessage(view, mContext, error,"8");
    }

    private void submitFeedback(String mobileNo, String astrologerId, int rating, String feedback) {

        if (!CUtils.isConnectedWithInternet(mContext)) {
            CUtils.showSnackbar(snackbarView, getResources().getString(R.string.no_internet), mContext);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(mContext);
            pd.show();
            pd.setCancelable(false);
            enableDesableSubmitBtn(false);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ASTROLOGER_FEEDBACK_SUBMIT_URL,
//                    RatingAndDakshinaDialog.this, false, getCallParams(mobileNo, astrologerId, rating, feedback), SUBMIT_FEEDBACK_REQ).getMyStringRequest();
//            queue.add(stringRequest);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.submitFeedback(getCallParams(mobileNo, astrologerId, rating, feedback));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if(response.body()!=null){
                            //{"status":"1","msg":"Feedback inserted successfully."}
                            //Log.e("SAN RADD ", "response="+response);
                            String myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            final String status = jsonObject.getString("status");
                            if (status.contains("1")) {
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_DETAIL_FEEDBACK_SUBMIT_BTN,
                                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                etDRDFeedback.setText("");
                                CUtils.showSnackbar(snackbarView, getResources().getString(R.string.feedback_success), mContext);
                            } else {
                                CUtils.showSnackbar(snackbarView, getResources().getString(R.string.server_error), mContext);
                            }
                            try {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (status.contains("1")) {
                                                if (mContext != null) {
                                                    if (ratingInt > 4) {
                                                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_RATE_APP_DIALOG,
                                                                CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                                               /* String Headingtext = mContext
                                                        .getString(R.string.app_mainheading_text_kundali);
                                                String SubHeadingtext = mContext
                                                        .getString(R.string.app_subchild_text_ask_a_question);
                                                String Subchildheading = mContext
                                                        .getString(R.string.app_subchild_text_kundali);
                                                AppRater.app_launched(mContext,
                                                        Headingtext, SubHeadingtext, Subchildheading);*/

                                                        BottomSheetRatingDialog bottomSheet = new BottomSheetRatingDialog();
                                                        bottomSheet.show(getFragmentManager(),"ModalBottomSheet");

                                                    }
                                                }
                                            }
                                            dismiss();

                                        } catch (Exception e) {

                                        }
                                    }
                                }, 1000); // Millisecond 1000 = 1 sec
                            }catch (Exception e){
                                //
                            }
                        }
                    }catch (Exception e){
                        //
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public Map<String, String> getCallParams(String phoneNo, String astrologerId, int rating, String feedback) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(CGlobalVariables.USER_PHONE_NO, phoneNo);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.STAR_RATING, "" + rating);
        headers.put(CGlobalVariables.FEEDBACK, feedback);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(mContext));
        headers.put(CGlobalVariables.SOURCE_PARAMS, CUtils.getAppPackageName(mContext));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.e("LoadMore", "headers="+headers);
        return CUtils.setRequiredParams(headers);
    }

    private void enableDesableSubmitBtn(boolean isEnabled) {
        if (isEnabled) {
            tvDRDSubmit.setEnabled(true);
            tvDRDSubmit.setAlpha(1);
            tvDRDSubmit.setOnClickListener(this);
        } else {
            tvDRDSubmit.setEnabled(false);
            tvDRDSubmit.setAlpha(0.6f);
            tvDRDSubmit.setOnClickListener(null);
        }
    }

    private void hideProgressBar() {
        progressDRD.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progressDRD.setVisibility(View.VISIBLE);
    }

    private void sendGift() {
        try {
            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_SEND_GIFT, FIREBASE_EVENT_ITEM_CLICK, "");
            giftModel = null;
            for (int i = 0; i < giftModelArrayList.size(); i++) {
                giftModel = giftModelArrayList.get(i);
                if (giftModel.isSelected()) {
                    break;
                } else {
                    giftModel = null;
                }
            }
            if (giftModel != null) {
                if (Float.parseFloat(giftModel.getActualraters()) > Float.parseFloat(CUtils.getWalletRs(mContext))) {
                    CUtils.showSnackbar(snackbarView, getResources().getString(R.string.insufficient_wallet_balance), mContext);
                } else {
                    sendGiftRequest(giftModel.getServiceid());
                }
            } else {
                CUtils.showSnackbar(snackbarView, getResources().getString(R.string.select_send_gift_error), mContext);
            }
        } catch (Exception e) {
            //
        }
    }

    public void sendGiftRequest(String giftId) {

        if (CUtils.isConnectedWithInternet(mContext)) {
//            String url = CGlobalVariables.SEND_GIFT_URL;
            //Log.e("SAN ", " gift channel name " + channelName );
            //Log.e("SAN ", " gift url " + url );
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                    this, false, sendGiftParams(giftId), SEND_GIFT_REQ).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.sendGift(sendGiftParams(giftId));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body()!=null){
                        try{
                            String myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                CUtils.showSnackbar(snackbarView, getResources().getString(R.string.gift_sucessful), mContext);
                                updateWalletBalance();
                                makeGiftUnSelected();
                            }
                            new Handler().postDelayed(RatingAndDakshinaDialog.this::dismiss, 2000);
                        }catch (Exception e){
                            //
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } else {
            CUtils.showSnackbar(snackbarView, getResources().getString(R.string.no_internet), mContext);
        }
    }

    public Map<String, String> sendGiftParams(String giftId) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(mContext));
        try {
            headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(mContext));
            headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(mContext));
            headers.put(CGlobalVariables.CHANNEL_NAME, CUtils.getCountryCode(mContext) + "-" + CUtils.getUserID(mContext));
        } catch (Exception e) {
            //
        }
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(mContext));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(mContext));
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerID);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(mContext));
        headers.put(CGlobalVariables.KEY_GIFT_ID, giftId);

        //Log.e("SAN ", " gift params " + headers.toString() );

        return headers;
    }

    private void updateWalletBalance() {
        try {
            if (giftModel == null)
                return;
            float currWalletBal = Float.parseFloat(CUtils.getWalletRs(mContext));
            currWalletBal = currWalletBal - Float.parseFloat(giftModel.getActualraters());
            CUtils.setWalletRs(mContext, String.valueOf(currWalletBal));
        } catch (Exception e) {
            //
        }
    }

    public void makeGiftUnSelected() {
        try {
            giftModel = null;
            if (giftModelArrayList == null)
                return;
            for (int i = 0; i < giftModelArrayList.size(); i++) {
                GiftModel giftModel = giftModelArrayList.get(i);
                giftModel.setSelected(false);
            }
            giftAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }

    }

}
