package com.ojassoft.astrosage.ui.fragments.matching;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;

import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;


public class NameHororscopeOtherFragment extends Fragment {
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Activity currentActivity;
    Context context;
    private View view;
    String interpretation;
    String title;


    public NameHororscopeOtherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
        context = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            interpretation = bundle.getString("interpretation");
            title = bundle.getString("Title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.name_horoscope_matching_frag_layout, container, false);
        TextView descTV = (TextView) view.findViewById(R.id.desc_tv);
        TextView nameTV = (TextView) view.findViewById(R.id.name_tv);
        descTV.setText(interpretation);
        nameTV.setText(title);
        nameTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface);
        descTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
