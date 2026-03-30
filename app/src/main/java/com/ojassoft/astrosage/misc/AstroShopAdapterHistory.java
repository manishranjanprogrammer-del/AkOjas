package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OrderedItemDetailActivity;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas-20 on 21/11/16.
 */
public class AstroShopAdapterHistory extends RecyclerView.Adapter<AstroShopAdapterHistory.ViewHolder> {
    private List<AstroShopItemDetails> _nameList;
    private Context context;
    private int itemPosition;
    private Bundle bundle;
    private AstroShopItemDetails itemdetails;
    private VolleySingleton vsing;
    private ImageLoader imageLoader;
    List<AstroShopItemDetails> astroShopItemDetailsList;
    private boolean isFindInList;

    public AstroShopAdapterHistory() {
    }

    public AstroShopAdapterHistory(Context context, List<AstroShopItemDetails> _nameList) {
        this.context = context;
        this.astroShopItemDetailsList = astroShopItemDetailsList;
        this._nameList = new ArrayList<AstroShopItemDetails>();
        this._nameList = _nameList;
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public AstroShopAdapterHistory.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_custom_cardview_astroshop_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        try {
            String[] separated = null;
            // itemdetails = new AstroShopItemDetails();
            itemdetails = _nameList.get(position);
            Log.e("tag", "outposition" + position);
            viewHolder.txt_ordernumber.setText(context.getString(R.string.order_id) + " " + itemdetails.getO_Id());
            viewHolder.txt_orderDate.setText(context.getString(R.string.order_on) + " "+itemdetails.getO_Date());
            if (itemdetails.getP_Status().equalsIgnoreCase("1") || itemdetails.getP_Status().equalsIgnoreCase("7") || itemdetails.getP_Status().equalsIgnoreCase("3")) {
                viewHolder.txt_orderstatus.setText(context.getResources().getString(R.string.orderCancelled));
            } else if (itemdetails.getP_Status().equalsIgnoreCase("6")) {
                viewHolder.txt_orderstatus.setText(context.getResources().getString(R.string.delivered));
                viewHolder.txt_orderstatus.setTextColor(context.getResources().getColor(R.color.astroshop_green));
            } else if (itemdetails.getP_Status().equalsIgnoreCase("2") || itemdetails.getP_Status().equalsIgnoreCase("5")) {
                //viewHolder.txt_orderstatus.setText(context.getResources().getString(R.string.processing));
            } else if (itemdetails.getP_Status().equalsIgnoreCase("4")||itemdetails.getP_Status().equalsIgnoreCase("8")) {
                viewHolder.txt_orderstatus.setText(context.getResources().getString(R.string.waitingpayment));
            } else if (itemdetails.getP_Status().equalsIgnoreCase("9")) {
                viewHolder.txt_orderstatus.setText(context.getResources().getString(R.string.returned));
            } /*else if (itemdetails.getP_Status().equalsIgnoreCase("8")) {
                viewHolder.txt_orderstatus.setText("");
            }*/

            if (itemdetails.getD_Date() != null && !itemdetails.getD_Date().isEmpty()) {
                viewHolder.txt_deliveredDate.setVisibility(View.VISIBLE);
                viewHolder.txt_deliveredDate.setText(itemdetails.getD_Date());
            } else {
                viewHolder.txt_deliveredDate.setVisibility(View.GONE);
            }
            if (itemdetails.getPImgUrl() != null && !itemdetails.getPImgUrl().isEmpty()) {
                String imageUrl = itemdetails.getPImgUrl();
                Glide.with(context).load(imageUrl).into( viewHolder.image_url);
               // viewHolder.image_url.setImageUrl(itemdetails.getPImgUrl(), imageLoader);
                //Log.e("testImageIssue", "Image hit" + position);

            } else {
                viewHolder.image_url.setImageDrawable(null);
            }

            if (itemdetails.getPName().contains("(")) {
                //viewHolder.item_des.setVisibility(View.VISIBLE);
                separated = itemdetails.getPName().split("\\(");
                viewHolder.item_name.setText(separated[0].trim());
                //viewHolder.item_des.setText("(" + separated[1].trim());
            } else {
                viewHolder.item_name.setText(itemdetails.getPName());
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
                    viewHolder.txt_disscount.setVisibility(View.GONE);

                }
                viewHolder.txt_sale_price.setVisibility(View.GONE);
                String discoumt = "";
                if (itemdetails.getP_SavePercentOfRs().contains(".")) {
                    String str[] = itemdetails.getP_SavePercentOfRs().split("\\.");
                    discoumt = str[0];
                } else {
                    discoumt = itemdetails.getP_SavePercentOfRs();
                }
                //  viewHolder.item_cost.setTextColor(context.getResources().getColor(R.color.red1));
                viewHolder.txt_disscount.setText(discoumt.trim() + "%" + "\n" + "off");
                //viewHolder.txt_sale_price.setText(context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2));
                //viewHolder.txt_sale_price.setPaintFlags(viewHolder.txt_sale_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                CUtils.setStrikeOnTextView(viewHolder.txt_sale_price, context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2) );

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
        public ImageView image_url;
        public TextView item_name;
        public TextView item_des;
        public TextView item_cost;
        public ImageView img_outstock;
        private TextView txt_disscount;
        private TextView txt_sale_price;
        private TextView txt_orderDate;
        private TextView txt_ordernumber;
        private TextView txt_orderstatus;
        private TextView txt_deliveredDate;
        private TextView txtStatus;




        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
            image_url =  itemLayoutView.findViewById(R.id.image_view);
            img_outstock = (ImageView) itemLayoutView.findViewById(R.id.img_outstock);
            item_name = (TextView) itemLayoutView.findViewById(R.id.text_title);
            item_des = (TextView) itemLayoutView.findViewById(R.id.text_sub_title);
            item_cost = (TextView) itemLayoutView.findViewById(R.id.text__title_description);
            txt_disscount = (TextView) itemLayoutView.findViewById(R.id.txt_disscount);
            txt_sale_price = (TextView) itemLayoutView.findViewById(R.id.txt_sale_price);
            item_name.setTypeface(((BaseInputActivity) context).robotRegularTypeface, Typeface.BOLD);
            item_cost.setTypeface(((BaseInputActivity) context).robotMediumTypeface);
            txt_orderDate = (TextView) itemLayoutView.findViewById(R.id.orderDate);
            txt_orderstatus = (TextView) itemLayoutView.findViewById(R.id.orderStatus);
            txt_ordernumber = (TextView) itemLayoutView.findViewById(R.id.orderID);
            txt_deliveredDate = (TextView) itemLayoutView.findViewById(R.id.deliverDate);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setDataTosendDescription(getLayoutPosition());
                }
            });

        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = _nameList.size();
        Log.e("tag", "Size=" + size);
        return size;
    }

    private void setDataTosendDescription(int itemPosition) {
        Intent itemdescriptionIntent = new Intent(context, OrderedItemDetailActivity.class);
        bundle = new Bundle();
        bundle.putSerializable("orderdetail", _nameList.get(itemPosition));
        /*if (itemdescriptionIntent != null && astroShopItemDetailsList != null) {
            for (int i = 0; i < astroShopItemDetailsList.size(); i++) {
                if (_nameList.get(itemPosition).getPId().equalsIgnoreCase(astroShopItemDetailsList.get(i).getPId())) {
                    itemdetails = astroShopItemDetailsList.get(i);
                    setObjectList();
                    isFindInList = true;
                    break;
                }
            }*/
        itemdescriptionIntent.putExtras(bundle);
        // if (!_nameList.get(itemPosition).getP_OutOfStock().equalsIgnoreCase("true") && astroShopItemDetailsList != null && isFindInList) {
        context.startActivity(itemdescriptionIntent);
        isFindInList = false;
        //}
        // }

    }

    private void setObjectList() {
        /*itemdetails = new AstroShopItemDetails();
        itemdetails = _nameList.get(itemPosition);*/
        bundle.putSerializable("key", itemdetails);
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void updateList(List<AstroShopItemDetails> data) {
        _nameList = new ArrayList<AstroShopItemDetails>();
        for (int i = 0; i < data.size(); i++) {
            _nameList.add(data.get(i));
        }
        notifyDataSetChanged();

    }

}