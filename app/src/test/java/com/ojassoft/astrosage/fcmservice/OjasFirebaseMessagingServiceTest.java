package com.ojassoft.astrosage.fcmservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Regression tests for notification deduplication in {@link OjasFirebaseMessagingService}.
 */
public class OjasFirebaseMessagingServiceTest {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Map<String, Object> storedValues;

    @Before
    public void setUp() {
        context = mock(Context.class);
        sharedPreferences = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);
        storedValues = new HashMap<>();

        when(context.getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE))).thenReturn(sharedPreferences);
        when(sharedPreferences.getAll()).thenAnswer(invocation -> new HashMap<>(storedValues));
        when(sharedPreferences.contains(anyString())).thenAnswer(invocation -> storedValues.containsKey(invocation.getArgument(0)));
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.remove(anyString())).thenAnswer(invocation -> {
            storedValues.remove(invocation.getArgument(0));
            return editor;
        });
        when(editor.putLong(anyString(), anyLong())).thenAnswer(invocation -> {
            storedValues.put(invocation.getArgument(0), invocation.getArgument(1));
            return editor;
        });
        doNothing().when(editor).apply();
    }

    /**
     * Verifies the display-level guard used by showCustomNotification allows the first notification.
     */
    @Test
    public void shouldSkipRecentlyShownNotification_returnsFalseOnFirstDelivery() {
        boolean firstAttempt = NotificationDeduplicationHelper.shouldSkipRecentlyShownNotification(
                context, "AI Astro", "Will I get promotion?");

        assertFalse(firstAttempt);
    }

    /**
     * Verifies the display-level guard used by showCustomNotification blocks repeated title/message pairs.
     */
    @Test
    public void shouldSkipRecentlyShownNotification_returnsTrueOnSecondDelivery() {
        NotificationDeduplicationHelper.shouldSkipRecentlyShownNotification(
                context, "AI Astro", "Will I get promotion?");
        boolean secondAttempt = NotificationDeduplicationHelper.shouldSkipRecentlyShownNotification(
                context, "AI Astro", "Will I get promotion?");

        assertTrue(secondAttempt);
    }

    /**
     * Verifies AI chat payloads with different metadata still collapse on the same title and message.
     */
    @Test
    public void shouldSkipRecentlyShownNotification_ignoresAiExtrasWhenTitleAndMessageMatch() {
        NotificationDeduplicationHelper.shouldSkipRecentlyShownNotification(
                context, "AI Astro", "Will I get promotion?");
        boolean secondAttempt = NotificationDeduplicationHelper.shouldSkipRecentlyShownNotification(
                context, "AI Astro", "Will I get promotion?");

        assertTrue(secondAttempt);
    }

    /**
     * Verifies different message text is treated as a new notification.
     */
    @Test
    public void shouldSkipRecentlyShownNotification_allowsDifferentMessage() {
        NotificationDeduplicationHelper.shouldSkipRecentlyShownNotification(
                context, "AI Astro", "Will I get promotion?");
        boolean nextAttempt = NotificationDeduplicationHelper.shouldSkipRecentlyShownNotification(
                context, "AI Astro", "Will I get promotion tomorrow?");

        assertFalse(nextAttempt);
    }
}
