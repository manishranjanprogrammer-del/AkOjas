package com.ojassoft.astrosage.varta.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.app.PendingIntent;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.TestApplication;
import com.ojassoft.astrosage.varta.aichat.GetAnswerFromServer;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNotification;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.shadows.ShadowPendingIntent;

import java.lang.reflect.Method;

/**
 * Robolectric tests for the local AI response-ready notification shown when the chat screen is not in foreground.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, application = TestApplication.class)
public class CreateCustomLocalNotificationTest {

    /**
     * Verifies a notification is posted under the stable id with the expected title and preview body.
     */
    @Test
    public void showAiResponseReadyNotification_postsNotificationWithExpectedTitleAndPreview() {
        Context context = ApplicationProvider.getApplicationContext();

        String title = "AstroSage";
        String preview = "Your AI response is ready";
        String expanded = "Your AI response is ready (expanded)";

        new CreateCustomLocalNotification(context).showAiResponseReadyNotification(title, preview, expanded);

        Notification notification = getPostedNotification(context);
        ShadowNotification shadowNotification = Shadows.shadowOf(notification);
        assertEquals(title, shadowNotification.getContentTitle());
        assertEquals(preview, String.valueOf(shadowNotification.getContentText()));
    }

    /**
     * Verifies blank input values fall back to safe defaults instead of producing empty notification content.
     */
    @Test
    public void showAiResponseReadyNotification_defaultsWhenInputsAreEmpty() {
        Context context = ApplicationProvider.getApplicationContext();

        new CreateCustomLocalNotification(context).showAiResponseReadyNotification(null, "   ", null);

        Notification notification = getPostedNotification(context);
        ShadowNotification shadowNotification = Shadows.shadowOf(notification);
        assertEquals(context.getString(R.string.app_name), shadowNotification.getContentTitle());
        assertTrue(String.valueOf(shadowNotification.getContentText()).contains("Your response is ready"));
    }

    /**
     * Verifies the production HTML-to-preview/expanded flow renders the expected text in the standard notification payload.
     */
    @Test
    public void showAiResponseReadyNotification_rendersHtmlAnswerInCollapsedAndExpandedPayload() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        String title = context.getString(R.string.app_name);
        String finalHtml = "Career update<br>Second line\\nThird line";

        String preview = invokeNotificationTextHelper("buildAnswerPreview", finalHtml);
        String expanded = invokeNotificationTextHelper("buildAnswerExpandedText", finalHtml);

        new CreateCustomLocalNotification(context).showAiResponseReadyNotification(title, preview, expanded);

        Notification notification = getPostedNotification(context);
        ShadowNotification shadowNotification = Shadows.shadowOf(notification);
        assertEquals("Career update Second line Third line", String.valueOf(shadowNotification.getContentText()));
        assertEquals(expanded, String.valueOf(notification.extras.getCharSequence(Notification.EXTRA_BIG_TEXT)));
        assertEquals(title, String.valueOf(notification.extras.getCharSequence(Notification.EXTRA_TITLE)));
    }

    /**
     * Verifies calling show multiple times does not "stack" notifications and updates the same stable id instead.
     */
    @Test
    public void showAiResponseReadyNotification_usesStableIdAndUpdatesSameNotification() {
        Context context = ApplicationProvider.getApplicationContext();

        new CreateCustomLocalNotification(context).showAiResponseReadyNotification("AstroSage", "Preview 1", "Expanded 1");
        Notification first = getPostedNotification(context);
        ShadowNotification firstShadow = Shadows.shadowOf(first);
        assertEquals("Preview 1", String.valueOf(firstShadow.getContentText()));

        new CreateCustomLocalNotification(context).showAiResponseReadyNotification("AstroSage", "Preview 2", "Expanded 2");
        Notification updated = getPostedNotification(context);
        ShadowNotification updatedShadow = Shadows.shadowOf(updated);
        assertEquals("Preview 2", String.valueOf(updatedShadow.getContentText()));
    }

    /**
     * Verifies cancel removes the notification under the stable response-ready id.
     */
    @Test
    public void cancelAiResponseReadyNotification_removesPostedNotification() {
        Context context = ApplicationProvider.getApplicationContext();
        CreateCustomLocalNotification notifier = new CreateCustomLocalNotification(context);

        notifier.showAiResponseReadyNotification("AstroSage", "Preview", "Expanded");
        assertNotNull(getPostedNotification(context));

        notifier.cancelAiResponseReadyNotification();
        assertNull(getPostedNotificationOrNull(context));
    }

    /**
     * Verifies the notification is configured as a message-type, public notification and auto-cancels on tap.
     */
    @Test
    public void showAiResponseReadyNotification_setsExpectedVisibilityCategoryAndFlags() {
        Context context = ApplicationProvider.getApplicationContext();

        new CreateCustomLocalNotification(context).showAiResponseReadyNotification("AstroSage", "Preview", "Expanded");

        Notification notification = getPostedNotification(context);
        assertEquals(Notification.VISIBILITY_PUBLIC, notification.visibility);
        assertEquals(Notification.CATEGORY_MESSAGE, notification.category);
        assertTrue((notification.flags & Notification.FLAG_AUTO_CANCEL) != 0);
        assertTrue((notification.flags & Notification.FLAG_ONLY_ALERT_ONCE) != 0);
    }

    /**
     * Verifies tapping the notification opens {@link DashBoardActivity} and does not clear the existing task.
     */
    @Test
    public void showAiResponseReadyNotification_contentIntentTargetsDashboardWithoutClearingTask() {
        Context context = ApplicationProvider.getApplicationContext();

        new CreateCustomLocalNotification(context).showAiResponseReadyNotification("AstroSage", "Preview", "Expanded");

        Notification notification = getPostedNotification(context);
        PendingIntent pendingIntent = notification.contentIntent;
        assertNotNull(pendingIntent);

        ShadowPendingIntent shadowPendingIntent = Shadows.shadowOf(pendingIntent);
        Intent intent = shadowPendingIntent.getSavedIntent();
        assertNotNull(intent.getComponent());
        assertEquals(DashBoardActivity.class.getName(), intent.getComponent().getClassName());

        // Must not clear the task stack when opening the dashboard.
        assertTrue((intent.getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TASK) == 0);
        assertTrue((intent.getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK) == 0);
    }

    /**
     * Verifies the preview/expanded helpers cap output at ~140 chars with ellipsis.
     */
    @Test
    public void buildAnswerHelpers_capLengthTo140CharsWithEllipsis() throws Exception {
        String longText = repeat("a", 500);
        String preview = invokeNotificationTextHelper("buildAnswerPreview", longText);
        String expanded = invokeNotificationTextHelper("buildAnswerExpandedText", longText);

        assertTrue(preview.length() <= 143);
        assertTrue(expanded.length() <= 143);
        assertTrue(preview.endsWith("..."));
        assertTrue(expanded.endsWith("..."));
    }

    /**
     * Reads the notification stored under the stable AI response-ready notification id.
     */
    private static Notification getPostedNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assertNotNull(notificationManager);

        ShadowNotificationManager shadowNotificationManager = Shadows.shadowOf(notificationManager);
        Notification notification =
                shadowNotificationManager.getNotification(CreateCustomLocalNotification.AI_RESPONSE_READY_NOTIFICATION_ID);
        assertNotNull(notification);
        return notification;
    }

    /**
     * Reads the notification stored under the stable id, or returns null when not present.
     */
    private static Notification getPostedNotificationOrNull(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assertNotNull(notificationManager);

        ShadowNotificationManager shadowNotificationManager = Shadows.shadowOf(notificationManager);
        return shadowNotificationManager.getNotification(CreateCustomLocalNotification.AI_RESPONSE_READY_NOTIFICATION_ID);
    }

    /**
     * Invokes the private helper so the test exercises the same formatting path used by the production call site.
     */
    private static String invokeNotificationTextHelper(String methodName, String answer) throws Exception {
        Method method = GetAnswerFromServer.class.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        return (String) method.invoke(null, answer);
    }

    /**
     * Tiny helper to build deterministic large test strings.
     */
    private static String repeat(String value, int count) {
        StringBuilder sb = new StringBuilder(value.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(value);
        }
        return sb.toString();
    }
}
