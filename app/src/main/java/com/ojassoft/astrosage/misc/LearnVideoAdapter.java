package com.ojassoft.astrosage.misc;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.SliderModal;
import com.ojassoft.astrosage.ui.act.ActLearnAstrology;
import com.ojassoft.astrosage.ui.act.ActPlayVideo;
import com.ojassoft.astrosage.ui.act.AstroShopItemDescription;
import com.ojassoft.astrosage.utils.CGlobalVariables;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on १/३/१६.
 */

public class LearnVideoAdapter extends RecyclerView.Adapter<LearnVideoAdapter.ViewHolder> {
    private List<SliderModal> _nameList;
    private Context context;
    private int itemPosition;
    private Bundle bundle;
    private SliderModal itemdetails;
    private VolleySingleton vsing;
    private ImageLoader imageLoader;
    public LearnVideoAdapter()
    {}

    public LearnVideoAdapter(Context context, List<SliderModal> _nameList) {
        this.context = context;
        this._nameList = new ArrayList<SliderModal>();
        this._nameList = _nameList;
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public LearnVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_recycler_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            String img_url="";
            itemdetails = new SliderModal();
            itemdetails = _nameList.get(position);
         //   //Log.e("outposition" + position);

            if(itemdetails.getThumbnailImageURL()==null||itemdetails.getThumbnailImageURL().isEmpty())
            {
                // img_url=CGlobalVariables.youTubeBaseUrl+entry.getVideo_url().trim()+getRandomImage();
                img_url= CGlobalVariables.youTubeBaseUrl+itemdetails.getVideo_url().trim()+"/0.jpg";

            }
            else
            {
                img_url=itemdetails.getThumbnailImageURL();
            }

            viewHolder.imgSlide.setImageUrl(img_url, imageLoader);


            viewHolder.label.setText(itemdetails.getTitle());
            viewHolder.cardView.setLayoutParams(new LinearLayout.LayoutParams(width/2-35, width/2-150));


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView imgSlide;
        TextView label;
        CardView cardView;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
            imgSlide=(NetworkImageView) itemLayoutView.findViewById(R.id.imgSlides);
            label = ((TextView) itemLayoutView.findViewById(R.id.tvTitle));
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  Bundle bundle=new Bundle();
                    bundle.putSerializable("DATA",_nameList.get(getLayoutPosition()));
                    Intent i=new Intent(context, ActPlayVideo.class);
                    i.putExtra("DATA",bundle);
                    i.putExtra("ISRESUME",true);

                    context.startActivity(i);

                }
            });

        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = _nameList.size();
        return size;
    }

    private void setDataTosendDescription(int itemPosition) {
        Intent itemdescriptionIntent = new Intent(context, AstroShopItemDescription.class);
        bundle = new Bundle();
        setObjectList(itemPosition);

        itemdescriptionIntent.putExtras(bundle);
        if (itemdescriptionIntent != null) {
            if (!_nameList.get(itemPosition).getUrl().equalsIgnoreCase("true"))
                context.startActivity(itemdescriptionIntent);
        }

    }

    private void setObjectList(int itemPosition) {
        itemdetails = new SliderModal();
        itemdetails = _nameList.get(itemPosition);
        bundle.putSerializable("key", itemdetails);
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void updateList(List<SliderModal> data) {
        _nameList=new ArrayList<SliderModal>();
        for (int i = 0; i < data.size(); i++) {
            _nameList.add(data.get(i));
        }
        notifyDataSetChanged();

    }
}