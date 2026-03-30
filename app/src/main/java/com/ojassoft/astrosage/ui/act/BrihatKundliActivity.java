package com.ojassoft.astrosage.ui.act;

import static android.view.View.VISIBLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BRIHAT_FROM_KUNDLI_DOWNLOADED_NOTIFICATION_CLICKED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;

import android.os.Bundle;
import android.view.View;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

/**
 * Activity to display the Brihat Kundli (Big Horoscope) screen.
 * Handles loading of horoscope data, caching, and WebView integration.
 * Manages UI components such as the progress bar and placeholder.
 */
public class BrihatKundliActivity extends BaseInputActivity {
    private static final String TAG = "BrihatKundliActivity"; // Tag for logging
    private CustomProgressDialog pd;
    String deepLinkServiceUrl = CGlobalVariables.BRIHAT_SERVICE_DEEPLINK_URL;

    /**
     * Default constructor for BrihatKundliActivity.
     * Sets the app name resource for the base activity.
     */
    public BrihatKundliActivity() {
        super(R.string.app_name);
    }

    /**
     * Called when the activity is created. Sets up the UI, analytics, and triggers data loading.
     * Configures the WebView for displaying horoscope content.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brihat_kundli);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.NEW_BRIHAT_SCREEN_OPEN_EVENT, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "brihat_kundli_new");

        if (getIntent() != null) {
            if (getIntent().hasExtra("partnerId")) {
                String partnerId = getIntent().getStringExtra("partnerId");
                CUtils.createSession(this, partnerId);
                CUtils.fcmAnalyticsEvents(BRIHAT_FROM_KUNDLI_DOWNLOADED_NOTIFICATION_CLICKED, FIREBASE_EVENT_ITEM_CLICK, "");
            }
        }

        View placeholder = findViewById(R.id.placeholder);
        placeholder.setVisibility(VISIBLE);
        placeholder.setAlpha(1f);

        CUtils.getUrlLink(deepLinkServiceUrl, this, LANGUAGE_CODE, 0);
        finish();
    }
}