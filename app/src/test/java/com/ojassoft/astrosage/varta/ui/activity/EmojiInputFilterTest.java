package com.ojassoft.astrosage.varta.ui.activity;

import android.app.Application;
import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, manifest = Config.NONE, application = Application.class)
public class EmojiInputFilterTest {

    private final EmojiInputFilter filter = new EmojiInputFilter();

    @Test
    public void filter_allowsAsciiCharacters() {
        CharSequence result = filter.filter("John", 0, 4, null, 0, 0);
        assertNull(result);
    }

    @Test
    public void filter_removesEmojiFromMiddleOfInput() {
        CharSequence result = filter.filter("Jo\uD83D\uDE00hn", 0, 6, null, 0, 0);
        assertEquals("John", result);
    }

    @Test
    public void filter_returnsEmptyWhenOnlyEmoji() {
        CharSequence result = filter.filter("\uD83D\uDE00", 0, 2, null, 0, 0);
        assertEquals("", result);
    }

    @Test
    public void filter_stripsDigits() {
        CharSequence result = filter.filter("John123", 0, 7, null, 0, 0);
        assertEquals("John", result);
    }
}
