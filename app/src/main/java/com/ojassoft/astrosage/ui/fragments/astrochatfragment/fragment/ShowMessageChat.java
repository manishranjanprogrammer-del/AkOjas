package com.ojassoft.astrosage.ui.fragments.astrochatfragment.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.ui.act.ActAstroPaymentOptions;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.adapter.RecyclerViewAdapterShowMessage;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.networkdataload.NetWorkSendQuestion;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ojas-10 on 9/3/16.
 */
public class ShowMessageChat extends Fragment {
    RecyclerView astrologerRecyclerView;
    ArrayList<MessageDecode> chatMessageArrayList;
    //Toolbar toolBar;
    ImageView sendButton;
    int i = 0;
    EditText questionAskAstrologer;
    RecyclerViewAdapterShowMessage myRecyclerAdapter;
    public static final int REQUEST_CODE_SELECT_PROFILE = 101;

    public ShowMessageChat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ourAstrologerView = inflater.inflate(R.layout.fragment_show_msg, container, false);
        initXmlLayout(ourAstrologerView);
        ((ActAstroPaymentOptions) getActivity()).setTitle(getResources().getString(R.string.show_chat_message_fragment));
        return ourAstrologerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setOurAstrologerRecyclerView();
        getMessageList();
        setClickListener();
        if (myRecyclerAdapter != null) {
            Log.w("TotalMessageCount", "" + myRecyclerAdapter.getItemCount());
            astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(CGlobalVariables.COPA_RESULT));
    }


    // save message each time when screen change even when partially.
    @Override
    public void onPause() {
        super.onPause();
        saveDataOnInternalStorage();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    //testing purpose
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            MessageDecode message = intent.getParcelableExtra(CGlobalVariables.COPA_MESSAGE);
            message.setNotPaidLayoutShow("False");
           // Toast.makeText(getActivity(), "call GCM Result" + message.getMessageText(), Toast.LENGTH_SHORT).show();
            Log.d("receiver", "Got message: " + message.getMessageText() + message.getMessageText());
            addToList(message);
        }
    };

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void initXmlLayout(View ourAstrologerView) {
        astrologerRecyclerView = (RecyclerView) ourAstrologerView.findViewById(R.id.astrologer_list);
        sendButton = (ImageView) ourAstrologerView.findViewById(R.id.sendbutton);
        questionAskAstrologer = (EditText) ourAstrologerView.findViewById(R.id.userQuestion);
    }

    public void setOurAstrologerRecyclerView() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setClickListener() {
        astrologerRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (myRecyclerAdapter != null) {
                    astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!questionAskAstrologer.getEditableText().toString().trim().isEmpty()) {
                    //  Check either user birth detail filled or not.
                    if (CUtils.getBooleanData(getActivity(), CGlobalVariables.IS_USER_PROFILE_FILLED, false)) {
                        MessageDecode messageDecode= setMessageDataAskByUser();
                        new NetWorkSendQuestion(getActivity(), messageDecode).sendDataOnServerAskQuestion();
                    } else {
                        Toast.makeText(getActivity(), "Fill your Birth Details First.", Toast.LENGTH_SHORT).show();
                        //((ActAstroPaymentOptions) getActivity()).replaceWithNewFragment(new NewProfile(), null);
                        callToGetDetailsFromHomeInputModule(isLocalKundliAvailable());
                    }
                } else {
                    MyCustomToast mct2 = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), ((BaseInputActivity)getActivity()).regularTypeface);
                    mct2.show(getResources().getString(R.string.questioncannotempty));
                }
            }
        });
    }

    private MessageDecode setMessageDataAskByUser() {
        MessageDecode messageDecode = new MessageDecode();
        messageDecode.setUserType("user");
        messageDecode.setNotificationShow("False");
        String currentDateTime = CUtils.getCurrentDateTime();
        messageDecode.setDateTimeShow(currentDateTime);
        messageDecode.setMessageText(questionAskAstrologer.getEditableText().toString());
        messageDecode.setColorOfMessage("Yellow");
        messageDecode.setRateShow("False");
        messageDecode.setShareLinkShow("False");
        messageDecode.setNoOfQuestion(CUtils.getIntData(getActivity(), CGlobalVariables.NOOFQUESTIONAVAILABLE, 0));
        int noOfQuestion = CUtils.getIntData(getActivity(), CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
        if (noOfQuestion > 0)
            messageDecode.setNotPaidLayoutShow("False");
        else
            messageDecode.setNotPaidLayoutShow("True");
        addToList(messageDecode);
        questionAskAstrologer.setText(" ");
        questionAskAstrologer.setHint(getResources().getString(R.string.type_text));
        return messageDecode;
    }

    private int isLocalKundliAvailable() {
        int screenId = 1;
        try {
            Map<String, String> mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                    getActivity().getApplicationContext(), "",
                    CGlobalVariables.BOTH_GENDER, -1);
            if (mapHoroID != null) {
                screenId = 0;
            } else {
                screenId = 1;
            }

        } catch (UIDataOperationException e) {
            e.printStackTrace();
        }
        return screenId;
    }


    // Add new message on the list and update adapter each time.
    private void addToList(MessageDecode messageDecode) {
        chatMessageArrayList.add(messageDecode);
        if (myRecyclerAdapter == null) {
            myRecyclerAdapter = new RecyclerViewAdapterShowMessage(chatMessageArrayList, getActivity(),false);
            astrologerRecyclerView.setAdapter(myRecyclerAdapter);
        }
        myRecyclerAdapter.notifyDataSetChanged();
    }

    // Get previous message from storage.
    // called once when app start/resume.
    private void getMessageList() {
        chatMessageArrayList = CUtils.getDataFromPrefrence(((ActAstroPaymentOptions) getActivity()));
        if (chatMessageArrayList != null) {
            myRecyclerAdapter = new RecyclerViewAdapterShowMessage(chatMessageArrayList, getActivity(),false);
            astrologerRecyclerView.setAdapter(myRecyclerAdapter);
        } else {
            astrologerRecyclerView.setAdapter(null);
            chatMessageArrayList = new ArrayList<>();
        }
        astrologerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //  Save/Update complete message list on storage.
    //  Call each time when screen changed.
    public void saveDataOnInternalStorage() {
        Intent intent = new Intent((ActAstroPaymentOptions) getActivity(), SaveDataInternalStorage.class);
        intent.putParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER, chatMessageArrayList);
        intent.putExtra(CGlobalVariables.ISINSERT, false);
        ((ActAstroPaymentOptions) getActivity()).startService(intent);
      //  Toast.makeText((ActAstroPaymentOptions) getActivity(), "  saving message (total: " + chatMessageArrayList.size() + ")  ", Toast.LENGTH_SHORT).show();
    }

    private void callToGetDetailsFromHomeInputModule(int screenId) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getActivity(), HomeInputScreen.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
        intent.putExtra(CGlobalVariables.ASK_QUESTION_QUERY_DATA, true);
        intent.putExtra("PAGER_INDEX", screenId);
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent, REQUEST_CODE_SELECT_PROFILE);
    }


}
