package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.BannerLinkModel;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class HomeBannerAdapter extends PagerAdapter {

    private final Context mContext;
    int width, height, modifyHeight;
    //public int[] imgArr = {R.drawable.home_banner1, R.drawable.home_bnner2, R.drawable.home_banner3};
    private ArrayList<String> bannerArrayList;
    private ArrayList<BannerLinkModel> bannerLinkArrayList;

    public HomeBannerAdapter(Context context, ArrayList<String> bannerArrayList, ArrayList<BannerLinkModel> bannerLinkArrayList) {
        this.mContext = context;
        this.bannerArrayList = bannerArrayList;
        if (this.bannerArrayList == null) {
            this.bannerArrayList = new ArrayList<>();
        }
        this.bannerLinkArrayList = bannerLinkArrayList;
        if (this.bannerLinkArrayList == null) {
            this.bannerLinkArrayList = new ArrayList<>();
        }
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        modifyHeight = (width/16)*5;

        Log.e("BANNERR ", width + "  "+height + "  "+ modifyHeight);*/


    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_home_banner, viewGroup, false);
        NetworkImageView bannerNetworkImg = layout.findViewById(R.id.banner_networkimg);
        LinearLayout parentlayout = layout.findViewById(R.id.parentlayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) CGlobalVariables.width, (int) CGlobalVariables.modifyHeight);
        parentlayout.setLayoutParams(layoutParams);
        if (bannerArrayList != null && bannerArrayList.size() > position) {
            String imgUrl = bannerArrayList.get(position);
            bannerNetworkImg.setImageUrl(imgUrl, VolleySingleton.getInstance(mContext).getImageLoader());
        }
        initClickListner(parentlayout, position);
        viewGroup.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (bannerArrayList == null) return 0;
        return bannerArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    private void initClickListner(LinearLayout parentlayout, final int position) {
        if (parentlayout == null) return;
        parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof DashBoardActivity) {
                    try {
                        BannerLinkModel linkModel = bannerLinkArrayList.get(position);
                        String link = linkModel.getLink();
                        if(link == null) link = "";
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HOME_BANNER_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, link);
                        ((DashBoardActivity) mContext).onBannerClicked(linkModel);
                    } catch (Exception e) {
                        //
                    }
                }
            }
        });
    }

}