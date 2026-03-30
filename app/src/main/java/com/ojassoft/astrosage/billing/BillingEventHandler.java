package com.ojassoft.astrosage.billing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;

import java.util.List;

public interface BillingEventHandler {
    void onPurchasesUpdated(@NonNull BillingResult var1, @Nullable List<Purchase> var2);
}
