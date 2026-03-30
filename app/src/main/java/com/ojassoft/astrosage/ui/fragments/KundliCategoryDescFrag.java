package com.ojassoft.astrosage.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.kundliCategoryDescAdapter;
import com.ojassoft.astrosage.model.PrintSubCategory;
import com.ojassoft.astrosage.ui.act.ActPrintKundliCategory;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.Calendar;

import io.apptik.widget.MultiSlider;


public class KundliCategoryDescFrag extends Fragment {
    PrintSubCategory printSubCategory;
    RecyclerView recyclerView;
    Spinner spinner;
    boolean isSelectAllCB;
    int selectedCategory = 0;
    public CheckBox selectAllCB;
    public kundliCategoryDescAdapter kundliCategoryAdapter;
    TextView varshfalHeading;
    TextView varshfalRange;
    MultiSlider multiSlider;
    int max = 100;
    int min = 0;
    int yearOfBirth = 1990;
    int currentYear;
    int preModule;
    boolean isNeedToNotifyAgain = true;
    LinearLayout spinnerContainer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            printSubCategory = (PrintSubCategory) getArguments().getSerializable("data");
        }
        yearOfBirth = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                .getDateTime().getYear();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        min = currentYear - yearOfBirth;
        max = min + 5;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.kundli_category_desc_layout, container, false);
        spinnerContainer = root.findViewById(R.id.spinner_container);
        selectAllCB = root.findViewById(R.id.select_all_cb);
        recyclerView = root.findViewById(R.id.category_list);
        varshfalHeading = root.findViewById(R.id.varshfal_heading_text);
        varshfalRange = root.findViewById(R.id.selected_range);
        multiSlider = root.findViewById(R.id.multiSlider);
        spinner = (Spinner) root.findViewById(R.id.module_spinner);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        setDataInList(printSubCategory);
        setSpinner();
        selectAllCB.setText(getCheckBoxHeading(printSubCategory.getCatName()));

        selectAllCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isNeedToNotifyAgain) {
                    kundliCategoryAdapter.notifyOnDataChanged(true);
                    ((ActPrintKundliCategory) getActivity()).updateSubCatData(selectedCategory, b);
                }
                isNeedToNotifyAgain = true;
            }

        });
        int planiD = CUtils.getUserPurchasedPlanFromPreference(getActivity());
        if (planiD == 8 || planiD == 9 || planiD == 10) {
            spinnerContainer.setVisibility(View.VISIBLE);
        } else {
            spinnerContainer.setVisibility(View.GONE);
        }

        return root;
    }

    public void refreshData(PrintSubCategory subCategories, String title, boolean isSelected, int selectedCatPos) {
        isSelectAllCB = isSelected;
        selectedCategory = selectedCatPos;
        if (selectAllCB != null) {
            selectAllCB.setChecked(isSelectAllCB);
            setDataInList(subCategories);
            selectAllCB.setText(getCheckBoxHeading(title));
            selectAllCB.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        }
        if (selectedCategory == 4) {
            showVarshfalData(true);
        } else {
            showVarshfalData(false);
        }
    }

    private void setDataInList(PrintSubCategory printSubCategory) {
        this.printSubCategory = printSubCategory;
        kundliCategoryAdapter = new kundliCategoryDescAdapter((ActPrintKundliCategory) getActivity(), printSubCategory, isSelectAllCB);
        recyclerView.setAdapter(kundliCategoryAdapter);
    }

    public static KundliCategoryDescFrag newInstance(PrintSubCategory subCategories) {
        KundliCategoryDescFrag kundliCategoryDescFrag = new KundliCategoryDescFrag();
        Bundle args = new Bundle();
        args.putSerializable("data", subCategories);
        kundliCategoryDescFrag.setArguments(args);
        return kundliCategoryDescFrag;
    }

    public void updateView(boolean b, boolean isNeedToNotifyAgain) {
        this.isNeedToNotifyAgain = isNeedToNotifyAgain;
        selectAllCB.setChecked(b);
    }

    int diff = 0;

    private void showVarshfalData(boolean val) {

        if (val) {
            int planId = CUtils.getUserPurchasedPlanFromPreference(getActivity());
            if (planId == CGlobalVariables.PLATINUM_PLAN_ID_10 ||
                    planId == CGlobalVariables.PLATINUM_PLAN_ID ||
                    planId == CGlobalVariables.PLATINUM_PLAN_ID_9 ||
                    planId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                varshfalHeading.setVisibility(View.VISIBLE);
                varshfalRange.setVisibility(View.VISIBLE);
                multiSlider.setVisibility(View.VISIBLE);
                varshfalHeading.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
                varshfalRange.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
                varshfalRange.setText((yearOfBirth + min) + "-" + (yearOfBirth + max));
                multiSlider.getThumb(0).setValue(min);
                multiSlider.getThumb(1).setValue(max);
            } else {
                varshfalHeading.setVisibility(View.GONE);
                varshfalRange.setVisibility(View.GONE);
                multiSlider.setVisibility(View.GONE);
            }
        } else {
            varshfalHeading.setVisibility(View.GONE);
            varshfalRange.setVisibility(View.GONE);
            multiSlider.setVisibility(View.GONE);
        }

        multiSlider.setOnThumbValueChangeListener(new MultiSlider.SimpleChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int
                    thumbIndex, int value) {
                diff = max - min;
                if (thumbIndex == 0) {
                    min = value;
                    if (diff > 25) {
                        max = max - 1;
                        multiSlider.getThumb(0).setValue(min);
                        multiSlider.getThumb(1).setValue(max);
                    }
                } else {
                    max = value;
                    if (diff > 25) {
                        min = min + 1;
                        multiSlider.getThumb(1).setValue(max);
                        multiSlider.getThumb(0).setValue(min);

                    }
                }
                varshfalRange.setText((yearOfBirth + min) + "-" + (yearOfBirth + max));
                ((ActPrintKundliCategory) getActivity()).startYear = yearOfBirth + min;
                ((ActPrintKundliCategory) getActivity()).numberOfYear = max - min;

            }
        });
    }

    int selectedModule;

    private void setSpinner() {
        String[] moduleList = getResources().getStringArray(R.array.print_module);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(requireContext(), R.layout.spinner_item,moduleList){

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                view.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bg_card_view_color));
                return view;
            }
        };

        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedModule = i;
                ((ActPrintKundliCategory) getActivity()).doActionOnModuleSelect(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ((ActPrintKundliCategory) getActivity()).doActionOnModuleSelect(selectedModule);
            }


        });
    }

    private String getCheckBoxHeading(String title) {
        String cbTitle = getResources().getString(R.string.all) + " " + title + " " + getResources().getString(R.string.pages);
        int langCode = ((BaseInputActivity) getActivity()).LANGUAGE_CODE;
        if (langCode == CGlobalVariables.TAMIL ||
                langCode == CGlobalVariables.TELUGU ||
                langCode == CGlobalVariables.KANNADA ||
                langCode == CGlobalVariables.MALAYALAM ||
                langCode == CGlobalVariables.MARATHI ||
                langCode == CGlobalVariables.GUJARATI ||
                langCode == CGlobalVariables.BANGALI
        ) {
            cbTitle = title;
        }
        return cbTitle;
    }

}
