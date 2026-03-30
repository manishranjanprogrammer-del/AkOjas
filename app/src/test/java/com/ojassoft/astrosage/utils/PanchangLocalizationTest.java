package com.ojassoft.astrosage.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.constants.ConstantsAs;
import com.ojassoft.astrosage.constants.ConstantsOr;
import com.ojassoft.astrosage.constants.LocalizedLagnaConstants;
import com.ojassoft.astrosage.misc.AajKaPanchangCalulation;
import com.ojassoft.astrosage.misc.Language;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Guards the local Panchang calculation path against Odia and Assamese fallback regressions.
 */
public class PanchangLocalizationTest {
    /**
     * Verifies Odia and Assamese Panchang values use dedicated localized constants instead of the
     * lagna-only English fallback wrapper.
     */
    @Test
    public void odiaAndAssamese_useDedicatedPanchangConstantsAndLocalizedModelValues() {
        PanchangUtil panchangUtil = new PanchangUtil();
        AajKaPanchangModel englishModel = createCalculation("en").getPanchang();
        AajKaPanchangModel odiaModel = createCalculation("or").getPanchang();
        AajKaPanchangModel assameseModel = createCalculation("as").getPanchang();

        assertEquals(ConstantsOr.class, panchangUtil.getIConstantsObj(Language.or).getClass());
        assertEquals(ConstantsAs.class, panchangUtil.getIConstantsObj(Language.as).getClass());
        assertNotEquals(LocalizedLagnaConstants.class, panchangUtil.getIConstantsObj(Language.or).getClass());
        assertNotEquals(LocalizedLagnaConstants.class, panchangUtil.getIConstantsObj(Language.as).getClass());

        assertLocalizedModel(englishModel, odiaModel, "[\\u0B00-\\u0B7F]");
        assertLocalizedModel(englishModel, assameseModel, "[\\u0980-\\u09FF]");
    }

    /**
     * Verifies the range and full-night formatter text is localized for Odia and Assamese.
     */
    @Test
    public void odiaAndAssamese_useLocalizedPanchangFormatterText() {
        AajKaPanchangCalulation odiaCalculation = createCalculation("or");
        AajKaPanchangCalulation assameseCalculation = createCalculation("as");

        assertTrue(odiaCalculation.GetString(new double[]{1, 31}, "Tith", 6, 1).contains("ପର୍ଯ୍ୟନ୍ତ"));
        assertTrue(assameseCalculation.GetString(new double[]{1, 31}, "Tith", 6, 1).contains("পৰ্য্যন্ত"));
        assertEquals("06:00 ରୁ 07:00 ପର୍ଯ୍ୟନ୍ତ", odiaCalculation.GetFromToString("06:00", "07:00"));
        assertEquals("06:00 পৰা 07:00 পৰ্য্যন্ত", assameseCalculation.GetFromToString("06:00", "07:00"));
        assertEquals("ପୂର୍ଣ୍ଣ ରାତ୍ରି", odiaCalculation.GetHoverStringFullNight(31, 6));
        assertEquals("সম্পূৰ্ণ ৰাতি", assameseCalculation.GetHoverStringFullNight(31, 6));
    }

    /**
     * Creates a stable local Panchang calculation for regression testing.
     */
    private static AajKaPanchangCalulation createCalculation(String languageKey) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
        calendar.clear();
        calendar.set(2026, Calendar.JANUARY, 15, 10, 0, 0);
        Date date = calendar.getTime();

        return new AajKaPanchangCalulation(date, "1261481", languageKey,
                "28.6139", "77.2090", "5.5", "Asia/Kolkata");
    }

    /**
     * Verifies the main Panchang value fields are populated with native-script labels.
     */
    private static void assertLocalizedModel(AajKaPanchangModel englishModel, AajKaPanchangModel localizedModel,
                                             String scriptPattern) {
        assertScriptDifference(englishModel.getTithiValue(), localizedModel.getTithiValue(), scriptPattern);
        assertScriptDifference(englishModel.getNakshatraValue(), localizedModel.getNakshatraValue(), scriptPattern);
        assertScriptDifference(englishModel.getKaranaValue(), localizedModel.getKaranaValue(), scriptPattern);
        assertScriptDifference(englishModel.getPakshaName(), localizedModel.getPakshaName(), scriptPattern);
        assertScriptDifference(englishModel.getYogaValue(), localizedModel.getYogaValue(), scriptPattern);
        assertScriptDifference(englishModel.getVaara(), localizedModel.getVaara(), scriptPattern);
        assertScriptDifference(englishModel.getMoonSignValue(), localizedModel.getMoonSignValue(), scriptPattern);
        assertScriptDifference(englishModel.getRitu(), localizedModel.getRitu(), scriptPattern);
        assertScriptDifference(englishModel.getShakaSamvatName(), localizedModel.getShakaSamvatName(), scriptPattern);
        assertScriptDifference(englishModel.getMonthAmanta(), localizedModel.getMonthAmanta(), scriptPattern);
    }

    /**
     * Confirms the localized value differs from English and contains native-script characters.
     */
    private static void assertScriptDifference(String englishValue, String localizedValue, String scriptPattern) {
        assertNotEquals(englishValue, localizedValue);
        assertTrue(localizedValue.matches(".*" + scriptPattern + ".*"));
    }
}
