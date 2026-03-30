package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;


public class FreeMinuteMinimizeDialog extends DialogFragment implements View.OnClickListener {

    Activity activity;
    TextView thankuTxt, getCallShortlyTxt;

    public FreeMinuteMinimizeDialog() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.free_minute_minimize_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setGravity(Gravity.BOTTOM);

        thankuTxt = view.findViewById(R.id.thanku_txt);
        getCallShortlyTxt = view.findViewById(R.id.get_call_shortly_txt);

        FontUtils.changeFont(getActivity(), thankuTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), getCallShortlyTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Log.e("SAN ", "FreeMinuteMinimizeDialog onDestroy call");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.free_minute_button:
                //dismiss();
                break;

            case R.id.ok_button:
                //Log.e("SAN ", "FreeMinuteMinimizeDialog button ok call");
                dismiss();
                break;
        }
    }
}
