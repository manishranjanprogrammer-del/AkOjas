package com.ojassoft.astrosage.varta.twiliochat.chat.history;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageChatHistoryAdapter extends RecyclerView.Adapter<MessageChatHistoryAdapter.ViewHolder> {

    Activity currentActivity;
    ArrayList<MessageHistory> messageHistoryArrayList;
    Bundle bundle;
    private RecyclerClickListner recyclerClickListner;

    public MessageChatHistoryAdapter(Activity currentActivity, ArrayList<MessageHistory> messageHistoryArrayList) {
        this.currentActivity = currentActivity;
        this.messageHistoryArrayList = messageHistoryArrayList;
    }

    public void UpdateMessageChatHistoryAdapter(ArrayList<MessageHistory> messageHistoryArrayListt) {
        this.messageHistoryArrayList = messageHistoryArrayListt;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.message, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (messageHistoryArrayList != null && messageHistoryArrayList.size() > 0) {
            MessageHistory messageHistory = messageHistoryArrayList.get(position);
            if (!(messageHistory.getFrom().equalsIgnoreCase(CGlobalVariables.USER))) {
                holder.userView.setVisibility(View.GONE);
                holder.astrologerView.setVisibility(View.VISIBLE);

                if(messageHistory.getMsgBody().length()>25){
                    holder.textViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    //holder.textViewMessage.setPadding(40,10,10,10);
                }else{
                    holder.textViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //holder.textViewMessage.setPadding(10,10,10,10);
                }

                holder.textViewMessage.setText(Html.fromHtml(convertToBoldHtml(messageHistory.getMsgBody())));
                //Log.e("LinkClick = ", "textViewMessage = "+holder.textViewMessage.getText().toString());
                //holder.textViewDate.setText(changeDateFormat(messageHistory.getMsgDate()));
                holder.textViewDate.setText(messageHistory.getMsgDate());

                setLinkclickEvent(holder.textViewMessage, new HandleLinkClickInsideTextView() {
                    public void onLinkClicked(String url) {
                        try {
                            com.ojassoft.astrosage.utils.CUtils.divertToScreen(currentActivity, url, 0);
                        }catch (Exception e){
                            //
                        }
                    }
                });

                String textMessage = String.valueOf(Html.fromHtml(convertToBoldHtml(messageHistory.getMsgBody())));

                if (textMessage.contains("~~")) {
                    holder.llButtonsParent.setVisibility(View.VISIBLE);
                    String text = textMessage.substring(0, textMessage.indexOf("~~"));
                    holder.textViewMessage.setText(text);
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    String[] data = messageHistoryArrayList.get(position).getMsgBody().split("~~");

                    holder.llChatButtons.removeAllViews();
                    for (int i = 1; i < data.length; i = i + 2) {
                        TextView tv = new TextView(currentActivity);

                        lparams.setMargins(50, 25, 50, 15);
                        lparams.gravity = Gravity.CENTER;
                        tv.setLayoutParams(lparams);
                        tv.setText(data[i]);
                        tv.setTextSize(16);
                        tv.setGravity(Gravity.CENTER);
                        if (data.length > i + 1)
                            tv.setTag(data[i + 1]);
                        tv.setTextColor(currentActivity.getResources().getColor(R.color.primary_orange));
                        //tv.setTypeface(openSensSemiBold);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String clickData = (String) v.getTag();
                                if (clickData.contains("http://") || clickData.contains("https://")) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(clickData));
                                    currentActivity.startActivity(intent);
                                }
                            }
                        });

                        LinearLayout.LayoutParams lparamsView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                        View line = new View(currentActivity);
                        View line2 = new View(currentActivity);
                        line2.setLayoutParams(lparamsView);
                        line.setLayoutParams(lparamsView);
                        line.setBackgroundColor(currentActivity.getResources().getColor(R.color.light_border));


                        holder.llChatButtons.addView(tv);
                        holder.llChatButtons.addView(line2);

                        if (data.length > i + 2) {
                            line.setVisibility(View.VISIBLE);
                            holder.llChatButtons.addView(line);
                        }
                    }

                } else {
                    holder.textViewMessage.setText(textMessage);
                }

            } else {
                holder.userView.setVisibility(View.VISIBLE);
                holder.astrologerView.setVisibility(View.GONE);

                if(messageHistory.getMsgBody().length()>25){
                    holder.usertextViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    //holder.usertextViewMessage.setPadding(40,10,10,10);
                }else{
                    holder.usertextViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    //holder.usertextViewMessage.setPadding(10,10,10,10);
                }

                /*if(messageHistory.getMsgBody().toLowerCase().contains("date of birth:")
                        && messageHistory.getMsgBody().toLowerCase().contains("place of birth:")){
                    holder.astrologerGetKundli.setVisibility(View.VISIBLE);
                }else{
                    holder.astrologerGetKundli.setVisibility(View.GONE);
                }*/

                holder.usertextViewMessage.setText(messageHistory.getMsgBody());
                //Log.e("LinkClick = ", "usertextViewMessage = "+holder.usertextViewMessage.getText().toString());

                //holder.usertextViewDate.setText(changeDateFormat(messageHistory.getMsgDate()));
                holder.usertextViewDate.setText(messageHistory.getMsgDate());

                setLinkclickEvent(holder.usertextViewMessage, new HandleLinkClickInsideTextView() {
                    public void onLinkClicked(String url) {
                        try {
                            com.ojassoft.astrosage.utils.CUtils.divertToScreen(currentActivity, url, 0);
                        }catch (Exception e){
                            //
                        }
                    }
                });
            }
        }

    }


    public String changeDateFormat(String datee){

        //"time": "2020-12-21T11:40:34Z"
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            if(datee != null) {
                date = formatter.parse(datee);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String orderOnDate = "";
        SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy - hh:mma");
        newFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        if(date!= null) {
            orderOnDate = newFormat.format(date);
        }

        return orderOnDate;
    }
    @Override
    public int getItemCount() {
        if (messageHistoryArrayList == null) return 0;
        return messageHistoryArrayList.size();
    }

    public void setOnItemClickListner(RecyclerClickListner recyclerClickListner) {
        this.recyclerClickListner = recyclerClickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView usertextViewDate;
        TextView usertextViewMessage;
        TextView textViewDate;
        TextView textViewMessage;
        LinearLayout userView, astrologerView, astrologerGetKundli, llButtonsParent, llChatButtons;

        public ViewHolder(View itemView) {
            super(itemView);

             userView = (LinearLayout) itemView.findViewById(R.id.user_view);
             astrologerView = (LinearLayout) itemView.findViewById(R.id.astrologer_view);
             //astrologerGetKundli = (LinearLayout) itemView.findViewById(R.id.ll_get_kundli);
             textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
             textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
             usertextViewMessage = (TextView) itemView.findViewById(R.id.usertextViewMessage);
             usertextViewDate = (TextView) itemView.findViewById(R.id.usertextViewDate);
            llButtonsParent = itemView.findViewById(R.id.llButtonsParent);
            llChatButtons = itemView.findViewById(R.id.llChatButtons);

//            FontUtils.changeFont(currentActivity, textViewMessage, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
//            FontUtils.changeFont(currentActivity, textViewDate, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
//            FontUtils.changeFont(currentActivity, usertextViewMessage, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
//            FontUtils.changeFont(currentActivity, usertextViewDate, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            //astrologerGetKundli.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            //recyclerClickListner.onItemClick(getAdapterPosition(), v);
            switch (v.getId()){

            }
        }
    }

    public static void setLinkclickEvent(TextView tv, HandleLinkClickInsideTextView clickInterface) {
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
                InternalURLSpan span = new InternalURLSpan();
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
        private HandleLinkClickInsideTextView clickInterface;

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

        public void setClickInterface(HandleLinkClickInsideTextView clickInterface) {
            this.clickInterface = clickInterface;
        }

        public HandleLinkClickInsideTextView getClickInterface() {
            return this.clickInterface;
        }

    }

    public String convertToBoldHtml(String input) {
        // Regular expression to find text between ** and replace with <b> tags
        Pattern pattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
        Matcher matcher = pattern.matcher(input);

        // Replace all occurrences of **text** with <b>text</b>
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, "<b>" + matcher.group(1) + "</b>");
        }
        matcher.appendTail(result);

        return result.toString();  // Return the converted HTML-compatible string
    }

    public interface HandleLinkClickInsideTextView {
        public void onLinkClicked(String url);
    }
}


