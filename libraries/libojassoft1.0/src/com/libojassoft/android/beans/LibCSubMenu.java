package com.libojassoft.android.beans;

import java.util.ArrayList;
import java.util.List;

public class LibCSubMenu {
	
	String _groupTitle="";
	List<String> _menu=new ArrayList<String>();
	
	public LibCSubMenu()
	{
		_menu.clear();
	}
		
	public void setGroupTitle(String groupTitle)
	{
		_groupTitle=groupTitle;
	}
	public String getGroupTitle()
	{
		return _groupTitle;
	}
	public void setGroupMenuTitle(String menuTitle)
	{
		_menu.add(menuTitle);
	}
	public String[] getGroupMenuTitles()
	{
		return getMenuTitles();
	}
	
	private String[] getMenuTitles()
	{
		String[] _title=new String[_menu.size()];
		if(_title.length>0)
		{
			for(int i=0;i<_menu.size();i++)			
				_title[i]=_menu.get(i);
		}	
		else
			return null;
		
		return _title;
		
		
	}
	

}
