package com.ojassoft.astrosage.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

/**
 * Created by ojas-20 on 11/4/18.
 */

public class FreeQuestionDeactivateFrag extends AstroSageParentDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.lay_free_question_deactivate, container);

        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView)view.findViewById(R.id.tvContent);
        Button btnOk = (Button) view.findViewById(R.id.btnOk);

        tvTitle.setTypeface(((BaseInputActivity) getActivity()).robotMediumTypeface);
        tvContent.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);

        btnOk.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FreeQuestionDeactivateFrag.this.dismiss();
            }
        });
        return view;
    }
}
