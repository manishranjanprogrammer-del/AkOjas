package com.ojassoft.astrosage.customadapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.CalendarContract;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.HinduCalenderData;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.FestvalDetail;
import com.ojassoft.astrosage.ui.act.uifestivaldetail.ActFestivalListView;
import com.ojassoft.astrosage.ui.act.uifestivaldetail.ActFestivalNavratraView;
import com.ojassoft.astrosage.ui.act.uifestivaldetail.ActFestivalStaticView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HinduClanderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List monthDataList;
    Context context;
    MyViewHolder myViewHolder;
    LinearLayout containerLayout;
    LayoutInflater inflater;
    View child;
    //MonthCalenderData bean = null;
    int selection1;
    String[] dayName;
    String[] dayShortName;
    String[] monthName;
    VolleySingleton vsing;
    ImageLoader imageLoader;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    Typeface robotoMediumTypeface;
    ArrayList<DetailApiModel> toSendDetail;

    //HinduCalenderData hinduCalenderData;
    //  HinduCalenderData.MonthDataDetail monthDataDetail;
    ArrayList<HinduCalenderData.MonthDataDetail.FestDetail> festList;
    ArrayList<ArrayList<HinduCalenderData.MonthDataDetail.FestDetail>> mainList;

    public HinduClanderAdapter(Context ctx, HinduCalenderData.MonthDataDetail monthDataDetail, ArrayList<DetailApiModel> obj) {
        context = ctx;
        regularTypeface = ((BaseInputActivity) ctx).regularTypeface;
        mediumTypeface = ((BaseInputActivity) ctx).mediumTypeface;
        robotoMediumTypeface = ((BaseInputActivity) ctx).robotMediumTypeface;
        this.monthDataList = monthDataList;
        this.toSendDetail = obj;
        //this.hinduCalenderData = hinduCalenderData;
        //this.monthDataDetail = monthDataDetail;
        festList = monthDataDetail.getMonthdata();
        mainList = filterFestArrayList(festList);
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
        dayName = ctx.getResources().getStringArray(R.array.week_day_sunday_to_saturday_list);
        dayShortName = ctx.getResources().getStringArray(R.array.weekday_short_name);
        monthName = ctx.getResources().getStringArray(R.array.month_short_name_list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hindu_calender_list_item, parent, false);
        containerLayout = (LinearLayout) view.findViewById(R.id.container_layout);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ArrayList<HinduCalenderData.MonthDataDetail.FestDetail> festDetail = mainList.get(position);
        myViewHolder = (MyViewHolder) holder;
        HinduCalenderData.MonthDataDetail.FestDetail bean;
        LinearLayout linearLayout = new LinearLayout(context);
        Calendar calendar = null;
        for (int i = 0; i < festDetail.size(); i++) {
            //selection = i;

            child = inflater.inflate(R.layout.text_image_layout, null);
            bean = (HinduCalenderData.MonthDataDetail.FestDetail) festDetail.get(i);
            calendar = getDateObj(bean.getFestDate());
            String id = String.valueOf(position) + String.valueOf(i);
            child.setId(Integer.parseInt(id));
            containerLayout.addView(child);
            myViewHolder.festNameTextView = (TextView) child.findViewById(R.id.fest_name_text);
            myViewHolder.weekDayTextView = (TextView) child.findViewById(R.id.fest_week_day_text);
            myViewHolder.sepratorLayout = (LinearLayout) child.findViewById(R.id.seprator);
            myViewHolder.networkImageView = (NetworkImageView) child.findViewById(R.id.image_view);
            myViewHolder.addToCalender = (ImageView) child.findViewById(R.id.add_to_calender);

            myViewHolder.festNameTextView.setTypeface(robotoMediumTypeface);
            myViewHolder.weekDayTextView.setTypeface(regularTypeface);


            myViewHolder.addToCalender.setId(Integer.parseInt(id));
            myViewHolder.festNameTextView.setId(Integer.parseInt(id));
            myViewHolder.networkImageView.setImageUrl(bean.getFestImgUrl(), imageLoader);
            myViewHolder.festNameTextView.setText(bean.getFestName());
            myViewHolder.weekDayTextView.setText(dayName[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
            if (i == festDetail.size() - 1) {
                myViewHolder.sepratorLayout.setVisibility(View.GONE);
            } else {
                myViewHolder.sepratorLayout.setVisibility(View.VISIBLE);
            }
            myViewHolder.addToCalender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    int remainder = id % 10;
                    int div = id / 10;
                    HinduCalenderData.MonthDataDetail.FestDetail festDetail1 = mainList.get(div).get(remainder);
                    showPopup(v, festDetail1);
                }
            });
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    int remainder = id % 10;
                    int div = id / 10;

                    HinduCalenderData.MonthDataDetail.FestDetail festDetail1 = mainList.get(div).get(remainder);
                    if (festDetail1.getFestival_page_view().equals("0")) {
                        Toast.makeText(context, R.string.page_unavialabe, Toast.LENGTH_SHORT).show();

                    } else if (festDetail1.getFestival_page_view().equals("1")) {
                        Intent intent = new Intent(context, FestvalDetail.class);
                        intent.putExtra("festurl", festDetail1.getFestUrl());
                        intent.putExtra("detailapi", toSendDetail);
                        context.startActivity(intent);
                    } else if (festDetail1.getFestival_page_view().equals("2")) {
                        Intent intent = new Intent(context, ActFestivalListView.class);
                        intent.putExtra("festurl", festDetail1.getFestUrl());
                        intent.putExtra("detailapi", toSendDetail);
                        context.startActivity(intent);

                    } else if (festDetail1.getFestival_page_view().equals("3")) {
                        Intent intent = new Intent(context, ActFestivalNavratraView.class);
                        intent.putExtra("festurl", festDetail1.getFestUrl());
                        intent.putExtra("detailapi", toSendDetail);
                        context.startActivity(intent);

                    }

                    else if (festDetail1.getFestival_page_view().equals("4")) {
                        Intent intent = new Intent(context, ActFestivalStaticView.class);
                        intent.putExtra("festurl", festDetail1.getFestUrl());
                        intent.putExtra("detailapi", toSendDetail);
                        context.startActivity(intent);

                    }
                    else {
                        Toast.makeText(context, R.string.page_unavialabe, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        if (calendar.get(Calendar.DATE) < 10) {
            myViewHolder.dayTextView.setText("0" + String.valueOf(calendar.get(Calendar.DATE)));
        } else {
            myViewHolder.dayTextView.setText(String.valueOf(calendar.get(Calendar.DATE)));
        }

        myViewHolder.monthTextView.setText(dayShortName[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        if (position % 2 == 0) {
            if (Build.VERSION.SDK_INT >= 16) {
                myViewHolder.imageContainerLayout.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_down_144));
            } else {
                myViewHolder.imageContainerLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down_144));
            }
        } else {
            if (Build.VERSION.SDK_INT >= 16) {
                myViewHolder.imageContainerLayout.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_down_light_144));
            } else {
                myViewHolder.imageContainerLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down_light_144));

            }
        }
        //myViewHolder.imageView.setImageUrl(bean.getSnippet().getThumbnail().getTmedium().getUrl(), VolleySingleton.getInstance(context).getImageLoader());

    }


    @Override
    public int getItemCount() {
        return mainList == null ? 0 : mainList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTextView;
        public TextView monthTextView;
        public TextView festNameTextView;
        public TextView weekDayTextView;
        public NetworkImageView networkImageView;
        public ImageView addToCalender;
        public LinearLayout sepratorLayout;
        public LinearLayout imageContainerLayout;

        public MyViewHolder(View view) {
            super(view);
            dayTextView = (TextView) view.findViewById(R.id.day_text);
            monthTextView = (TextView) view.findViewById(R.id.month_text);
            dayTextView.setTypeface(mediumTypeface);
            monthTextView.setTypeface(regularTypeface);
            //festNameTextView = (TextView) view.findViewById(R.id.fest_name_text);
            //weekDayTextView = (TextView) view.findViewById(R.id.fest_week_day_text);
            //networkImageView = (NetworkImageView) view.findViewById(R.id.image_view);
            imageContainerLayout = (LinearLayout) view.findViewById(R.id.image_container);

        }
    }

    @TargetApi(11)
    public void showPopup(View v, final HinduCalenderData.MonthDataDetail.FestDetail festDetail1) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.addtocalendermenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onAddEventClicked(festDetail1.getFestName(), getDateObj(festDetail1.getFestDate()), getDateObj(festDetail1.getFestDate()));
                return false;
            }
        });
        popup.show();
    }

    private Calendar getDateObj(String festivalDate) {


        Calendar calendar = Calendar.getInstance();
        try {
            Date obj = new SimpleDateFormat("dd/MM/yyyy").parse(festivalDate);
            calendar.setTime(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    private ArrayList<ArrayList<HinduCalenderData.MonthDataDetail.FestDetail>> filterFestArrayList(ArrayList<HinduCalenderData.MonthDataDetail.FestDetail> arrayList) {
        ArrayList<ArrayList<HinduCalenderData.MonthDataDetail.FestDetail>> festList = new ArrayList<>();
        ArrayList<HinduCalenderData.MonthDataDetail.FestDetail> festDetailArrayList;
        for (int i = 0; i < arrayList.size(); ) {
            festDetailArrayList = new ArrayList<>();
            String dateStr1 = arrayList.get(i).getFestDate();
            Calendar date1 = getDateObj(dateStr1);
            for (int j = 0; j < arrayList.size(); j++) {
                String dateStr2 = arrayList.get(j).getFestDate();
                Calendar date2 = getDateObj(dateStr2);
                if (date1.equals(date2)) {
                    festDetailArrayList.add(arrayList.get(j));
                    i++;
                }
            }
            festList.add(festDetailArrayList);
        }
        return festList;
    }

    public void onAddEventClicked(String festName, Calendar startDate, Calendar endDate) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

       /* Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis() + 60 * 60 * 1000;*/

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(CalendarContract.Events.TITLE, festName);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "");
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        context.startActivity(intent);
    }

}