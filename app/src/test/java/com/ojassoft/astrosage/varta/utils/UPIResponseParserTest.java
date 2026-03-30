package com.ojassoft.astrosage.varta.utils;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.ojassoft.astrosage.varta.model.UPIResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/** Robolectric checks for parsing UPI app callback responses from Intent payloads. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, manifest = Config.NONE, application = Application.class)
public class UPIResponseParserTest {

    /** Verifies standard success payload parsing on modern SDK path. */
    @Test
    @Config(sdk = Build.VERSION_CODES.P)
    public void parse_whenValidUpiResponse_returnsParsedFields() {
        Intent data = new Intent();
        data.putExtra("response", "Status=SUCCESS&txnId=TXN123&txnRef=REF123&responseCode=00");

        UPIResponse response = UPIResponseParser.parse(data);

        assertEquals("SUCCESS", response.status);
        assertEquals("TXN123", response.txnId);
        assertEquals("REF123", response.txnRef);
        assertEquals("00", response.responseCode);
    }

    /** Verifies legacy (pre-N) parsing still maps fields correctly. */
    @Test
    @Config(sdk = Build.VERSION_CODES.M)
    public void parse_whenPreNougatDevice_usesLegacyMapLookupAndStillParses() {
        Intent data = new Intent();
        data.putExtra("response", "status=SUCCESS&txnId=TXN555&txnRef=REF555&responseCode=00");

        UPIResponse response = UPIResponseParser.parse(data);

        assertEquals("SUCCESS", response.status);
        assertEquals("TXN555", response.txnId);
        assertEquals("REF555", response.txnRef);
        assertEquals("00", response.responseCode);
    }

    /** Verifies null Intent handling returns a safe failure response. */
    @Test
    public void parse_whenIntentIsNull_returnsFailure() {
        UPIResponse response = UPIResponseParser.parse(null);

        assertEquals("FAILURE", response.status);
        assertEquals(null, response.txnId);
        assertEquals(null, response.txnRef);
        assertEquals(null, response.responseCode);
    }
}
