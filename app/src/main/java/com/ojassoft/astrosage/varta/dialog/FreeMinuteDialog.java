package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;


public class FreeMinuteDialog extends DialogFragment implements View.OnClickListener {

    Activity activity;
    TextView thankuTxt, getCallShortlyTxt,
            callNotRecieveMsgTxt;
    LinearLayout mainlayout;
    Button freeMinuteButton, okButton;
    private String offerType;
    public FreeMinuteDialog(){
        
    }
    public FreeMinuteDialog(String offerType) {
        this.offerType = offerType;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.free_minute_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        thankuTxt = view.findViewById(R.id.thanku_txt);
        getCallShortlyTxt = view.findViewById(R.id.get_call_shortly_txt);
        callNotRecieveMsgTxt = view.findViewById(R.id.call_not_recieve_msg_txt);
        freeMinuteButton = view.findViewById(R.id.free_minute_button);
        try {
            if(offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
                freeMinuteButton.setText(getActivity().getResources().getString(R.string.first_call_free));
            }else {
                freeMinuteButton.setText(getActivity().getResources().getString(R.string.first_minute_free));
            }
        }catch (Exception e){

        }


        okButton = view.findViewById(R.id.ok_button);
        mainlayout = view.findViewById(R.id.mainlayout);

        FontUtils.changeFont(getActivity(), thankuTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), getCallShortlyTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), callNotRecieveMsgTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), freeMinuteButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(getActivity(), okButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        freeMinuteButton.setOnClickListener(this);
        okButton.setOnClickListener(this);

       /* try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 1500);
        }catch (Exception e){
            e.printStackTrace();
        }*/

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.free_minute_button:
                //dismiss();
                break;

            case R.id.ok_button:
                dismiss();
                break;
        }
    }
}
