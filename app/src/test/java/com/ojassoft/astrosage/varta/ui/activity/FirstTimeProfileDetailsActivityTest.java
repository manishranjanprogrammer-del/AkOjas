package com.ojassoft.astrosage.varta.ui.activity;

import android.app.Application;
import android.os.Build;
import android.widget.CheckBox;

import com.ojassoft.astrosage.varta.model.BeanDateTime;
import com.ojassoft.astrosage.varta.model.UserDateTimeTempSingleton;
import com.ojassoft.astrosage.varta.model.UserProfileData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/** Regression tests for `setDateAndTime(long)` profile date/time persistence rules. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, manifest = Config.NONE, application = Application.class)
public class FirstTimeProfileDetailsActivityTest {

    /** Verifies near-current entered birth date-time clears date and time in profile payload. */
    @Test
    public void setDateAndTime_clearsProfileDateTime_whenInputIsWithinFiveMinutes() throws Exception {
        FirstTimeProfileDetailsActivity activity = newActivity(false);
        BeanDateTime beanDateTime = newBeanDateTime(2026, 1, 15, 10, 3, 0);
        UserProfileData userProfileData = new UserProfileData();

        setField(activity, "beanDateTime", beanDateTime);
        setField(activity, "userProfileData", userProfileData);

        invokeSetDateAndTime(activity, millisOf(2026, 1, 15, 10, 0, 0));

        assertEquals("", userProfileData.getDay());
        assertEquals("", userProfileData.getMonth());
        assertEquals("", userProfileData.getYear());
        assertEquals("", userProfileData.getHour());
        assertEquals("", userProfileData.getMinute());
        assertEquals("", userProfileData.getSecond());

        UserDateTimeTempSingleton singleton = UserDateTimeTempSingleton.getInstance();
        assertEquals("15", singleton.getDay());
        assertEquals("1", singleton.getMonth());
        assertEquals("2026", singleton.getYear());
        assertEquals("10", singleton.getHour());
        assertEquals("3", singleton.getMinute());
        assertEquals("0", singleton.getSecond());
    }

    /** Verifies unknown-time selection on a different date keeps date but clears time fields. */
    @Test
    public void setDateAndTime_savesOnlyDate_whenDontKnowTimeCheckedAndDateDiffers() throws Exception {
        FirstTimeProfileDetailsActivity activity = newActivity(true);
        BeanDateTime beanDateTime = newBeanDateTime(2026, 1, 14, 8, 30, 5);
        UserProfileData userProfileData = new UserProfileData();

        setField(activity, "beanDateTime", beanDateTime);
        setField(activity, "userProfileData", userProfileData);

        invokeSetDateAndTime(activity, millisOf(2026, 1, 15, 10, 0, 0));

        assertEquals("14", userProfileData.getDay());
        assertEquals("1", userProfileData.getMonth());
        assertEquals("2026", userProfileData.getYear());
        assertEquals("", userProfileData.getHour());
        assertEquals("", userProfileData.getMinute());
        assertEquals("", userProfileData.getSecond());
    }

    /** Verifies known-time selection stores full date and time when input is not near current time. */
    @Test
    public void setDateAndTime_savesFullDateTime_whenKnownTimeAndNotNearCurrentTime() throws Exception {
        FirstTimeProfileDetailsActivity activity = newActivity(false);
        BeanDateTime beanDateTime = newBeanDateTime(2026, 1, 10, 8, 30, 5);
        UserProfileData userProfileData = new UserProfileData();

        setField(activity, "beanDateTime", beanDateTime);
        setField(activity, "userProfileData", userProfileData);

        invokeSetDateAndTime(activity, millisOf(2026, 1, 15, 10, 0, 0));

        assertEquals("10", userProfileData.getDay());
        assertEquals("1", userProfileData.getMonth());
        assertEquals("2026", userProfileData.getYear());
        assertEquals("8", userProfileData.getHour());
        assertEquals("30", userProfileData.getMinute());
        assertEquals("5", userProfileData.getSecond());
    }

    /** Creates an activity instance with only the checkbox dependency initialized for the private method. */
    private FirstTimeProfileDetailsActivity newActivity(boolean dontKnowTimeChecked) throws Exception {
        FirstTimeProfileDetailsActivity activity = Robolectric.buildActivity(FirstTimeProfileDetailsActivity.class).get();
        CheckBox dontKnowTimeCheckBox = new CheckBox(RuntimeEnvironment.getApplication());
        dontKnowTimeCheckBox.setChecked(dontKnowTimeChecked);
        setField(activity, "dontKnowTimeCB", dontKnowTimeCheckBox);
        return activity;
    }

    /** Builds a `BeanDateTime` using the 1-based month format expected by the production code. */
    private BeanDateTime newBeanDateTime(int year, int month, int day, int hour, int minute, int second) {
        BeanDateTime beanDateTime = new BeanDateTime(false, false);
        beanDateTime.setYear(year);
        beanDateTime.setMonth(month);
        beanDateTime.setDay(day);
        beanDateTime.setHour(hour);
        beanDateTime.setMin(minute);
        beanDateTime.setSecond(second);
        return beanDateTime;
    }

    /** Invokes the private `setDateAndTime(long)` method through reflection for branch verification. */
    private void invokeSetDateAndTime(FirstTimeProfileDetailsActivity activity, long currentTimeMillis) throws Exception {
        Method method = FirstTimeProfileDetailsActivity.class.getDeclaredMethod("setDateAndTime", long.class);
        method.setAccessible(true);
        method.invoke(activity, currentTimeMillis);
    }

    /** Sets a private field on the activity under test to isolate the method from UI setup. */
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /** Produces epoch milliseconds for deterministic comparisons used by `setDateAndTime(long)`. */
    private long millisOf(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}
