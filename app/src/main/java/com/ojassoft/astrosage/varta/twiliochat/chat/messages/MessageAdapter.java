package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.app.Activity;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.twiliochat.chat.history.MessageChatHistoryAdapter;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.MessageDiffCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final int TYPE_MESSAGE = 0;
    private final int TYPE_STATUS = 1;

    private final int TYPE_USER = 0;
    private final int TYPE_ASTROLOGER = 1;
    private Activity mActivity;
    private List<ChatMessage> messages;
    private ArrayList<Integer> copyMessage;
    private LayoutInflater layoutInflater;
    private TreeSet<Integer> statusMessageSet = new TreeSet<>();
    private Boolean isLongPressClicked = false;
    private  String followAstro = "0";

    public MessageAdapter(Activity activity) {
        layoutInflater = activity.getLayoutInflater();
        messages = new ArrayList<>();
        copyMessage = new ArrayList<>();
        mActivity = activity;
    }


    public TreeSet<Integer>  getStatusList(){
        return statusMessageSet;
    }
    public List<ChatMessage> getMessageList(){
        return new ArrayList<>(messages);
    }

    public List<Integer> getCopyMessageList(){
        return copyMessage;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        List<ChatMessage> newList = new ArrayList<>(messages);
        MessageDiffCallback diffCallback = new MessageDiffCallback(messages, newList, mActivity);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        messages.add(new UserMessage(message));
        diffResult.dispatchUpdatesTo(this);
        notifyItemInserted(getItemCount()-1);

    }
    public void updateSeenMsg(Message message) {
        if (messages != null && messages.size() > 0) {
            int index = getIndexByChatID(message.getChatId());
            if(index != -1){
                messages.remove(index);
                messages.add(index,new UserMessage(message));
                notifyItemChanged(index);
            }
        }
    }

    public int getIndexByChatID(long chatId){
        int index = -1;
        for(int i = messages.size()-1; i >= 0; i--){
            ChatMessage data = messages.get(i);
            if (data.chatId() == chatId){
                index = i;
                break;
            }
        }
        return index;
    }


    public void addStatusMessage(StatusMessage message) {
        messages.add(message);
        //statusMessageSet.add(messages.size() - 1);
        notifyItemChanged(messages.size() - 1);
    }

    public void setStatusMessage(TreeSet<Integer> statusList) {
        statusMessageSet.addAll(statusList);
    }

    private List<ChatMessage> convertTwilioMessages(List<Message> messages) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (Message message : messages) {
            chatMessages.add(new UserMessage(message));
        }
        return chatMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.message, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View convertView = holder.itemView;
        int type = getItemViewType(position);
        LinearLayout llStatusLayout = convertView.findViewById(R.id.llStatusLayout);
        LinearLayout llMessageLayout = convertView.findViewById(R.id.llMessageLayout);
        switch (type) {
            case TYPE_MESSAGE:
                /*res = R.layout.message;
                convertView = layoutInflater.inflate(res, viewGroup, false);*/
                llMessageLayout.setVisibility(View.VISIBLE);
                llStatusLayout.setVisibility(View.GONE);
                ChatMessage message = messages.get(position);
                messageToShow(message, convertView, position);
                break;
            case TYPE_STATUS:
                /*res = R.layout.status_message;
                convertView = layoutInflater.inflate(res, viewGroup, false);*/
                llMessageLayout.setVisibility(View.GONE);
                llStatusLayout.setVisibility(View.VISIBLE);
                ChatMessage status = messages.get(position);
                LinearLayout status_msg_layout = (LinearLayout) convertView.findViewById(R.id.status_msg_layout);
                LinearLayout low_bal_layout = (LinearLayout) convertView.findViewById(R.id.low_bal_layout);
                RelativeLayout llFollowLayout = (RelativeLayout) convertView.findViewById(R.id.follow_msg_layout);

                TextView textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatusMessage);
                TextView low_bal_msg_txt = (TextView) convertView.findViewById(R.id.low_bal_msg_txt);
                TextView textViewFollow = (TextView) convertView.findViewById(R.id.textViewFollowMessage);
                TextView followAstrologer = (TextView) convertView.findViewById(R.id.followAstrologer);
                llFollowLayout.setVisibility(View.GONE);
                String statusMessage = status.getMessageBody(mActivity);
