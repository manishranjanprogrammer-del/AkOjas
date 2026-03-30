package com.ojassoft.astrosage.varta.ui.fragments;

import static com.ojassoft.astrosage.ui.act.OutputMasterActivity.CURRENT_SCREEN_ID_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BASIC_LAGNA_SCREEN_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.KundliChatHistoryBean;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.KundliChatHistoryAdapter;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.utils.CDatabaseHelper;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KundliAIChatHistoryFragment extends Fragment implements KundliChatHistoryAdapter.OnChatItemClick {

    RecyclerView recyclerView;
    LinearLayout linearLayout;
    FrameLayout frCallHistory;
    TextView linkTextView,txtViewNoDataFound;
    KundliChatHistoryAdapter adapter;
    CustomProgressDialog pdFrag;
    private LinearLayoutManager mLayoutManager;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private Parcelable recyclerViewState;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kundli_a_i_chat_history, container, false);
        linearLayout = view.findViewById(R.id.no_item_available);
        frCallHistory = view.findViewById(R.id.frCallHistory);

        linkTextView = view.findViewById(R.id.link_tv);
        txtViewNoDataFound = view.findViewById(R.id.txtViewNoDataFound);
        recyclerView = view.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new KundliChatHistoryAdapter(requireContext(),this);
        recyclerView.setAdapter(adapter);
        loadData();
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        txtViewNoDataFound.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium));
        linkTextView.setTypeface(CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular));
        setScrollListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //LocalBroadcastManager.getInstance(requireContext()).registerReceiver(dataLoadedReceiver,new IntentFilter(CGlobalVariables.ACTION_DATA_LOADED));
        if(recyclerViewState != null){
            refreshListData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerViewState = mLayoutManager.onSaveInstanceState();
    }

    public void showErrorMsg(boolean isDataAvaiable) {
        if (linearLayout != null && recyclerView != null) {
            if (isDataAvaiable) {
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    public void loadData(){
        try (CDatabaseHelper db = new CDatabaseHelper(requireContext())){
            ArrayList<KundliChatHistoryBean> listData = db.getKundaliChatHistory(CUtils.getCountryCode(requireContext())+CUtils.getUserID(requireContext()),0,10);
            //Log.d("ChatHistory", "list Data : " + listData);
            if(adapter != null && listData != null && !listData.isEmpty()){
                adapter.setHistoryList(listData);
            }else{
                showErrorMsg(false);
            }

        }
    }

    @Override
    public void onClick(KundliChatHistoryBean item) {
        LoadChatMessages(item.getConversationId());
    }
    private final BroadcastReceiver dataLoadedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
        }
    };

    public void LoadChatMessages(String conversationId){
        Log.e("chatBackup","Conversation id = " + conversationId);
        try(CDatabaseHelper db = new CDatabaseHelper(requireContext())){
            ArrayList<ChatMessage> listMessage = db.getConversation(conversationId,0,10);
            if(listMessage != null && !listMessage.isEmpty()){
                Collections.sort(listMessage, (m1, m2) -> m1.getDateCreated().compareTo(m2.getDateCreated()));
                AstrosageKundliApplication.kundliChatMessages = listMessage;
                AstrosageKundliApplication.KUNDLI_CHAT_CONVERSATION_ID = conversationId;
                startHistoryActivity(conversationId);
            }
        }
    }
    boolean loading = false;
    private void setScrollListener(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = true;
                            try (CDatabaseHelper db = new CDatabaseHelper(requireContext())) {
                                ArrayList<KundliChatHistoryBean> listData = db.getKundaliChatHistory(CUtils.getCountryCode(requireContext()) + CUtils.getUserID(requireContext()), adapter.getItemCount(), 10);
                                if(listData != null)
                                    adapter.addHistoryData(listData);
                                loading = false;
                            }catch (Exception ignore){}
                        }
                    }
                }
            }
        });
    }

    public void getConversationDetailsFormServer(String  conversationId,int pageNo) {

        Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getUserChatHistory(getUserChatHistoryParams(conversationId,pageNo));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        Log.e("chatBackup", "response : "+data);
                        if(!data.isEmpty()){
                            parseConversationHistory(data, conversationId);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Map<String, String> getUserChatHistoryParams(String conversationId,int pageNo) {
        Map<String, String> params = new HashMap<>();
        params.put("packagename", com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME);
        params.put("methodname", "getconversationhistory");
        params.put("userid", com.ojassoft.astrosage.utils.CUtils.getUserName(requireContext()));
        params.put("conversationid", conversationId);
        params.put("pageno", String.valueOf(pageNo));
        params.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(requireContext()));
        params.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        params.put(CGlobalVariables.KEY_API,CUtils.getApplicationSignatureHashCode(requireContext()));
        Log.e("chatBackup", "params : "+params);
        return params;
    }

    public void parseConversationHistory(String data,String conversationId){
        try (CDatabaseHelper db = new CDatabaseHelper(requireContext())){
            JSONArray jsonArray = new JSONArray(data);
            ArrayList<ChatMessage> KundliMessages = new ArrayList<>();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    conversationId = jsonObject.getString("CONVERSATIONID");
                    String question = jsonObject.getString("QUESTION");
                    String answer = jsonObject.getString("ANSWER");
                    String time = jsonObject.getString("TIME");
                    String answerId = String.valueOf(jsonObject.getInt("ANSWERID"));
                    int isliked = jsonObject.optInt("ISLIKED");
                    int isUnliked = jsonObject.optInt("ISUNLIKED");
                    long rowId = 0;
                    if(!question.isEmpty()){
                        Message userQuestion = createMessage("",question, time, "USER");
                        KundliMessages.add(new UserMessage(userQuestion));
                    }
                    if(!answer.isEmpty()){
                        Message astroAnswer = createMessage(answerId,answer, time, "Astrologer");
                        astroAnswer.setUnlike(isUnliked);
                        astroAnswer.setLike(isliked);
                        KundliMessages.add(new UserMessage(astroAnswer));
                    }
                }
                Collections.sort(KundliMessages, (m1, m2) -> m1.getDateCreated().compareTo(m2.getDateCreated()));
                AstrosageKundliApplication.kundliChatMessages = KundliMessages;
                startHistoryActivity(conversationId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Message createMessage(String chatId,String text, String timeStamp,String author) {
        Message message = new Message();
        try {
            message.setAuthor(author);
            message.setDateCreated(formatDateAndTime(timeStamp));
            message.setMessageBody(text);
            message.setSeen(false);
            message.setChatId(chatId.isEmpty() ? (new Random().nextInt(999) + 1) : Integer.parseInt(chatId));
        } catch (Exception e) {
            //
        }
        return message;
    }
    public void startHistoryActivity(String conversationId){
        Intent intent = new Intent(requireActivity(), MiniChatWindow.class);
        intent.putExtra(CGlobalVariables.KEY_CONVERSATION_ID,conversationId);
        intent.putExtra(CURRENT_SCREEN_ID_KEY, BASIC_LAGNA_SCREEN_ID);
        intent.putExtra(CGlobalVariables.KEY_FROM_HISTORY,true);
        intent.putExtra(CGlobalVariables.SOURCE_OF_SCREEN,CGlobalVariables.SOURCE_FROM_HISTORY);//If payment done, source flow remains from History for this case
        requireActivity().startActivity(intent);
    }

    private String formatDateAndTime(String timeStamp) throws Exception {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date formattedDate = inputFormat.parse(timeStamp);
        return   new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(formattedDate);
    }

    private void refreshListData(){
        try (CDatabaseHelper db = new CDatabaseHelper(requireContext())){

            ArrayList<KundliChatHistoryBean> listData = db.getKundaliChatHistory(CUtils.getCountryCode(requireContext())+CUtils.getUserID(requireContext()),0,adapter.getItemCount());
            //Log.d("ChatHistory", "list Data : " + listData);
            if(adapter != null && listData != null && !listData.isEmpty()){
                adapter.setHistoryList(listData);
            }else{
                showErrorMsg(false);
            }
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        }
    }

}