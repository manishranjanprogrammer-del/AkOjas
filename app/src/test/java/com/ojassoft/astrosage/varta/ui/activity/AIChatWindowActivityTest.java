package com.ojassoft.astrosage.varta.ui.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.text.TextUtils;

import com.ojassoft.astrosage.varta.adapters.AIChatMessageAdapter;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/** Regression tests for AI chat streaming helpers inside {@link AIChatWindowActivity}. */
public class AIChatWindowActivityTest {

    /** Verifies empty streamed text does not create a placeholder AI message. */
    @Test
    public void ensureAiMessageInitializedForStream_ignoresEmptyText() throws Exception {
        TestAIChatWindowActivity activity = newActivity();

        try (MockedStatic<TextUtils> mockedTextUtils = mockTextUtilsIsEmpty()) {
            invokeEnsureAiMessageInitializedForStream(activity, "", 33L);
        }

        assertNull(getField(activity, "currentMessage"));
        assertEquals(-1L, getLongField(activity, "activeAiAnswerId"));
        assertEquals(0, activity.addMessageCallCount);
    }

    /** Verifies a streamed response creates a new AI message when no backing item exists yet. */
    @Test
    public void ensureAiMessageInitializedForStream_addsMessageWithProvidedAnswerId() throws Exception {
        TestAIChatWindowActivity activity = newActivity();

        try (MockedStatic<TextUtils> mockedTextUtils = mockTextUtilsIsEmpty()) {
            invokeEnsureAiMessageInitializedForStream(activity, "First streamed answer", 77L);
        }

        Message currentMessage = (Message) getField(activity, "currentMessage");
        assertEquals(1, activity.addMessageCallCount);
        assertEquals(77L, activity.lastChatId);
        assertEquals(77L, getLongField(activity, "activeAiAnswerId"));
        assertSame(activity.lastAddedMessage, currentMessage);
        assertEquals("First streamed answer", currentMessage.getMessageBody());
    }

    /** Verifies the helper skips duplicate bubble creation for the same active AI answer id. */
    @Test
    public void ensureAiMessageInitializedForStream_reusesExistingMessageForSameAnswerId() throws Exception {
        TestAIChatWindowActivity activity = newActivity();
        Message existingMessage = new Message();
        existingMessage.setMessageBody("Existing streamed answer");

        setField(activity, "currentMessage", existingMessage);
        setField(activity, "activeAiAnswerId", 88L);

        try (MockedStatic<TextUtils> mockedTextUtils = mockTextUtilsIsEmpty()) {
            invokeEnsureAiMessageInitializedForStream(activity, "Updated streamed answer", 88L);
        }

        assertEquals(0, activity.addMessageCallCount);
        assertSame(existingMessage, getField(activity, "currentMessage"));
        assertEquals(88L, getLongField(activity, "activeAiAnswerId"));
    }

    /** Verifies missing server answer ids fall back to a deterministic local chat id. */
    @Test
    public void ensureAiMessageInitializedForStream_generatesFallbackAnswerIdWhenMissing() throws Exception {
        TestAIChatWindowActivity activity = newActivity();
        Random random = mock(Random.class);
        when(random.nextInt(999)).thenReturn(41);
        setField(activity, "numRandom", random);

        try (MockedStatic<TextUtils> mockedTextUtils = mockTextUtilsIsEmpty()) {
            invokeEnsureAiMessageInitializedForStream(activity, "Answer without id", 0L);
        }

        assertEquals(42L, activity.lastChatId);
        assertEquals(42L, getLongField(activity, "activeAiAnswerId"));
    }

    /** Verifies human-like AI typing persistence merges chunks with double-newline separators. */
    @Test
    public void persistCurrentAiMessageForAdapter_mergesHumanChunksWithParagraphBreaks() throws Exception {
        TestAIChatWindowActivity activity = newActivity();
        AIChatMessageAdapter messageAdapter = mock(AIChatMessageAdapter.class);
        Message currentMessage = new Message();
        currentMessage.setMessageBody("stale");

        when(messageAdapter.getItemCount()).thenReturn(1);
        setField(activity, "messageAdapter", messageAdapter);
        setField(activity, "currentMessage", currentMessage);
        setField(activity, "messageChunks", new ArrayList<>(Arrays.asList("Line 1", "Line 2")));
        setField(activity, "isAiBehaveLikeHuman", true);

        try (MockedStatic<TextUtils> mockedTextUtils = mockTextUtilsIsEmpty()) {
            invokePersistCurrentAiMessageForAdapter(activity);
        }

        assertEquals("Line 1\n\nLine 2", currentMessage.getMessageBody());
        verify(messageAdapter, times(1)).updateMessageInAdapter("Line 1\n\nLine 2");
    }

