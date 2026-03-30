package com.ojassoft.astrosage.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.fragments.KundliModules_Frag;
import com.ojassoft.astrosage.ui.fragments.vratfragment.Frag_Year;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class OthersHomeItemAdapter extends RecyclerView.Adapter<OthersHomeItemAdapter.ViewHolder> {

    Integer[] moduleIconList;
    String[] moduleNameList;
    Context context;
    int LANGUAGE_CODE = 0;

    public OthersHomeItemAdapter(Context context, Integer[] moduleIconList, String[] moduleNameList){
        this.moduleIconList = moduleIconList;
        this.context = context;
        this.moduleNameList = moduleNameList;
        this.LANGUAGE_CODE = ((ActAppModule) context).LANGUAGE_CODE;
    }

    @NonNull
    @Override
    public OthersHomeItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.home_screen_other_section_item_layout, parent, false);
        return new OthersHomeItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OthersHomeItemAdapter.ViewHolder holder, int position) {
        holder.rashiIcon.setImageResource(moduleIconList[position]);
        if (moduleNameList[position].contains("$")) {
            if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                //   holder.rashiName.setText(moduleNameList[position].replace("$", "") + " " + CUtils.getStringData(context, CGlobalVariables.ASTROASKAQUESTIONPRICE, "").replace(".","-"));
                holder.rashiName.setText(moduleNameList[position].replace("$", ""));

            } else {
                //     holder.rashiName.setText(moduleNameList[position].replace("$", "") + " " + CUtils.getStringData(context, CGlobalVariables.ASTROASKAQUESTIONPRICE, ""));
                holder.rashiName.setText(moduleNameList[position].replace("$", ""));

            }
        } else {

            //check plan to update page count text
            if (moduleNameList[position].contains("#")) {
                holder.rashiName.setText(moduleNameList[position].replace("#",
                        CUtils.getCurrentPlanCustomReturnString(CUtils.getUserPurchasedPlanFromPreference(context))));
            } else {
                holder.rashiName.setText(moduleNameList[position]);
            }
        }


        if ((moduleIconList[position] == R.drawable.icon_cogni_astro)) {
                holder.rashiName.setText(context.getResources().getString(R.string.text_varta));
                holder.rashiIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary_day_night));
            holder.rashiIcon.setImageResource(R.drawable.icon_varta);
        }

        if ((moduleIconList[position] == R.drawable.ic_kundali)) {
            if (LANGUAGE_CODE == CGlobalVariables.TELUGU || LANGUAGE_CODE == CGlobalVariables.TAMIL ||
                    LANGUAGE_CODE == CGlobalVariables.KANNADA || LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
                Glide.with(context).load(R.drawable.icon_south_kundli_ai_gif).into(holder.rashiIcon);
            } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
                Glide.with(context).load(R.drawable.icon_east_kundli_ai_gif).into(holder.rashiIcon);
            } else {
                Glide.with(context).load(R.drawable.icon_kundli_ai_gif).into(holder.rashiIcon);
            }
        }
            holder.rashiName.setPadding(10,15,10,15);
            holder.rashiName.setTextSize(14);
            holder.rashiName.setMinLines(2);

        holder.itemView.setOnClickListener(view1 -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.OTHERS_SERVICES_CLICK_EVENT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "Home screen");
            KundliModules_Frag.callActivity(position + 4);
        });

    }

    @Override
    public int getItemCount() {
        return moduleIconList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView rashiIcon;
        TextView rashiName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rashiIcon =itemView.findViewById(R.id.imageViewRashiIcon);
            rashiName = itemView.findViewById(R.id.textViewRashiName);
        }
    }
}
