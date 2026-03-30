package com.ojassoft.astrosage.customadapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ojassoft.astrosage.model.SliderModal;
import com.ojassoft.astrosage.ui.fragments.SliderFragment;

import java.util.List;

/**
 * Created by ojas on २१/७/१६.
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    List<SliderModal> list;

    public CustomPagerAdapter(FragmentManager fm, List<SliderModal> list) {
        super(fm);
        this.list=list;
    }
    @Override
    public Fragment getItem(int position) {
        SliderFragment frag=new SliderFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("DATA",list.get(position));
        frag.setArguments(bundle);
        return  frag;
    }
    @Override
    public int getCount() {
        return list.size();
    }
}
