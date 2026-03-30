package com.ojassoft.astrosage.fcmservice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * NotificationDeduplicationHelper centralizes key generation and storage checks for push deduplication.
 */
final class NotificationDeduplicationHelper {

    private static final long EXPIRY_DURATION = 2L * 60 * 1000;
    private static final String DISPLAYED_NOTIFICATION_PREFS = "recent_shown_notifications";

    private NotificationDeduplicationHelper() {
    }

    /**
     * Tracks title and message pairs that were already displayed recently to avoid duplicate tray entries.
     */
    static boolean shouldSkipRecentlyShownNotification(Context context, String title, String message) {
        if (context == null) {
            return false;
        }
        String dedupKey = buildTitleMessageKey(title, message);
        if (dedupKey == null) {
            return false;
        }
        try {
            SharedPreferences prefs = context.getSharedPreferences(DISPLAYED_NOTIFICATION_PREFS, Context.MODE_PRIVATE);
            long now = System.currentTimeMillis();
            Map<String, ?> all = prefs.getAll();
            SharedPreferences.Editor editor = prefs.edit();
            for (Map.Entry<String, ?> entry : all.entrySet()) {
                if (!(entry.getValue() instanceof Long)) {
                    editor.remove(entry.getKey());
                    continue;
                }
                long savedTime = (long) entry.getValue();
                if (now - savedTime > EXPIRY_DURATION) {
                    editor.remove(entry.getKey());
                }
            }
            editor.apply();

            if (prefs.contains(dedupKey)) {
                return true;
            }

            prefs.edit().putLong(dedupKey, now).apply();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Converts null strings to empty values so content-hash generation stays deterministic.
     */
    static String emptyIfNull(String value) {
        return value == null ? "" : value;
    }

    /**
     * Creates the shared key format used for title and message based duplicate checks.
     */
    static String buildTitleMessageKey(String title, String message) {
        String normalizedTitle = emptyIfNull(title);
        String normalizedMessage = emptyIfNull(message);
        if (isBlank(normalizedTitle) && isBlank(normalizedMessage)) {
            return null;
        }
        return "titlemsg:" + String.valueOf((normalizedTitle + "|" + normalizedMessage).hashCode());
    }

    /**
     * Checks blank strings without depending on Android framework text helpers in unit tests.
     */
    static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