    /** Verifies standard AI typing persistence merges chunks with HTML paragraph separators. */
    @Test
    public void persistCurrentAiMessageForAdapter_mergesStandardChunksWithHtmlBreaks() throws Exception {
        TestAIChatWindowActivity activity = newActivity();
        AIChatMessageAdapter messageAdapter = mock(AIChatMessageAdapter.class);
        Message currentMessage = new Message();
        currentMessage.setMessageBody("stale");

        when(messageAdapter.getItemCount()).thenReturn(1);
        setField(activity, "messageAdapter", messageAdapter);
        setField(activity, "currentMessage", currentMessage);
        setField(activity, "messageChunks", new ArrayList<>(Arrays.asList("Line 1", "Line 2")));
        setField(activity, "isAiBehaveLikeHuman", false);

        try (MockedStatic<TextUtils> mockedTextUtils = mockTextUtilsIsEmpty()) {
            invokePersistCurrentAiMessageForAdapter(activity);
        }

        assertEquals("Line 1<br><br>Line 2", currentMessage.getMessageBody());
        verify(messageAdapter, times(1)).updateMessageInAdapter("Line 1<br><br>Line 2");
    }

    /** Creates an unstarted activity instance so helper methods can be tested in isolation. */
    private TestAIChatWindowActivity newActivity() {
        try {
            TestAIChatWindowActivity activity = instantiateWithoutConstructor(TestAIChatWindowActivity.class);
            setField(activity, "activeAiAnswerId", -1L);
            return activity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Invokes the private stream-initialization helper for direct branch verification. */
    private void invokeEnsureAiMessageInitializedForStream(AIChatWindowActivity activity, String updatedString, long answerId) throws Exception {
        Method method = AIChatWindowActivity.class.getDeclaredMethod("ensureAiMessageInitializedForStream", String.class, long.class);
        method.setAccessible(true);
        method.invoke(activity, updatedString, answerId);
    }

    /** Invokes the private adapter-persistence helper to validate final-text consolidation. */
    private void invokePersistCurrentAiMessageForAdapter(AIChatWindowActivity activity) throws Exception {
        Method method = AIChatWindowActivity.class.getDeclaredMethod("persistCurrentAiMessageForAdapter");
        method.setAccessible(true);
        method.invoke(activity);
    }

    /** Sets a private field on the activity while supporting fields declared in parent classes. */
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /** Reads a private field from the activity while supporting fields declared in parent classes. */
    private Object getField(Object target, String fieldName) throws Exception {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    /** Reads a numeric private field as a primitive long for simpler assertions. */
    private long getLongField(Object target, String fieldName) throws Exception {
        return ((Number) getField(target, fieldName)).longValue();
    }

    /** Resolves a declared field by walking the test-subclass hierarchy back to the activity. */
    private Field findField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> currentType = type;
        while (currentType != null) {
            try {
                return currentType.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                currentType = currentType.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    /** Provides a local JVM-safe implementation for the Android `TextUtils.isEmpty` helper used by production code. */
    private MockedStatic<TextUtils> mockTextUtilsIsEmpty() {
        MockedStatic<TextUtils> mockedTextUtils = Mockito.mockStatic(TextUtils.class);
        mockedTextUtils.when(() -> TextUtils.isEmpty(any())).thenAnswer(invocation -> {
            CharSequence value = invocation.getArgument(0);
            return value == null || value.length() == 0;
        });
        return mockedTextUtils;
    }

    /** Allocates a test instance without running Android framework constructors that are stubbed in local JVM tests. */
    @SuppressWarnings("unchecked")
    private <T> T instantiateWithoutConstructor(Class<T> type) throws Exception {
        Field unsafeField = Class.forName("sun.misc.Unsafe").getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Object unsafe = unsafeField.get(null);
        Method allocateInstance = unsafe.getClass().getMethod("allocateInstance", Class.class);
        return (T) allocateInstance.invoke(unsafe, type);
    }

    /** Lightweight test double that captures message creation without touching RecyclerView or storage. */
    public static class TestAIChatWindowActivity extends AIChatWindowActivity {
        int addMessageCallCount;
        long lastChatId;
        Message lastAddedMessage;

        /** Captures message-add requests so stream helper tests stay isolated from UI infrastructure. */
        @Override
        public Message addMessageToAdapter(String messageText, String messageFrom, long chatId, boolean animate, boolean isGreetingMsg) {
            addMessageCallCount++;
            lastChatId = chatId;
            lastAddedMessage = new Message();
            lastAddedMessage.setAuthor(messageFrom);
            lastAddedMessage.setChatId(chatId);
            lastAddedMessage.setSeen(animate);
            lastAddedMessage.setMessageBody(messageText);
            return lastAddedMessage;
        }
    }
}
