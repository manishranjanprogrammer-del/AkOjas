package com.ojassoft.astrosage.customadapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
//import com.google.analytics.tracking.android.Log;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.act.ActAstroShopServices;
import com.ojassoft.astrosage.ui.act.ActAstrologerDescription;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on १/३/१६.
 */

public class AstroShopServiceAdapter extends RecyclerView.Adapter<AstroShopServiceAdapter.ViewHolder> {
    private List<ServicelistModal> _nameList;
    private Context context;
    private int itemPosition;
    private Bundle bundle;
    private ServicelistModal itemdetails;

    private VolleySingleton vsing;
    private ImageLoader imageLoader;
    String astrologerId="";
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    public AstroShopServiceAdapter()
    {}

    public AstroShopServiceAdapter(Context context, List<ServicelistModal> _nameList,String astrologerId,int LANGUAGE_CODE) {
        this.context = context;
        this._nameList = new ArrayList<ServicelistModal>();
        this._nameList = _nameList;
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
        this.astrologerId=astrologerId;
        this.LANGUAGE_CODE = LANGUAGE_CODE;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public AstroShopServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_service_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {
            String[] separated = null;
            itemdetails = new ServicelistModal();
            itemdetails = _nameList.get(position);
            //Log.e("outposition" + position);

            if (itemdetails.getSmallImgURL() != null && !itemdetails.getSmallImgURL().isEmpty()) {
                viewHolder.image_url.setImageUrl(itemdetails.getSmallImgURL(), imageLoader);
                //Log.e("Image hit" + position);

            } else {
                viewHolder.image_url.setImageDrawable(null);
            }
            viewHolder.item_name.setText(itemdetails.getTitle());
            viewHolder.item_cost.setText(itemdetails.getSmallDesc());

            if((itemdetails.getP_OriginalPriceInDollar()==null)||(itemdetails.getP_OriginalPriceInRs()==null)||(itemdetails.getP_OriginalPriceInRs().isEmpty())||itemdetails.getPriceInRS().equalsIgnoreCase(itemdetails.getP_OriginalPriceInRs())){
                viewHolder.txt_disscount.setVisibility(View.GONE);

            }else{

                viewHolder.txt_disscount.setVisibility(View.VISIBLE);
                String discoumt="";
                if(itemdetails.getP_SavePercentOfRs().contains("."))
                {
                    String str[]= itemdetails.getP_SavePercentOfRs().split("\\.");
                    discoumt=str[0];
                }
                else
                {
                    discoumt= itemdetails.getP_SavePercentOfRs();
                }
                viewHolder.txt_disscount.setText(discoumt.trim()+"%"+"\n"+"off");
            }


            CUtils.showServiceProductDiscountedText(context, viewHolder.textDiscountPlan,
                    itemdetails.getMessageOfCloudPlanText1(),itemdetails.getMessageOfCloudPlanText2(),
                    CGlobalVariables.FROM_SERVICE_TEXT);

            CUtils.showBasicPlanUserText(context,viewHolder.msgForBasicPlanText,viewHolder.basicPlanUserLayout,
                    itemdetails.getMessageOfCloudPlanText1(),itemdetails.getMessageOfCloudPlanText2());

           /* if(getItemCount()-1 == position){
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 25);
                viewHolder.cardView.setLayoutParams(buttonLayoutParams);
            }else{
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(25, 25, 25, 0);
                viewHolder.cardView.setLayoutParams(buttonLayoutParams);
            }*/

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
        public TextView item_name;
        public TextView item_des;
        public TextView item_cost;
        public ImageView img_outstock;
        private TextView txt_disscount;
        private TextView textDiscountPlan;
        private LinearLayout basicPlanUserLayout;
        private TextView msgForBasicPlanText, unlockPlanText;



        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
            image_url = (NetworkImageView) itemLayoutView.findViewById(R.id.image_view);
            img_outstock = (ImageView) itemLayoutView.findViewById(R.id.img_outstock);
            item_name = (TextView) itemLayoutView.findViewById(R.id.text_title);
            item_des = (TextView) itemLayoutView.findViewById(R.id.text_sub_title);
            item_cost = (TextView) itemLayoutView.findViewById(R.id.text__title_description);
            item_name.setTypeface(((BaseInputActivity) context).robotRegularTypeface, Typeface.BOLD);

            item_cost.setTypeface(((BaseInputActivity) context).robotRegularTypeface);
            txt_disscount = (TextView) itemLayoutView.findViewById(R.id.txt_disscount);

            textDiscountPlan = (TextView)itemLayoutView.findViewById(R.id.text_discount_plan);
            msgForBasicPlanText = (TextView)itemLayoutView.findViewById(R.id.msg_for_basic_plan_text);
            unlockPlanText = (TextView)itemLayoutView.findViewById(R.id.unlock_plan_text);
            basicPlanUserLayout = (LinearLayout)itemLayoutView.findViewById(R.id.basic_plan_user_layout);



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Log.e("card view clicked");
                    setDataTosendDescription(getLayoutPosition());

                }
            });


            basicPlanUserLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CUtils.gotoProductPlanListUpdated(((Activity) context),
                            LANGUAGE_CODE, BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"astro_shop_service_adapter");

                }
            });
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = _nameList.size();
        //Log.e("Size=" + size);
        return size;
    }

    private void setDataTosendDescription(int itemPosition) {

        ServicelistModal servicelistModal = _nameList.get(itemPosition);
        //For Gold and Silver Plan
        //Added by Ankit on 17-12-2019 platinum serviceid check for deeplink
        if(servicelistModal != null && servicelistModal.getServiceId()!= null && servicelistModal.getServiceId().equals("68") || servicelistModal.getServiceId().equals("69") || servicelistModal.getServiceId().equals(CGlobalVariables.PLATINUM_SERVICE_ID) || servicelistModal.getServiceId().equals("157")) {
            CUtils.getUrlLink(servicelistModal.getServiceDeepLinkURL(),((Activity) context),LANGUAGE_CODE,0);
        }else {

            Intent itemdescriptionIntent = new Intent(context, ActAstrologerDescription.class);
            bundle = new Bundle();
            //  bundle.putSerializable("KEY",_nameList);
            setObjectList(itemPosition);

            itemdescriptionIntent.putExtras(bundle);
            if (itemdescriptionIntent != null) {
//            if (!_nameList.get(itemPosition).getP_OutOfStock().equalsIgnoreCase("true"))
                context.startActivity(itemdescriptionIntent);
            }
        }

    }

    private void setObjectList(int itemPosition) {
        //  itemdetails = new AstroShopItemDetails();
        itemdetails = _nameList.get(itemPosition);
        bundle.putSerializable("key", itemdetails);
        bundle.putString("astrologerId",astrologerId);
        if(context instanceof ActAstroShopServices){
            CUtils.BRIHAT_KUNDALI_PURCHASE_SOURCE = "AstroShop";
        }
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void updateListData(ArrayList<ServicelistModal> data)
    {
        _nameList=new ArrayList<ServicelistModal>();
        for (int i = 0; i < data.size(); i++) {
            _nameList.add(data.get(i));
        }
        notifyDataSetChanged();
        //Log.e("Updating from service");
    }
}