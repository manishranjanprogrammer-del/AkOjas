package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTimeForPanchang;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.MuhuratModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.ArrayList;
import java.util.Calendar;

import static com.ojassoft.astrosage.utils.CGlobalVariables.MUHURAT_TOTAL_MONTH_COUNT;

public class PanchangMuhuratFragment extends Fragment implements View.OnClickListener {

    private static final int MUHURAT_YEAR = 2026;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public int SELECTED_MODULE;
    ImageView whatsAppIV;
    String language;
    String whatsAppData;
    IPanchang panchang;
    LinearLayout llCustomAdv = null;
    Activity activity;
    Typeface typeface;
    private View view;

    private int currentMonth;
    private int currentYear;
    private Button btnNextDate;
    private Button btnPreviousDate;
    private TextView currentMonthTV;
    private TextView lblNoteTV;
    private TextView noteTV;

    private MuhuratFragment fragment;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;

    public static PanchangMuhuratFragment newInstance(String text) {
        PanchangMuhuratFragment panchakInputFragment = new PanchangMuhuratFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", text);
        panchakInputFragment.setArguments(bundle);
        return panchakInputFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_frag_muhurat, container, false);
        SELECTED_MODULE = CGlobalVariables.MODULE_ASTROSAGE_PANCHANG;
        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((InputPanchangActivity) activity).regularTypeface, "SPNRA"));
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode();
        language = CUtils.getLanguageKey(CUtils.getLanguageCodeFromPreference(activity));
        typeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);
        if (activity instanceof InputPanchangActivity) {
            whatsAppIV = ((InputPanchangActivity) activity).imgWhatsApp;
            if (whatsAppIV != null) {
                whatsAppIV.setVisibility(View.VISIBLE);
            }
        }
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);
        initAdClickListner();
        getData();
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }
        initViews();
        initListners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        Calendar calendar = Calendar.getInstance();
        currentMonth = (calendar.get(Calendar.MONTH));
        currentYear = (calendar.get(Calendar.YEAR));
        btnNextDate = view.findViewById(R.id.btnNext);
        btnPreviousDate = view.findViewById(R.id.btnPrevious);
        currentMonthTV = view.findViewById(R.id.currentMonthTV);
        lblNoteTV = view.findViewById(R.id.lblNoteTV);
        noteTV = view.findViewById(R.id.noteTV);
        FontUtils.changeFont(activity, currentMonthTV, AppConstants.FONT_ROBOTO_MEDIUM);
        FontUtils.changeFont(activity, lblNoteTV, AppConstants.FONT_ROBOTO_MEDIUM);
        FontUtils.changeFont(activity, noteTV, AppConstants.FONT_ROBOTO_REGULAR);
        if (currentYear == (MUHURAT_YEAR - 1)) {
       //     Log.e("dateCheck", "initView: currentMonth:"+currentMonth );
            if (currentMonth == Calendar.DECEMBER) {
                currentMonth = 1;
            }
        } else {
            currentMonth += 2;
        }
        updateView();
    }
    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_39_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_39_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S39");
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
                topAdData = CUtils.getSlotData(adList, "39");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void initListners() {
        btnPreviousDate.setOnClickListener(this);
        btnNextDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext: {
                if (currentMonth < (MUHURAT_TOTAL_MONTH_COUNT - 1)) {
                    currentMonth++;
                    updateView();
                }
                break;
            }
            case R.id.btnPrevious: {
                if (currentMonth > 0) {
                    currentMonth--;
                    updateView();
                }
                break;
            }
        }
    }

    private void updateView() {
            BeanDateTimeForPanchang beanDateTime = new BeanDateTimeForPanchang();
            beanDateTime.setDay(1);
          //  Log.e("dateCheck", "updateView: currentMonth:"+currentMonth );
          //  Log.e("dateCheck", "updateView: currentYear:"+currentYear );

            if((currentMonth - 2) == -1) {
                beanDateTime.setYear(currentYear - 1);
                beanDateTime.setMonth(11);
                beanDateTime.setCalender(11, 1, (currentYear - 1));
            } else if((currentMonth - 2) == -2) {
                beanDateTime.setYear(currentYear - 1);
                beanDateTime.setMonth(10);
                beanDateTime.setCalender(10, 1, (currentYear - 1));
            } else {
                beanDateTime.setYear(currentYear);
                beanDateTime.setMonth(currentMonth - 2);
                beanDateTime.setCalender((currentMonth - 2), Calendar.getInstance().get(Calendar.DATE), currentYear);
        }
        toSettingTitle();
        changeViewColor();
        replaceFragment();
    }

    private void toSettingTitle() {
        switch (currentMonth) {
            case 2: {
                currentMonthTV.setText(getString(R.string.tab_month_jan));
                break;
            }
            case 3: {
                currentMonthTV.setText(getString(R.string.tab_month_feb));
                break;
            }
            case 4: {
                currentMonthTV.setText(getString(R.string.tab_month_mar));
                break;
            }
            case 5: {
                currentMonthTV.setText(getString(R.string.tab_month_apr));
                break;
            }
            case 6: {
                currentMonthTV.setText(getString(R.string.tab_month_may));
                break;
            }
            case 7: {
                currentMonthTV.setText(getString(R.string.tab_month_jun));
                break;
            }
            case 8: {
                currentMonthTV.setText(getString(R.string.tab_month_july));
                break;
            }
            case 9: {
                currentMonthTV.setText(getString(R.string.tab_month_aug));
                break;
            }
            case 10: {
                currentMonthTV.setText(getString(R.string.tab_month_sep));
                break;
            }
            case 11: {
                currentMonthTV.setText(getString(R.string.tab_month_oct));
                break;
            }
            case 0:
            case 12: {
                currentMonthTV.setText(getString(R.string.tab_month_nov));
                break;
            }
            case 1:
            case 13: {
                currentMonthTV.setText(getString(R.string.tab_month_dec));
                break;
            }
        }
        if (currentMonth < 2) {
            currentMonthTV.append(" " + (MUHURAT_YEAR - 1));
        } else {
            currentMonthTV.append(" " + MUHURAT_YEAR);
        }
    }

    private void changeViewColor() {
        int position = currentMonth;
        if (position <= 0) {
            btnPreviousDate.setAlpha(0.5f);
            btnNextDate.setAlpha(1.0f);

        } else if (position >= (MUHURAT_TOTAL_MONTH_COUNT-1)) {
            btnPreviousDate.setAlpha(1.0f);
            btnNextDate.setAlpha(0.5f);
        } else {
            btnPreviousDate.setAlpha(1.0f);
            btnNextDate.setAlpha(1.0f);
        }
    }

    public void replaceFragment() {

        try {
            fragment = new MuhuratFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.KEY_MONTH, currentMonth);
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, fragment, null);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        try {
            if (activity != null && isVisibleToUser) {

                // If we are becoming invisible, then...
                if (whatsAppIV != null) {
                    whatsAppIV.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception ex) {
            //
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    public void shareContentData(String packageName) {
        if (fragment == null) return;
        whatsAppData = "";

        String enter1 = "\n";
        String enter2 = "\n\n";
        String enter3 = "\n\n\n";

        String headingImage = "🚩";
        String titleImage = "☀ ";
        String contentImage = "🔅 ";
        String heading1 = "🚩श्री गणेशाय नम:🚩 " + enter1;
        String heading = heading1;


        String date = activity.getResources().getString(R.string.muhurat) + " (" + currentMonthTV.getText().toString() + ")";

        heading = heading + "☀ " + date + enter1;
        heading = heading + "☀ New Delhi, India";

        whatsAppData = heading + enter2;

        whatsAppData = whatsAppData.concat(getDataToShare(enter1, enter2, enter3));


        CUtils.shareData(activity, whatsAppData, packageName, activity.getResources().getString(R.string.bhadra));
    }

    /**
     * get data to share
     *
     * @param singleSpace
     * @param doubleSpace
     * @param tripleSpace
     * @return
     */
    private String getDataToShare(String singleSpace, String doubleSpace, String tripleSpace) {
        String titleImage = "🔅 ";
        StringBuilder stringBuilder = new StringBuilder();

        ArrayList<MuhuratModel> muhuratModelList = fragment.muhuratModelList;

        if (muhuratModelList != null && muhuratModelList.size() > 0) {
            for (int iterator = 0; iterator < muhuratModelList.size(); iterator++) {
                MuhuratModel muhuratModel = muhuratModelList.get(iterator);
                if (muhuratModel == null) continue;
                stringBuilder.append(titleImage);

                stringBuilder.append(muhuratModel.getName() + "\n" + muhuratModel.getDateString());

                stringBuilder.append(doubleSpace);
            }
        } else {
            stringBuilder.append(("-" + giveMeSpace(5) + "-"));
        }

        return stringBuilder.toString();
    }

    private String giveMeSpace(int num) {

        String space = "";

        for (int i = 1; i <= num; i++) {
            space = space + " ";
        }
        return space;
    }
}
