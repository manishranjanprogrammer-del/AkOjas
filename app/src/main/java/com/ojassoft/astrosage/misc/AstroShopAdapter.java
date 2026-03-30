package com.ojassoft.astrosage.misc;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.ui.act.AstroShopItemDescription;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

////import com.google.analytics.tracking.android.Log;

/**
 * Created by ojas on १/३/१६.
 */

public class AstroShopAdapter extends RecyclerView.Adapter<AstroShopAdapter.ViewHolder> {
    private List<AstroShopItemDetails> _nameList;
    private List<AstroShopItemDetails> _allList;

    private Context context;
    private int itemPosition;
    private Bundle bundle;
    private AstroShopItemDetails itemdetails;
    private VolleySingleton vsing;
    private ImageLoader imageLoader;
    int LANGUAGE_CODE= CGlobalVariables.ENGLISH;
    public AstroShopAdapter()
    {}

    public AstroShopAdapter(Context context, List<AstroShopItemDetails> _allList) {
        this.context = context;
        this._allList = _allList;
        this._nameList=getNameList(this._allList);
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();

        try {
            LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(context);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    // Create new views (invoked by the layout manager)
    @Override
    public AstroShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_custom_cardview_astroshop, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {
            String[] separated = null;
            itemdetails = new AstroShopItemDetails();
            itemdetails = _nameList.get(position);
            //Log.e("tag","outposition" + position);

                if (itemdetails.getPImgUrl() != null && !itemdetails.getPImgUrl().isEmpty()) {
                    viewHolder.image_url.setImageUrl(itemdetails.getPImgUrl(), imageLoader);
                    //Log.e("tag","Image hit" + position);

                } else {
                    viewHolder.image_url.setImageDrawable(null);
                }

                if (itemdetails.getP_VirtualName().contains("(")) {
                    viewHolder.item_des.setVisibility(View.VISIBLE);
                    separated = itemdetails.getP_VirtualName().split("\\(");
                    viewHolder.item_name.setText(separated[0].trim());
                    viewHolder.item_des.setText("(" + separated[1].trim());
                } else {
                    viewHolder.item_name.setText(itemdetails.getP_VirtualName());
                }
                if (itemdetails.getP_OutOfStock().equalsIgnoreCase("true")) {
                    viewHolder.img_outstock.setVisibility(View.VISIBLE);
                    viewHolder.img_outstock.setImageResource(R.drawable.outofstock);
                    viewHolder.txt_disscount.setVisibility(View.GONE);
                } else {
                    viewHolder.img_outstock.setVisibility(View.INVISIBLE);
                }
                viewHolder.item_cost.setText(context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPPriceInDoller()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getPPriceInRs()), 2));
                if ((itemdetails.getP_OriginalPriceInDollar() == null) || (itemdetails.getP_OriginalPriceInRs() == null) || (itemdetails.getP_OriginalPriceInRs().isEmpty()) || itemdetails.getPPriceInRs().equalsIgnoreCase(itemdetails.getP_OriginalPriceInRs())) {
                    viewHolder.txt_disscount.setVisibility(View.GONE);
                    viewHolder.txt_sale_price.setVisibility(View.GONE);
                    viewHolder.item_cost.setTextColor(context.getResources().getColor(R.color.black));

                } else {
                    if (itemdetails.getP_OutOfStock().equalsIgnoreCase("false")) {
                        viewHolder.txt_disscount.setVisibility(View.VISIBLE);

                    }
                    viewHolder.txt_sale_price.setVisibility(View.VISIBLE);
                    String discoumt = "";
                    if (itemdetails.getP_SavePercentOfRs().contains(".")) {
                        String str[] = itemdetails.getP_SavePercentOfRs().split("\\.");
                        discoumt = str[0];
                    } else {
                        discoumt = itemdetails.getP_SavePercentOfRs();
                    }
                    viewHolder.item_cost.setTextColor(context.getResources().getColor(R.color.red1));
                    viewHolder.txt_disscount.setText(discoumt.trim() + "%" + "\n" + "off");
                    //viewHolder.txt_sale_price.setText(context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2));
                    //viewHolder.txt_sale_price.setPaintFlags(viewHolder.txt_sale_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(viewHolder.txt_sale_price, context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2) );

                }


            CUtils.showServiceProductDiscountedText(context, viewHolder.textDiscountPlan,
                    itemdetails.getMessageOfCloudPlan1(), itemdetails.getMessageOfCloudPlan2(), CGlobalVariables.FROM_PRODUCT_TEXT);

            CUtils.showBasicPlanUserText(context,viewHolder.msgForBasicPlanText,viewHolder.basicPlanUserLayout,
                    itemdetails.getMessageOfCloudPlan1(),itemdetails.getMessageOfCloudPlan2());


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
        public TextView item_name;
        public TextView item_des;
        public TextView item_cost;
        public ImageView img_outstock;
        private TextView txt_disscount;
        private TextView txt_sale_price;
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
            txt_disscount = (TextView) itemLayoutView.findViewById(R.id.txt_disscount);
            txt_sale_price= (TextView) itemLayoutView.findViewById(R.id.txt_sale_price);
          //  item_name.setTypeface(((BaseInputActivity) context).robotRegularTypeface);
            item_name.setTypeface(((BaseInputActivity) context).robotMediumTypeface, Typeface.BOLD);
            item_cost.setTypeface(((BaseInputActivity) context).robotMediumTypeface);

            textDiscountPlan = (TextView)itemLayoutView.findViewById(R.id.text_discount_plan);
            msgForBasicPlanText = (TextView)itemLayoutView.findViewById(R.id.msg_for_basic_plan_text);
            unlockPlanText = (TextView)itemLayoutView.findViewById(R.id.unlock_plan_text);
            basicPlanUserLayout = (LinearLayout)itemLayoutView.findViewById(R.id.basic_plan_user_layout);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    setDataTosendDescription(getLayoutPosition());

                }
            });

            basicPlanUserLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CUtils.gotoProductPlanListUpdated(((Activity) context),
                            LANGUAGE_CODE, BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"product_list");
                }
            });

        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (_nameList == null) {
            return 0;
        } else {
            return _nameList.size();
        }
    }

    private void setDataTosendDescription(int itemPosition) {
        Intent itemdescriptionIntent = new Intent(context, AstroShopItemDescription.class);
        bundle = new Bundle();
        ArrayList<AstroShopItemDetails> itemList= CUtils.getAllchilditems(_nameList.get(itemPosition),_allList);
        bundle.putSerializable("key", itemList);
        itemdescriptionIntent.putExtras(bundle);
        if (itemdescriptionIntent != null) {
                context.startActivity(itemdescriptionIntent);
        }

    }

    private void setObjectList(int itemPosition) {
        itemdetails = new AstroShopItemDetails();
        itemdetails = _nameList.get(itemPosition);
        bundle.putSerializable("key", itemdetails);
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void updateList(List<AstroShopItemDetails> data) {
        _nameList=new ArrayList<AstroShopItemDetails>();
        for (int i = 0; i < data.size(); i++) {
            _nameList.add(data.get(i));
        }
        notifyDataSetChanged();

    }

    List<AstroShopItemDetails> getNameList(List<AstroShopItemDetails> list)
    {
        _nameList=new ArrayList<>();
        for(AstroShopItemDetails item:list)
        {
            if(item.getIsDefault().equalsIgnoreCase("true"))
            {
                _nameList.add((item));
            }
        }
        return _nameList;
    }
}