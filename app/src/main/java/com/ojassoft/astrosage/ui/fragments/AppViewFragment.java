package com.ojassoft.astrosage.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.ICategoryPrediction;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customviews.predictions.WebViewPredictions;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class AppViewFragment extends Fragment {
    private View view = null;
    int moduleId;
    int subModuleId;
    ICategoryPrediction iCategoryPrediction;
    //private Typeface _typeFace;
    Activity activity;
    boolean isAdTitleHaseBeenGivenTOTabs;
    int localAdvertismentPosition;

    public static AppViewFragment newInstance(int moduleId, int subModuleId, boolean isAdTitleAdded, int adPosition) {
        AppViewFragment appViewFragment = new AppViewFragment();

        Bundle args = new Bundle();
        args.putInt("moduleId", moduleId);
        args.putInt("subModuleId", subModuleId);
        args.putBoolean("isAdTitleHaseBeenGivenTOTabs", isAdTitleAdded);
        args.putInt("localAdvertismentPosition", adPosition);
        appViewFragment.setArguments(args);

        return appViewFragment;
    }

    public AppViewFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        iCategoryPrediction = (ICategoryPrediction) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iCategoryPrediction = null;
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        moduleId = getArguments().getInt("moduleId", 0);
        subModuleId = getArguments().getInt("subModuleId", 0);
        isAdTitleHaseBeenGivenTOTabs = getArguments().getBoolean("isAdTitleHaseBeenGivenTOTabs");
        localAdvertismentPosition = getArguments().getInt("localAdvertismentPosition");
        try {
            int LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
            CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        }catch (Exception e){
            //
        }

        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.setFillViewport(true);
        scrollView.setPadding(0,0,0, 0);
        scrollView.setClipToPadding(false);
        scrollView.setFocusable(false);
        scrollView.setFocusableInTouchMode(false);
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        //scrollView.setBackgroundColor(getResources().getColor(R.color.white));
        LinearLayout v = new LinearLayout(getActivity());
        //v.setPadding(16,16,16,16);
        v.setBackgroundColor(getActivity().getColor(R.color.white));
        v.setVerticalScrollBarEnabled(true);
        v.setOrientation(LinearLayout.VERTICAL);
        v.setId("VI".hashCode());
        int bottomPaddingInPixel = CGenerateAppViews.convertDpToPixel(82,getContext());
        v.setPadding(0,0,0,bottomPaddingInPixel);

        if (view == null) {
            try {

                view = CGenerateAppViews.getViewFor(moduleId, subModuleId, activity, ((BaseInputActivity) getActivity()).mediumTypeface,
                        ((OutputMasterActivity) getActivity()).chart_Style, ((BaseInputActivity) getActivity()).LANGUAGE_CODE,
                        ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, 0);

            } catch (Exception e) {
                view = new TextView(getActivity());
                ((TextView) view).setText(e.getMessage());
                //Toast.makeText(getActivity(),"Exception---"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        if (view != null) {
            if (view.getTag() != null) {

                try {
                    v.addView(new WebViewPredictions(getActivity(), view.getTag().toString()));
                } catch (Exception e) {
                    view = new TextView(getActivity());
                    ((TextView) view).setText(e.getMessage());
                    v.addView(view);
                }

            } else {
                v.addView(view);
            }
        }

        if (moduleId == CGlobalVariables.MODULE_BASIC && subModuleId == 1) {
            LinearLayout divider = new LinearLayout(getActivity());
            divider.setBackgroundColor(getActivity().getResources().getColor(android.R.color.darker_gray));
            v.addView(divider);
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) divider.getLayoutParams();
            layoutParams1.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams1.height = 1;
            divider.setLayoutParams(layoutParams1);

            ListView ls = new ListView(getActivity());
            // v.addView(ls);
            //initCategoryList(ls);
            //LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) ls.getLayoutParams();
            //layoutParams2.width = LinearLayout.LayoutParams.MATCH_PARENT;
            //layoutParams2.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            //ls.setLayoutParams(layoutParams2);
            //stratchListViewFullly(ls);
        }
        scrollView.addView(v);
        return scrollView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    public static void stratchListViewFullly(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            return;
        }
        // set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 2;
        }
        // setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + 60;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        myListView.setLayoutParams(params);
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
                    R.array.predictions_module_list));
        } else {
            elements1 = getActivity().getResources().getStringArray(
                    R.array.predictions_module_list);
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
                row_title1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String str = ((TextView) v).getText().toString();
                        int index = Integer.valueOf(v.getTag().toString());
                        //Toast.makeText(activity, "index-" + str, Toast.LENGTH_SHORT).show();
                        // iCategoryPrediction.onSetectedItemCategoryPrediction(Integer.valueOf(v.getTag().toString()));
                        ((OutputMasterActivity) activity).moduleNavigate(CGlobalVariables.MODULE_PREDICTION, str);
                    }
                });
            }

            if (categoryItem.index2 > -1) {
                row_title2.setVisibility(View.VISIBLE);
                row_title2.setText(categoryItem.title2);
                row_title2.setTag(categoryItem.index2);
                row_title2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //iCategoryPrediction.onSetectedItemCategoryPrediction(Integer.valueOf(v.getTag().toString()));
                        String str = ((TextView) v).getText().toString();
                        int index = Integer.valueOf(v.getTag().toString());
                        // Toast.makeText(activity, "index-" + str, Toast.LENGTH_SHORT).show();
                        ((OutputMasterActivity) activity).moduleNavigate(CGlobalVariables.MODULE_PREDICTION, str);
                    }
                });
            }

            row_title1.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            row_title2.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

            return convertView;
        }

    }

    class CategoryItem {
        public String title1 = null, title2 = null;
        public int index1 = -1, index2 = -1;
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

    public void callMethodTextView1(Context context) {
        if ( context != null ) {
            //Log.e("SAN ", " callMethodTextView1  isAdTitleHaseBeenGivenTOTabs => " + isAdTitleHaseBeenGivenTOTabs );
            if ( ((OutputMasterActivity) context).isAdTitleHaseBeenGivenTOTabs ) {
                ((OutputMasterActivity) context).onSetectedItemCategoryBasic(CGlobalVariables.SUB_MODULE_BASIC_PLANETS);
            } else {
                ((OutputMasterActivity) context).onSetectedItemCategoryBasic(CGlobalVariables.SUB_MODULE_BASIC_PLANETS - 1);
            }

        }
    }

    public void callMethodTextView2(Context context) {
        if ( context != null ) {
            ((OutputMasterActivity) context).onSelectedCategoryBoardItem(1);
        }
    }

    public void callMethodTextView3(Context context) {
        if ( context != null ) {
            ((OutputMasterActivity) context).onSelectedCategoryBoardItem(2);
        }
    }

    public void callMethodTextView4(Context context) {
        if ( context != null ) {
            ((OutputMasterActivity) context).onSelectedCategoryBoardItem(3);
        }
    }

    public void callMethodTextView5(Context context) {
        if ( context != null ) {
            ((OutputMasterActivity) context).onSelectedCategoryBoardItem(4);
        }
    }

    public void callMethodTextView6(Context context) {
        if ( context != null ) {
            ((OutputMasterActivity) context).onSelectedCategoryBoardItem(5);
        }
    }

    public void callMethodTextView7(Context context) {
        if ( context != null ) {
            ((OutputMasterActivity) context).onSelectedCategoryBoardItem(6);
        }
    }

    public void callMethodTextView8(Context context) {
        if ( context != null ) {
            ((OutputMasterActivity) context).onSelectedCategoryBoardItem(7);
        }
    }

    public void callMethodTextView9(Context context) {
        if ( context != null ) {
            //Log.e("SAN ", " callMethodTextView9  ((OutputMasterActivity) activity) isAdTitleHaseBeenGivenTOTabs => " + ((OutputMasterActivity) activity).isAdTitleHaseBeenGivenTOTabs );
            if ( ((OutputMasterActivity) context).isAdTitleHaseBeenGivenTOTabs ) {
                ((OutputMasterActivity) context).onSetectedItemCategoryBasic(CGlobalVariables.SUB_MODULE_BASIC_GOACHER);
            } else {
                ((OutputMasterActivity) context).onSetectedItemCategoryBasic(CGlobalVariables.SUB_MODULE_BASIC_GOACHER - 1);
            }

        }
    }

    public void callMethodForRedirection(Activity activity, int moduleId, int subModuleId) {
        if ( activity != null ) {
            if ( ((OutputMasterActivity) activity).isAdTitleHaseBeenGivenTOTabs ) {
                ((OutputMasterActivity) activity).onSelectedCategoryBoardItem(moduleId, subModuleId + 1);
            } else {
                ((OutputMasterActivity) activity).onSelectedCategoryBoardItem(moduleId, subModuleId);
            }
        }
    }



}
