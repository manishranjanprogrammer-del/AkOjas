package com.ojassoft.astrosage;

import com.ojassoft.astrosage.custompushnotification.CustomAINotificationTest;
import com.ojassoft.astrosage.ui.fragments.WizardChooseLanguageTest;
import com.ojassoft.astrosage.utils.ChecklistTestingAkApp;
import com.ojassoft.astrosage.utils.FrenchLanguageSupportTest;
import com.ojassoft.astrosage.utils.LagnaLanguageLocalizationTest;
import com.ojassoft.astrosage.utils.ValidatorTest;
import com.ojassoft.astrosage.varta.ui.activity.EmojiInputFilterTest;
import com.ojassoft.astrosage.varta.ui.activity.FirstTimeProfileDetailsActivityTest;
import com.ojassoft.astrosage.varta.utils.CUtilsBusinessRulesTest;
import com.ojassoft.astrosage.varta.utils.UPIResponseParserTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** Curated smoke suite to run key app unit tests together. */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ValidatorTest.class,
        CUtilsBusinessRulesTest.class,
        UPIResponseParserTest.class,
        FirstTimeProfileDetailsActivityTest.class,
        EmojiInputFilterTest.class,
        CustomAINotificationTest.class,
        ChecklistTestingAkApp.class,
        WizardChooseLanguageTest.class,
        FrenchLanguageSupportTest.class,
        LagnaLanguageLocalizationTest.class
})
public class SmokeTestSuite {
}
