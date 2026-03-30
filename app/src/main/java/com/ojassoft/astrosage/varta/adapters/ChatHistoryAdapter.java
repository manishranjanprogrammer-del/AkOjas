package com.ojassoft.astrosage.varta.adapters;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.model.AstrologerBioModel;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.ChatHistoryBean;
import com.ojassoft.astrosage.varta.twiliochat.chat.history.ChatHistoryNew;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.MyViewHolder> implements VolleyResponse {

    Context context;
    ArrayList<ChatHistoryBean> chatHistoryList;
    String astrologer;

    public ChatHistoryAdapter(Context context, ArrayList<ChatHistoryBean> chatHistoryList) {
        if (this.chatHistoryList == null) {
            this.chatHistoryList = new ArrayList<>();
        }else {
            this.chatHistoryList.clear();

        }
        this.chatHistoryList.addAll(chatHistoryList);
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_history_row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int pos = position;
        final ChatHistoryBean chatHistoryBean = chatHistoryList.get(pos);
        holder.astrologerNameTxt.setText(chatHistoryBean.getAstrologerName());
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(chatHistoryBean.getConsultationTime());
            Date endDate = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).parse(Calendar.getInstance().getTime().toString());
            long duration = endDate.getTime() - startDate.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
            if (diffInDays > 90){
                holder.tvShare.setVisibility(View.GONE);
            } else {
                holder.tvShare.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){
            //Log.d("cHistory","e=>"+e);
        }
        holder.dateTimeTxt.setText(CUtils.covertDateFormate(chatHistoryBean.getConsultationTime()));

        String chatRateStr = chatHistoryBean.getAstrologerServiceRs();
        if(!TextUtils.isEmpty(chatRateStr) && chatRateStr.equals("0.0")){
            holder.rate_tv.setText(context.getResources().getString(R.string.text_free));
        }else {
            holder.rate_tv.setText("@ " + context.getResources().getString(R.string.rs_sign) + chatRateStr + "/" + context.getResources().getString(R.string.short_minute));
        }
        holder.astrologerPriceTxt.setText(context.getResources().getString(R.string.rs_sign) + chatHistoryBean.getCallAmount());

        // Determine the duration value and unit label (handles "minute", "second", null, or empty types)
        String unitType = chatHistoryBean.getDurationUnitType();
        boolean isMinute = "minute".equalsIgnoreCase(unitType);

        // Minutes use 'getCallDurationMin()'; Seconds/Null/Empty use 'getCallDuration()'
        String durationValue = isMinute ? chatHistoryBean.getCallDurationMin() : chatHistoryBean.getCallDuration();
        String unitLabel = context.getString(isMinute ? R.string.minute : R.string.full_second);

        if (isMinute) {
            try {
                // If duration is greater than 1.0, switch to plural "minutes"
                if (!TextUtils.isEmpty(durationValue) && Float.parseFloat(durationValue) > 1.0) {
                    unitLabel = context.getString(R.string.minutes);
                }
            } catch (Exception e) {
                // Fallback to singular "minute" already set above
                Log.e("ChatHistoryAdapter", "Error parsing minute duration: " + durationValue);
            }
        }

        // Set the final formatted text: e.g., "Duration: 5 Minutes" or "Duration: 45 Seconds"
        holder.duration_tv.setText(String.format("%s: %s %s",
                context.getString(R.string.duration),
                durationValue,
                unitLabel));

        if (chatHistoryBean.getRefundStatus().equalsIgnoreCase("true"))
            holder.refundStatus.setVisibility(View.VISIBLE);
        else
            holder.refundStatus.setVisibility(View.GONE);

        String astrologerProfileUrl = "";
        if (chatHistoryBean.getAstrologerImageFile() != null && chatHistoryBean.getAstrologerImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + chatHistoryBean.getAstrologerImageFile();
           // holder.riProfileImg.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
            Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.riProfileImg);

        }

        holder.chat_now_btn_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openChatHistory = new Intent(context, ChatHistoryNew.class);
                openChatHistory.putExtra(CGlobalVariables.CHANNEL_ID, chatHistoryBean.getCallChatId());
                openChatHistory.putExtra(CGlobalVariables.ASTROLOGER_NAME, chatHistoryBean.getAstrologerName());
                openChatHistory.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_PIC, chatHistoryBean.getAstrologerImageFile());
                openChatHistory.putExtra(CGlobalVariables.URL_TEXT, chatHistoryBean.getUrlText());
                openChatHistory.putExtra("astrologerphone", chatHistoryBean.getAstrologerPhoneNo());
                openChatHistory.putExtra(CGlobalVariables.CHAT_HISTORY_BEAN, chatHistoryBean); //for initiate chat
                context.startActivity(openChatHistory);
            }
        });

        holder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents("share_chat_with_friends", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                astrologer = chatHistoryBean.getAstrologerName();
                getChatHistory(chatHistoryBean.getCallChatId(),1,false);
            }
        });

        holder.riProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAstrologerDetail(pos, v);
            }
        });

        holder.astrologerNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAstrologerDetail(pos, v);
            }
        });
        holder.chatAgainTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processChatAgain(pos,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        int count =0;
        if(chatHistoryList.size()>0){
            count = chatHistoryList.size();
        }
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView astrologerNameTxt, astrologerPriceTxt, dateTimeTxt, chatStatusTxt, statusTxt, duration_tv,chat_now_btn_txt,rate_tv;
        CircularNetworkImageView riProfileImg;
        TextView chatAgainTV;
        TextView refundStatus,tvShare;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            astrologerNameTxt = (TextView) itemView.findViewById(R.id.astrologer_name_txt);
            astrologerPriceTxt = (TextView) itemView.findViewById(R.id.astrologer_price_txt);
            dateTimeTxt = (TextView) itemView.findViewById(R.id.date_time_txt);
            chatStatusTxt = (TextView) itemView.findViewById(R.id.chat_status_txt);
            statusTxt = (TextView)itemView.findViewById(R.id.status_txt);
            duration_tv = (TextView)itemView.findViewById(R.id.duration_tv);
            riProfileImg = (CircularNetworkImageView) itemView.findViewById(R.id.ri_profile_img);
            chat_now_btn_txt = (TextView)itemView.findViewById(R.id.chat_now_btn_txt);
            rate_tv = (TextView)itemView.findViewById(R.id.rate_tv);
            chatAgainTV = itemView.findViewById(R.id.chat_again_btn_txt);
            refundStatus = (TextView) itemView.findViewById(R.id.refund_status);
            tvShare = (TextView) itemView.findViewById(R.id.tv_share);

            FontUtils.changeFont(context, astrologerNameTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, astrologerPriceTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, dateTimeTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, rate_tv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, chatStatusTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(context, chat_now_btn_txt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, chatAgainTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, refundStatus, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, tvShare, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        }
    }

    private void openAstrologerDetail(int pos, View view) {
        try {
            Bundle bundle = new Bundle();
            String phoneNumber = chatHistoryList.get(pos).getAstrologerPhoneNo();
            String urlText = chatHistoryList.get(pos).getUrlText();

            bundle.putString("phoneNumber", phoneNumber);
            bundle.putString("urlText", urlText);
            Intent intent = new Intent(context, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    private void getChatHistory(String channelID, int pageNoo, boolean isShowProgressBarr) {
        //RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            String Url = "";
            if(channelID != null){
                if(channelID.startsWith("FCH")){
                    Url = CGlobalVariables.CHAT_HISTORY_URL_V2;
                    ApiList api = RetrofitClient.getInstance().create(ApiList.class);
                    Call<ResponseBody> call = api.getChatHistoryV2(getChatParams(channelID, pageNoo));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String myResponse = response.body().string();
                                parseHistoryResponse(myResponse);
                            } catch (Exception e) {
                                //
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //
                        }
                    });
                }else{
                    Url = CGlobalVariables.CHAT_HISTORY_URL;
                    ApiList api = RetrofitClient.getInstance().create(ApiList.class);
                    Call<ResponseBody> call = api.getChatHistory(getChatParams(channelID, pageNoo));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String myResponse = response.body().string();
                                parseHistoryResponse(myResponse);
                            } catch (Exception e) {
                                //
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //
                        }
                    });
                }
            }
            //Log.e("SAN CHAT Hist url " ,  Url);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, Url,
