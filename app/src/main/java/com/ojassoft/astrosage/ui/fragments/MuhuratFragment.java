package com.ojassoft.astrosage.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.MuhuratAdapter;
import com.ojassoft.astrosage.model.MuhuratModel;

import java.util.ArrayList;

import static com.ojassoft.astrosage.constants.AppConstants.KEY_MONTH;

/**
 * A simple {@link Fragment} subclass.
 */
public class MuhuratFragment extends Fragment {

    private Context context;
    private Activity currentActivity;
    private View view;
    private RecyclerView recyclerView;
    public ArrayList<MuhuratModel> muhuratModelList;
    private MuhuratAdapter muhuratAdapter;
    private String[] vivahMuhuratArr;
    private String[] mundanMuhuratArr;
    private String[] grihaPraveshArr;
    private String[] namkaranMuhuratArr;
    private String[] karnavedhaMuhuratArr;
    private String[] vidyarambhMuhuratArr;
    private String[] annaprashanMuhuratArr;
    private String[] upnayanMuhuratArr;
    private String[] sarvarthSiddhiMuhuratArr;
    private String[] amritSiddhiMuhuratArr;
    private String[] vehicleMuhuratArr;
    private String[] propertyMuhuratArr;
    private int calendarMonth;

    private String[] panchakMuhuratArr;

    private String[] bhadraMuhuratArr;

    public MuhuratFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_muhurat, container, false);
        initContext();
        initViews();
        return view;
    }

    protected void initViews() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            calendarMonth = bundle.getInt(KEY_MONTH);
        }
        muhuratModelList = new ArrayList<>();
        vivahMuhuratArr = getResources().getStringArray(R.array.vivah_muhurat);
        mundanMuhuratArr = getResources().getStringArray(R.array.mundan_muhurat);
        grihaPraveshArr = getResources().getStringArray(R.array.griha_pravesh);
        namkaranMuhuratArr = getResources().getStringArray(R.array.namkaran_muhurat);
        karnavedhaMuhuratArr = getResources().getStringArray(R.array.karnavedha_muhurat);
        vidyarambhMuhuratArr = getResources().getStringArray(R.array.vidyarambh_muhurat);
        annaprashanMuhuratArr = getResources().getStringArray(R.array.annaprashan_muhurat);
        upnayanMuhuratArr = getResources().getStringArray(R.array.upnayan_muhurat);
        sarvarthSiddhiMuhuratArr = getResources().getStringArray(R.array.sarvart_muhurat);
        amritSiddhiMuhuratArr = getResources().getStringArray(R.array.amrit_muhurat);
        vehicleMuhuratArr = getResources().getStringArray(R.array.vehicle_purchase_muhurat);
        propertyMuhuratArr = getResources().getStringArray(R.array.property_purchase_muhurat);

        panchakMuhuratArr = getResources().getStringArray(R.array.panchak_muhurat);

        bhadraMuhuratArr = getResources().getStringArray(R.array.bhadra_muhurat);

        getMuhuratdata();
        recyclerView = view.findViewById(R.id.recyclerView);
        muhuratAdapter = new MuhuratAdapter(context, muhuratModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(muhuratAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    protected void initContext() {
        context = getActivity();
        currentActivity = getActivity();
    }

    private void getMuhuratdata() {
        try {
            muhuratModelList.clear();
            MuhuratModel muhuratModel = null;

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_vivah_muhurat));
            muhuratModel.setDateString(vivahMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_mundan_muhurat));
            muhuratModel.setDateString(mundanMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_griha_pravesh));
            muhuratModel.setDateString(grihaPraveshArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_namkaran_muhurat));
            muhuratModel.setDateString(namkaranMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_annaprashan_muhurat));
            muhuratModel.setDateString(annaprashanMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_karnavedha_muhurat));
            muhuratModel.setDateString(karnavedhaMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_vidyarambh_muhurat));
            muhuratModel.setDateString(vidyarambhMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_upnayan_muhurat));
            muhuratModel.setDateString(upnayanMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_viehcle_muhurat));
            muhuratModel.setDateString(vehicleMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_property_muhurat));
            muhuratModel.setDateString(propertyMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_sarvartha_siddhi_yoga));
            muhuratModel.setDateString(sarvarthSiddhiMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.text_amrit_siddhi_yoga));
            muhuratModel.setDateString(amritSiddhiMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.panchak));
            muhuratModel.setDateString(panchakMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);

            muhuratModel = new MuhuratModel();
            muhuratModel.setName(getString(R.string.bhadra));
            muhuratModel.setDateString(bhadraMuhuratArr[calendarMonth]);
            muhuratModelList.add(muhuratModel);
        } catch (Exception e){
            //
        }
    }

}
