package com.ojassoft.astrosage.varta.adapters;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_AVAILABLE;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.OnlineAstrologerAdapter;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.ui.fragments.VartaHomeFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class VartaHomeExpertiseAdapter extends RecyclerView.Adapter<VartaHomeExpertiseAdapter.ExpertiseViewHolder> {

    private final ArrayList<ArrayList<AstrologerDetailBean>> allAstroList;
    private final ArrayList<String> astroExpertiseNames;
    private final Activity activity;
    private final VartaHomeFragment parent;

    public VartaHomeExpertiseAdapter(ArrayList<ArrayList<AstrologerDetailBean>> allAstroList, ArrayList<String> astroExpertiseNames, Activity activity, VartaHomeFragment parent){
        this.allAstroList = allAstroList;
        this.astroExpertiseNames =  astroExpertiseNames;
        this.activity = activity;
        this.parent = parent;
    }

    @NonNull
    @Override
    public VartaHomeExpertiseAdapter.ExpertiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expertise_lists_layout, parent, false);
        return new VartaHomeExpertiseAdapter.ExpertiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VartaHomeExpertiseAdapter.ExpertiseViewHolder holder, int position) {
        try {
            ArrayList<AstrologerDetailBean> list = allAstroList.get(position);
            if (!list.isEmpty()) {
                OnlineAstrologerAdapter onlineAstrologerAdapter = new OnlineAstrologerAdapter(activity, list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
                holder.rvExpertise.setAdapter(onlineAstrologerAdapter);
                holder.rvExpertise.setLayoutManager(layoutManager);
                holder.rvExpertise.setNestedScrollingEnabled(false);
                holder.tvExpertiseHeading.setVisibility(View.VISIBLE);
                holder.tvExpertiseSeeMore.setVisibility(View.VISIBLE);
                FontUtils.changeFont(activity, holder.tvExpertiseHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
                holder.tvExpertiseHeading.setText(activity.getResources().getStringArray(R.array.astro_expertise_names)[position]);
            } else {
                holder.tvExpertiseHeading.setVisibility(View.GONE);
                holder.tvExpertiseSeeMore.setVisibility(View.GONE);
            }
        } catch (Exception e){
            Log.d("mResponse", "exception == "+e);
        }

        holder.tvExpertiseSeeMore.setOnClickListener(v->{
            CUtils.regionalFilterType = -1;
            CUtils.expertiseFilterType = position+1;
            if (parent != null){
                parent.openCallChatAstrologers(FILTER_TYPE_AVAILABLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return astroExpertiseNames.size();
    }

    public class ExpertiseViewHolder extends RecyclerView.ViewHolder {
        TextView tvExpertiseHeading,tvExpertiseSeeMore;
        RecyclerView rvExpertise;
        public ExpertiseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExpertiseHeading = itemView.findViewById(R.id.tvExpertiseHeading);
            tvExpertiseSeeMore = itemView.findViewById(R.id.tvExpertiseSeeMore);
            rvExpertise = itemView.findViewById(R.id.rvExpertise);
        }
    }
}
