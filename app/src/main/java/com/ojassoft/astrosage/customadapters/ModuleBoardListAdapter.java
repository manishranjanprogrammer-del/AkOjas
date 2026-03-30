package com.ojassoft.astrosage.customadapters;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanCategoryOnBoardItem;
import com.ojassoft.astrosage.jinterface.IModuleBoardListAdapter;

public class ModuleBoardListAdapter extends BaseAdapter {

    private Context context;

    private Typeface typeface;

    ArrayList<BeanCategoryOnBoardItem> beanCategoryOnBoardItems;

    IModuleBoardListAdapter iModuleBoardListAdapter;
    Activity activity;

    public ModuleBoardListAdapter(Context context,
                                  ArrayList<BeanCategoryOnBoardItem> beanCategoryOnBoardItems,
                                  Typeface typeface, Activity activity) {

        this.context = context;
        this.beanCategoryOnBoardItems = beanCategoryOnBoardItems;
        this.typeface = typeface;
        this.activity = activity;
        iModuleBoardListAdapter = (IModuleBoardListAdapter) this.activity;

    }

    public int getCount() {
        return this.beanCategoryOnBoardItems.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        iModuleBoardListAdapter = (IModuleBoardListAdapter) context;
        TextView textViewCategoryName1, textViewCategoryName2, textViewCategoryName3;
        LinearLayout llCategory1, llCategory2, llCategory3;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.module_board_row_item, null);
        }
        BeanCategoryOnBoardItem beanCategoryOnBoardItem = this.beanCategoryOnBoardItems
                .get(position);
        ((ImageView) convertView.findViewById(R.id.imageCategory1))
                .setImageResource(beanCategoryOnBoardItem.ImageId1);
        ((ImageView) convertView.findViewById(R.id.imageCategory2))
                .setImageResource(beanCategoryOnBoardItem.ImageId2);
        ((ImageView) convertView.findViewById(R.id.imageCategory3))
                .setImageResource(beanCategoryOnBoardItem.ImageId3);

        textViewCategoryName1 = (TextView) convertView
                .findViewById(R.id.textViewCategoryName1);
        textViewCategoryName2 = (TextView) convertView
                .findViewById(R.id.textViewCategoryName2);
        textViewCategoryName3 = (TextView) convertView
                .findViewById(R.id.textViewCategoryName3);

        textViewCategoryName1.setText(beanCategoryOnBoardItem.Title1);
        textViewCategoryName2.setText(beanCategoryOnBoardItem.Title2);
        textViewCategoryName3.setText(beanCategoryOnBoardItem.Title3);

        textViewCategoryName1.setTypeface(typeface);
        textViewCategoryName2.setTypeface(typeface);
        textViewCategoryName3.setTypeface(typeface);

        llCategory1 = (LinearLayout) convertView.findViewById(R.id.llCategory1);
        llCategory2 = (LinearLayout) convertView.findViewById(R.id.llCategory2);
        llCategory3 = (LinearLayout) convertView.findViewById(R.id.llCategory3);

        llCategory1.setTag(beanCategoryOnBoardItem.Index1);
        llCategory2.setTag(beanCategoryOnBoardItem.Index2);
        llCategory3.setTag(beanCategoryOnBoardItem.Index3);

        llCategory1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    iModuleBoardListAdapter.onSelectedCategoryBoardItem(Integer.valueOf(v.getTag().toString()));
                } catch (Exception e) {

                }

            }
        });
        llCategory2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    iModuleBoardListAdapter.onSelectedCategoryBoardItem(Integer.valueOf(v.getTag().toString()));
                } catch (Exception e) {

                }

            }
        });
        llCategory3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    iModuleBoardListAdapter.onSelectedCategoryBoardItem(Integer.valueOf(v.getTag().toString()));
                } catch (Exception e) {

                }
            }
        });

        return convertView;
    }
}
