package com.ojassoft.astrosage.varta.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class CustomTypefacesForGujrati {
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface get(Context c, String name){
		synchronized(cache){
			if(!cache.containsKey(name)){				
				Typeface t = Typeface.createFromAsset(c.getAssets(),	"fonts/Roboto-Regular.ttf");
				cache.put(name, t);
			}
			return cache.get(name);
		}
	}
}
