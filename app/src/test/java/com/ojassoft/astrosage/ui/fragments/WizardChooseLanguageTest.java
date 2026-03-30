package com.ojassoft.astrosage.ui.fragments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.content.SharedPreferences;
import android.widget.Button;

import androidx.test.core.app.ApplicationProvider;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActWizardScreens;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.TestAstrosageKundliApplication;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.model.ModelManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Verifies that the wizard’s language pickers persist preferences, refresh caches, and launch the correct screens
 * in both the first-install and in-app language-change flows.
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.P,
        application = TestAstrosageKundliApplication.class,
        shadows = {WizardChooseLanguageTest.ShadowModelManager.class}
)
public class WizardChooseLanguageTest {

    @Before
    public void setUp() {
        ActWizardScreens.isFromSplash = false;
        if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setApplicationId("1:000:android:test")
                    .setApiKey("test-api-key")
                    .setDatabaseUrl("https://test.firebaseio.com")
                    .setProjectId("test-project")
                    .build();
            FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext(), options);
        }
        clearSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME);
        clearSharedPreferences(CGlobalVariables.APP_PREFS_NAME);
        clearCaches();
    }

    @Test
    public void chooseLanguage_firstLaunch_updatesPrefsAndStartsLogin() throws Exception {
        ActWizardScreens.isFromSplash = true;
        ActivityController<ActWizardScreens> controller =
                Robolectric.buildActivity(ActWizardScreens.class, new Intent(ApplicationProvider.getApplicationContext(), ActWizardScreens.class)
                        .putExtra("isFromSplash", true));
        ActWizardScreens activity = controller.create().start().resume().get();
        WizardChooseLanguage fragment = attachFragment(activity);
        setLanguageChoice(fragment, CGlobalVariables.HINDI);

        Button continueButton = fragment.requireView().findViewById(R.id.btnContinue);
        continueButton.performClick();

        SharedPreferences languagePrefs = activity.getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        assertEquals(CGlobalVariables.HINDI, languagePrefs.getInt(CGlobalVariables.APP_PREFS_AppLanguage, -1));

        SharedPreferences chartPrefs = activity.getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        assertEquals(CGlobalVariables.CHART_NORTH_STYLE, chartPrefs.getInt(CGlobalVariables.APP_PREFS_ChartStyle, -1));

        AstrosageKundliApplication application = (AstrosageKundliApplication) activity.getApplication();
        assertEquals(CGlobalVariables.HINDI, application.getLanguageCode());

        ShadowActivity shadow = Shadows.shadowOf(activity);
        Intent started = shadow.getNextStartedActivity();
        assertNotNull(started);
        assertEquals(LoginSignUpActivity.class.getName(), started.getComponent().getClassName());
    }

    @Test
    public void chooseLanguage_homeFlow_clearsCachesAndNavigatesHome() throws Exception {
        ActWizardScreens.isFromSplash = false;
        ActivityController<ActWizardScreens> controller =
                Robolectric.buildActivity(ActWizardScreens.class, new Intent(ApplicationProvider.getApplicationContext(), ActWizardScreens.class)
                        .putExtra("isFromSplash", false));
        ActWizardScreens activity = controller.create().start().resume().get();
        WizardChooseLanguage fragment = attachFragment(activity);
        populateAstrologerCaches();
        setLanguageChoice(fragment, CGlobalVariables.BANGALI);

        Button continueButton = fragment.requireView().findViewById(R.id.btnContinue);
        continueButton.performClick();

        SharedPreferences languagePrefs = activity.getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        assertEquals(CGlobalVariables.BANGALI, languagePrefs.getInt(CGlobalVariables.APP_PREFS_AppLanguage, -1));

        SharedPreferences chartPrefs = activity.getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        assertEquals(CGlobalVariables.CHART_EAST_STYLE, chartPrefs.getInt(CGlobalVariables.APP_PREFS_ChartStyle, -1));

        assertTrue(CUtils.allAstrologersArrayList.isEmpty());
        assertTrue(CUtils.aiAstrologersArrayList.isEmpty());

        ShadowActivity shadow = Shadows.shadowOf(activity);
        Intent started = shadow.getNextStartedActivity();
        assertNotNull(started);
        assertEquals(ActAppModule.class.getName(), started.getComponent().getClassName());
        assertTrue((started.getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0);
    }

    /** Attaches the WizardChooseLanguage fragment to the provided host activity. */
    private static WizardChooseLanguage attachFragment(ActWizardScreens activity) {
        WizardChooseLanguage fragment = new WizardChooseLanguage();
        activity.getSupportFragmentManager().beginTransaction().add(fragment, "wizard").commitNow();
        return fragment;
    }

    /** Overrides the fragment's tracking field so tests can simulate a selection. */
    private static void setLanguageChoice(WizardChooseLanguage fragment, int languageCode) throws Exception {
        Field field = WizardChooseLanguage.class.getDeclaredField("newLanguageIndexVar");
        field.setAccessible(true);
        field.setInt(fragment, languageCode);
    }

    /** Wipes the named preference store so each test starts from clean state. */
    private static void clearSharedPreferences(String preferenceName) {
        SharedPreferences prefs = ApplicationProvider.getApplicationContext().getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    /** Clears the static astrologer caches used by home-screen flows. */
    private static void clearCaches() {
        CUtils.allAstrologersArrayList.clear();
        CUtils.aiAstrologersArrayList.clear();
    }

    /** Seeds the astrologer caches so we can assert that the language change path empties them. */
    private static void populateAstrologerCaches() {
        CUtils.allAstrologersArrayList.clear();
        CUtils.aiAstrologersArrayList.clear();
        CUtils.allAstrologersArrayList.add(new AstrologerDetailBean());
        CUtils.aiAstrologersArrayList.add(new AstrologerDetailBean());
    }

    @Implements(ModelManager.class)
    public static class ShadowModelManager {
        @Implementation
        public void saveUserGCMRegistrationInformationOnOjasServer(Context context, String regid, int languageCode, String loginId) {
            // No-op shadow to keep tests deterministic.
        }
    }
}
