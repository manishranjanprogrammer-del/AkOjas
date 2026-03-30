package com.ojassoft.astrosage.varta.ui.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/** Unit tests for the extracted AI typewriter completion and retry rules. */
public class AIChatTypewriterFlowTest {

    /** Verifies duplicate completion callbacks are ignored so the next chunk is not rendered twice. */
    @Test
    public void onChunkFinished_ignoresDuplicateCompletionCallbacks() {
        AIChatTypewriterFlow.FinishDecision decision = AIChatTypewriterFlow.onChunkFinished(
                1,
                new ArrayList<>(Arrays.asList("A\n", "B\n")),
                false,
                "A\\n\\nB",
                2,
                0,
                1000L,
                1000L,
                3
        );

        assertEquals(AIChatTypewriterFlow.Action.IGNORE_DUPLICATE, decision.action());
        assertEquals(2, decision.lastHandledTypingIndex());
    }

    /** Verifies the flow waits briefly for more streamed content when the next chunk has not arrived yet. */
    @Test
    public void onChunkFinished_requestsRetryWhileStreamingWindowIsStillOpen() {
        AIChatTypewriterFlow.FinishDecision decision = AIChatTypewriterFlow.onChunkFinished(
                0,
                new ArrayList<>(Arrays.asList("A\n")),
                false,
                "A",
                -1,
                0,
                1500L,
                1500L,
                1
        );

        assertEquals(AIChatTypewriterFlow.Action.WAIT_RETRY, decision.action());
        assertEquals(1, decision.pendingChunkRetryCount());
        assertEquals(200L, decision.retryDelayMs());
    }

    /** Verifies the flow can recover newly arrived chunks from the latest message body during retry. */
    @Test
    public void onChunkFinished_recoversNewChunkFromLatestMessageBody() {
        AIChatTypewriterFlow.FinishDecision decision = AIChatTypewriterFlow.onChunkFinished(
                0,
                new ArrayList<>(Arrays.asList("A\n")),
                false,
                "A\\n\\nB",
                -1,
                0,
                1500L,
                1500L,
                1
        );

        assertEquals(AIChatTypewriterFlow.Action.START_NEXT_CHUNK, decision.action());
        assertEquals(1, decision.nextPosition());
        assertEquals("B\n", decision.nextChunkText());
        assertEquals(Arrays.asList("A\n", "B\n"), decision.messageChunks());
        assertEquals(0, decision.pendingChunkRetryCount());
    }

    /** Verifies the flow finishes cleanly once no more chunks remain or typing is stopped. */
    @Test
    public void onChunkFinished_completesWhenAllChunksAreDone() {
        AIChatTypewriterFlow.FinishDecision decision = AIChatTypewriterFlow.onChunkFinished(
                1,
                new ArrayList<>(Arrays.asList("A\n", "B\n")),
                false,
                "A\\n\\nB",
                -1,
                0,
                3000L,
                1000L,
                2
        );

        assertEquals(AIChatTypewriterFlow.Action.COMPLETE, decision.action());
        assertEquals(1, decision.lastHandledTypingIndex());
        assertNull(decision.nextChunkText());
    }

    /** Verifies already-rendered chunks are not attached again when the completion callback races with UI updates. */
    @Test
    public void onChunkFinished_skipsChunkWhenViewAlreadyExists() {
        AIChatTypewriterFlow.FinishDecision decision = AIChatTypewriterFlow.onChunkFinished(
                0,
                new ArrayList<>(Arrays.asList("A\n", "B\n")),
                false,
                "A\\n\\nB",
                -1,
                0,
                2000L,
                2000L,
                3
        );

        assertEquals(AIChatTypewriterFlow.Action.ALREADY_RENDERED, decision.action());
        assertEquals(0, decision.lastHandledTypingIndex());
    }

    /** Verifies a valid next chunk triggers immediate rendering when it has not been attached yet. */
    @Test
    public void onChunkFinished_startsNextChunkWhenAvailable() {
        AIChatTypewriterFlow.FinishDecision decision = AIChatTypewriterFlow.onChunkFinished(
                0,
                new ArrayList<>(Arrays.asList("A\n", "B\n")),
                false,
                "A\\n\\nB",
                -1,
                0,
                2000L,
                2000L,
                1
        );

        assertEquals(AIChatTypewriterFlow.Action.START_NEXT_CHUNK, decision.action());
        assertEquals(1, decision.nextPosition());
        assertEquals("B\n", decision.nextChunkText());
        assertTrue(decision.messageChunks().contains("B\n"));
    }
}
