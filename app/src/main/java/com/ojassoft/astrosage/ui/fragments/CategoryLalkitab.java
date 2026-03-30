package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanCategoryOnBoardItem;
import com.ojassoft.astrosage.customadapters.ModuleBoardListAdapter;
import com.ojassoft.astrosage.jinterface.ICategoryLalkitab;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class CategoryLalkitab extends Fragment {

    public Integer[] moduleIconList = {
            R.drawable.ic_module_basic,
            R.drawable.ic_module_dasha,
            R.drawable.ic_module_prediction,
            R.drawable.ic_module_kp,
            R.drawable.ic_module_shodashvarga,
            R.drawable.ic_module_varshphal};
    public int[] moduleIndexList = {
            CGlobalVariables.MODULE_BASIC,
            CGlobalVariables.MODULE_DASA,
            CGlobalVariables.MODULE_PREDICTION,
            CGlobalVariables.MODULE_KP,
            CGlobalVariables.MODULE_SHODASHVARGA,
            CGlobalVariables.MODULE_VARSHAPHAL};

    private String[] moduleNameList = null;

    ICategoryLalkitab iCategoryLalkitab;
    // private Typeface _typeFace;
    Activity activity;
    boolean isAdTitleHaseBeenGivenTOTabs;
    int localAdvertismentPosition;

    public static CategoryLalkitab newInstance(boolean isAdTitleAdded, int adPosition) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isAdTitleHaseBeenGivenTOTabs", isAdTitleAdded);
        bundle.putInt("localAdvertismentPosition", adPosition);
        CategoryLalkitab appViewFragment = new CategoryLalkitab();
        appViewFragment.setArguments(bundle);
        return appViewFragment;
    }

    public CategoryLalkitab() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAdTitleHaseBeenGivenTOTabs = getArguments().getBoolean("isAdTitleHaseBeenGivenTOTabs");
        localAdvertismentPosition = getArguments().getInt("localAdvertismentPosition");
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        iCategoryLalkitab = (ICategoryLalkitab) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iCategoryLalkitab = null;
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        ListView lstCategoryPrediction = null, lstModuleBoard = null;
        //_typeFace = ((OutputMasterActivity) getActivity()).typeface;
        /*View rootView = inflater.inflate(R.layout.lay_category_list,
                container, false);*/
        try {
            int LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
            CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        }catch (Exception e){
            //
        }
        View rootView = inflater.inflate(R.layout.lay_category_list_new,
                container, false);
        lstCategoryPrediction = (ListView) rootView
                .findViewById(R.id.lstCategory);
        initCategoryList(lstCategoryPrediction);

        moduleNameList = getActivity().getResources().getStringArray(R.array.module_board_lalkitab_list);
        lstModuleBoard = (ListView) rootView.findViewById(R.id.lstModuleBoard);
        lstModuleBoard.setAdapter(new ModuleBoardListAdapter(getActivity(), getModuleBoardList(),
                ((BaseInputActivity) getActivity()).mediumTypeface, getActivity()));

        //Add advertisment in footer 10-Dec-2015
        LinearLayout llCustomAdv = (LinearLayout) rootView.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(activity, false, ((BaseInputActivity) getActivity()).mediumTypeface,"SLALK"));

        CUtils.stratchListViewFullly(lstCategoryPrediction);//TO STRACH LIST VIEW FULLY
        CUtils.stratchListViewFullly(lstModuleBoard);//TO STRACH LIST VIEW FULLY

        return rootView;
    }

    private ArrayList<BeanCategoryOnBoardItem> getModuleBoardList() {
        ArrayList<BeanCategoryOnBoardItem> itemCategoryOnBoardItems = new ArrayList<BeanCategoryOnBoardItem>();

        //OBJECT -1
        BeanCategoryOnBoardItem itemCategoryOnBoardItem1 = new BeanCategoryOnBoardItem();

        itemCategoryOnBoardItem1.ImageId1 = moduleIconList[0];
        itemCategoryOnBoardItem1.ImageId2 = moduleIconList[1];
        itemCategoryOnBoardItem1.ImageId3 = moduleIconList[2];

        itemCategoryOnBoardItem1.Title1 = moduleNameList[0];
        itemCategoryOnBoardItem1.Title2 = moduleNameList[1];
        itemCategoryOnBoardItem1.Title3 = moduleNameList[2];

        itemCategoryOnBoardItem1.Index1 = moduleIndexList[0];
        itemCategoryOnBoardItem1.Index2 = moduleIndexList[1];
        itemCategoryOnBoardItem1.Index3 = moduleIndexList[2];


        //OBJECT -2
        BeanCategoryOnBoardItem itemCategoryOnBoardItem2 = new BeanCategoryOnBoardItem();

        itemCategoryOnBoardItem2.ImageId1 = moduleIconList[3];
        itemCategoryOnBoardItem2.ImageId2 = moduleIconList[4];
        itemCategoryOnBoardItem2.ImageId3 = moduleIconList[5];

        itemCategoryOnBoardItem2.Title1 = moduleNameList[3];
        itemCategoryOnBoardItem2.Title2 = moduleNameList[4];
        itemCategoryOnBoardItem2.Title3 = moduleNameList[5];

        itemCategoryOnBoardItem2.Index1 = moduleIndexList[3];
        itemCategoryOnBoardItem2.Index2 = moduleIndexList[4];
        itemCategoryOnBoardItem2.Index3 = moduleIndexList[5];

        itemCategoryOnBoardItems.add(itemCategoryOnBoardItem1);
        itemCategoryOnBoardItems.add(itemCategoryOnBoardItem2);
        return itemCategoryOnBoardItems;
    }

    private void initCategoryList(ListView lv) {
        ArrayList<CategoryItem> itemCat = getCategoryList();
        if (itemCat.size() > 0) {
            ListviewCategoryPredictionAdapter lla = new ListviewCategoryPredictionAdapter(
                    itemCat);
            lv.setAdapter(lla);

        }
    }

    private ArrayList<CategoryItem> getCategoryList() {

        ArrayList<CategoryItem> itemCat = new ArrayList<CategoryItem>();
        String[] elements1 = null;
        String[] elements2 = null;

        if (isAdTitleHaseBeenGivenTOTabs) {
            elements1 = getElementWithAdvertisement(getActivity().getResources().getStringArray(
                    R.array.lalkitab_module_list));
        } else {
            elements1 = getActivity().getResources().getStringArray(
                    R.array.lalkitab_module_list);
        }

        elements2 = new String[elements1.length - 1];
        for (int i = 0; i < elements1.length; i++) {
            if (i > 0)
                elements2[i - 1] = elements1[i];
        }
        int size = (elements2.length / 2);
        if ((elements2.length % 2) > 0)
            size += 1;

        int index = 0;
        CategoryItem catIt;
        for (int i = 0; i < elements2.length; i++) {
            if (i < size) {
                catIt = new CategoryItem();
                catIt.index1 = index;

                catIt.title1 = elements2[index];

                if ((index + 1) < elements2.length) {
                    catIt.title2 = elements2[index + 1];
                    catIt.index2 = index + 1;

                }
                itemCat.add(catIt);
                index += 2;
            }
        }

        return itemCat;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class CategoryItem {
        public String title1 = null, title2 = null;
        public int index1 = -1, index2 = -1;
    }

    class ListviewCategoryPredictionAdapter extends BaseAdapter {

        ArrayList<CategoryItem> itemCat;

        public ListviewCategoryPredictionAdapter(ArrayList<CategoryItem> items) {
            itemCat = items;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return itemCat.size();
        }

        @Override
        public CategoryItem getItem(int position) {
            // TODO Auto-generated method stub
            return itemCat.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView row_title1, row_title2;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.category_row, null);
            }
            row_title1 = (TextView) convertView.findViewById(R.id.row_title1);
            row_title2 = (TextView) convertView.findViewById(R.id.row_title2);
            CategoryItem categoryItem = itemCat.get(position);

            if (categoryItem.index1 > -1) {
                row_title1.setVisibility(View.VISIBLE);
                row_title1.setText(categoryItem.title1);
                row_title1.setTag(categoryItem.index1);

                row_title1.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        iCategoryLalkitab.onSetectedItemCategoryLalkitab(Integer.valueOf(v.getTag().toString()));

                    }
                });
            }

            if (categoryItem.index2 > -1) {
                row_title2.setVisibility(View.VISIBLE);
                row_title2.setText(categoryItem.title2);
                row_title2.setTag(categoryItem.index2);
                row_title2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        iCategoryLalkitab.onSetectedItemCategoryLalkitab(Integer.valueOf(v.getTag().toString()));

                    }
                });
            }

            row_title1.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            row_title2.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

            return convertView;
        }

    }

    private String[] getElementWithAdvertisement(String[] element) {

        //Add 1 extra title for Advertisemnet
        String[] returnElement = new String[element.length + 1];
        boolean titleAdded = false;
        int j = 1;
        for (int i = 0; i < element.length; i++) {
            if (i == localAdvertismentPosition) {
                returnElement[i] = activity.getResources().getString(R.string.featured);
                returnElement[j] = element[i];
                titleAdded = true;
            } else if (titleAdded) {
                returnElement[j] = element[i];
            } else {
                returnElement[i] = element[i];
            }
            j++;
        }
        return returnElement;
    }


}
