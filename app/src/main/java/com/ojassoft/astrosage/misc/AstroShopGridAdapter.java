package com.ojassoft.astrosage.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

/**
 * Created by ojas on २/३/१६.
 */
public class AstroShopGridAdapter extends BaseAdapter {
    private List<AstroShopItemDetails> _nameList;
    private List<AstroShopItemDetails> _allList;

    private Context context;
    private Bundle bundle;
    private AstroShopItemDetails itemdetails;
    int LANGUAGE_CODE= CGlobalVariables.ENGLISH;

    public AstroShopGridAdapter() {
    }

    public AstroShopGridAdapter(Context context, List<AstroShopItemDetails> _allList) {
        this.context = context;
        this._allList=_allList;
        this._nameList=getNameList(this._allList);

        try {
            LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(context);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        if (_nameList == null) {
            return 0;
        } else {
            return _nameList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return _nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.lay_custom_cardview_grid_astroshop, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cardView = (CardView) convertView.findViewById(R.id.card_view);
            viewHolder.image_url = (NetworkImageView) convertView.findViewById(R.id.image_view);
            viewHolder.item_name = (TextView) convertView.findViewById(R.id.text_title);
            viewHolder.item_des = (TextView) convertView.findViewById(R.id.text_sub_title);
            viewHolder.item_cost = (TextView) convertView.findViewById(R.id.text__title_description);
            viewHolder.img_outstock = (ImageView) convertView.findViewById(R.id.img_outstock);
            viewHolder.txt_disscount = (TextView) convertView.findViewById(R.id.txt_disscount);
            viewHolder.txt_sale_price = (TextView) convertView.findViewById(R.id.txt_sale_price);
            viewHolder.textDiscountPlan = (TextView) convertView.findViewById(R.id.text_discount_plan);

            viewHolder.msgForBasicPlanText = (TextView)convertView.findViewById(R.id.msg_for_basic_plan_text);
            viewHolder.unlockPlanText = (TextView)convertView.findViewById(R.id.unlock_plan_text);
            viewHolder.basicPlanUserLayout = (LinearLayout)convertView.findViewById(R.id.basic_plan_user_layout);

            viewHolder.item_name.setTypeface(((BaseInputActivity) context).robotMediumTypeface, Typeface.BOLD);

           // viewHolder.item_name.setTypeface(((BaseInputActivity) context).robotRegularTypeface);
            viewHolder.item_cost.setTypeface(((BaseInputActivity) context).robotMediumTypeface);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
        String[] separated = null;
        itemdetails = new AstroShopItemDetails();
        itemdetails = _nameList.get(position);
        if (itemdetails.getP_OutOfStock().equalsIgnoreCase("true")) {
            viewHolder.img_outstock.setVisibility(View.VISIBLE);
            viewHolder.img_outstock.setImageResource(R.drawable.outofstock);
            viewHolder.txt_disscount.setVisibility(View.GONE);
        } else {
            //  viewHolder.txt_disscount.setVisibility(View.VISIBLE);

            viewHolder.img_outstock.setVisibility(View.INVISIBLE);
        }

        viewHolder.image_url.setImageUrl(itemdetails.getPImgUrl(), VolleySingleton.getInstance(context).getImageLoader());

        if (itemdetails.getP_VirtualName().contains("(")) {
            viewHolder.item_des.setVisibility(View.VISIBLE);
            separated = itemdetails.getP_VirtualName().split("\\(");
            viewHolder.item_name.setText(separated[0].trim());
            viewHolder.item_des.setText("(" + separated[1].trim());
        } else {
            viewHolder.item_name.setText(itemdetails.getP_VirtualName());
        }

            viewHolder.item_cost.setText(context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPPriceInDoller()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getPPriceInRs()), 2));
            if((itemdetails.getP_OriginalPriceInDollar()==null)||(itemdetails.getP_OriginalPriceInRs()==null)||(itemdetails.getP_OriginalPriceInRs().isEmpty())||itemdetails.getPPriceInRs().equalsIgnoreCase(itemdetails.getP_OriginalPriceInRs())){
                viewHolder.txt_disscount.setVisibility(View.GONE);
                viewHolder.txt_sale_price.setVisibility(View.INVISIBLE);
                viewHolder.item_cost.setTextColor(context.getResources().getColor(R.color.black));

            } else {
                if (itemdetails.getP_OutOfStock().equalsIgnoreCase("false"))
                {
                    viewHolder.txt_disscount.setVisibility(View.VISIBLE);

                }
                viewHolder.txt_sale_price.setVisibility(View.VISIBLE);
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
                viewHolder.item_cost.setTextColor(context.getResources().getColor(R.color.red1));

                viewHolder.txt_disscount.setText(discoumt.trim()+"%"+"\n"+"off");

                //viewHolder.txt_sale_price.setText(context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2));
                //viewHolder.txt_sale_price.setPaintFlags(viewHolder.txt_sale_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(viewHolder.txt_sale_price, context.getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2) );

            }

        } catch (Exception e) {

        }

        CUtils.showServiceProductDiscountedText(context, viewHolder.textDiscountPlan,
                itemdetails.getMessageOfCloudPlan1(), itemdetails.getMessageOfCloudPlan2(), CGlobalVariables.FROM_PRODUCT_TEXT);

        CUtils.showBasicPlanUserText(context,viewHolder.msgForBasicPlanText,viewHolder.basicPlanUserLayout,
                itemdetails.getMessageOfCloudPlan1(),itemdetails.getMessageOfCloudPlan2());


        //  viewHolder.item_cost.setText(context.getResources().getString(R.string.astroshop_dollar_sign) + itemdetails.getPPriceInDoller() + " / " + context.getResources().getString(R.string.astroshop_rupees_sign) + itemdetails.getPPriceInRs());

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setDataTosendDescription(position);


            }
        });

        viewHolder.basicPlanUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.gotoProductPlanListUpdated(((Activity) context),
                        LANGUAGE_CODE, BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"product_list");

            }
        });


        return convertView;
    }

    public class ViewHolder {
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

    public void updateListGrid(List<AstroShopItemDetails> data) {
        //Log.e("Data in grid", "" + data.size());
        this._nameList = new ArrayList<AstroShopItemDetails>();
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
