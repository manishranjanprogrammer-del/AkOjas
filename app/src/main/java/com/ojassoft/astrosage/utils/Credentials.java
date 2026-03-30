/**
 *
 */
package com.ojassoft.astrosage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Naveen
 */
public class Credentials {

    private Context context;

    public static final String USER_IS_VERFIY = "user_verify";
    public static final String KEY = "verificationkey";

    public Credentials(Context context) {
        this.context = context;
    }

    public boolean saveisVerify(boolean log) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
                .edit();
        editor.putBoolean(USER_IS_VERFIY, log);
        return editor.commit();
    }

	/*
     * public boolean DeliveryTime(String time) { Editor editor =
	 * context.getSharedPreferences(KEY, Context.MODE_PRIVATE) .edit();
	 * editor.putString(ESTIMATE_DELIVERYTIME, time); return editor.commit(); }
	 */

    public boolean restoreisVerify() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY,
                Context.MODE_PRIVATE);
        boolean log = sharedPreferences.getBoolean(USER_IS_VERFIY, false);
        return log;
    }

    public boolean deleteCredentials() {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
                .edit();
        editor.clear();
        return editor.commit();
    }

}
