package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.AstroShopMaindata;
import com.ojassoft.astrosage.misc.AstroShopAdapter;
import com.ojassoft.astrosage.ui.act.ActAstroShop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on २६/२/१६.
 */
public class FragYantras extends Fragment {
    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AstroShopMaindata data = new AstroShopMaindata();
    private List<AstroShopItemDetails> arrayListdata = new ArrayList<AstroShopItemDetails>();

    private View view;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.lay_astroshop_gemstone, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (((ActAstroShop) getActivity()).data != null && ((ActAstroShop) getActivity()).data.size() > 0) {
            data = ((ActAstroShop) getActivity()).data.get(5);

        }

        if (data != null && data.getYantras().size() > 0) {
            arrayListdata = data.getYantras();
            mAdapter = new AstroShopAdapter(getActivity(), arrayListdata);
            mRecyclerView.setAdapter(mAdapter);

        }

        return view;
    }


}
