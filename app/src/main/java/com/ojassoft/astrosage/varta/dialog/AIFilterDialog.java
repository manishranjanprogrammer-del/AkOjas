package com.ojassoft.astrosage.varta.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.adapters.FilterAstrologerAdapter;
import com.ojassoft.astrosage.varta.model.LangAndExpertiseData;
import com.ojassoft.astrosage.varta.ui.fragments.AIAstrologersFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.ArrayList;
import java.util.List;

public class AIFilterDialog extends BottomSheetDialogFragment implements FilterAstrologerAdapter.FilterClickCallbacks {

    public static final String TAG = "AIFilterDialog";
    private final int TYPE_LANGUAGE = 0;
    private final int TYPE_CATEGORY = 1;
    private final int TYPE_SKILL = 3;
    private final int TYPE_PRICE = 5;
    private BottomSheetBehavior mBehavior;

    String[] langID = {"Language", "English", "Hindi", "Tamil", "Telugu", "Kannada", "Marathi",
            "Bengali", "Gujarati", "Malayalam", "Punjabi", "Odia", "Assamese", "Urdu"};

    String[] expertiseID = {"Expertise", "Vedic", "Kp", "Lal Kitab",
            "Vastu", "Numerology", "Tarot", "Reiki", "Feng Shui", "Horary"};

    String[] orderID = {"Economic", "Premium", "Popular", "Most Rated", "Marriage"};
    String[] priceID = {"P_Economic", "P_Premium"};

    String[] consultationID = {"Call", "Chat", "Live"};
    List<String> filterList;
    List<String> expertiseFilterList;
    List<String> langFilterList;

    private static ArrayList<LangAndExpertiseData> langArrayList;
    private ArrayList<LangAndExpertiseData> skillArrayList;
    public static ArrayList<LangAndExpertiseData> categoryArrayList;

    public static ArrayList<LangAndExpertiseData> priceArrayList;


    private RecyclerView skill_recyclerview;
    private RecyclerView language_recyclerview;
    private RecyclerView category_recyclerview;
    private RecyclerView price_recyclerview;

    FilterAstrologerAdapter languageAdapter;
    FilterAstrologerAdapter skillAdapter;
    FilterAstrologerAdapter categoryAdapter;
    FilterAstrologerAdapter priceAdapter;

    private TextView tv_clear_all;
    private TextView tv_apply;

    private ImageView iv_close;

    private static AIFilterDialog obj;

    public static AIFilterDialog getInstance() {
        if (obj == null) {
            obj = new AIFilterDialog();
        }
        return obj;
    }

    public AIFilterDialog() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
        filterList = AIAstrologersFragment.filterList;
        if (filterList == null) {
            filterList = new ArrayList<>();
        }
        expertiseFilterList = AIAstrologersFragment.expertiseFilterList;
        if (expertiseFilterList == null) {
            expertiseFilterList = new ArrayList<>();
        }
        langFilterList = AIAstrologersFragment.langFilterList;
        if (langFilterList == null) {
            langFilterList = new ArrayList<>();
        }
        langArrayList = getLangData();
        skillArrayList = getExpetiseData();
        categoryArrayList = getOrderData();
        priceArrayList = getPriceData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_layout_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generatedID(view);
        registerEvent();
        setAdapter();
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void setAdapter() {

        GridLayoutManager languagelayoutManager = new GridLayoutManager(getContext(), 3);
        languageAdapter = new FilterAstrologerAdapter(getContext(), langArrayList, this, R.layout.filter_row, TYPE_LANGUAGE);
        language_recyclerview.setLayoutManager(languagelayoutManager);
        language_recyclerview.setAdapter(languageAdapter);

        GridLayoutManager skilllayoutManager = new GridLayoutManager(getContext(), 3);
        skillAdapter = new FilterAstrologerAdapter(getContext(), skillArrayList, this, R.layout.filter_row, TYPE_SKILL);
        skill_recyclerview.setLayoutManager(skilllayoutManager);
        skill_recyclerview.setAdapter(skillAdapter);

        GridLayoutManager orderlayoutManager = new GridLayoutManager(getContext(), 3);
        categoryAdapter = new FilterAstrologerAdapter(getContext(), categoryArrayList, this, R.layout.filter_row, TYPE_CATEGORY);
        category_recyclerview.setLayoutManager(orderlayoutManager);
        category_recyclerview.setAdapter(categoryAdapter);

        GridLayoutManager filterlayoutManager = new GridLayoutManager(getContext(), 3);

        GridLayoutManager pricelayoutManager = new GridLayoutManager(getContext(), 3);
        priceAdapter = new FilterAstrologerAdapter(getContext(), priceArrayList, this, R.layout.filter_row, TYPE_PRICE);
        price_recyclerview.setLayoutManager(pricelayoutManager);
        price_recyclerview.setAdapter(priceAdapter);
    }

