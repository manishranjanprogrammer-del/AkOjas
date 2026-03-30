package com.ojassoft.astrosage.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.customadapters.NotesCategoryAdapter;
import com.ojassoft.astrosage.model.CategoryModel;
import com.ojassoft.astrosage.ui.act.indnotes.BaseActivity;
import com.ojassoft.astrosage.ui.act.indnotes.UserNotesActivity;
import com.ojassoft.astrosage.utils.indnotes.CategoryUtils;

import java.util.List;


public class ChooseCategoryFragmentDailog extends AstroParentFrag {

    private RecyclerView recyclerView;
    private List<CategoryModel> categoryModels;
    private NotesCategoryAdapter categoryAdapter;
    private Button butChooseLanguageOk;
    private Button butChooseLanguageCancel;
    private int selectedCategoryId;

    private Context context;

    public ChooseCategoryFragmentDailog() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.choose_category_dialog, container);

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedCategoryId = bundle.getInt(AppConstants.KEY_CATEGORY_ID);
        }

        butChooseLanguageOk = view.findViewById(R.id.butChooseLanguageOk);
        butChooseLanguageCancel = view.findViewById(R.id.butChooseLanguageCancel);

        getCategoryList();
        recyclerView = view.findViewById(R.id.recyclerView);
        categoryAdapter = new NotesCategoryAdapter(context, categoryModels);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);

        butChooseLanguageOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToChooseLanguageOK();
            }
        });
        butChooseLanguageCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ChooseCategoryFragmentDailog.this.dismiss();
            }
        });
        return view;
    }


    private void goToChooseLanguageOK() {
        CategoryModel selectedCategoryModel = null;

        if (categoryModels != null) {
            for (int i = 0; i < categoryModels.size(); i++) {
                CategoryModel categoryModel = categoryModels.get(i);
                if (categoryModel == null) continue;
                if (categoryModel.isSelected()) {
                    selectedCategoryModel = categoryModel;
                    break;
                }
            }
        }
        if (selectedCategoryModel == null) {
            ((BaseActivity) context).showSnackbar(recyclerView, context.getString(R.string.msg_choose_category_title));
            return;
        }
        ((UserNotesActivity) getActivity()).setSelectedCategory(selectedCategoryModel);
        this.dismiss();
    }

    private void getCategoryList() {
        categoryModels = CategoryUtils.getCategoryList(context);
        if (categoryModels == null || selectedCategoryId == 0) {
            return;
        }

        for (int i = 0; i < categoryModels.size(); i++) {
            CategoryModel categoryModel = categoryModels.get(i);
            if (categoryModel == null) continue;
            if (categoryModel.getId() == selectedCategoryId) {
                categoryModel.setSelected(true);
                break;
            }
        }
    }

}
