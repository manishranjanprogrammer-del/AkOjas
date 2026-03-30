package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.GiftModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class LiveGiftAdapter extends RecyclerView.Adapter<LiveGiftAdapter.MyViewHolder> {
    public OnItemClick onItemClick;
    private final Activity activity;
    private final ArrayList<GiftModel> giftModelArrayList;
    private boolean changeTextColor = false;

    public LiveGiftAdapter(Activity activity, ArrayList<GiftModel> giftModelArrayList) {
        this.activity = activity;
        this.giftModelArrayList = giftModelArrayList;
    }

    public LiveGiftAdapter(Activity activity, ArrayList<GiftModel> giftModelArrayList, boolean changeTextColor) {
        this.activity = activity;
        this.giftModelArrayList = giftModelArrayList;
        this.changeTextColor = changeTextColor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_gift, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            if (giftModelArrayList != null && giftModelArrayList.size() > position) {
                GiftModel giftModel = giftModelArrayList.get(position);
                if (!TextUtils.isEmpty(giftModel.getSmalliconfile())) {
                    holder.giftImg.setImageUrl((CUtils.getGiftImageBaseUrl() + giftModel.getSmalliconfile()), VolleySingleton.getInstance(activity).getImageLoader());
                }

                holder.giftNameTV.setText(giftModel.getServicename());
                String price = giftModel.getActualraters();
                try {
                    if (!TextUtils.isEmpty(price) && price.contains(".")) {
                        int intPrice = (int) Float.parseFloat(price); //remove decimal points
                        price = String.valueOf(intPrice);
                    }
                } catch (Exception e){
                    //
                }
                holder.giftPriceTV.setText(activity.getResources().getString(R.string.rs_sign) + price);

                if (giftModel.isSelected()) {
                    holder.llGiftBg.setBackground(activity.getResources().getDrawable(R.drawable.live_gift_bg_selected));
                } else {
                    holder.llGiftBg.setBackground(activity.getResources().getDrawable(R.drawable.live_gift_bg_normal));
                }

                if (changeTextColor) {
                    holder.giftNameTV.setTextColor(ContextCompat.getColor(activity,R.color.black));
                    holder.giftPriceTV.setTextColor(ContextCompat.getColor(activity,R.color.black));
                } else {
                    holder.giftNameTV.setTextColor(ContextCompat.getColor(activity,R.color.no_change_white));
                    holder.giftPriceTV.setTextColor(ContextCompat.getColor(activity,R.color.no_change_white));
                }

                holder.parentlayout.setOnClickListener(view -> {

                    if (Float.parseFloat(giftModel.getActualraters()) > Float.parseFloat(CUtils.getWalletRs(activity))) {
                        CUtils.showSnackbar(holder.parentlayout, activity.getResources().getString(R.string.insufficient_wallet_balance), activity);
                    } else {

                        onItemClick.itemClick(view, holder.getAdapterPosition());

                    }

                });
            }
        }catch (Exception e){
            //
        }
    }

    @Override
    public int getItemCount() {
        return giftModelArrayList != null ? giftModelArrayList.size() : 0;
    }


    public interface OnItemClick {
        void itemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView giftImg;
        TextView giftNameTV, giftPriceTV;
        LinearLayout parentlayout, llGiftBg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentlayout = itemView.findViewById(R.id.parentlayout);
            llGiftBg = itemView.findViewById(R.id.llGiftBg);
            giftImg = itemView.findViewById(R.id.giftImg);
            giftNameTV = itemView.findViewById(R.id.giftNameTV);
            giftPriceTV = itemView.findViewById(R.id.giftPriceTV);

            try {
                FontUtils.changeFont(activity, giftNameTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                FontUtils.changeFont(activity, giftPriceTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            } catch (Exception e){
                //
            }
        }
    }
}
