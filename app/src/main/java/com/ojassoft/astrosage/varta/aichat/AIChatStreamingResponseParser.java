package com.ojassoft.astrosage.varta.aichat;

import org.json.JSONObject;

/**
 * Parses accumulated AI streaming responses into deterministic steps for the chat UI.
 *
 * This parser keeps the low-level response-shape rules out of `GetAnswerFromServer` so the
 * network caller only has to react to typed parsing results.
 */
public final class AIChatStreamingResponseParser {

    /** Placeholder value historically used while waiting for the first real streamed answer text. */
    public static final String ANSWER_ID_PLACEHOLDER = "ParseAnswerId";

    /**
     * Prevents instantiation because the parser exposes stateless utility methods only.
     */
    private AIChatStreamingResponseParser() {
    }

    /**
     * Parses the current accumulated server response into a stream step that the caller can act on.
     *
     * @param accumulatedResponse latest raw response built from the streamed lines
     * @param chunkCount 1-based chunk counter for the current stream
     * @param fallbackAnswerId fallback id used when the backend answer id cannot be parsed
     * @return immutable parsing result for the current accumulated response
     */
    public static StreamStep parseAccumulatedResponse(String accumulatedResponse, int chunkCount, long fallbackAnswerId) {
        if (accumulatedResponse == null || accumulatedResponse.isEmpty()) {
            return new StreamStep(Mode.NONE, fallbackAnswerId, null, null);
        }

        if (chunkCount == 1) {
            if (isCompleteJson(accumulatedResponse)) {
                return fromCompleteJson(accumulatedResponse, fallbackAnswerId);
            }
            return new StreamStep(Mode.ANSWER_ID_ONLY, parseAnswerId(accumulatedResponse.replace("null", ""), fallbackAnswerId), null, null);
        }

        if (isCompleteJson(accumulatedResponse)) {
            return fromCompleteJson(accumulatedResponse, fallbackAnswerId);
        }

        String resultText = extractPartialResult(accumulatedResponse);
        return new StreamStep(Mode.STREAM_TEXT, fallbackAnswerId, resultText, null);
    }

    /**
     * Returns whether the provided streamed text should be persisted as a final visible answer.
     *
     * @param streamedResult latest parsed result text
     * @return true when the streamed result represents a meaningful final message
     */
    public static boolean shouldPersistFinalMessage(String streamedResult) {
        return streamedResult != null
                && !streamedResult.isEmpty()
                && !ANSWER_ID_PLACEHOLDER.equalsIgnoreCase(streamedResult);
    }

    /**
     * Returns whether the response currently represents a complete JSON payload.
     *
     * @param accumulatedResponse accumulated raw response
     * @return true when the response starts and ends like a full JSON object
     */
    public static boolean isCompleteJson(String accumulatedResponse) {
        return accumulatedResponse != null
                && !accumulatedResponse.isEmpty()
                && accumulatedResponse.charAt(0) == '{'
                && accumulatedResponse.charAt(accumulatedResponse.length() - 1) == '}';
    }

    /**
     * Parses a full JSON response into a stream step.
     *
     * @param accumulatedResponse accumulated raw response
     * @param fallbackAnswerId fallback id used when the backend answer id cannot be parsed
     * @return immutable parsing result for the full JSON payload
     */
    private static StreamStep fromCompleteJson(String accumulatedResponse, long fallbackAnswerId) {
        try {
            JSONObject jsonObject = new JSONObject(accumulatedResponse);
            long answerId = parseAnswerId(jsonObject.optString("ANSWERID", ""), fallbackAnswerId);
            return new StreamStep(Mode.COMPLETE_JSON, answerId, jsonObject.optString("RESULT", ""), jsonObject);
        } catch (Exception ignored) {
            return new StreamStep(Mode.NONE, fallbackAnswerId, null, null);
        }
    }

    /**
     * Parses an answer id from either a full JSON snippet or a bare answer-id string.
     *
     * @param source source text containing the answer id
     * @param fallbackAnswerId fallback id used when parsing fails
     * @return parsed backend answer id, or the fallback when unavailable
     */
    private static long parseAnswerId(String source, long fallbackAnswerId) {
        try {
            String answerIdSource = source;
            if (answerIdSource.contains("\"ANSWERID\":\"")) {
                answerIdSource = answerIdSource.substring(answerIdSource.indexOf("\"ANSWERID\":\"") + 12);
                answerIdSource = answerIdSource.substring(0, answerIdSource.indexOf("\""));
            }
            return Long.parseLong(answerIdSource);
        } catch (Exception ignored) {
            return fallbackAnswerId;
        }
    }

    /**
     * Extracts the streamed `RESULT` body from an incomplete JSON payload.
     *
     * @param accumulatedResponse accumulated raw response
     * @return partial result text when present, otherwise null
     */
    private static String extractPartialResult(String accumulatedResponse) {
        try {
            if (!accumulatedResponse.contains("\"RESULT\":\"")) {
                return null;
            }
            return accumulatedResponse.substring(accumulatedResponse.indexOf("\"RESULT\":\"") + 10, accumulatedResponse.length() - 1);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Enumerates the high-level parsing states used by the streaming response handler.
     */
    public enum Mode {
        NONE,
        ANSWER_ID_ONLY,
        STREAM_TEXT,
        COMPLETE_JSON
    }

    /**
     * Immutable parsing result for one accumulated streaming response state.
     */
    public static final class StreamStep {
        private final Mode mode;
        private final long answerId;
        private final String resultText;
        private final JSONObject jsonObject;

        /**
         * Captures the parsed response state for one accumulated stream snapshot.
         *
         * @param mode parsing mode that tells the caller how to react
         * @param answerId safe answer id parsed from the payload or fallback
         * @param resultText current streamed result text, if available
         * @param jsonObject parsed full JSON payload when available
         */
        public StreamStep(Mode mode, long answerId, String resultText, JSONObject jsonObject) {
            this.mode = mode;
            this.answerId = answerId;
            this.resultText = resultText;
            this.jsonObject = jsonObject;
        }

        /**
         * Returns the parsing mode for the current stream snapshot.
         *
         * @return parsing mode
         */
        public Mode mode() {
            return mode;
        }

        /**
         * Returns the parsed or fallback answer id.
         *
         * @return answer id
         */
        public long answerId() {
            return answerId;
        }

        /**
         * Returns the latest available streamed result text.
         *
         * @return streamed result text, or null when unavailable
         */
        public String resultText() {
            return resultText;
        }

        /**
         * Returns the parsed JSON payload when the response is complete.
         *
         * @return parsed JSON object, or null for incomplete streamed states
         */
        public JSONObject jsonObject() {
            return jsonObject;
        }

        /**
         * Returns whether the current result text should be persisted as a final AI message.
         *
         * @return true when the result text is meaningful final content
         */
        public boolean shouldPersistFinalMessage() {
            return AIChatStreamingResponseParser.shouldPersistFinalMessage(resultText);
        }
    }
}
