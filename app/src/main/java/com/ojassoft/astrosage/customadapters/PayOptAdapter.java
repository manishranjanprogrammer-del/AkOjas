package com.ojassoft.astrosage.customadapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ojassoft.astrosage.R;

import com.ojassoft.astrosage.model.CardTypeDTO;
import com.ojassoft.astrosage.model.PaymentOptionDTO;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;


public class PayOptAdapter extends ArrayAdapter<CardTypeDTO> {
    private Activity context;
    ArrayList<CardTypeDTO> data = null;

    public PayOptAdapter(Activity context, int resource,
                         ArrayList<CardTypeDTO> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        return getDropDownView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        CardTypeDTO paymentOption = data.get(position);
        if (paymentOption != null) { // Parse the data from each object and set it.
            TextView optionName = (TextView) row.findViewById(R.id.item_value);
            optionName.setTypeface(((BaseInputActivity) context).robotRegularTypeface);
            optionName.setText(paymentOption.getCardName());
        }
        return row;
    }
}