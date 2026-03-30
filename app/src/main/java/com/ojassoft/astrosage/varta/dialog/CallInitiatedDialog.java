package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;


public class CallInitiatedDialog extends DialogFragment implements View.OnClickListener {

    Activity activity;
    TextView thankuTxt, getCallShortlyTxt,
            callNotRecieveMsgTxt, receiveCallFromTxt, astroPhoneNumber, numberSwitchedOnMsgTxt;
    RelativeLayout mainlayout;
    Button freeMinuteButton, okButton;
    String callsId = "", talkTime = "", exophoneNo="";
    String fromWhich="";
    String internationalCharges="";
    ImageView crossBtn;
    TextView tvInternationalCallNote;
    String callingAstroName,callingAstroImage;
    CircularNetworkImageView astroCircularNetworkImageView;
   /* public CallInitiatedDialog(String callsId, String talkTime, String exophoneNo)
    {
        this.callsId = callsId;
        this.talkTime = talkTime;
        this.exophoneNo = exophoneNo;
    }
*/
    public CallInitiatedDialog(String callsId, String talkTime, String exophoneNo, String fromWhich, String internationalCharges,String callingAstroName)
    {
        this.callsId = callsId;
        this.talkTime = talkTime;
        this.exophoneNo = exophoneNo;
        this.fromWhich = fromWhich;
        this.internationalCharges = internationalCharges;
        this.callingAstroName = callingAstroName;
    }
    public CallInitiatedDialog(String callsId, String talkTime, String exophoneNo, String fromWhich, String internationalCharges,String callingAstroName,String profileUrl)
    {
        this.callsId = callsId;
        this.talkTime = talkTime;
        this.exophoneNo = exophoneNo;
        this.fromWhich = fromWhich;
        this.internationalCharges = internationalCharges;
        this.callingAstroName = callingAstroName;
        this.callingAstroImage = CGlobalVariables.IMAGE_DOMAIN +  profileUrl;
    }

    public CallInitiatedDialog(){ }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.call_initiated_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(false);
        thankuTxt = view.findViewById(R.id.thanku_txt);
        getCallShortlyTxt = view.findViewById(R.id.get_call_shortly_txt);
        callNotRecieveMsgTxt = view.findViewById(R.id.call_not_recieve_msg_txt);
        freeMinuteButton = view.findViewById(R.id.free_minute_button);
        astroPhoneNumber = view.findViewById(R.id.astro_phone_number);
        TextView astrologerName = view.findViewById(R.id.astrologerName);
        astroCircularNetworkImageView = view.findViewById(R.id.asto_profile_img);
        receiveCallFromTxt = view.findViewById(R.id.receive_call_from_txt);
        numberSwitchedOnMsgTxt = view.findViewById(R.id.number_switched_on_msg_txt);
        crossBtn = view.findViewById(R.id.cross_btn);
        tvInternationalCallNote = view.findViewById(R.id.tvInternationalCallNote);

        okButton = view.findViewById(R.id.ok_button);
        mainlayout = view.findViewById(R.id.mainlayout);

