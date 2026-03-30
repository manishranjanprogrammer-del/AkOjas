package com.ojassoft.astrosage.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.res.Resources;

import androidx.test.core.app.ApplicationProvider;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.Constants;
import com.ojassoft.astrosage.constants.LocalizedLagnaConstants;
import com.ojassoft.astrosage.jinterface.IConstants;
import com.ojassoft.astrosage.misc.Language;
import com.ojassoft.astrosage.ui.act.TestAstrosageKundliApplication;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * End-to-end guardrail for new language onboarding across picker, locale helpers, lagna flow,
 * topic subscription, and app-side plan metadata.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestAstrosageKundliApplication.class)
public class LanguageAdditionEndToEndGuardrailTest {
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
     * Verifies every visible picker language is fully wired for the currently documented language
     * addition flow and fails if a newly added language misses any mandatory integration point.
     */
    @Test
    public void visiblePickerLanguages_areReadyForEndToEndLanguageAdditionFlow() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        Resources englishResources = com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources(context, "en");
        ArrayList<com.ojassoft.astrosage.varta.model.Language> pickerLanguages = CUtils.getLanguageList(context);
        PanchangUtil panchangUtil = new PanchangUtil();
        Method isLanguageNotificationTopic = CUtils.class.getDeclaredMethod("isLanguageNotificationTopic", String.class);
        isLanguageNotificationTopic.setAccessible(true);
        Set<Integer> pickerLanguageIds = new TreeSet<>();
        Set<Integer> retryRegistryLanguageIds = new TreeSet<>();
        String englishBasicPlan = PlanLocalizationUtil.getBasicPlanFeatureJson(CGlobalVariables.ENGLISH);
        String englishPlatinumPlan = PlanLocalizationUtil.getPlatinumPlanFeatureJson(CGlobalVariables.ENGLISH);

        for (int languageCode : CGlobalVariables.langArr) {
            retryRegistryLanguageIds.add(languageCode);
        }

        for (com.ojassoft.astrosage.varta.model.Language pickerLanguage : pickerLanguages) {
            int languageId = pickerLanguage.getLanguageId();
            String localeKey = CUtils.getLanguageKey(languageId);
            String panchangLanguageKey = CUtils.getLanguage(languageId);
            String topicName = CUtils.getTopicName(languageId);
            Resources localizedResources = com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources(context, localeKey);
            Language parsedLanguage = Language.getLanguage(localeKey);
            IConstants constants = panchangUtil.getIConstantsObj(parsedLanguage);
            String basicPlanJson = PlanLocalizationUtil.getBasicPlanFeatureJson(languageId);
            String platinumPlanJson = PlanLocalizationUtil.getPlatinumPlanFeatureJson(languageId);
            int languageNameResId = getLanguageNameResId(languageId);

            pickerLanguageIds.add(languageId);

            assertNotEquals("", localeKey);
            assertEquals("Lagṇa and panchang locale helpers must stay aligned for language id " + languageId,
                    localeKey, panchangLanguageKey);
            assertEquals("Language enum must resolve the locale key used by the lagna flow.",
                    localeKey, parsedLanguage.name());
            assertNotEquals("", topicName);
            assertTrue("Every picker language must be treated as a language topic.",
                    (boolean) isLanguageNotificationTopic.invoke(null, topicName));
            assertTrue("Every picker language must be present in the language-topic retry registry.",
                    retryRegistryLanguageIds.contains(languageId));

            if (languageId != CGlobalVariables.ENGLISH) {
                assertNotEquals("Non-English lagna flow must not fall back to default English constants.",
                        Constants.class, constants.getClass());
            }
            if (languageId == CGlobalVariables.ODIA || languageId == CGlobalVariables.ASAMMESSE) {
                assertNotEquals("Odia and Assamese Panchang flows must not use the lagna-only constants wrapper.",
                        LocalizedLagnaConstants.class, constants.getClass());
            }

            assertPlanJsonShape(basicPlanJson, 5);
            assertPlanJsonShape(platinumPlanJson, 4);
            assertLocalizedKundliAiPlusStrings(localizedResources);
            assertNotEquals("", localizedResources.getString(languageNameResId).trim());

            if (languageId != CGlobalVariables.ENGLISH) {
                assertNotEquals("Non-English basic plan JSON must not silently reuse English.",
                        englishBasicPlan, basicPlanJson);
                assertNotEquals("Non-English KundliAI+/Dhruv plan JSON must not silently reuse English.",
                        englishPlatinumPlan, platinumPlanJson);
                assertNotEquals("New language additions must translate the KundliAI+ question row.",
                        englishResources.getString(R.string.num_ques_a_day_txt),
                        localizedResources.getString(R.string.num_ques_a_day_txt));
                assertNotEquals("New language additions must translate the KundliAI+ helper description.",
                        englishResources.getString(R.string.kundli_ai_plus_question_limit_description),
                        localizedResources.getString(R.string.kundli_ai_plus_question_limit_description));
                assertNotEquals("New language additions must translate the plan CTA text.",
                        englishResources.getString(R.string.button_text_with_price),
                        localizedResources.getString(R.string.button_text_with_price));
            }
        }

