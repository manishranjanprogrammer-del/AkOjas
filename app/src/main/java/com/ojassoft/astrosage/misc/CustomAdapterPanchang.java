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

import java.util.List;

public class CustomAdapterPanchang extends BaseAdapter {

    private Context context;
    private List<BeanNameValueCardListData> data;
    private LayoutInflater inflater = null;
    private Typeface typeFace;

    public CustomAdapterPanchang(Context context, List<BeanNameValueCardListData> data, Typeface typeface) {
        this.context = context;
        this.data = data;
        this.typeFace = typeface;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
                rowView = inflater.inflate(R.layout.layout3, null);
                holder = new Holder();
                holder.name = (TextView) rowView.findViewById(R.id.tvName);
                holder.value = (TextView) rowView.findViewById(R.id.tvValue);
                holder.tvTime = (TextView) rowView.findViewById(R.id.tvTime);

                rowView.setTag(holder);
            } else {
                holder = (Holder) rowView.getTag();
            }

            holder.name.setText(data.get(position).getName());
            holder.name.setTypeface(typeFace);
            holder.value.setText(data.get(position).getValue());
            holder.value.setTypeface(typeFace);
            holder.tvTime.setText(data.get(position).getTime());
            holder.tvTime.setTypeface(typeFace);

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
