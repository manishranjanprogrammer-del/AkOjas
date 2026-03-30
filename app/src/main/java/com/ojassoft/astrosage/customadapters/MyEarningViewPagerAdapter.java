package com.ojassoft.astrosage.customadapters;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.util.ArrayList;

public class MyEarningViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragList;
    ArrayList<String> titleList;
    Context context;

    public MyEarningViewPagerAdapter(Context context, FragmentManager fm, ArrayList<Fragment> fragList, ArrayList<String> titleList) {
        super(fm);
        this.fragList = fragList;
        this.titleList = titleList;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titleList.get(position);
    }

    public View getTabView(int position) {

        final View view = LayoutInflater.from(context).inflate(R.layout.lay_input_kundli_tab_title, null);
        TextView tv = (TextView) view.findViewById(R.id.tabtext);
        tv.setTypeface(((BaseInputActivity) context).robotMediumTypeface);
        tv.setText(titleList.get(position).toUpperCase());
        return view;

    }
}
