package com.ojassoft.astrosage.varta.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for HTML-aware typewriter cursor movement.
 */
public class TypeWriterRenderCursorTest {

    /**
     * Verifies plain text advances one character at a time.
     */
    @Test
    public void advanceToNextRenderableIndex_advancesPlainTextByOneCharacter() {
        assertEquals(1, TypeWriterRenderCursor.advanceToNextRenderableIndex("Hello", 0));
        assertEquals(2, TypeWriterRenderCursor.advanceToNextRenderableIndex("Hello", 1));
    }

    /**
     * Verifies HTML tags are consumed in one cursor step instead of stalling visible text.
     */
    @Test
    public void advanceToNextRenderableIndex_skipsWholeHtmlTag() {
        String text = "<strong>Hello</strong>";

        assertEquals(8, TypeWriterRenderCursor.advanceToNextRenderableIndex(text, 0));
        assertEquals(9, TypeWriterRenderCursor.advanceToNextRenderableIndex(text, 8));
    }

    /**
     * Verifies HTML entities are consumed in one cursor step instead of several invisible ticks.
     */
    @Test
    public void advanceToNextRenderableIndex_skipsWholeHtmlEntity() {
        String text = "&amp; value";

        assertEquals(5, TypeWriterRenderCursor.advanceToNextRenderableIndex(text, 0));
        assertEquals(6, TypeWriterRenderCursor.advanceToNextRenderableIndex(text, 5));
    }

    /**
     * Verifies the cursor clamps to the text length when the full text is already rendered.
     */
    @Test
    public void advanceToNextRenderableIndex_clampsAtTextLength() {
        assertEquals(5, TypeWriterRenderCursor.advanceToNextRenderableIndex("Hello", 5));
        assertEquals(0, TypeWriterRenderCursor.advanceToNextRenderableIndex("", 0));
    }
}
