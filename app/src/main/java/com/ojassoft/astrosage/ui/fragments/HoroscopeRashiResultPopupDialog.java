package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * Created by ojas on 4/5/16.
 */
public class HoroscopeRashiResultPopupDialog extends AstroSageParentDialogFragment {

    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    String msg, btnText;
    int rashiIndexFromServer;
    static final String msgKey = "MsgKey";
    static final String btnTxtKey = "btnTxtKey";
    static final String rashiIndexFromServerKey = "rashiIndexFromServerKey";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        mediumTypeface = ((BaseInputActivity) activity).mediumTypeface;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msg = getArguments().getString(msgKey);
        btnText = getArguments().getString(btnTxtKey);
        rashiIndexFromServer = getArguments().getInt(rashiIndexFromServerKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.lay_alert_pop_up, container);
        TextView heading = (TextView) view.findViewById(R.id.tvPopupHeading);

        heading.setText(msg);
        Button btnChange = (Button) view.findViewById(R.id.btnPopupGetRashi);
        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            btnChange.setText(btnText.toUpperCase());
        } else {
            btnChange.setText(btnText);
        }
        heading.setTypeface(regularTypeface);
        btnChange.setTypeface(mediumTypeface);
        btnChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                ((HoroscopeHomeActivity) activity).gotoRashiPrediction(rashiIndexFromServer);
            }

        });
        return view;

    }

    public static HoroscopeRashiResultPopupDialog getInstance(String msg, String btnText, int rashiIndex) {
        Bundle bundle = new Bundle();
        bundle.putString(msgKey, msg);
        bundle.putString(btnTxtKey, btnText);
        bundle.putInt(rashiIndexFromServerKey, rashiIndex);
        HoroscopeRashiResultPopupDialog horoscopeRashiResultPopupDialog = new HoroscopeRashiResultPopupDialog();
        horoscopeRashiResultPopupDialog.setArguments(bundle);
        return horoscopeRashiResultPopupDialog;
    }
}
