package com.ojassoft.astrosage.custompushnotification;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NOTIFICATION_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_NEW_STYLE_NOTIFICATION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION_NEW;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_PROFILE_URL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_NOTIFICATION_TITLE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REVERT_QUESTION_COUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONException;
import org.json.JSONObject;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * Regression tests for {@link CustomAINotification} to cover the main branches that build
 * notification data and trigger the notification display flow.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = CustomAINotificationTest.TestApp.class, sdk = Build.VERSION_CODES.P, manifest = Config.NONE)
public class CustomAINotificationTest {

    private AstrosageKundliApplication application;

    @Before
    public void setUp() throws Exception {
        application = (AstrosageKundliApplication) ApplicationProvider.getApplicationContext();
        setAppContext(application);
        PreferenceManager.getDefaultSharedPreferences(application).edit().clear().commit();
        CUtils.saveIntData(application, KEY_NOTIFICATION_COUNT, 0);
        CUtils.saveBooleanData(application, SHOW_NEW_STYLE_NOTIFICATION, false);
        AstrosageKundliApplication.aINotificationIssueLogs = "";
    }

    @Test
    public void loadNotification_withExtrasUsesProvidedAstrologer() throws Exception {
        CustomAINotification notification = new CustomAINotification(application,
                buildExtras("What is next?", "Follow-up question", "12", "Priya", "/images/priya.png", true, "AI Update"),
                "https://example.com/ai");

        runLoadNotification(notification, target -> target.onResourceReady(createBitmap(), null));

        assertEquals(1, CUtils.getIntData(application, KEY_NOTIFICATION_COUNT, 0));
        assertTrue(AstrosageKundliApplication.aINotificationIssueLogs.contains("12"));
        assertTrue(getPrivateField(notification, "extras").contains("What is next?"));
    }

    /**
     * Verifies an invalid astrologer id falls back to the saved last-used AI astrologer when present.
     */
    @Test
    public void loadNotification_withInvalidAstrologerIdUsesSavedLastUsedAstrologer() throws Exception {
        CUtils.saveStringData(application, "lastAiId", "13");
        CUtils.saveStringData(application, "lastAiName", "Love Oracle");
        CUtils.saveStringData(application, "lastAiImgUrl", "/images/astrologer/ai-astro/love_oracle.png");

        CustomAINotification notification = new CustomAINotification(application,
                buildExtras("How are the stars?", "Extra question", "0", "Unknown", "/images/unknown.png", false, "AI Update"),
                "https://example.com/fallback");

        runLoadNotification(notification, target -> target.onResourceReady(createBitmap(), null));

        assertEquals("13", getPrivateField(notification, "astroId"));
        assertEquals("Love Oracle", getPrivateField(notification, "name"));
        assertTrue(getPrivateField(notification, "profilePicture").endsWith("/images/astrologer/ai-astro/love_oracle.png"));
    }

    /**
     * Verifies an invalid astrologer id falls back to Rahasya Veda when no last-used AI astrologer is stored.
     */
    @Test
    public void loadNotification_withInvalidAstrologerIdFallsBackToRahasyaVedaDefault() throws Exception {
        CustomAINotification notification = new CustomAINotification(application,
                buildExtras("How are the stars?", "Extra question", "0", "Unknown", "/images/unknown.png", false, "AI Update"),
                "https://example.com/fallback");

        runLoadNotification(notification, target -> target.onResourceReady(createBitmap(), null));

        assertEquals("29", getPrivateField(notification, "astroId"));
        assertEquals("Rahasya Veda", getPrivateField(notification, "name"));
        assertTrue(getPrivateField(notification, "profilePicture").endsWith("/images/astrologer/ai-astro/rahasya_veda.png"));
    }

    @Test
    public void loadNotification_withoutExtrasUsesPersonalizedLink() throws Exception {
        CustomAINotification notification = new CustomAINotification(application,
                "Personalized question that should be saved to db", "Daily AI", true);

        runLoadNotification(notification, target -> target.onResourceReady(createBitmap(), null));

        assertEquals("https://varta.astrosage.com/chat-with-ai-astrologers", getPrivateField(notification, "link"));
        assertTrue(getPrivateField(notification, "extras").contains("Personalized question"));
    }

    @Test
    public void loadNotification_handlesImageLoadFailureGracefully() throws Exception {
        CustomAINotification notification = new CustomAINotification(application,
                buildExtras("Another question", "Another follow-up", "11", "Sonal", "/images/sonal.png", true, "AI Update"),
                "https://example.com/failure");

        runLoadNotification(notification, target -> target.onLoadFailed(null));

        assertEquals(1, CUtils.getIntData(application, KEY_NOTIFICATION_COUNT, 0));
        assertTrue(AstrosageKundliApplication.aINotificationIssueLogs.contains("11"));
    }

    private void runLoadNotification(CustomAINotification notification,
                                     Consumer<CustomTarget<Bitmap>> targetAction) {
        try (MockedStatic<Glide> glideMock = org.mockito.Mockito.mockStatic(Glide.class)) {
            RequestManager requestManager = mock(RequestManager.class);
            RequestBuilder<Bitmap> requestBuilder = mock(RequestBuilder.class);

            glideMock.when(() -> Glide.with(any(Context.class))).thenReturn(requestManager);
            when(requestManager.asBitmap()).thenReturn(requestBuilder);
            when(requestBuilder.load(anyString())).thenReturn(requestBuilder);
            when(requestBuilder.into(any(CustomTarget.class))).thenAnswer(invocation -> {
                CustomTarget<Bitmap> target = invocation.getArgument(0);
                targetAction.accept(target);
                return target;
            });

            notification.loadNotification();
        }
    }

    private static Bitmap createBitmap() {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    private String buildExtras(String question, String newQuestion, String astroId,
                               String name, String profileUrl, boolean online, String title) throws JSONException {
        JSONObject extras = new JSONObject();
        extras.put(KEY_AI_QUESTION, question);
        extras.put(KEY_AI_QUESTION_NEW, newQuestion);
        extras.put(KEY_AI_ASTROLOGER_ID, astroId);
        extras.put(KEY_AI_ASTROLOGER_NAME, name);
        extras.put(KEY_AI_PROFILE_URL, profileUrl);
        extras.put(KEY_REVERT_QUESTION_COUNT, "2");
        extras.put(KEY_AI_NOTIFICATION_TITLE, title);
        extras.put(KEY_AI_ASTROLOGER_ONLINE, online);
        return extras.toString();
    }

    private String getPrivateField(CustomAINotification notification, String fieldName) throws Exception {
        Field field = CustomAINotification.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = field.get(notification);
        return value != null ? value.toString() : null;
    }

    private void setAppContext(AstrosageKundliApplication app) throws Exception {
        Field field = AstrosageKundliApplication.class.getDeclaredField("astrosageKundliApplication");
        field.setAccessible(true);
        field.set(null, app);
    }

    public static class TestApp extends AstrosageKundliApplication {
        @Override
        public void onCreate() {
            // skip base initialization to avoid Firebase/remote dependencies
        }
    }
}
