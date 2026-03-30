package com.ojassoft.astrosage.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.AstroShopMaindata;
import com.ojassoft.astrosage.misc.AstroShopGridAdapter;
import com.ojassoft.astrosage.ui.act.ActAstroShop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on १०/३/१६.
 */
public class FragGridJadiTreeRoot extends Fragment {
    private GridView grid_view;
    private View view;
    private AstroShopMaindata data = new AstroShopMaindata();
    private List<AstroShopItemDetails> arrayListdata = new ArrayList<AstroShopItemDetails>();
    public AstroShopGridAdapter adpter=new AstroShopGridAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.lay_astroshop_gemstone, container, false);
        } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        grid_view = (GridView) view.findViewById(R.id.grid_view);
        grid_view.setVisibility(View.VISIBLE);

        data = ((ActAstroShop) getActivity()).data.get(1);
        if (data != null && data.getJadiTreeRoots().size() > 0) {

            arrayListdata = data.getJadiTreeRoots();
            adpter=new AstroShopGridAdapter(getActivity(), arrayListdata);
            grid_view.setAdapter(adpter);
        }
        return view;
    }

}
