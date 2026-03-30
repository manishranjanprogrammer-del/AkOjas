package com.ojassoft.astrosage.customadapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.PrintSubCategory;
import com.ojassoft.astrosage.ui.act.ActPrintKundliCategory;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.fragments.KundliCategoryFrag;

import java.util.ArrayList;

public class kundliCategoryDescAdapter extends RecyclerView.Adapter<kundliCategoryDescAdapter.MyViewHolder> {

    private ArrayList<PrintSubCategory.SubCatDetail> subCatDetailList;
    KundliCategoryFrag.RefreshList refreshList;
    PrintSubCategory printSubCategory;
    boolean isNeedToNotifyAgain = true;
    //boolean selectAllCB;
    private RecyclerView mRecyclerView;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.check_box);
        }
    }


    public kundliCategoryDescAdapter(KundliCategoryFrag.RefreshList refreshList, PrintSubCategory printSubCategory, boolean selectAllCB) {
        this.printSubCategory = printSubCategory;
        this.subCatDetailList = printSubCategory.getSubCatDetails();
        this.refreshList = refreshList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kundli_category_item_desc_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PrintSubCategory.SubCatDetail catNameStr = subCatDetailList.get(position);
        holder.checkBox.setText(catNameStr.getSuCatName());
        holder.checkBox.setTypeface(((BaseInputActivity) refreshList).regularTypeface);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        holder.checkBox.setTextColor(((ActPrintKundliCategory) refreshList).getResources().getColor(R.color.black));
                        catNameStr.setCatSelected(true);
                    } else {
                        holder.checkBox.setTextColor(((ActPrintKundliCategory) refreshList).getResources().getColor(R.color.hint_text_color));
                        catNameStr.setCatSelected(false);
                        printSubCategory.setAllSubCatSelected(false);
                        ((ActPrintKundliCategory) refreshList).kundliCategoryDescFrag.updateView(false, isNeedToNotifyAgain);
                        isNeedToNotifyAgain = true;
                    }
                    if (((ActPrintKundliCategory) refreshList).isAllItemSelected(subCatDetailList)) {
                        printSubCategory.setAllSubCatSelected(true);
                        ((ActPrintKundliCategory) refreshList).kundliCategoryDescFrag.updateView(true, isNeedToNotifyAgain);
                        isNeedToNotifyAgain = true;
                    }
                    ((ActPrintKundliCategory) refreshList).updateView();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //((ActPrintKundliCategory) refreshList).selectAllCategory ||
        if (printSubCategory.isAllSubCatSelected()) {
            holder.checkBox.setChecked(true);
            holder.checkBox.setTextColor(((ActPrintKundliCategory) refreshList).getResources().getColor(R.color.black));

        } else {
            if (catNameStr.isCatSelected()) {
                holder.checkBox.setTextColor(((ActPrintKundliCategory) refreshList).getResources().getColor(R.color.black));
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
                holder.checkBox.setTextColor(((ActPrintKundliCategory) refreshList).getResources().getColor(R.color.hint_text_color));

            }
        }
    }

    @Override
    public int getItemCount() {
        return subCatDetailList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public void notifyOnDataChanged(boolean isNeedToNotifyAgain) {
        this.isNeedToNotifyAgain = isNeedToNotifyAgain;
        if(mRecyclerView != null) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

}