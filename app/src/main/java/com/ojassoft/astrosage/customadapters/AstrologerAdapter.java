package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
//import com.google.analytics.tracking.android.Log;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.AstrologerInfo;
import com.ojassoft.astrosage.ui.act.ActAboutAstrologer;
import com.ojassoft.astrosage.ui.act.ActAstroShopServices;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on ७/६/१६.
 */
public class AstrologerAdapter extends RecyclerView.Adapter<AstrologerAdapter.ViewHolder> {
    private List<AstrologerInfo> _nameList;
    private Context cxt;
    private int itemPosition;
    private Bundle bundle;
    private AstrologerInfo itemdetails;
    private VolleySingleton vsing;
    private ImageLoader imageLoader;
    FragmentManager fm;


    public AstrologerAdapter() {
    }

    public AstrologerAdapter(Context context, FragmentManager fragmentManager, List<AstrologerInfo> _nameList) {
        this.cxt = context;
        this._nameList = new ArrayList<AstrologerInfo>();
        this._nameList = _nameList;
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
        fm = fragmentManager;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public AstrologerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_astrologer_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {
            String[] separated = null;
            itemdetails = new AstrologerInfo();
            itemdetails = _nameList.get(position);


            if (itemdetails.getImageURL() != null && !itemdetails.getImageURL().isEmpty()) {
                viewHolder.image_url.setImageUrl(itemdetails.getImageURL(), imageLoader);


            } else {
                viewHolder.image_url.setImageDrawable(null);
            }
            viewHolder.astrologer_name.setText(itemdetails.getName());
            viewHolder.astrologer_des.setText(itemdetails.getFocusAreas());
            viewHolder.img_info.setTag(position);
            viewHolder.cardView.setTag(position);
            viewHolder.img_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemdetails = _nameList.get((Integer) v.getTag());
                    PopupMenu popup = new PopupMenu(cxt, viewHolder.img_info);

                    popup.inflate(R.menu.about_astrologer);

        /*            Intent abIntent = new Intent(cxt, ActAboutAstrologer.class);
                    Bundle b=new Bundle();
                    b.putString("astroloname",itemdetails.getName());
                    b.putString("focusarea",itemdetails.getFocusAreas());
                    b.putString("description",itemdetails.getDescription());
                    b.putString("astrologerid",itemdetails.getDescription());
                    b.putString("experience",itemdetails.getDescription());
                    abIntent.putExtras(b);
                    cxt.startActivity(abIntent);*/

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    Intent abIntent = new Intent(cxt, ActAboutAstrologer.class);
                                    Bundle b = new Bundle();
                                    b.putString("astroloname", itemdetails.getName());
                                    b.putString("focusarea", itemdetails.getFocusAreas());
                                    b.putString("description", itemdetails.getDescription());
                                    b.putString("astrologerid", ""+itemdetails.getAstrologerID());
                                    b.putString("experience", itemdetails.getExperience());
                                    abIntent.putExtras(b);
                                    cxt.startActivity(abIntent);
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemdetails = _nameList.get((Integer) v.getTag());
                    //Log.e("IDDDDDDDDDDDDDDDD" + itemdetails.getAstrologerID());
                    Intent itemdescriptionIntent = new Intent(cxt, ActAstroShopServices.class);
                    itemdescriptionIntent.putExtra("AstroId", String.valueOf(itemdetails.getAstrologerID()));
                    cxt.startActivity(itemdescriptionIntent);


                }
            });

            if(getItemCount()-1 == position){
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 25);
                viewHolder.cardView.setLayoutParams(buttonLayoutParams);
            }else{
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 0);
                viewHolder.cardView.setLayoutParams(buttonLayoutParams);
            }

            //  viewHolder.setIsRecyclable(false);

        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, "EXCEP", Toast.LENGTH_LONG).show();
        }
        //viewHolder.genderImage.setText(itemsData[position].getTitle());


    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public NetworkImageView image_url;
        public TextView astrologer_name;
        public TextView astrologer_des;
        public NetworkImageView img_info;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
            image_url = (NetworkImageView) itemLayoutView.findViewById(R.id.image_view);
            astrologer_name = (TextView) itemLayoutView.findViewById(R.id.text_title);
            astrologer_des = (TextView) itemLayoutView.findViewById(R.id.text__title_description);
            astrologer_name.setTypeface(((BaseInputActivity) cxt).robotMediumTypeface);
            astrologer_des.setTypeface(((BaseInputActivity) cxt).robotRegularTypeface);
            img_info = (NetworkImageView) itemLayoutView.findViewById(R.id.img_info);


        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = _nameList.size();
        //Log.e("Size=" + size);
        return size;
    }


    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public void updateListData(ArrayList<AstrologerInfo> data) {
        _nameList = new ArrayList<AstrologerInfo>();
        for (int i = 0; i < data.size(); i++) {
            _nameList.add(data.get(i));
        }
        notifyDataSetChanged();
        //Log.e("Updating from service");
    }

    /*
    *
    *
    public void updateList(List<AstroShopItemDetails> data) {
        _nameList=new ArrayList<AstroShopItemDetails>();
        for (int i = 0; i < data.size(); i++) {
            _nameList.add(data.get(i));
        }
        notifyDataSetChanged();

    }
    * */

}