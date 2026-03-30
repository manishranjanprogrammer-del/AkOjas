package com.ojassoft.astrosage.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.res.Resources;

import androidx.test.core.app.ApplicationProvider;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.TestAstrosageKundliApplication;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Guards KundliAI Plus localized feature metadata and fragment-specific strings against fallback
 * regressions for languages that rely on app-side plan JSON instead of a remote feature payload.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestAstrosageKundliApplication.class)
public class KundliAiPlusPlanLocalizationTest {
    private static final List<Integer> LOCALIZED_EXTENDED_PLAN_LANGUAGES = Arrays.asList(
            CGlobalVariables.ODIA,
            CGlobalVariables.ASAMMESSE,
            CGlobalVariables.GERMAN,
            CGlobalVariables.FRENCH
    );

    /**
     * Verifies KundliAI Plus feature rows remain localized for extended app-side languages.
     */
    @Test
    public void kundliAiPlusFeatureJson_isLocalizedForExtendedPlanLanguages() throws Exception {
        String englishPlanJson = PlanLocalizationUtil.getPlatinumPlanFeatureJson(CGlobalVariables.ENGLISH);

        for (int languageId : LOCALIZED_EXTENDED_PLAN_LANGUAGES) {
            String localizedPlanJson = PlanLocalizationUtil.getPlatinumPlanFeatureJson(languageId);

            assertPlanJsonShape(localizedPlanJson);
            assertNotEquals("Extended plan language must not fall back to English local JSON: " + languageId,
                    englishPlanJson, localizedPlanJson);
        }

        JSONObject odiaFirstPlan = firstPlan(CGlobalVariables.ODIA);
        JSONObject assameseFirstPlan = firstPlan(CGlobalVariables.ASAMMESSE);
        JSONObject germanFirstPlan = firstPlan(CGlobalVariables.GERMAN);
        JSONObject frenchFirstPlan = firstPlan(CGlobalVariables.FRENCH);

        assertEquals("ପ୍ରତି ମାସ 1 ପ୍ରିମିୟମ୍ କୁଣ୍ଡଳୀ \n(ମୂଲ୍ୟ ₹499)", odiaFirstPlan.getString("heading"));
        assertEquals("প্ৰতি মাহে 1 টা প্ৰিমিয়াম কুণ্ডলী \n(মূল্য ₹499)", assameseFirstPlan.getString("heading"));
        assertEquals("1 Premium-Kundli pro Monat \n(Wert ₹499)", germanFirstPlan.getString("heading"));
        assertEquals("1 kundli premium chaque mois \n(valeur 499 ₹)", frenchFirstPlan.getString("heading"));
    }

    /**
     * Verifies the extra question-limit row and CTA strings used by KundliAI Plus stay localized.
     */
    @Test
    public void kundliAiPlusFragmentStrings_areLocalizedForExtendedPlanLanguages() {
        Context context = ApplicationProvider.getApplicationContext();
        Resources englishResources = com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources(context, "en");

        assertLocalizedFragmentStrings(context, "or",
                "କୁଣ୍ଡଳୀ AI ରେ ପ୍ରତିଦିନ %s ପ୍ରଶ୍ନ",
                "ଆବଶ୍ୟକ ସମୟରେ ଦିନନିୟ ମର୍ଗଦର୍ଶନ ଓ ଉତ୍ତର ପାଆନ୍ତୁ।",
                "%s/ମାସରେ ସଦସ୍ୟତା ନିଅନ୍ତୁ",
                "%s/3 ମାସ ପାଇଁ ସବସ୍କ୍ରାଇବ୍ କରନ୍ତୁ",
                "ଦେୟ ଦିଅନ୍ତୁ %s",
                "₹0 ରେ ଏବେ ଟ୍ରାୟ କରନ୍ତୁ");

        assertLocalizedFragmentStrings(context, "as",
                "কুণ্ডলী AI-ত দিনে %sটা প্ৰশ্ন",
                "প্ৰয়োজনীয় সময়ত দৈনিক পথপ্ৰদৰ্শন আৰু উত্তৰ লাভ কৰক।",
                "%s ত মাহটোৰ বাবে চাবস্ক্ৰাইব কৰক।",
                "%s/3 মাহৰ বাবে ছাবস্ক্ৰাইব কৰক",
                "পৰিশোধ কৰক %s",
                "₹0 ত বিনামূলীয়া চেষ্টা কৰক");

        Resources germanResources = assertLocalizedFragmentStrings(context, "de",
                "%s Fragen/Tag auf Kundli AI",
                "Erhalten Sie täglich Orientierung und Antworten, genau dann, wenn Sie sie brauchen.",
                "Abonnieren für %s/Monat",
                "Abonnieren Sie für %s/3 Monate",
                "Zahlen Sie %s",
                "Jetzt für ₹0 ausprobieren");

        Resources frenchResources = assertLocalizedFragmentStrings(context, "fr",
                "%s questions/jour sur Kundli AI",
                "Obtenez des conseils et des réponses chaque jour, quand vous en avez besoin.",
                "Abonnez-vous pour %s/mois",
                "Abonnez-vous pour %s/3 mois",
                "Payer %s",
                "Essayez maintenant pour ₹0");

        assertEquals("PhonePe", germanResources.getString(R.string.phonepe));
        assertEquals("Paytm", germanResources.getString(R.string.paytm));
        assertEquals("Razorpay", germanResources.getString(R.string.razorpay));
        assertEquals("CRED", germanResources.getString(R.string.cred));
        assertEquals("BHIM", germanResources.getString(R.string.bhim));

        assertEquals("PhonePe", frenchResources.getString(R.string.phonepe));
        assertEquals("Paytm", frenchResources.getString(R.string.paytm));
        assertEquals("Razorpay", frenchResources.getString(R.string.razorpay));
        assertEquals("CRED", frenchResources.getString(R.string.cred));
        assertEquals("BHIM", frenchResources.getString(R.string.bhim));

        assertNotEquals(englishResources.getString(R.string.kundli_ai_plus_question_limit_description),
                germanResources.getString(R.string.kundli_ai_plus_question_limit_description));
        assertNotEquals(englishResources.getString(R.string.kundli_ai_plus_question_limit_description),
                frenchResources.getString(R.string.kundli_ai_plus_question_limit_description));
    }

