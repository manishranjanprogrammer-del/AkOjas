package com.ojassoft.astrosage.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Regression checks for Visa card validation behavior in Validator. */
public class ValidatorTest {

    /** Ensures a known-valid Visa number is accepted. */
    @Test
    public void validVisa_passesValidation() {
        assertTrue(Validator.validate("4111111111111111", Validator.VISA));
    }

    /** Ensures an invalid Visa number is rejected. */
    @Test
    public void invalidVisa_failsValidation() {
        assertFalse(Validator.validate("4111111111111121", Validator.VISA));
    }
}
