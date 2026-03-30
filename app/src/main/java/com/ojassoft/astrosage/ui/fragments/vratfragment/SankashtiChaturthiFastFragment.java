package com.ojassoft.astrosage.ui.fragments.vratfragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.customadapters.PurnimaFastAdapter;
import com.ojassoft.astrosage.model.AllPanchangData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.ActYearlyVrat;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

/**
 * Created by ojas-02 on 15/1/18.
 */

public class SankashtiChaturthiFastFragment extends Fragment implements OnRefreshListener {
    private Activity activity;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    private RecyclerView.Adapter mAdapter;
    TextView txtPlaceName, txtPlaceDetail;
    public static final int SUB_ACTIVITY_PLACE_SEARCH = 1001;

    private TextView tvDatePicker, tvPlace;
    //int year;
    //private BeanHoroPersonalInfo beanHoroPersonalInfo;
    //private static boolean isChecked = false;
    BeanPlace beanPlace;
    boolean isFirstTime;
    public AllPanchangData allPanchangData;
    private SwipeRefreshLayout swipeView;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    private LinearLayout llCustomAdv;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public static Fragment newInstance(String panchang) {

        SankashtiChaturthiFastFragment f = new SankashtiChaturthiFastFragment();
        Bundle b = new Bundle();
        b.putString("msg", panchang);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sankashti__fast_fargment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_sankashti_recycler_view);
        initTimePickerView(view);

        addRecyclerView();
        //isChecked = CUtils.getUsersCheckedDefaultCityForVrat(activity);

