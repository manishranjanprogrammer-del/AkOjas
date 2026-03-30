package com.ojassoft.astrosage.misc;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libojassoft.android.beans.LibOutPlace;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.BeanUserMapping;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.MychartActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.MychartActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas-08 on 1/9/17.
 */

public class AutoCompleteCityAdapter extends ArrayAdapter<LibOutPlace> {

    Activity context;
    boolean isShowProgressbar = true;
    //int resource, textViewResourceId;
    List<LibOutPlace> cityList, tempItems, suggestions;



    public AutoCompleteCityAdapter(Context context, int resource, List<LibOutPlace> cityList) {
        super(context, resource, cityList);
        this.context = (Activity) context;
        this.isShowProgressbar = isShowProgressbar;
        //this.resource = resource;
        //this.textViewResourceId = textViewResourceId;
        this.cityList = cityList;
        tempItems = new ArrayList<LibOutPlace>(cityList); // this makes the difference.
        suggestions = new ArrayList<LibOutPlace>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.citysearchfrag_customlist, parent, false);
        }
        LibOutPlace libOutPlace = cityList.get(position);

        TextView cityTextView = (TextView) view.findViewById(R.id.text1);
        TextView countryTextView = (TextView) view.findViewById(R.id.text2);
        cityTextView.setText(libOutPlace.getName().trim() + ", "
                + libOutPlace.getState().trim());

        countryTextView.setText(libOutPlace.getCountry());


        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            LibOutPlace libOutPlace = (LibOutPlace) resultValue;
            return libOutPlace.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {

                suggestions.clear();
                for (LibOutPlace libOutPlace : tempItems) {

                    if (libOutPlace.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        Log.i("CharSequence", libOutPlace.getName());
                        suggestions.add(libOutPlace);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<LibOutPlace> filterList = (ArrayList<LibOutPlace>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (LibOutPlace libOutPlace : filterList) {
                    add(libOutPlace);
                    notifyDataSetChanged();
                }
            }
        }
    };

   /* public void refreshList(String searchText) {
        ArrayList<String> list = new ArrayList();
        int items = getCount();
        for (int i = 0; i < tempItems.size(); i++) {
            if (tempItems.get(i).toLowerCase().contains(searchText.toLowerCase())) {
                //Log.i("CharSequence", city);
                add(tempItems.get(i));
                notifyDataSetChanged();
            }
        }
    }*/

  /*  public void addItem(ArrayList<String> arrayList) {
        for (int j = 0; j < arrayList.size(); j++) {
            boolean isAddToList = true;

            if (isAddToList) {
                cityList.add(arrayList.get(j));
            }
        }

        tempItems = new ArrayList<String>(cityList);
        isShowProgressbar = false;
        notifyDataSetChanged();
    }*/

    /*public void clearData() {
        items.clear();
        tempItems.clear();
    }*/

  /*  public void replaceItem(ArrayList<String> arrayList) {
        for (int i = 0; i < cityList.size(); i++) {
            cityList.remove(i);
            tempItems.remove(i);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            cityList.add(arrayList.get(i));
        }
        tempItems = new ArrayList<String>(cityList);
        isShowProgressbar = true;
        notifyDataSetChanged();
    }*/


    public LibOutPlace getClickedItem(int position) {
        return cityList.get(position);
    }


    @Override
    public int getCount() {
        return cityList.size();
    }

    public List<LibOutPlace> getItems() {
        return cityList;
    }

}
