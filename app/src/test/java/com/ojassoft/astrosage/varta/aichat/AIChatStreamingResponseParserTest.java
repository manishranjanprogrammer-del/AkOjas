package com.ojassoft.astrosage.varta.aichat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

/** Unit tests for the extracted AI chat streaming response parser. */
public class AIChatStreamingResponseParserTest {

    /** Verifies the first streamed chunk extracts the backend answer id and waits for more text. */
    @Test
    public void parseAccumulatedResponse_extractsAnswerIdFromFirstChunk() {
        AIChatStreamingResponseParser.StreamStep step =
                AIChatStreamingResponseParser.parseAccumulatedResponse("{\"ANSWERID\":\"77\",\"RESULT\":\"Hel", 1, 999L);

        assertEquals(AIChatStreamingResponseParser.Mode.ANSWER_ID_ONLY, step.mode());
        assertEquals(77L, step.answerId());
        assertNull(step.resultText());
        assertFalse(step.shouldPersistFinalMessage());
    }

    /** Verifies malformed first chunks still produce a safe fallback answer id instead of crashing the stream. */
    @Test
    public void parseAccumulatedResponse_fallsBackWhenFirstChunkCannotParseAnswerId() {
        AIChatStreamingResponseParser.StreamStep step =
                AIChatStreamingResponseParser.parseAccumulatedResponse("{\"RESULT\":\"Hello", 1, 1234L);

        assertEquals(AIChatStreamingResponseParser.Mode.ANSWER_ID_ONLY, step.mode());
        assertEquals(1234L, step.answerId());
        assertNull(step.resultText());
    }

    /** Verifies later complete JSON chunks expose the latest result and parsed metadata. */
    @Test
    public void parseAccumulatedResponse_readsCompleteJsonResultAndQuestionLimit() throws Exception {
        AIChatStreamingResponseParser.StreamStep step =
                AIChatStreamingResponseParser.parseAccumulatedResponse(
                        "{\"ANSWERID\":\"77\",\"RESULT\":\"Hello\",\"QUESLIMIT\":\"1\",\"QUESNUMBER\":\"5\"}",
                        2,
                        999L
                );

        assertEquals(AIChatStreamingResponseParser.Mode.COMPLETE_JSON, step.mode());
        assertEquals(77L, step.answerId());
        assertEquals("Hello", step.resultText());
        assertTrue(step.shouldPersistFinalMessage());
        JSONObject jsonObject = step.jsonObject();
        assertNotNull(jsonObject);
        assertEquals("1", jsonObject.getString("QUESLIMIT"));
        assertEquals("5", jsonObject.getString("QUESNUMBER"));
    }

    /** Verifies incomplete later chunks still expose the currently streamed result body. */
    @Test
    public void parseAccumulatedResponse_readsPartialStreamedResult() {
        AIChatStreamingResponseParser.StreamStep step =
                AIChatStreamingResponseParser.parseAccumulatedResponse("{\"ANSWERID\":\"77\",\"RESULT\":\"Hello world\"", 2, 999L);

        assertEquals(AIChatStreamingResponseParser.Mode.STREAM_TEXT, step.mode());
        assertEquals("Hello world", step.resultText());
        assertNull(step.jsonObject());
        assertTrue(step.shouldPersistFinalMessage());
    }

    /** Verifies non-meaningful streamed placeholders are not treated as final messages. */
    @Test
    public void shouldPersistFinalMessage_rejectsAnswerIdPlaceholder() {
        assertFalse(AIChatStreamingResponseParser.shouldPersistFinalMessage(null));
        assertFalse(AIChatStreamingResponseParser.shouldPersistFinalMessage(""));
        assertFalse(AIChatStreamingResponseParser.shouldPersistFinalMessage("ParseAnswerId"));
        assertTrue(AIChatStreamingResponseParser.shouldPersistFinalMessage("Actual answer"));
    }
}

