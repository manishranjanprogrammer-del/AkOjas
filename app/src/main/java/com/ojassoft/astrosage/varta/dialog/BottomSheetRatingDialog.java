package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.NextOfferBean;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetRatingDialog} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetRatingDialog extends BottomSheetDialogFragment {

    public BottomSheetRatingDialog() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View v = inflater.inflate(R.layout.loving_astrosage_dialog, container, false);
        TextView tvHeading = v.findViewById(R.id.tv_heading);
        TextView tvRateUs = v.findViewById(R.id.tv_rate_us);
        TextView tvRateNow = v.findViewById(R.id.tv_rate_now);
        TextView tvCancel = v.findViewById(R.id.tv_cancel);

        FontUtils.changeFont(getActivity(), tvHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvRateUs, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), tvRateNow, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvCancel, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);


        tvRateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.BOTTOMSHEET_RATE_NOW_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                SharedPreferences prefs = getActivity().getSharedPreferences("rate_app_new",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("HAS_GIVEN_RATE", true);
                editor.commit();

                final Uri marketUri = Uri.parse(com.ojassoft.astrosage.utils.CGlobalVariables.RATE_APP_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, marketUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.BOTTOMSHEET_RATE_NOT_NOW_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                SharedPreferences prefs = getActivity().getSharedPreferences("rate_app_new",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("Not_Showing_Interest_To_Rate_App", true);
                editor.commit(); // StartTimerForPopup(mContext);
                dismiss();
            }
        });
        return v;
    }
}
