package com.ojassoft.astrosage.varta.dialog;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.receiver.OngoingCallChatManager;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

public class JoinChatDialog extends DialogFragment {

    public JoinChatDialog() {
        //
    }

    private String CHANNEL_ID;
    private String chatJsonObject;
    private String astrologerName;
    private String astrologerProfileUrl;
    private String astrologerId;
    private String userChatTime;
    private String chatinitiatetype;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.layout_join_chat_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView iv_close_view = view.findViewById(R.id.iv_close_view);
        Button btn_join_chat = view.findViewById(R.id.btn_join_chat);
        Button btnCancelChat = view.findViewById(R.id.btnCancelChat);
        iv_close_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ongoingChatObserver != null) {
                    OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
                }
                dismiss();
            }
        });
        btnCancelChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ongoingChatObserver != null) {
                    OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
                }
                dismiss();
            }
        });
        btn_join_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (TextUtils.isEmpty(CHANNEL_ID) && TextUtils.isEmpty(chatJsonObject)) {
                        return;
                    }
                    AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_RUNNING;
                    // Log.d("TestChatIssue","Data is ==>>"+CHANNEL_ID +"  ============="+chatJsonObject+" -----------------"+astrologerName+"----------"+astrologerProfileUrl+"------------"+astrologerId+"------------"+userChatTime);
                    Intent intent1;
                    if (chatinitiatetype.equals(CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER)) {
                        intent1 = new Intent(getActivity(), ChatWindowActivity.class);
                    } else {
                        intent1 = new Intent(getActivity(), AIChatWindowActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHANNEL_ID);
                    bundle.putBoolean("isFromJoinButton", true);
                    bundle.putString("connect_chat_bean", chatJsonObject);
                    bundle.putString("astrologer_name", astrologerName);
                    bundle.putString("astrologer_profile_url", astrologerProfileUrl);
                    bundle.putString("astrologer_id", astrologerId);
                    bundle.putString("userChatTime", userChatTime);
                    intent1.putExtras(bundle);
                    getActivity().startActivity(intent1);
                } catch (Exception e) {
                    //
                }
            }
        });
        showOrHideOngoingChat();
        return view;
    }

    private final Observer<Intent> ongoingChatObserver = new Observer<Intent>() {
        @Override
        public void onChanged(Intent intent) {
            String remTime = intent.getStringExtra("rem_time");
            CHANNEL_ID = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
            chatJsonObject = intent.getStringExtra("connect_chat_bean");
            astrologerName = intent.getStringExtra("astrologer_name");
            astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
            astrologerId = intent.getStringExtra("astrologer_id");
            userChatTime = intent.getStringExtra("userChatTime");
            chatinitiatetype = intent.getStringExtra(CGlobalVariables.CHATINITIATETYPE);
            if (remTime.equals("00:00:00")) {
                dismiss();
            }
        }
    };

    public void showOrHideOngoingChat() {
        if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
            OngoingCallChatManager.getOngoingChatLiveData().observe(this, ongoingChatObserver);
        } else {
            if (ongoingChatObserver != null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
            }
        }

    }

    @Override
    public void onDestroy() {
        if (ongoingChatObserver != null) {
            OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
        }
        super.onDestroy();

    }
}
