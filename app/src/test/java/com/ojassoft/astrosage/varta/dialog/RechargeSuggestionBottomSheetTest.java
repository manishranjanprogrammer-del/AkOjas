package com.ojassoft.astrosage.varta.dialog;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.ojassoft.astrosage.varta.utils.CGlobalVariables;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** Robolectric coverage for dismissing the recharge suggestion bottom sheet on successful payment. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P, manifest = Config.NONE, application = Application.class)
public class RechargeSuggestionBottomSheetTest {

    /** Ensures the bottom sheet dismisses when payment succeeds with quick recharge result code. */
    @Test
    public void onActivityResult_whenQuickRechargeSuccess_dismissesBottomSheet() {
        FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class)
                .setup()
                .get();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        RechargeSuggestionBottomSheet sheet = new RechargeSuggestionBottomSheet();
        sheet.show(fragmentManager, RechargeSuggestionBottomSheet.TITLE);
        fragmentManager.executePendingTransactions();

        assertTrue(sheet.isAdded());
        assertNotNull(sheet.getDialog());

        sheet.onActivityResult(1, CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE, new Intent());
        fragmentManager.executePendingTransactions();

        assertFalse(sheet.isAdded());
    }
}
