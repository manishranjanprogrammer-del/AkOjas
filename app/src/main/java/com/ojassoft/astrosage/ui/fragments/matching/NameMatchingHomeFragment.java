package com.ojassoft.astrosage.ui.fragments.matching;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.NameMatchingAdapter;
import com.ojassoft.astrosage.customadapters.NameMatchingDetailAdapter;
import com.ojassoft.astrosage.customadapters.NameSwarAdapter;
import com.ojassoft.astrosage.model.NameGunaMilanModel;
import com.ojassoft.astrosage.model.NameHoroscopeMatchingModel;
import com.ojassoft.astrosage.model.NameMatchingDetailModel;
import com.ojassoft.astrosage.model.NameSwarCombModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.NameMatchingOutputActivity;
import com.ojassoft.astrosage.ui.customcontrols.ClickSpan;
import com.ojassoft.astrosage.utils.CGlobalVariables;

import java.util.ArrayList;
import java.util.List;

public class NameMatchingHomeFragment extends Fragment {

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Activity currentActivity;
    Context context;
    private View view;

    private LinearLayout resultLayout;
    private TextView noResultFoundTV;
    private TextView swar1TV;
    private TextView matchingResultTV;
    private TextView lblAshtakootMatchingTV;
    private TextView valAshtakootMatchingTV;
    private TextView lblConclusionTV;
    private TextView valConclusionTV;
    private TextView lblBoyTV;
    private TextView lblGirlTV;
    private TextView lblBoyNakshatraTV;
    private TextView lblBoyCharanTV;
    private TextView lblBoyRashiTV;
    private TextView lblGirlNakshatraTV;
    private TextView lblGirlCharanTV;
    private TextView lblGirlRashiTV;
    private TextView valBoyNakshatraTV;
    private TextView valBoyCharanTV;
    private TextView valBoyRashiTV;
    private TextView valGirlNakshatraTV;
    private TextView valGirlCharanTV;
    private TextView valGirlRashiTV;
    private TextView lblGunaMilanTV;
    private TextView lblGunBoyTV;
    private TextView lblGunTV;
    private TextView lblGunGirlTV;
    private TextView lblGunnaTV;
    private TextView lblMaxOptdTV;
    private TextView lblOptdpointTV;
    private TextView lblAreaLifeTV;
    private RecyclerView recyclerViewGunMatching;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerViewGunDetail;
    private RecyclerView swarRecyclerView;
    private LinearLayout greenLL;
    private ImageView goodIV;


    private NameHoroscopeMatchingModel nameHoroscopeMatchingModel;
    private NameMatchingAdapter nameMatchingAdapter;
    private NameMatchingDetailAdapter nameMatchingDetailAdapter;
    private List<NameGunaMilanModel> milanModelList;

    private String boyName;
    private String girlName;
    private String boySwar;
    private String girlswar;

