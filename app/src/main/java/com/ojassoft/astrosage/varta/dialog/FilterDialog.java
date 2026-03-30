package com.ojassoft.astrosage.varta.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.ojassoft.astrosage.varta.ui.fragments.HomeFragment1;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.ArrayList;
import java.util.List;

public class FilterDialog extends BottomSheetDialogFragment implements FilterAstrologerAdapter.FilterClickCallbacks {

    public static final String TAG = "ActionBottomDialog";
    private final int TYPE_LANGUAGE = 0;
    private final int TYPE_CATEGORY = 1;
    private final int TYPE_SKILL = 3;
    private final int TYPE_CONSULTATON = 4;
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
    public static ArrayList<LangAndExpertiseData> consultationArrayList;
    public static ArrayList<LangAndExpertiseData> priceArrayList;



    private RecyclerView skill_recyclerview;
    private RecyclerView language_recyclerview;
    private RecyclerView category_recyclerview;
    private RecyclerView consultation_recyclerview;
    private RecyclerView price_recyclerview;

    FilterAstrologerAdapter languageAdapter;
    FilterAstrologerAdapter skillAdapter;
    FilterAstrologerAdapter categoryAdapter;
    FilterAstrologerAdapter consultationAdapter;
    FilterAstrologerAdapter priceAdapter;

    private TextView tv_clear_all;
    private TextView tv_apply;

    private ImageView iv_close;

    private static FilterDialog obj;

    public static FilterDialog getInstance() {
        if (obj == null) {
            obj = new FilterDialog();
        }
        return obj;
    }

