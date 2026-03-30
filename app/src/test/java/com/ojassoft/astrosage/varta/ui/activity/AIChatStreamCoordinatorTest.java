package com.ojassoft.astrosage.varta.ui.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/** Unit tests for server-stream decision rules extracted from the AI chat activity. */
public class AIChatStreamCoordinatorTest {

    /** Verifies a typewriter-start event creates a new AI bubble when no active message exists. */
    @Test
    public void planTypewriterStart_addsBubbleForFreshAnswer() {
        AIChatStreamCoordinator.StartPlan plan =
                AIChatStreamCoordinator.planTypewriterStart("First answer", 77L, false, -1L, new Random(0));

        assertTrue(plan.shouldAddMessage());
        assertFalse(plan.shouldRefreshExistingMessage());
        assertEquals("First answer", plan.messageBody());
        assertEquals(77L, plan.activeAnswerId());
        assertEquals(0, plan.pendingChunkRetryCount());
        assertEquals(-1, plan.lastHandledTypingIndex());
    }

    /** Verifies a repeated typewriter-start update reuses the existing bubble for the same answer id. */
    @Test
    public void planTypewriterStart_reusesExistingBubbleForSameAnswerId() {
        AIChatStreamCoordinator.StartPlan plan =
                AIChatStreamCoordinator.planTypewriterStart("Updated answer", 88L, true, 88L, new Random(0));

        assertFalse(plan.shouldAddMessage());
        assertTrue(plan.shouldRefreshExistingMessage());
        assertEquals("Updated answer", plan.messageBody());
        assertEquals(88L, plan.activeAnswerId());
    }

    /** Verifies missing server answer ids still create a deterministic local fallback id. */
    @Test
    public void planTypewriterStart_generatesFallbackAnswerIdWhenMissing() {
        Random random = mock(Random.class);
        when(random.nextInt(999)).thenReturn(41);

        AIChatStreamCoordinator.StartPlan plan =
                AIChatStreamCoordinator.planTypewriterStart("Fallback answer", 0L, false, -1L, random);

        assertTrue(plan.shouldAddMessage());
        assertEquals(42L, plan.activeAnswerId());
    }

    /** Verifies a late server chunk update creates the missing backing message before streaming continues. */
    @Test
    public void planStreamUpdate_addsMissingBubbleWhenUpdateArrivesBeforeStart() {
        AIChatStreamCoordinator.StreamUpdatePlan plan =
                AIChatStreamCoordinator.planStreamUpdate("Late answer", 55L, false, -1L, new Random(0));

        assertTrue(plan.shouldAddMessage());
        assertEquals(55L, plan.messageChatId());
        assertEquals("Late answer", plan.messageBody());
        assertEquals("Late answer", plan.animatedText());
        assertNull(plan.messageChunks());
    }

    /** Verifies chunked server text is split once and the first chunk drives the active typewriter view. */
    @Test
    public void planStreamUpdate_splitsChunkedAnswerAndUsesFirstChunkForAnimation() {
        AIChatStreamCoordinator.StreamUpdatePlan plan =
                AIChatStreamCoordinator.planStreamUpdate("Line 1\\n\\nLine 2", 66L, true, 66L, new Random(0));

        assertFalse(plan.shouldAddMessage());
        assertNotNull(plan.messageChunks());
        assertEquals(Arrays.asList("Line 1\n", "Line 2\n"), plan.messageChunks());
        assertEquals("Line 1\n", plan.animatedText());
        assertEquals("Line 1\\n\\nLine 2", plan.messageBody());
    }

    /** Verifies plain server updates keep the full text as the typewriter payload when no chunk separators exist. */
    @Test
    public void planStreamUpdate_keepsPlainTextWhenNoChunkSeparatorExists() {
        AIChatStreamCoordinator.StreamUpdatePlan plan =
                AIChatStreamCoordinator.planStreamUpdate("Single line answer", 99L, true, 99L, new Random(0));

        assertNull(plan.messageChunks());
        assertEquals("Single line answer", plan.animatedText());
        assertEquals("Single line answer", plan.messageBody());
    }
}
