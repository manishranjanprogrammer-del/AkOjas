package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.ScheduleLiveAstroModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class SchedulesLiveAstroAdapter extends RecyclerView.Adapter<SchedulesLiveAstroAdapter.MyViewHolder> {

    Context context;
    ArrayList<ScheduleLiveAstroModel> scheduleLiveAstroModels;
    int showAllAstro;

    public SchedulesLiveAstroAdapter(Context context, ArrayList<ScheduleLiveAstroModel> scheduleLiveAstroModels, int showAllAstro) {
        this.context = context;
        this.scheduleLiveAstroModels = scheduleLiveAstroModels;
        this.showAllAstro = showAllAstro;
    }

    public SchedulesLiveAstroAdapter() {
        //
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_live_astrologer_all, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (scheduleLiveAstroModels != null && scheduleLiveAstroModels.size() > position) {
            ScheduleLiveAstroModel scheduleLiveAstroModel = scheduleLiveAstroModels.get(position);
            if (scheduleLiveAstroModel == null) return;
            try {
                String dateInput = scheduleLiveAstroModel.getDate();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date date = format.parse(dateInput);
                String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
                String day = (String) DateFormat.format("dd", date);
                String monthString = (String) DateFormat.format("MMM", date);
                String dateOutput = day + " " + monthString + ", " + dayOfTheWeek;
                holder.txtDate.setText(dateOutput);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    String result = LocalTime.parse(scheduleLiveAstroModel.getTime(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
                    holder.txtTime.setText(result);

                } else {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(scheduleLiveAstroModel.getTime());
                    holder.txtTime.setText(new SimpleDateFormat("K:mm").format(dateObj));

                }
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            try {
                String astroName = scheduleLiveAstroModel.getName();
                holder.astroTitleTv.setText(astroName.trim());
                holder.txtTopic.setText(scheduleLiveAstroModel.getTopic().trim());
                String astrologerProfileUrl = "";
                if (scheduleLiveAstroModel.getName() != null && scheduleLiveAstroModel.getUrl().length() > 0) {
                    astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + scheduleLiveAstroModel.getImage();
                    Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.roundImage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ScheduleLiveAstroModel scheduleLiveAstroModel = scheduleLiveAstroModels.get(position);
                    holder.ivShare.setVisibility(View.GONE);

                    Uri imageUri = CUtils.getViewBitmap(context,holder.mainlayoutLL);

                    holder.ivShare.setVisibility(View.VISIBLE);
                    String astroUrl = CGlobalVariables.varta_astrosage_urls + CGlobalVariables.LINK_HAS_LIVE + "upcoming";
                    String body = (context.getResources().getString(R.string.share_upcoming_live)).replace("#", scheduleLiveAstroModel.getName());
                    body = body+" "+astroUrl;

                    CUtils.shareDataToOtherApps(context,body,imageUri);
                } catch (Exception e){

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (scheduleLiveAstroModels != null) {
            return scheduleLiveAstroModels.size();
        } else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView astroTitleTv, txtTopic, txtTime, txtDate;
        CircularNetworkImageView roundImage;
        CardView mainlayoutLL;
        ImageView onlineImage,ivShare;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            astroTitleTv = itemView.findViewById(R.id.astroTitleTV);
            mainlayoutLL = itemView.findViewById(R.id.mainlayoutLL);
            roundImage = itemView.findViewById(R.id.ri_profile_img);
            onlineImage = itemView.findViewById(R.id.online_offline_img);
            txtTopic = itemView.findViewById(R.id.txt_topic);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtDate = itemView.findViewById(R.id.txt_date);
            ivShare = itemView.findViewById(R.id.iv_share);
            FontUtils.changeFont(context, txtTopic, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, astroTitleTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        }
    }
}
