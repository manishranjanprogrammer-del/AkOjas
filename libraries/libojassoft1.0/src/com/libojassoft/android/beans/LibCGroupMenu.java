package com.libojassoft.android.beans;

import java.util.ArrayList;
import java.util.List;

public class LibCGroupMenu {
	List<LibCSubMenu> _groupMenu=new ArrayList<LibCSubMenu>();
	public LibCGroupMenu()
	{
		_groupMenu.clear();
	}
	public void addSubMenu(LibCSubMenu cSubMenu)
	{
		_groupMenu.add(cSubMenu);
		
	}
	public List<LibCSubMenu> getGroupMenu()
	{
		return _groupMenu;
		
	}
	public LibCSubMenu getGroupMenuItem(int index)
	{
		return _groupMenu.get(index);
	}

}
