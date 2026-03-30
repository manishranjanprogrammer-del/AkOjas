package com.ojassoft.astrosage.varta.adapters;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SELECTED_KUNDLI_PROFILE;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.SavedKundliListActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileListViewHolder>{

    private final Context mContext;
    private final Activity mActivity;
    private final ArrayList<BeanHoroPersonalInfo> mList;

    public ProfileListAdapter(Activity activity, Context context, ArrayList<BeanHoroPersonalInfo> list){
        this.mActivity = activity;
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ProfileListAdapter.ProfileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfileListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_kundli_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.ProfileListViewHolder holder, int position) {

        BeanHoroPersonalInfo item = mList.get(position);

        FontUtils.changeFont(mContext,holder.tvSavedKundliName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(mContext,holder.tvSavedKundliTime, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(mContext,holder.tvSavedKundliPlace, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        if (item.getGender().equals("Male") || item.getGender().equals("M")){
            holder.ivSavedKundli.setImageResource(R.drawable.male_user);
        } else if (item.getGender().equals("Female") || item.getGender().equals("F")){
            holder.ivSavedKundli.setImageResource(R.drawable.female_user);
        }

        /*String selectedProfile = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(mContext,
                SELECTED_KUNDLI_PROFILE,"");

        if (String.valueOf(item.getLocalChartId()).equals(selectedProfile)){
            holder.tvSavedProfileSelected.setVisibility(View.VISIBLE);
            FontUtils.changeFont(mContext,holder.tvSavedProfileSelected, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        } else {
            holder.tvSavedProfileSelected.setVisibility(View.GONE);
        }*/

        BeanDateTime beanDateTime = item.getDateTime();
        String day = String.format("%02d", beanDateTime.getDay());
        String month = String.format("%02d", beanDateTime.getMonth()+1);
        int year = beanDateTime.getYear();
        String hour = String.format("%02d", beanDateTime.getHour());
        String min = String.format("%02d", beanDateTime.getMin());
        String sec = String.format("%02d", beanDateTime.getSecond());

        String date = year+"-"+month+"-"+day;
        date = CUtils.formatDateForShiping(date);
        String time = " | "+hour+":"+min+":"+sec;

        holder.tvSavedKundliName.setText(item.getName());
        holder.tvSavedKundliTime.setText(date+time);

        String place = "";
        String city = item.getPlace().getCityName();
        String state = item.getPlace().getState();
        if (state.equals("")) {
            place = city;
        } else {
            place = city + ","+state;
        }
        holder.tvSavedKundliPlace.setText(place);

        holder.clSavedKundli.setOnClickListener(v->
                ((SavedKundliListActivity)mActivity).onKundliSelected(position));

    }

    @Override
    public int getItemCount() {
        int mLimit = 5;
        return Math.min(mList.size(), mLimit);
    }

    public static class ProfileListViewHolder extends RecyclerView.ViewHolder {
        TextView tvSavedKundliName, tvSavedKundliTime, tvSavedKundliPlace, tvSavedProfileSelected;
        ImageView ivSavedKundli;
        ConstraintLayout clSavedKundli;
        public ProfileListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSavedKundli = itemView.findViewById(R.id.ivSavedKundli);
            tvSavedKundliName = itemView.findViewById(R.id.tvSavedKundliName);
            tvSavedKundliTime = itemView.findViewById(R.id.tvSavedKundliTime);
            tvSavedKundliPlace = itemView.findViewById(R.id.tvSavedKundliPlace);
            tvSavedProfileSelected = itemView.findViewById(R.id.tvSavedProfileSelected);
            clSavedKundli = itemView.findViewById(R.id.clSavedKundli);
        }
    }
}
