package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.fragment.app.FragmentManager;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.DetailApiModel;
import com.ojassoft.astrosage.model.PurnimaFastData;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.FestvalDetail;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ojas-02 on 15/1/18.
 */

public class PurnimaFastAdapter extends RecyclerView.Adapter<PurnimaFastAdapter.ViewHolder> {

    private List<PurnimaFastData> _nameList;
    private Context cxt;
    private PurnimaFastData itemdetails;
    FragmentManager fm;
    int langCode;
    ArrayList<DetailApiModel> toSendDetail;

    public PurnimaFastAdapter(Context context, FragmentManager fragmentManager, List<PurnimaFastData> _nameList, int langCode, ArrayList<DetailApiModel> toSendDetail) {

        this.cxt = context;
        this._nameList = new ArrayList<PurnimaFastData>();
        this._nameList = _nameList;
        this.langCode = langCode;
        this.toSendDetail=toSendDetail;

        fm = fragmentManager;

    }

    @Override
    public PurnimaFastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purnima_fast_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        try {

            itemdetails = new PurnimaFastData();
            itemdetails = _nameList.get(position);
            String[] separated = itemdetails.getFestivalDate().split("/");
            ArrayList<Integer> dayandMonth = CUtils.returDayandMonth(itemdetails.getFestivalDate());

            String[] dayName = cxt.getResources().getStringArray(R.array.week_day_sunday_to_saturday_list);
            String[] monthName = cxt.getResources().getStringArray(R.array.month_short_name_list_for_panchang);

            viewHolder.festival_name.setText(itemdetails.getFestivalName());
            viewHolder.festival_date.setText(dayName[(dayandMonth.get(0)) - 1]);
            if (langCode == 1) {
                viewHolder.festival_month.setText(monthName[dayandMonth.get(1)]);
            } else {
                viewHolder.festival_month.setText(monthName[dayandMonth.get(1)].toUpperCase());
            }

            viewHolder.festival_day.setText(separated[0]);
            viewHolder.img_info.setTag(position);
            viewHolder.cardView.setTag(position);

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PurnimaFastData fastData=_nameList.get(position);

                    if (fastData.getFestival_page_view().equals("0")) {
                        Toast.makeText(cxt, R.string.page_unavialabe_vrat, Toast.LENGTH_SHORT).show();

                    } else if (fastData.getFestival_page_view().equals("1")) {
                        Intent intent = new Intent(cxt, FestvalDetail.class);
                        intent.putExtra("festurl", fastData.getFestival_url());
                        intent.putExtra("detailapi", toSendDetail);
                        intent.putExtra("isvrat", true);

                        cxt.startActivity(intent);
                    }

                    /*else if (fastData.getFestival_page_view().equals("2")) {
                        Intent intent = new Intent(cxt, ActFestivalListView.class);
                        intent.putExtra("festurl", fastData.getFestival_url());
                        intent.putExtra("detailapi", toSendDetail);
                        cxt.startActivity(intent);
                    }
                    else if (fastData.getFestival_page_view().equals("3")) {
                        Intent intent = new Intent(cxt, ActFestivalNavratraView.class);
                        intent.putExtra("festurl", fastData.getFestival_url());
                        intent.putExtra("detailapi", toSendDetail);
                        cxt.startActivity(intent);
                    }

                    else if (fastData.getFestival_page_view().equals("4")) {
                        Intent intent = new Intent(cxt, ActFestivalStaticView.class);
                        intent.putExtra("festurl", fastData.getFestival_url());
                        intent.putExtra("detailapi", toSendDetail);
                        cxt.startActivity(intent);

                    } */else {
                        Toast.makeText(cxt, R.string.page_unavialabe_vrat, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            viewHolder.img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemdetails = _nameList.get((Integer) v.getTag());
                    PopupMenu popup = new PopupMenu(cxt, viewHolder.img_info);

                    popup.inflate(R.menu.fast_menu);


                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    CUtils.onAddEventClicked(cxt, itemdetails.getFestivalName(), getDateObj(itemdetails.getFestivalDate()), getDateObj(itemdetails.getFestivalDate()));

                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });


           /* if (getItemCount() - 1 == position) {
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 25);
                viewHolder.cardView.setLayoutParams(buttonLayoutParams);
            } else {
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 0);
                viewHolder.cardView.setLayoutParams(buttonLayoutParams);
            }*/

            //  viewHolder.setIsRecyclable(false);

        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, "EXCEP", Toast.LENGTH_LONG).show();
        }


        if (position % 2 == 0) {
            if (Build.VERSION.SDK_INT >= 16) {
                viewHolder.imageLayout.setBackground(cxt.getResources().getDrawable(R.drawable.ic_arrow_down_144));
            }
            else {
                viewHolder.imageLayout.setBackgroundDrawable(cxt.getResources().getDrawable(R.drawable.ic_arrow_down_144));
            }
        } else {
            if (Build.VERSION.SDK_INT>=16) {
                viewHolder.imageLayout.setBackground(cxt.getResources().getDrawable(R.drawable.ic_arrow_down_light_144));
            }
            else {
                viewHolder.imageLayout.setBackgroundDrawable(cxt.getResources().getDrawable(R.drawable.ic_arrow_down_light_144));

            }
        }

    }


    @Override
    public int getItemCount() {
        int size = _nameList.size();
        //Log.e("Size=" + size);
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public TextView festival_name;
        public TextView festival_date, festival_day, festival_month;
        public ImageView img_info;
        public LinearLayout imageLayout;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view_purnima);
            festival_name = (TextView) itemLayoutView.findViewById(R.id.text_festival_name);
            festival_date = (TextView) itemLayoutView.findViewById(R.id.text__featival_date);
            festival_name.setTypeface(((BaseInputActivity) cxt).robotMediumTypeface);
            //festival_date.setTypeface(((BaseInputActivity) cxt).robotRegularTypeface);
            img_info = (ImageView) itemLayoutView.findViewById(R.id.img_info_fetival);
            festival_day = (TextView) itemLayoutView.findViewById(R.id.day_text_purnima);
            festival_month = (TextView) itemLayoutView.findViewById(R.id.month_text_purnima);
            festival_date.setTypeface(CUtils.getRobotoFont(cxt, langCode, CGlobalVariables.regular));
            festival_month.setTypeface(CUtils.getRobotoFont(cxt, langCode, CGlobalVariables.regular));
            imageLayout = (LinearLayout) itemLayoutView.findViewById(R.id.dateLayout);


        }
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

}
