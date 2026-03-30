package com.ojassoft.astrosage.varta.adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.varta.dialog.QuickRechargeBottomSheet;
import com.ojassoft.astrosage.varta.dialog.RechargeSuggestionBottomSheet;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.OnItemClickListener;
import com.ojassoft.astrosage.vartalive.activities.LiveActivityNew;

public class WalletRechargeSuggestionAdapter
        extends RecyclerView.Adapter<WalletRechargeSuggestionAdapter.ViewHolder> {
    int fontSize = 18;
    private WalletAmountBean walletAmountBean;
    private Context context;
    private OnItemClickListener listener;
    public WalletRechargeSuggestionAdapter(Context context, WalletAmountBean walletAmountBean,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.walletAmountBean = walletAmountBean;
        this.listener = onItemClickListener;
        getAmountLength();
    }

    public WalletAmountBean.Services getSelectedItem() {
        for (WalletAmountBean.Services item : walletAmountBean.getServiceList()) {
            if (item.isSelected()) return item;
        }
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recharge_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        WalletAmountBean.Services data = walletAmountBean.getServiceList().get(position);
        if (!data.getServiceid().equals(CGlobalVariables.SERVICE_ID_MORE_RECHARGES)) {
            viewHolder.txtViewRupees.setVisibility(View.VISIBLE);
            viewHolder.amount.setText(CUtils.convertAmtIntoIndianFormat(data.getActualraters()));
            viewHolder.amount.setTextSize(16);
            if (!data.getOffermessage().isEmpty()) {
                viewHolder.offer.setVisibility(View.VISIBLE);
            } else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);
                viewHolder.linLayoutCurrencySymbol.setLayoutParams(params);
                viewHolder.offer.setVisibility(View.GONE);

                viewHolder.imageFl.setVisibility(View.GONE);
            }
            viewHolder.offer.setText(data.getOffermessage());


        } else {
            if (data.getOffermessage().isEmpty()) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);
                viewHolder.linLayoutCurrencySymbol.setLayoutParams(params);
                viewHolder.offer.setVisibility(View.GONE);
                viewHolder.imageFl.setVisibility(View.GONE);
            }
            viewHolder.amount.setGravity(Gravity.CENTER);
            viewHolder.amount.setText(data.getServicename());
            viewHolder.amount.setTextSize(13);
            viewHolder.txtViewRupees.setVisibility(View.GONE);
        }
        FontUtils.changeFont(context, viewHolder.amount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, viewHolder.offer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getServiceid().equals(CGlobalVariables.SERVICE_ID_MORE_RECHARGES)) {
                    try {
                        if (context != null) {
                            com.ojassoft.astrosage.utils.CUtils.createSession(context, CGlobalVariables.PID_INSUFFICIANT_BALANCE_CLICK);
                            if (context instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) context).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                            } else if (context instanceof DashBoardActivity) {
                                ((DashBoardActivity) context).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                            } else if (context instanceof ActAppModule) {
                                ((ActAppModule) context).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                            } else if (context instanceof ChatWindowActivity) {
                                ((ChatWindowActivity) context).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                            } else if (context instanceof AIChatWindowActivity) {
                                ((AIChatWindowActivity) context).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                            } else if (context instanceof AIVoiceCallingActivity) {
                                ((AIVoiceCallingActivity) context).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                            } else if (context instanceof LiveActivityNew) {
                                ((LiveActivityNew) context).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                            } else {
                                ((BaseActivity) context).openWalletScreenDashboard(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                            }

//                            if(RechargeSuggestionBottomSheet.getInstance() != null && RechargeSuggestionBottomSheet.getInstance().isAdded()){
//                                RechargeSuggestionBottomSheet.getInstance().dismiss();
//                            }
                        }
                    } catch (Exception e) {
                        //
                    }
                }else {
                    for (int i = 0; i < walletAmountBean.getServiceList().size(); i++) {
                        walletAmountBean.getServiceList().get(i).setSelected(false);
                    }
                    walletAmountBean.getServiceList().get(position).setSelected(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onRechargeServicesClick();
                        }
                    },300);

                }
                notifyDataSetChanged();
            }
        });
        if (data.isSelected()) {
            viewHolder.relLayoutWalletPrice.setBackground(
                    ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.bg_border_wallet_card_selected)
            );
            viewHolder.cardViewSavePrice.setBackground(
                    ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.bg_recharge_item_offer_selected)
            );
            viewHolder.offer.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.text_color_black));
            viewHolder.amount.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorPrimary_day_night));
            viewHolder.txtViewRupees.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorPrimary_day_night));
        } else {
            viewHolder.relLayoutWalletPrice.setBackground(
                    ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.bg_border_wallet_card)
            );
            viewHolder.cardViewSavePrice.setBackground(
                    ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.bg_recharge_item_offer)
            );
            viewHolder.offer.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorPrimary_day_night));
            viewHolder.amount.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.text_color_black));
            viewHolder.txtViewRupees.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.text_color_black));

        }

    }

    @Override
    public int getItemCount() {
        return walletAmountBean.getServiceList().size();
    }

    private void getAmountLength() {
        for (WalletAmountBean.Services item : walletAmountBean.getServiceList()) {
            if (CUtils.convertAmtIntoIndianFormat(item.getActualraters()).length() > 5) {
                fontSize = 13;
            } else {
                fontSize = 15;
            }

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, txtViewRupees, offer;
        LinearLayout linLayoutCurrencySymbol;
        ConstraintLayout container;
        FrameLayout imageFl, cardViewSavePrice;
        RelativeLayout relLayoutWalletPrice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.recharge1_tv);
            txtViewRupees = itemView.findViewById(R.id.txtViewRupees);
            linLayoutCurrencySymbol = itemView.findViewById(R.id.linLayoutCurrencySymbol);
            offer = itemView.findViewById(R.id.recharge1_offer_tv);
            container = itemView.findViewById(R.id.recharge1_ll);
            imageFl = itemView.findViewById(R.id.imagefl);
            relLayoutWalletPrice = itemView.findViewById(R.id.relLayoutWalletPrice);
            cardViewSavePrice = itemView.findViewById(R.id.cardViewSavePrice);
            amount.setTextSize(fontSize);
            txtViewRupees.setTextSize(fontSize + 2);
        }
    }
}

