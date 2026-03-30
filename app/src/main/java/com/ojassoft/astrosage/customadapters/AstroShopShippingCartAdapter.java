package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.jinterface.IHandleSavedCards;
import com.ojassoft.astrosage.ui.act.AstroShopItemDescription;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on ४/११/१६.
 */
public class AstroShopShippingCartAdapter extends RecyclerView.Adapter<AstroShopShippingCartAdapter.ViewHolder> {
    private List<AstroShopItemDetails> _nameList;
    private Context context;
    private int itemPosition;
    private Bundle bundle;
    private AstroShopItemDetails itemdetails;
    private VolleySingleton vsing;
    private ImageLoader imageLoader;
    private IHandleSavedCards isaveCart;
    public AstroShopShippingCartAdapter()
    {}

    public AstroShopShippingCartAdapter(Context context, List<AstroShopItemDetails> _nameList,IHandleSavedCards isaveCart) {
        this.context = context;
        this._nameList = new ArrayList<AstroShopItemDetails>();
        this._nameList = _nameList;
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
        this.isaveCart=isaveCart;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lay_astroshop_shipping_cart, parent, false);
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

            if (itemdetails.getPName().contains("(")) {
                viewHolder.item_des.setVisibility(View.VISIBLE);
                separated = itemdetails.getPName().split("\\(");
                viewHolder.item_name.setText(separated[0].trim());
                viewHolder.item_des.setText("(" + separated[1].trim());
            } else {
                viewHolder.item_name.setText(itemdetails.getPName());
            }
            if (itemdetails.getP_OutOfStock().equalsIgnoreCase("true")) {
                viewHolder.img_outstock.setVisibility(View.VISIBLE);
                viewHolder.img_outstock.setImageResource(R.drawable.outofstock);
            } else {
                viewHolder.img_outstock.setVisibility(View.INVISIBLE);
            }
            viewHolder.item_cost.setText(context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPPriceInDoller()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " "+roundFunction(Double.parseDouble(itemdetails.getPPriceInRs()), 2));
            if((itemdetails.getP_OriginalPriceInDollar()==null)||(itemdetails.getP_OriginalPriceInRs()==null)||(itemdetails.getP_OriginalPriceInRs().isEmpty())||itemdetails.getPPriceInRs().equalsIgnoreCase(itemdetails.getP_OriginalPriceInRs())){
             //
                //   viewHolder.item_cost.setTextColor(context.getResources().getColor(R.color.black));

            }else{
                if (itemdetails.getP_OutOfStock().equalsIgnoreCase("false"))
                {

                }
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
            //    viewHolder.item_cost.setTextColor(context.getResources().getColor(R.color.red1));
            }

            //  viewHolder.setIsRecyclable(false);


            viewHolder.remove_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isaveCart.deleteCard(position);
                 //   CUtils.removeproductFromCart(context,itemdetails);

                }
            });
           /* viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //com.google.analytics.tracking.android.//Log.e("itemPositionitemPosition"+position);
                    isaveCart.deleteCard(position);
                    CUtils.removeproductFromCart(context,itemdetails);

                    //      setDataTosendDescription(getLayoutPosition());

                }
            });
*/
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
        private ImageView remove_add_cart;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);
            image_url = (NetworkImageView) itemLayoutView.findViewById(R.id.image_view);
            img_outstock = (ImageView) itemLayoutView.findViewById(R.id.img_outstock);
            item_name = (TextView) itemLayoutView.findViewById(R.id.text_title);
            item_des = (TextView) itemLayoutView.findViewById(R.id.text_sub_title);
            item_cost = (TextView) itemLayoutView.findViewById(R.id.text__title_description);
            item_name.setTypeface(((BaseInputActivity) context).robotRegularTypeface);
            item_cost.setTypeface(((BaseInputActivity) context).robotMediumTypeface);
            remove_add_cart=(ImageView) itemLayoutView.findViewById(R.id.remove_add_cart);



        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = _nameList.size();
        //Log.e("tag","Size=" + size);
        return size;
    }

    private void setDataTosendDescription(int itemPosition) {
        Intent itemdescriptionIntent = new Intent(context, AstroShopItemDescription.class);
        bundle = new Bundle();
        setObjectList(itemPosition);

        itemdescriptionIntent.putExtras(bundle);
        if (itemdescriptionIntent != null) {
            if (!_nameList.get(itemPosition).getP_OutOfStock().equalsIgnoreCase("true"))
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
}


