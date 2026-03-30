package com.ojassoft.astrosage.varta.utils;

import android.app.Application;
import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Business-rule checks for AI Pass plan-to-coupon mapping. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, manifest = Config.NONE, application = Application.class)
public class CUtilsBusinessRulesTest {

    /** Verifies known plan service IDs return the expected coupon code. */
    @Test
    public void aiPassCouponMapping_returnsExpectedCouponForEachPlan() {
        assertEquals(CGlobalVariables.AIPASS1, CUtils.getAIPassCouponByPlanServiceId(CGlobalVariables.PLAN_SERVICE_ID_259));
        assertEquals(CGlobalVariables.AIPASS2, CUtils.getAIPassCouponByPlanServiceId(CGlobalVariables.PLAN_SERVICE_ID_262));
        assertEquals(CGlobalVariables.AIPASS3, CUtils.getAIPassCouponByPlanServiceId(CGlobalVariables.PLAN_SERVICE_ID_260));
    }

    /** Verifies unknown or null plan IDs safely return an empty coupon value. */
    @Test
    public void aiPassCouponMapping_returnsEmptyForUnknownOrNullPlan() {
        assertEquals("", CUtils.getAIPassCouponByPlanServiceId("999"));
        assertEquals("", CUtils.getAIPassCouponByPlanServiceId(null));
    }

    /** Verifies the app signature method returns the cached signature key (not the old hardcoded fallback). */
    @Test
    public void getApplicationSignatureHashCode_returnsCachedSignatureKey() throws Exception {
        Field signatureKeyField = CUtils.class.getDeclaredField("signatureKey");
        signatureKeyField.setAccessible(true);
        Object originalValue = signatureKeyField.get(null);
        try {
            signatureKeyField.set(null, "TEST_SIGNATURE_KEY");
            assertEquals("TEST_SIGNATURE_KEY", CUtils.getApplicationSignatureHashCode(null));
        } finally {
            signatureKeyField.set(null, originalValue);
        }
    }

    /** Verifies app-level `utils.CUtils` keeps hardcoded fallback commented and returns `signatureKey`. */
    @Test
    public void appCUtils_getApplicationSignatureHashCode_usesSignatureKeyReturn() throws Exception {
        Path sourcePath = resolveAppCUtilsSourcePath();
        String source = new String(Files.readAllBytes(sourcePath), StandardCharsets.UTF_8);

        assertTrue(source.matches("(?s).*TextUtils\\.isEmpty\\(signatureKey\\) \\? \\\"-1489918760\\\" : signatureKey;.*"));
        assertTrue(!source.matches("(?sm).*^\\s*return \\\"-1489918760\\\";\\s*$.*"));
    }

    /** Resolves `com.ojassoft.astrosage.utils.CUtils` source path across common Gradle test working directories. */
    private Path resolveAppCUtilsSourcePath() {
        Path projectRelative = Paths.get("app", "src", "main", "java", "com", "ojassoft", "astrosage", "utils", "CUtils.java");
        if (Files.exists(projectRelative)) {
            return projectRelative;
        }
        Path moduleRelative = Paths.get("src", "main", "java", "com", "ojassoft", "astrosage", "utils", "CUtils.java");
        if (Files.exists(moduleRelative)) {
            return moduleRelative;
        }
        return projectRelative;
    }
}
