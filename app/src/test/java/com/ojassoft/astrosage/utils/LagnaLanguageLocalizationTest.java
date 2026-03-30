package com.ojassoft.astrosage.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import com.ojassoft.astrosage.constants.Constants;
import com.ojassoft.astrosage.jinterface.IConstants;
import com.ojassoft.astrosage.misc.Language;
import com.ojassoft.astrosage.ui.act.TestAstrosageKundliApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.DateFormat;
import java.util.Date;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Guards the lagna flow against language fallback regressions by keeping picker languages, locale
 * helpers, and lagna constants aligned for both current and future language additions.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestAstrosageKundliApplication.class)
public class LagnaLanguageLocalizationTest {
    private static final Set<Integer> EXPECTED_VISIBLE_LANGUAGE_PICKER_IDS = new TreeSet<>(Arrays.asList(
            CGlobalVariables.ENGLISH,
            CGlobalVariables.HINDI,
            CGlobalVariables.TAMIL,
            CGlobalVariables.BANGALI,
            CGlobalVariables.TELUGU,
            CGlobalVariables.MARATHI,
            CGlobalVariables.KANNADA,
            CGlobalVariables.GUJARATI,
            CGlobalVariables.MALAYALAM,
            CGlobalVariables.ASAMMESSE,
            CGlobalVariables.ODIA,
            CGlobalVariables.GERMAN,
            CGlobalVariables.FRENCH
    ));

    /**
     * Verifies the app language helper now returns locale keys for every lagna-supported language.
     */
    @Test
    public void appLanguageHelper_mapsAllLagnaLanguages() {
        assertEquals("or", CUtils.getLanguage(CGlobalVariables.ODIA));
        assertEquals("as", CUtils.getLanguage(CGlobalVariables.ASAMMESSE));
        assertEquals("es", CUtils.getLanguage(CGlobalVariables.SPANISH));
        assertEquals("zh", CUtils.getLanguage(CGlobalVariables.CHINESE));
        assertEquals("ja", CUtils.getLanguage(CGlobalVariables.JAPANESE));
        assertEquals("pt", CUtils.getLanguage(CGlobalVariables.PORTUGUESE));
        assertEquals("de", CUtils.getLanguage(CGlobalVariables.GERMAN));
        assertEquals("it", CUtils.getLanguage(CGlobalVariables.ITALIAN));
        assertEquals("fr", CUtils.getLanguage(CGlobalVariables.FRENCH));
    }

    /**
     * Verifies API-backed language requests stay aligned with every currently supported locale
     * code instead of silently falling back to English.
     */
    @Test
    public void apiLanguageCodeHelper_mapsAllSupportedLanguages() {
        assertEquals("en", CUtils.getLanguageCodeName(CGlobalVariables.ENGLISH));
        assertEquals("hi", CUtils.getLanguageCodeName(CGlobalVariables.HINDI));
        assertEquals("ta", CUtils.getLanguageCodeName(CGlobalVariables.TAMIL));
        assertEquals("mr", CUtils.getLanguageCodeName(CGlobalVariables.MARATHI));
        assertEquals("bn", CUtils.getLanguageCodeName(CGlobalVariables.BANGALI));
        assertEquals("ka", CUtils.getLanguageCodeName(CGlobalVariables.KANNADA));
        assertEquals("te", CUtils.getLanguageCodeName(CGlobalVariables.TELUGU));
        assertEquals("ml", CUtils.getLanguageCodeName(CGlobalVariables.MALAYALAM));
        assertEquals("gu", CUtils.getLanguageCodeName(CGlobalVariables.GUJARATI));
        assertEquals("or", CUtils.getLanguageCodeName(CGlobalVariables.ODIA));
        assertEquals("as", CUtils.getLanguageCodeName(CGlobalVariables.ASAMMESSE));
        assertEquals("es", CUtils.getLanguageCodeName(CGlobalVariables.SPANISH));
        assertEquals("zh", CUtils.getLanguageCodeName(CGlobalVariables.CHINESE));
        assertEquals("ja", CUtils.getLanguageCodeName(CGlobalVariables.JAPANESE));
        assertEquals("pt", CUtils.getLanguageCodeName(CGlobalVariables.PORTUGUESE));
        assertEquals("de", CUtils.getLanguageCodeName(CGlobalVariables.GERMAN));
        assertEquals("it", CUtils.getLanguageCodeName(CGlobalVariables.ITALIAN));
        assertEquals("fr", CUtils.getLanguageCodeName(CGlobalVariables.FRENCH));
    }