    /**
     * Verifies the fragment continues to rely on local feature JSON for languages without dedicated
     * remote platinum-plan feature keys.
     */
    @Test
    public void kundliAiPlusExtendedLanguages_useLocalPlanJsonFallbackPath() {
        List<String> remotePlatinumKeyFieldNames = Arrays.asList(Arrays.stream(CGlobalVariables.class.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName -> fieldName.startsWith("key_PlatinumPlanAstrologicalFeatures"))
                .toArray(String[]::new));

        assertEquals("PlatinumPlanAstrologicalFeaturesEnglishV1",
                CGlobalVariables.key_PlatinumPlanAstrologicalFeaturesEnglish);
        assertEquals("PlatinumPlanAstrologicalFeaturesMalayalamV1",
                CGlobalVariables.key_PlatinumPlanAstrologicalFeaturesMalayalam);
        assertTrue(remotePlatinumKeyFieldNames.contains("key_PlatinumPlanAstrologicalFeaturesEnglish"));
        assertTrue(remotePlatinumKeyFieldNames.contains("key_PlatinumPlanAstrologicalFeaturesMalayalam"));
        assertTrue(!remotePlatinumKeyFieldNames.contains("key_PlatinumPlanAstrologicalFeaturesOdia"));
        assertTrue(!remotePlatinumKeyFieldNames.contains("key_PlatinumPlanAstrologicalFeaturesAssamese"));
        assertTrue(!remotePlatinumKeyFieldNames.contains("key_PlatinumPlanAstrologicalFeaturesGerman"));
        assertTrue(!remotePlatinumKeyFieldNames.contains("key_PlatinumPlanAstrologicalFeaturesFrench"));
        assertTrue("Extended languages still depend on app-side localized JSON for KundliAI Plus feature rows.",
                PlanLocalizationUtil.getPlatinumPlanFeatureJson(CGlobalVariables.ODIA).contains("କୁଣ୍ଡଳୀ"));
        assertTrue("Extended languages still depend on app-side localized JSON for KundliAI Plus feature rows.",
                PlanLocalizationUtil.getPlatinumPlanFeatureJson(CGlobalVariables.ASAMMESSE).contains("কুণ্ডলী"));
        assertTrue("Extended languages still depend on app-side localized JSON for KundliAI Plus feature rows.",
                PlanLocalizationUtil.getPlatinumPlanFeatureJson(CGlobalVariables.GERMAN).contains("Premium-Kundli"));
        assertTrue("Extended languages still depend on app-side localized JSON for KundliAI Plus feature rows.",
                PlanLocalizationUtil.getPlatinumPlanFeatureJson(CGlobalVariables.FRENCH).contains("kundli"));
    }

    /**
     * Loads locale resources for a KundliAI Plus language and verifies the fragment-owned strings.
     */
    private static Resources assertLocalizedFragmentStrings(Context context, String localeKey,
                                                            String expectedQuestionTemplate,
                                                            String expectedQuestionDescription,
                                                            String expectedSubscribeMonthly,
                                                            String expectedSubscribeQuarterly,
                                                            String expectedPayButtonText,
                                                            String expectedFreeTrialCta) {
        Resources localizedResources = com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources(context, localeKey);

        assertEquals(expectedQuestionTemplate, localizedResources.getString(R.string.num_ques_a_day_txt));
        assertEquals(expectedQuestionDescription,
                localizedResources.getString(R.string.kundli_ai_plus_question_limit_description));
        assertEquals(expectedSubscribeMonthly, localizedResources.getString(R.string.subscribe_for_text));
        assertEquals(expectedSubscribeQuarterly, localizedResources.getString(R.string.subscribe_for_3_text));
        assertEquals(expectedPayButtonText, localizedResources.getString(R.string.button_text_with_price));
        assertEquals(expectedFreeTrialCta, localizedResources.getString(R.string.try_now_for_free));

        return localizedResources;
    }

    /**
     * Returns the first localized KundliAI Plus feature row for a single app language.
     */
    private static JSONObject firstPlan(int languageId) throws Exception {
        JSONObject planObject = new JSONObject(PlanLocalizationUtil.getPlatinumPlanFeatureJson(languageId));
        return planObject.getJSONArray("plans").getJSONObject(0);
    }

    /**
     * Parses localized plan metadata and verifies the expected KundliAI Plus feature shape.
     */
    private static void assertPlanJsonShape(String planJson) throws Exception {
        JSONObject planObject = new JSONObject(planJson);
        JSONArray plans = planObject.getJSONArray("plans");

        assertEquals(4, plans.length());
        for (int index = 0; index < plans.length(); index++) {
            JSONObject plan = plans.getJSONObject(index);
            assertNotEquals("", plan.getString("heading").trim());
            assertNotEquals("", plan.getString("desc").trim());
            assertNotEquals("", plan.getString("icon").trim());
        }
    }
}
