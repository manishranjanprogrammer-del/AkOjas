package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanNameValueCardListData;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

//import com.google.analytics.tracking.android.Log;

public class CustomAdapter extends BaseAdapter {

    private boolean AM_PM_NOT_REQUIRED = false;
    Context context;
    List<BeanNameValueCardListData> data;
    LayoutInflater inflater = null;
    Typeface typeFace;

    public CustomAdapter(Context context, List<BeanNameValueCardListData> data, boolean AM_PM_FLAG) {
        this.context = context;
        this.data = data;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.typeFace = ((BaseInputActivity) context).regularTypeface;
        this.AM_PM_NOT_REQUIRED = AM_PM_FLAG;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //Toast.makeText(context, data.size()+"", Toast.LENGTH_SHORT).show();
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = null;
        try {
            rowView = convertView;
            Holder holder;
            if (convertView == null) {
                rowView = inflater.inflate(R.layout.list_view_inner_layout, null);
                holder = new Holder();
                holder.name = (TextView) rowView.findViewById(R.id.tvName);
                holder.value = (TextView) rowView.findViewById(R.id.tvValue);
                holder.tvTime = (TextView) rowView.findViewById(R.id.tvTime);
                holder.infoIV = (ImageView) rowView.findViewById(R.id.info_iv);

                rowView.setTag(holder);
            } else {
                holder = (Holder) rowView.getTag();
            }

            holder.name.setText(data.get(position).getName());
            holder.name.setTypeface(typeFace);
            if (position == 2 && AM_PM_NOT_REQUIRED == true) {
                holder.value.setText(data.get(position).getValue());

                holder.tvTime.setText(data.get(position).getTime());
            } else {
                holder.value.setText(checkSplitValue(data.get(position).getValue()));
                holder.tvTime.setText(checkSplitValue(data.get(position).getTime()));
            }
            if (data.get(position).getName().equals(context.getResources().getString(R.string.month_amanta))
                    || data.get(position).getName().equals(context.getResources().getString(R.string.month_purnimanta))) {
                holder.infoIV.setVisibility(View.VISIBLE);
            } else {
                holder.infoIV.setVisibility(View.GONE);
            }
            holder.infoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CUtils.openLanguageSelectDialog(((AppCompatActivity) context).getSupportFragmentManager());
                }
            });

        } catch (Exception ex) {
        }
        return rowView;
    }

    public class Holder {
        TextView name;
        TextView value;
        TextView tvTime;
        ImageView infoIV;
    }

    private String checkSplitValue(String rawValue) {
        String returnString = "";
        String[] stringSplit = null;
        if (rawValue.contains("\n")) {
            stringSplit = rawValue.split("\n");
            if (stringSplit[0].contains(",")) {
                returnString = CUtils.getTimeInFormate(stringSplit[0]).replace("+", context.getString(R.string.tomorrow_label) + "\n")
                        + ",\n"
                        + CUtils.getTimeInFormate(stringSplit[1]).replace("\n", "").replace("+", context.getString(R.string.tomorrow_label) + "\n");
            } else {
                returnString = CUtils.getTimeInFormate(stringSplit[0]).replace("+", context.getString(R.string.tomorrow_label) + "\n")
                        + "\n"
                        + CUtils.getTimeInFormate(stringSplit[1]).replace("\n", "").replace("+", context.getString(R.string.tomorrow_label) + "\n");
            }
        } else {
            if (rawValue.contains(",")) {
                stringSplit = rawValue.split(",");
                returnString = CUtils.getTimeInFormate(stringSplit[0]).replace("+", context.getString(R.string.tomorrow_label) + "\n")
                        + ",\n"
                        + CUtils.getTimeInFormate(stringSplit[1]).replace("\n", "").replace("+", context.getString(R.string.tomorrow_label) + "\n");
            } else {
                returnString = CUtils.getTimeInFormate(rawValue).replace("+", context.getString(R.string.tomorrow_label) + "\n");
            }
        }
        return returnString.replace(",,", ",");
    }
}
