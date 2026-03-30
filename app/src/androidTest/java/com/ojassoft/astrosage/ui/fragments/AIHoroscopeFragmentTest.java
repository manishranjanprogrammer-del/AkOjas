package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.utils.CUtils.urlEncodeData;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.model.UserProfileData;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class AIHoroscopeFragmentTest {

    @Test
    public void newInstance_setsRashiTypeArgument() {
        AIHoroscopeFragment fragment = AIHoroscopeFragment.newInstance(7);

        assertNotNull(fragment.getArguments());
        assertEquals(7, fragment.getArguments().getInt("rashiType"));
    }

    @Test
    public void buildHoroscopeParamsForRequest_populatesExpectedKeysAndValues() {
        UserProfileData profile = new UserProfileData();
        profile.setName("Tushar");
        profile.setGender("M");
        profile.setDay("1");
        profile.setMonth("2");
        profile.setYear("2000");
        profile.setHour("10");
        profile.setMinute("20");
        profile.setSecond("30");
        profile.setPlace("Delhi");
        profile.setLongdeg("77");
        profile.setLongmin("13");
        profile.setLongew("E");
        profile.setLatdeg("28");
        profile.setLatmin("36");
        profile.setLatns("N");
        profile.setTimezone("5.5");
        profile.setMaritalStatus("Single");
        profile.setOccupation("Engineer");

        Map<String, String> params = AIHoroscopeFragment.buildHoroscopeParamsForRequest(
                profile,
                CGlobalVariables.ENGLISH,
                "user+91",
                "p@ss word"
        );

        assertEquals("Tushar", params.get("name"));
        assertEquals("M", params.get("sex"));
        assertEquals("1", params.get("day"));
        assertEquals("2", params.get("month"));
        assertEquals("2000", params.get("year"));
        assertEquals("Delhi", params.get("place"));
        assertEquals("77", params.get("longdeg"));
        assertEquals("13", params.get("longmin"));
        assertEquals("E", params.get("longew"));
        assertEquals("28", params.get("latdeg"));
        assertEquals("36", params.get("latmin"));
        assertEquals("N", params.get("latns"));
        assertEquals("5.5", params.get("timezone"));
        assertEquals("Single", params.get("ms"));
        assertEquals("Engineer", params.get("oc"));
        assertEquals(String.valueOf(CGlobalVariables.ENGLISH), params.get("languagecode"));
        assertEquals(urlEncodeData("user+91"), params.get("userid"));
        assertEquals(urlEncodeData("p@ss word"), params.get("pw"));
    }
}
