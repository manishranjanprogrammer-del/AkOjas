package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.kundliCategoryAdapter;

import java.util.ArrayList;

public class KundliCategoryFrag extends Fragment {
    public ArrayList<String> categoryList;
    static RefreshList callBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBack = (RefreshList) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryList = (ArrayList<String>) getArguments().getSerializable("data");
            callBack.changeDataOnClick(0, categoryList.get(0));

        }
        //categoryList = ((ActPrintKundliCategory) callBack).categoryList;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.kundli_category_layout, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.category_list);
        kundliCategoryAdapter kundliCategoryAdapter = new kundliCategoryAdapter(callBack, categoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_item_divider));
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setAdapter(kundliCategoryAdapter);

        return root;
    }


    public static KundliCategoryFrag newInstance(ArrayList<String> catList) {
        KundliCategoryFrag kundliCategoryFrag = new KundliCategoryFrag();
        /* See this code gets executed immediately on your object construction */
        Bundle args = new Bundle();
        args.putSerializable("data", catList);
        kundliCategoryFrag.setArguments(args);
        return kundliCategoryFrag;
    }

    public interface RefreshList {
        void changeDataOnClick(int position, String title);
    }
}
