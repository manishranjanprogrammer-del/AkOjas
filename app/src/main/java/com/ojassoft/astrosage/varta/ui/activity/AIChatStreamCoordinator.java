package com.ojassoft.astrosage.varta.ui.activity;

import java.util.ArrayList;
import java.util.Random;

/**
 * Centralizes AI server-stream decisions so activity code can focus on UI work only.
 *
 * This coordinator decides when a streamed answer needs a new bubble, when an existing bubble
 * should be reused, and how incoming text should be prepared for the active typewriter.
 */
public final class AIChatStreamCoordinator {

    /**
     * Prevents instantiation because the coordinator exposes stateless utility methods only.
     */
    private AIChatStreamCoordinator() {
    }

    /**
     * Plans the initial typewriter-start callback for a streamed AI answer.
     *
     * @param updatedString latest answer text received from the server
     * @param answerId backend answer id for the response
     * @param hasCurrentMessage whether the activity already has a visible backing AI message
     * @param activeAnswerId answer id currently associated with the active backing message
     * @param random random source used for local fallback ids
     * @return immutable plan describing whether to add or refresh a chat bubble
     */
    public static StartPlan planTypewriterStart(String updatedString, long answerId, boolean hasCurrentMessage, long activeAnswerId, Random random) {
        long safeAnswerId = AIChatMessageEngine.resolveSafeAnswerId(answerId, random);
        boolean shouldAddMessage = !hasCurrentMessage || activeAnswerId != safeAnswerId;
        boolean shouldRefreshExistingMessage = hasCurrentMessage && activeAnswerId == safeAnswerId;
        return new StartPlan(shouldAddMessage, shouldRefreshExistingMessage, updatedString, safeAnswerId, 0, -1);
    }

    /**
     * Plans a streamed server update that can arrive before or during the typewriter sequence.
     *
     * @param updatedString latest answer text received from the server
     * @param answerId backend answer id for the response
     * @param hasCurrentMessage whether the activity already has a visible backing AI message
     * @param activeAnswerId answer id currently associated with the active backing message
     * @param random random source used for local fallback ids
     * @return immutable plan describing message creation and typewriter update work
     */
    public static StreamUpdatePlan planStreamUpdate(String updatedString, long answerId, boolean hasCurrentMessage, long activeAnswerId, Random random) {
        return planStreamUpdate(updatedString, answerId, hasCurrentMessage, activeAnswerId, 0, random);
    }

    /**
     * Plans a streamed server update while preserving the chunk currently visible in the typewriter.
     *
     * @param updatedString latest answer text received from the server
     * @param answerId backend answer id for the response
     * @param hasCurrentMessage whether the activity already has a visible backing AI message
     * @param activeAnswerId answer id currently associated with the active backing message
     * @param activeChunkIndex chunk index currently being animated in the UI
     * @param random random source used for local fallback ids
     * @return immutable plan describing message creation and typewriter update work
     */
    public static StreamUpdatePlan planStreamUpdate(String updatedString, long answerId, boolean hasCurrentMessage, long activeAnswerId, int activeChunkIndex, Random random) {
        long safeAnswerId = AIChatMessageEngine.resolveSafeAnswerId(answerId, random);
        boolean shouldAddMessage = !isNullOrEmpty(updatedString) && (!hasCurrentMessage || activeAnswerId != safeAnswerId);
        ArrayList<String> messageChunks = null;
        String animatedText = updatedString;
        if (hasChunkSeparator(updatedString)) {
            messageChunks = AIChatMessageEngine.splitMessageChunks(updatedString);
            if (messageChunks != null && !messageChunks.isEmpty()) {
                animatedText = resolveAnimatedChunk(messageChunks, activeChunkIndex);
            }
        }
        return new StreamUpdatePlan(shouldAddMessage, safeAnswerId, safeAnswerId, updatedString, animatedText, messageChunks);
    }

    /**
     * Returns the chunk that should keep animating for the current streamed update.
     *
     * When the active chunk index is out of range due to late server/body reconciliation, we use the
     * latest available chunk instead of incorrectly rewinding the typewriter to the first paragraph.
     *
     * @param messageChunks latest known message chunks
     * @param activeChunkIndex chunk index currently rendered by the typewriter
     * @return chunk text that should remain bound to the active typewriter
     */
    private static String resolveAnimatedChunk(ArrayList<String> messageChunks, int activeChunkIndex) {
        if (messageChunks == null || messageChunks.isEmpty()) {
            return null;
        }
        if (activeChunkIndex < 0) {
            return messageChunks.get(0);
        }
        if (activeChunkIndex >= messageChunks.size()) {
            return messageChunks.get(messageChunks.size() - 1);
        }
        return messageChunks.get(activeChunkIndex);
    }

    /**
     * Returns whether the supplied server answer contains paragraph separators used by the typewriter flow.
     *
     * @param updatedString latest answer text received from the server
     * @return true when chunk splitting should be applied
     */
    public static boolean hasChunkSeparator(String updatedString) {
        if (isNullOrEmpty(updatedString)) {
            return false;
        }
        return updatedString.contains("\\n\\n")
                || updatedString.contains("\n\n")
                || updatedString.contains("<br><br>");
    }

