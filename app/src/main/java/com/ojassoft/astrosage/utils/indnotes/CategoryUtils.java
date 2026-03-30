package com.ojassoft.astrosage.utils.indnotes;

import android.content.Context;


import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryUtils {

    public static List<CategoryModel> getCategoryList(Context context) {

        List<CategoryModel> categoryModelList = new ArrayList<>();
        CategoryModel categoryModel = null;

        categoryModel = new CategoryModel();
        categoryModel.setId(1);
        categoryModel.setName(context.getString(R.string.cat_newspaper));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(2);
        categoryModel.setName(context.getString(R.string.cat_magazine));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(3);
        categoryModel.setName(context.getString(R.string.cat_tuition));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(4);
        categoryModel.setName(context.getString(R.string.cat_milk));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(5);
        categoryModel.setName(context.getString(R.string.cat_gas));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(6);
        categoryModel.setName(context.getString(R.string.cat_bill));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(7);
        categoryModel.setName(context.getString(R.string.cat_maid));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(8);
        categoryModel.setName(context.getString(R.string.cat_trash));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(9);
        categoryModel.setName(context.getString(R.string.cat_mobile));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(10);
        categoryModel.setName(context.getString(R.string.cat_telephone));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(11);
        categoryModel.setName(context.getString(R.string.cat_income));
        categoryModelList.add(categoryModel);
        categoryModel = new CategoryModel();
        categoryModel.setId(12);
        categoryModel.setName(context.getString(R.string.cat_expance));
        categoryModelList.add(categoryModel);

        categoryModel = new CategoryModel();
        categoryModel.setId(13);
        categoryModel.setName(context.getString(R.string.cat_other));
        categoryModelList.add(categoryModel);


        return categoryModelList;
    }
}
