package com.ojassoft.astrosage.ui.JobServices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;
import androidx.work.WorkerParameters;

import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNotification;
import org.robolectric.shadows.ShadowNotificationManager;

import java.lang.reflect.Method;

/**
 * Verifies the local birthday notification still posts correctly and routes click analytics/navigation through
 * the tracked pending intent flow.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, application = com.ojassoft.astrosage.TestApplication.class)
public class BirthdayWishNotificationFlowTest {

    /**
     * Ensures the birthday notification is posted, logs a delivered event, and its click intent logs the click
     * event before opening the expected app screen.
     */
    @Test
    public void birthdayNotification_postsAndClickIntentLaunchesHomeInputScreen() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assertNotNull(notificationManager);
        notificationManager.cancelAll();

        BirthdayWishService worker = new BirthdayWishService(context, org.mockito.Mockito.mock(WorkerParameters.class));

        try (MockedStatic<CUtils> analyticsMock = org.mockito.Mockito.mockStatic(CUtils.class)) {
            invokeShowHappyBirthdayNotification(worker, context, "Test User");

            Notification notification = getPostedNotification(notificationManager, 1004);
            ShadowNotification shadowNotification = Shadows.shadowOf(notification);
            assertTrue(String.valueOf(shadowNotification.getContentTitle()).contains("Test User"));
            assertNotNull(notification.contentIntent);

            analyticsMock.verify(() -> CUtils.fcmAnalyticsEvents(
                    CGlobalVariables.BIRTHDAY_INTENT_ACTION,
                    CGlobalVariables.FIREBASE_NOTIFICATION_DELIVERED_EVENT,
                    ""
            ), times(1));

            notification.contentIntent.send();
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            Intent trampolineIntent = Shadows.shadowOf((Application) context).getNextStartedActivity();
            assertNotNull("Expected trampoline activity launch after tapping notification", trampolineIntent);
            assertEquals(BirthdayWishNotificationClickActivity.class.getName(),
                    trampolineIntent.getComponent() != null ? trampolineIntent.getComponent().getClassName() : null);

            Robolectric.buildActivity(BirthdayWishNotificationClickActivity.class, trampolineIntent)
                    .create()
                    .start()
                    .resume()
                    .visible();
            Shadows.shadowOf(Looper.getMainLooper()).idle();

            Intent startedIntent = Shadows.shadowOf((Application) context).getNextStartedActivity();
            assertNotNull("Expected HomeInputScreen launch after tapping notification", startedIntent);
            assertEquals("Notification", startedIntent.getStringExtra("from"));
            assertEquals(CGlobalVariables.MODULE_VARSHAPHAL,
                    startedIntent.getIntExtra(CGlobalVariables.MODULE_TYPE_KEY, -1));

            analyticsMock.verify(() -> CUtils.fcmAnalyticsEvents(
                    CGlobalVariables.BIRTHDAY_INTENT_ACTION,
                    CGlobalVariables.FIREBASE_NOTIFICATION_CLICKED_EVENT,
                    ""
            ), times(1));
        }
    }

    /**
     * Invokes the production private helper so the test exercises the exact notification construction path.
     */
    private static void invokeShowHappyBirthdayNotification(BirthdayWishService worker, Context context, String name)
            throws Exception {
        Method method = BirthdayWishService.class.getDeclaredMethod(
                "showHappyBirthDayNotificationInternal",
                Context.class,
                String.class,
                com.ojassoft.astrosage.beans.BeanHoroPersonalInfo.class
        );
        method.setAccessible(true);
        method.invoke(null, context, name, null);
    }

    /**
     * Reads a notification posted under the expected static id from the Robolectric notification manager shadow.
     */
    private static Notification getPostedNotification(NotificationManager notificationManager, int notificationId) {
        ShadowNotificationManager shadowNotificationManager = Shadows.shadowOf(notificationManager);
        Notification notification = shadowNotificationManager.getNotification(notificationId);
        assertNotNull("Expected notification id " + notificationId + " to be posted", notification);
        return notification;
    }
}
