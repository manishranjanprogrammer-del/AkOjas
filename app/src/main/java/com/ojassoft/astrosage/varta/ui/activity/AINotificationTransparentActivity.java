package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.PERSONALIZED_AI_NOTIFICATION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_NOTIFICATION_TITLE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REVERT_QUESTION_COUNT;
import static com.ojassoft.astrosage.varta.utils.CUtils.chatWindowOpenType;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

/**
 * AINotificationTransparentActivity handles the transparent activity that processes AI notifications
 * and routes them to appropriate chat windows or modules.
 * 
 * This activity serves as an intermediary that:
 * 1. Processes incoming notification intents
 * 2. Determines the appropriate destination (AI chat, human chat, or app module)
 * 3. Routes the user to the correct activity with proper parameters
 * 4. Handles analytics tracking for notification interactions
 */
public class AINotificationTransparentActivity extends AppCompatActivity {

    /**
     * Initializes the activity and processes the incoming notification intent.
     * This method:
     * 1. Sets up the edge-to-edge display
     * 2. Processes the incoming intent
     * 3. Determines the appropriate destination activity
     * 4. Routes the user with proper parameters
     * 5. Tracks analytics events
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_ainotification_transparent);
            AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n AINotificationTransparentActivity oncreate : ";

            Context context = this;
            if (getIntent() != null) {
                boolean isAIWindowOpen = false;
                Intent intent = getIntent();
                Intent resultIntent;

                // Determine the appropriate destination based on chat window type
                if (chatWindowOpenType.equals(CGlobalVariables.OPEN_WINDOW_TYPE_AI) && !AIChatWindowActivity.isChatCompleted) {
                    // Handle AI chat window routing
                    isAIWindowOpen = intent.getBooleanExtra(KEY_AI_ASTROLOGER_ONLINE, false);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_USER_IN_AI_CHAT,
                            PERSONALIZED_AI_NOTIFICATION, "");
                    chatWindowOpenType = "";

                    // Set up intent for AI chat window
                    resultIntent = new Intent(context, AIChatWindowActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    resultIntent.setAction(Intent.ACTION_VIEW);
                    resultIntent.putExtra("newQuestion", intent.getStringExtra(KEY_AI_QUESTION));
                } else if (chatWindowOpenType.equals(CGlobalVariables.OPEN_WINDOW_TYPE_HUMAN) && !ChatWindowActivity.isChatCompleted) {
                    // Handle human chat window routing
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_NOTIFICATION_USER_IN_HUMAN_CHAT,
                            PERSONALIZED_AI_NOTIFICATION, "");
                    chatWindowOpenType = "";

                    // Set up intent for human chat window
                    resultIntent = new Intent(context, ChatWindowActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    resultIntent.setAction(Intent.ACTION_VIEW);
                    resultIntent.putExtra(KEY_AI_QUESTION, intent.getStringExtra(KEY_AI_QUESTION));
                    resultIntent.putExtra(KEY_REVERT_QUESTION_COUNT,
                            intent.getStringExtra(KEY_REVERT_QUESTION_COUNT));
                    resultIntent.putExtra(KEY_AI_ASTROLOGER_ID,
                            intent.getStringExtra(KEY_AI_ASTROLOGER_ID));
                } else {
                    AstrosageKundliApplication.aINotificationIssueLogs = AstrosageKundliApplication.aINotificationIssueLogs +"\n AINotificationTransparentActivity aiastroid : "+intent.getStringExtra(KEY_AI_ASTROLOGER_ID);

                    String link = intent.getStringExtra("link");
                    // Handle app module routing
                    resultIntent = new Intent(context, ActAppModule.class);
                    resultIntent.setAction(Intent.ACTION_VIEW);
                    if (link != null) {
                        resultIntent.setData(Uri.parse(link));
                    }
                    resultIntent.putExtra(KEY_AI_QUESTION, intent.getStringExtra(KEY_AI_QUESTION));
                    resultIntent.putExtra(KEY_REVERT_QUESTION_COUNT,
                            intent.getStringExtra(KEY_REVERT_QUESTION_COUNT));
                    resultIntent.putExtra(KEY_AI_ASTROLOGER_ID,
                            intent.getStringExtra(KEY_AI_ASTROLOGER_ID));
                }

                // Add common extras to all intents
                resultIntent.putExtra(KEY_AI_NOTIFICATION_TITLE,
                        intent.getStringExtra(KEY_AI_NOTIFICATION_TITLE));
                resultIntent.putExtra(KEY_AI_ASTROLOGER_ONLINE,
                        intent.getBooleanExtra(KEY_AI_ASTROLOGER_ONLINE, false));
//                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // Log notification handling
                Log.e("notificationTest", "onCreate: AI NOTIFICATION CALLED AND OPENED AI-CHATWINDOW");

                // Start the appropriate activity if AI window is not already open
                if (!isAIWindowOpen) {
                    startActivity(resultIntent);
                }
                finish();
            }
        } catch (Exception e) {
            //
        }
    }
}