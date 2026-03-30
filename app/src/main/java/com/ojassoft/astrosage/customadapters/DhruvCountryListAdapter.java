package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.interfaces.DhruvCityClickListener;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ojas on २८/३/१६.
 */
public class DhruvCountryListAdapter extends RecyclerView.Adapter<DhruvCountryListAdapter.MyViewHolder> {
    List<CountryBean> astroCoumtryNameList;
    ArrayList<CountryBean> astroCoumtryNameListduplicate = new ArrayList<CountryBean>();;
    Context context;
    private RecyclerClickListner mRecyclerClickListner;
    //  Typeface typeFace;
    DhruvCityClickListener dhruvCityClickListener;


    public DhruvCountryListAdapter(Context context, ArrayList<CountryBean> astroCoumtryNameList, DhruvCityClickListener dhruvCityClickListener) {
        this.context = context;
        this.astroCoumtryNameList = astroCoumtryNameList;
        this.astroCoumtryNameListduplicate.addAll(astroCoumtryNameList);
        this.dhruvCityClickListener = dhruvCityClickListener;
        //  this.typeFace = typeFace;

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        astroCoumtryNameList.clear();
        if (charText.length() == 0) {
            astroCoumtryNameList.addAll(astroCoumtryNameListduplicate);
        } else {
            for (CountryBean wp : astroCoumtryNameListduplicate) {
                if (wp.getCountryName().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    astroCoumtryNameList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cityname_list, parent, false);
        return new DhruvCountryListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(astroCoumtryNameList != null) {
            FontUtils.changeFont(context, holder.tvCountryName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            holder.tvCountryName.setTextColor(context.getResources().getColor(R.color.darker_gray));
            String countryName = astroCoumtryNameList.get(position).getCountryName();
            String countryCode = astroCoumtryNameList.get(position).getCountryCode();
            holder.tvCountryName.setText(countryName);

            if(position == astroCoumtryNameList.size()-1){
                holder.headerview.setBackgroundResource(R.drawable.bg_border_four_side_dark_grey);
            }else{
                holder.headerview.setBackgroundResource(R.drawable.bg_border_three_side_dark_grey);
            }
            holder.headerview.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        if(astroCoumtryNameList == null){
           return 0;
        }
        return astroCoumtryNameList.size();
    }

    public void setOnClickListener(RecyclerClickListner recyclerClickListner) {
        mRecyclerClickListner = recyclerClickListner;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCountryName;
        LinearLayout headerview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountryName = (TextView)itemView.findViewById(R.id.tvCountryName);
            headerview = (LinearLayout)itemView.findViewById(R.id.headerview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = Integer.parseInt(headerview.getTag().toString());
            dhruvCityClickListener.onItemClick(astroCoumtryNameList.get(pos));
            //mRecyclerClickListner.onItemClick(getAdapterPosition(),v);
        }
    }
}
