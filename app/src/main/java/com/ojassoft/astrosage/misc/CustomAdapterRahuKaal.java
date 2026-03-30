package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanNameValueCardListData;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.util.List;

/**
 * Created by ojas-20 on 14/12/16.
 */
public class CustomAdapterRahuKaal extends BaseAdapter {

    Context context;
    List<BeanNameValueCardListData> data;
    LayoutInflater inflater = null;
    Typeface typeFace;

    public CustomAdapterRahuKaal(Context context, List<BeanNameValueCardListData> data) {
        this.context = context;
        this.data = data;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.typeFace = ((BaseInputActivity) context).regularTypeface;

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

                rowView.setTag(holder);
            } else {
                holder = (Holder) rowView.getTag();
            }

            holder.name.setText(data.get(position).getDate());
            holder.name.setTypeface(typeFace);
            holder.value.setText(data.get(position).getStartTime());
            holder.tvTime.setText(data.get(position).getEndTime());

        } catch (Exception ex) {
        }
        return rowView;
    }

    public class Holder {
        TextView name;
        TextView value;
        TextView tvTime;
    }


}
