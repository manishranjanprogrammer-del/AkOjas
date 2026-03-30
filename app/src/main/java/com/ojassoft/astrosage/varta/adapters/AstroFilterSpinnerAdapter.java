package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.FilterData;
import com.ojassoft.astrosage.varta.model.LangAndExpertiseData;

import java.util.List;

public class AstroFilterSpinnerAdapter extends BaseAdapter {
    Activity activity;
    List<FilterData> itemList;
    public int selectedPos = 0;

    public AstroFilterSpinnerAdapter(Activity activity, List itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        if(itemList == null) return 0;
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        selectedPos = position;
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = ((Activity) activity).getLayoutInflater().inflate(R.layout.spinner_heading_layout, null);
        final TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setTextColor(activity.getResources().getColor(R.color.color_title));
        // textView.setTextSize(activity.getResources().getDimension(R.dimen.spinner_text_size));
        switch (selectedPos) {
            case 0:
                textView.setText(activity.getResources().getString(R.string.all_text));
                break;
            case 1:
                textView.setText(activity.getResources().getString(R.string.available));
                break;
            case 2:
                textView.setText(activity.getResources().getString(R.string.call));
                break;
            case 3:
                textView.setText(activity.getResources().getString(R.string.chat_now));
                break;
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = ((Activity) activity).getLayoutInflater().inflate(R.layout.spinner_item_layout, null);
        LinearLayout containerLayout = view.findViewById(R.id.spinner_container);
        TextView textview = view.findViewById(R.id.textview);
        ImageView imageView = view.findViewById(R.id.imageView);
        //CheckBox checkBox = view.findViewById(R.id.checkbox);
        //textview.setTextSize(activity.getResources().getDimension(R.dimen.spinner_text_size));

        if (itemList.get(position).isItemSelected()) {
            imageView.setImageResource(R.drawable.checked_icon);
        } else {
            imageView.setImageResource(R.drawable.unchecked_icon);
        }
        /*if (position == 0) {
            containerLayout.setPadding(0, 30, 0, 16);
            containerLayout.setBackground(activity.getResources().getDrawable(R.drawable.rect_with_top_rounded));
        } else */
        if (position == itemList.size() - 1) {
            containerLayout.setPadding(0, 16, 0, 30);
            containerLayout.setBackground(activity.getResources().getDrawable(R.drawable.rect_with_bottom_rounded));
        } else {
            containerLayout.setPadding(0, 16, 0, 16);
        }
        String title = itemList.get(position).getItem();
        textview.setText(title);
        return view;
    }


}

