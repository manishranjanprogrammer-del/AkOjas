package com.ojassoft.astrosage.misc;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by ojas on १८/१/१९.
 */

public class ActivityDetailWithExtraData {
    Class activity;
    ArrayList<String> keys;
    ArrayList values;
    //String val2;
    public ActivityDetailWithExtraData(Class activity, ArrayList<String> key, ArrayList val) {
        this.activity = activity;
        this.keys = key;
        this.values = val;
    }
    public ActivityDetailWithExtraData() {

    }

    public Class getActivity() {
        return activity;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public ArrayList getValues() {
        return values;
    }


  /*  public ActivityDetailWithExtraData(Activity activity, String key, String val) {
        this.activity = activity;
        this.key = key;
        this.val2 = val;
    }*/


}
