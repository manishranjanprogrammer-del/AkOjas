package com.ojassoft.astrosage.varta.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.UPIAppModel;

import java.util.List;

public class UPIAppAdapter extends ArrayAdapter<UPIAppModel> {
    private final LayoutInflater inflater;
    private final int resourceDrop;
    private final int resourceSelected;

    public UPIAppAdapter(@NonNull Context context, int resourceSelected, int resourceDrop, @NonNull List<UPIAppModel> objects) {
        super(context, resourceSelected, objects);
        this.inflater = LayoutInflater.from(context);
        this.resourceDrop = resourceDrop;
        this.resourceSelected = resourceSelected;
    }

    // view for the closed spinner (selected item)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) view = inflater.inflate(resourceSelected, parent, false);

        UPIAppModel item = getItem(position);
        if (item != null) {
            ImageView iv = view.findViewById(R.id.ivIcon);
            TextView tv = view.findViewById(R.id.tvName);
            iv.setImageResource(item.getIconResId());
            tv.setText(item.getName());
        }
        return view;
    }

    // view for each dropdown row
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) view = inflater.inflate(resourceDrop, parent,false);

        UPIAppModel item = getItem(position);
        if (item != null) {
            ImageView iv = view.findViewById(R.id.ivIcon);
            TextView tv = view.findViewById(R.id.tvName);
            iv.setImageResource(item.getIconResId());
            tv.setText(item.getName());
        }
        return view;
    }

}