//                    this, false, getChatParams(channelID, pageNoo), 1).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);



    }

    private void parseHistoryResponse(String response){
        try {
            // String sResponse = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            String data ="Astrologer - "+astrologer+"\n\n";
            Intent intent = new Intent(Intent.ACTION_SEND);
            JSONObject jsonObject = new JSONObject(response);
            String statuss = jsonObject.getString("status");
            if(statuss.equalsIgnoreCase("1")) {

                JSONArray msgJsonArray = jsonObject.getJSONArray("messages");
                if (msgJsonArray != null && msgJsonArray.length() > 0) {
                    for (int i = 0; i < msgJsonArray.length(); i++) {
                        JSONObject msgObject = msgJsonArray.getJSONObject(i);
                        String from = msgObject.getString("from");
                        String body = msgObject.getString("body");
                        String time = msgObject.getString("time");
                        if(from.equalsIgnoreCase("User"))
                            data += time +" - " +"Me: " +body +"\n";
                        else
                            data += time +" - "+ from +": " +body +"\n";
                    }
                    data = data.replace("<br>", "\n");
                }
            }
            intent.putExtra(Intent.EXTRA_TEXT,data);
            intent.setType("text/plain");
            context.startActivity(Intent.createChooser(intent, "Send To"));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public Map<String, String> getChatParams(String channelID, int pageNumber) {
        String key = CUtils.getApplicationSignatureHashCode(context);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.APP_KEY, key);
        params.put(CGlobalVariables.CHAT_CHANNEL_ID, channelID);
        if (channelID.startsWith("FCH")) {
            params.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
            params.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
        }else{
            params.put(CGlobalVariables.CHAT_PAGE_NO, ""+pageNumber);
        }
        params.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));

    //    Log.d("ChatHistory1", params.toString());
        return CUtils.setRequiredParams(params);
    }

    @Override
    public void onResponse(String response, int method) {
//       Log.d("ChatHistory1",response);
//        try {
//           // String sResponse = new String(response.getBytes("ISO-8859-1"), "UTF-8");
//            String data ="Astrologer - "+astrologer+"\n\n";
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            JSONObject jsonObject = new JSONObject(response);
//            String statuss = jsonObject.getString("status");
//            if(statuss.equalsIgnoreCase("1")) {
//
//                JSONArray msgJsonArray = jsonObject.getJSONArray("messages");
//                if (msgJsonArray != null && msgJsonArray.length() > 0) {
//
//                    for (int i = 0; i < msgJsonArray.length(); i++) {
//                        JSONObject msgObject = msgJsonArray.getJSONObject(i);
//
//                        String from = msgObject.getString("from");
//                        String body = msgObject.getString("body");
//                        String time = msgObject.getString("time");
//                        if(from.equalsIgnoreCase("User"))
//                            data += time +" - " +"Me: " +body +"\n";
//                        else
//                            data += time +" - "+ from +": " +body +"\n";
//                    }
//
//                }
//            }
//            intent.putExtra(Intent.EXTRA_TEXT,data);
//            intent.setType("text/plain");
//            context.startActivity(Intent.createChooser(intent, "Send To"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
    }

    @Override
    public void onError(VolleyError error) {
        Log.d("ChatHistory1",error.toString());
    }

    public void historyRecordsUpdate(ArrayList<ChatHistoryBean> historyBeans)
    {

        if (this.chatHistoryList == null) {
            this.chatHistoryList = new ArrayList();
        }else {
            this.chatHistoryList.clear();
        }
        this.chatHistoryList.addAll(historyBeans);

        notifyDataSetChanged();
    }

    private void processChatAgain(int pos, View v){
        try {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_CHAT_AGAIN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_HISTORY_CHAT_BTN_PARTNER_ID);

            try {
                ChatHistoryBean chatHistoryBean = chatHistoryList.get(pos);
                AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
                astrologerDetailBean.setAiAstrologerId(chatHistoryBean.getAiAstroId());
                if (CUtils.isAiAstrologer(astrologerDetailBean)) {
                    astrologerDetailBean.setName(chatHistoryBean.getAstrologerName());
                    astrologerDetailBean.setImageFile(chatHistoryBean.getAstrologerImageFile());
                    astrologerDetailBean.setImageFileLarge(chatHistoryBean.getAstroImageFileLarge());
                    astrologerDetailBean.setAstroWalletId(chatHistoryBean.getAstroWalletId());
                    astrologerDetailBean.setUrlText(chatHistoryBean.getUrlText());
                    astrologerDetailBean.setDesignation(chatHistoryBean.getAstroExpertise());
                    String className = CUtils.getActivityName(context);
                    if(TextUtils.isEmpty(className)){className = "";}
                    astrologerDetailBean.setCallSource(className);
                    astrologerDetailBean.setAiAstrologerId(chatHistoryBean.getAiAstroId());
                    astrologerDetailBean.setAstrologerId(chatHistoryBean.getAstroWalletId());
                    if(chatHistoryBean.isFreeForChat()) {
                        astrologerDetailBean.setFreeForChat(true);
                        astrologerDetailBean.setUseIntroOffer(true);
                    }
                    if (com.ojassoft.astrosage.varta.utils.CUtils.isChatNotInitiated() && !TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId())) {
                        ChatUtils.getInstance((Activity) context).initAIChat(astrologerDetailBean);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
                    }

                    return;
                }
            }catch (Exception e){
                //
            }
            openAstrologerDetail(pos, v);
        }catch (Exception e){
            //
        }
    }
}