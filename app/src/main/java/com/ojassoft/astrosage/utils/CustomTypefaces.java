package com.ojassoft.astrosage.utils;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

/**
 * This is a custom class fo type face
 *
 * @author Bijendra (17-oct-14)
 */

public class CustomTypefaces {

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c, String name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(c.getAssets(), "fonts/K045.otf");
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }


}
