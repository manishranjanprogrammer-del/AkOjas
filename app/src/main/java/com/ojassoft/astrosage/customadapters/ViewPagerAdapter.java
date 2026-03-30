package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAstroShop;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.utils.CGlobalVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * @created by : Amit Rautela
 * @date : 23-2-2016
 * @Description : This class is used to set the fragments in view pager
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Typeface typeface;
    Activity activity;
    int LANGUAGE_CODE;

    public ViewPagerAdapter(FragmentManager manager, Activity activity) {
        super(manager);
        this.typeface = ((BaseInputActivity) activity).mediumTypeface;
        this.LANGUAGE_CODE = ((BaseInputActivity) activity).LANGUAGE_CODE;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void replaceFrag(int positon, Fragment fragment) {
        mFragmentList.set(positon, fragment);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public View getTabView(int position) {

        final View view = LayoutInflater.from(activity).inflate(R.layout.lay_input_kundli_tab_title, null);
        TextView tv = (TextView) view.findViewById(R.id.tabtext);
        ImageView tab_icon = (ImageView) view.findViewById(R.id.nav_icon);
        ImageView tab_icon2 = (ImageView) view.findViewById(R.id.nav_icon2);
        // View separater = (View) view.findViewById(R.id.view);
        if (activity instanceof ActAstroShop) {
            tv.setTypeface(((BaseInputActivity) activity).robotMediumTypeface);
        } else {
            tv.setTypeface(typeface);
        }

        if (activity instanceof DetailedHoroscope){
            if (position==0){
//                tab_icon2.setVisibility(View.VISIBLE);
//                Glide.with(tab_icon2).load(R.drawable.ic_ai_star_icon_gradient).into(tab_icon2);

            }else {
                tab_icon2.setVisibility(View.GONE);
            }
        }else {
            tab_icon2.setVisibility(View.GONE);
        }


        /*if ((activity instanceof HomeInputScreen) || (activity instanceof HomeMatchMakingInputScreen) || (activity instanceof ActLogin) || (activity instanceof ActivityLoginAndSignin) || (activity instanceof ActAstroPaymentOptions)) {
            tv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        } else {
            tv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }*/

       /* if (position == 0) {
            separater.setVisibility(View.GONE);
        } else {
            separater.setVisibility(View.VISIBLE);
        }*/

        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            tv.setText(mFragmentTitleList.get(position));//.toUpperCase());
        } else {
            tv.setText(mFragmentTitleList.get(position));
        }

        if (mFragmentTitleList.get(position).equalsIgnoreCase(activity.getString(R.string.consult))) {
            //tab_icon.setVisibility(View.VISIBLE);
            //tab_icon.setColorFilter(ContextCompat.getColor(activity, R.color.ivTintColorDN));

           // tab_icon.setImageResource(R.drawable.consult);
           // tv.setPadding(10, 0, 30, 0);
        } else {
            tab_icon.setVisibility(View.GONE);
        }


        return view;

    }

    public Fragment getFragment(int position) {
        return mFragmentList.get(position);
    }

    public void setAlpha(int position, TabLayout tabLayout) {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View view = tabLayout.getTabAt(i).getCustomView();
                    TextView textView = (TextView) view.findViewById(R.id.tabtext);
                    ImageView tab_icon = (ImageView) view.findViewById(R.id.nav_icon);
                    if (position == i) {
                        textView.setAlpha(1F);
                        tab_icon.setAlpha(1F);
                    } else {
                        textView.setAlpha(0.5F);
                        tab_icon.setAlpha(0.5F);
                    }
                }
            }
        } catch (Exception ex) {
            //Log.i("Exception",ex.getMessage());
        }
    }

}