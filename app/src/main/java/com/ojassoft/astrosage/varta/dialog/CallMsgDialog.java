package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;


public class CallMsgDialog extends DialogFragment implements View.OnClickListener {

    Activity activity;
    TextView thankuTxt, msgTxt;
    RelativeLayout mainlayout;
    Button okButton;
    ImageView crossBtn;
    String message="", title="";
    boolean isShowOkBtn = false;
    String fromWhich="";

   /* public CallMsgDialog(String message, String title, boolean isShowOkBtn)
    {
        this.message = message;
        this.title = title;
        isShowOkBtn = isShowOkBtn;
    }*/

    public CallMsgDialog(String message, String title, boolean isShowOkBtn, String fromWhich)
    {
        this.message = message;
        this.title = title;
        isShowOkBtn = isShowOkBtn;
        this.fromWhich = fromWhich;
    }

    public CallMsgDialog(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.call_msg_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(false);
        thankuTxt = view.findViewById(R.id.thanku_txt);
        msgTxt = view.findViewById(R.id.msg_txt);
        crossBtn = view.findViewById(R.id.cross_btn);
        okButton = view.findViewById(R.id.ok_button);
        //mainlayout = view.findViewById(R.id.mainlayout);

        FontUtils.changeFont(getActivity(), thankuTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), msgTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), okButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        msgTxt.setText(message);
        if(title.length()>0)
        {
            thankuTxt.setText(title);
        }

        /*if(isShowOkBtn)
        {*/
            okButton.setVisibility(View.VISIBLE);
        /*}else
        {
            okButton.setVisibility(View.GONE);
        }*/

        okButton.setOnClickListener(this);
        crossBtn.setOnClickListener(this);

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
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        //Log.e("LoadMore ", "Dialog onDismiss");
        try {
            if(fromWhich.length()>0) {
                if (fromWhich.equals(CGlobalVariables.CALL_CLICK)) {
                    if (activity instanceof DashBoardActivity) {
                        ((DashBoardActivity) activity).RefreshHomeFragment(fromWhich);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    @Override
//    public void show(FragmentManager manager, String tag) {
//        try {
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.add(this, tag);
//            ft.commit();
//        } catch (IllegalStateException e) {
//            if(activity!=null){
//                Toast.makeText(activity, title+" "+message, Toast.LENGTH_LONG).show();
//            }
//
//            Log.d("ABSDIALOGFRAG", "Exception", e);
//        }
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cross_btn:
                dismiss();
                break;

            case R.id.ok_button:
                dismiss();
                break;
        }
    }
}
