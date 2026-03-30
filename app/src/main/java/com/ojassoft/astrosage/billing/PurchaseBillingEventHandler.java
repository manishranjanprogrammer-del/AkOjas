package com.ojassoft.astrosage.billing;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.ArrayList;
import java.util.List;

public class PurchaseBillingEventHandler implements PurchasesUpdatedListener {

    static BillingEventHandler billingEventHandler;

    public void setBillingEventHandler(BillingEventHandler handler) {
        Log.e("BillingClient", "onPurchasesUpdated() addHandler="+handler);
        billingEventHandler = handler;
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        Log.e("BillingClient", "onPurchasesUpdated() billingEventHandler = "+billingEventHandler);
        if(billingEventHandler != null) {
            billingEventHandler.onPurchasesUpdated(billingResult, list);
        }
    }
}
