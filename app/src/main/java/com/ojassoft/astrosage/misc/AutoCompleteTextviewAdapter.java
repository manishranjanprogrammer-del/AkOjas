package com.ojassoft.astrosage.misc;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.MychartActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 1/2/15.
 */
public class AutoCompleteTextviewAdapter extends ArrayAdapter<BeanHoroPersonalInfo> {

    Activity context;
    boolean isShowProgressbar = true;
    //int resource, textViewResourceId;
    List<BeanHoroPersonalInfo> items, tempItems, suggestions;
    String monthNames[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    public AutoCompleteTextviewAdapter(Context context, int resource, List<BeanHoroPersonalInfo> items, boolean isShowProgressbar) {
        super(context, resource, items);
        this.context = (Activity) context;
        this.isShowProgressbar = isShowProgressbar;
        //this.resource = resource;
        //this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<BeanHoroPersonalInfo>(items); // this makes the difference.
        suggestions = new ArrayList<BeanHoroPersonalInfo>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (context != null) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.autotext_list_item_layout, parent, false);
            }
            if (items != null && items.size() > position) {
                final BeanHoroPersonalInfo beanHoroPersonalInfo = items.get(position);
                if (beanHoroPersonalInfo != null) {
                    BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
                    BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();
                    LinearLayout progressbar = (LinearLayout) view.findViewById(R.id.progressbar);
                    if (CUtils.isConnectedWithInternet(context) && CUtils.isUserLogedIn(context) && position == 0 && isShowProgressbar) {
                        progressbar.setVisibility(View.VISIBLE);
                    } else {
                        progressbar.setVisibility(View.GONE);
                    }
                    TextView lblName = (TextView) view.findViewById(R.id.personName);
                    TextView dob = (TextView) view.findViewById(R.id.birthDateTime);
                    // TextView dop = (TextView) view.findViewById(R.id.birthPlace);
                    ImageView cloudImageView = (ImageView) view.findViewById(R.id.cloud_imageview);
                    if (beanHoroPersonalInfo.getOnlineChartId().equals("0") || beanHoroPersonalInfo.getOnlineChartId().equals("") || beanHoroPersonalInfo.getOnlineChartId().equals("-1")) {
                        cloudImageView.setVisibility(View.GONE);
                    } else {
                        cloudImageView.setVisibility(View.VISIBLE);
                    }
                    TextView cancelButton = (TextView) view.findViewById(R.id.cancelButton);
                    LinearLayout llMyChart = (LinearLayout) view.findViewById(R.id.container_lay);
                    //lblName.setTypeface(((BaseInputActivity) context).mediumTypeface);
                    // dob.setTypeface(((BaseInputActivity) context).regularTypeface);
                    // dop.setTypeface(((BaseInputActivity) context).regularTypeface);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String kundliId = "";
                            boolean isLocalKundli;
                            if (beanHoroPersonalInfo.getOnlineChartId().equals("-1") || beanHoroPersonalInfo.getOnlineChartId().equals("")) {
                                kundliId = String.valueOf(beanHoroPersonalInfo.getLocalChartId());
                                isLocalKundli = true;
                            } else {
                                kundliId = beanHoroPersonalInfo.getOnlineChartId();
                                isLocalKundli = false;
                            }
                            if (context instanceof HomeInputScreen) {
                                ((HomeInputScreen) context).searchBirthDetailsFragment.
                                        confirmDeleteToKundli(beanHoroPersonalInfo, true, isLocalKundli, false);
                            } else if (context instanceof HomeMatchMakingInputScreen) {
                                ((HomeMatchMakingInputScreen) context).searchBirthDetailsFragment.
                                        confirmDeleteToKundli(beanHoroPersonalInfo, true, isLocalKundli, false);
                            } else {
                                ((MychartActivity) context).searchBirthDetailsFragment.
                                        confirmDeleteToKundli(beanHoroPersonalInfo, true, isLocalKundli, false);
                            }

                        }
                    });

                    llMyChart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int selectedModule = 0;
                            int selectedSubModule = 0;
                            if (context instanceof HomeInputScreen) {
                                selectedModule = ((HomeInputScreen) context).SELECTED_MODULE;
                                selectedSubModule = ((HomeInputScreen) context).SELECTED_SUB_SCREEN;
                            } else {
                                // selectedModule = ((HomeMatchMakingInputScreen) context).SELECTED_MODULE;
                                selectedModule = 0;
                            }
                            //editTextSearchKundli.showDropDown();
                            if (context instanceof HomeMatchMakingInputScreen) {
                                int isboyOrGirlKundli = HomeMatchMakingInputScreen.PERSON1;

                                if (HomeMatchMakingInputScreen.collectDataForPerson != -1) {
                                    isboyOrGirlKundli = HomeMatchMakingInputScreen.collectDataForPerson;
                                } else {

                                    if (beanHoroPersonalInfo != null) {
                                        String gender = beanHoroPersonalInfo.getGender();
                                        if (gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("Male")) {
                                            isboyOrGirlKundli = HomeMatchMakingInputScreen.PERSON1;
                                        } else {
                                            isboyOrGirlKundli = HomeMatchMakingInputScreen.PERSON2;
                                        }
                                    }
                                }

                                (((HomeMatchMakingInputScreen) context).searchBirthDetailsFragment).editRecentKundli(beanHoroPersonalInfo, isboyOrGirlKundli);
                            } else if (context instanceof MychartActivity) {
                                (((MychartActivity) context).searchBirthDetailsFragment).selectPersonalKundali(context, beanHoroPersonalInfo);
                            } else {

                                CUtils.saveKundliInPreference(context, beanHoroPersonalInfo);

                                if ((context instanceof HomeInputScreen) && ((HomeInputScreen) context).ASK_QUESTION_QUERY_DATA) {
                                    CalculateKundli kundli = new CalculateKundli(beanHoroPersonalInfo, false, context, ((BaseInputActivity) context).regularTypeface, selectedModule, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, selectedSubModule);
                                    kundli.calculate();
                                } else {
                                    CalculateKundli kundli = new CalculateKundli(beanHoroPersonalInfo, true, context, ((BaseInputActivity) context).regularTypeface, selectedModule, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, selectedSubModule);
                                    kundli.calculate();
                                }

                                CUtils.googleAnalyticSendWitPlayServie(context,
                                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SEARCH_KUNDLI, null);
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SEARCH_KUNDLI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            }
                        }
                    });


                    if (lblName != null && beanPlace != null && beanDateTime != null && beanDateTime.getMonth() >= 0 && beanDateTime.getMonth() < 12) {
                        String state = beanPlace.getState(), country = beanPlace.getCountryName();
                        String dobstr = beanDateTime.getDay() + "-" + monthNames[beanDateTime.getMonth()] + "-" + beanDateTime.getYear();
                        String dotStr = beanDateTime.getHour() + ":" + beanDateTime.getMin() + ":" + beanDateTime.getSecond();
                        String dopStr = beanPlace.getCityName() /*+ "," + state + "," + country*/;
                        lblName.setText(beanHoroPersonalInfo.getName());
                        dob.setText(dobstr + ", " + dotStr + ", " + dopStr);
                    }
                }
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((BeanHoroPersonalInfo) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (BeanHoroPersonalInfo people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<BeanHoroPersonalInfo> filterList = (ArrayList<BeanHoroPersonalInfo>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (BeanHoroPersonalInfo people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public void addItem(ArrayList<BeanHoroPersonalInfo> arrayList) {
        for (int j = 0; j < arrayList.size(); j++) {
            boolean isAddToList = true;
            for (int i = 0; i < items.size(); i++) {
                if (arrayList.get(j).getOnlineChartId().equals(items.get(i).getOnlineChartId())) {
                    isAddToList = false;
                    break;
                }
            }
            if (isAddToList) {
                items.add(arrayList.get(j));
            }
        }

        tempItems = new ArrayList<BeanHoroPersonalInfo>(items);
        isShowProgressbar = false;
        notifyDataSetChanged();
    }

    /*public void clearData() {
        items.clear();
        tempItems.clear();
    }*/

    public void replaceItem(ArrayList<BeanHoroPersonalInfo> arrayList) {
        for (int i = 0; i < items.size(); i++) {
            items.remove(i);
            tempItems.remove(i);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            items.add(arrayList.get(i));
        }
        tempItems = new ArrayList<BeanHoroPersonalInfo>(items);
        isShowProgressbar = true;
        notifyDataSetChanged();
    }

    public void deleteKundli(long kundliid, boolean isLocalKundli) {
        if (isLocalKundli) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getLocalChartId() == kundliid) {
                    items.remove(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getOnlineChartId().equals(String.valueOf(kundliid))) {
                    items.remove(i);
                    break;
                }
            }
        }

        tempItems = new ArrayList<BeanHoroPersonalInfo>(items);
        isShowProgressbar = false;
        notifyDataSetChanged();
    }

    public BeanHoroPersonalInfo getClickedItem(int position) {
        return items.get(position);
    }


    @Override
    public int getCount() {
        return items.size();
    }

    public List<BeanHoroPersonalInfo> getItems() {
        return items;
    }
}