    /**
     * Returns whether the supplied text is null or empty without depending on Android framework helpers.
     *
     * @param value value to check
     * @return true when the value is null or empty
     */
    private static boolean isNullOrEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * Describes how the activity should react to a typewriter-start callback.
     */
    public static final class StartPlan {
        private final boolean shouldAddMessage;
        private final boolean shouldRefreshExistingMessage;
        private final String messageBody;
        private final long activeAnswerId;
        private final int pendingChunkRetryCount;
        private final int lastHandledTypingIndex;

        /**
         * Captures the immutable result of planning a typewriter-start callback.
         *
         * @param shouldAddMessage whether a new visible AI bubble should be inserted
         * @param shouldRefreshExistingMessage whether the current bubble body should be updated
         * @param messageBody full message text that should back the current bubble
         * @param activeAnswerId safe active answer id for this stream
         * @param pendingChunkRetryCount retry counter reset to apply after a new stream starts
         * @param lastHandledTypingIndex last handled typewriter index reset value
         */
        public StartPlan(boolean shouldAddMessage, boolean shouldRefreshExistingMessage, String messageBody, long activeAnswerId, int pendingChunkRetryCount, int lastHandledTypingIndex) {
            this.shouldAddMessage = shouldAddMessage;
            this.shouldRefreshExistingMessage = shouldRefreshExistingMessage;
            this.messageBody = messageBody;
            this.activeAnswerId = activeAnswerId;
            this.pendingChunkRetryCount = pendingChunkRetryCount;
            this.lastHandledTypingIndex = lastHandledTypingIndex;
        }

        /**
         * Returns whether a new backing AI bubble should be inserted.
         *
         * @return true when the activity should add a message
         */
        public boolean shouldAddMessage() {
            return shouldAddMessage;
        }

        /**
         * Returns whether the existing backing bubble should be refreshed instead of recreated.
         *
         * @return true when the activity should update the current message body
         */
        public boolean shouldRefreshExistingMessage() {
            return shouldRefreshExistingMessage;
        }

        /**
         * Returns the full message body that should be associated with the current answer.
         *
         * @return message body from the server
         */
        public String messageBody() {
            return messageBody;
        }

        /**
         * Returns the safe answer id that should be associated with the current stream.
         *
         * @return resolved answer id
         */
        public long activeAnswerId() {
            return activeAnswerId;
        }

        /**
         * Returns the retry counter value that should be applied after stream start.
         *
         * @return pending chunk retry count
         */
        public int pendingChunkRetryCount() {
            return pendingChunkRetryCount;
        }

        /**
         * Returns the last handled typewriter index reset value.
         *
         * @return last handled typewriter index
         */
        public int lastHandledTypingIndex() {
            return lastHandledTypingIndex;
        }
    }

    /**
     * Describes how the activity should react to a non-start streamed server update.
     */
    public static final class StreamUpdatePlan {
        private final boolean shouldAddMessage;
        private final long messageChatId;
        private final long activeAnswerId;
        private final String messageBody;
        private final String animatedText;
        private final ArrayList<String> messageChunks;

        /**
         * Captures the immutable result of planning a streamed answer update.
         *
         * @param shouldAddMessage whether a new visible AI bubble should be inserted
         * @param messageChatId safe chat id for a message created by this update
         * @param activeAnswerId safe answer id that should be tracked for this stream
         * @param messageBody full latest answer text
         * @param animatedText text that should be applied to the active typewriter view
         * @param messageChunks optional chunk list derived from the latest answer text
         */
        public StreamUpdatePlan(boolean shouldAddMessage, long messageChatId, long activeAnswerId, String messageBody, String animatedText, ArrayList<String> messageChunks) {
            this.shouldAddMessage = shouldAddMessage;
            this.messageChatId = messageChatId;
            this.activeAnswerId = activeAnswerId;
            this.messageBody = messageBody;
            this.animatedText = animatedText;
            this.messageChunks = messageChunks;
        }

        /**
         * Returns whether the activity should create a backing message for this streamed update.
         *
         * @return true when a new bubble is required
         */
        public boolean shouldAddMessage() {
            return shouldAddMessage;
        }

        /**
         * Returns the safe chat id to associate with a newly created backing message.
         *
         * @return message chat id
         */
        public long messageChatId() {
            return messageChatId;
        }

        /**
         * Returns the safe answer id that should be tracked as active after this update.
         *
         * @return resolved active answer id
         */
        public long activeAnswerId() {
            return activeAnswerId;
        }

        /**
         * Returns the latest full message body received from the server.
         *
         * @return current answer body
         */
        public String messageBody() {
            return messageBody;
        }

        /**
         * Returns the text that should be pushed into the visible typewriter view.
         *
         * @return active typewriter text
         */
        public String animatedText() {
            return animatedText;
        }

        /**
         * Returns the optional chunk list produced from the latest answer text.
         *
         * @return chunk list, or null when the text is not chunked
         */
        public ArrayList<String> messageChunks() {
            return messageChunks;
        }
    }
}
