package com.ojassoft.astrosage.varta.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.ui.ChatListener;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.MessageDiffCallback;
import com.ojassoft.astrosage.varta.utils.TypeWritterKundali;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class KundliAIChatMsgAdapter extends RecyclerView.Adapter<KundliAIChatMsgAdapter.MyHolder> implements View.OnClickListener {
    private final int TYPE_MESSAGE = 0;
    private final int TYPE_STATUS = 1;
    private final int TYPE_USER = 0;
    private final int TYPE_ASTROLOGER = 1;
    private final Activity mActivity;
    private List<ChatMessage> messages;
    public final ArrayList<Integer> copyMessage;
    public final ArrayList<ChatMessage> messagesToDeleteList;
    private final LayoutInflater layoutInflater;
    private final TreeSet<Integer> statusMessageSet = new TreeSet<>();

    boolean flag = false;
    public Boolean isLongPressClicked = false;
    private final int lang;
    public ChatListener listener;



    public KundliAIChatMsgAdapter(Activity activity, ChatListener listener) {
        layoutInflater = activity.getLayoutInflater();
        messages = new ArrayList<>();
        copyMessage = new ArrayList<>();
        messagesToDeleteList = new ArrayList<>();
        mActivity = activity;
        lang = CUtils.getIntData(mActivity, CGlobalVariables.app_language_key, CGlobalVariables.ENGLISH);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.kundli_ai_chat_message_item_new, parent, false);
        return new MyHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ChatMessage message = messages.get(position);
        messageToShow(message, holder.itemView, position);
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_MESSAGE;
    }


    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (messages == null) {
            return 0;
        } else return messages.size();
    }




    public TreeSet<Integer> getStatusList() {
        return statusMessageSet;
    }

    public List<ChatMessage> getMessageList() {
        return new ArrayList<>(messages);
    }

    public List<ChatMessage> getUpdatedMessageList() {
        List<ChatMessage> list = new ArrayList<>();

        for (ChatMessage item : messages) {
            Message message = new Message();
            message.setAuthor(item.getAuthor());
            message.setDateCreated(item.getDateCreated());
            message.setMessageBody(item.getMessageBody(mActivity));
            message.setSeen(false);
            message.setChatId(item.chatId());
            message.setLike(item.getLike());
            message.setUnlike(item.getUnlike());
            message.setIsError(item.getIsError());
            list.add(new UserMessage(message));
        }

        return list;
    }

    public List<Integer> getCopyMessageList() {
        return copyMessage;
    }

    public void addMessage(Message message) {
        flag = false;
        updateMessageList(message);
    }

    public void updateMessageInAdapter(String updatedText) {
        int lastItem = getItemCount() - 1;
        ChatMessage chatMessage = messages.get(lastItem);
        chatMessage.setMessageBody(updatedText);
    }

    public void refreshMessageList(List<ChatMessage> history) {
        if(messages != null) {
            messages.clear();
            messages.addAll(history);
//            Log.e("greetingCheck", "refreshMessageList: "+history.size() );
        }
    }

    public void appendMessageList(ArrayList<ChatMessage> list){
        messages.addAll(0,list);
        notifyItemRangeInserted(0,list.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessages(List<ChatMessage> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
        if (mActivity instanceof MiniChatWindow)
            ((MiniChatWindow) mActivity).scrollMyListViewToBottom();
    }

    public void updateMessageList(Message message) {
        List<ChatMessage> newList = new ArrayList<>(messages);
        newList.add(new UserMessage(message));

        MessageDiffCallback diffCallback = new MessageDiffCallback(messages, newList, mActivity);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        messages.add(new UserMessage(message));
        diffResult.dispatchUpdatesTo(this);
        notifyItemInserted(getItemCount() - 1);
  //      Log.e("greetingCheck", "updateMessageList: for message"+message.getMessageBody() );

    }

    public boolean isStopTyping = false;


    public void refreshSingleItem(String greeting) {
        messages.get(0).setMessageBody(greeting.replace("\\n\\n","<br><br>"));
    }

    public void removeMessage(Message message) {
        messages.remove(messages.indexOf(message));
        notifyDataSetChanged();
    }
    public void removeMessageByChatId(long chatId) {
        for (int i = 0; i < messages.size(); i++) {
            if(messages.get(i).chatId() == chatId) {
                messages.remove(messages.get(i));
               // break;
            }
        }
        notifyDataSetChanged();
    }

    private List<ChatMessage> convertTwilioMessages(List<Message> messages) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (Message message : messages) {
            chatMessages.add(new UserMessage(message));
        }
        return chatMessages;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void messageToShow(ChatMessage message, View convertView, int position) {
        try {
            LinearLayout userView = (LinearLayout) convertView.findViewById(R.id.user_view);
            LinearLayout astrologerView = (LinearLayout) convertView.findViewById(R.id.astrologer_view);
            LinearLayout llMessageLayout = (LinearLayout) convertView.findViewById(R.id.llMessageLayout);
            TextView tvStatusMessage = convertView.findViewById(R.id.tvStatusMessage);
            tvStatusMessage.setVisibility(GONE);
            String author = message.getAuthor();
            Log.d("KundliAIChatTesting", "messageToShow author: " + author);
            llMessageLayout.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_chat_selected_removed));
            tvStatusMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TestLink", "onClick="+tvStatusMessage.getSelectionStart());
                    if (tvStatusMessage.getSelectionStart() == -1 && tvStatusMessage.getSelectionEnd() == -1) {
                        //do nothing
                    } else {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.UPDATE_PLAN_FROM_STATUS_MESSAGE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                        ((MiniChatWindow)mActivity).isPlanPurchaseScreenOpen = true;
                        com.ojassoft.astrosage.varta.utils.CUtils.openPurchaseDhruvPlanActivity(mActivity,true, true,((MiniChatWindow)mActivity).source,false,true);
                    }
                }
            });

            if (author.equalsIgnoreCase("SAMPLE_REPORT")) {
                userView.setVisibility(GONE);
                astrologerView.setVisibility(GONE);
            } else if (author.equalsIgnoreCase("USER")) {
                userView.setVisibility(VISIBLE);
                astrologerView.setVisibility(GONE);

                TextView usertextViewMessage = convertView.findViewById(R.id.usertextViewMessage);
                TextView usertextViewDate = convertView.findViewById(R.id.usertextViewDate);
                FontUtils.changeFont(mActivity,usertextViewMessage , com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_POPPINS_LIGHT);
                FontUtils.changeFont(mActivity,usertextViewDate , com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_POPPINS_LIGHT);
                Log.e("KundliAIChatTesting", "messageToShow: " + message.getMessageBody(mActivity));
                usertextViewMessage.setText(message.getMessageBody(mActivity));

                usertextViewDate.setText(com.ojassoft.astrosage.varta.utils.CUtils.getConvertDateFormatedForMessage(message.getDateCreated()));

            } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_WARNING) ||
                    author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_WELCOME) ||
                    author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_LOW_BALANCE) ||
                    author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_CHAT_AGAIN) ||
                    author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_END_DUE_TO_LOW_BALANCE)) {
                userView.setVisibility(GONE);
                astrologerView.setVisibility(GONE);
                tvStatusMessage.setVisibility(VISIBLE);
                if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_WARNING)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvStatusMessage.setText(Html.fromHtml(message.getMessageBody(mActivity), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tvStatusMessage.setText(Html.fromHtml(message.getMessageBody(mActivity)));
                    }
                    tvStatusMessage.setMovementMethod(LinkMovementMethod.getInstance());
                } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_WELCOME)) {
                    tvStatusMessage.setText(mActivity.getString(R.string.ai_welcome_message));
                } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_LOW_BALANCE)) {
                    tvStatusMessage.setText(mActivity.getString(R.string.low_balance_message));
                } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_CHAT_AGAIN)) {
                    tvStatusMessage.setText(mActivity.getString(R.string.chat_restarted));
                } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_END_DUE_TO_LOW_BALANCE)) {
                    tvStatusMessage.setText(mActivity.getString(R.string.chat_ended_due_to_low_balance));
                }else if(author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_LOAD_MORE_CHAT)){

                }
            } else {
                ImageView ivLikeMessage, ivUnlikeMessage, ivShareMsg,ivCopyChat, ivSpeakMessage;
                RatingBar ratingBar;
                userView.setVisibility(GONE);
                astrologerView.setVisibility(VISIBLE);

                TypeWritterKundali textViewMessage = convertView.findViewById(R.id.textViewKundaliMsg);
                TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
                TextView tvAnswerIdMissing = convertView.findViewById(R.id.tvAnswerIdMissing);
                FontUtils.changeFont(mActivity,textViewDate , com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_POPPINS_LIGHT);
                LinearLayout llChatButtons = convertView.findViewById(R.id.llChatButtons);
                llChatButtons.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                View view = (View) convertView.findViewById(R.id.tv_view);
                ivLikeMessage = convertView.findViewById(R.id.ivLikeMessage);
                ivUnlikeMessage = convertView.findViewById(R.id.ivUnlikeMessage);

                ivShareMsg = convertView.findViewById(R.id.ivShareMessage);
                ivCopyChat = convertView.findViewById(R.id.iv_copy_chat);

                if (messages.get(position).chatId() == -1){
                    ivShareMsg.setVisibility(View.GONE);
                }
                ivSpeakMessage = convertView.findViewById(R.id.ivSpeakMessage);
                if(((MiniChatWindow) mActivity).speakingPosition != position){
                    message.setIsSpeaking(false);
                    ivSpeakMessage.setBackgroundResource(0);
                    int tintColor = ContextCompat.getColor(mActivity, R.color.black);
                    ivSpeakMessage.setColorFilter(tintColor);
                }

                if (message.isAnswerIdMissing()) {
                    tvAnswerIdMissing.setVisibility(VISIBLE);
                } else {
                    tvAnswerIdMissing.setVisibility(View.GONE);
                }

                message.setIsSpeaking(false);
                Log.d("LikeDislike", "LikeUnlike chatID = " + message.chatId() + " isliked = " + message.getLike());
                if (message.getLike() == 1) {
                    ivLikeMessage.setImageResource(R.drawable.ic_like);
                    ivUnlikeMessage.setImageResource(R.drawable.ic_like_outline);
                } else if (message.getUnlike() == 1) {
                    ivLikeMessage.setImageResource(R.drawable.ic_like_outline);
                    ivUnlikeMessage.setImageResource(R.drawable.ic_like);
                }
                ivCopyChat.setOnClickListener(v -> {
                    CUtils.copyTextToClipBoard(removeHtmlTags(message.getMessageBody(mActivity)),mActivity);
                });
                String textMessage = messages.get(position).getMessageBody(mActivity);
                ivShareMsg.setOnClickListener(view1 -> {
                    try {
                        String question = "";
                        ChatMessage msg = messages.get(position - 1);
                        if (msg.getAuthor().equals("USER")) {
                            question = msg.getMessageBody(mActivity);
                        } else {
                            question = messages.get(position - 1).getMessageBody(mActivity);
                        }
                        // String text = removeHtmlTags(textMessage);
                        ((MiniChatWindow) mActivity).shareMessage(question, textMessage);
                    } catch (Exception e) {
                        //
                    }
                });
                ivLikeMessage.setOnClickListener(v -> {
                    int likeValue;
                    if(message.getLike() == 1){
                        ivLikeMessage.setImageResource(R.drawable.ic_like_outline);
                        likeValue = 0;
                    }else {
                        ivLikeMessage.setImageResource(R.drawable.ic_like);
                        likeValue = 1;
                    }
                    ivUnlikeMessage.setImageResource(R.drawable.ic_like_outline);
                    message.setLike(likeValue);
                    message.setUnlike(0);
                    ((MiniChatWindow)mActivity).setLikeUnlike(String.valueOf(message.chatId()), likeValue,0);
                });
                ivUnlikeMessage.setOnClickListener(v -> {
                    int unLikeValue;
                    if(message.getUnlike()==1){
                        unLikeValue = 0;
                        ivUnlikeMessage.setImageResource(R.drawable.ic_like_outline);
                    }else {
                        unLikeValue = 1;
                        ivUnlikeMessage.setImageResource(R.drawable.ic_like);
                    }
                    message.setUnlike(unLikeValue);
                    message.setLike(0);
                    ivLikeMessage.setImageResource(R.drawable.ic_like_outline);

                    ((MiniChatWindow)mActivity).setLikeUnlike(String.valueOf(message.chatId()), 0,unLikeValue);
                });

                message.setIsSpeaking(false);

                ivSpeakMessage.setOnClickListener(v -> {
                    int speakPos = ((MiniChatWindow) mActivity).speakingPosition;
                    if (speakPos == position) {
                        ((MiniChatWindow) mActivity).stopSpeaking();
                        message.setIsSpeaking(false);
                    } else {
                        if ( speakPos> -1 && speakPos < messages.size() ) {//it means other message is speaking currently
                            messages.get(speakPos).setIsSpeaking(false);
                            ((MiniChatWindow) mActivity).stopSpeaking();
                        }
                        String text = removeHtmlTags(textMessage);

                        // Regular expression to remove emojis
                        text = text.replaceAll("[\\p{So}\\p{Cn}]", "");

                       // ((MiniChatWindow) mActivity).selectVoices("male");


                        ((MiniChatWindow) mActivity).speakOutMsg(text.replace("*", ""), position);
                        message.setIsSpeaking(true);
                        ivSpeakMessage.setBackgroundResource(R.drawable.circle_yellow);
                        int tintColor = ContextCompat.getColor(mActivity, R.color.white);
                        ivSpeakMessage.setColorFilter(tintColor);
                    }
                });
                ratingBar = convertView.findViewById(R.id.ratingBar);
                ratingBar.setVisibility(GONE);
                TextView tvTellUsMore = convertView.findViewById(R.id.tv_tell_us_more);
                tvTellUsMore.setVisibility(GONE);
                ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
                    ((MiniChatWindow) mActivity).callRatingApi(rating, message.getAnswerId());
//                ratingBar1.setIsIndicator(true);
                    tvTellUsMore.setVisibility(VISIBLE);
                });
                tvTellUsMore.setOnClickListener(v -> {
                    ((MiniChatWindow) mActivity).showTellUsMoreDialog(ratingBar.getRating(), message.getAnswerId());
                });

                String url = message.getMessageBody(mActivity).replace("youtube", "");
                String videoId = CUtils.extractYTId(url);
                if (message.getMessageBody(mActivity).contains("youtube")) {
                    textViewMessage.setVisibility(GONE);
                    view.setVisibility(GONE);
                } else {
                    String text = "";
                    if (textMessage.contains("~~")) {
                        view.setVisibility(GONE);
                        text = textMessage.substring(0, textMessage.indexOf("~~"));
                        /*LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        String[] data = messages.get(position).getMessageBody(mActivity).split("~~");

                        llChatButtons.removeAllViews();
                        for (int i = 1; i < data.length; i = i + 2) {
                            TextView tv = new TextView(mActivity);

                            lparams.setMargins(50, 25, 50, 15);
                            lparams.gravity = Gravity.CENTER;
                            tv.setLayoutParams(lparams);
                            tv.setText(data[i]);
                            tv.setTextSize(16);
                            tv.setGravity(Gravity.CENTER);
                            if (data.length > i + 1)
                                tv.setTag(data[i + 1]);
                            tv.setTextColor(mActivity.getResources().getColor(R.color.primary_orange));
                            tv.setOnClickListener(this);


                            LinearLayout.LayoutParams lparamsView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                            View line = new View(mActivity);
                            View line2 = new View(mActivity);
                            line2.setLayoutParams(lparamsView);
                            line.setLayoutParams(lparamsView);
                            line.setBackgroundColor(mActivity.getResources().getColor(R.color.light_border));


                            llChatButtons.addView(tv);
                            llChatButtons.addView(line2);

                            if (data.length > i + 2) {
                                line.setVisibility(View.VISIBLE);
                                llChatButtons.addView(line);
                            }
                        }*/

                    } else {
                        view.setVisibility(GONE);
                        text = textMessage;
                    }
                    setMessageText(text, messages.get(position).isSeen(), convertView, position);
                }

                textViewDate.setText(com.ojassoft.astrosage.varta.utils.CUtils.getConvertDateFormatedForMessage(message.getDateCreated()));
            }


            final float[] startX = {0};
            final float[] startY = {0};
            final float[] endX = {0};
            final float[] endY = {0};
            final float[] diffX = {0};
            final float[] diffY = {0};
            final boolean[] bClick = {false};
            final Timer[] tmrClick = {null};

            astrologerView.setOnTouchListener((v, event) -> {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX[0] = event.getX();
                        startY[0] = event.getY();

                        bClick[0] = true;
                        tmrClick[0] = new Timer();
                        tmrClick[0].schedule(new TimerTask() {
                            public void run() {
                                if (bClick[0]) {
                                    bClick[0] = false;
                                    Log.d("KundliAIChatTesting", "Hey, a long press event!");
                                    //Handle the longpress event.
                                }
                            }
                        }, 500); //500ms is the standard longpress response time. Adjust as you see fit.

                        return false;
                    case MotionEvent.ACTION_UP:
                        endX[0] = event.getX();
                        endY[0] = event.getY();

                        diffX[0] = Math.abs(startX[0] - endX[0]);
                        diffY[0] = Math.abs(startY[0] - endY[0]);

                        if (diffX[0] <= 5 && diffY[0] <= 5 && bClick[0]) {
                            if (isLongPressClicked) {
                                updateCopyList(position, llMessageLayout);
                            }
                            Log.d("KundliAIChatTesting", "single clicked!");
                            bClick[0] = false;
                        }
                        return false;
                    default:
                        return false;
                }
            });

            astrologerView.setOnLongClickListener(v -> {
                Log.d("KundliAIChatTesting ", " astrologerView long press => " + position);
                if (isLongPressClicked) {
                    // nothing to do
                } else {
                    isLongPressClicked = true;
                    updateCopyList(position, llMessageLayout);
                }

                return true;
            });

            userView.setOnTouchListener((v, event) -> {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX[0] = event.getX();
                        startY[0] = event.getY();

                        bClick[0] = true;
                        tmrClick[0] = new Timer();
                        tmrClick[0].schedule(new TimerTask() {
                            public void run() {
                                if (bClick[0]) {
                                    bClick[0] = false;
                                    Log.d("KundliAIChatTesting", "Hey, a long press event!");
                                    //Handle the longpress event.
                                }
                            }
                        }, 500); //500ms is the standard longpress response time. Adjust as you see fit.

                        return false;
                    case MotionEvent.ACTION_UP:
                        endX[0] = event.getX();
                        endY[0] = event.getY();

                        diffX[0] = Math.abs(startX[0] - endX[0]);
                        diffY[0] = Math.abs(startY[0] - endY[0]);

                        if (diffX[0] <= 5 && diffY[0] <= 5 && bClick[0]) {
                            if (isLongPressClicked)
                                updateCopyList(position, llMessageLayout);
                            Log.d("KundliAIChatTesting", "single clicked!");
                            bClick[0] = false;
                        }
                        return false;
                    default:
                        return false;
                }
            });
            userView.setOnLongClickListener(v -> {
                Log.d("MyTag ", " astrologerView long press => " + position);

                if (!isLongPressClicked) {
                    isLongPressClicked = true;
                    updateCopyList(position, llMessageLayout);
                }

                return true;
            });

            astrologerView.setOnLongClickListener(v -> {
                Log.d("MyTag ", " astrologerView long press => " + position);

                if (!isLongPressClicked) {
                    isLongPressClicked = true;
                    updateCopyList(position, llMessageLayout);
                }

                return false;
            });
        } catch (Exception e) {
         //   Log.e("myError", "messageToShow ,error :" + e);
        }
    }

    private static String removeHtmlTags(String textMessage) {
        String text = textMessage.replace("<br><br>", "\n").replace("<br>", "\n")
                .replace("<strong>", "")
                .replace("<em>", "")
                .replace("</em>", "")
                .replace("</strong>", "");
        if (text.contains("^^")) {
            int index = text.indexOf("^^");
            if (index == -1) {
                index = text.length();
            }
            text = text.substring(0, index - 1);
        }
        return text;
    }

    private void setMessageText(String text, boolean isSeen, View view, int position) {
        ArrayList<String> textMessage = split(text);
        ((MiniChatWindow) mActivity).currentView = view;
        LinearLayout llLikeUnlike = view.findViewById(R.id.llLikeUnlike);
        /*Log.e("KundliAIChatTesting", "setMessageText: messge list...." + textMessage);
        Log.e("KundliAIChatTesting", "setMessageText: isSeen...." + isSeen);*/
        if (textMessage.isEmpty()) return;
        LinearLayout llTypeWriter = view.findViewById(R.id.type_writer_layout);

        ((MiniChatWindow) mActivity).llTypeWriterParent = llTypeWriter;
        llTypeWriter.removeAllViews();
        TypeWritterKundali typeWriter = typeWriterInstance(0);

        if (!flag && isSeen) {
            flag = true;
            llTypeWriter.addView(typeWriter);
            ((MiniChatWindow) mActivity).scrollWhileTypeWriter = true;
            ((MiniChatWindow) mActivity).stopTypeWriter = false;
            ((MiniChatWindow) mActivity).llChatButton = view;
            ((MiniChatWindow) mActivity).currentTypeWriterPosition = position;
            ((MiniChatWindow) mActivity).currentTypeWriter = typeWriter;
            llLikeUnlike.setVisibility(GONE);
            if (!textMessage.isEmpty()) {
                typeWriter.animateText(textMessage.get(0), (MiniChatWindow) mActivity, ((MiniChatWindow) mActivity));

            }
        } else {
            AstrosageKundliApplication.brokenMessageLogs += "\n"+"adapter setMessageText text = \n"+text;
            int count = 0;
            llLikeUnlike.setVisibility(VISIBLE);
            do {
                TypeWritterKundali instance = typeWriterInstance(0);
                llTypeWriter.addView(instance);
                instance.setText(Html.fromHtml(convertBulletPoint(textMessage.get(count++))));
            } while (textMessage.size() > count);
        }
    }

    public String convertBulletPoint(String input) {

        if (input.trim().startsWith("-")) {
            input = input.replace("-", "");
            input = "&#x2022;" + input;
        }

        return input; // Return the converted HTML-compatible string
    }

    private void updateCopyList(int position, View view) {

        Log.e("MyTag ", " updateCopyList position => " + position);
        Log.e("MyTag ", " updateCopyList brfore copyMessage array => " + copyMessage.toString());

        if (copyMessage.contains(position)) {
            copyMessage.remove((Integer) position);
            messagesToDeleteList.remove(messages.get(position));
            view.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_chat_selected_removed));
        } else {
            copyMessage.add(position);
            messagesToDeleteList.add(messages.get(position));
            view.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_chat_selected_grey));
        }
        Log.e("MyTag ", " updateCopyList after copyMessage array => " + copyMessage);
        if (!copyMessage.isEmpty()) {
            if (listener != null) {
                listener.onLongClickEnabled(true);
            }
            ((MiniChatWindow) mActivity).updateCopyView(true);
        } else {
            if (listener != null) {
                listener.onLongClickEnabled(false);
            }
            ((MiniChatWindow) mActivity).updateCopyView(false);
            isLongPressClicked = false;
        }
    }


    public void clearMessageList() {
        messages.clear();
    }

    @Override
    public void onClick(View v) {
        String clickData = (String) v.getTag();
        if (clickData.contains("http://") || clickData.contains("https://")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(clickData));
            mActivity.startActivity(intent);
        }
    }

    private TypeWritterKundali typeWriterInstance(int index) {
        TypeWritterKundali newTypeWriter = new TypeWritterKundali(mActivity, index);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, toPixel(10), 0, 0);
        newTypeWriter.setLayoutParams(params);
        newTypeWriter.setPadding(toPixel(10), toPixel(4), toPixel(10), toPixel(4));
        newTypeWriter.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
        newTypeWriter.setTextSize(17);
        FontUtils.changeFont(mActivity,newTypeWriter , com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_POPPINS_LIGHT);
        newTypeWriter.setTextColor(ContextCompat.getColor(mActivity,R.color.black));
        newTypeWriter.setBackgroundResource(R.drawable.bg_ai_astro_mgs);

        return newTypeWriter;
    }


    public void markLastQuestionError() {
        try {
            ChatMessage message = messages.get(getItemCount() - 1);
            if (message.getAuthor().equalsIgnoreCase("USER")) {
                messages.get(getItemCount() - 1).setIsError(true);
                notifyItemChanged(getItemCount() - 1);
//                Log.e("greetingCheck", "markLastQuestionError: " +message.getMessageBody(mActivity));
            }
//            Log.e("Question",message.getAuthor());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private int toPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mActivity.getResources().getDisplayMetrics());
    }

    private ArrayList<String> split(String text) {
        String[] split;
        ArrayList<String> list = new ArrayList<>();

        split = text.split("<br><br>");

        for (String s : split) {
            if (!s.trim().isEmpty()) {
                list.add(s);

            }
        }
        return list;
    }

    public void stopTyping() {
        isStopTyping = true;
        int lastItem = getItemCount() - 1;
        ChatMessage chatMessage = messages.get(lastItem);
        chatMessage.setDelayed(false);
    }


    public ArrayList<ChatMessage> getMessagesToDeleteList() {
        return messagesToDeleteList;
    }
    public void removeMessageFromList(ChatMessage chatMessage) {
        messages.remove(chatMessage);
    }

    /**
     * Adds a list of older messages to the beginning of the adapter's list
     * and notifies the RecyclerView to render them.
     *
     * @param messagesToPrepend The list of messages to add to the top.
     */
    public void prependMessages(ArrayList<ChatMessage> messagesToPrepend) {
        // Add the new list at index 0 (the beginning)
        this.messages.addAll(0, messagesToPrepend);

        // Notify the adapter that a range of items was inserted at the beginning
        notifyItemRangeInserted(0, messagesToPrepend.size());
    }
}

