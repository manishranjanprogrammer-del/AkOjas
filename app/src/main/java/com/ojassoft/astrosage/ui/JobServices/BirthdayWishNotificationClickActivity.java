package com.ojassoft.astrosage.ui.JobServices;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Transparent trampoline activity that records birthday notification clicks before forwarding to HomeInputScreen.
 */
public class BirthdayWishNotificationClickActivity extends AppCompatActivity {

    public static final String EXTRA_NOTIFICATION_ANALYTICS_LABEL = "extra_notification_analytics_label";

    /**
     * Handles the notification click payload, records analytics, and opens the intended destination screen.
     *
     * @param savedInstanceState previously saved instance state, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleNotificationIntent(getIntent());
        finish();
    }

    /**
     * Reprocesses a new notification tap intent when the trampoline activity is reused by the system.
     *
     * @param intent latest notification click intent routed to this activity
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleNotificationIntent(intent);
        finish();
    }

    /**
     * Logs click analytics and opens HomeInputScreen with the original notification extras.
     *
     * @param intent notification click payload containing analytics and navigation extras
     */
    private void handleNotificationIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        String notificationLabel = intent.getStringExtra(EXTRA_NOTIFICATION_ANALYTICS_LABEL);
        if (notificationLabel != null && !notificationLabel.trim().isEmpty()) {
            CUtils.fcmAnalyticsEvents(notificationLabel, CGlobalVariables.FIREBASE_NOTIFICATION_CLICKED_EVENT, "");
        }

        Intent launchIntent = new Intent(this, HomeInputScreen.class);
        if (intent.getExtras() != null) {
            launchIntent.putExtras(new Bundle(intent.getExtras()));
            launchIntent.removeExtra(EXTRA_NOTIFICATION_ANALYTICS_LABEL);
        }
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(launchIntent);
    }
}
