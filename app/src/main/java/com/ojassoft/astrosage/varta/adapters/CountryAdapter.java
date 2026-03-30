package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<CountryBean> {

Context context;
    public CountryAdapter(Context context,
                          ArrayList<CountryBean> algorithmList)
    {
        super(context, 0, algorithmList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_list_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.text1);
        FontUtils.changeFont(context,textViewName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR );
        textViewName.setTextColor(context.getResources().getColor(R.color.darker_gray));
        //textViewName.setTextSize(context.getResources().getDimension(R.dimen.spinner_text_size));
        textViewName.setGravity(Gravity.LEFT);
        textViewName.setPadding(10, 5, 10, 5);
        CountryBean currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            String countryName = currentItem.getCountryName();
            String countryCode = currentItem.getCountryCode();
            textViewName.setText(countryName + "(+"+countryCode+")");
        }
        return convertView;
    }

}
