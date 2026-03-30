package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.ProductCategory;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.fragments.Astroshop_Frag;

import java.util.List;

public class AstroShopCategoryAdapter extends RecyclerView.Adapter<AstroShopCategoryAdapter.CategoryViewHolder> {

    private static int CONSTANT_TO_UPDATE_POSITION_FOR_BELOW_GRID = 4;
    Integer[] moduleIconList;
    String[] moduleNameList;
    Activity activity;
    Typeface typeface;
    //Fragment fragment;
    int LANGUAGE_CODE = 0;
    boolean isBelowGrid;
    public boolean isCategoryChatOption = false;
    private List<ProductCategory> categoryList;
    String deepLinkUrl="";


    public AstroShopCategoryAdapter(Activity activity, List<ProductCategory> categoryList, Typeface typeface/*,Fragment fragment*/) {
        this.activity = activity;
        this.typeface = typeface;
        //this.fragment = fragment;
        this.categoryList = categoryList;
        if (activity != null && activity instanceof BaseInputActivity) {
            this.LANGUAGE_CODE = ((BaseInputActivity) activity).LANGUAGE_CODE;
        } else {
            this.LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode(); // or set to a default/fallback value
        }
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_astro_shop_category, parent, false);
        return new CategoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        deepLinkUrl = categoryList.get(position).getCategoryUrl();
        String categoryFullName = categoryList.get(position).getCategoryShortName();
       /* String firstCategoryNameWord;
        if (categoryFullName.contains(context.getString(R.string.platinum_plan))){
            firstCategoryNameWord = context.getString(R.string.platinum_plan);
        }else {
            firstCategoryNameWord = categoryFullName.split("\\s+")[0]; // Splitting by whitespace
        }*/


        String mainModuleId = categoryFullName+"_Main_Module";
        Log.e("SAN ", " CUtils CategoryUrl deeplink " + deepLinkUrl );

        holder.rashiName.setText(categoryFullName);
        holder.rashiName.setTypeface(typeface);

        //rashiName.setText(categoryList.get(position).getCategoryFullName());
        if (categoryList.get(position).getCategoryImagePath() != null) {
            // Load from URL
            Glide.with(activity)
                    .load(categoryList.get(position).getCategoryImagePath())
                    .into(holder.rashiIcon);
        } else {
            // Load from drawable resource
            holder.rashiIcon.setImageResource(Integer.parseInt(categoryList.get(position).getResourceImage()));
        }

        holder.itemView.setOnClickListener(view1 -> {

            Astroshop_Frag.callActivity(activity,position,categoryList.get(position).getCategoryUrl(),mainModuleId,categoryList.get(position).getCategoryShortName());

        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // ViewHolder Class
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView rashiIcon;
        ImageView imageViewNew;
        TextView rashiName;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
             rashiIcon = (ImageView) itemView
                    .findViewById(R.id.imageViewRashiIcon);
             imageViewNew = (ImageView) itemView.findViewById(R.id.imageViewNew);
             rashiName = (TextView) itemView
                    .findViewById(R.id.textViewRashiName);
        }
    }
}