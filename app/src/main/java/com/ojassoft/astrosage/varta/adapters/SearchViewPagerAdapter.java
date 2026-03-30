package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @created by : Amit Rautela
 * @date : 23-2-2016
 * @Description : This class is used to set the fragments in view pager
 */
public class SearchViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Typeface typeface;
    Activity activity;
    int LANGUAGE_CODE;

    public SearchViewPagerAdapter(FragmentManager manager, Activity activity) {
        super(manager);
        //this.typeface = ((BaseInputActivity) activity).mediumTypeface;
        this.LANGUAGE_CODE = 0;
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
       // View separater = (View) view.findViewById(R.id.view);
        /*if (activity instanceof ActAstroShop) {
            FontUtils.changeFont(activity, tv, CGlobalVariables.FONT_RALEWAY_BOLD);
        } else {
            FontUtils.changeFont(activity, tv, CGlobalVariables.FONT_RALEWAY_BOLD);
        }


        if ((activity instanceof HomeInputScreen) || (activity instanceof HomeMatchMakingInputScreen) || (activity instanceof ActLogin) || (activity instanceof ActivityLoginAndSignin) || (activity instanceof ActAstroPaymentOptions)) {
            tv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        } else {
            tv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        }*/

       /* if (position == 0) {
            separater.setVisibility(View.GONE);
        } else {
            separater.setVisibility(View.VISIBLE);
        }*/

            tv.setText(mFragmentTitleList.get(position));
            tv.setTextSize(16);
            tv.setTextColor(activity.getResources().getColor(R.color.black));
            FontUtils.changeFont(activity, tv, CGlobalVariables.FONTS_OPEN_SANS_BOLD);


        return view;

    }

    public Fragment getFragment(int position) {
        return mFragmentList.get(position);
    }

    public void setAlpha(int position, TabLayout tabLayout){
        try {
            /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View view = tabLayout.getTabAt(i).getCustomView();
                    TextView textView = (TextView) view.findViewById(R.id.tabtext);
                    if (position == i) {
                        textView.setAlpha(1F);
                    } else {
                        textView.setAlpha(1F);
                    }
                }
            }*/
        }catch (Exception ex){
            //Log.i("Exception",ex.getMessage());
        }
    }

}