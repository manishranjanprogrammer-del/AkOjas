package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.ChalisaPlayListAdapter;
import com.ojassoft.astrosage.model.ChalisaDataModel;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class MantraAudioFrag extends Fragment {


    private Activity activity;
    private RecyclerView videoRecyclerView;
    private ChalisaPlayListAdapter chalisaPlayListAdapter;
    private ArrayList<ChalisaDataModel> chalisaDataModelArrayList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lay_mantra_video_list, container, false);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_MANTRA_CHALISA_AUDIO, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        chalisaDataModelArrayList = new ArrayList<>();
        videoRecyclerView = rootView.findViewById(R.id.video_recycler_view);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }


    private void setYoutubeAdapter() {
        if (activity == null) return;
        chalisaPlayListAdapter = new ChalisaPlayListAdapter(activity, chalisaDataModelArrayList);
        videoRecyclerView.setAdapter(chalisaPlayListAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateMantraChalisa(ArrayList<ChalisaDataModel> chalisaDataList) {
        if (chalisaDataModelArrayList != null) {
            chalisaDataModelArrayList.clear();
        } else {
            chalisaDataModelArrayList = new ArrayList<>();
        }
        chalisaDataModelArrayList.addAll(chalisaDataList);
        setYoutubeAdapter();
    }
}
