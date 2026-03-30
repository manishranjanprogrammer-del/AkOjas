package com.ojassoft.astrosage.varta.utils;

/**
 * Calculates safe typewriter cursor jumps for strings that contain HTML tags or entities.
 *
 * The AI chat typewriter animates HTML-formatted chunks. Advancing one raw character at a time
 * through markup makes visible text appear stalled, so this helper skips over complete tags and
 * entities in one cursor step while keeping plain text character-by-character.
 */
public final class TypeWriterRenderCursor {

    /**
     * Prevents instantiation because this helper exposes stateless cursor utilities only.
     */
    private TypeWriterRenderCursor() {
    }

    /**
     * Advances the exclusive end index to the next renderable boundary for the supplied text.
     *
     * Plain text advances by one character. If the next segment starts with an HTML tag or entity,
     * the index jumps to the end of that segment so the formatted visible text keeps moving.
     *
     * @param text full text being animated
     * @param currentExclusiveEnd current exclusive end index already rendered
     * @return next exclusive end index, clamped to the text length
     */
    public static int advanceToNextRenderableIndex(String text, int currentExclusiveEnd) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        if (currentExclusiveEnd < 0) {
            currentExclusiveEnd = 0;
        }
        if (currentExclusiveEnd >= text.length()) {
            return text.length();
        }

        char nextChar = text.charAt(currentExclusiveEnd);
        if (nextChar == '<') {
            int tagEnd = text.indexOf('>', currentExclusiveEnd);
            if (tagEnd >= 0) {
                return tagEnd + 1;
            }
        } else if (nextChar == '&') {
            int entityEnd = text.indexOf(';', currentExclusiveEnd);
            if (entityEnd >= 0) {
                return entityEnd + 1;
            }
        }

        return Math.min(text.length(), currentExclusiveEnd + 1);
    }
}
