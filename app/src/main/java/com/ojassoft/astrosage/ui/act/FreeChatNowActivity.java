package com.ojassoft.astrosage.ui.act;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

public class FreeChatNowActivity extends AppCompatActivity {
    TextView getFreeChat;
    ImageView imgViewBack;
    Button btnAiChatNow,btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_chat_now);

        imgViewBack = findViewById(R.id.imgViewBack);
        getFreeChat = findViewById(R.id.first_call_in_1rs_tv);
        btnAiChatNow = findViewById(R.id.btnAiChatNow);
        btnExit = findViewById(R.id.btnExit);
        String text = getString(R.string.get_your_free_chat).toLowerCase();  // Convert all to lowercase
        String formattedText = text.substring(0, 1).toUpperCase() + text.substring(1);  // Capitalize the first character

        getFreeChat.setText(formattedText);

        imgViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnExit.setOnClickListener(v -> {
            // Close and exit the app
            finishAffinity(); // Closes all activities
           // System.exit(0);   // Exits the app completely
        });
        btnAiChatNow.setOnClickListener(v -> {
            boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(this);
            String firstFreeChatType = com.ojassoft.astrosage.utils.CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE, "");
            String secondFreeChatType = com.ojassoft.astrosage.utils.CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE, "");
            String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(this);

            com.ojassoft.astrosage.utils.CUtils.createSession(this, com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_PARTNER_ID);
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.EXIT_SCREEN_CALL_BTN_CLICK_AI, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            String datee = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.WEEK_DAYS);
            com.ojassoft.astrosage.utils.CUtils.saveConsultPremimumDateData(this, true, datee);


                if (CUtils.isSecondFreeChat(this) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    if (secondFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
                      //  Toast.makeText(this,"ai chat 1",Toast.LENGTH_SHORT).show();
                        CUtils.initiateRandomAiChat(this, CGlobalVariables.PREMIUM_AI_FREE_CALL_CHAT_DIALOG,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                        finish();
                    }else {
                       // Toast.makeText(this," chat 1",Toast.LENGTH_SHORT).show();

                        CUtils.initiateRandomChat(this, CGlobalVariables.FREE_CHAT_PREMIUM_ASTROLOGER,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                        finish();
                    }
                } else {
                    if(firstFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)){
                       // Toast.makeText(this,"ai chat 2",Toast.LENGTH_SHORT).show();

                        CUtils.initiateRandomAiChat(this, CGlobalVariables.PREMIUM_AI_FREE_CALL_CHAT_DIALOG,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                        finish();
                    } else {
                       // Toast.makeText(this," chat 2",Toast.LENGTH_SHORT).show();

                        CUtils.initiateRandomChat(this, CGlobalVariables.FREE_CHAT_PREMIUM_ASTROLOGER, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                        finish();
                    }
                }



        });
    }

    private String getFirstCapitalCharacter(String str){
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }
}