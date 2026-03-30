package com.ojassoft.astrosage.varta.ui.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * Regression tests for intermittent AI chat chunk issues seen on some devices during streaming.
 *
 * These tests focus on active-chunk selection because later server updates must keep extending the
 * currently visible chunk instead of resetting the typewriter back to the first paragraph.
 */
public class AIChatIntermittentChunkRegressionTest {

    /**
     * Verifies streamed updates keep extending the currently active later chunk instead of the first chunk.
     */
    @Test
    public void planStreamUpdate_usesActiveLaterChunkForAnimatedText() {
        AIChatStreamCoordinator.StreamUpdatePlan plan =
                AIChatStreamCoordinator.planStreamUpdate(
                        "Intro\\n\\nSecond paragraph grows",
                        44L,
                        true,
                        44L,
                        1,
                        new Random(0)
                );

        assertFalse(plan.shouldAddMessage());
        assertEquals(Arrays.asList("Intro\n", "Second paragraph grows\n"), plan.messageChunks());
        assertEquals("Second paragraph grows\n", plan.animatedText());
        assertEquals("Intro\\n\\nSecond paragraph grows", plan.messageBody());
    }

    /**
     * Verifies out-of-range active chunk indexes fall back to the latest available chunk, not the first one.
     */
    @Test
    public void planStreamUpdate_fallsBackToLatestChunkWhenActiveIndexIsOutOfRange() {
        AIChatStreamCoordinator.StreamUpdatePlan plan =
                AIChatStreamCoordinator.planStreamUpdate(
                        "Intro\\n\\nClosing line",
                        55L,
                        true,
                        55L,
                        5,
                        new Random(0)
                );

        assertEquals(Arrays.asList("Intro\n", "Closing line\n"), plan.messageChunks());
        assertEquals("Closing line\n", plan.animatedText());
    }

    /**
     * Verifies plain single-chunk answers ignore the active chunk index and keep the full text animated.
     */
    @Test
    public void planStreamUpdate_keepsSingleChunkTextWhenNoSeparatorsExist() {
        AIChatStreamCoordinator.StreamUpdatePlan plan =
                AIChatStreamCoordinator.planStreamUpdate(
                        "Single line answer grows",
                        66L,
                        true,
                        66L,
                        3,
                        new Random(0)
                );

        assertNull(plan.messageChunks());
        assertEquals("Single line answer grows", plan.animatedText());
    }
}
