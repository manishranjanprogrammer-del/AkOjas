package com.ojassoft.astrosage.varta.aichat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formats AI server responses into the HTML and delimiter syntax expected by the chat UI.
 *
 * This formatter centralizes markdown conversion and button/link serialization so those rules stay
 * consistent across streamed and non-streamed answer paths.
 */
public final class AIChatServerResponseFormatter {

    /**
     * Prevents instantiation because the formatter exposes stateless utility methods only.
     */
    private AIChatServerResponseFormatter() {
    }

    /**
     * Converts the supported markdown subset into the legacy HTML tags used by the chat UI.
     *
     * @param text markdown text returned by the AI backend
     * @return HTML-like formatted text compatible with the current chat renderer
     */
    public static String convertMarkdownToHtml(String text) {
        String formattedText = text;
        formattedText = replaceMarkdown(formattedText, "(?m)^###### (.*?)$", "<strong>$1</strong>");
        formattedText = replaceMarkdown(formattedText, "(?m)^##### (.*?)$", "<strong>$1</strong>");
        formattedText = replaceMarkdown(formattedText, "(?m)^#### (.*?)$", "<strong>$1</strong>");
        formattedText = replaceMarkdown(formattedText, "(?m)^### (.*?)$", "<strong>$1</strong>");
        formattedText = replaceMarkdown(formattedText, "(?m)^## (.*?)$", "<strong>$1</strong>");
        formattedText = replaceMarkdown(formattedText, "(?m)^# (.*?)$", "<strong>$1</strong>");
        formattedText = replaceMarkdown(formattedText, "(\\*\\*|__)(.*?)\\1", "<strong>$2</strong>");
        formattedText = replaceMarkdown(formattedText, "(\\*|_)(.*?)\\1", "<em>$2</em>");
        return formattedText;
    }

    /**
     * Extracts plain button models from the backend `buttons` JSON array.
     *
     * @param jsonArray backend button array
     * @return plain button models in server order
     */
    public static List<ResponseButton> extractButtons(JSONArray jsonArray) {
        ArrayList<ResponseButton> buttons = new ArrayList<>();
        if (jsonArray == null) {
            return buttons;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject == null) {
                continue;
            }
            buttons.add(new ResponseButton(jsonObject.optString("text", ""), jsonObject.optString("value", "")));
        }
        return buttons;
    }

    /**
     * Appends external links and response buttons using the legacy `~~label~~value` syntax.
     *
     * @param baseText visible answer text
     * @param buttons backend buttons already mapped to plain models
     * @param link external answer link when available
     * @param openSiteLabel localized label shown for the external link CTA
     * @return answer text with serialized buttons and links appended
     */
    public static String appendButtons(String baseText, List<ResponseButton> buttons, String link, String openSiteLabel) {
        StringBuilder result = new StringBuilder(baseText == null ? "" : baseText);
        if (link != null && !link.isEmpty()) {
            result.append("~~").append(openSiteLabel).append("~~").append(link);
        }
        if (buttons != null) {
            for (ResponseButton button : buttons) {
                String value = button.value() == null || button.value().isEmpty() ? "@" : button.value();
                result.append("~~").append(button.text()).append("~~").append(value);
            }
        }
        return result.toString();
    }

    /**
     * Applies a regex replacement used by the markdown conversion helpers.
     *
     * @param text source text
     * @param regex markdown regex pattern
     * @param replacement replacement HTML fragment
     * @return text with the pattern replaced
     */
    private static String replaceMarkdown(String text, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replacement);
    }

    /**
     * Plain response-button model used to keep formatter tests and callers independent from JSON.
     */
    public static final class ResponseButton {
        private final String text;
        private final String value;

        /**
         * Stores the backend button text/value pair for later UI serialization.
         *
         * @param text visible button label
         * @param value button payload sent back to the backend
         */
        public ResponseButton(String text, String value) {
            this.text = text;
            this.value = value;
        }

        /**
         * Returns the button label shown to the user.
         *
         * @return button text
         */
        public String text() {
            return text;
        }

        /**
         * Returns the backend payload associated with the button.
         *
         * @return button value
         */
        public String value() {
            return value;
        }
    }
}
