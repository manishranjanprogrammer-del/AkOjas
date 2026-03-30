package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopCountryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ojas on २८/३/१६.
 */
public class AstroShopCountryListAdapter extends BaseAdapter {
    List<AstroShopCountryModel> astroCoumtryNameList;
    ArrayList<AstroShopCountryModel> astroCoumtryNameListduplicate;
    Context context;
    LayoutInflater inflater = null;
    private AstroShopCountryModel mFilter = new AstroShopCountryModel();
    //  Typeface typeFace;


    public AstroShopCountryListAdapter(Context context, ArrayList<AstroShopCountryModel> astroCoumtryNameList) {
        this.context = context;
        this.astroCoumtryNameList = astroCoumtryNameList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.astroCoumtryNameListduplicate = new ArrayList<AstroShopCountryModel>();
        this.astroCoumtryNameListduplicate.addAll(astroCoumtryNameList);
        //  this.typeFace = typeFace;

    }


    @Override
    public int getCount() {
        return astroCoumtryNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;
        try {
            rowView = convertView;
            Holder holder;
            if (convertView == null) {
                rowView = inflater.inflate(R.layout.activity_astroshop_cityname_list,
                        null);
                holder = new Holder();

                holder.tvCountryName = (TextView) rowView
                        .findViewById(R.id.tvCountryName);
                rowView.setTag(holder);
            } else {
                holder = (Holder) rowView.getTag();
            }
            holder.tvCountryName.setText(astroCoumtryNameList.get(position).getCn_Name());

        } catch (Exception ex) {
        }
        return rowView;
    }


    public class Holder {
        TextView tvCountryName;

    }

    // Filter Class
    public List<AstroShopCountryModel> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        astroCoumtryNameList.clear();
        if (charText.length() == 0) {
            astroCoumtryNameList.addAll(astroCoumtryNameListduplicate);
        } else {
            for (AstroShopCountryModel wp : astroCoumtryNameListduplicate) {
                if (wp.getCn_Name().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    astroCoumtryNameList.add(wp);
                }
            }
        }
        notifyDataSetChanged();

       return astroCoumtryNameList;
    }
}
