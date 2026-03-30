package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.NetworkImageView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.NumerologyCalculatorOutputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NumerologyLoShuGridFragment extends Fragment {

    Activity currentActivity;
    TextView lblHeadingTV;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    GridView gridView;
    LoShuGridAdapter adapter;
    private View view;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private AdData topAdData;
    ImageView mentalPlane,emotionalPlane,practicalPlane,visionPlane,willPlane,actionPlane,rajyogGold, rajyogSilver;
    private LinearLayout llCustomAdv;
    private final List<String> defaultNumber = new ArrayList<>();
    private  Map<String,Integer> loShuNumbers;
    public NumerologyLoShuGridFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_numerology_lo_shu_grid, container, false);
        initView();
        setData();
        return view;
    }

    private void setData() {
        defaultNumber.add("4");
        defaultNumber.add("9");
        defaultNumber.add("2");
        defaultNumber.add("3");
        defaultNumber.add("5");
        defaultNumber.add("7");
        defaultNumber.add("8");
        defaultNumber.add("1");
        defaultNumber.add("6");
        String[] dob = ((NumerologyCalculatorOutputActivity) currentActivity).dob.split("-");
        String redialNumber = ((NumerologyCalculatorOutputActivity) currentActivity).numerologyOutputModel.getRadicalNumber();
        String destinyNumber = ((NumerologyCalculatorOutputActivity) currentActivity).numerologyOutputModel.getDestinyNumber();
        int day = Integer.parseInt(dob[0]);
        int month = Integer.parseInt(dob[1]);
        int year = Integer.parseInt(dob[2]);
        loShuNumbers = calculateGridNumbers(day,month,year,redialNumber,destinyNumber);
        adapter = new LoShuGridAdapter(requireContext(),defaultNumber);
        gridView.setAdapter(adapter);
        setListViewHeightBasedOnChildren();

        if(planeCheck("4","9","2")){
            mentalPlane.setImageResource(R.drawable.ic_check);
        }else {
            mentalPlane.setImageResource(R.drawable.ic_red_cross);
        }
        if (planeCheck("3", "5", "7")) {
            emotionalPlane.setImageResource(R.drawable.ic_check);
        } else {
            emotionalPlane.setImageResource(R.drawable.ic_red_cross);
        }
        if (planeCheck("8", "1", "6")) {
            practicalPlane.setImageResource(R.drawable.ic_check);
        } else {
            practicalPlane.setImageResource(R.drawable.ic_red_cross);
        }
        if (planeCheck("4", "3", "8")) {
            visionPlane.setImageResource(R.drawable.ic_check);
        } else {
            visionPlane.setImageResource(R.drawable.ic_red_cross);
        } if (planeCheck("9", "5", "1")) {
            willPlane.setImageResource(R.drawable.ic_check);
        } else {
            willPlane.setImageResource(R.drawable.ic_red_cross);
        }
        if (planeCheck("2", "7", "6")) {
            actionPlane.setImageResource(R.drawable.ic_check);
        } else {
            actionPlane.setImageResource(R.drawable.ic_red_cross);
        }
        if (planeCheck("4", "5", "6")) {
            rajyogGold.setImageResource(R.drawable.ic_check);
        } else {
            rajyogGold.setImageResource(R.drawable.ic_red_cross);
        }
        if (planeCheck("2", "5", "8")) {
            rajyogSilver.setImageResource(R.drawable.ic_check);
        } else {
            rajyogSilver.setImageResource(R.drawable.ic_red_cross);
        }


    }

    private void initView() {
        if (currentActivity == null) return;
        LANGUAGE_CODE = ((AstrosageKundliApplication) currentActivity.getApplication()).getLanguageCode();
        lblHeadingTV = view.findViewById(R.id.lblHeadingTV);
        gridView = view.findViewById(R.id.grid_view);
        mentalPlane = view.findViewById(R.id.iv_mental_plane);
        practicalPlane = view.findViewById(R.id.iv_practical_plane);
        emotionalPlane = view.findViewById(R.id.iv_emotional_plane);
        visionPlane = view.findViewById(R.id.iv_vision_plane);
        willPlane = view.findViewById(R.id.iv_will_plane);
        actionPlane = view.findViewById(R.id.iv_action_plane);
        rajyogGold = view.findViewById(R.id.iv_rajyog_golden);
        rajyogSilver = view.findViewById(R.id.iv_rajyog_silver);
        lblHeadingTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface);
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);
        initAdClickListner();
        topAdData = ((NumerologyCalculatorOutputActivity) currentActivity).getAddData("42");
        if (topAdData != null && topAdData.getImageObj() != null && !topAdData.getImageObj().isEmpty()) {
            setTopAdd(topAdData);
        }

        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(currentActivity, false, ((NumerologyCalculatorOutputActivity) currentActivity).regularTypeface, "AKNRA"));
        ((NumerologyCalculatorOutputActivity) currentActivity).addExtraSpaceInBottom(view);
    }

    private static Map<String,Integer> calculateGridNumbers(int day, int month, int year, String radialNumber, String destinyNumber) {
        String dates = ""+day+month+year+radialNumber+destinyNumber;
        String newDate = dates.replace("0","");

        Map<String,Integer> numbers = new HashMap<>();
        for (char c : newDate.toCharArray()) {

            if (numbers.containsKey(""+c)) {
                numbers.put(""+c, numbers.get(""+c) + 1);
            } else {
                numbers.put(""+c, 1);
            }
        }
        return numbers;
    }
    class LoShuGridAdapter extends ArrayAdapter<String>{
        public LoShuGridAdapter(@NonNull Context context, @NonNull List<String> objects) {
            super(context, R.layout.lo_shu_grid_item_layout, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                // Layout Inflater inflates each item to be displayed in GridView.
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.lo_shu_grid_item_layout, parent, false);
            }

            Integer number = loShuNumbers.get(getItem(position));
            TextView tvNumber = itemView.findViewById(R.id.textView_number);
            tvNumber.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface,Typeface.BOLD);
            TextView missingText = itemView.findViewById(R.id.tv_missing_txt);
            missingText.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
            ImageView crossImage = itemView.findViewById(R.id.cross_imageView);
            ConstraintLayout layout = itemView.findViewById(R.id.constraint_layout);
            switch (position){
                case 0: layout.setBackgroundResource(R.color.lavender);
                        tvNumber.setTextColor(getResources().getColor(R.color.deep_lilac));
                    break;
                case 1: layout.setBackgroundResource(R.color.watermelon);
                    tvNumber.setTextColor(getResources().getColor(R.color.pastel_red));
                    break;
                case 2: layout.setBackgroundResource(R.color.flamingo);
                        tvNumber.setTextColor(getResources().getColor(R.color.light_carmine_pink));
                    break;
                case 3: layout.setBackgroundResource(R.color.soft_green);
                    tvNumber.setTextColor(getResources().getColor(R.color.light_forest_green));
                    break;
                case 4: layout.setBackgroundResource(R.color.mustard);
                         tvNumber.setTextColor(getResources().getColor(R.color.orange_gold));
                    break;
                case 5: layout.setBackgroundResource(R.color.cloudy);
                        tvNumber.setTextColor(getResources().getColor(R.color.ironic_grey));
                    break;
                case 6: layout.setBackgroundResource(R.color.moody_blue);
                       tvNumber.setTextColor(getResources().getColor(R.color.blue_berry));
                    break;
                case 7: layout.setBackgroundResource(R.color.grey_teal);
                        tvNumber.setTextColor(getResources().getColor(R.color.greensh_blue));
                    break;
                case 8: layout.setBackgroundResource(R.color.dark_sand);
                        tvNumber.setTextColor(getResources().getColor(R.color.reef_gold));
                    break;
                default:
            }
            if(number == null){
                crossImage.setVisibility(View.VISIBLE);
                missingText.setVisibility(View.VISIBLE);
                tvNumber.setText(defaultNumber.get(position));
            }else{
                tvNumber.setText(repeatChar(defaultNumber.get(position),number));
                crossImage.setVisibility(View.GONE);
                missingText.setVisibility(View.GONE);
            }

            return itemView;
        }
    }

    private String repeatChar(String ch,int num){
        StringBuilder str = new StringBuilder();
        for(int i = 1; i<= num ; i++)
            str.append(ch);

        return str.toString();
    }




    public  void setListViewHeightBasedOnChildren() {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) return;
        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        int totalHeight = listItem.getMeasuredHeight()*3;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (5 * (listAdapter.getCount()));
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(currentActivity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_42_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_42_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(currentActivity, "S42");
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
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), com.libojassoft.android.misc.VolleySingleton.getInstance(currentActivity).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(currentActivity) != 1) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        }

    }

    public boolean planeCheck(String a,String b,String c){
        return loShuNumbers.get(a) != null && loShuNumbers.get(b) != null && loShuNumbers.get(c) != null;
    }
}