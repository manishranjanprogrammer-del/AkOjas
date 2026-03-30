package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAstroShopServices;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

public class ShowCustomRateFragmentDialog extends AstroSageParentDialogFragment {
    private TextView txtAstrologername, txtexperince, txtexperincecontent, txtexpertise, txtexpertiseinfo, txtaboutastrologer, txtaboutastrologerdes;
    private String astroloname, focusarea, description, experience;
    int astrologerId;
    private Button btnyes, btnno;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        astroloname = getArguments().getString("astroloname");
        focusarea = getArguments().getString("focusarea");
        description = getArguments().getString("description");
        astrologerId = getArguments().getInt("astrologerId");
        experience = getArguments().getString("experience");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //    setContentView(R.layout.frag_astrologer_description);
        View view = inflater.inflate(R.layout.frag_astrologer_description, container);//lay_app_rate_frag

        btnyes = (Button) view.findViewById(R.id.btnyes);
        btnyes.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        btnno = (Button) view.findViewById(R.id.btnno);
        btnno.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("IDDDDDDDDDDDDDDDD", String.valueOf(astrologerId));
                Intent itemdescriptionIntent = new Intent(getActivity(), ActAstroShopServices.class);
                itemdescriptionIntent.putExtra("AstroId", String.valueOf(astrologerId));

                getActivity().startActivity(itemdescriptionIntent);
                dialog.dismiss();
            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtAstrologername = (TextView)
                view.findViewById(R.id.txtAstrologername);
        //   txtAstrologername.setTypeface(mediumtTypeface);
        txtAstrologername.setText(astroloname);


        txtexperince = (TextView) view.findViewById(R.id.txtexperince);
        txtexperincecontent = (TextView) view.findViewById(R.id.txtexperincecontent);
        txtexperincecontent.setText(experience);
        txtexpertise = (TextView)
                view.findViewById(R.id.txtexpertise);
        txtexpertiseinfo = (TextView) view.findViewById(R.id.txtexpertiseinfo);
        txtexpertiseinfo.setText(focusarea);
        txtaboutastrologer = (TextView) view.findViewById(R.id.txtaboutastrologer);
        txtaboutastrologerdes = (TextView) view.findViewById(R.id.txtaboutastrologerdes);
        txtaboutastrologerdes.setText(getResources().getString(R.string.about_astrologer) + " " + Html.fromHtml(description.replace("@^", "<").replace("^@", " >")));
        return view;
    }

    public static ShowCustomRateFragmentDialog getInstance(String astroloname, String focusarea, String description, int astrologerId, String experience) {
        Bundle bundle = new Bundle();
        bundle.putString("astroloname", astroloname);
        bundle.putString("focusarea", focusarea);
        bundle.putString("description", description);
        bundle.putInt("astrologerId", astrologerId);
        bundle.putString("experience", experience);
        ShowCustomRateFragmentDialog showCustomRateFragmentDialog = new ShowCustomRateFragmentDialog();
        showCustomRateFragmentDialog.setArguments(bundle);
        return showCustomRateFragmentDialog;
    }
}