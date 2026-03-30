package com.ojassoft.astrosage.varta.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.ui.activity.AINotificationChatActivity;
import com.ojassoft.astrosage.varta.utils.MessageDiffCallback;
import com.ojassoft.astrosage.varta.utils.TypeWriter;
import com.ojassoft.astrosage.varta.utils.TypeWriterNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class AINotificationChatMessageAdapter extends RecyclerView.Adapter<AINotificationChatMessageAdapter.MyHolder> implements View.OnClickListener {
    private final int TYPE_MESSAGE = 0;
    private final int TYPE_STATUS = 1;

    private final int TYPE_USER = 0;
    private final int TYPE_ASTROLOGER = 1;
    private final Activity mActivity;
    private List<ChatMessage> messages;
    private final ArrayList<Integer> copyMessage;
    private final LayoutInflater layoutInflater;
    private final TreeSet<Integer> statusMessageSet = new TreeSet<>();

    boolean flag = false;
    public Boolean isLongPressClicked = false;
    private final int lang;

    public AINotificationChatMessageAdapter(Activity activity) {
        layoutInflater = activity.getLayoutInflater();
        messages = new ArrayList<>();
        copyMessage = new ArrayList<>();
        mActivity = activity;
        lang = CUtils.getIntData(mActivity, CGlobalVariables.app_language_key, CGlobalVariables.ENGLISH);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.ai_notification_message_item, parent, false);
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

        for (ChatMessage item: messages) {
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

    public void addAllMessages(List<ChatMessage> messagesList) {
        messages.addAll(messagesList);
        refreshList();
    }

    public void addMessage(Message message) {
        flag = false;
        updateMessageList(message);
    }


    public void updateMessageList(Message message) {
        List<ChatMessage> newList = new ArrayList<>(messages);
        newList.add(new UserMessage(message));

        MessageDiffCallback diffCallback = new MessageDiffCallback(messages, newList, mActivity);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        messages.add(new UserMessage(message));
        diffResult.dispatchUpdatesTo(this);
        //notifyItemInserted(getItemCount()-1);
    }

    public boolean isStopTyping = false;

    public void refreshList() {
        notifyDataSetChanged();
    }

    public void refreshSingleItem(int position) {
        notifyItemChanged(position);
    }

    public void removeMessage(Message message) {
        messages.remove(messages.indexOf(message));
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
        LinearLayout userView = (LinearLayout) convertView.findViewById(R.id.user_view);
        LinearLayout astrologerView = (LinearLayout) convertView.findViewById(R.id.astrologer_view);
        LinearLayout llMessageLayout = (LinearLayout) convertView.findViewById(R.id.llMessageLayout);
        TextView tvStatusMessage = convertView.findViewById(R.id.tvStatusMessage);
        ImageView astroProfileImage = convertView.findViewById(R.id.ivFRPAFC);
        LinearLayout errorLayout = convertView.findViewById(R.id.error_layout);
        ImageView ivWaitMsg = convertView.findViewById(R.id.ivWaitMsg);
        tvStatusMessage.setVisibility(View.GONE);
        ivWaitMsg.setVisibility(View.GONE);
        String author = message.getAuthor();
        Log.d("AIChatTesting", "messageToShow author: "+author);
        llMessageLayout.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_chat_selected_removed));
        if (((AINotificationChatActivity) mActivity).astrologerProfileUrl != null && ((AINotificationChatActivity) mActivity).astrologerProfileUrl.length() > 0) {
            String astroImage = com.ojassoft.astrosage.varta.utils.CGlobalVariables.IMAGE_DOMAIN + ((AINotificationChatActivity) mActivity).astrologerProfileUrl;
            Glide.with(astroProfileImage).load(astroImage).circleCrop().into(astroProfileImage);
        }
        astroProfileImage.setOnClickListener(view -> {
            ((AINotificationChatActivity) mActivity).showAstrologorFullProfile();
        });

        if (author.equalsIgnoreCase("SAMPLE_REPORT")) {
            userView.setVisibility(View.GONE);
            astrologerView.setVisibility(View.GONE);
        } else if (author.equalsIgnoreCase("USER")) {
            userView.setVisibility(View.VISIBLE);
            astrologerView.setVisibility(View.GONE);

            TextView usertextViewMessage = convertView.findViewById(R.id.usertextViewMessage);
            TextView usertextViewDate = convertView.findViewById(R.id.usertextViewDate);

            usertextViewMessage.setText(message.getMessageBody(mActivity));

            usertextViewDate.setText(message.getDateCreated());

            if (message.getIsError()) {
                errorLayout.setVisibility(View.VISIBLE);
                errorLayout.setOnClickListener(view ->  ((AINotificationChatActivity) mActivity).showSendAgainAlert(message.getMessageBody(mActivity)));
                userView.setOnClickListener(view -> {
                    ((AINotificationChatActivity) mActivity).showSendAgainAlert(message.getMessageBody(mActivity));
                });
            } else {
                errorLayout.setVisibility(View.GONE);
                convertView.setOnClickListener(view -> {
                });
            }
        } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_WARNING) ||
                author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_WELCOME) ||
                author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_SUGGESTION) ||
                author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_LOW_BALANCE) ||
                author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_CHAT_AGAIN) ||
                author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_END_DUE_TO_LOW_BALANCE) ||
                author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_TYPING)) {
            userView.setVisibility(View.GONE);
            astrologerView.setVisibility(View.GONE);
            tvStatusMessage.setVisibility(View.VISIBLE);
            if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_WARNING)) {
                tvStatusMessage.setText(mActivity.getString(R.string.warning_message));
            } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_WELCOME)) {
                tvStatusMessage.setText(mActivity.getString(R.string.ai_welcome_message));
            } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_LOW_BALANCE)) {
                tvStatusMessage.setText(mActivity.getString(R.string.low_balance_message));
            } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_CHAT_AGAIN)) {
                tvStatusMessage.setText(mActivity.getString(R.string.chat_restarted));
            } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_END_DUE_TO_LOW_BALANCE)) {
                tvStatusMessage.setText(mActivity.getString(R.string.chat_ended_due_to_low_balance));
            } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_SUGGESTION)) {
                tvStatusMessage.setText(mActivity.getString(R.string.suggestion_message));
            }else if(author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_TYPING)){
                tvStatusMessage.setVisibility(View.GONE);
                ivWaitMsg.setVisibility(View.VISIBLE);
                Glide.with(ivWaitMsg).load(R.drawable.typing).into(ivWaitMsg);
            }
        } else {
            userView.setVisibility(View.GONE);
            astrologerView.setVisibility(View.VISIBLE);

            TypeWriterNotification textViewMessage = (TypeWriterNotification) convertView.findViewById(R.id.textViewMessage);
            // textViewMessage.setTypeface(openSensSemiBold);
            TextView textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
            //textViewDate.setTypeface(openSensSemiBold);
            LinearLayout llAstroChat = (LinearLayout) convertView.findViewById(R.id.ll_astro_chat);
            LinearLayout llChatButtons = convertView.findViewById(R.id.llChatButtons);
            LinearLayout llButtonsParent = convertView.findViewById(R.id.llButtonsParent);
            llChatButtons.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
            View view = (View) convertView.findViewById(R.id.tv_view);
            ImageView ivLikeMessage = convertView.findViewById(R.id.ivLikeMessage);
            ImageView ivUnlikeMessage = convertView.findViewById(R.id.ivUnlikeMessage);
            LinearLayout llLikeUnlike = convertView.findViewById(R.id.llLikeUnlike);
            ImageView ivShareMsg = convertView.findViewById(R.id.ivShareMessage);
            //TextView tvDefaultChatButton = convertView.findViewById(R.id.tvDefaultChatButton);
            if(!((AINotificationChatActivity)mActivity).isTypingGreetingMessage)
                llLikeUnlike.setVisibility(View.VISIBLE);
            Log.d("LikeDislike","LikeUnlike chatID = " +message.chatId() + "isliked = " + message.getLike());
            if(message.getLike() == 1){
                ivLikeMessage.setImageResource(R.drawable.ic_like);
                ivUnlikeMessage.setImageResource(R.drawable.ic_like_outline);
            }else if(message.getUnlike() ==1){
                ivLikeMessage.setImageResource(R.drawable.ic_like_outline);
                ivUnlikeMessage.setImageResource(R.drawable.ic_like);
            }
            String textMessage = messages.get(position).getMessageBody(mActivity);
            ivShareMsg.setOnClickListener(view1 -> {
                try {
                    String question = "";
                    ChatMessage msg = messages.get(position);
                    if (msg.getAuthor().equals("USER")) {
                        question = msg.getMessageBody(mActivity);
                    } else {
                        question = messages.get(position).getMessageBody(mActivity);
                    }
                    String text = textMessage.replace("<br><br>", "\n").replace("<br>", "\n");
                    ((AINotificationChatActivity) mActivity).shareMessage(question, text);
                } catch (Exception e) {
                    //
                }
            });

            ivLikeMessage.setOnClickListener(v -> {
                message.setLike(1);
                message.setUnlike(0);
                ivLikeMessage.setImageResource(R.drawable.ic_like);
                ivUnlikeMessage.setImageResource(R.drawable.ic_like_outline);
                AINotificationChatActivity.setLikeUnlike(mActivity,String.valueOf(message.chatId()),1);
            });

            ivUnlikeMessage.setOnClickListener(v -> {
                message.setLike(0);
                message.setUnlike(1);
                ivLikeMessage.setImageResource(R.drawable.ic_like_outline);
                ivUnlikeMessage.setImageResource(R.drawable.ic_like);
                AINotificationChatActivity.setLikeUnlike(mActivity,String.valueOf(message.chatId()),0);
            });

            /*if (lang == 1)
                textViewMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, hindiFntSize);*/

            FrameLayout youtubeFL = (FrameLayout) convertView.findViewById(R.id.youtubeLayout);
            NetworkImageView youtubeThumbnailIV = convertView.findViewById(R.id.youtubeThumbnailIV);

            String url = message.getMessageBody(mActivity).replace("youtube", "");
            String videoId = CUtils.extractYTId(url);
            youtubeThumbnailIV.setImageUrl(com.ojassoft.astrosage.varta.utils.CUtils.getYoutubeUrl(videoId), VolleySingleton.getInstance(mActivity).getImageLoader());

            //  Typeface robotRegularTypeface = CUtils.getRobotoRegular(mActivity);
            // Typeface robotMediumTypeface = CUtils.getRobotoMedium(mActivity);
            //  textViewMessage.setTypeface(openSensSemiBold);

            if (message.getMessageBody(mActivity).contains("youtube")) {
                youtubeFL.setVisibility(View.VISIBLE);
                textViewMessage.setVisibility(View.GONE);
                view.setVisibility(View.GONE);

                youtubeFL.setOnClickListener(v -> {
                    /*YoutubeVideoPlayerFragment youtubeVideoPlayerFragment = new YoutubeVideoPlayerFragment(videoId);
                    youtubeVideoPlayerFragment.show(((AINotificationChatActivity) mActivity).getSupportFragmentManager(), "youtubeVideoPlayerFragment");*/
                });

                ((AINotificationChatActivity) mActivity).scrollMyListViewToBottom();

            } else {
                youtubeFL.setVisibility(View.GONE);

                String text = "";
                if (textMessage.contains("~~")) {
                    view.setVisibility(View.VISIBLE);
                    text = textMessage.substring(0, textMessage.indexOf("~~"));
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
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
                    }

                } else {
                    view.setVisibility(View.GONE);
                    text = textMessage;
                }

                setMessageText(text, messages.get(position).isSeen(), convertView, position);
            }

            textViewDate.setText(message.getDateCreated());
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
                                Log.d("MyTag", "Hey, a long press event!");
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
                        Log.d("MyTag", "single clicked!");
                        bClick[0] = false;
                    }
                    return false;
                default:
                    return false;
            }
        });

        astrologerView.setOnLongClickListener(v -> {
            Log.d("MyTag ", " astrologerView long press => " + position);
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
                                Log.d("MyTag", "Hey, a long press event!");
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
                        Log.d("MyTag", "single clicked!");
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

    }

    private void setMessageText(String text, boolean isSeen, View view, int position) {
        //Log.e("TypeWriter","All text = "+text);
        ArrayList<String> textMessage = split(text);
        ((AINotificationChatActivity) mActivity).currentView = view;
        if (textMessage.isEmpty()) return;
        LinearLayout llTypeWriter = view.findViewById(R.id.type_writer_layout);
        ((AINotificationChatActivity) mActivity).llTypeWriterParent = llTypeWriter;
        llTypeWriter.removeAllViews();
        //LinearLayout chatButtonLayout = view.findViewById(R.id.llChatButtons);
        TypeWriterNotification typeWriter = typeWriterInstance(0);

       /* TypeWriterNotification.OnTypingComplete typingComplete = new TypeWriterNotification.OnTypingComplete() {
            @Override
            public void onFinishTyping(int index) {
                if (index + 1 >= textMessage.size() || isStopTyping) {
                    ((AINotificationChatActivity) mActivity).toggleSendStopButtonVisibility(true);
                    return;
                }
                TypeWriterNotification instance = typeWriterInstance((index + 1));
                llTypeWriter.addView(instance);
              //  ((AINotificationChatActivity) mActivity).scrollWhileTypeWriter = true;
                ((AINotificationChatActivity) mActivity).stopTypeWriter = false;
                ((AINotificationChatActivity) mActivity).currentTypeWriterPosition = position;
                ((AINotificationChatActivity) mActivity).currentTypeWriter = instance;
                //Log.e("TypeWriterNotification","All text = "+textMessage[index + 1]);
                instance.animateText(Html.fromHtml(convertBulletPoint(textMessage.get(index + 1))), (AINotificationChatActivity) mActivity, this);
            }
        };*/
        if (!flag && isSeen) {
            flag = true;
            llTypeWriter.addView(typeWriter);
            ((AINotificationChatActivity) mActivity).scrollWhileTypeWriter = true;
            ((AINotificationChatActivity) mActivity).stopTypeWriter = false;
            ((AINotificationChatActivity) mActivity).llChatButton = view;
            ((AINotificationChatActivity) mActivity).currentTypeWriterPosition = position;
            ((AINotificationChatActivity) mActivity).currentTypeWriter = typeWriter;
            //Log.e("TypeWriterNotification","All text = "+textMessage[0]);
            if (!textMessage.isEmpty()) {
                typeWriter.animateText(textMessage.get(0), (AINotificationChatActivity) mActivity, ((AINotificationChatActivity) mActivity));
            }
        } else {
            int count = 0;
            do {
                TypeWriterNotification newTypeWriter = typeWriterInstance(0);
                llTypeWriter.addView(newTypeWriter);
                newTypeWriter.setText(Html.fromHtml(convertBulletPoint(textMessage.get(count))));
                count++;
            } while (count < textMessage.size());
            LinearLayout layout = view.findViewById(R.id.llButtonsParent);
            layout.setVisibility(View.VISIBLE);

        }
    }

    public String convertBulletPoint(String input) {

        if(input.trim().startsWith("-")){
            input = input.replace("-","");
           input = "&#x2022;"+ input;
        }

        return input; // Return the converted HTML-compatible string
    }

    private void updateCopyList(int position, View view) {

        Log.e("MyTag ", " updateCopyList position => " + position);
        Log.e("MyTag ", " updateCopyList brfore copyMessage array => " + copyMessage.toString());

        if (copyMessage.contains(position)) {
            copyMessage.remove((Integer) position);
            view.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_chat_selected_removed));
        } else {
            copyMessage.add(position);
            view.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_chat_selected_grey));
        }

        if (!copyMessage.isEmpty())
            ((AINotificationChatActivity) mActivity).updateCopyView(true);
        else {
            ((AINotificationChatActivity) mActivity).updateCopyView(false);
            isLongPressClicked = false;
        }


        Log.e("MyTag ", " updateCopyList after copyMessage array => " + copyMessage);

    }

    /*public void callKMService(String ques) {
        Log.d("MyTag ", " callKMService ");
        Log.d("MyTag", "getMessageList ().size () method calling " + getMessageList().size());

        try {
            BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils.getCustomObject(mActivity);
            String[] name = beanHoroPersonalInfo.getName().split(" ");

            Log.d("MyTag ", " callKMService 2");
            Log.d("MyTag", "getMessageList ().size () before API Call" + getMessageList().size());


            Call<YogaResponse> call = ApiClient.getClient().create(ApiInterface.class).knowMore(name[0], beanHoroPersonalInfo.getGender(),
                    String.valueOf(beanHoroPersonalInfo.getDateTime().getDay()), String.valueOf((beanHoroPersonalInfo.getDateTime().getMonth() + 1)),
                    String.valueOf(beanHoroPersonalInfo.getDateTime().getYear()), String.valueOf(beanHoroPersonalInfo.getDateTime().getHour()),
                    String.valueOf(beanHoroPersonalInfo.getDateTime().getMin()), String.valueOf(beanHoroPersonalInfo.getDateTime().getSecond()),
                    String.valueOf(beanHoroPersonalInfo.getDST()), beanHoroPersonalInfo.getPlace().getCityName().replace(" ", "%20"),
                    beanHoroPersonalInfo.getPlace().getLongDeg(), beanHoroPersonalInfo.getPlace().getLongMin(),
                    beanHoroPersonalInfo.getPlace().getLongDir(), beanHoroPersonalInfo.getPlace().getLatDeg(),
                    beanHoroPersonalInfo.getPlace().getLatMin(), beanHoroPersonalInfo.getPlace().getLatDir(), String.valueOf(beanHoroPersonalInfo.getPlace().getTimeZoneValue()),
                    String.valueOf(beanHoroPersonalInfo.getAyanIndex()), "0", String.valueOf(beanHoroPersonalInfo.getHoraryNumber()),
                    String.valueOf(lang), CUtils.getMyAndroidId(mActivity), "getanswer", ques, CUtils.getApplicationSignatureHashCode(mActivity));

            call.enqueue(new Callback<YogaResponse>() {
                @Override
                public void onResponse(Call<YogaResponse> call, Response<YogaResponse> response) {
                    ((AINotificationChatActivity) mActivity).hide3Dots();
                    try {
                        YogaResponse apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getStatus() != null && apiResponse.getStatus().equals("1")) {
                            String message = apiResponse.getMessage();
                            Log.d("MyTag ", " callKMService 3");
                            try {
                                Log.d("MyTag ", " callKMService 4");
                                Log.d("MyTag", "getMessageList ().size () before " + getMessageList().size());

                                ((AINotificationChatActivity) mActivity).setResponse(message, true);

                                Log.d("MyTag", "getMessageList ().size () after " + getMessageList().size());
                                Log.d("MyTag", "db " + message);

                            } catch (Exception e) {
                                ((AINotificationChatActivity) mActivity).hide3Dots();
                                e.printStackTrace();
                                Log.e("SAN java.KM ", "Exception " + e.getMessage());
                            }
                        }

                    } catch (Exception e) {
                        ((AINotificationChatActivity) mActivity).hide3Dots();
                    }
                }

                @Override
                public void onFailure(Call<YogaResponse> call, Throwable t) {
                    ((AINotificationChatActivity) mActivity).hide3Dots();
                    Log.e("SAN java.KM ", "t " + t.getMessage());
                }
            });
        } catch (Exception e) {
            ((AINotificationChatActivity) mActivity).hide3Dots();
            Log.e("SAN java.KM", e.toString());
        }
    }

    public void getReportButtonResponse(String ques) {

        try {
            BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils.getCustomObject(mActivity);
            String[] name = beanHoroPersonalInfo.getName().split(" ");

            Call<YogaResponse> call = ApiClient.getClient().create(ApiInterface.class).getReportButtonResponse(name[0], beanHoroPersonalInfo.getGender(),
                    String.valueOf(beanHoroPersonalInfo.getDateTime().getDay()), String.valueOf((beanHoroPersonalInfo.getDateTime().getMonth() + 1)),
                    String.valueOf(beanHoroPersonalInfo.getDateTime().getYear()), String.valueOf(beanHoroPersonalInfo.getDateTime().getHour()),
                    String.valueOf(beanHoroPersonalInfo.getDateTime().getMin()), String.valueOf(beanHoroPersonalInfo.getDateTime().getSecond()),
                    String.valueOf(beanHoroPersonalInfo.getDST()), beanHoroPersonalInfo.getPlace().getCityName().replace(" ", "%20"),
                    beanHoroPersonalInfo.getPlace().getLongDeg(), beanHoroPersonalInfo.getPlace().getLongMin(),
                    beanHoroPersonalInfo.getPlace().getLongDir(), beanHoroPersonalInfo.getPlace().getLatDeg(),
                    beanHoroPersonalInfo.getPlace().getLatMin(), beanHoroPersonalInfo.getPlace().getLatDir(), String.valueOf(beanHoroPersonalInfo.getPlace().getTimeZoneValue()),
                    String.valueOf(beanHoroPersonalInfo.getAyanIndex()), "0", String.valueOf(beanHoroPersonalInfo.getHoraryNumber()),
                    String.valueOf(lang), CUtils.getMyAndroidId(mActivity), "getanswer", ques, CUtils.getApplicationSignatureHashCode(mActivity),
                    CUtils.getUserID(mActivity));

            call.enqueue(new Callback<YogaResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<YogaResponse> call, Response<YogaResponse> response) {
                    ((AINotificationChatActivity) mActivity).hide3Dots();
                    try {
                        YogaResponse apiResponse = response.body();
                        if (apiResponse != null && !TextUtils.isEmpty(apiResponse.getResult())) {
                            StringBuilder message = new StringBuilder(apiResponse.getResult());
                            ArrayList<Buttons> buttons = apiResponse.getButtons();
                            for (Buttons item : buttons) {
                                String value = item.getValue();
                                if (TextUtils.isEmpty(item.getValue())) {
                                    value = "@";
                                }
                                message.append("#").append(item.getText()).append("#").append(value);
                            }
                            message.insert(0, apiResponse.getAnswerId() + "___");
                            try {
                                ((AINotificationChatActivity) mActivity).setResponse(message.toString(), true);
                                Log.e("SAN java.KM ", "db " + message);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("SAN java.KM ", "Exception " + e.getMessage());
                            }
                        }

                    } catch (Exception e) {
                        ((AINotificationChatActivity) mActivity).hide3Dots();
                    }
                }

                @Override
                public void onFailure(Call<YogaResponse> call, Throwable t) {
                    ((AINotificationChatActivity) mActivity).hide3Dots();
                    Log.d("java.KM ", "t " + t.getMessage());
                }
            });
        } catch (Exception e) {
            ((AINotificationChatActivity) mActivity).hide3Dots();
            Log.d("java.KM", e.toString());
        }
    }*/

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

    /*public long getLastVisibleQuestionId(int last) {
        if (messages != null) {
            int size = messages.size() - last;
            ChatMessage questionDetail;
            if (size >= 0) {
                if (last == -1)
                    questionDetail = messages.get(1);
                else
                    questionDetail = messages.get(messages.size() - 1);
                long id = questionDetail.chatId();
                String data = questionDetail.getMessageBody(mActivity);
                Log.d("java.workmanager", " chatId " + id);
                Log.d("java.workmanager", " chatId2 " + data);
                if (id == -1) {
                    return getLastVisibleQuestionId(++last);
                } else {
                    return id;
                    //     }
                }
            }
            return -1;
        }
    }*/
    private TypeWriterNotification typeWriterInstance(int index) {
        TypeWriterNotification newTypeWriter = new TypeWriterNotification(mActivity, index);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, toPixel(10), 0, 0);
        newTypeWriter.setLayoutParams(params);
        newTypeWriter.setPadding(toPixel(10), toPixel(4), toPixel(10), toPixel(4));
        newTypeWriter.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
        newTypeWriter.setTextSize(18);

        newTypeWriter.setTextColor(ContextCompat.getColor(mActivity,R.color.black));
        newTypeWriter.setBackgroundResource(R.drawable.bg_chat_white);

        return newTypeWriter;
    }

    public void markLastQuestionError() {
        try {
            ChatMessage message = messages.get(getItemCount() - 1);
            if (message.getAuthor().equalsIgnoreCase("USER")) {
                messages.get(getItemCount() - 1).setIsError(true);
                notifyItemChanged(getItemCount() - 1);
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

    private ArrayList<String> split(String text){
        String[] split = text.split("<br><br>");
        ArrayList<String> list = new ArrayList<>();
        for (String s : split) {
            if (!s.trim().isEmpty()) {
                list.add(s);
            }
        }
        return list;
    }

    public void updateMessageInAdapter(String updatedText) {
        int lastItem = getItemCount()-1;
        ChatMessage chatMessage = messages.get(lastItem);
        chatMessage.setMessageBody(updatedText);
        //refreshSingleItem(lastItem);
        //flag = false
        //setMessageText(updatedText, false, ((AINotificationChatActivity) mActivity).currentView, lastItem);
    }

    public void addTyping(){
        UserMessage message = new UserMessage();
        message.setAuthor(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_TYPING);
        message.setChatId(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_TYPING_CHAT_ID);
        messages.add(message);
        notifyItemChanged(messages.size()-1);
    }

    public void removeTyping(){
        int index = getIndexByChatId(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_TYPING_CHAT_ID);
        //Log.e("AIChatTesting","indexLastIndexOfTyping => " + index);
        if (index != -1) {
            messages.remove(index);
            notifyItemRemoved(index);
        }
    }

    private int getIndexByChatId(int chatId){
        int index = -1;
        for(int i = messages.size()-1; i > 0; i--){
            if(messages.get(i).chatId() == chatId){
                index = i;
                break;
            }
        }
        return index;
    }

}

