package com.ojassoft.astrosage.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.res.Resources;

import androidx.test.core.app.ApplicationProvider;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.TestAstrosageKundliApplication;
import com.ojassoft.astrosage.varta.model.Language;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Verifies the app-level language registry exposes French consistently across picker and helper mappings.
 */
@RunWith(RobolectricTestRunner.class)
@Config(application = TestAstrosageKundliApplication.class)
public class FrenchLanguageSupportTest {
    private static final Set<Integer> PLAN_JSON_LANGUAGE_IDS = new TreeSet<>(Arrays.asList(
            CGlobalVariables.ENGLISH,
            CGlobalVariables.HINDI,
            CGlobalVariables.TAMIL,
            CGlobalVariables.BANGALI,
            CGlobalVariables.TELUGU,
            CGlobalVariables.MARATHI,
            CGlobalVariables.KANNADA,
            CGlobalVariables.GUJARATI,
            CGlobalVariables.MALAYALAM,
            CGlobalVariables.ODIA,
            CGlobalVariables.ASAMMESSE,
            CGlobalVariables.GERMAN,
            CGlobalVariables.FRENCH
    ));


    /**
     * Verifies French appears in the shared language picker data with the expected language code.
     */
    @Test
    public void languageList_includesFrenchOption() {
        Context context = ApplicationProvider.getApplicationContext();
        List<Language> languageList = CUtils.getLanguageList(context);

        boolean hasFrench = false;
        for (Language language : languageList) {
            if (context.getString(R.string.french).equals(language.getName())
                    && language.getLanguageId() == CGlobalVariables.FRENCH) {
                hasFrench = true;
                break;
            }
        }

        assertTrue(hasFrench);
    }

    /**
     * Verifies French resolves to the expected locale key and push topic helpers.
     */
    @Test
    public void languageHelpers_mapFrenchToLocaleAndTopic() {
        assertEquals("fr", CUtils.getLanguageKey(CGlobalVariables.FRENCH));
        assertEquals("fr", CUtils.getLanguage(CGlobalVariables.FRENCH));
        assertEquals(CGlobalVariables.TOPIC_FRENCH, CUtils.getTopicName(CGlobalVariables.FRENCH));
    }

    /**
     * Verifies French locale resources resolve translated strings instead of falling back to the base language.
     */
    @Test
    public void localeResources_returnFrenchTranslations() {
        Context context = ApplicationProvider.getApplicationContext();
        Resources frenchResources = com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources(context, "fr");

        assertEquals("Annuler", frenchResources.getString(R.string.cancel));
        assertEquals("Soumettre", frenchResources.getString(R.string.submit));
        assertEquals("Français", frenchResources.getString(R.string.french));
    }

    /**
     * Verifies French locale arrays resolve translated calendar and language picker labels.
     */
    @Test
    public void localeResources_returnFrenchArrays() {
        Context context = ApplicationProvider.getApplicationContext();
        Resources frenchResources = com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources(context, "fr");

        String[] frenchMonths = frenchResources.getStringArray(R.array.MonthName);
        String[] frenchLanguages = frenchResources.getStringArray(R.array.language_spinner_data);

        assertEquals("janvier", frenchMonths[0]);
        assertEquals("décembre", frenchMonths[11]);
        assertEquals("Langue", frenchLanguages[0]);
        assertEquals("Français", frenchLanguages[frenchLanguages.length - 1]);
    }

    /**
     * Verifies the French strings catalog defines every string key available in the base resources.
     */
    @Test
    public void frenchStringsCatalog_matchesBaseCoverage() throws IOException {
        Set<String> baseStrings = loadResourceNames("app/src/main/res/values/strings.xml", "string");
        Set<String> frenchStrings = loadResourceNames("app/src/main/res/values-fr/strings.xml", "string");
        baseStrings.removeAll(Arrays.asList(
                "astro_shop_module_brihat",
                "astro_shop_astro_services",
                "astro_shop_dhruv",
                "astro_shop_platinum_plan",
                "astro_shop_module_cogni_astro"
        ));

        assertTrue("French strings.xml must contain every base string key.",
                frenchStrings.containsAll(baseStrings));
    }

    /**
     * Verifies the French array catalog defines every string-array key available in the base resources.
     */
    @Test
    public void frenchArrayCatalog_matchesBaseCoverage() throws IOException {
        Set<String> baseArrays = loadResourceNames("app/src/main/res/values/array.xml", "string-array");
        Set<String> frenchArrays = loadResourceNames("app/src/main/res/values-fr/array.xml", "string-array");

        assertTrue("French array.xml must contain every base string-array key.",
                frenchArrays.containsAll(baseArrays));
    }

    /**
     * Verifies the shared language-topic cleanup registry includes French for unsubscribe and retry flows.
     */
    @Test
    public void languageTopicRegistry_includesFrench() {
        assertTrue(Arrays.stream(CGlobalVariables.langArr)
                .anyMatch(languageCode -> languageCode == CGlobalVariables.FRENCH));
    }

