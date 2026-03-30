package com.ojassoft.astrosage.varta.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.NextOfferBean;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetDialog} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetDialog extends BottomSheetDialogFragment implements VolleyResponse {

    private Button openNow;
    //RequestQueue queue;
    private NextOfferBean nextOfferBean;
    private TextView mMessageTv, mRupee, mExtra;
    private String rechargeServiceId;
    public BottomSheetDialog(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
//        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
//        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View v = inflater.inflate(R.layout.fragment_bottom_sheet_dialog,
                container, false);
        //queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        ImageView crossButton = v.findViewById(R.id.cross_btn);
        openNow = v.findViewById(R.id.open_now);

        mMessageTv = v.findViewById(R.id.message_tv);
        mExtra = v.findViewById(R.id.extra);
//        mRupee = v.findViewById(R.id.rupee);

        FontUtils.changeFont(getActivity(), mMessageTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), mExtra, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
//        FontUtils.changeFont(getActivity(), mRupee, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
//        FontUtils.changeFont(getActivity(), openNow, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        CUtils.setPrefNextOffer(getActivity(), CUtils.getCurrentDateInString());

        setData();

        //getNextRechargeFromApi();

        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        openNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NEXT_OFFER_RECHARGE_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                if(getActivity() != null) {
                    //com.ojassoft.astrosage.varta.utils.CUtils.createSession(getActivity(), CGlobalVariables.PID_NEXT_OFFER_RECHARGE_CLICK);
                }
                openWalletScreen();
                dismiss();
            }
        });
        return v;
    }

    public void openWalletScreen() {
        Activity activity = getActivity();
        if (activity == null) return;
        try {
            Intent intent = new Intent(activity, WalletActivity.class);
            intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
            intent.putExtra(CGlobalVariables.FROM_ONE_RS_DIALOG, "1");
            intent.putExtra(CGlobalVariables.RECHARGE_SERVICE_ID, rechargeServiceId);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void getNextRechargeFromApi() {
//        if (!CUtils.isConnectedWithInternet(getActivity())) {
//        } else {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, NEXT_RECHARGE,
//                    BottomSheetDialog.this, false, getParamsNew(), 1).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
//        }
//    }

    @SuppressLint("SetTextI18n")
    private void setData() {

        Bundle bndl = getArguments();
        if (bndl != null) {

//            String offerAmountString = bndl.getString("amount").replace("Rs.",getString(R.string.astroshop_rupees_sign));
            String staticText = bndl.getString("extra").replace("Rs.",getString(R.string.astroshop_rupees_sign));
//            String staticText = getResources().getString(R.string.get_extra,offerAmountString);
            mMessageTv.setText(bndl.getString("message").replace("Rs.",getString(R.string.astroshop_rupees_sign)));
            mExtra.setText(staticText);
            rechargeServiceId = bndl.getString("serviceId");
        } else {
            dismiss();
        }
    }


//    public Map<String, String> getParamsNew() {
//
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(getActivity()));
//        headers.put(CGlobalVariables.PHONE_NO, /*"8860085780"*/CUtils.getUserID(getActivity()));
//        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(getContext()));
//        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
//        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(getActivity()));
//        headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
//
//        return CUtils.setRequiredParams(headers);
//    }

    @Override
    public void onResponse(String response, int method) {
        try {
            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) { }


        if (response != null && method == 1) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        nextOfferBean = gson.fromJson(jsonObject.toString(), NextOfferBean.class);

//                        mMessageTv.setText("");
//                        mExtra.setText("");
//                        mRupee.setText("");

                    } else {
                        dismiss();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        dismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("pop_under_recharge_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }
}