    public NameMatchingHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_name_matching_home, container, false);

        initViews();
        initListner();
        return view;
    }

    private void initViews() {
        if (currentActivity == null) return;

        Bundle bundle = getArguments();
        if (bundle != null) {
            nameHoroscopeMatchingModel = bundle.getParcelable("nameHoroscopeMatchingModel");
        }

        milanModelList = new ArrayList<>();
        LANGUAGE_CODE = ((AstrosageKundliApplication) currentActivity.getApplication()).getLanguageCode();

        resultLayout = view.findViewById(R.id.resultLayout);
        noResultFoundTV = view.findViewById(R.id.noResultFoundTV);
        swar1TV = view.findViewById(R.id.swar1TV);

        matchingResultTV = view.findViewById(R.id.matchingResultTV);
        lblAshtakootMatchingTV = view.findViewById(R.id.lblAshtakootMatchingTV);
        valAshtakootMatchingTV = view.findViewById(R.id.valAshtakootMatchingTV);
        lblConclusionTV = view.findViewById(R.id.lblConclusionTV);
        valConclusionTV = view.findViewById(R.id.valConclusionTV);
        lblBoyTV = view.findViewById(R.id.lblBoyTV);
        lblGirlTV = view.findViewById(R.id.lblGirlTV);
        lblBoyNakshatraTV = view.findViewById(R.id.lblBoyNakshatraTV);
        lblBoyCharanTV = view.findViewById(R.id.lblBoyCharanTV);
        lblBoyRashiTV = view.findViewById(R.id.lblBoyRashiTV);
        lblGirlNakshatraTV = view.findViewById(R.id.lblGirlNakshatraTV);
        lblGirlCharanTV = view.findViewById(R.id.lblGirlCharanTV);
        lblGirlRashiTV = view.findViewById(R.id.lblGirlRashiTV);
        valBoyNakshatraTV = view.findViewById(R.id.valBoyNakshatraTV);
        valBoyCharanTV = view.findViewById(R.id.valBoyCharanTV);
        valBoyRashiTV = view.findViewById(R.id.valBoyRashiTV);
        valGirlNakshatraTV = view.findViewById(R.id.valGirlNakshatraTV);
        valGirlCharanTV = view.findViewById(R.id.valGirlCharanTV);
        valGirlRashiTV = view.findViewById(R.id.valGirlRashiTV);
        lblGunaMilanTV = view.findViewById(R.id.lblGunaMilanTV);
        lblGunBoyTV = view.findViewById(R.id.lblGunBoyTV);
        lblGunTV = view.findViewById(R.id.lblGunTV);
        lblGunGirlTV = view.findViewById(R.id.lblGunGirlTV);
        lblGunnaTV = view.findViewById(R.id.lblGunnaTV);
        lblMaxOptdTV = view.findViewById(R.id.lblMaxOptdTV);
        lblOptdpointTV = view.findViewById(R.id.lblOptdpointTV);
        lblAreaLifeTV = view.findViewById(R.id.lblAreaLifeTV);
        recyclerViewGunMatching = view.findViewById(R.id.recyclerViewGunMatching);
        recyclerViewGunDetail = view.findViewById(R.id.recyclerViewGunDetail);
        swarRecyclerView = view.findViewById(R.id.swarRecyclerView);
        greenLL = view.findViewById(R.id.green_ll);
        goodIV = view.findViewById(R.id.conclusionIV);

        nameMatchingAdapter = new NameMatchingAdapter(context, milanModelList);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerViewGunMatching.setLayoutManager(mLayoutManager);
        recyclerViewGunMatching.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGunMatching.setAdapter(nameMatchingAdapter);
        recyclerViewGunMatching.setNestedScrollingEnabled(false);

        nameMatchingDetailAdapter = new NameMatchingDetailAdapter(context, milanModelList);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerViewGunDetail.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewGunDetail.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGunDetail.setAdapter(nameMatchingDetailAdapter);
        recyclerViewGunDetail.setNestedScrollingEnabled(false);
        setTypeface();
        setDataOnViews();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setDataOnViews() {
        if (currentActivity == null) return;
        if (currentActivity instanceof NameMatchingOutputActivity) {
            boyName = ((NameMatchingOutputActivity) currentActivity).boyName;
            girlName = ((NameMatchingOutputActivity) currentActivity).girlName;
            boySwar = ((NameMatchingOutputActivity) currentActivity).boySwar;
            girlswar = ((NameMatchingOutputActivity) currentActivity).girlswar;
        }

        String matchingResultStr = getString(R.string.text_matching_result);
        String boyGirlName = getString(R.string.boy_girl_name);
        boyGirlName = boyGirlName.replace("#boy", boyName);
        boyGirlName = boyGirlName.replace("#girl", girlName);
        matchingResultStr = matchingResultStr.replace("#", boyGirlName);
        SpannableStringBuilder sb = new SpannableStringBuilder(matchingResultStr);
        int color = getResources().getColor(R.color.ColorPrimaryDark);
        ForegroundColorSpan fcs = new ForegroundColorSpan(color);
        int firstIndex = matchingResultStr.indexOf(boyGirlName);
        int length = firstIndex + boyGirlName.length();
        sb.setSpan(fcs, firstIndex, length, 0);
        matchingResultTV.setText(sb);

        if (!TextUtils.isEmpty(boySwar) && !TextUtils.isEmpty(girlswar)) {
            String swarTextAftermatch = getString(R.string.text_swar_after_match);
            swarTextAftermatch = swarTextAftermatch.replace("#boy", boySwar);
            swarTextAftermatch = swarTextAftermatch.replace("#girl", girlswar);
            matchingResultTV.setText(swarTextAftermatch);
        }
        setMatchingValue();
    }


    private void initListner() {

    }

    private void setTypeface() {
        BaseInputActivity baseInputActivity = ((BaseInputActivity) currentActivity);
        matchingResultTV.setTypeface(baseInputActivity.mediumTypeface);
        lblAshtakootMatchingTV.setTypeface(baseInputActivity.mediumTypeface);
        valAshtakootMatchingTV.setTypeface(baseInputActivity.mediumTypeface, Typeface.BOLD);
        lblConclusionTV.setTypeface(baseInputActivity.mediumTypeface);
        valConclusionTV.setTypeface(baseInputActivity.mediumTypeface);
        lblBoyTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGirlTV.setTypeface(baseInputActivity.mediumTypeface);
        valBoyNakshatraTV.setTypeface(baseInputActivity.mediumTypeface);
        valBoyCharanTV.setTypeface(baseInputActivity.mediumTypeface);
        valBoyRashiTV.setTypeface(baseInputActivity.mediumTypeface);
        valGirlNakshatraTV.setTypeface(baseInputActivity.mediumTypeface);
        valGirlCharanTV.setTypeface(baseInputActivity.mediumTypeface);
        valGirlRashiTV.setTypeface(baseInputActivity.mediumTypeface);
        lblBoyNakshatraTV.setTypeface(baseInputActivity.mediumTypeface);
        lblBoyCharanTV.setTypeface(baseInputActivity.mediumTypeface);
        lblBoyRashiTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGirlNakshatraTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGirlCharanTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGirlRashiTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGunaMilanTV.setTypeface(baseInputActivity.mediumTypeface);
        noResultFoundTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGunBoyTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGunTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGunGirlTV.setTypeface(baseInputActivity.mediumTypeface);
        lblGunnaTV.setTypeface(baseInputActivity.mediumTypeface);
        lblMaxOptdTV.setTypeface(baseInputActivity.mediumTypeface);
        lblOptdpointTV.setTypeface(baseInputActivity.mediumTypeface);
        lblAreaLifeTV.setTypeface(baseInputActivity.mediumTypeface);
    }

    private void setMatchingValue() {
        if (nameHoroscopeMatchingModel == null) return;
        valConclusionTV.setText(nameHoroscopeMatchingModel.getConclusion());
        if (TextUtils.isEmpty(nameHoroscopeMatchingModel.getMatchingResult())) {
            nameHoroscopeMatchingModel.setMatchingResult("0");
        }
        valAshtakootMatchingTV.setText(nameHoroscopeMatchingModel.getMatchingResult() + "/36");
        if (Double.parseDouble(nameHoroscopeMatchingModel.getMatchingResult()) < 18) {
            setNotPerferableValues();
        }
        NameMatchingDetailModel boysDetailModel = nameHoroscopeMatchingModel.getBoysDetails();
        NameMatchingDetailModel girlDetailModel = nameHoroscopeMatchingModel.getGirlDetails();
        if (boysDetailModel != null) {
            valBoyNakshatraTV.setText(boysDetailModel.getNakshatra());
            valBoyCharanTV.setText(boysDetailModel.getCharan());
            valBoyRashiTV.setText(boysDetailModel.getRashi());
        }
        if (girlDetailModel != null) {
            valGirlNakshatraTV.setText(girlDetailModel.getNakshatra());
            valGirlCharanTV.setText(girlDetailModel.getCharan());
            valGirlRashiTV.setText(girlDetailModel.getRashi());
        }
        milanModelList.addAll(nameHoroscopeMatchingModel.getGunaMilanModels());
        nameMatchingAdapter.notifyDataSetChanged();
        nameMatchingDetailAdapter.notifyDataSetChanged();
        if (TextUtils.isEmpty(boySwar) || TextUtils.isEmpty(girlswar)) {
            handleBoyANdGirlSwar();
        }
    }

    private void handleBoyANdGirlSwar() {

        String boySwar = nameHoroscopeMatchingModel.getBoySwar();
        String girlSwar = nameHoroscopeMatchingModel.getGirlSwar();
        if (TextUtils.isEmpty(boySwar) || TextUtils.isEmpty(girlSwar)) {
            return;
        }

        String swar1 = getString(R.string.text_sawar1);
        swar1 = swar1.replace("#boy", boySwar);
        swar1 = swar1.replace("#girl", girlSwar);

        String clickHereText = getResources().getString(R.string.text_click_here);
        swar1TV.setText(swar1 + " " + clickHereText);
        swar1TV.setLinkTextColor(ContextCompat.getColor(context, R.color.primary_orange));
        clickify(swar1TV, clickHereText, new ClickSpan.OnClickListener() {
            @Override
            public void onClick() {
                if (swarRecyclerView.getVisibility() == View.VISIBLE) {
                    swarRecyclerView.setVisibility(View.GONE);
                } else {
                    swarRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        List<NameSwarCombModel> nameSwarCombModels = nameHoroscopeMatchingModel.getOtherSwarComb();
        if (nameSwarCombModels != null && !nameSwarCombModels.isEmpty()) {
            swarRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            NameSwarAdapter adapter = new NameSwarAdapter(context, nameSwarCombModels);
            swarRecyclerView.setAdapter(adapter);
            swar1TV.setVisibility(View.VISIBLE);
        } else {
            swar1TV.setVisibility(View.GONE);
        }
    }

    private void clickify(TextView view, final String clickableText,
                          final ClickSpan.OnClickListener listener) {

        CharSequence text = view.getText();
        String string = text.toString();
        ClickSpan span = new ClickSpan(listener);

        int start = string.indexOf(clickableText);
        int end = start + clickableText.length();
        if (start == -1) return;

        if (text instanceof Spannable) {
            ((Spannable) text).setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            SpannableString s = SpannableString.valueOf(text);
            s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.setText(s);
        }

        MovementMethod m = view.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void setNotPerferableValues() {
        greenLL.setBackgroundColor(getResources().getColor(R.color.red1));
        goodIV.setImageResource(R.drawable.hand_red);
        valConclusionTV.setTextColor(getResources().getColor(R.color.red1));
        valAshtakootMatchingTV.setTextColor(getResources().getColor(R.color.red1));
    }

}