    public FilterDialog() {
       /* this.fragment = HomeFragment1.getInstance();
        this.filterList = filterList;*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
        filterList = HomeFragment1.filterList;
        if (filterList == null){
            filterList = new ArrayList<>();
        }
        expertiseFilterList = HomeFragment1.expertiseFilterList;
        if (expertiseFilterList == null){
            expertiseFilterList = new ArrayList<>();
        }
        langFilterList = HomeFragment1.langFilterList;
        if (langFilterList == null){
            langFilterList = new ArrayList<>();
        }
        langArrayList = getLangData();
        skillArrayList = getExpetiseData();
        categoryArrayList = getOrderData();
        consultationArrayList = getConsultation();
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
        consultationAdapter = new FilterAstrologerAdapter(getContext(), consultationArrayList, this, R.layout.filter_row, TYPE_CONSULTATON);
        consultation_recyclerview.setLayoutManager(filterlayoutManager);
        consultation_recyclerview.setAdapter(consultationAdapter);

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
                    HomeFragment1 frag = HomeFragment1.getInstance();
                    frag.clearFilter();
                } catch (Exception e){
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
                    HomeFragment1 frag = HomeFragment1.getInstance();
                    frag.applyFilter(filterList, langFilterList, expertiseFilterList);
                } catch (Exception e){
                    //
                }

            }
        });

    }

    private void generatedID(View view) {
        language_recyclerview = view.findViewById(R.id.language_recyclerview);
        skill_recyclerview = view.findViewById(R.id.skill_recyclerview);

        category_recyclerview = view.findViewById(R.id.category_recyclerview);
        consultation_recyclerview = view.findViewById(R.id.consultation_recyclerview);
        price_recyclerview = view.findViewById(R.id.price_recyclerview);


        tv_clear_all = view.findViewById(R.id.tv_clear_all);
        tv_apply = view.findViewById(R.id.tv_apply);

        iv_close = view.findViewById(R.id.iv_close);
    }


    private ArrayList<LangAndExpertiseData> getConsultation() {
        ArrayList<LangAndExpertiseData> langAndExpertiseDataArrayList = new ArrayList<>();

        LangAndExpertiseData langAndExpertiseData3 = new LangAndExpertiseData();
        langAndExpertiseData3.setLangName(getContext().getResources().getString(R.string.call));
        langAndExpertiseData3.setLangVal(consultationID[0]);
        if (filterList.contains(langAndExpertiseData3.getLangVal())){
            langAndExpertiseData3.setLangSelected(true);
        }
        langAndExpertiseDataArrayList.add(langAndExpertiseData3);

        LangAndExpertiseData langAndExpertiseData4 = new LangAndExpertiseData();
        langAndExpertiseData4.setLangName(getContext().getResources().getString(R.string.chat_now));
        langAndExpertiseData4.setLangVal(consultationID[1]);
        if (filterList.contains(langAndExpertiseData4.getLangVal())){
            langAndExpertiseData4.setLangSelected(true);
        }
        langAndExpertiseDataArrayList.add(langAndExpertiseData4);

        LangAndExpertiseData langAndExpertiseData2 = new LangAndExpertiseData();
        langAndExpertiseData2.setLangName(getContext().getResources().getString(R.string.live));
        langAndExpertiseData2.setLangVal(consultationID[2]);
        if (filterList.contains(langAndExpertiseData2.getLangVal())){
            langAndExpertiseData2.setLangSelected(true);
        }
        langAndExpertiseDataArrayList.add(langAndExpertiseData2);
        return langAndExpertiseDataArrayList;
    }

    private ArrayList<LangAndExpertiseData> getOrderData() {
        final String[] itemList = getContext().getResources().getStringArray(R.array.order_spinner_data);
        ArrayList<LangAndExpertiseData> langAndExpertiseDataArrayList = new ArrayList<>();
        LangAndExpertiseData langAndExpertiseData;
        for (int i = 0; i < itemList.length; i++) {
            langAndExpertiseData = new LangAndExpertiseData();
            langAndExpertiseData.setLangName(itemList[i]);
            langAndExpertiseData.setLangVal(orderID[i]);
            if (filterList.contains(langAndExpertiseData.getLangVal())){
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
            if (filterList.contains(langAndExpertiseData.getLangVal())){
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
            if (filterList.contains(langAndExpertiseData.getLangVal())){
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
            if (filterList.contains(langAndExpertiseData.getLangVal())){
                langAndExpertiseData.setLangSelected(true);
            }
            langAndExpertiseDataArrayList.add(langAndExpertiseData);
        }
        return langAndExpertiseDataArrayList;
    }

    @Override
    public void onFilterItemClick(int position, int type) {
        if (type == TYPE_LANGUAGE) {
            if (langArrayList.get(position).isLangSelected()) {
                langArrayList.get(position).setLangSelected(false);
            } else {
                langArrayList.get(position).setLangSelected(true);
            }
            languageAdapter.notifyDataSetChanged();
        } else if (type == TYPE_SKILL) {
            if (skillArrayList.get(position).isLangSelected()) {
                skillArrayList.get(position).setLangSelected(false);
            } else {
                skillArrayList.get(position).setLangSelected(true);
            }
            skillAdapter.notifyDataSetChanged();
        } else if (type == TYPE_CONSULTATON) {
            /*if (consultationArrayList.get(position).isLangSelected()) {
                consultationArrayList.get(position).setLangSelected(false);
            } else {
                consultationArrayList.get(position).setLangSelected(true);
            }*/
            for (LangAndExpertiseData item:consultationArrayList){
                if (item == consultationArrayList.get(position)){
                    item.setLangSelected(true);
                }else{
                    item.setLangSelected(false);
                }
            }
            consultationAdapter.notifyDataSetChanged();

            if (consultationArrayList.get(position).getLangVal().equals("Live")){

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            dismiss();
                            HomeFragment1 frag = HomeFragment1.getInstance();
                            frag.showLiveAstrologer();
                        } catch (Exception e){
                            //
                        }
                    }
                },300);
            }

        } else if (type == TYPE_CATEGORY) {
            /*if (categoryArrayList.get(position).isLangSelected()) {
                categoryArrayList.get(position).setLangSelected(false);
            } else {
                categoryArrayList.get(position).setLangSelected(true);
            }*/
            for (LangAndExpertiseData item:categoryArrayList){
                if (item == categoryArrayList.get(position)){
                    item.setLangSelected(true);
                }else{
                    item.setLangSelected(false);
                }
            }

            categoryAdapter.notifyDataSetChanged();
        }else if (type == TYPE_PRICE) {
            for (LangAndExpertiseData item:priceArrayList){
                if (item == priceArrayList.get(position)){
                    item.setLangSelected(true);
                }else{
                    item.setLangSelected(false);
                }
            }
            priceAdapter.notifyDataSetChanged();
        }
    }

    private void addFilterList(){
        filterList.clear();
        expertiseFilterList.clear();
        langFilterList.clear();
        for (LangAndExpertiseData item:langArrayList){
            if (item.isLangSelected()){
                filterList.add(item.getLangVal());
                langFilterList.add(item.getLangVal());
            }
        }
        for (LangAndExpertiseData item:skillArrayList){
            if (item.isLangSelected()){
                filterList.add(item.getLangVal());
                expertiseFilterList.add(item.getLangVal());
            }
        }
        for (LangAndExpertiseData item:categoryArrayList){
            if (item.isLangSelected()){
                filterList.add(item.getLangVal());
                break;
            }
        }

        for (LangAndExpertiseData item:priceArrayList){
            if (item.isLangSelected()){
                filterList.add(item.getLangVal());
                break;
            }
        }

        for (LangAndExpertiseData item:consultationArrayList){
            if (item.isLangSelected()){
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
