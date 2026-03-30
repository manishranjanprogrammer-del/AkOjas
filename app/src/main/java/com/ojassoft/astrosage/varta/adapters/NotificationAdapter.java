package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.NotificationModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private RecyclerClickListner recyclerClickListner;
    private Context context;
    private List<NotificationModel> notificationModels;

    public NotificationAdapter(Context context, List<NotificationModel> notificationModels) {
        this.notificationModels = notificationModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_notification, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (notificationModels != null && notificationModels.size() > position) {
            NotificationModel notificationModel = notificationModels.get(position);
            if (notificationModel == null) {
                return;
            }


            holder.titleText.setText(notificationModel.getTitle());
            holder.msgText.setText(notificationModel.getMessage());
            String dateTime = notificationModel.getTimestamp();
            if (!TextUtils.isEmpty(dateTime)) {
                holder.timeText.setText(CUtils.convertTimeInString(context, Long.parseLong(dateTime)));
            }
            if (position % 2 == 0) {
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_purple));
            } else {
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_orange));
            }

            holder.dropdownArrowBtn.setTag(position);
            holder.cardView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        if (notificationModels == null) return 0;
        return notificationModels.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public void setOnClickListner(RecyclerClickListner recyclerClickListner) {
        this.recyclerClickListner = recyclerClickListner;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public TextView titleText;
        public TextView msgText;
        public TextView timeText;
        ImageView dropdownArrowBtn;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            titleText = view.findViewById(R.id.title_text);
            msgText = view.findViewById(R.id.message_text);
            timeText = view.findViewById(R.id.time_text);
            dropdownArrowBtn = view.findViewById(R.id.dropdown_arrow_btn);
            FontUtils.changeFont(context, titleText, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, timeText, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, msgText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

            view.setOnClickListener(this);
            dropdownArrowBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //recyclerClickListner.onItemClick(getAdapterPosition(), v);
            if(v.getId() == R.id.card_view) {
                recyclerClickListner.onItemClick(getAdapterPosition(), v);
            }else if(v.getId() == R.id.dropdown_arrow_btn){
                int pos = Integer.parseInt(dropdownArrowBtn.getTag().toString());
                if(notificationModels.get(pos).isDropDownShow().equalsIgnoreCase("true")){
                    notificationModels.get(pos).setDropDownShow("false");
                    msgText.setSingleLine(true);
                    msgText.setText(notificationModels.get(pos).getMessage());
                    dropdownArrowBtn.setImageResource(R.drawable.ic_keyboard_arrow_down_black);
                }else{
                    notificationModels.get(pos).setDropDownShow("true");
                    msgText.setSingleLine(false);
                    msgText.setText(notificationModels.get(pos).getMessage());
                    dropdownArrowBtn.setImageResource(R.drawable.ic_keyboard_arrow_up_black);
                }
                //Toast.makeText(context,"DROP DOWN "+ pos,Toast.LENGTH_LONG).show();
            }
        }
    }
}
