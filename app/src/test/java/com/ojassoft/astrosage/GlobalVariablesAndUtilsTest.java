package com.ojassoft.astrosage;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.provider.Settings;

import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowPackageManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Regression tests covering shared global constants and device/app identity helpers.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, manifest = Config.NONE, application = Application.class)
public class GlobalVariablesAndUtilsTest {

    /**
     * Verifies both utils helpers return the Android ID stored in settings.
     */
    @Test
    public void getMyAndroidId_returnsAndroidIdFromSettings() {
        Context context = RuntimeEnvironment.getApplication();
        String expectedAndroidId = "test-android-id";
        Settings.Secure.putString(context.getContentResolver(), Settings.Secure.ANDROID_ID, expectedAndroidId);

        assertEquals(expectedAndroidId, CUtils.getMyAndroidId(context));
        assertEquals(expectedAndroidId, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(context));
    }

    /**
     * Verifies both utils helpers return the signature hash code, not a hard-coded fallback.
     */
    @Test
    public void getApplicationSignatureHashCode_returnsSignatureHashCode() throws Exception {
        Context context = RuntimeEnvironment.getApplication();
        Signature signature = new Signature(new byte[]{1, 2, 3, 4});

        PackageInfo packageInfo = new PackageInfo();
        packageInfo.packageName = context.getPackageName();
        packageInfo.applicationInfo = new ApplicationInfo();
        packageInfo.applicationInfo.packageName = context.getPackageName();
        packageInfo.signatures = new Signature[]{signature};

        ShadowPackageManager shadowPackageManager = Shadows.shadowOf(context.getPackageManager());
        shadowPackageManager.installPackage(packageInfo);

        CUtils.signatureKey = "";
        com.ojassoft.astrosage.varta.utils.CUtils.signatureKey = "";
        String expectedHashCode = String.valueOf(signature.hashCode());

        assertEquals(expectedHashCode, CUtils.getApplicationSignatureHashCode(context));
        assertEquals(expectedHashCode, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(context));
        assertNotEquals("-1489918760", CUtils.getApplicationSignatureHashCode(context));
        assertNotEquals("-1489918760", com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(context));
    }

    /**
     * Confirms the varta live domain uses the shared base URL and the old constant is not used.
     */
    @Test
    public void liveDomain_usesSharedVartaBaseUrl() {
        String expectedLiveDomain = com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_BASE_URL + "sdk/";

        assertEquals(expectedLiveDomain, CGlobalVariables.LIVE_DOMAIN);
        assertNotEquals("https://talk.astrosage.com/sdk/", CGlobalVariables.LIVE_DOMAIN);
    }

    /**
     * Confirms Varta testing flag is set to live mode by default.
     */
    @Test
    public void valIsTestingAstro_defaultsToLive() {
        assertEquals("0", CGlobalVariables.VAL_IS_TESTING_ASTRO);
    }

    /**
     * Confirms AI base URLs remain on their intended production hosts.
     */
    @Test
    public void aiBaseUrls_useProductionHosts() {
        assertEquals("https://ai.astrosage.com", com.ojassoft.astrosage.utils.CGlobalVariables.AI_BASE_URL);
        assertEquals("https://ai2.astrosage.com", com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_BASE_URL);
    }
}
