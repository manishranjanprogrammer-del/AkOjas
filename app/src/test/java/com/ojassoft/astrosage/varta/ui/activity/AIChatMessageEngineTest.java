package com.ojassoft.astrosage.varta.ui.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

/** Unit tests for the extracted AI chat message engine business rules. */
public class AIChatMessageEngineTest {

    private static final String USER = "User";
    private static final String ASTROLOGER = "Astrologer";
    private static final String MSG_TYPE_GREET = "msg_type_greet";
    private static final String GREET = "GREET";
    private static final String KEY_MSG_TYPE = "MsgType";

    /** Verifies a valid backend answer id is preserved instead of generating a local fallback. */
    @Test
    public void resolveSafeAnswerId_returnsProvidedServerId() {
        Random random = mock(Random.class);

        long resolvedId = AIChatMessageEngine.resolveSafeAnswerId(77L, random);

        assertEquals(77L, resolvedId);
    }

    /** Verifies missing server ids fall back to the legacy local random-id behavior. */
    @Test
    public void resolveSafeAnswerId_generatesFallbackWhenServerIdMissing() {
        Random random = mock(Random.class);
        when(random.nextInt(999)).thenReturn(41);

        long resolvedId = AIChatMessageEngine.resolveSafeAnswerId(0L, random);

        assertEquals(42L, resolvedId);
    }

    /** Verifies escaped double-newline separators are split into display chunks with trailing line breaks. */
    @Test
    public void splitMessageChunks_handlesEscapedDoubleNewlines() {
        assertEquals(Arrays.asList("Line 1\n", "Line 2\n"), AIChatMessageEngine.splitMessageChunks("Line 1\\n\\nLine 2"));
    }

    /** Verifies HTML paragraph breaks are converted into separate chunks for the existing typewriter flow. */
    @Test
    public void splitMessageChunks_handlesHtmlBreaks() {
        assertEquals(Arrays.asList("Line 1\n", "Line 2\n"), AIChatMessageEngine.splitMessageChunks("Line 1<br><br>Line 2"));
    }

    /** Verifies persistence merge uses human-readable paragraph breaks for human-like AI mode. */
    @Test
    public void mergeChunksForPersistence_usesPlainParagraphBreaksForHumanLikeAi() {
        String merged = AIChatMessageEngine.mergeChunksForPersistence(Arrays.asList("Line 1", "Line 2"), true);

        assertEquals("Line 1\n\nLine 2", merged);
    }

    /** Verifies persistence merge uses HTML paragraph breaks for the standard AI mode. */
    @Test
    public void mergeChunksForPersistence_usesHtmlParagraphBreaksForStandardAi() {
        String merged = AIChatMessageEngine.mergeChunksForPersistence(Arrays.asList("Line 1", "Line 2"), false);

        assertEquals("Line 1<br><br>Line 2", merged);
    }

    /** Verifies empty chunk lists do not produce placeholder persistence output. */
    @Test
    public void mergeChunksForPersistence_returnsNullForEmptyChunks() {
        assertNull(AIChatMessageEngine.mergeChunksForPersistence(Collections.emptyList(), true));
    }

    /** Verifies the Firebase payload contains greet metadata only for greet messages. */
    @Test
    public void buildFirebasePayload_addsGreetTypeOnlyForGreetingMessages() {
        Map<String, Object> payload = AIChatMessageEngine.buildFirebasePayload("Hello", 99L, ASTROLOGER, USER, MSG_TYPE_GREET);

        assertEquals("Hello", payload.get("Text"));
        assertEquals(99L, payload.get("chatId"));
        assertEquals(GREET, payload.get(KEY_MSG_TYPE));
        assertTrue(payload.containsKey("MsgTime"));
    }

    /** Verifies non-greeting messages do not carry greet metadata in the Firebase payload. */
    @Test
    public void buildFirebasePayload_omitsGreetTypeForNormalMessages() {
        Map<String, Object> payload = AIChatMessageEngine.buildFirebasePayload("Hello", 99L, USER, ASTROLOGER, "");

        assertFalse(payload.containsKey(KEY_MSG_TYPE));
    }

    /** Verifies local history persistence only happens for user messages or greeting inserts. */
    @Test
    public void shouldPersistLocally_matchesExistingPersistenceRules() {
        assertTrue(AIChatMessageEngine.shouldPersistLocally(USER, false));
        assertTrue(AIChatMessageEngine.shouldPersistLocally(ASTROLOGER, true));
        assertFalse(AIChatMessageEngine.shouldPersistLocally(ASTROLOGER, false));
    }

    /** Verifies message creation centralizes shared message field defaults for adapter insertion. */
    @Test
    public void createMessage_populatesExpectedUiFields() {
        Message message = AIChatMessageEngine.createMessage("Answer", ASTROLOGER, 123L, true, false, "10 March", 5000L);

        assertEquals("Answer", message.getMessageBody());
        assertEquals(ASTROLOGER, message.getAuthor());
        assertEquals(123L, message.getChatId());
        assertTrue(message.isSeen());
        assertFalse(message.isDelayed());
        assertEquals("10 March", message.getDateCreated());
        assertEquals(5000L, message.getTimeStamp());
    }
}