        assertEquals("If a new language is added to the visible picker, this guardrail test must be updated too.",
                EXPECTED_VISIBLE_LANGUAGE_PICKER_IDS, pickerLanguageIds);
    }

    /**
     * Verifies Astroshop_Frag static category labels remain Hindi-only for Hindi and use base
     * English fallback for every other visible non-English locale.
     */
    @Test
    public void visiblePickerLanguages_keepAstroshopStaticCategoryFallbackPolicy() {
        Context context = ApplicationProvider.getApplicationContext();
        Resources englishResources = com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources(context, "en");
        ArrayList<com.ojassoft.astrosage.varta.model.Language> pickerLanguages = CUtils.getLanguageList(context);

        for (com.ojassoft.astrosage.varta.model.Language pickerLanguage : pickerLanguages) {
            int languageId = pickerLanguage.getLanguageId();
            Resources localizedResources = com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources(
                    context,
                    CUtils.getLanguageKey(languageId)
            );

            assertAstroshopStaticCategoryFallbackStrings(languageId, englishResources, localizedResources);
        }
    }

    /**
     * Verifies the minimal KundliAI+ fragment strings remain present for plan localization flows.
     */
    private static void assertLocalizedKundliAiPlusStrings(Resources localizedResources) {
        assertNotEquals("", localizedResources.getString(R.string.num_ques_a_day_txt).trim());
        assertNotEquals("", localizedResources.getString(R.string.kundli_ai_plus_question_limit_description).trim());
        assertNotEquals("", localizedResources.getString(R.string.subscribe_for_text).trim());
        assertNotEquals("", localizedResources.getString(R.string.button_text_with_price).trim());
    }

    /**
     * Verifies Astroshop_Frag static category labels stay Hindi-only for Hindi and fall back to
     * base English for every other non-English locale.
     */
    private static void assertAstroshopStaticCategoryFallbackStrings(
            int languageId,
            Resources englishResources,
            Resources localizedResources
    ) {
        int[] astroshopStaticResIds = new int[]{
                R.string.astro_shop_module_brihat,
                R.string.astro_shop_astro_services,
                R.string.astro_shop_dhruv,
                R.string.astro_shop_platinum_plan,
                R.string.astro_shop_module_cogni_astro
        };

        for (int resId : astroshopStaticResIds) {
            String englishValue = englishResources.getString(resId);
            String localizedValue = localizedResources.getString(resId);

            if (languageId == CGlobalVariables.HINDI) {
                assertNotEquals("Hindi must keep localized Astro Shop static labels for " + resId,
                        englishValue, localizedValue);
            } else if (languageId != CGlobalVariables.ENGLISH) {
                assertEquals("Non-Hindi Astro Shop static labels must fall back to English for " + resId,
                        englishValue, localizedValue);
            }
        }
    }

    /**
     * Parses localized plan metadata and verifies the expected number of translatable plan rows.
     */
    private static void assertPlanJsonShape(String planJson, int expectedPlanCount) throws Exception {
        JSONObject planObject = new JSONObject(planJson);
        JSONArray plans = planObject.getJSONArray("plans");

        assertEquals(expectedPlanCount, plans.length());
        for (int index = 0; index < plans.length(); index++) {
            JSONObject plan = plans.getJSONObject(index);
            assertNotEquals("", plan.getString("heading").trim());
            assertNotEquals("", plan.getString("desc").trim());
            assertNotEquals("", plan.getString("icon").trim());
        }
    }

    /**
     * Maps a picker language id to the resource key that names the language in the current locale.
     */
    private static int getLanguageNameResId(int languageId) {
        switch (languageId) {
            case CGlobalVariables.ENGLISH:
                return R.string.english;
            case CGlobalVariables.HINDI:
                return R.string.hindi;
            case CGlobalVariables.TAMIL:
                return R.string.tamil;
            case CGlobalVariables.BANGALI:
                return R.string.bangali;
            case CGlobalVariables.TELUGU:
                return R.string.telugu;
            case CGlobalVariables.MARATHI:
                return R.string.marathi;
            case CGlobalVariables.KANNADA:
                return R.string.kannad;
            case CGlobalVariables.GUJARATI:
                return R.string.gujarati;
            case CGlobalVariables.MALAYALAM:
                return R.string.malayalam;
            case CGlobalVariables.ASAMMESSE:
                return R.string.assamese;
            case CGlobalVariables.ODIA:
                return R.string.odia;
            case CGlobalVariables.GERMAN:
                return R.string.german;
            case CGlobalVariables.FRENCH:
                return R.string.french;
            default:
                throw new IllegalArgumentException("Unsupported visible picker language id: " + languageId);
        }
    }
}
