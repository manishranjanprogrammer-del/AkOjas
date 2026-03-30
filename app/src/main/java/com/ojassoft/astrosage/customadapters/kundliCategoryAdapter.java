package com.ojassoft.astrosage.customadapters;

import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.fragments.KundliCategoryFrag;

import java.util.List;

public class kundliCategoryAdapter extends RecyclerView.Adapter<kundliCategoryAdapter.MyViewHolder> {

    private List<String> categoryList;
    KundliCategoryFrag.RefreshList refreshList;
    int selectedItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView catName;
        public LinearLayout bgLayout;


        public MyViewHolder(View view) {
            super(view);
            catName = (TextView) view.findViewById(R.id.item_name);
            bgLayout = (LinearLayout) view.findViewById(R.id.item_background);
            bgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItem = getLayoutPosition();
                    notifyDataSetChanged();
                    refreshList.changeDataOnClick(selectedItem, categoryList.get(selectedItem));
                }
            });

        }
    }


    public kundliCategoryAdapter(KundliCategoryFrag.RefreshList refreshList, List<String> categoryList) {
        this.categoryList = categoryList;
        this.refreshList = refreshList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kundli_category_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String catNameStr = categoryList.get(position);
        holder.catName.setText(catNameStr);
        if (selectedItem == position) {
            holder.bgLayout.setBackgroundColor(((BaseInputActivity) refreshList).getColor(R.color.backgroundColor));
            holder.catName.setTypeface(((BaseInputActivity) refreshList).mediumTypeface);
            holder.catName.setTextColor(((BaseInputActivity) refreshList).getColor(R.color.black));
        } else {
            holder.bgLayout.setBackgroundColor(((BaseInputActivity) refreshList).getColor(R.color.bg_card_view_color));
            holder.catName.setTypeface(((BaseInputActivity) refreshList).regularTypeface);
            holder.catName.setTextColor(((BaseInputActivity) refreshList).getColor(R.color.hint_text_color));
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}