package com.ojassoft.astrosage.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * @author Hukum
 */
public class CBookMarkItemCollection implements Serializable {

    private static final long serialVersionUID = 1L;
    private static CBookMarkItemCollection bookMarkItemCollection;
    public List<CBookMarkItem> UserBookMarkedScreen = null;

    private CBookMarkItemCollection(Context context) throws Exception {
        UserBookMarkedScreen = new ArrayList<CBookMarkItem>();
        initBookmarkCollection(context);
    }

    public CBookMarkItemCollection() {
        UserBookMarkedScreen = new ArrayList<CBookMarkItem>();
    }

    private void initBookmarkCollection(Context context) throws Exception {
        bookMarkItemCollection = (CBookMarkItemCollection) SerializeAndDeserializeBeans.getDeSerializedBeanObject(context, CGlobalVariables.BOOKMARK_COLLECTION_FILE_NAME);
        if (bookMarkItemCollection == null) {
            bookMarkItemCollection = new CBookMarkItemCollection();
        }
    }

    /**
     * @return the bookMarkItemCollection
     */
    public static CBookMarkItemCollection getBookMarkItemCollection(Context context) throws Exception {

        if (bookMarkItemCollection == null) {
            new CBookMarkItemCollection(context);
        }
        return bookMarkItemCollection;
    }

}
