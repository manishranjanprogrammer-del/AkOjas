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
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.model.PurnimaFastData;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.FestvalDetail;
import com.ojassoft.astrosage.ui.act.uifestivaldetail.ActFestivalListView;
import com.ojassoft.astrosage.ui.act.uifestivaldetail.ActFestivalNavratraView;
import com.ojassoft.astrosage.ui.act.uifestivaldetail.ActFestivalStaticView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ojas-02 on 26/3/18.
 */

public class NavratraViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //private List monthDataList;
    Context context;
    private MyViewHolder myViewHolder;
    //LinearLayout containerLayout;
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

    ArrayList<PurnimaFastData> festList;
    ArrayList<ArrayList<PurnimaFastData>> mainList;
    ArrayList<DetailApiModel> toSendDetail;
    int langaugeCode;

    public NavratraViewAdapter(Context ctx, ArrayList<PurnimaFastData> monthDataDetail, ArrayList<DetailApiModel> arrayListObj) {
        context = ctx;
        regularTypeface = ((BaseInputActivity) ctx).regularTypeface;
        mediumTypeface = ((BaseInputActivity) ctx).mediumTypeface;
        robotoMediumTypeface = ((BaseInputActivity) ctx).robotMediumTypeface;

        festList = monthDataDetail;
        mainList = filterFestArrayList(festList);
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
        dayName = ctx.getResources().getStringArray(R.array.week_day_sunday_to_saturday_list);
        dayShortName = ctx.getResources().getStringArray(R.array.weekday_short_name);
        monthName = ctx.getResources().getStringArray(R.array.month_short_name_list_for_panchang);
        langaugeCode = ((AstrosageKundliApplication) ((BaseInputActivity) ctx).getApplication()).getLanguageCode();
        monthName = ctx.getResources().getStringArray(R.array.month_short_name_list_for_panchang);
        this.toSendDetail = arrayListObj;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navrata_view_list_item, parent, false);
        //containerLayout = (LinearLayout) view.findViewById(R.id.container_layout_navratra);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ArrayList<PurnimaFastData> festDetail = mainList.get(position);
        myViewHolder = (MyViewHolder) holder;
        PurnimaFastData bean;
        LinearLayout linearLayout = new LinearLayout(context);
        Calendar calendar = null;
        for (int i = 0; i < festDetail.size(); i++) {
            //selection = i;

            if (i == 0) {
                myViewHolder.containerLayout.removeAllViews();
            }

            child = inflater.inflate(R.layout.text_image_layout_nav, null);
            bean = (PurnimaFastData) festDetail.get(i);
            calendar = getDateObj(bean.getFestivalDate());
            String id = String.valueOf(position) + String.valueOf(i);
            child.setId(Integer.parseInt(id));

            //  myViewHolder.containerLayout.removeView(child);
            // if(child.getParent()!=null && child.getParent() == containerLayout)
            // containerLayout.removeView(child.findViewById(Integer.parseInt(id)));

            myViewHolder.containerLayout.addView(child);

            myViewHolder.festNameTextView = (TextView) child.findViewById(R.id.fest_name_text_nav);
            myViewHolder.weekDayTextView = (TextView) child.findViewById(R.id.fest_week_day_text_nav);
            myViewHolder.sepratorLayout = (LinearLayout) child.findViewById(R.id.seprator_nav);
            myViewHolder.networkImageView = (NetworkImageView) child.findViewById(R.id.image_view_nav);
            myViewHolder.addToCalender = (ImageView) child.findViewById(R.id.add_to_calender_nav);

            myViewHolder.festNameTextView.setTypeface(robotoMediumTypeface);
            myViewHolder.weekDayTextView.setTypeface(regularTypeface);

            myViewHolder.addToCalender.setId(Integer.parseInt(id));
            myViewHolder.festNameTextView.setId(Integer.parseInt(id));
            myViewHolder.networkImageView.setImageUrl(bean.getFestival_image_url(), imageLoader);
            myViewHolder.festNameTextView.setText(bean.getFestivalName());
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
                    PurnimaFastData festDetail1 = mainList.get(div).get(remainder);
                    showPopup(v, festDetail1);
                }
            });


            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    int remainder = id % 10;
                    int div = id / 10;

                    PurnimaFastData festDetail1 = mainList.get(div).get(remainder);
                    if (festDetail1.getFestival_page_view().equals("0")) {
                        Toast.makeText(context, R.string.page_unavialabe, Toast.LENGTH_SHORT).show();

                    } else if (festDetail1.getFestival_page_view().equals("1")) {
                        Intent intent = new Intent(context, FestvalDetail.class);
                        intent.putExtra("festurl", festDetail1.getFestival_url());
                        intent.putExtra("detailapi", toSendDetail);
                        context.startActivity(intent);
                    } else if (festDetail1.getFestival_page_view().equals("2")) {
                        Intent intent = new Intent(context, ActFestivalListView.class);
                        intent.putExtra("festurl", festDetail1.getFestival_url());
                        intent.putExtra("detailapi", toSendDetail);
                        context.startActivity(intent);
                    } else if (festDetail1.getFestival_page_view().equals("3")) {
                        Intent intent = new Intent(context, ActFestivalNavratraView.class);
                        intent.putExtra("festurl", festDetail1.getFestival_url());
                        intent.putExtra("detailapi", toSendDetail);
                        context.startActivity(intent);
                    } else if (festDetail1.getFestival_page_view().equals("4")) {
                        Intent intent = new Intent(context, ActFestivalStaticView.class);
                        intent.putExtra("festurl", festDetail1.getFestival_url());
                        intent.putExtra("detailapi", toSendDetail);
                        context.startActivity(intent);

                    } else {
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
        if (langaugeCode == 1) {
            myViewHolder.monthTextView.setText(monthName[calendar.get(Calendar.MONTH)]);
        } else {
            myViewHolder.monthTextView.setText(monthName[calendar.get(Calendar.MONTH)].toUpperCase());
        }

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


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView dayTextView;
        public TextView monthTextView;
        public TextView festNameTextView;
        public TextView weekDayTextView;
        public NetworkImageView networkImageView;
        public ImageView addToCalender;
        public LinearLayout sepratorLayout;
        public LinearLayout imageContainerLayout;
        public LinearLayout containerLayout;

        public MyViewHolder(View view) {
            super(view);
            dayTextView = (TextView) view.findViewById(R.id.day_text_nav);
            monthTextView = (TextView) view.findViewById(R.id.month_text_nav);
            dayTextView.setTypeface(mediumTypeface);
            monthTextView.setTypeface(regularTypeface);
            imageContainerLayout = (LinearLayout) view.findViewById(R.id.image_container_nav);
            containerLayout = (LinearLayout) view.findViewById(R.id.container_layout_navratra);
            // containerLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {


        }
    }

    @TargetApi(11)
    public void showPopup(View v, final PurnimaFastData festDetail1) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.addtocalendermenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onAddEventClicked(festDetail1.getFestivalName(), getDateObj(festDetail1.getFestivalDate()), getDateObj(festDetail1.getFestivalDate()));
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

    private ArrayList<ArrayList<PurnimaFastData>> filterFestArrayList(ArrayList<PurnimaFastData> arrayList) {
        ArrayList<ArrayList<PurnimaFastData>> festList = new ArrayList<>();
        ArrayList<PurnimaFastData> festDetailArrayList;
        for (int i = 0; i < arrayList.size(); ) {
            festDetailArrayList = new ArrayList<>();
            String dateStr1 = arrayList.get(i).getFestivalDate();
            Calendar date1 = getDateObj(dateStr1);
            for (int j = 0; j < arrayList.size(); j++) {
                String dateStr2 = arrayList.get(j).getFestivalDate();
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
