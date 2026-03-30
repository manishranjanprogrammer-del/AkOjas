package com.ojassoft.astrosage.varta.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.DisplayMetrics;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.customviews.common.ViewDrawRotateKundli;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.MessageDiffCallback;
import com.ojassoft.astrosage.varta.utils.TypeWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class AIChatMessageAdapter extends RecyclerView.Adapter<AIChatMessageAdapter.MyHolder> implements View.OnClickListener {
    private final int TYPE_MESSAGE = 0;
    private final int TYPE_STATUS = 1;

    private final int TYPE_USER = 0;
    private final int TYPE_ASTROLOGER = 1;
    private final Activity mActivity;
    private List<ChatMessage> messages;
    private final ArrayList<Integer> copyMessage;
    private final LayoutInflater layoutInflater;
    private final TreeSet<Integer> statusMessageSet = new TreeSet<>();
    public CScreenConstants SCREEN_CONSTANTS;
    public Boolean isLongPressClicked = false;
    private final int lang;
    int chartStyle;
    public AIChatMessageAdapter(Activity activity) {
        layoutInflater = activity.getLayoutInflater();
        messages = new ArrayList<>();
        copyMessage = new ArrayList<>();
        mActivity = activity;
        lang = CUtils.getIntData(mActivity, CGlobalVariables.app_language_key, CGlobalVariables.ENGLISH);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels - CUtils.convertDpToPx(activity,130);
        Typeface regularTypeface = CUtils.getRobotoFont(activity,lang , CGlobalVariables.regular);
        SCREEN_CONSTANTS = new CScreenConstants(activity,width,regularTypeface);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        chartStyle =  sharedPreferences.getInt(CGlobalVariables.APP_PREFS_ChartStyle,
                CGlobalVariables.CHART_NORTH_STYLE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.ai_chat_message_item, parent, false);
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

    public int getSize(){
        if(messages == null) {
            return 0;
        } else {
            int count = 0;
            for(ChatMessage message : messages){
                if(message.getAuthor().equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_ASTROLOGER)){
                    continue;
                }
                count++;
            }
            return count;
        }
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
            if(item.chatId() == com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_TYPING_CHAT_ID) continue;
            Message message = new Message();
            message.setAuthor(item.getAuthor());
            message.setDateCreated(item.getDateCreated());
            message.setMessageBody(item.getMessageBody(mActivity));
            message.setSeen(false);
            message.setChatId(item.chatId());
            message.setLike(item.getLike());
            message.setUnlike(item.getUnlike());
            message.setIsError(item.getIsError());
            message.setPlanetsInRashiLagna(item.getPlanetsInRashiLagna());
            message.setPlanetDegreeArray(item.getPlanetDegreeArray());
            list.add(new UserMessage(message));
        }

        return list;
    }

    public List<Integer> getCopyMessageList() {
        return copyMessage;
    }

    public void addAllMessages(List<ChatMessage> messagesList) {
        try {
            messages.clear();
            messages.addAll(messagesList);
            refreshList();
        } catch (Exception e){
            //
        }
    }

    public void addMessage(Message message) {
        updateMessageList(message);
    }


    public void updateMessageList(Message message) {
        int existingIndex = getMessagePosition(message.getAuthor(), message.getChatId());
        if (existingIndex != -1) {
            ChatMessage existingMessage = messages.get(existingIndex);
            existingMessage.setMessageBody(message.getMessageBody());
            existingMessage.setSeen(message.isSeen());
            existingMessage.setDelayed(message.isDelayed());
            existingMessage.setIsError(message.getIsError());
            existingMessage.setLike(message.getLike());
            existingMessage.setUnlike(message.getUnlike());
            existingMessage.setPlanetsInRashiLagna(message.getPlanetsInRashiLagna());
            existingMessage.setPlanetDegreeArray(message.getPlanetDegreeArray());
            existingMessage.setTimeStamp(message.getTimeStamp());
            notifyItemChanged(existingIndex);
            return;
        }

        List<ChatMessage> newList = new ArrayList<>(messages);
        newList.add(new UserMessage(message));

        MessageDiffCallback diffCallback = new MessageDiffCallback(messages, newList, mActivity);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        messages.add(new UserMessage(message));
        diffResult.dispatchUpdatesTo(this);
        //notifyItemInserted(getItemCount()-1);
    }

    private boolean isStopTyping = false;

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
        ImageView ivWaitMsg = convertView.findViewById(R.id.ivWaitMsg);
        RelativeLayout followLayout = convertView.findViewById(R.id.follow_msg_layout);
        TextView tvStatusMessage = convertView.findViewById(R.id.tvStatusMessage);
        ImageView astroProfileImage = convertView.findViewById(R.id.ivFRPAFC);
        LinearLayout errorLayout = convertView.findViewById(R.id.error_layout);
        tvStatusMessage.setVisibility(View.GONE);
        ivWaitMsg.setVisibility(View.GONE);
        followLayout.setVisibility(View.GONE);
        String author = message.getAuthor();
        Log.d("AIChatTesting", "messageToShow author: "+author);
        llMessageLayout.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_chat_selected_removed));
        if (mActivity instanceof AIChatWindowActivity && ((AIChatWindowActivity) mActivity).astrologerProfileUrl != null && ((AIChatWindowActivity) mActivity).astrologerProfileUrl.length() > 0) {
            String astroImage = com.ojassoft.astrosage.varta.utils.CGlobalVariables.IMAGE_DOMAIN + ((AIChatWindowActivity) mActivity).astrologerProfileUrl;
            Glide.with(astroProfileImage).load(astroImage).circleCrop().into(astroProfileImage);
        }
        astroProfileImage.setOnClickListener(view -> {
            CUtils.fcmAnalyticsEvents("astrologer_picture_click_in_ai_chat_window", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            ((AIChatWindowActivity) mActivity).showAstrologorFullProfile();
        });

        if (author.equalsIgnoreCase("SAMPLE_REPORT")) {
            userView.setVisibility(View.GONE);
            astrologerView.setVisibility(View.GONE);
        } else if (author.equalsIgnoreCase("USER")) {
            userView.setVisibility(View.VISIBLE);
            astrologerView.setVisibility(View.GONE);

            TextView usertextViewMessage = convertView.findViewById(R.id.usertextViewMessage);
            TextView usertextViewDate = convertView.findViewById(R.id.usertextViewDate);
            ImageView seenImageView = convertView.findViewById(R.id.seenImageView);
            usertextViewMessage.setText(message.getMessageBody(mActivity));
            usertextViewDate.setText(message.getDateCreated());
            if(((AIChatWindowActivity)mActivity).isAiBehaveLikeHuman) seenImageView.setVisibility(View.VISIBLE);
            if (message.getIsError()) {
                errorLayout.setVisibility(View.VISIBLE);
                errorLayout.setOnClickListener(view ->  ((AIChatWindowActivity) mActivity).showSendAgainAlert(message.getMessageBody(mActivity)));
                userView.setOnClickListener(view -> {
                    ((AIChatWindowActivity) mActivity).showSendAgainAlert(message.getMessageBody(mActivity));
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
                author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_INACTIVITY) ||
                author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_PASS_QUESTION_LIMIT_REACHED) ||
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
            }else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_INACTIVITY)) {
                tvStatusMessage.setText(mActivity.getString(R.string.chat_ended_due_to_inactivity));
            } else if (author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_PASS_QUESTION_LIMIT_REACHED)) {
                tvStatusMessage.setText(mActivity.getString(R.string.error_ai_chat_limit_reached));
            }else if(author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_TYPING)){
                tvStatusMessage.setVisibility(View.GONE);
                ivWaitMsg.setVisibility(View.VISIBLE);
                Glide.with(ivWaitMsg).load(R.drawable.typing).into(ivWaitMsg);
            }
        }else if(author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_SHOW_FOLLOW_LAYOUT)){
            userView.setVisibility(View.GONE);
            astrologerView.setVisibility(View.GONE);
            tvStatusMessage.setVisibility(View.GONE);
            followLayout.setVisibility(View.VISIBLE);
            TextView followAstrologer = convertView.findViewById(R.id.followAstrologer);
            if(AIChatWindowActivity.followStatus.equalsIgnoreCase("0"))
                followAstrologer.setText(mActivity.getString(R.string.follow));
            else
                followAstrologer.setText(mActivity.getString(R.string.following));


            followAstrologer.setOnClickListener(v -> {
                String followAstro="";
                if(AIChatWindowActivity.followStatus.equalsIgnoreCase("0")) {
                    followAstro = "1";
                    followAstrologer.setText(mActivity.getString(R.string.following));
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("chat_follow_btn_click",
                            com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                } else {
                    followAstrologer.setText(mActivity.getString(R.string.follow));
                    followAstro = "0";
                    com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("chat_unfollowed_btn_click",
                            com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }
                ((AIChatWindowActivity) mActivity).onFollowClick(followAstro);
            });
        } else {
            userView.setVisibility(View.GONE);
            astrologerView.setVisibility(View.VISIBLE);

            TypeWriter textViewMessage = (TypeWriter) convertView.findViewById(R.id.textViewMessage);
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
            ImageView ivSpeaker = convertView.findViewById(R.id.ivSpeakMessage);
            LinearLayout llLikeUnlike = convertView.findViewById(R.id.llLikeUnlike);
            ImageView ivShareMsg = convertView.findViewById(R.id.ivShareMessage);
            ImageView ivCopyChat = convertView.findViewById(R.id.iv_copy_chat);
            //TextView tvDefaultChatButton = convertView.findViewById(R.id.tvDefaultChatButton);
            if(!((AIChatWindowActivity)mActivity).isTypingGreetingMessage)
                llLikeUnlike.setVisibility(View.VISIBLE);
            if(((AIChatWindowActivity) mActivity).speakingPosition != position){
                message.setIsSpeaking(false);
                ivSpeaker.setBackgroundResource(0);
                int tintColor = ContextCompat.getColor(mActivity, R.color.black);
                ivSpeaker.setColorFilter(tintColor);
            }
            if(message.getLike() == 1){
                ivLikeMessage.setImageResource(R.drawable.ic_like);
                ivUnlikeMessage.setImageResource(R.drawable.ic_like_outline);
            }else if(message.getUnlike() ==1){
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
                    if (msg.getAuthor().equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER) && position >3) {
                        question = msg.getMessageBody(mActivity);
                    }
                    String text = messages.get(position).getMessageBody(mActivity);
                    ((AIChatWindowActivity) mActivity).shareMessage(question, text);
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
                AIChatWindowActivity.setLikeUnlike(mActivity,message.chatId(),likeValue,0);
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

                AIChatWindowActivity.setLikeUnlike(mActivity,message.chatId(),0,unLikeValue);
            });

            ivSpeaker.setOnClickListener(v -> {
                if (((AIChatWindowActivity) mActivity).speakingPosition == position) {
                    ((AIChatWindowActivity) mActivity).stopSpeaking();
                    message.setIsSpeaking(false);
                } else {
                    int speakPos = ((AIChatWindowActivity) mActivity).speakingPosition;
                    if ( speakPos> -1 && speakPos < messages.size() ) {//it means other message is speaking currently
                        messages.get(((AIChatWindowActivity) mActivity).speakingPosition ).setIsSpeaking(false);
                        ((AIChatWindowActivity) mActivity).stopSpeaking();
                    }
                    String text = removeHtmlTags(message.getMessageBody(mActivity));
                    // Regular expression to remove emojis
                    text = text.replaceAll("[\\p{So}\\p{Cn}]", "");

                    ((AIChatWindowActivity) mActivity).speakOutMessage(text, position);
                    message.setIsSpeaking(true);
                    ivSpeaker.setBackgroundResource(R.drawable.circle_yellow);
                    int tintColor = ContextCompat.getColor(mActivity, R.color.white);
                    ivSpeaker.setColorFilter(tintColor);
                }
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
                if(textViewMessage!=null)textViewMessage.setVisibility(View.GONE);
                view.setVisibility(View.GONE);

                youtubeFL.setOnClickListener(v -> {
                    /*YoutubeVideoPlayerFragment youtubeVideoPlayerFragment = new YoutubeVideoPlayerFragment(videoId);
                    youtubeVideoPlayerFragment.show(((AIChatWindowActivity) mActivity).getSupportFragmentManager(), "youtubeVideoPlayerFragment");*/
                });

                ((AIChatWindowActivity) mActivity).scrollMyListViewToBottom();

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
                        tv.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary_day_night));
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

                }else if(author.equalsIgnoreCase(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDLI_ASTROLOGER)){
                    /**
                     * This block is used to show kundli view
                     */
                    LinearLayout llTypeWriter = convertView.findViewById(R.id.type_writer_layout);
                    LinearLayout layout = new LinearLayout(mActivity);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    TextView textView = new TextView(mActivity);
                    textView.setTextSize(18);
                    textView.setPadding(0,0,0,toPixel(10));
                    textView.setTextColor(ContextCompat.getColor(mActivity,R.color.black));
                    textView.setText(message.getMessageBody(mActivity));
                    layout.addView(textView);
                    llTypeWriter.removeAllViews();
                    llLikeUnlike.setVisibility(View.GONE);
                    //Array of KundaliType ENUM to get Type of selected kundali style
                    CGlobalVariables.enuKundliType[] kundliTypes = new CGlobalVariables.enuKundliType[] {CGlobalVariables.enuKundliType.NORTH, CGlobalVariables.enuKundliType.SOUTH, CGlobalVariables.enuKundliType.EAST};

                    View resultView = new ViewDrawRotateKundli(mActivity, mActivity.getResources().getStringArray(R.array.VarChartPlanets), message.getPlanetsInRashiLagna(), message.getPlanetDegreeArray(), kundliTypes[chartStyle], CUtils.isTablet(mActivity), SCREEN_CONSTANTS, -1, lang,true);
                    layout.setBackgroundResource(R.drawable.bg_chat_white);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    int padding = toPixel(10);
                    layout.setPadding(padding,padding,padding,padding);
                    resultView.setLayoutParams(new ViewGroup.LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth, ((int) SCREEN_CONSTANTS.DeviceScreenHeight)));
                    layout.addView(resultView);
                    llTypeWriter.addView(layout);
                    textViewDate.setText(message.getDateCreated());
                    return;
                }else {
                    view.setVisibility(View.GONE);
                    text = textMessage;
                }

                setMessageText(text, messages.get(position).isSeen(), convertView,message.isDelayed(), position);
                message.setDelayed(false);
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

    private void setMessageText(String text, boolean isSeen, View view,boolean isDelayed, int position) {
        //Log.e("TypeWriter","All text = "+text);
        ArrayList<String> textMessage = split(text);

        if (textMessage.isEmpty()) return;

        LinearLayout llTypeWriter = view.findViewById(R.id.type_writer_layout);
        llTypeWriter.removeAllViews();
        TypeWriter typeWriter = typeWriterInstance(0);

        if (isSeen) {
            llTypeWriter.addView(typeWriter);
            ((AIChatWindowActivity) mActivity).scrollWhileTypeWriter = true;
            ((AIChatWindowActivity) mActivity).stopTypeWriter = false;
            ((AIChatWindowActivity) mActivity).llChatButton = view;
            // Streaming always starts from the first chunk inside this chat row.
            ((AIChatWindowActivity) mActivity).currentTypeWriterPosition = 0;
            ((AIChatWindowActivity) mActivity).llTypeWriterParent = llTypeWriter;
            ((AIChatWindowActivity) mActivity).currentTypeWriter = typeWriter;
            //Log.e("TypeWriter","All text = "+textMessage[0]);
            if (!textMessage.isEmpty()) {
                typeWriter.animateText(textMessage.get(0), (AIChatWindowActivity) mActivity, ((AIChatWindowActivity) mActivity));
                ((AIChatWindowActivity) mActivity).scheduleTypewriterSafetyCompletion(typeWriter, 0, textMessage.get(0));
            }
        } else {
            isStopTyping = false;
            TypeWriter.OnTypingComplete typingComplete = new TypeWriter.OnTypingComplete() {
                @Override
                public void onFinishTyping(int index) {
                    if (index + 1 >= textMessage.size() || isStopTyping) {
                        return;
                    }
                    TypeWriter instance = typeWriterInstance((index + 1));
                    llTypeWriter.addView(instance);
                    if(index+2 >= textMessage.size()) ((AIChatWindowActivity) mActivity).humanTypingToggle(false);
                    instance.setText(convertBulletPoint(textMessage.get(index + 1)), this);
                }
            };
            TypeWriter newTypeWriter = typeWriterInstance(0);
            llTypeWriter.addView(newTypeWriter);
            newTypeWriter.setText(textMessage.get(0), typingComplete);
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
            ((AIChatWindowActivity) mActivity).updateCopyView(true);
        else {
            ((AIChatWindowActivity) mActivity).updateCopyView(false);
            isLongPressClicked = false;
        }


        Log.e("MyTag ", " updateCopyList after copyMessage array => " + copyMessage);

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


    private TypeWriter typeWriterInstance(int index) {
        TypeWriter newTypeWriter = new TypeWriter(mActivity, index);
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
        String[] split;
        boolean isEnglish = true;
        if(((AIChatWindowActivity)mActivity).isAiBehaveLikeHuman){
            isEnglish = CUtils.isNotDevanagari(text.substring(0, Math.min(text.length(), 15)));
            split = isEnglish ? text.split("(?<!\\d)\\.(?!\\d)") : text.split("।") ;

        }else{
            split = text.split("<br><br>");
        }
        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<split.length;i++) {
           String s = split[i];
            if (!s.trim().isEmpty()) {
                if (((AIChatWindowActivity)mActivity).isAiBehaveLikeHuman && i<split.length-1){
                    s = s + (isEnglish ? "." : "।");
                }
                list.add(s);
            }
        }
        return list;
    }

    /**
     * Updates the most recent adapter item for the supplied chat id and returns its position.
     *
     * This keeps streamed AI updates pinned to the active answer bubble even when status rows
     * such as typing/follow/recharge messages are currently the last adapter items.
     *
     * @param chatId active chat id whose message should be refreshed
     * @param updatedText latest full message body for that chat id
     * @return adapter position that was updated, or `-1` when no matching item exists
     */
    public int updateMessageInAdapter(long chatId, String updatedText) {
        int messageIndex = getLatestMessageIndexByChatId(chatId);
        if (messageIndex == -1) {
            return -1;
        }
        ChatMessage chatMessage = messages.get(messageIndex);
        chatMessage.setMessageBody(updatedText);
        return messageIndex;
    }

    /**
     * Updates the current last adapter item as a legacy fallback for flows that do not track chat id.
     *
     * @param updatedText latest message text for the last visible item
     */
    public void updateMessageInAdapter(String updatedText) {
        int lastItem = getItemCount()-1;
        if (lastItem < 0) {
            return;
        }
        ChatMessage chatMessage = messages.get(lastItem);
        chatMessage.setMessageBody(updatedText);
    }

    /**
     * Finds the newest adapter row that belongs to the supplied chat id.
     *
     * @param chatId message chat id to locate
     * @return matching adapter index, or `-1` when not found
     */
    private int getLatestMessageIndexByChatId(long chatId) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).chatId() == chatId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Updates the exact message row that matches the streamed author/chatId pair.
     *
     * @return adapter position for the updated row, or `-1` when no matching row exists yet
     */
    public int updateMessageInAdapter(String author, long chatId, String updatedText) {
        int position = getMessagePosition(author, chatId);
        if (position == -1) {
            return -1;
        }
        messages.get(position).setMessageBody(updatedText);
        return position;
    }

    public void stopTyping(){
        isStopTyping = true;
        int lastItem = getItemCount()-1;
        ChatMessage chatMessage = messages.get(lastItem);
        chatMessage.setDelayed(false);
    }

    /**
     * Stops delayed rendering on the exact message row that owns the active AI answer.
     */
    public int stopTyping(String author, long chatId){
        isStopTyping = true;
        int position = getMessagePosition(author, chatId);
        if (position == -1) {
            return -1;
        }
        messages.get(position).setDelayed(false);
        return position;
    }

    /**
     * Marks the streamed message as fully rendered so future rebinds show static text instead of retyping it.
     */
    public int finishTyping(String author, long chatId) {
        int position = stopTyping(author, chatId);
        if (position == -1) {
            return -1;
        }
        messages.get(position).setSeen(false);
        return position;
    }

    private static String removeHtmlTags(String textMessage) {
        String text = textMessage.replace("<br><br>", "\n").replace("<br>", "\n")
                .replace("<strong>", "")
                .replace("<em>", "")
                .replace("</em>", "")
                .replace("</strong>", "")
                .replace("*", "")
                .replace("- <strong>","")
                .replace("</strong>:","");
        if (text.contains("^^")) {
            int index = text.indexOf("^^");
            if (index == -1) {
                index = text.length();
            }
            text = text.substring(0, index - 1);
        }
        return text;
    }

    public void appendMessageList(ArrayList<ChatMessage> list){
        if (list == null || list.isEmpty()) {
            return;
        }
        messages.addAll(0,list);
        notifyItemRangeInserted(0, list.size());
    }

    public void addTyping(){
        if (getIndexByChatId(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_TYPING_CHAT_ID) != -1) {
            return;
        }
        UserMessage message = new UserMessage();
        message.setAuthor(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STATUS_TYPING);
        message.setChatId(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_TYPING_CHAT_ID);
        messages.add(message);
        notifyItemInserted(messages.size()-1);
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

    public void addFollowMessage(){
        if (getIndexByChatId(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SHOW_FOLLOW_LAYOUT_CHAT_ID) != -1) {
            return;
        }
        UserMessage message = new UserMessage();
        message.setAuthor(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_SHOW_FOLLOW_LAYOUT);
        message.setChatId(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SHOW_FOLLOW_LAYOUT_CHAT_ID);
        messages.add(message);
        notifyItemInserted(messages.size()-1);
    }

    public void removeFollowMessage(){
        int index = getIndexByChatId(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SHOW_FOLLOW_LAYOUT_CHAT_ID);
        //Log.e("AIChatTesting","indexLastIndexOfTyping => " + index);
        if (index != -1) {
            messages.remove(index);
            notifyItemRemoved(index);
        }
    }

    /**
     * Returns the adapter position for a logical chat message identified by author and chatId.
     */
    public int getMessagePosition(String author, long chatId) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessage message = messages.get(i);
            if (message.chatId() == chatId && author.equalsIgnoreCase(message.getAuthor())) {
                return i;
            }
        }
        return -1;
    }

}

