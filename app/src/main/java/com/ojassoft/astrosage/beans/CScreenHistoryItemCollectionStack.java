package com.ojassoft.astrosage.beans;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class CScreenHistoryItemCollectionStack {

    private static CScreenHistoryItemCollectionStack screenHistoryItemCollectionStack;
    private List<CBookMarkItem> userHistoryScreens = null;

    /**
     * This function is sued to add screen in history
     *
     * @param moduleId
     * @param screenId
     */
    public void addScreenInHistory(int moduleId, int screenId) {
        boolean haveScreen = false;
        for (CBookMarkItem obj : userHistoryScreens) {
            if ((obj.ModuleId == moduleId) && (obj.ScreenId == screenId))
                haveScreen = true;
        }
        if (!haveScreen) {
            userHistoryScreens.add(new CBookMarkItem(moduleId, screenId));
            ////Log.e("Added ", String.valueOf(moduleId) +" " +String.valueOf(screenId));
        }

    }

    public void removeTopItem(int moduleIndex, int screenIndex) {
        int size = userHistoryScreens.size();
        if (size > 0) {
            if (userHistoryScreens.get(size - 1).ModuleId == moduleIndex)
                userHistoryScreens.remove(size - 1);
        }

    }

    public void clearHistoryCollectionStack() {
        if (userHistoryScreens != null)
            userHistoryScreens.clear();
    }

    /**
     * This function is used to return the  collection history screen
     *
     * @return List<CBookMarkItem>
     */
    public List<CBookMarkItem> getHistory() {
        return userHistoryScreens;
    }

    private CScreenHistoryItemCollectionStack(Context context) throws Exception {
        userHistoryScreens = new ArrayList<CBookMarkItem>();
        initScreenHistoryCollection(context);
    }

    private CScreenHistoryItemCollectionStack() {
        userHistoryScreens = new ArrayList<CBookMarkItem>();
    }

    private void initScreenHistoryCollection(Context context) throws Exception {

        if (screenHistoryItemCollectionStack == null) {
            screenHistoryItemCollectionStack = new CScreenHistoryItemCollectionStack();
        }
    }

    /**
     * @return the screenHistoryItemCollection
     */
    public static CScreenHistoryItemCollectionStack getScreenHistoryItemCollection(Context context) throws Exception {
        if (screenHistoryItemCollectionStack == null) {
            new CScreenHistoryItemCollectionStack(context);
        }
        return screenHistoryItemCollectionStack;
    }


}
