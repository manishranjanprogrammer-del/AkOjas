package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.graphics.drawable.DrawableCompat;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on ३/११/१७.
 */

public class AstrologerInfoCancellationFrag extends AstroSageParentDialogFragment {
    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        mediumTypeface = ((BaseInputActivity) activity).mediumTypeface;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.astrologer_info_cancel_frag_lay, container);//lay_app_rate_frag
        TextView descTextView = (TextView) view.findViewById(R.id.desc_text);
        descTextView.setTypeface(regularTypeface);
        Button okButton = (Button) view.findViewById(R.id.ok_btn);
        if(((BaseInputActivity)activity).LANGUAGE_CODE== CGlobalVariables.HINDI){
            okButton.setText(getResources().getString(R.string.hindi_ok));
        }else{
            okButton.setText(getResources().getString(R.string.ok));
        }
        okButton.setTypeface(mediumTypeface);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ((DialogFragment) getParentFragment()).dismiss();
            }
        });

        return view;

    }


    public static AppRateFrag getInstance() {
        AppRateFrag appRateFrag = new AppRateFrag();
        return appRateFrag;
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        final float scale = metrics.density;
        int margin = (int) (50 * scale);
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = (int) width - margin;
            wmlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.hideMyKeyboard(activity);
    }
}
