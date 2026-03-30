package com.ojassoft.astrosage.varta.aichat;

import static org.junit.Assert.assertEquals;

import com.ojassoft.astrosage.TestApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

/**
 * Unit tests for the private notification text helpers used before posting the AI response-ready notification.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, application = TestApplication.class)
public class GetAnswerFromServerNotificationTextTest {

    /**
     * Verifies preview text is flattened to a single line and trimmed to the notification-safe limit.
     */
    @Test
    public void buildAnswerPreview_collapsesBreaksAndTruncates() throws Exception {
        Method method = GetAnswerFromServer.class.getDeclaredMethod("buildAnswerPreview", String.class);
        method.setAccessible(true);

        String preview = (String) method.invoke(null, "Hello<br>World\\nNext\nLine");
        assertEquals("Hello World Next Line", preview);

        String longText = (String) method.invoke(null, repeat("a", 200));
        assertEquals(143, longText.length());
    }

    /**
     * Verifies expanded text preserves line breaks and still applies the same max-length protection.
     */
    @Test
    public void buildAnswerExpandedText_retainsBreaksAndTruncates() throws Exception {
        Method method = GetAnswerFromServer.class.getDeclaredMethod("buildAnswerExpandedText", String.class);
        method.setAccessible(true);

        String expanded = (String) method.invoke(null, "Hello<br>World");
        assertEquals("Hello\nWorld", expanded);

        String longText = (String) method.invoke(null, repeat("b", 200));
        assertEquals(143, longText.length());
    }

    /**
     * Creates deterministic long text for helper truncation assertions.
     */
    private static String repeat(String text, int count) {
        StringBuilder builder = new StringBuilder(text.length() * count);
        for (int i = 0; i < count; i++) {
            builder.append(text);
        }
        return builder.toString();
    }
}
