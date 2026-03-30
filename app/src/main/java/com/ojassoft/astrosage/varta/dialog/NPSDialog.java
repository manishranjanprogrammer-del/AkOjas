package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAllAstrologers;
import com.ojassoft.astrosage.varta.adapters.NpsRatingAdapter;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPSDialog extends DialogFragment implements View.OnClickListener, VolleyResponse {

    private Context mContext;
    private View view;
    private TextView tvDRDTitle;

    private Button tvDRDSubmit;
    private RecyclerView ratingRV;
    private ImageView ivDRDClose;
    private Activity mActivity;
    private RequestQueue queue;
    CustomProgressDialog pd;
    String astrologerID;
    String callsId, customId;
    String consultationType;
    int ratingInt = 0;
    private LinearLayout snackbarView;

    private NpsRatingAdapter npsRatingAdapter;
    private final int SUBMIT_FEEDBACK_REQ = 1;

    public NPSDialog() {
    }

    public NPSDialog(Context context, Activity activity, String astrologerID, String callsId, String customId, String consultationType) {
        this.mContext = context;
        this.mActivity = activity;
        this.astrologerID = astrologerID;
        this.callsId = callsId;
        this.customId = customId;
        this.consultationType = consultationType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_nps, container);
        try {
            if (mContext == null)
                mContext = getActivity();
            queue = VolleySingleton.getInstance(mContext).getRequestQueue();

            tvDRDTitle = view.findViewById(R.id.tvDRDTitle);
            tvDRDSubmit = view.findViewById(R.id.tvDRDSubmit);
            ratingRV = view.findViewById(R.id.ratingRV);
            ivDRDClose = view.findViewById(R.id.ivDRDClose);
            snackbarView = view.findViewById(R.id.rlDRDParent);

            FontUtils.changeFont(mContext, tvDRDTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(mContext, tvDRDSubmit, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            ivDRDClose.setOnClickListener(this);
            tvDRDSubmit.setOnClickListener(this);
            Integer[] ratings = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            List<Integer> ratingsList =  Arrays.asList(ratings);
            npsRatingAdapter = new NpsRatingAdapter(mActivity, ratingsList);
            ratingRV.setLayoutManager( new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
            ratingRV.setAdapter(npsRatingAdapter);
        } catch (Exception e) {
            Log.e("NpsRatingAdapter", "exp="+e);
        }

        return view;
    }


    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("rating_dakshina_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDRDClose:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NPS_BTN_CLOSE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                dismiss();
                break;
            case R.id.tvDRDSubmit:
                if (validation()) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NPS_SUBMIT_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    //float rating = starsDRD.getRating();
                    //int ratingInt = (int) rating;
                    submitFeedback(ratingInt);
                }
                break;
        }
    }

    public boolean validation() {
        boolean isValid = true;
        if (ratingInt == 0) {
            CUtils.showSnackbar(snackbarView, getResources().getString(R.string.select_rating), mActivity);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        Log.e("NPSDialog", "response=" + response);
        if (response != null && response.length() > 0) {
            try {
                if (method == SUBMIT_FEEDBACK_REQ) {
                    JSONObject jsonObject = new JSONObject(response);
                    final String status = jsonObject.getString("status");
                    if (status.contains("1")) {
                        CUtils.showSnackbar(snackbarView, getResources().getString(R.string.feedback_success), mContext);
                    } else {
                        CUtils.showSnackbar(snackbarView, getResources().getString(R.string.server_error), mContext);
                    }
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
        CUtils.showVolleyErrorRespMessage(view, mContext, error,"7");
    }

    private void submitFeedback(int rating) {

        if (!CUtils.isConnectedWithInternet(mContext)) {
            CUtils.showSnackbar(snackbarView, getResources().getString(R.string.no_internet), mContext);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(mContext);
            pd.show();
            pd.setCancelable(false);
            enableDesableSubmitBtn(false);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.USER_NPS_SUBMIT_URL,
                    NPSDialog.this, false, getFeedbackParams(rating), SUBMIT_FEEDBACK_REQ).getMyStringRequest();
            queue.add(stringRequest);
        }
    }

    public Map<String, String> getFeedbackParams(int rating) {

        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(mContext);
        String offerType = CUtils.getCallChatOfferType(mContext);
        String phoneNo = CUtils.getUserID(mContext);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerID);
        headers.put(CGlobalVariables.NPS_RATING, String.valueOf(rating));
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(mContext));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(mContext));
        headers.put(CGlobalVariables.USER_PHONE_NO, phoneNo);
        headers.put(CGlobalVariables.KEY_CALLS_ID, callsId);
        headers.put(CGlobalVariables.KEY_CUSTOMID, customId);
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(CGlobalVariables.KEY_CONSULTATION_TYPE, consultationType);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        String gender = "NA";
        String hourofbirth = "NA";
        String minuteofbirth = "NA";
        String secondofbirth = "NA";
        String dayofbirth = "NA";
        String monthofbirth = "NA";
        String yearofbirth = "NA";

        if (userProfileData != null) {
            gender = userProfileData.getGender();
            hourofbirth = userProfileData.getHour();
            minuteofbirth = userProfileData.getMinute();
            secondofbirth = userProfileData.getSecond();
            dayofbirth = userProfileData.getDay();
            monthofbirth = userProfileData.getMonth();
            yearofbirth = userProfileData.getYear();
        }

        headers.put(CGlobalVariables.KEY_GENDER, gender);
        headers.put("hourofbirth", hourofbirth);
        headers.put("minuteofbirth", minuteofbirth);
        headers.put("secondofbirth", secondofbirth);
        headers.put("dayofbirth", dayofbirth);
        headers.put("monthofbirth", monthofbirth);
        headers.put("yearofbirth", yearofbirth);

        Log.e("NPSDialog", "headers=" + headers);
        return headers;
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
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

}
