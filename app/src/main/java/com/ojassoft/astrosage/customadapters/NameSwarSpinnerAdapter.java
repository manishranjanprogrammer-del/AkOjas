package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.misc.FontUtils;
import com.ojassoft.astrosage.model.NameSwarModel;

import java.util.List;

public class NameSwarSpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<NameSwarModel> items;
    private final int mResource;

    public NameSwarSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                  @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        TextView swarTV = view.findViewById(R.id.swarTV);
        FontUtils.changeFont(mContext, swarTV, AppConstants.FONT_ROBOTO_REGULAR);
        NameSwarModel nameSwarModel = items.get(position);
        if (nameSwarModel != null) {
            swarTV.setText(nameSwarModel.getSwar());
        }
        return view;
    }
}