    /**
     * Verifies stale English remedies cached under Assamese/Odia keys are discarded so the
     * horoscope screen can refetch localized content.
     */
    @Test
    public void remediesCache_discardsStaleEnglishFallbackForOdiaAndAssamese() {
        Context context = ApplicationProvider.getApplicationContext();
        String todayKey = DateFormat.getDateInstance(DateFormat.MEDIUM, java.util.Locale.ENGLISH)
                .format(new Date());
        String staleEnglishRemedies = "[{\"Rashi\":\"Aries Horoscope\",\"Remedy\":\"English fallback\"}]";

//        assertTrue(CUtils.isLocalizedRemediesEnglishFallback(CGlobalVariables.ASAMMESSE, staleEnglishRemedies));
//        assertTrue(CUtils.isLocalizedRemediesEnglishFallback(CGlobalVariables.ODIA, staleEnglishRemedies));
        assertRemediesCacheInvalidated(context, CGlobalVariables.ASAMMESSE, todayKey, staleEnglishRemedies);
        assertRemediesCacheInvalidated(context, CGlobalVariables.ODIA, todayKey, staleEnglishRemedies);
    }

    /**
     * Verifies every language exposed in the app picker is explicitly supported by the lagna flow.
     */
    @Test
    public void languagePicker_and_lagnaFlowStayInSync() {
        Context context = ApplicationProvider.getApplicationContext();
        ArrayList<com.ojassoft.astrosage.varta.model.Language> pickerLanguages = CUtils.getLanguageList(context);
        Set<Integer> pickerLanguageIds = new TreeSet<>();
        PanchangUtil panchangUtil = new PanchangUtil();

        for (com.ojassoft.astrosage.varta.model.Language pickerLanguage : pickerLanguages) {
            int languageId = pickerLanguage.getLanguageId();
            String lagnaLanguageKey = CUtils.getLanguageKey(languageId);
            String panchangLanguageKey = CUtils.getLanguage(languageId);
            Language parsedLanguage = Language.getLanguage(lagnaLanguageKey);
            IConstants constants = panchangUtil.getIConstantsObj(parsedLanguage);

            pickerLanguageIds.add(languageId);

            assertEquals("Locale helpers must stay aligned for language id " + languageId,
                    lagnaLanguageKey, panchangLanguageKey);
            assertEquals("Language enum must recognize lagna locale key " + lagnaLanguageKey,
                    lagnaLanguageKey, parsedLanguage.name());
            if (languageId != CGlobalVariables.ENGLISH) {
                assertNotEquals("Non-English lagna flows must not fall back to default English constants for language id " + languageId,
                        Constants.class, constants.getClass());
            }
        }

        assertEquals("If a language is added to the visible app picker, lagna flow mappings and this guardrail test must be updated too.",
                EXPECTED_VISIBLE_LANGUAGE_PICKER_IDS, pickerLanguageIds);
    }

    /**
     * Verifies every picker language has a dedicated notification topic and is tracked as a language topic.
     */
    @Test
    public void languagePicker_and_topicSubscriptionFlowStayInSync() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        ArrayList<com.ojassoft.astrosage.varta.model.Language> pickerLanguages = CUtils.getLanguageList(context);
        Method isLanguageNotificationTopic = CUtils.class.getDeclaredMethod("isLanguageNotificationTopic", String.class);
        isLanguageNotificationTopic.setAccessible(true);
        Set<Integer> pickerLanguageIds = new TreeSet<>();
        Set<Integer> retryRegistryLanguageIds = new TreeSet<>();

