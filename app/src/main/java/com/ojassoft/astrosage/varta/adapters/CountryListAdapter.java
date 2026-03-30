package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
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
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.MyViewHolder> {
    List<CountryBean> astroCoumtryNameList;
    ArrayList<CountryBean> astroCoumtryNameListduplicate;
    Context context;
    private RecyclerClickListner mRecyclerClickListner;
    //  Typeface typeFace;


    public CountryListAdapter(Context context, ArrayList<CountryBean> astroCoumtryNameList) {
        this.context = context;
        this.astroCoumtryNameList = astroCoumtryNameList;
        this.astroCoumtryNameListduplicate = new ArrayList<CountryBean>();
        this.astroCoumtryNameListduplicate.addAll(astroCoumtryNameList);
        //  this.typeFace = typeFace;

    }

    // Filter Class
    public List<CountryBean> filter(String charText) {
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

       return astroCoumtryNameList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cityname_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(astroCoumtryNameList != null) {
            FontUtils.changeFont(context, holder.tvCountryName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            holder.tvCountryName.setTextColor(ContextCompat.getColor(context,R.color.text_day_gray_night_white));
            String countryName = astroCoumtryNameList.get(position).getCountryName();
            String countryCode = astroCoumtryNameList.get(position).getCountryCode();
            holder.tvCountryName.setText(countryName + " (+" + countryCode + ")");
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountryName = (TextView)itemView.findViewById(R.id.tvCountryName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecyclerClickListner.onItemClick(getAdapterPosition(),v);
        }
    }
}
