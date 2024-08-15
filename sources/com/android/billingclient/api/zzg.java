package com.android.billingclient.api;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.internal.play_billing.zzb;
import com.google.android.gms.internal.play_billing.zzfb;
import java.util.List;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzg extends BroadcastReceiver {
    final /* synthetic */ zzh zza;
    private final PurchasesUpdatedListener zzb;
    private final zzar zze;
    private boolean zzf;

    public /* synthetic */ zzg(zzh zzhVar, zzaz zzazVar, zzar zzarVar, zzf zzfVar) {
        this.zza = zzhVar;
        this.zzb = null;
        this.zze = zzarVar;
    }

    public static /* bridge */ /* synthetic */ zzaz zza(zzg zzgVar) {
        zzgVar.getClass();
        return null;
    }

    private final void zze(Bundle bundle, BillingResult billingResult, int i) {
        if (bundle.getByteArray("FAILURE_LOGGING_PAYLOAD") != null) {
            try {
                this.zze.zza(zzfb.zzx(bundle.getByteArray("FAILURE_LOGGING_PAYLOAD"), com.google.android.gms.internal.play_billing.zzbn.zza()));
                return;
            } catch (Throwable unused) {
                zzb.zzj("BillingBroadcastManager", "Failed parsing Api failure.");
                return;
            }
        }
        this.zze.zza(zzaq.zza(23, i, billingResult));
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        int i = 1;
        if (extras == null) {
            zzb.zzj("BillingBroadcastManager", "Bundle is null.");
            zzar zzarVar = this.zze;
            BillingResult billingResult = zzat.zzj;
            zzarVar.zza(zzaq.zza(11, 1, billingResult));
            PurchasesUpdatedListener purchasesUpdatedListener = this.zzb;
            if (purchasesUpdatedListener != null) {
                purchasesUpdatedListener.onPurchasesUpdated(billingResult, null);
                return;
            }
            return;
        }
        BillingResult zzd = zzb.zzd(intent, "BillingBroadcastManager");
        String action = intent.getAction();
        String string = extras.getString("INTENT_SOURCE");
        if (string == "LAUNCH_BILLING_FLOW" || (string != null && string.equals("LAUNCH_BILLING_FLOW"))) {
            i = 2;
        }
        if (action.equals("com.android.vending.billing.PURCHASES_UPDATED")) {
            List<Purchase> zzh = zzb.zzh(extras);
            if (zzd.getResponseCode() == 0) {
                this.zze.zzb(zzaq.zzb(i));
            } else {
                zze(extras, zzd, i);
            }
            this.zzb.onPurchasesUpdated(zzd, zzh);
        } else if (action.equals("com.android.vending.billing.ALTERNATIVE_BILLING")) {
            if (zzd.getResponseCode() != 0) {
                zze(extras, zzd, i);
                this.zzb.onPurchasesUpdated(zzd, com.google.android.gms.internal.play_billing.zzu.zzk());
                return;
            }
            zzb.zzj("BillingBroadcastManager", "AlternativeBillingListener is null.");
            zzar zzarVar2 = this.zze;
            BillingResult billingResult2 = zzat.zzj;
            zzarVar2.zza(zzaq.zza(15, i, billingResult2));
            this.zzb.onPurchasesUpdated(billingResult2, com.google.android.gms.internal.play_billing.zzu.zzk());
        }
    }

    @SuppressLint({"UnprotectedReceiver"})
    public final void zzc(Context context, IntentFilter intentFilter) {
        zzg zzgVar;
        zzg zzgVar2;
        if (this.zzf) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 33) {
            zzgVar2 = this.zza.zzb;
            context.registerReceiver(zzgVar2, intentFilter, 2);
        } else {
            zzgVar = this.zza.zzb;
            context.registerReceiver(zzgVar, intentFilter);
        }
        this.zzf = true;
    }

    public /* synthetic */ zzg(zzh zzhVar, PurchasesUpdatedListener purchasesUpdatedListener, AlternativeBillingListener alternativeBillingListener, zzar zzarVar, zzf zzfVar) {
        this.zza = zzhVar;
        this.zzb = purchasesUpdatedListener;
        this.zze = zzarVar;
    }
}