    private void registerEvent() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tv_clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CUtils.regionalFilterType = -1;
                    CUtils.expertiseFilterType = -1;
                    filterList.clear();
                    expertiseFilterList.clear();
                    langFilterList.clear();
                    dismiss();
                    AIAstrologersFragment frag = AIAstrologersFragment.getInstance();
                    frag.clearFilter();
                } catch (Exception e) {
                    //
                }
            }
        });
        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                try {
                    CUtils.expertiseFilterType = -1;
                    CUtils.regionalFilterType = -1;
                    addFilterList();
                    AIAstrologersFragment frag = AIAstrologersFragment.getInstance();
                    frag.applyFilter(filterList, langFilterList, expertiseFilterList);
                } catch (Exception e) {
                    //
                }

            }
        });

    }

    private void generatedID(View view) {
        language_recyclerview = view.findViewById(R.id.language_recyclerview);
        skill_recyclerview = view.findViewById(R.id.skill_recyclerview);

        category_recyclerview = view.findViewById(R.id.category_recyclerview);
        price_recyclerview = view.findViewById(R.id.price_recyclerview);


        tv_clear_all = view.findViewById(R.id.tv_clear_all);
        tv_apply = view.findViewById(R.id.tv_apply);

        iv_close = view.findViewById(R.id.iv_close);
    }

    private ArrayList<LangAndExpertiseData> getOrderData() {
        final String[] itemList = getContext().getResources().getStringArray(R.array.order_spinner_data);
        ArrayList<LangAndExpertiseData> langAndExpertiseDataArrayList = new ArrayList<>();
        LangAndExpertiseData langAndExpertiseData;
        for (int i = 0; i < itemList.length; i++) {
            langAndExpertiseData = new LangAndExpertiseData();
            langAndExpertiseData.setLangName(itemList[i]);
            langAndExpertiseData.setLangVal(orderID[i]);
            if (filterList.contains(langAndExpertiseData.getLangVal())) {
                langAndExpertiseData.setLangSelected(true);
            }
            langAndExpertiseDataArrayList.add(langAndExpertiseData);
        }
        return langAndExpertiseDataArrayList;
    }

    private ArrayList<LangAndExpertiseData> getLangData() {
        final String[] itemList = getContext().getResources().getStringArray(R.array.language_spinner_data);
        ArrayList<LangAndExpertiseData> langAndExpertiseDataArrayList = new ArrayList<>();
        LangAndExpertiseData langAndExpertiseData;
        for (int i = 0; i < itemList.length; i++) {
            langAndExpertiseData = new LangAndExpertiseData();
            langAndExpertiseData.setLangName(itemList[i]);
            langAndExpertiseData.setLangVal(langID[i]);
            if (filterList.contains(langAndExpertiseData.getLangVal())) {
                langAndExpertiseData.setLangSelected(true);
            }
            langAndExpertiseDataArrayList.add(langAndExpertiseData);
        }
        langAndExpertiseDataArrayList.remove(0);
        return langAndExpertiseDataArrayList;
    }

    private ArrayList<LangAndExpertiseData> getExpetiseData() {
        final String[] itemList = getContext().getResources().getStringArray(R.array.expertise_spinner_data);
        ArrayList<LangAndExpertiseData> langAndExpertiseDataArrayList = new ArrayList<>();
        for (int i = 0; i < itemList.length; i++) {
            LangAndExpertiseData langAndExpertiseData = new LangAndExpertiseData();
            langAndExpertiseData.setLangName(itemList[i]);
            langAndExpertiseData.setLangVal(expertiseID[i]);
            if (filterList.contains(langAndExpertiseData.getLangVal())) {
                langAndExpertiseData.setLangSelected(true);
            }
            langAndExpertiseDataArrayList.add(langAndExpertiseData);
        }
        langAndExpertiseDataArrayList.remove(0);
        return langAndExpertiseDataArrayList;
    }

    private ArrayList<LangAndExpertiseData> getPriceData() {
        final String[] itemList = getContext().getResources().getStringArray(R.array.order_price_data);
        ArrayList<LangAndExpertiseData> langAndExpertiseDataArrayList = new ArrayList<>();
        LangAndExpertiseData langAndExpertiseData;
        for (int i = 0; i < itemList.length; i++) {
            langAndExpertiseData = new LangAndExpertiseData();
            langAndExpertiseData.setLangName(itemList[i]);
            langAndExpertiseData.setLangVal(priceID[i]);
            if (filterList.contains(langAndExpertiseData.getLangVal())) {
                langAndExpertiseData.setLangSelected(true);
            }
            langAndExpertiseDataArrayList.add(langAndExpertiseData);
        }
        return langAndExpertiseDataArrayList;
    }

    @Override
    public void onFilterItemClick(int position, int type) {
        if (type == TYPE_LANGUAGE) {
            langArrayList.get(position).setLangSelected(!langArrayList.get(position).isLangSelected());
            languageAdapter.notifyDataSetChanged();
        } else if (type == TYPE_SKILL) {
            skillArrayList.get(position).setLangSelected(!skillArrayList.get(position).isLangSelected());
            skillAdapter.notifyDataSetChanged();
        } else if (type == TYPE_CATEGORY) {
            for (LangAndExpertiseData item : categoryArrayList) {
                item.setLangSelected(item == categoryArrayList.get(position));
            }

            categoryAdapter.notifyDataSetChanged();
        } else if (type == TYPE_PRICE) {
            for (LangAndExpertiseData item : priceArrayList) {
                item.setLangSelected(item == priceArrayList.get(position));
            }
            priceAdapter.notifyDataSetChanged();
        }
    }

    private void addFilterList() {
        filterList.clear();
        expertiseFilterList.clear();
        langFilterList.clear();
        for (LangAndExpertiseData item : langArrayList) {
            if (item.isLangSelected()) {
                filterList.add(item.getLangVal());
                langFilterList.add(item.getLangVal());
            }
        }
        for (LangAndExpertiseData item : skillArrayList) {
            if (item.isLangSelected()) {
                filterList.add(item.getLangVal());
                expertiseFilterList.add(item.getLangVal());
            }
        }
        for (LangAndExpertiseData item : categoryArrayList) {
            if (item.isLangSelected()) {
                filterList.add(item.getLangVal());
                break;
            }
        }

        for (LangAndExpertiseData item : priceArrayList) {
            if (item.isLangSelected()) {
                filterList.add(item.getLangVal());
                break;
            }
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("filter_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }
}