        FontUtils.changeFont(getActivity(), thankuTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), receiveCallFromTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), getCallShortlyTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), callNotRecieveMsgTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), tvInternationalCallNote, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), numberSwitchedOnMsgTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), astrologerName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), freeMinuteButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(getActivity(), astroPhoneNumber, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(getActivity(), okButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        String astroName = activity.getResources().getString(R.string.connecting_with);
        callingAstroName = "<html><body><font color=\"#ff6f00\"><b>"+callingAstroName+"</b></font></body></html>";
        astroName = astroName.replace("#", callingAstroName);
        astrologerName.setText(Html.fromHtml(astroName));
        if ( callingAstroImage != null && callingAstroImage.length() > 0) {
            astroCircularNetworkImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext().getApplicationContext()).load(callingAstroImage).circleCrop().placeholder(R.drawable.ic_profile_view).into(astroCircularNetworkImageView);
        }else astroCircularNetworkImageView.setVisibility(View.GONE);
        talkTime = talkTime+" "+getResources().getString(R.string.short_minute);
        astroPhoneNumber.setVisibility(View.GONE);

        if(fromWhich.equals(CGlobalVariables.CHAT_CLICK)){
            thankuTxt.setText(activity.getResources().getString(R.string.chat_initiated));
            receiveCallFromTxt.setText(activity.getResources().getString(R.string.waiting_for_astrologer_to_accept_chat));
            getCallShortlyTxt.setText(activity.getResources().getString(R.string.astrologer_doesnot_accept_request_msg));
            numberSwitchedOnMsgTxt.setVisibility(View.GONE);
            String noteMinMsg = activity.getResources().getString(R.string.call_initiated_note_msg_chat);
            noteMinMsg = noteMinMsg.replace("#", talkTime);
            callNotRecieveMsgTxt.setText(noteMinMsg);
            //okButton.setVisibility(View.GONE);
            //crossBtn.setVisibility(View.INVISIBLE);

        }else
        {
            if(exophoneNo==null || exophoneNo.equals(""))
            {
                exophoneNo = activity.getResources().getString(R.string.customer_care_phone1);
            }
            thankuTxt.setText(activity.getResources().getString(R.string.call_initiated));
            String first = activity.getResources().getString(R.string.receive_call_from_txt);
            //String next = "<b><font color='#8d88cf'>"+activity.getResources().getString(R.string.astro_phone_number)+"</font></b>";
            String two = "<b><font color='#ff6f00'>" +" "+exophoneNo+ " "+"</font></b>";
            String three = activity.getResources().getString(R.string.on_your_txt);
            String four = "<b><font color='#ff6f00'>" +" "+ CUtils.getUserID(activity) + " "+"</font></b>";
            String five = activity.getResources().getString(R.string.number_txt);

            first = first.replace("#",two);
            first = first.replace("@",four);
            receiveCallFromTxt.setText(Html.fromHtml(first));

            numberSwitchedOnMsgTxt.setVisibility(View.VISIBLE);
            String numberSwitchedOnStr = getResources().getString(R.string.call_switched_on_msg);
            numberSwitchedOnStr = numberSwitchedOnStr.replace("#", CUtils.getUserID(activity));
            numberSwitchedOnMsgTxt.setText(numberSwitchedOnStr);

            getCallShortlyTxt.setText(activity.getResources().getString(R.string.astrologer_call_shortly_msg));
            String noteMinMsg = activity.getResources().getString(R.string.call_initiated_note_msg);
            noteMinMsg = noteMinMsg.replace("#", talkTime);
            callNotRecieveMsgTxt.setText(noteMinMsg);
            //okButton.setVisibility(View.VISIBLE);
            //crossBtn.setVisibility(View.VISIBLE);
        }

        String availableMinMsg = activity.getResources().getString(R.string.available_talktime);
        availableMinMsg = availableMinMsg.replace("#", talkTime);
        freeMinuteButton.setText(availableMinMsg);

        if (internationalCharges != null && !internationalCharges.equals("0.00")){
            tvInternationalCallNote.setVisibility(View.VISIBLE);
            String tvText = activity.getResources().getString(R.string.international_call_note);
            tvInternationalCallNote.setText(tvText.replace("###",internationalCharges));
        }else {
            tvInternationalCallNote.setVisibility(View.GONE);;
        }

        freeMinuteButton.setOnClickListener(this);
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
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        //Log.e("LoadMore ", "Dialog onDismiss");
        try {
            if(fromWhich.length() > 0 && fromWhich.equals(CGlobalVariables.CALL_CLICK)) {
                if (activity instanceof DashBoardActivity) {
//                    ((DashBoardActivity) activity).popUpOverPopUp();
                    ((DashBoardActivity) activity).RefreshHomeFragment(fromWhich);
                } else if (activity instanceof AstrologerDescriptionActivity) {
                    ((AstrologerDescriptionActivity) activity).popUpOverPopUp();
                }
            }else{
              /*  if(fromWhich.length() > 0 && fromWhich.equals(CGlobalVariables.CHAT_CLICK)) {
                    ((DashBoardActivity) activity).openDialog(callsId, CGlobalVariables.WAITING_ASTROLOGER);
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                /*if(fromWhich.length() > 0 && fromWhich.equals(CGlobalVariables.CHAT_CLICK)) {
                    ((DashBoardActivity) activity).openDialog(callsId, CGlobalVariables.WAITING_ASTROLOGER);
                }*/
                dismiss();
                break;

            case R.id.cross_btn:
                /*if(fromWhich.length() > 0 && fromWhich.equals(CGlobalVariables.CHAT_CLICK)) {
                    ((DashBoardActivity) activity).openDialog(callsId, CGlobalVariables.WAITING_ASTROLOGER);
                }*/
                dismiss();
                break;
        }
    }
}
