package com.ojassoft.astrosage.utils;

/**
 * Centralizes local plan-feature JSON selection so all plan screens use the same language mapping.
 */
public final class PlanLocalizationUtil {

    private PlanLocalizationUtil() {
    }

    /**
     * Returns the localized basic-plan feature JSON for the requested app language.
     */
    public static String getBasicPlanFeatureJson(int languageCode) {
        switch (languageCode) {
            case CGlobalVariables.HINDI:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesHindi;
            case CGlobalVariables.TAMIL:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesTamil;
            case CGlobalVariables.BANGALI:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesBangali;
            case CGlobalVariables.MARATHI:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesMarathi;
            case CGlobalVariables.TELUGU:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesTelugu;
            case CGlobalVariables.KANNADA:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesKannad;
            case CGlobalVariables.GUJARATI:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesGujrati;
            case CGlobalVariables.MALAYALAM:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesMalayalam;
            case CGlobalVariables.ODIA:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesOdia;
            case CGlobalVariables.ASAMMESSE:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesAssamese;
            case CGlobalVariables.SPANISH:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesSpanish;
            case CGlobalVariables.JAPANESE:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesJapanese;
            case CGlobalVariables.GERMAN:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesGerman;
            case CGlobalVariables.FRENCH:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesFrench;
            case CGlobalVariables.ENGLISH:
            default:
                return CGlobalVariables.data_BasicPlanAstrologicalFeaturesEnglish;
        }
    }

    /**
     * Returns the localized KundliAI Plus and Dhruv plan feature JSON for the requested app language.
     */
    public static String getPlatinumPlanFeatureJson(int languageCode) {
        switch (languageCode) {
            case CGlobalVariables.HINDI:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesHindi;
            case CGlobalVariables.TAMIL:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesTamil;
            case CGlobalVariables.BANGALI:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesBangali;
            case CGlobalVariables.MARATHI:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesMarathi;
            case CGlobalVariables.TELUGU:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesTelugu;
            case CGlobalVariables.KANNADA:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesKannad;
            case CGlobalVariables.GUJARATI:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesGujrati;
            case CGlobalVariables.MALAYALAM:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesMalayalam;
            case CGlobalVariables.ODIA:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesOdia;
            case CGlobalVariables.ASAMMESSE:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesAssamese;
            case CGlobalVariables.SPANISH:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesSpanish;
            case CGlobalVariables.JAPANESE:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesJapanese;
            case CGlobalVariables.GERMAN:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesGerman;
            case CGlobalVariables.FRENCH:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesFrench;
            case CGlobalVariables.ENGLISH:
            default:
                return CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesEnglish;
        }
    }
}
