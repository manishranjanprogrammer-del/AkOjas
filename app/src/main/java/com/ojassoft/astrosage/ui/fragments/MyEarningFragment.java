package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.MyEarningBean;
import com.ojassoft.astrosage.customadapters.EarningAdapter;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.util.ArrayList;

public class MyEarningFragment extends Fragment {
    RecyclerView recyclerView;
    EarningAdapter earningAdapter;
    ArrayList<MyEarningBean> myEarningBeanArrayList;
    Activity activity;
    String heading;
    TextView headingTV;
    TextView monthHeadingTV;
    TextView orderHeadingTV;
    TextView earningHeadingTV;
    boolean isProduct;


    public static MyEarningFragment newInstance(ArrayList<MyEarningBean> arrayList, String heading, boolean isProduct) {

        Bundle args = new Bundle();
        args.putSerializable("arrayList", arrayList);
        args.putString("heading", heading);
        args.putBoolean("isProduct", isProduct);
        MyEarningFragment fragment = new MyEarningFragment();
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myEarningBeanArrayList = (ArrayList<MyEarningBean>) getArguments().getSerializable("arrayList");
            heading = getArguments().getString("heading");
            isProduct = getArguments().getBoolean("isProduct");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.earning_frag_layout, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        headingTV = view.findViewById(R.id.heading_tv);
        monthHeadingTV = view.findViewById(R.id.month_heading_tv);
        orderHeadingTV = view.findViewById(R.id.order_heading_tv);
        earningHeadingTV = view.findViewById(R.id.earning_heading_tv);
        headingTV.setText(heading);
        headingTV.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        monthHeadingTV.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        orderHeadingTV.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        earningHeadingTV.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        setRVAdapter();
        return view;
    }

    private void setRVAdapter() {
        earningAdapter = new EarningAdapter(activity, myEarningBeanArrayList, isProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(earningAdapter);
    }
}
