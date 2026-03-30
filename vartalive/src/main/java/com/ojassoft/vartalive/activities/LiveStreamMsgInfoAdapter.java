package com.ojassoft.vartalive.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.ChatMessageModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.List;

public class LiveStreamMsgInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_MSG = 0;
    private static final int TYPE_GIFT = 1;
    private static final int TYPE_FOLLOW = 2;
    Activity currentActivity;
    List<ChatMessageModel> messageDetailModelList;
    Bundle bundle;
    private RecyclerClickListner recyclerClickListner;
    public String[] mColors = {"#3F51B5","#FF9800","#009688","#673AB7"};
    public OnItemClickListener onItemClickListener;
    public LiveStreamMsgInfoAdapter(Activity currentActivity, List<ChatMessageModel> messageDetailModelList,OnItemClickListener listener) {
        this.currentActivity = currentActivity;
        this.messageDetailModelList = messageDetailModelList;
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MSG) {
            // Here Inflating your recyclerview item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_live_stream_msg_details, parent, false);
            return new MsgViewHolder(itemView);
        } else if (viewType == TYPE_GIFT) {
            // Here Inflating your header viewquestion_price_txt
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_live_stream_msg_details, parent, false);
            return new GiftViewHolder(itemView);
        }else if (viewType == TYPE_FOLLOW) {
            // Here Inflating your header viewquestion_price_txt
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_live_stream_msg_details, parent, false);
            return new FollowViewHolder(itemView);
        }
         else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch ( holder.getItemViewType () ) {
            case TYPE_MSG:
                MsgViewHolder msgViewHolder = (MsgViewHolder) holder;
                msgViewHolder.llMsgParentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick();
                    }
                });
                if (messageDetailModelList != null && messageDetailModelList.size() > position) {
                    final ChatMessageModel ChatMessageModel = messageDetailModelList.get(position);
                    if (ChatMessageModel == null) return;
                    try {
                        String s;
                        if(ChatMessageModel.getFrom().equalsIgnoreCase("System") ||
                                ChatMessageModel.getFrom().equalsIgnoreCase("Payment") ) {
                            msgViewHolder.fromTV.setVisibility(View.GONE);
                            s=ChatMessageModel.getText().substring(0,1);
                        } else {
                            msgViewHolder.fromTV.setVisibility(View.VISIBLE);
                            s=ChatMessageModel.getFrom().substring(0,1);
                        }

                        msgViewHolder.nameTV.setText(s);
                        msgViewHolder.fromTV.setText(ChatMessageModel.getFrom());
                        msgViewHolder.messageTV.setText( ChatMessageModel.getText() );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    msgViewHolder.nameTvParent.setCardBackgroundColor(Color.parseColor(mColors[position % 4]));

                    msgViewHolder.bgMsgLL.setBackgroundResource(R.drawable.bg_live_steaming_chat_trans);

                }

                break;
            case TYPE_GIFT:
                GiftViewHolder giftViewHolder = (GiftViewHolder) holder;
                if (messageDetailModelList != null && messageDetailModelList.size() > position) {
                    final ChatMessageModel ChatMessageModel = messageDetailModelList.get(position);
                    if (ChatMessageModel == null) return;
                    try {
                        String s;
                        if(ChatMessageModel.getFrom().equalsIgnoreCase("System") ||
                                ChatMessageModel.getFrom().equalsIgnoreCase("Payment") ) {
                            giftViewHolder.fromTV.setVisibility(View.GONE);
                            s=ChatMessageModel.getText().substring(0,1);
                        } else {
                            giftViewHolder.fromTV.setVisibility(View.VISIBLE);
                            s=ChatMessageModel.getFrom().substring(0,1);
                        }

                        giftViewHolder.nameTV.setText(s);
                        giftViewHolder.fromTV.setText(ChatMessageModel.getFrom());
                        giftViewHolder.messageTV.setText( ChatMessageModel.getText() );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    giftViewHolder.nameTvParent.setCardBackgroundColor(Color.parseColor(mColors[position % 4]));

                    giftViewHolder.bgMsgLL.setBackgroundResource(R.drawable.bg_live_steaming_chat_gift);


                }

                break;
            case TYPE_FOLLOW:
                FollowViewHolder followViewHolder = (FollowViewHolder) holder;
                if (messageDetailModelList != null && messageDetailModelList.size() > position) {
                    final ChatMessageModel ChatMessageModel = messageDetailModelList.get(position);
                    if (ChatMessageModel == null) return;
                    try {
                        String s;
                        if(ChatMessageModel.getFrom().equalsIgnoreCase("System") ||
                                ChatMessageModel.getFrom().equalsIgnoreCase("Payment") ) {
                            followViewHolder.fromTV.setVisibility(View.GONE);
                            s=ChatMessageModel.getText().substring(0,1);
                        } else {
                            followViewHolder.fromTV.setVisibility(View.VISIBLE);
                            s=ChatMessageModel.getFrom().substring(0,1);
                        }

                        followViewHolder.nameTV.setText(s);
                        followViewHolder.fromTV.setText(ChatMessageModel.getFrom());
                        followViewHolder.messageTV.setText( ChatMessageModel.getText() );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    followViewHolder.nameTvParent.setCardBackgroundColor(Color.parseColor(mColors[position % 4]));

                    followViewHolder.bgMsgLL.setBackgroundResource(R.drawable.bg_live_steaming_chat_gift);


                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        if (messageDetailModelList == null) return 0;
        return messageDetailModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageDetailModelList != null && messageDetailModelList.size() > position) {
            final ChatMessageModel ChatMessageModel = messageDetailModelList.get(position);
            if (ChatMessageModel == null) return  101;
            String chatType = ChatMessageModel.getType();

            if(!TextUtils.isEmpty(chatType) && chatType.equalsIgnoreCase(CGlobalVariables.CHAT_TYPE_GIFT)) {
                return TYPE_GIFT;
            }else if(!TextUtils.isEmpty(chatType) && chatType.equalsIgnoreCase(CGlobalVariables.CHAT_TYPE_FOLLOW)) {
                return TYPE_FOLLOW;
            }else {
                return TYPE_MSG;
            }
        }
        return  101;
    }

    public class MsgViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView fromTV;
        TextView messageTV;
        CardView nameTvParent;
        LinearLayout bgMsgLL,llMsgParentLayout;
        public MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.messageTV);
            fromTV = itemView.findViewById(R.id.fromTV);
            nameTV = itemView.findViewById(R.id.nameTv);
            nameTvParent = itemView.findViewById(R.id.nameTvParent);
            bgMsgLL = itemView.findViewById(R.id.bgMsgLL);
            llMsgParentLayout = itemView.findViewById(R.id.llMsgParentLayout);

            FontUtils.changeFont(currentActivity, fromTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, messageTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(currentActivity, nameTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        }
    }
    public class GiftViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView fromTV;
        TextView messageTV;
        CardView nameTvParent;
        LinearLayout bgMsgLL;

        public GiftViewHolder(@NonNull View itemView) {
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
    }
    public class FollowViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView fromTV;
        TextView messageTV;
        CardView nameTvParent;
        LinearLayout bgMsgLL;

        public FollowViewHolder(@NonNull View itemView) {
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
    }
    public interface OnItemClickListener {
        void onItemClick();
    }
}
