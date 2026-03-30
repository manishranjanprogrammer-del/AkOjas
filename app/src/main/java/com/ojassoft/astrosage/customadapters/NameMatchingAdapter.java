package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.model.NameGunaMilanModel;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.List;

public class NameMatchingAdapter extends RecyclerView.Adapter<NameMatchingAdapter.MyViewHolder> {

    private Context context;
    private List<NameGunaMilanModel> milanModelList;

    public NameMatchingAdapter(Context context, List<NameGunaMilanModel> milanModelList) {
        this.milanModelList = milanModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gun_milan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (milanModelList != null && milanModelList.size() > position) {
            NameGunaMilanModel milanModel = milanModelList.get(position);
            if (milanModel == null) {
                return;
            }

            holder.valBoyTV.setText(milanModel.getBoy());
            holder.valGirlTV.setText(milanModel.getGirl());
            holder.valGunTV.setText(milanModel.getGun());
        }
    }

    @Override
    public int getItemCount() {
        if (milanModelList == null) return 0;
        return milanModelList.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView valBoyTV;
        public TextView valGunTV;
        public TextView valGirlTV;

        public MyViewHolder(View view) {
            super(view);

            valBoyTV = view.findViewById(R.id.valBoyTV);
            valGunTV = view.findViewById(R.id.valGunTV);
            valGirlTV = view.findViewById(R.id.valGirlTV);

            FontUtils.changeFont(context, valGunTV, AppConstants.FONT_ROBOTO_MEDIUM);
            FontUtils.changeFont(context, valBoyTV, AppConstants.FONT_ROBOTO_MEDIUM);
            FontUtils.changeFont(context, valGirlTV, AppConstants.FONT_ROBOTO_MEDIUM);
        }
    }
}
