package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IChoosePayOption;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on २४/८/१७.
 */


/**
 * Created by ojas on १३/२/१७.
 */
public class ChooseServicePayOPtionDialog extends DialogFragment implements View.OnClickListener {
    RadioGroup payOptions;
    private int oldPayIndex = 0, checkedRadioButtonId = R.id.radioPaytm;
    private EditText etmobile;
    //PaytmPGService Service = null;
    TextView tvRazor, tvPaytm,radiosubRazor,radiosubPaytm;
    int radioBtnId[] = {R.id.radioRazor, R.id.radioPaytm};
    private Button btnOk, btnCancel;
    private IChoosePayOption iChoosePayOption;
    private LinearLayout llPaytm, llRazor;
    private Activity activity;

    /*
        SharedPreferences sharedPreferencesForPay;
    */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_serice_pay_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvRazor = (TextView) view.findViewById(R.id.radioRazor);
        tvPaytm = (TextView) view.findViewById(R.id.radioPaytm);
        radiosubRazor=(TextView) view.findViewById(R.id.radiosubRazor);
        radiosubPaytm=(TextView) view.findViewById(R.id.radiosubPaytm);
        llPaytm=(LinearLayout) view.findViewById(R.id.llPaytm);
        llRazor =(LinearLayout) view.findViewById(R.id.llRazor);
        llPaytm.setOnClickListener(this);
        llRazor.setOnClickListener(this);


        tvRazor.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        tvPaytm.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);


        ((TextView) view.findViewById(R.id.textViewHeading))
                .setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        init();
        return view;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iChoosePayOption = (IChoosePayOption) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iChoosePayOption=null;
        activity=null;
    }

    private void init()
    {
        oldPayIndex = 0;/*sharedPreferencesForPay.getInt(
                CGlobalVariables.SELECTED_PAY_OPTION, 0);*/

        switch (oldPayIndex)
        {
            case 0:
                //langOptions.check(R.id.radioEnglish);
                break;
            case 1:
                //langOptions.check(R.id.radioHindi);
                break;
        }



        boolean razorPaymentVisibility = CUtils.getBooleanData(activity, CGlobalVariables.key_RazorPayVisibilityServices,true);
        boolean paytmPaymentVisibility = CUtils.getBooleanData(activity,CGlobalVariables.key_PaytmWalletVisibilityServices,true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            razorPaymentVisibility=false;
            CUtils.saveBooleanData(activity, CGlobalVariables.key_RazorPayVisibilityServices, razorPaymentVisibility);

        }
        if(!razorPaymentVisibility){
            tvRazor.setVisibility(View.GONE);
            radiosubRazor.setVisibility(View.GONE);
            checkedRadioButtonId = radioBtnId[1];
        }

        if(!paytmPaymentVisibility){
            tvPaytm.setVisibility(View.GONE);
            radiosubPaytm.setVisibility(View.GONE);
            checkedRadioButtonId = radioBtnId[0];
        }
         if(!razorPaymentVisibility && !paytmPaymentVisibility)
        {
            tvRazor.setVisibility(View.VISIBLE);
            radiosubRazor.setVisibility(View.VISIBLE);
            tvPaytm.setVisibility(View.VISIBLE);
            radiosubPaytm.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.llRazor:
          /*  case R.id.radioGoogle:
            case R.id.radiosubGoogle:*/
                checkedRadioButtonId = radioBtnId[0];
                iChoosePayOption.onSelectedOption(checkedRadioButtonId,"");
                dismiss();
                break;
            case R.id.llPaytm:
          /*  case R.id.radioPaytm:
            case R.id.radiosubPaytm:*/
                checkedRadioButtonId = radioBtnId[1];
                iChoosePayOption.onSelectedOption(checkedRadioButtonId,"");
                dismiss();

                break;

        }

    }
}
