package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ojassoft.astrosage.R;


public class ListAdapterDasa extends ArrayAdapter<String> {
    String[] _objects;
    private Typeface _typeFace;
    Activity _activity;

    public ListAdapterDasa(Activity activity, int textViewResourceId,
                           String[] objects, Typeface typeFace) {
        super(activity, textViewResourceId, objects);
        _activity = activity;
        _objects = objects;
        _typeFace = typeFace;
    }

    public int getCount() {
        return _objects.length;
    }

    public String getItem(int position) {
        return _objects[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dasha_row, null);
        }

        String[] parseObj = null;
        TextView title = (TextView) convertView.findViewById(R.id.row_title);
        TextView row_date = (TextView) convertView.findViewById(R.id.row_date);
        title.setTypeface(_typeFace);
        try {
            parseObj = _objects[position].split("\\$");
            title.setText(parseObj[0]);
            row_date.setText(parseObj[1]);

        } catch (Exception e) {
            title.setText(_objects[position]);
        }
        //title.setText(_objects[position]);
        return convertView;
    }

}
