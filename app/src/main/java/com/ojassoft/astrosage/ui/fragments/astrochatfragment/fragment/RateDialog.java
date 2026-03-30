package com.ojassoft.astrosage.ui.fragments.astrochatfragment.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 20/6/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RateDialog extends DialogFragment {
    Button notNow,yesILike;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_app_rate_frag_show, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        notNow = (Button) view.findViewById(R.id.rateNotNow);
        yesILike= (Button) view.findViewById(R.id.rateYes);

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateDialog.this.dismiss();
            }
        });

        yesILike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.rateAppication(getActivity(), true);
                CUtils.saveBooleanData(getActivity(), CGlobalVariables.prefs_key_for_five_star,true);
                RateDialog.this.dismiss();
            }
        });
        return view;
    }
}
