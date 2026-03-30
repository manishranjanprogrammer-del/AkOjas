package com.ojassoft.astrosage.customadapters;

import android.content.Context;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.interfaces.RecyclerClickListner;
import com.libojassoft.android.models.NotificationModel;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

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
                holder.timeText.setText(CUtils.convertTimeInString(context,Long.parseLong(dateTime)));
            }

            if (holder.lineViewLL != null) {
                if (position == notificationModels.size() - 1) {
                    holder.lineViewLL.setVisibility(View.GONE);
                } else {
                    holder.lineViewLL.setVisibility(View.VISIBLE);
                }
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout containerLayout;
        public LinearLayout lineViewLL;
        public TextView titleText;
        public TextView msgText;
        public TextView timeText;
        ImageView dropdownArrowBtn;
        CardView cardView;

        public MyViewHolder(View view) {
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
            //recyclerClickListner.onItemClick(getAdapterPosition(), v);
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

    public void setOnClickListner(RecyclerClickListner recyclerClickListner) {
        this.recyclerClickListner = recyclerClickListner;
    }
}
