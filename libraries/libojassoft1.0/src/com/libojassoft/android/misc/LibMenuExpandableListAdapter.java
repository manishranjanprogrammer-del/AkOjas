package com.libojassoft.android.misc;
 


import com.libojassoft.android.R;
import com.libojassoft.android.beans.LibCGroupMenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;

public class LibMenuExpandableListAdapter extends BaseExpandableListAdapter implements ExpandableListAdapter  {
	public Context context;	
    private LayoutInflater vi;
   
    private static final int GROUP_ITEM_RESOURCE = R.layout.lib_menu_group_item;
    private static final int CHILD_ITEM_RESOURCE = R.layout.lib_menu_child_item;
    LibCGroupMenu _cGroupMenu;
    Typeface _customFontType;
   
    public LibMenuExpandableListAdapter(Context context, Activity activity,LibCGroupMenu cGroupMenu,Typeface customFontType)
    {
        this.context = context;
        vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _cGroupMenu=cGroupMenu;   
        _customFontType=customFontType;
       
    }
        
   
    public String getChild(int groupPosition, int childPosition) {
       return _cGroupMenu.getGroupMenuItem(groupPosition).getGroupMenuTitles()[childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
      	 return _cGroupMenu.getGroupMenuItem(groupPosition).getGroupMenuTitles().length;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        String child = getChild(groupPosition, childPosition);
             
        if (child != null) {
            v = vi.inflate(CHILD_ITEM_RESOURCE, null);
            CustomViewHolder holder = new CustomViewHolder(v);
            holder.text.setText(Html.fromHtml(child));
            holder.text.setTypeface(_customFontType);
        }
        return v;
    }

    public String getGroup(int groupPosition) {
        return "group-" + groupPosition;
    }

    public int getGroupCount() {
       // return data.length;
    	 return _cGroupMenu.getGroupMenu().size();
    }


    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    public String  getGroupTitle(int groupPosition) {
        //return groupPosition;
    	return _cGroupMenu.getGroupMenuItem(groupPosition).getGroupTitle();
    }


    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        String group = null;        
       
        group=getGroupTitle(groupPosition);
       
        if (group != null) {
            v = vi.inflate(GROUP_ITEM_RESOURCE, null);
            CustomViewHolder holder = new CustomViewHolder(v);
            holder.text.setText(Html.fromHtml(group));
            holder.text.setTypeface(_customFontType);
           
        }
        return v;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
    	
        return true;
    }


    public boolean hasStableIds() {
        return true;
    }
  
    
} 