    /**
     * Verifies a failed French topic subscription is persisted as a language topic for retry.
     */
    @Test
    public void failedFrenchSubscription_isSavedAsLanguageTopic() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        CUtils.saveStringData(context, CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
        CUtils.saveStringData(context, CGlobalVariables.EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");

        Method saveTopicsForSubscribe = CUtils.class.getDeclaredMethod("saveTopicsForSubscribe", String.class);
        saveTopicsForSubscribe.setAccessible(true);
        saveTopicsForSubscribe.invoke(null, CGlobalVariables.TOPIC_FRENCH);

        assertEquals(CGlobalVariables.TOPIC_FRENCH,
                CUtils.getStringData(context, CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, ""));
        assertEquals("",
                CUtils.getStringData(context, CGlobalVariables.EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, ""));
    }

    /**
     * Verifies local plan JSON translations cover all current local-plan languages and stay in sync
     * with visible picker languages plus the fully translated French locale.
     */
    @Test
    public void localizedPlanJson_translationsCoverPickerLanguagesAndFrench() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        Set<Integer> expectedPlanLanguages = new TreeSet<>();

        for (Language language : CUtils.getLanguageList(context)) {
            expectedPlanLanguages.add(language.getLanguageId());
        }
        expectedPlanLanguages.add(CGlobalVariables.FRENCH);

        assertEquals(expectedPlanLanguages, PLAN_JSON_LANGUAGE_IDS);

        String englishBasicPlan = PlanLocalizationUtil.getBasicPlanFeatureJson(CGlobalVariables.ENGLISH);
        String englishPlatinumPlan = PlanLocalizationUtil.getPlatinumPlanFeatureJson(CGlobalVariables.ENGLISH);

        for (int languageId : PLAN_JSON_LANGUAGE_IDS) {
            String basicPlanJson = PlanLocalizationUtil.getBasicPlanFeatureJson(languageId);
            String platinumPlanJson = PlanLocalizationUtil.getPlatinumPlanFeatureJson(languageId);

            assertPlanJsonShape(basicPlanJson, 5);
            assertPlanJsonShape(platinumPlanJson, 4);

            if (languageId != CGlobalVariables.ENGLISH) {
                assertNotEquals("Non-English basic plan JSON must not silently fall back to English.",
                        englishBasicPlan, basicPlanJson);
                assertNotEquals("Non-English KundliAI+/Dhruv plan JSON must not silently fall back to English.",
                        englishPlatinumPlan, platinumPlanJson);
            }
        }

        JSONObject frenchBasicPlan = new JSONObject(PlanLocalizationUtil.getBasicPlanFeatureJson(CGlobalVariables.FRENCH))
                .getJSONArray("plans").getJSONObject(0);
        JSONObject frenchPlatinumPlan = new JSONObject(PlanLocalizationUtil.getPlatinumPlanFeatureJson(CGlobalVariables.FRENCH))
                .getJSONArray("plans").getJSONObject(0);

        assertEquals("Créer une kundli", frenchBasicPlan.getString("heading"));
        assertEquals("Créez votre kundli personnalisée", frenchBasicPlan.getString("desc"));
        assertEquals("1 kundli premium chaque mois \n(valeur 499 ₹)", frenchPlatinumPlan.getString("heading"));
        assertEquals("Débloquez des prévisions de vie détaillées grâce à un rapport premium complet.",
                frenchPlatinumPlan.getString("desc"));
    }

    /**
     * Extracts resource names for a single XML tag type from a project resource file.
     */
    private static Set<String> loadResourceNames(String relativePath, String tagName) throws IOException {
        String fileContent = new String(Files.readAllBytes(resolveProjectFile(relativePath)), StandardCharsets.UTF_8);
        Matcher matcher = Pattern.compile("<" + tagName + "\\s+name=\"([^\"]+)\"").matcher(fileContent);
        Set<String> resourceNames = new TreeSet<>();

        while (matcher.find()) {
            resourceNames.add(matcher.group(1));
        }

        return resourceNames;
    }

    /**
     * Parses plan metadata JSON and verifies the expected number of translatable plan rows exists.
     */
    private static void assertPlanJsonShape(String planJson, int expectedPlanCount) throws Exception {
        JSONObject planObject = new JSONObject(planJson);
        JSONArray plans = planObject.getJSONArray("plans");

        assertEquals(expectedPlanCount, plans.length());
        for (int index = 0; index < plans.length(); index++) {
            JSONObject plan = plans.getJSONObject(index);
            assertTrue(plan.has("heading"));
            assertTrue(plan.has("desc"));
            assertTrue(plan.has("icon"));
            assertNotEquals("", plan.getString("heading").trim());
            assertNotEquals("", plan.getString("desc").trim());
            assertNotEquals("", plan.getString("icon").trim());
        }
    }

    /**
     * Resolves a repository-relative file path so tests can inspect resource coverage directly from source files.
     */
    private static Path resolveProjectFile(String relativePath) {
        Path currentPath = Paths.get(System.getProperty("user.dir")).toAbsolutePath();

        while (currentPath != null) {
            Path candidate = currentPath.resolve(relativePath);
            if (Files.exists(candidate)) {
                return candidate;
            }
            currentPath = currentPath.getParent();
        }

        throw new IllegalStateException("Unable to resolve project file: " + relativePath);
    }
}