        for (int languageCode : CGlobalVariables.langArr) {
            retryRegistryLanguageIds.add(languageCode);
        }

        for (com.ojassoft.astrosage.varta.model.Language pickerLanguage : pickerLanguages) {
            int languageId = pickerLanguage.getLanguageId();
            String topicName = CUtils.getTopicName(languageId);
            boolean isLanguageTopic = (boolean) isLanguageNotificationTopic.invoke(null, topicName);

            pickerLanguageIds.add(languageId);

            assertNotEquals("Every picker language must have a dedicated topic mapping, not a fallback.",
                    "", topicName);
            if (languageId != CGlobalVariables.ENGLISH) {
                assertNotEquals("Non-English picker languages must not fall back to the English topic.",
                        CGlobalVariables.TOPIC_ENGLISH, topicName);
            }
            assertEquals("Language topics must be classified into the language retry bucket.", true, isLanguageTopic);
        }

        assertEquals("If a language is added to the visible app picker, this guardrail test must be updated too.",
                EXPECTED_VISIBLE_LANGUAGE_PICKER_IDS, pickerLanguageIds);
        assertEquals("Every visible picker language must be present in the language-topic retry registry.",
                true, retryRegistryLanguageIds.containsAll(pickerLanguageIds));
    }

    /**
     * Verifies extended lagna languages resolve localized zodiac and nature labels instead of English fallbacks.
     */
    @Test
    public void panchangUtil_returnsLocalizedLagnaLabelsForExtendedLanguages() {
        PanchangUtil panchangUtil = new PanchangUtil();

        assertLocalizedLagna(panchangUtil, "or", "ମେଷ", "ଚର");
        assertLocalizedLagna(panchangUtil, "as", "মেষ", "চৰ");
        assertLocalizedLagna(panchangUtil, "es", "Aries", "Cardinal");
        assertLocalizedLagna(panchangUtil, "zh", "白羊座", "本位");
        assertLocalizedLagna(panchangUtil, "ja", "牡羊座", "活動宮");
        assertLocalizedLagna(panchangUtil, "pt", "Áries", "Cardinal");
        assertLocalizedLagna(panchangUtil, "de", "Widder", "Kardinal");
        assertLocalizedLagna(panchangUtil, "it", "Ariete", "Cardinale");
        assertLocalizedLagna(panchangUtil, "fr", "Bélier", "Cardinal");
    }

    /**
     * Asserts the first lagna and its modality for a single locale key.
     */
    private static void assertLocalizedLagna(PanchangUtil panchangUtil, String languageKey,
                                             String expectedLagna, String expectedNature) {
        IConstants constants = panchangUtil.getIConstantsObj(Language.getLanguage(languageKey));

        assertEquals(expectedLagna, constants.getLagna(1));
        assertEquals(expectedNature, constants.getLagnaNature(1));
        assertEquals(expectedLagna, constants.getRashi(1));
    }

    /**
     * Seeds stale remedies JSON for a locale and verifies the getter clears it instead of serving
     * the old English payload.
     */
    private static void assertRemediesCacheInvalidated(Context context, int languageCode, String todayKey,
                                                       String staleEnglishRemedies) {
        SharedPreferences sharedPreferences = CUtils.getTodaySharedPreferenceAccordingLang(context, languageCode);
        sharedPreferences.edit()
                .putString(CGlobalVariables.DAILY_REMEDIES_PREF_KEY, todayKey)
                .putString("Remedies", staleEnglishRemedies)
                .commit();

        assertEquals("", CUtils.getTodayRemedies(languageCode, context));
        assertEquals("", sharedPreferences.getString(CGlobalVariables.DAILY_REMEDIES_PREF_KEY, ""));
        assertEquals("", sharedPreferences.getString("Remedies", ""));
        assertTrue(sharedPreferences.contains("Remedies"));
    }
}