//                String statusMessage = mActivity.getResources().getString(R.string.varta_chat_intro);
                //textViewStatus.setText(statusMessage);
                /*FontUtils.changeFont(mActivity, textViewStatus, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                FontUtils.changeFont(mActivity, low_bal_msg_txt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);*/

                if(statusMessage.toLowerCase().contains("Your wallet balance")){
                    low_bal_layout.setVisibility(View.VISIBLE);
                    status_msg_layout.setVisibility(View.GONE);
                    low_bal_msg_txt.setText(statusMessage);
                }else {
                    low_bal_layout.setVisibility(View.GONE);
                    status_msg_layout.setVisibility(View.VISIBLE);
                    if (statusMessage.toLowerCase().contains("left the channel")) {
                        textViewStatus.setText(mActivity.getResources().getString(R.string.chat_end_by_you_msg));
                    } else if (statusMessage.toLowerCase().contains(mActivity.getString(R.string.varta_chat_intro))) {
                        textViewStatus.setTextColor(mActivity.getResources().getColor(R.color.black));
                        textViewStatus.setText(statusMessage);
                    } else if (statusMessage.contains(mActivity.getResources().getString(R.string.follow_astrologer))) {
                        textViewFollow.setTextColor(mActivity.getResources().getColor(R.color.black));
                        textViewFollow.setText(Html.fromHtml(statusMessage));
                        llFollowLayout.setVisibility(View.VISIBLE);
                        status_msg_layout.setVisibility(View.GONE);

                        if(ChatWindowActivity.followStatus.equalsIgnoreCase("1")) {
                            llStatusLayout.setVisibility(View.GONE);
                            followAstrologer.setText("Following");
                        } else {
                            followAstrologer.setText("Follow");
                            llStatusLayout.setVisibility(View.VISIBLE);

                        }
                    } else {
                        textViewStatus.setText(statusMessage);
                    }
                }
                followAstrologer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //     Log.d("chatEndedBy"," follow msg adapter "+ChatWindowActivity.followStatus);
                        if(ChatWindowActivity.followStatus.equalsIgnoreCase("0")) {
                            followAstro = "1";
                            followAstrologer.setText("Following");
                            CUtils.fcmAnalyticsEvents("chat_follow_btn_click",
                                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        } else {
                            followAstrologer.setText("Follow");
                            followAstro = "0";
                            CUtils.fcmAnalyticsEvents("chat_unfollowed_btn_click",
                                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        }
                        ((ChatWindowActivity) v.getContext()).onFollowClick(followAstro);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position) instanceof StatusMessage ? TYPE_STATUS : TYPE_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void messageToShow(ChatMessage message, View convertView, int position) {

        LinearLayout userView = (LinearLayout) convertView.findViewById(R.id.user_view);
        LinearLayout astrologerView = (LinearLayout) convertView.findViewById(R.id.astrologer_view);
        LinearLayout llMessageLayout = (LinearLayout) convertView.findViewById(R.id.llMessageLayout);
        String username = mActivity.getResources().getString(R.string.user_) + CUtils.getCountryCode(mActivity)+"-"+ CUtils.getUserID(mActivity);
        if (message.getAuthor().equalsIgnoreCase(CGlobalVariables.USER/*"USER_91-" +CUtils.getUserID(mActivity))*/)){
            userView.setVisibility(View.VISIBLE);
            astrologerView.setVisibility(View.GONE);

            TextView usertextViewMessage = (TextView) convertView.findViewById(R.id.usertextViewMessage);
            TextView usertextViewDate = (TextView) convertView.findViewById(R.id.usertextViewDate);
            ImageView seenImageView = (ImageView) convertView.findViewById(R.id.seenImageView);

            /*FontUtils.changeFont(mActivity, usertextViewMessage, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(mActivity, usertextViewDate, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);*/

            if(message.getMessageBody(mActivity).length()>25){
                usertextViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                //usertextViewMessage.setPadding(40,10,10,10);
            }else{
                usertextViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                // usertextViewMessage.setPadding(10,10,10,10);
            }

            if (message.isSeen()){
                seenImageView.setImageResource(R.drawable.seen_double_tick);
                //seenImageView.setColorFilter(mActivity.getResources().getColor(R.color.blue_light));
            }else {
                seenImageView.setImageResource(R.drawable.delivered_double);
                // seenImageView.setColorFilter(mActivity.getResources().getColor(R.color.Greycolor));
            }

            usertextViewMessage.setText(message.getMessageBody(mActivity));
            //usertextViewAuthor.setText(message.getAuthor());
            //usertextViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated()));
            try{
                usertextViewDate.setText(convertDate(message.getTimeStamp()));
            }catch (Exception e){
                //
            }
            setLinkclickEvent(usertextViewMessage, new MessageChatHistoryAdapter.HandleLinkClickInsideTextView() {
                public void onLinkClicked(String url) {
                    try {
                        com.ojassoft.astrosage.utils.CUtils.divertToScreen(mActivity, url, 0);
                    }catch (Exception e){
                        //
                    }
                }
            });
        } else {
            userView.setVisibility(View.GONE);
            astrologerView.setVisibility(View.VISIBLE);

            TextView textViewMessage = (TextView) convertView.findViewById(R.id.textViewMessage);
            //TextView textViewAuthor = (TextView) convertView.findViewById(R.id.textViewAuthor);
            TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);

            /*FontUtils.changeFont(mActivity, textViewMessage, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(mActivity, textViewDate, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);*/

            if(message.getMessageBody(mActivity).length()>25){
                textViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                //textViewMessage.setPadding(40,10,10,10);
            }else{
                textViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                // textViewMessage.setPadding(10,10,10,10);
            }

            textViewMessage.setText(message.getMessageBody(mActivity).trim());

            //textViewAuthor.setText(message.getAuthor());
            //textViewDate.setText(DateFormatter.getFormattedDateFromISOString(message.getDateCreated()));
            try{
                textViewDate.setText(convertDate(message.getTimeStamp()));
            }catch (Exception e){
                //
            }
            setLinkclickEvent(textViewMessage, new MessageChatHistoryAdapter.HandleLinkClickInsideTextView() {
                public void onLinkClicked(String url) {
                    try {
                        com.ojassoft.astrosage.utils.CUtils.divertToScreen(mActivity, url, 0);
                    }catch (Exception e){
                        //
                    }
                }
            });
        }

        astrologerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Log.e("SAN ", " astrologerView normal press => " + position );

                if ( isLongPressClicked ) {
                    updateCopyList(position, llMessageLayout);
                }
                //Log.e("SAN ", " messageAdapter astrologerView onClick " + message.getMessageBody(mActivity) );
                //Toast.makeText(mActivity, message.getMessageBody(mActivity), Toast.LENGTH_LONG).show();
                //CUtils.copyPasteText(mActivity, message.getMessageBody(mActivity));
            }
        });


        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("SAN ", " userView normal press => " + position );

                if ( isLongPressClicked ) {
                    updateCopyList(position, llMessageLayout);
                }

                //Log.e("SAN ", " messageAdapter userview onClick " + message.getMessageBody(mActivity) );
                //Toast.makeText(mActivity, message.getMessageBody(mActivity), Toast.LENGTH_LONG).show();
                //CUtils.copyPasteText(mActivity, message.getMessageBody(mActivity));
            }
        });


        astrologerView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Log.e("SAN ", " astrologerView long press => " + position );

                if ( isLongPressClicked ) {
                    // nothing to do
                } else {
                    isLongPressClicked = true;
                    updateCopyList(position, llMessageLayout);
                    CUtils.copyPasteText(mActivity, message.getMessageBody(mActivity));
                }

                return false;
            }

        });

        userView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.e("SAN ", " userView long press => " + position );


                if ( isLongPressClicked ) {
                    // nothing to do
                } else {
                    isLongPressClicked = true;
                    updateCopyList(position, llMessageLayout);
                    CUtils.copyPasteText(mActivity, message.getMessageBody(mActivity));
                }

                return false;
            }
        });


    }

    private void updateCopyList(int position, View view) {

        Log.e("SAN ", " updateCopyList position => " + position );
        Log.e("SAN ", " updateCopyList brfore copyMessage array => " + copyMessage.toString() );

        if ( copyMessage.contains(position) ) {

            copyMessage.remove(copyMessage.indexOf(position));
            view.setBackground( mActivity.getResources().getDrawable(R.drawable.bg_chat_selected_removed) );

        } else {

            copyMessage.add(position);
            view.setBackground( mActivity.getResources().getDrawable(R.drawable.bg_chat_selected_grey) );

        }

        if ( copyMessage.size() > 0 ) {
            ((ChatWindowActivity) mActivity ).updateCopyView(true);
        } else {
            ((ChatWindowActivity) mActivity ).updateCopyView(false);
        }

        Log.e("SAN ", " updateCopyList after copyMessage array => " + copyMessage.toString() );

    }


    public static String convertDate(long dateInMilliseconds) {

        String dateFormat = "dd MMM yyyy hh:mm a";
        String dateTime = "";
        try {
                dateTime = DateFormat.format(dateFormat, dateInMilliseconds).toString();
        } catch (Exception e) {}

        return dateTime;
    }


    public static void setLinkclickEvent(TextView tv, MessageChatHistoryAdapter.HandleLinkClickInsideTextView clickInterface) {
        try {
            String text = tv.getText().toString();
            //Log.e("LinkClick = ", "Val = " + text);
            String str = "([Hh][tT][tT][pP][sS]?:\\/\\/[^ ,'\">\\]\\)]*[^\\. ,'\">\\]\\)])";
            Pattern pattern = Pattern.compile(str);
            Matcher matcher = pattern.matcher(tv.getText());
            while (matcher.find()) {
                int x = matcher.start();
                int y = matcher.end();
                final android.text.SpannableString f = new android.text.SpannableString(
                        tv.getText());
                MessageChatHistoryAdapter.InternalURLSpan span = new MessageChatHistoryAdapter.InternalURLSpan();
                span.setText(text.substring(x, y));
                span.setClickInterface(clickInterface);
                f.setSpan(span, x, y,
                        android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(f);
            }
            tv.setLinksClickable(true);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            tv.setFocusable(false);
        }catch (Exception e){
            //
        }
    }

    public static class InternalURLSpan extends android.text.style.ClickableSpan {

        private String text;
        private MessageChatHistoryAdapter.HandleLinkClickInsideTextView clickInterface;

        @Override
        public void onClick(View widget) {
            getClickInterface().onLinkClicked(getText());
        }

        public void setText(String textString) {
            this.text = textString;
        }

        public String getText() {
            return this.text;
        }

        public void setClickInterface(MessageChatHistoryAdapter.HandleLinkClickInsideTextView clickInterface) {
            this.clickInterface = clickInterface;
        }

        public MessageChatHistoryAdapter.HandleLinkClickInsideTextView getClickInterface() {
            return this.clickInterface;
        }

    }

    public interface HandleLinkClickInsideTextView {
        public void onLinkClicked(String url);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void appendMessages(final ArrayList<ChatMessage> list){
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            messages.addAll(0, list);
            notifyItemRangeInserted(0, list.size() - 1);
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messages.addAll(0, list);
                    notifyItemRangeInserted(0, list.size() - 1);
                }
            });
        }
    }

    public void removeFlowAstrologer(){
        for(int i = messages.size()-1; i >=0 ; i--){
            if(messages.get(i) instanceof StatusMessage){
                messages.remove(i);
                break;
            }
        }
    }

    public void removeIfAvailable(Message message){
        if (messages != null && messages.size() > 0) {
            int index = getIndexByChatID(message.getChatId());
            if(index != -1){
                messages.remove(index);
                notifyItemRemoved(index);
            }
        }
    }
}
