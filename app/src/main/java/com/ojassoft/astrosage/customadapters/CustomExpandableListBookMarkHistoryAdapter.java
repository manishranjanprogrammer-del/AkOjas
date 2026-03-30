package com.ojassoft.astrosage.customadapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CBookMarkItemCollection;
import com.ojassoft.astrosage.beans.CScreenHistoryItemCollection;
import com.ojassoft.astrosage.beans.SerializeAndDeserializeBeans;
import com.ojassoft.astrosage.jinterface.IHistoryCollectionScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a custom adapter for inner expended menu
 */
public class CustomExpandableListBookMarkHistoryAdapter extends BaseExpandableListAdapter
        implements ExpandableListAdapter {
    IHistoryCollectionScreen iHistoryCollectionScreen;//ADDED BY BIJENDRA ON 10-06-14
    public Context context;
    private int languageCode;
    private Typeface typeface;
    String dash = " ";
    private LayoutInflater vi;
    CExpendedMenuItems cemiBookMark = new CExpendedMenuItems();
    CExpendedMenuItems cemiHistory = new CExpendedMenuItems();
    CustomExpendedMenuItemCollection cemic = new CustomExpendedMenuItemCollection();

    private static final int GROUP_ITEM_RESOURCE = R.layout.book_marked_list_title;
    private static final int CHILD_ITEM_RESOURCE = R.layout.book_marked_item_row;
    private static final int CHILD_HISTORY_ITEM_RESOURCE = R.layout.history_item_row;


    public CustomExpandableListBookMarkHistoryAdapter(Context context, Activity activity, Typeface typeface, int languageCode) {
        this.languageCode = languageCode;
        this.typeface = typeface;
        this.context = context;
        iHistoryCollectionScreen = (IHistoryCollectionScreen) activity;//ADDED BY BIJENDRA ON 10-06-14
        vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            initMenuItems();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initMenuItems() throws Exception {

        cemiBookMark.GroupTitle = context.getApplicationContext().getResources().getString(R.string.bookMarkScreenHeading);
        cemiHistory.GroupTitle = context.getApplicationContext().getResources().getString(R.string.history_screen_heding);

        // BookMark
        String[] saAc = new String[CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.size()];
        int saAcL = saAc.length;
        dash = "-";
        //change from krutidev to unicode
        if (languageCode == CGlobalVariables.HINDI) {
            dash = "-";
        }
        for (int i = 0; i < saAcL; i++) {

            saAc[i] = getModuletitle(CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.get(i).ModuleId).trim() + dash + getScreenTitle(CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.get(i).ModuleId,
                    CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.get(i).ScreenId).trim();
        }
        for (int i = 0; i < saAcL; i++)
            cemiBookMark.GroupItems.add(saAc[i]);

        // History
        String[] saNac = new String[CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().size()];
        int saNacL = saNac.length;
        for (int i = 0; i < saNacL; i++) {
            saNac[i] = getModuletitle(CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(i).ModuleId).trim() + dash + getScreenTitle(CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(i).ModuleId,
                    CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(i).ScreenId).trim();
        }
        for (int i = 0; i < saNacL; i++)
            cemiHistory.GroupItems.add(saNac[i]);

        cemic.ExpendedMenu.add(cemiBookMark);
        cemic.ExpendedMenu.add(cemiHistory);
    }

    public String getChild(int groupPosition, int childPosition) {
        return cemic.ExpendedMenu.get(groupPosition).GroupItems.get(childPosition);

    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        if(cemic.ExpendedMenu != null && cemic.ExpendedMenu.size() > groupPosition) {
            return cemic.ExpendedMenu.get(groupPosition).GroupItems.size();
        }else {
            return 0;
        }
    }

    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        String child = getChild(groupPosition, childPosition);
        try {
            if (child != null) {
                if (groupPosition == 1) {
                    v = vi.inflate(CHILD_HISTORY_ITEM_RESOURCE, null);
                    ExpendedMenuViewHolderHistory holder = new ExpendedMenuViewHolderHistory(v);
                    holder.textModule.setText(child);
                    holder.bookMarkToggle.setChecked(CUtils.isScreenBookMarked(context, CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ModuleId, CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ScreenId));
                    holder.bookMarkToggle.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                boolean addedInBookMarkList = CUtils.bookMarkOrUnBookMarkScreen((Activity) CustomExpandableListBookMarkHistoryAdapter.this.context, CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ModuleId, CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ScreenId);
                                if (addedInBookMarkList) {
                                    CustomExpandableListBookMarkHistoryAdapter.this.cemiBookMark.GroupItems.add(getModuletitle(CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ModuleId).trim() + dash + getScreenTitle(CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ModuleId,
                                            CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ScreenId).trim());
                                    CustomExpandableListBookMarkHistoryAdapter.this.notifyDataSetChanged();
                                } else {
                                    refreshWholeMenu();
                                    CustomExpandableListBookMarkHistoryAdapter.this.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    holder.textModule.setTypeface(typeface);

                  /*  holder.textModule.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            try {

                                iHistoryCollectionScreen.onSelectHistoryitem(1, childPosition);//ADDED BY BIJENDRA ON 10-06-14
                                //DISABLED BY BIJENDRA  ON 10-06-14
                                //CUtils.goToUserBookMarkHistoryScreen(CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ModuleId, CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ScreenId,(Activity)CustomExpandableListBookMarkHistoryAdapter.this.context);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });*/

                    holder.relativeLayoutHistory.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                iHistoryCollectionScreen.onSelectHistoryitem(1, childPosition);//ADDED BY BIJENDRA ON 10-06-14
                                //DISABLED BY BIJENDRA  ON 10-06-14
                                //CUtils.goToUserBookMarkHistoryScreen(CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ModuleId, CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(childPosition).ScreenId,(Activity)CustomExpandableListBookMarkHistoryAdapter.this.context);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {
                    v = vi.inflate(CHILD_ITEM_RESOURCE, null);
                    ExpendedMenuViewHolder holder = new ExpendedMenuViewHolder(v);
                    holder.textModule.setText(child);
                    holder.imageview.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            try {
                                CUtils.removeBookMarkScreen(
                                        context, CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.get(childPosition).ModuleId,
                                        CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.get(childPosition).ScreenId);
                                //save Bookmark Collection in file system
                                SerializeAndDeserializeBeans.saveSerializedBeanObject(context, CGlobalVariables.BOOKMARK_COLLECTION_FILE_NAME, CBookMarkItemCollection.getBookMarkItemCollection(context));
                                CustomExpandableListBookMarkHistoryAdapter.this.cemiBookMark.GroupItems.remove(childPosition);
                                CustomExpandableListBookMarkHistoryAdapter.this.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    holder.textModule.setTypeface(typeface);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return v;
    }

    protected void refreshWholeMenu() throws Exception {


        cemiBookMark = new CExpendedMenuItems();
        cemiHistory = new CExpendedMenuItems();
        cemiBookMark.GroupTitle = context.getApplicationContext().getResources().getString(R.string.bookMarkScreenHeading);
        cemiHistory.GroupTitle = context.getApplicationContext().getResources().getString(R.string.history_screen_heding);

        cemic = new CustomExpendedMenuItemCollection();
        // BookMark
        String[] saAc = new String[CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.size()];
        int saAcL = saAc.length;
        dash = "-";
        if (languageCode == CGlobalVariables.HINDI) {
            dash = "&";
        }
        for (int i = 0; i < saAcL; i++) {

            saAc[i] = getModuletitle(CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.get(i).ModuleId).trim() + dash + getScreenTitle(CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.get(i).ModuleId,
                    CBookMarkItemCollection.getBookMarkItemCollection(context).UserBookMarkedScreen.get(i).ScreenId).trim();
        }
        for (int i = 0; i < saAcL; i++)
            cemiBookMark.GroupItems.add(saAc[i]);

        // History
        String[] saNac = new String[CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().size()];
        int saNacL = saNac.length;
        for (int i = 0; i < saNacL; i++) {
            saNac[i] = getModuletitle(CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(i).ModuleId).trim() + dash + getScreenTitle(CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(i).ModuleId,
                    CScreenHistoryItemCollection.getScreenHistoryItemCollection(context).getHistory().get(i).ScreenId).trim();
        }
        for (int i = 0; i < saNacL; i++)
            cemiHistory.GroupItems.add(saNac[i]);

        cemic.ExpendedMenu.add(cemiBookMark);
        cemic.ExpendedMenu.add(cemiHistory);
    }

    public String getGroup(int groupPosition) {
        return "group-" + groupPosition;
    }

    public int getGroupCount() {
        return cemic.ExpendedMenu.size();

    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View v = convertView;
        String group = null;

        long group_id = getGroupId(groupPosition);

        group = cemic.ExpendedMenu.get((int) group_id).GroupTitle;

        if (group != null) {
            v = vi.inflate(GROUP_ITEM_RESOURCE, null);
            ExpendedMenuViewHolderGroup holder = new ExpendedMenuViewHolderGroup(v);
            holder.text.setText(group);
            holder.text.setTypeface(typeface);

            if(isExpanded){
                holder.image.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_expand_less_white_36dp, null));
            }else{
                holder.image.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_expand_more_white_36dp, null));
            }


        }
        return v;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    class ExpendedMenuViewHolder {

        public TextView textModule;
        public ImageView imageview;
        public RelativeLayout relativeLayoutbookmark;


        public ExpendedMenuViewHolder(View v) {
            this.textModule = (TextView) v.findViewById(R.id.tvbookMarkedModule);
            this.imageview = (ImageView) v.findViewById(R.id.deleteItemFromList);
            this.relativeLayoutbookmark = (RelativeLayout) v.findViewById(R.id.RelativeLayout2);

        }

    }

    class ExpendedMenuViewHolderHistory {

        public TextView textModule;
        public ToggleButton bookMarkToggle;
        public RelativeLayout relativeLayoutHistory;

        public ExpendedMenuViewHolderHistory(View v) {
            this.textModule = (TextView) v.findViewById(R.id.tvRecentScreens);
            this.bookMarkToggle = (ToggleButton) v.findViewById(R.id.addRemoveFavToggle);
            this.relativeLayoutHistory=(RelativeLayout)v.findViewById(R.id.RelativeLayout1);
        }

    }

    class ExpendedMenuViewHolderGroup {

        public TextView text;
        public ImageView image;

        public ExpendedMenuViewHolderGroup(View v) {
            this.text = (TextView) v.findViewById(R.id.txtBookMarkedListTitle);
            this.image = (ImageView) v.findViewById(R.id.image);
        }

    }

    private String getModuletitle(int moduleId) {
       // moduleId++;
        moduleId = moduleId + 2;
        return context.getResources().getStringArray(R.array.module_list)[moduleId];
    }

    private String getScreenTitle(int moduleId, int screenId) {
        String screenName = "";
        switch (moduleId) {
            case CGlobalVariables.MODULE_BASIC:
                screenName = context.getResources().getStringArray(R.array.basic_module_list)[screenId];
                break;
            case CGlobalVariables.MODULE_KP:
                screenName = context.getResources().getStringArray(R.array.kpsystem_module_list)[screenId];
                break;
            case CGlobalVariables.MODULE_DASA:
                screenName = context.getResources().getStringArray(R.array.dasha_sub)[screenId];
                break;
            case CGlobalVariables.MODULE_SHODASHVARGA:
                screenName = context.getResources().getStringArray(R.array.shodasvarga_module_list)[screenId];
                break;
            case CGlobalVariables.MODULE_LALKITAB:
                screenName = context.getResources().getStringArray(R.array.lalkitab_module_list)[screenId];
                break;
            case CGlobalVariables.MODULE_PREDICTION:
                screenName = context.getResources().getStringArray(R.array.predictions_module_list)[screenId];
                break;
            case CGlobalVariables.MODULE_VARSHAPHAL:
                screenName = context.getResources().getStringArray(R.array.tajik_module_list)[screenId];
                break;
        }
        return screenName;
    }

    public class CExpendedMenuItems {
        public String GroupTitle = "";
        public List<String> GroupItems = new ArrayList<String>();

        public CExpendedMenuItems() {

        }
    }

    public class CustomExpendedMenuItemCollection {
        public List<CExpendedMenuItems> ExpendedMenu = new ArrayList<CExpendedMenuItems>();
    }
}