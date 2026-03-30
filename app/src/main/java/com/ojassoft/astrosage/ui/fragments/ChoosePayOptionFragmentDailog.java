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
 * Created by ojas on १३/२/१७.
 */
public class ChoosePayOptionFragmentDailog extends DialogFragment implements View.OnClickListener {
    RadioGroup payOptions;
    private int oldPayIndex = 0, checkedRadioButtonId = R.id.radioGoogle;
    private EditText etmobile;
    //PaytmPGService Service = null;
    TextView tvGoogle, tvPaytm,radiosubGoogle,radiosubPaytm, tvRazor,radiosubRazor;
    int radioBtnId[] = {R.id.radioGoogle, R.id.radioPaytm,R.id.radioRazor};
    private Button btnOk, btnCancel;
    private IChoosePayOption iChoosePayOption;
    private LinearLayout llPaytm,llGoogle,llRazor;
    View paytmview,razorpayview;
    boolean razorPaymentVisibility = false,
            googleWalletPaymentVisibility  = false,
            paytmPaymentVisibility = false;
/*
    SharedPreferences sharedPreferencesForPay;
*/
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* sharedPreferencesForPay = getActivity()
                .getSharedPreferences(
                        CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                        Context.MODE_PRIVATE);
        oldPayIndex = sharedPreferencesForPay.getInt(
                CGlobalVariables.SELECTED_PAY_OPTION, 0);*/


        View view = inflater.inflate(R.layout.choose_pay_option_frag_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvRazor = (TextView) view.findViewById(R.id.radioRazor);
        radiosubRazor=(TextView) view.findViewById(R.id.radiosubRazor);
        tvGoogle = (TextView) view.findViewById(R.id.radioGoogle);
        tvPaytm = (TextView) view.findViewById(R.id.radioPaytm);
        radiosubGoogle=(TextView) view.findViewById(R.id.radiosubGoogle);
        radiosubPaytm=(TextView) view.findViewById(R.id.radiosubPaytm);
        llPaytm=(LinearLayout) view.findViewById(R.id.llPaytm);
        llGoogle=(LinearLayout) view.findViewById(R.id.llGoogle);
        llRazor =(LinearLayout) view.findViewById(R.id.llRazor);
        razorpayview = (View)view.findViewById(R.id.razorpayview);
        paytmview = (View)view.findViewById(R.id.paytmview);
        llRazor.setOnClickListener(this);
        llPaytm.setOnClickListener(this);
        llGoogle.setOnClickListener(this);

        tvRazor.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        tvGoogle.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
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



         razorPaymentVisibility = CUtils.getBooleanData(activity, CGlobalVariables.key_RazorPayVisibilityServices,true);
         googleWalletPaymentVisibility = CUtils.getBooleanData(activity,CGlobalVariables.key_GoogleWalletPaymentVisibility,true);
         paytmPaymentVisibility = CUtils.getBooleanData(activity,CGlobalVariables.key_PaytmPaymentVisibility,true);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            razorPaymentVisibility=false;
            CUtils.saveBooleanData(activity, CGlobalVariables.key_RazorPayVisibilityServices, razorPaymentVisibility);
        }

        if(!googleWalletPaymentVisibility){
            tvGoogle.setVisibility(View.GONE);
            radiosubGoogle.setVisibility(View.GONE);
            paytmview.setVisibility(View.GONE);
            //checkedRadioButtonId = radioBtnId[1];
        }

        if(!paytmPaymentVisibility){
            tvPaytm.setVisibility(View.GONE);
            radiosubPaytm.setVisibility(View.GONE);
            paytmview.setVisibility(View.GONE);
            //checkedRadioButtonId = radioBtnId[2];
        }

        if(!razorPaymentVisibility){
            tvRazor.setVisibility(View.GONE);
            radiosubRazor.setVisibility(View.GONE);
            razorpayview.setVisibility(View.GONE);
            //checkedRadioButtonId = radioBtnId[0];
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.llGoogle:
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

            case R.id.llRazor:
          /*  case R.id.radioGoogle:
            case R.id.radiosubGoogle:*/
                checkedRadioButtonId = radioBtnId[2];
                iChoosePayOption.onSelectedOption(checkedRadioButtonId,"");
                dismiss();
                break;
        }

    }
}
