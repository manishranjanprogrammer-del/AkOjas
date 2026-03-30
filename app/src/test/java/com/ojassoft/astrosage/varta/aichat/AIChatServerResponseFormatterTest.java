package com.ojassoft.astrosage.varta.aichat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

/** Unit tests for the extracted AI chat markdown and button formatter helpers. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, manifest = Config.NONE)
public class AIChatServerResponseFormatterTest {

    /** Verifies markdown headers, bold, and italic spans are converted into the existing HTML format. */
    @Test
    public void convertMarkdownToHtml_convertsSupportedMarkdown() {
        String html = AIChatServerResponseFormatter.convertMarkdownToHtml("# Title\n**Bold** and _italic_");

        assertTrue(html.contains("<strong>Title</strong>"));
        assertTrue(html.contains("<strong>Bold</strong>"));
        assertTrue(html.contains("<em>italic</em>"));
    }

    /** Verifies external links and buttons are appended in the UI-specific delimiter format. */
    @Test
    public void appendButtons_appendsLinkAndButtonsInOrder() {
        String formatted = AIChatServerResponseFormatter.appendButtons(
                "Base",
                Arrays.asList(
                        new AIChatServerResponseFormatter.ResponseButton("Retry", "retry"),
                        new AIChatServerResponseFormatter.ResponseButton("Later", "")
                ),
                "https://example.com",
                "Open Site"
        );

        assertEquals("Base~~Open Site~~https://example.com~~Retry~~retry~~Later~~@", formatted);
    }

    /** Verifies button extraction preserves text and normalizes missing values for later formatting. */
    @Test
    public void extractButtons_readsJSONArrayIntoPlainButtonModels() throws Exception {
        JSONArray jsonArray = new JSONArray()
                .put(new JSONObject().put("text", "Retry").put("value", "retry"))
                .put(new JSONObject().put("text", "Later").put("value", ""));

        assertEquals(2, AIChatServerResponseFormatter.extractButtons(jsonArray).size());
        assertEquals("Retry", AIChatServerResponseFormatter.extractButtons(jsonArray).get(0).text());
        assertEquals("", AIChatServerResponseFormatter.extractButtons(jsonArray).get(1).value());
    }
}
