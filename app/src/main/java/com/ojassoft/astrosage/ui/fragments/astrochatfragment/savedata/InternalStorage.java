package com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InternalStorage {

    public InternalStorage() {
    }

    public synchronized static void writeObject(Context context, Object object)  {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        CUtils.saveStringData(context, CGlobalVariables.CHATWITHASTROLOGER,json);
    }

    public static Object readObject(Context context, String key)   {
        Gson gson = new Gson();
        Type type = new TypeToken<List<MessageDecode>>(){}.getType();
        String json= CUtils.getStringData(context, CGlobalVariables.CHATWITHASTROLOGER, null);
        Log.e("TestAskAQ","json= "+json);
        ArrayList<MessageDecode> obj = null;
        try {
            obj = (ArrayList<MessageDecode>) gson.fromJson(json, type);
        }catch (Exception e){
            Log.e("TestAskAQ", "Exception=" + e);
            CUtils.saveStringData(context, CGlobalVariables.CHATWITHASTROLOGER,"");
        }
        return obj;
    }
}