package com.ojassoft.vartalive.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.ChatMessageModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class LiveStreamMsgDetailAdapter extends RecyclerView.Adapter<LiveStreamMsgDetailAdapter.ViewHolder> {

    Activity currentActivity;
    List<ChatMessageModel> messageDetailModelList;
    Bundle bundle;
    private RecyclerClickListner recyclerClickListner;
    public String[] mColors = {"#3F51B5","#FF9800","#009688","#673AB7"};

    public LiveStreamMsgDetailAdapter(Activity currentActivity, List<ChatMessageModel> messageDetailModelList) {
        this.currentActivity = currentActivity;
        this.messageDetailModelList = messageDetailModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.row_item_live_stream_msg_details, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Log.e("msg ", " msgAdapter inside onBindViewHolder() " );
        if (messageDetailModelList != null && messageDetailModelList.size() > position) {
            final ChatMessageModel ChatMessageModel = messageDetailModelList.get(position);
            if (ChatMessageModel == null) return;
            try {
                String s;
                if(ChatMessageModel.getFrom().equalsIgnoreCase("System") ||
                        ChatMessageModel.getFrom().equalsIgnoreCase("Payment") ) {
                    holder.fromTV.setVisibility(View.GONE);
                    s=ChatMessageModel.getText().substring(0,1);
                } else {
                    holder.fromTV.setVisibility(View.VISIBLE);
                    s=ChatMessageModel.getFrom().substring(0,1);
                }

                holder.nameTV.setText(s);
                holder.fromTV.setText(ChatMessageModel.getFrom());
                holder.messageTV.setText( ChatMessageModel.getText() );

                Log.e("msg ", " msgAdapter msg=> " + ChatMessageModel.getText() );

            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.nameTvParent.setCardBackgroundColor(Color.parseColor(mColors[position % 4]));

            String chatType = ChatMessageModel.getType();
            if(chatType == null){
                chatType = "";
            }
            if(chatType.equalsIgnoreCase(CGlobalVariables.CHAT_TYPE_GIFT)) {
                holder.bgMsgLL.setBackgroundResource(R.drawable.bg_live_steaming_chat_gift);
            }else {
                holder.bgMsgLL.setBackgroundResource(R.drawable.bg_live_steaming_chat_trans);
            }
        }

    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }


    @Override
    public int getItemCount() {
        if (messageDetailModelList == null) return 0;
        return messageDetailModelList.size();
    }

    public void setOnItemClickListner(RecyclerClickListner recyclerClickListner) {
        this.recyclerClickListner = recyclerClickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTV;
        TextView fromTV;
        TextView messageTV;
        CardView nameTvParent;
        LinearLayout bgMsgLL;

        public ViewHolder(View itemView) {
            super(itemView);

            messageTV = itemView.findViewById(R.id.messageTV);
            fromTV = itemView.findViewById(R.id.fromTV);
            nameTV = itemView.findViewById(R.id.nameTv);
            nameTvParent = itemView.findViewById(R.id.nameTvParent);
            bgMsgLL = itemView.findViewById(R.id.bgMsgLL);

            FontUtils.changeFont(currentActivity, fromTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, messageTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, nameTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        }
        @Override
        public void onClick(View v) {
            //recyclerClickListner.onItemClick(getAdapterPosition(), v);
        }
    }
    
}


