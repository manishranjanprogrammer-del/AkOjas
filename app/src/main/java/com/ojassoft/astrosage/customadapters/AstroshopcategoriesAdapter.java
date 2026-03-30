package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAstroShopCategories;

/**
 * Created by ojas on १३/५/१६.
 */

/**
 * Created by Amit RAutela on 22/2/16.
 * this Adapter is used to set modules in the classes
 */
public class AstroshopcategoriesAdapter extends BaseAdapter {

    Integer[] moduleIconList;
    String[] moduleNameList;
    Context context;
    Typeface typeface;
    Activity activity;
    String fromWhichScreen="";

    public AstroshopcategoriesAdapter(Context context,Integer[] moduleIconList,
                                      String[] moduleNameList,Typeface typeface,Activity act){
        this.moduleIconList = moduleIconList;
        this.context=context;
        this.moduleNameList = moduleNameList;
        this.typeface = typeface;
        this.activity = act;
        this.fromWhichScreen = fromWhichScreen;
    }

    @Override
    public int getCount() {
        return moduleIconList.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {


            view = LayoutInflater.from(context).inflate(
                    R.layout.home_screen_grid_item, viewGroup, false);

        }
        try {
            ImageView rashiIcon = (ImageView) view
                    .findViewById(R.id.imageViewRashiIcon);
            TextView rashiName = (TextView) view
                    .findViewById(R.id.textViewRashiName);
            rashiIcon
                    .setImageResource(moduleIconList[position]);

            rashiName.setText(moduleNameList[position]);
            rashiName.setTypeface(typeface);
            view.setId(position);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    callModulesFromCallingMethods(position);
                }
            });
        } catch (Exception e) {
            //
        }
        return view;
    }

    /**
     * @created by : Amit Rautela
     * @created on : 2/2/16
     * @disc : This method is used to call modules from calling fragments
     */
    private void callModulesFromCallingMethods(int position){
        ActAstroShopCategories.callActivity(position);

    }

}
