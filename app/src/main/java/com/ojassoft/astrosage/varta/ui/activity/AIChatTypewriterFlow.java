package com.ojassoft.astrosage.varta.ui.activity;

import java.util.ArrayList;

/**
 * Encapsulates typewriter completion rules for streamed AI answers.
 *
 * This flow decides whether the next chunk should start immediately, whether the UI should wait
 * for more streamed content, or whether the current response has finished.
 */
public final class AIChatTypewriterFlow {

    private static final long STREAM_RETRY_WINDOW_MS = 1200L;
    private static final long RETRY_DELAY_MS = 200L;
    private static final int MAX_PENDING_RETRIES = 8;

    /**
     * Prevents instantiation because the flow exposes stateless decision helpers only.
     */
    private AIChatTypewriterFlow() {
    }

    /**
     * Plans the next typewriter action after one chunk finishes animating.
     *
     * @param finishedIndex chunk index that just finished typing
     * @param messageChunks currently known chunk list
     * @param stopTypeWriter whether the typing flow has been force-stopped
     * @param latestMessageBody latest full message body available from the server
     * @param lastHandledTypingIndex last completion callback index already handled
     * @param pendingChunkRetryCount current retry counter for delayed chunk arrivals
     * @param nowMs current wall-clock time in milliseconds
     * @param lastAiChunkUpdateTimeMs wall-clock time of the last server chunk update
     * @param renderedChunkCount current count of already attached typewriter views
     * @return immutable decision describing the next typewriter action
     */
    public static FinishDecision onChunkFinished(int finishedIndex, ArrayList<String> messageChunks, boolean stopTypeWriter, String latestMessageBody, int lastHandledTypingIndex, int pendingChunkRetryCount, long nowMs, long lastAiChunkUpdateTimeMs, int renderedChunkCount) {
        if (finishedIndex <= lastHandledTypingIndex) {
            return new FinishDecision(Action.IGNORE_DUPLICATE, messageChunks, pendingChunkRetryCount, lastHandledTypingIndex, -1, null, 0L);
        }

        int nextPosition = finishedIndex + 1;
        ArrayList<String> latestChunks = messageChunks;

        if ((latestChunks == null || nextPosition >= latestChunks.size()) && !stopTypeWriter) {
            ArrayList<String> recoveredChunks = AIChatStreamCoordinator.hasChunkSeparator(latestMessageBody)
                    ? AIChatMessageEngine.splitMessageChunks(latestMessageBody)
                    : null;
            boolean shouldRetryForStreaming = (nowMs - lastAiChunkUpdateTimeMs) < STREAM_RETRY_WINDOW_MS;
            if (recoveredChunks != null && nextPosition < recoveredChunks.size()) {
                latestChunks = recoveredChunks;
                pendingChunkRetryCount = 0;
            } else if (shouldRetryForStreaming && pendingChunkRetryCount < MAX_PENDING_RETRIES) {
                return new FinishDecision(Action.WAIT_RETRY, latestChunks, pendingChunkRetryCount + 1, lastHandledTypingIndex, -1, null, RETRY_DELAY_MS);
            }
        }

        if (latestChunks == null || nextPosition >= latestChunks.size() || stopTypeWriter) {
            return new FinishDecision(Action.COMPLETE, latestChunks, 0, finishedIndex, -1, null, 0L);
        }

        if (renderedChunkCount > nextPosition) {
            return new FinishDecision(Action.ALREADY_RENDERED, latestChunks, 0, finishedIndex, nextPosition, null, 0L);
        }

        return new FinishDecision(Action.START_NEXT_CHUNK, latestChunks, 0, finishedIndex, nextPosition, latestChunks.get(nextPosition), 0L);
    }

    /**
     * Enumerates the high-level actions that the activity can perform after a chunk finishes.
     */
    public enum Action {
        IGNORE_DUPLICATE,
        WAIT_RETRY,
        COMPLETE,
        ALREADY_RENDERED,
        START_NEXT_CHUNK
    }

    /**
     * Describes the next typewriter action after a completion callback is processed.
     */
    public static final class FinishDecision {
        private final Action action;
        private final ArrayList<String> messageChunks;
        private final int pendingChunkRetryCount;
        private final int lastHandledTypingIndex;
        private final int nextPosition;
        private final String nextChunkText;
        private final long retryDelayMs;

        /**
         * Captures the immutable result of evaluating a typewriter completion callback.
         *
         * @param action next action the activity should perform
         * @param messageChunks latest known chunk list
         * @param pendingChunkRetryCount retry counter value to keep in activity state
         * @param lastHandledTypingIndex last handled callback index to store in activity state
         * @param nextPosition next chunk position, or `-1` when not applicable
         * @param nextChunkText next chunk text to animate, or null when not applicable
         * @param retryDelayMs delay for the next retry when waiting for streamed content
         */
        public FinishDecision(Action action, ArrayList<String> messageChunks, int pendingChunkRetryCount, int lastHandledTypingIndex, int nextPosition, String nextChunkText, long retryDelayMs) {
            this.action = action;
            this.messageChunks = messageChunks;
            this.pendingChunkRetryCount = pendingChunkRetryCount;
            this.lastHandledTypingIndex = lastHandledTypingIndex;
            this.nextPosition = nextPosition;
            this.nextChunkText = nextChunkText;
            this.retryDelayMs = retryDelayMs;
        }

        /**
         * Returns the action the activity should perform next.
         *
         * @return planned typewriter action
         */
        public Action action() {
            return action;
        }

        /**
         * Returns the latest known chunk list that should replace the activity state.
         *
         * @return latest chunk list, or null when unavailable
         */
        public ArrayList<String> messageChunks() {
            return messageChunks;
        }

        /**
         * Returns the retry counter value that should be stored back into activity state.
         *
         * @return pending chunk retry count
         */
        public int pendingChunkRetryCount() {
            return pendingChunkRetryCount;
        }

        /**
         * Returns the last handled completion index that should be stored back into activity state.
         *
         * @return last handled typing index
         */
        public int lastHandledTypingIndex() {
            return lastHandledTypingIndex;
        }

        /**
         * Returns the next chunk position to render when a new chunk should start.
         *
         * @return next chunk position, or `-1` when not applicable
         */
        public int nextPosition() {
            return nextPosition;
        }

        /**
         * Returns the next chunk text to animate when a new chunk should start.
         *
         * @return next chunk text, or null when not applicable
         */
        public String nextChunkText() {
            return nextChunkText;
        }

        /**
         * Returns the retry delay in milliseconds when the flow is waiting for more streamed content.
         *
         * @return retry delay in milliseconds
         */
        public long retryDelayMs() {
            return retryDelayMs;
        }
    }
}
