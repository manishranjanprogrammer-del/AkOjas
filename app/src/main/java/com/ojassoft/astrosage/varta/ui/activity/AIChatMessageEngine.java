package com.ojassoft.astrosage.varta.ui.activity;

import com.google.firebase.database.ServerValue;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Encapsulates deterministic AI chat message rules so the activity keeps only UI orchestration.
 *
 * This engine handles payload generation, local message creation, chunk splitting, chunk merging,
 * and answer-id fallback behavior that was previously spread across AI chat activity methods.
 */
public final class AIChatMessageEngine {

    private static final String KEY_FROM = "From";
    private static final String KEY_TO = "To";
    private static final String KEY_TEXT = "Text";
    private static final String KEY_MSG_TIME = "MsgTime";
    private static final String KEY_IS_SEEN = "isSeen";
    private static final String KEY_CHAT_ID = "chatId";
    private static final String KEY_MSG_TYPE = "MsgType";
    private static final String MSG_TYPE_GREET = "msg_type_greet";
    private static final String GREET = "GREET";
    private static final String USER = "User";

    private AIChatMessageEngine() {
    }

    /**
     * Returns the backend answer id when available, otherwise reproduces the legacy local fallback id.
     *
     * @param answerId backend answer id parsed from the server response
     * @param random random source used for local fallback ids
     * @return safe chat id that can be used by the adapter and Firebase payload
     */
    public static long resolveSafeAnswerId(long answerId, Random random) {
        if (answerId > 0) {
            return answerId;
        }
        return random.nextInt(999) + 1L;
    }

    /**
     * Splits a multi-paragraph AI response into typewriter-friendly chunks.
     *
     * @param text raw response body
     * @return non-empty chunks in display order, each preserving the legacy trailing line break
     */
    public static ArrayList<String> splitMessageChunks(String text) {
        String[] split;
        if (text.contains("\\n\\n")) {
            split = text.split("\\\\n\\\\n");
        } else if (text.contains("\n\n")) {
            split = text.split("\\n\\n");
        } else if (text.contains("<br><br>")) {
            split = text.split("<br><br>");
        } else {
            split = new String[]{text};
        }

        ArrayList<String> list = new ArrayList<>();
        for (String part : split) {
            if (part != null && !part.trim().isEmpty()) {
                list.add(part + "\n");
            }
        }
        return list;
    }

    /**
     * Merges streamed chunks into the final persisted text shown after rebind or chat completion.
     *
     * @param chunks current streamed chunks
     * @param isHumanLikeAi whether the AI is using human-like plain-text formatting
     * @return merged final text, or null when nothing meaningful is available
     */
    public static String mergeChunksForPersistence(List<String> chunks, boolean isHumanLikeAi) {
        if (chunks == null || chunks.isEmpty()) {
            return null;
        }

        String separator = isHumanLikeAi ? "\n\n" : "<br><br>";
        StringBuilder merged = new StringBuilder();
        for (String chunk : chunks) {
            if (chunk == null || chunk.trim().isEmpty()) {
                continue;
            }
            if (merged.length() > 0) {
                merged.append(separator);
            }
            merged.append(chunk);
        }

        return merged.length() == 0 ? null : merged.toString();
    }

    /**
     * Builds the Firebase payload used by AI chat message persistence.
     *
     * @param message message body
     * @param chatId stable chat id
     * @param from sender role
     * @param to receiver role
     * @param msgType optional message subtype
     * @return mutable payload map ready for Firebase submission
     */
    public static Map<String, Object> buildFirebasePayload(String message, long chatId, String from, String to, String msgType) {
        Map<String, Object> chatMap = new HashMap<>();
        chatMap.put(KEY_FROM, from);
        chatMap.put(KEY_TO, to);
        chatMap.put(KEY_TEXT, message);
        chatMap.put(KEY_MSG_TIME, ServerValue.TIMESTAMP);
        chatMap.put(KEY_IS_SEEN, false);
        chatMap.put(KEY_CHAT_ID, chatId);
        if (MSG_TYPE_GREET.equals(msgType)) {
            chatMap.put(KEY_MSG_TYPE, GREET);
        }
        return chatMap;
    }

    /**
     * Returns whether the message should be inserted into local history caches and DB.
     *
     * @param messageFrom author of the message
     * @param isGreetingMsg true when the insert represents a locally-created greeting
     * @return true when the message should be persisted locally
     */
    public static boolean shouldPersistLocally(String messageFrom, boolean isGreetingMsg) {
        return USER.equals(messageFrom) || isGreetingMsg;
    }

    /**
     * Creates the message model shared by adapter insertion and local history persistence.
     *
     * @param messageText visible message body
     * @param messageFrom message author
     * @param chatId stable chat id
     * @param animate whether the adapter should animate the message
     * @param isDelayed whether delayed rendering is expected
     * @param dateCreated formatted display timestamp
     * @param timeStamp raw event timestamp
     * @return populated message model
     */
    public static Message createMessage(String messageText, String messageFrom, long chatId, boolean animate, boolean isDelayed, String dateCreated, long timeStamp) {
        Message message = new Message();
        message.setAuthor(messageFrom);
        message.setDateCreated(dateCreated);
        message.setTimeStamp(timeStamp);
        message.setMessageBody(messageText);
        message.setSeen(animate);
        message.setChatId(chatId);
        message.setDelayed(isDelayed);
        return message;
    }
}
