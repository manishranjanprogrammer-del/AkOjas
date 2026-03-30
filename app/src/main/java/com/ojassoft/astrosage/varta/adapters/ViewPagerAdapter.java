package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragList;
    Activity activity;

    public ViewPagerAdapter(Activity activity, FragmentManager fm, ArrayList<Fragment> fragList) {
        super(fm);
        this.fragList = fragList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    public View getTabView(String text) {

        final View view = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
        TextView tv = view.findViewById(R.id.tabtext);
        tv.setText(text);
        tv.setTextColor(activity.getResources().getColor(R.color.black));
        FontUtils.changeFont(activity, tv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        return view;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        String[] titleArr = activity.getResources().getStringArray(R.array.consultation_history_title);
//        if (position == 0) {
//            title = titleArr[0];
//        } else if (position == 1) {
//            title = titleArr[1];
//        } else if (position == 2) {
//            title = titleArr[2];
//        } else if (position == 3) {
//            title = titleArr[3];
//        } else if (position == 4) {
//            title = titleArr[4];
//        } else if (position == 5) {
//            title = titleArr[5];
//        }
        return titleArr[position];
    }
}