       /* beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        if (CUtils.getBeanHoroPersonalInfo(activity) != null || CUtils.getBeanPalce(activity) != null) {
            beanHoroPersonalInfo = CUtils.getBeanHoroPersonalInfo(activity);
        } else {
            beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        }*/
        beanPlace = CUtils.getBeanPalce(activity);
        if (beanPlace == null) {
            beanPlace = CUtils.getDefaultPlace();
        }
        initSwipeRefreshLayout(view);
        //this.beanPlace = CUtils.getBeanPalce(activity);
        //this.city_Id = String.valueOf(this.beanPlace.getCityId());
        LinearLayout layPlaceHolder = (LinearLayout) view.findViewById(R.id.layPlaceHolder);
        txtPlaceName = (TextView) view.findViewById(R.id.textViewPlaceName);
        txtPlaceDetail = (TextView) view.findViewById(R.id.textViewPlaceDetails);
        Button nextButton = (Button) view.findViewById(R.id.btnNextDate);
        Button previousButton = (Button) view.findViewById(R.id.btnPreviousDate);
        //beanPlace=(((ActYearlyVrat) activity).beanPlace);
        if ((((ActYearlyVrat) activity).beanPlace) != null) {
            setPlaceDataView();
        }
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);
        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((ActYearlyVrat) activity).regularTypeface, "SPCHO"));
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }
        layPlaceHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSearchPlace(beanPlace);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDatePicker.setText(((ActYearlyVrat) activity).getYearText(++((ActYearlyVrat) activity).year));

                /*set kundli date in newKundliSelectedDate*/
                if (tvDatePicker.toString().length() > 0) {
                    CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                }
                setPlaceDataView();

                ((ActYearlyVrat) activity).downloadDataWhenChangeInDate(CGlobalVariables.sankashtiVratUrl, ((ActYearlyVrat) activity).year, (((ActYearlyVrat) activity)).cityId, true);
                //downloadDataWhenChangeInDate(selectedYear, (((ActYearlyVrat) activity).beanPlace).getCityId());
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDatePicker.setText(((ActYearlyVrat) activity).getYearText(--((ActYearlyVrat) activity).year));

                /*set kundli date in newKundliSelectedDate*/
                if (tvDatePicker.toString().length() > 0) {
                    CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
                }
                setPlaceDataView();

                ((ActYearlyVrat) activity).downloadDataWhenChangeInDate(CGlobalVariables.sankashtiVratUrl, ((ActYearlyVrat) activity).year, (((ActYearlyVrat) activity)).cityId, true);
                //downloadDataWhenChangeInDate(selectedYear, (((ActYearlyVrat) activity).beanPlace).getCityId());

            }
        });

        if (allPanchangData != null) {
            upDataList(allPanchangData);
        }
        return view;
    }
    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_59_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_59_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S59");
                CustomAddModel modal = topAdData.getImageObj().get(0);
                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);
            }
        });
    }

    public void setTopAdd(AdData topData) {
        if (topData != null) {
            IsShowBanner = topData.getIsShowBanner();
            IsShowBanner = IsShowBanner == null ? "" : IsShowBanner;
        }
        if (topData == null || topData.getImageObj() == null || topData.getImageObj().size() <= 0 || IsShowBanner.equalsIgnoreCase("False")) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        } else {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.VISIBLE);
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(activity).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        }

    }

    private void getData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "59");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initSwipeRefreshLayout(View view) {
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);//
    }

    private void addRecyclerView() {


        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initTimePickerView(View view) {
        tvDatePicker = (TextView) view.findViewById(R.id.tvDatePicker);

        tvDatePicker.setText(String.valueOf(((ActYearlyVrat) activity).year));

        /*set kundli date in newKundliSelectedDate*/
        if (tvDatePicker.toString().length() > 0) {
            CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
        }

        tvDatePicker.setTypeface(typeface);
        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActYearlyVrat) activity).initMonthPicker(((ActYearlyVrat) activity).year, tvDatePicker);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);

                    (((ActYearlyVrat) activity).beanPlace) = place;
                    setPlaceDataView();
                    String cityId = "";
                    if (place != null) {
                        cityId = String.valueOf(place.getCityId());
                        ((ActYearlyVrat) activity).cityId = cityId;
                    }
                    beanPlace = place;
                    CUtils.saveBeanPalce(activity, place);
                    //isChecked = CUtils.getUsersCheckedDefaultCityForVrat(activity);
                    //downloadDataWhenChangeInDate(selectedYear,(((ActYearlyVrat) activity).beanPlace).getCityId());
                    ((ActYearlyVrat) activity).downloadDataWhenChangeInDate(CGlobalVariables.sankashtiVratUrl, ((ActYearlyVrat) activity).year, cityId, true);
                    CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                            .setPlace(place);
                    (((ActYearlyVrat) activity).vratViewPagerAdapter).notifyDataSetChanged();
                    ((ActYearlyVrat) activity).addCustomViewInTAbs();
                   // CUtils.savePlacePreference(activity, ((ActYearlyVrat) activity).beanPlace, this.beanHoroPersonalInfo);

                }
                break;
        }
    }

    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(activity, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, ((ActYearlyVrat) activity).selectedModule);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        this.startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    private void setPlaceDataView() {

        if (((ActYearlyVrat) activity).beanPlace != null && ((ActYearlyVrat) activity).beanPlace.getCountryName() != null && ((ActYearlyVrat) activity).beanPlace.getCountryName().trim().equalsIgnoreCase("Nepal")) {

            if (CUtils.getNewKundliSelectedDate() <= 1985) {
                ((ActYearlyVrat) activity).beanPlace.setTimeZoneName("GMT+5.5");
                ((ActYearlyVrat) activity).beanPlace.setTimeZone("5.5");
                ((ActYearlyVrat) activity).beanPlace.setTimeZoneValue(Float.parseFloat("5.5"));
            } else {
                ((ActYearlyVrat) activity).beanPlace.setTimeZoneName("GMT+5.75");
                ((ActYearlyVrat) activity).beanPlace.setTimeZone("5.75");
                ((ActYearlyVrat) activity).beanPlace.setTimeZoneValue(Float.parseFloat("5.75"));
            }
        }
        if (((ActYearlyVrat) activity).beanPlace != null && ((ActYearlyVrat) activity).beanPlace.getCountryName() != null && ((ActYearlyVrat) activity).beanPlace.getCountryName().trim().equalsIgnoreCase("Suriname")) {
            if (CUtils.getNewKundliSelectedDate() <= 1984) {
                if (CUtils.getNewKundliSelectedDate() == 1984) {
                    ((ActYearlyVrat) activity).beanPlace.setTimeZoneName("GMT-3.0");
                    ((ActYearlyVrat) activity).beanPlace.setTimeZone("-3.0");
                    ((ActYearlyVrat) activity).beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
                } else {
                    ((ActYearlyVrat) activity).beanPlace.setTimeZoneName("GMT-3.5");
                    ((ActYearlyVrat) activity).beanPlace.setTimeZone("-3.5");
                    ((ActYearlyVrat) activity).beanPlace.setTimeZoneValue(Float.parseFloat("-3.5"));
                }
            } else {
                ((ActYearlyVrat) activity).beanPlace.setTimeZoneName("GMT-3.0");
                ((ActYearlyVrat) activity).beanPlace.setTimeZone("-3.0");
                ((ActYearlyVrat) activity).beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
            }
        }
        if (txtPlaceName != null && ((ActYearlyVrat) activity).beanPlace != null && (((ActYearlyVrat) activity).beanPlace).getCityName() != null)
            txtPlaceName.setText((((ActYearlyVrat) activity).beanPlace).getCityName());
        if (txtPlaceDetail != null && ((ActYearlyVrat) activity).beanPlace != null)
            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString((((ActYearlyVrat) activity).beanPlace)));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (activity != null) {
            tvDatePicker.setText(String.valueOf(((ActYearlyVrat) activity).year));
            CUtils.hideMyKeyboard(activity);
        }
    }

    public void upDataList(AllPanchangData allPanchangData) {
        if (swipeView != null) {
            swipeView.setRefreshing(false);
        }
        this.allPanchangData = allPanchangData;
        if (mRecyclerView != null && allPanchangData.getVratFestivalApiData() != null && allPanchangData.getVratFestivalApiData().size() > 0) {
            FragmentManager fm = getFragmentManager();
            DetailApiModel obj = new DetailApiModel(CUtils.getLanguageCodeName(LANGUAGE_CODE), String.valueOf(ActYearlyVrat.year), ActYearlyVrat.cityId);
            ArrayList<DetailApiModel> arrayListObj = new ArrayList<DetailApiModel>();
            arrayListObj.add(obj);
            mAdapter = new PurnimaFastAdapter(activity, fm, allPanchangData.getVratFestivalApiData(), LANGUAGE_CODE, arrayListObj);

            //  mAdapter = new PurnimaFastAdapter(activity, fm, allPanchangData.getVratFestivalApiData(), LANGUAGE_CODE);
            mRecyclerView.setAdapter(mAdapter);
            if (((ActYearlyVrat) activity).beanPlace != null) {
                setPlaceDataView();
            }
        }
    }

    public void setDateText(int year) {
        ((ActYearlyVrat) activity).year = year;
        tvDatePicker.setText(String.valueOf(year));

        /*set kundli date in newKundliSelectedDate*/
        if (tvDatePicker.toString().length() > 0) {
            CUtils.setNewKundliSelectedDate(Integer.parseInt(tvDatePicker.getText().toString()));
        }

        ((ActYearlyVrat) activity).downloadDataWhenChangeInDate(CGlobalVariables.sankashtiVratUrl, ((ActYearlyVrat) activity).year, (((ActYearlyVrat) activity)).cityId, true);
        ((ActYearlyVrat) activity).vratViewPagerAdapter.notifyDataSetChanged();
        ((ActYearlyVrat) activity).addCustomViewInTAbs();

    }

    @Override
    public void onRefresh() {
        if (swipeView != null) {
            swipeView.setRefreshing(true);
        }
        ((ActYearlyVrat) activity).downloadDetails(CGlobalVariables.sankashtiVratUrl, ((ActYearlyVrat) activity).year, (((ActYearlyVrat) activity)).cityId, false);
        // downloadDetails(url, selectedYear, cityId);
    }
}
