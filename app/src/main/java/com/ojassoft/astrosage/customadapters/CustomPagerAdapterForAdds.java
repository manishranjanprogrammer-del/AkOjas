package com.ojassoft.astrosage.customadapters;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.fragments.SliderFragmentForCustomAdds;

import java.util.List;

/**
 * Created by ojas on २८/२/१७.
 */
public class CustomPagerAdapterForAdds extends FragmentStatePagerAdapter {
    List<CustomAddModel> list;
    String screen_Name;

    public CustomPagerAdapterForAdds(FragmentManager fm, List<CustomAddModel> list,String screen_Name) {
        super(fm);
        this.list=list;
        this.screen_Name=screen_Name;
    }
    @Override
    public Fragment getItem(int position) {
        SliderFragmentForCustomAdds frag=new SliderFragmentForCustomAdds();
        Bundle bundle=new Bundle();
        bundle.putSerializable("DATA",list.get(position));
        bundle.putString("SCREEN_NAME",screen_Name);
        frag.setArguments(bundle);
        return  frag;
    }
    @Override
    public int getCount() {
        return list.size();
    }
}
