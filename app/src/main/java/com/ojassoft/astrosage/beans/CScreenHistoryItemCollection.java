package com.ojassoft.astrosage.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ojassoft.astrosage.utils.CGlobalVariables;

import android.content.Context;

/**
 * This class is used to store out put screen history opened by user
 */
public class CScreenHistoryItemCollection implements Serializable {

    private static final long serialVersionUID = 1L;
    private static CScreenHistoryItemCollection screenHistoryItemCollection;
    private static final int HISTORY_SIZE = 5;// SIZE OF HISTORY
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
                haveScreen = false;
        }
        if (!haveScreen) {
            if (userHistoryScreens.size() >= HISTORY_SIZE)
                userHistoryScreens.remove(0);

            userHistoryScreens.add(new CBookMarkItem(moduleId, screenId));
        }

    }

    /**
     * This function is used to return the  collection history screen
     *
     * @return List<CBookMarkItem>
     */
    public List<CBookMarkItem> getHistory() {
        return userHistoryScreens;
    }

    private CScreenHistoryItemCollection(Context context) throws Exception {
        userHistoryScreens = new ArrayList<CBookMarkItem>();
        initScreenHistoryCollection(context);
    }

    private CScreenHistoryItemCollection() {
        userHistoryScreens = new ArrayList<CBookMarkItem>();
    }

    private void initScreenHistoryCollection(Context context) throws Exception {
        screenHistoryItemCollection = (CScreenHistoryItemCollection) SerializeAndDeserializeBeans.getDeSerializedBeanObject(context, CGlobalVariables.SCREEN_HISTORY_COLLECTION_FILE_NAME);
        if (screenHistoryItemCollection == null) {
            screenHistoryItemCollection = new CScreenHistoryItemCollection();
        }
    }

    /**
     * @return the screenHistoryItemCollection
     */
    public static CScreenHistoryItemCollection getScreenHistoryItemCollection(Context context) throws Exception {
        if (screenHistoryItemCollection == null) {
            new CScreenHistoryItemCollection(context);
        }
        return screenHistoryItemCollection;
    }

}
