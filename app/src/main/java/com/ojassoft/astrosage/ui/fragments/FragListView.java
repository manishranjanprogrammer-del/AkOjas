package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.customadapters.CustomPagerAdapterForAdds;
import com.ojassoft.astrosage.misc.AstroShopAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActAstroShop;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.customcontrols.HeightWrappingViewPager;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
//import com.viewpagerindicator.CirclePageIndicator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ojas on २६/२/१६.
 */
public class FragListView extends Fragment {
    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<AstroShopItemDetails> arrayListdata = new ArrayList<AstroShopItemDetails>();
    View view;


    // add for showimg custom adds
    private HeightWrappingViewPager mViewPageradd;
    // private CirclePageIndicator indicator;
    private String cacheResult;
    private static int NUM_PAGES = 0;
    private CustomPagerAdapterForAdds mPagerAdapter;
    private static int currentPage = 0;
    private List<CustomAddModel> customaddmodel;
    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    CustomProgressDialog pd = null;
    Activity act;
    ArrayList<CustomAddModel> sliderList;
    RelativeLayout frameLay;
    private String IsShowBanner = "False";
    private int fragPos;
    private static Handler handler;
    private static Runnable Update;
    private static Timer swipeTimer;
    private TabLayout tab_layout;

    ArrayList<String> categoryFullName, categoryShortName;
    JSONArray jsonArrayProductCategoryName;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        act = activity;
        super.onAttach(act);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String msg = getArguments().getString("msg");
        fragPos = getArguments().getInt("fragPos");

        try {
            /*arrayListdata = new Gson().fromJson(msg, new TypeToken<ArrayList<AstroShopItemDetails>>() {
            }.getType());*/
            //String result = CUtils.getStringData(getActivity(), "CUSTOMADDSFORPRODUCT", "");
            String result = ((ActAstroShop) act).customAdsStr;
            if (result != null && !result.equals("")) {
                sliderList = ((ActAstroShop) act).parseData(result);
                IsShowBanner = ((ActAstroShop) act).getVisibility();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // if (view == null) {
        view = inflater.inflate(R.layout.lay_astroshop_gemstone, container, false);
       /* } else {
            if (((ViewGroup) view.getParent()) != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

        }*/
        LANGUAGE_CODE = ((AstrosageKundliApplication) act.getApplicationContext())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                act, LANGUAGE_CODE, CGlobalVariables.regular);

        mViewPageradd = (HeightWrappingViewPager) view.findViewById(R.id.viewpageradd);
        //  mViewPageradd.setOffscreenPageLimit(3);
        frameLay = (RelativeLayout) view.findViewById(R.id.frameLay);
        // indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                    mViewPageradd.setCurrentItem(currentPage, true);
                } else {
                    mViewPageradd.setCurrentItem(currentPage++, true);

                }
            }
        };
        if (swipeTimer != null) {
            swipeTimer.cancel();
        }
        swipeTimer = new Timer();

        if (fragPos == 0 && sliderList != null && sliderList.size() > 0) {
            if (sliderList.size() <= 1) {
                // indicator.setVisibility(View.GONE);
                tab_layout.setVisibility(View.GONE);
            } else {
                swipeTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 5000, 5000);
            }
            setCustomAdd(sliderList);
        }


        /*indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //    mPagerAdapter.notifyDataSetChanged();
                currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        /*if (act != null && ((ActAstroShop) act).sliderList != null && ((ActAstroShop) act).sliderList.size() > 0) {
            setAdd(((ActAstroShop) act).sliderList);
        }*/

        String data = CUtils.getStringData(act, CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");

        arrayListdata = parseAstroShopData(data);

        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(act);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //if (arrayListdata != null) {
            mAdapter = new AstroShopAdapter(act, arrayListdata);
            mRecyclerView.setAdapter(mAdapter);
        //}


        return view;
    }

    private List<AstroShopItemDetails> parseAstroShopData(String mainData) {
        categoryFullName = new ArrayList<>();
        categoryShortName = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(mainData);
            JSONObject productCategoryNames = jsonArray.getJSONObject(0);
            jsonArrayProductCategoryName = productCategoryNames.optJSONArray("ProductsCategoryName");
            for (int i = 0; i < jsonArrayProductCategoryName.length(); i++) {
                categoryFullName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName"));
                categoryShortName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryShortName"));
            }
            Log.d("tooLargeCrash", "parseAstroShopData categoryFullName: "+ Arrays.toString(categoryFullName.toArray()));; //for testing
            return new Gson().fromJson(getItemsInDetail(categoryFullName.get(fragPos),mainData), new TypeToken<ArrayList<AstroShopItemDetails>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<AstroShopItemDetails> allData = new ArrayList<>();

    private String getItemsInDetail(String title, String mainData) {
        allData.clear();
        try {
            JSONArray jsonArray = new JSONArray(mainData);
            JSONObject innerObject;
            JSONArray arrayOfItems;
            for (int i = 1; i <= jsonArray.length(); i++) {
                innerObject = jsonArray.getJSONObject(i);
                Iterator<String> iter = innerObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    if (key.equals(title)) {
                        arrayOfItems = innerObject.getJSONArray(title);
                        allData.addAll(new Gson().fromJson(arrayOfItems.toString(), new TypeToken<ArrayList<AstroShopItemDetails>>() {
                        }.getType()));
                        //Log.e("All length", "" + allData.size());
                        return arrayOfItems.toString();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FragListView newInstance(JSONArray text, int pos) {

        //Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        FragListView f = new FragListView();
        Bundle b = new Bundle();
        b.putString("msg", text.toString());
        b.putInt("fragPos", pos);
        f.setArguments(b);
        return f;
    }


    public void setCustomAdd(ArrayList<CustomAddModel> addList) {

        if (fragPos == 0 && mViewPageradd != null) {
            mPagerAdapter = new CustomPagerAdapterForAdds(getChildFragmentManager(), (addList), "ActAstroShop");
            mViewPageradd.setAdapter(mPagerAdapter);

            if (frameLay != null) {
                if (IsShowBanner.equalsIgnoreCase("False")) {
                    frameLay.setVisibility(View.GONE);
                } else {
                    frameLay.setVisibility(View.VISIBLE);
                }
            }

            NUM_PAGES = addList.size();
            mViewPageradd.setCurrentItem(0);
/*            indicator.setViewPager(mViewPageradd);
            indicator.setRadius(10);
            indicator.setStrokeWidth(3);*/

            mViewPageradd.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

	                   /* if(act != null){
	                        for (int i = 0; i < dotsCount; i++) {
	                            dots[i].setImageDrawable(act.getResources().getDrawable(R.drawable.nonselecteditem_dot));
	                        }

	                        dots[position].setImageDrawable(act.getResources().getDrawable(R.drawable.selecteditem_dot));
	                    }*/
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            // setUiPageViewController();
            tab_layout.setupWithViewPager(mViewPageradd);
        }

        /*} else {
            mViewPageradd.getAdapter().notifyDataSetChanged();
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();

        /*try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/
        act = null;
    }


}
