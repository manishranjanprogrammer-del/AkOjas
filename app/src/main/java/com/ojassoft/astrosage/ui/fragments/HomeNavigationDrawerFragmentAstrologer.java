package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.HomeNavigationAstrologerAdapter;
import com.ojassoft.astrosage.model.AllAstrologerInfo;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.util.List;

/**
 * Created by ojas-20 on 12/3/18.
 */

public class HomeNavigationDrawerFragmentAstrologer extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private List<AllAstrologerInfo> menuItems;
    private HomeNavigationAstrologerAdapter navigationAdapter;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    public boolean isDrawerOpen;
    private ProgressBar progressbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_act_all_astrologers, container, false);

        setLayRef(view);

        return recyclerView;
    }

    private void setLayRef(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        progressbar = (ProgressBar)view.findViewById(R.id.progressbar);
    }

    private void setHomeNavigationAdapter() {
        navigationAdapter = new HomeNavigationAstrologerAdapter(context, ((BaseInputActivity) context).regularTypeface,menuItems);
        recyclerView.setAdapter(navigationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolBar,List<AllAstrologerInfo> menuItems,boolean isProgressBarNeedToShow) {
        this.menuItems = menuItems;
        setHomeNavigationAdapter();
        //containerView = ((Activity)context).findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        containerView = ((Activity)context).findViewById(fragmentId);

        mDrawerToggle = new ActionBarDrawerToggle((Activity) context, mDrawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpen = false;
                //getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (Build.VERSION.SDK_INT > 11) {
                    if (slideOffset < 0.6)
                        toolBar.setAlpha(1 - slideOffset);
                }
            }
        };

        setProgressBarVisibility(isProgressBarNeedToShow);

       // mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void setProgressBarVisibility(boolean isProgressBarNeedToShow){
        if(progressbar != null) {
            if (isProgressBarNeedToShow) {
                progressbar.setVisibility(View.VISIBLE);
            } else {
                progressbar.setVisibility(View.GONE);
            }
        }
    }

    public void updateResult(List<AllAstrologerInfo> menuItems,boolean isProgressBarNeedToShow){
        setProgressBarVisibility(isProgressBarNeedToShow);
        this.menuItems = menuItems;
        navigationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public void openDrawer(){
        if(mDrawerLayout != null)
        mDrawerLayout.openDrawer(containerView);
    }

    public void closeDrawer(){
        if(mDrawerLayout != null)
            mDrawerLayout.closeDrawer(containerView);
    }
}
