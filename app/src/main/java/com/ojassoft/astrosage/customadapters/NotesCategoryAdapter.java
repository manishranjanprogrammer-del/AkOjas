package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;


import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.ojassoft.astrosage.model.CategoryModel;

import java.util.List;

public class NotesCategoryAdapter extends RecyclerView.Adapter<NotesCategoryAdapter.MyViewHolder> {

    private RecyclerClickListner recyclerClickListner;
    private Context context;
    private List<CategoryModel> categoryModels;

    public NotesCategoryAdapter(Context context, List<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_notes_category, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (categoryModels != null && categoryModels.size() > position) {
            final CategoryModel categoryModel = categoryModels.get(position);
            if (categoryModel == null) {
                return;
            }
            holder.radioCategory.setText(categoryModel.getName() + "");
            if (categoryModel.isSelected()) {
                holder.radioCategory.setChecked(true);
            } else {
                holder.radioCategory.setChecked(false);
            }
            holder.radioCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < categoryModels.size(); i++) {
                        CategoryModel categoryModel = categoryModels.get(i);
                        if (categoryModel == null) continue;
                        categoryModel.setSelected(false);
                    }
                    categoryModel.setSelected(true);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (categoryModels == null) return 0;
        return categoryModels.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout containerLayout;
        public RadioButton radioCategory;

        public MyViewHolder(View view) {
            super(view);

            containerLayout = view.findViewById(R.id.containerLayout);
            radioCategory = view.findViewById(R.id.radioCategory);
        }
    }
}
