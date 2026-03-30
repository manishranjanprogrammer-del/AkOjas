package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.libojassoft.android.models.NotificationModel;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;

import java.util.List;

public class NotificationAdapterNew extends RecyclerView.Adapter {
    int SPECIAL_NOTIFICATION = 0;
    int DEFAULT_NOTIFICATION = 1;

    private RecyclerClickListner recyclerClickListner;
    private Context context;
    private List<NotificationModel> notificationModels;

    public NotificationAdapterNew(Context context, List<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == DEFAULT_NOTIFICATION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_notification, parent, false);
            return new DefaultNotiViewHolder(view);
        } else if (viewType == SPECIAL_NOTIFICATION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_items_notification, parent, false);
            return new SpecialNotiViewHolder(view);
        }  else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DefaultNotiViewHolder) {
            if (notificationModels != null && notificationModels.size() > position) {
                NotificationModel notificationModel = notificationModels.get(position);
                if (notificationModel == null) {
                    return;
                }


                ((DefaultNotiViewHolder) holder).titleText.setText(notificationModel.getTitle());
                ((DefaultNotiViewHolder) holder).msgText.setText(notificationModel.getMessage());
                String dateTime = notificationModel.getTimestamp();
                if (!TextUtils.isEmpty(dateTime)) {
                    ((DefaultNotiViewHolder) holder).timeText.setText(CUtils.convertTimeInString(context,Long.parseLong(dateTime)));
                }

//                if (position % 2 == 0) {
//                    ((DefaultNotiViewHolder) holder).cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.bg_notification_light_purple));
//                } else {
//                    ((DefaultNotiViewHolder) holder).cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.bg_notification_light_orange));
//                }

                ((DefaultNotiViewHolder) holder).dropdownArrowBtn.setTag(position);
                ((DefaultNotiViewHolder) holder).cardView.setTag(position);
            }
        } else if (holder instanceof SpecialNotiViewHolder) {
            if (notificationModels != null && notificationModels.size() > position) {
                NotificationModel notificationModel = notificationModels.get(position);
                if (notificationModel == null) {
                    return;
                }
                ((SpecialNotiViewHolder) holder).titleText.setText(notificationModel.getTitle());
                ((SpecialNotiViewHolder) holder).messageText.setText(notificationModel.getMessage());
                ((SpecialNotiViewHolder) holder).cardView.setTag(position);

            }
        }
    }

    @Override
    public int getItemCount() {
        if (notificationModels == null) return 0;
        return notificationModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (notificationModels.get(position).getImgUrl().contains("FIRSTCHATFREE"))
                return SPECIAL_NOTIFICATION;
            else return DEFAULT_NOTIFICATION;
        } catch (Exception e){
            return DEFAULT_NOTIFICATION;
        }
    }

    public  class DefaultNotiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout containerLayout;
        public LinearLayout lineViewLL;
        public TextView titleText;
        public TextView msgText;
        public TextView timeText;
        ImageView dropdownArrowBtn;
        CardView cardView;

        public DefaultNotiViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            dropdownArrowBtn = view.findViewById(R.id.dropdown_arrow_btn);
            lineViewLL = view.findViewById(R.id.lineViewLL);
            containerLayout = view.findViewById(R.id.containerLayout);
            titleText = view.findViewById(R.id.title_text);
            msgText = view.findViewById(R.id.message_text);
            timeText = view.findViewById(R.id.time_text);

            FontUtils.changeFont(context, titleText, AppConstants.FONT_ROBOTO_MEDIUM);
            FontUtils.changeFont(context, timeText, AppConstants.FONT_ROBOTO_REGULAR);
            FontUtils.changeFont(context, msgText, AppConstants.FONT_ROBOTO_REGULAR);

            //view.setOnClickListener(this);
            cardView.setOnClickListener(this);
            dropdownArrowBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.card_view) {
                recyclerClickListner.onItemClick(getAdapterPosition(), v);
            }else if(v.getId() == R.id.dropdown_arrow_btn){
                int pos = Integer.parseInt(dropdownArrowBtn.getTag().toString());
                if(notificationModels.get(pos).isDropDownShow().equalsIgnoreCase("true")){
                    notificationModels.get(pos).setDropDownShow("false");
                    msgText.setSingleLine(true);
                    msgText.setMaxLines(1);
                    msgText.setEllipsize(TextUtils.TruncateAt.END);
                    msgText.setText(notificationModels.get(pos).getMessage());
                    dropdownArrowBtn.setImageResource(R.drawable.ic_keyboard_arrow_down_black);
                }else{
                    notificationModels.get(pos).setDropDownShow("true");
                    msgText.setSingleLine(false);
                    msgText.setMaxLines(4);
                    msgText.setEllipsize(TextUtils.TruncateAt.END);
                    msgText.setText(notificationModels.get(pos).getMessage());
                    dropdownArrowBtn.setImageResource(R.drawable.ic_keyboard_arrow_up_black);
                }
                //Toast.makeText(context,"DROP DOWN "+ pos,Toast.LENGTH_LONG).show();
            }
        }
    }

    public  class SpecialNotiViewHolder extends RecyclerView.ViewHolder {
        TextView titleText,messageText;
        Button btnFreeCall;
        CardView cardView;
        public SpecialNotiViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
            messageText = itemView.findViewById(R.id.message_text);
            btnFreeCall = itemView.findViewById(R.id.btn_free_call);
            cardView = itemView.findViewById(R.id.card_view);
            FontUtils.changeFont(context, titleText, AppConstants.FONT_ROBOTO_MEDIUM);
            FontUtils.changeFont(context, messageText, AppConstants.FONT_ROBOTO_REGULAR);
            FontUtils.changeFont(context, btnFreeCall, AppConstants.FONT_ROBOTO_MEDIUM);
            btnFreeCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("notification_center_get_free_call_now", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(context)) {
                        try{
                            FragmentActivity activity = (FragmentActivity)(context);
                            FragmentManager fm = activity.getSupportFragmentManager();
                            PopUpLogin popUpLogin = new PopUpLogin
                                    ("notification_center",
                                            "ADAPTER");
                            popUpLogin.show(fm, "PopUpFreeCall");
                        }catch (Exception e){
                            //
                        }

                    } else {
                        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(context);
                        if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                            //eventsFreeCallChat();
                            com.ojassoft.astrosage.varta.utils.CUtils.popUpLoginFreeChatClicked = true;
//                            if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context).equals("91")) {
//                                com.ojassoft.astrosage.varta.utils.CUtils.popUpLoginFreeChatClicked = true;
//                            } else {
//                                com.ojassoft.astrosage.varta.utils.CUtils.popUpLoginFreeCallClicked = true;
//                            }
                        }
                        //CUtils.popUpLoginFreeCallClicked = true;

                        Intent i = new Intent(context, DashBoardActivity.class);
                        i.putExtra(CGlobalVariables.IS_FROM_SCREEN, "ActNotificationCenter");
                        context.startActivity(i);
                    }
                }
            });
        }

    }
    public void setOnClickListner(RecyclerClickListner recyclerClickListner) {
        this.recyclerClickListner = recyclerClickListner;
    }

}
