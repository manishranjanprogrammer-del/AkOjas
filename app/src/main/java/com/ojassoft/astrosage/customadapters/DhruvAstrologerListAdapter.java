package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.DhruvAstrologerListModel;
import com.ojassoft.astrosage.interfaces.GetConsultaionInterface;
import com.ojassoft.astrosage.ui.act.AstrologerListingDetailsActivity;
import com.ojassoft.astrosage.varta.dao.NotificationDBManager;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.interfacefile.LoadMoreList;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.BookmarkModel;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class DhruvAstrologerListAdapter extends RecyclerView.Adapter<DhruvAstrologerListAdapter.MyViewHolder> {

    Context context;
    ArrayList<DhruvAstrologerListModel> astrologerDetailBeanArrayList;
    GetConsultaionInterface getConsultaionInterface;

    public DhruvAstrologerListAdapter(Context context, ArrayList<DhruvAstrologerListModel> astrologerDetailBeanArrayList,
                                      GetConsultaionInterface getConsultaionInterface) {
        this.context = context;
        this.astrologerDetailBeanArrayList = astrologerDetailBeanArrayList;
        this.getConsultaionInterface = getConsultaionInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dhruv_astrologist_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (astrologerDetailBeanArrayList == null) return;
        DhruvAstrologerListModel astrologerDetailBean = astrologerDetailBeanArrayList.get(position);
        if (astrologerDetailBean == null) return;

        holder.astrologerNameTxt.setText(astrologerDetailBean.getName());

        String astrologerProfileUrl = astrologerDetailBean.getImageFile();
        //Log.e("astroProfileUrl", "astroProfileUrl = "+astrologerProfileUrl);
        holder.astrologer_img.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());

        String astroCity="";
        if(astrologerDetailBean.getState() != null && astrologerDetailBean.getState().length()>0){
            astroCity = astrologerDetailBean.getCity() + ", "+ astrologerDetailBean.getState();
        }else{
            astroCity = astrologerDetailBean.getCity();
        }
        holder.astrologer_city_txt.setText(astroCity);

        if(astrologerDetailBean.getAverageRating() != null && astrologerDetailBean.getAverageRating().length()>0) {
            holder.rating_txt.setText(astrologerDetailBean.getAverageRating());
            holder.ratingStar.setRating(Float.parseFloat(astrologerDetailBean.getAverageRating()));
        }
        holder.votes_txt.setText(astrologerDetailBean.getTotalRating() + " " + context.getResources().getString(R.string.votes));

        holder.get_consultation_txt.setTag(position);
        holder.upper_layout.setTag(position);
        //holder.bookmarkImg.setTag(position);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (astrologerDetailBeanArrayList != null) {
            count = astrologerDetailBeanArrayList.size();
        }
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView astrologerNameTxt, astrologer_city_txt, rating_txt, votes_txt, get_consultation_txt;
        ImageView verified_img;
        LinearLayout upper_layout;
        NetworkImageView astrologer_img;
        AppCompatRatingBar ratingStar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingStar = (AppCompatRatingBar) itemView.findViewById(R.id.rating_star);
            rating_txt = itemView.findViewById(R.id.rating_txt);
            votes_txt = itemView.findViewById(R.id.votes_txt);
            astrologerNameTxt = itemView.findViewById(R.id.astrologer_name_txt);
            get_consultation_txt = itemView.findViewById(R.id.get_consultation_txt);
            verified_img = itemView.findViewById(R.id.verified_img);
            upper_layout = itemView.findViewById(R.id.upper_layout);
            astrologer_city_txt = itemView.findViewById(R.id.astrologer_city_txt);
            astrologer_img = (NetworkImageView) itemView.findViewById(R.id.astrologer_img);


            FontUtils.changeFont(context, astrologerNameTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, rating_txt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, votes_txt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, astrologer_city_txt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, get_consultation_txt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

            upper_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int pos = Integer.parseInt(upper_layout.getTag().toString());
                        Intent intent = new Intent(context, AstrologerListingDetailsActivity.class);
                        intent.putExtra("profileId", astrologerDetailBeanArrayList.get(pos).getProfileId());
                        context.startActivity(intent);
                    }catch (Exception e){
                        //
                    }
                }
            });

            get_consultation_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(get_consultation_txt.getTag().toString());
                   getConsultaionInterface.getConsultationClick(astrologerDetailBeanArrayList.get(pos).getProfileId());
                }
            });

        }
    }
}