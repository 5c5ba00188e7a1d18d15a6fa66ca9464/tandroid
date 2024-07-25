package com.android.billingclient.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import com.google.android.gms.internal.play_billing.zzb;
import com.google.android.gms.internal.play_billing.zzd;
import com.google.android.gms.internal.play_billing.zze;
import com.google.android.gms.internal.play_billing.zzgd;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzaf implements ServiceConnection {
    final /* synthetic */ BillingClientImpl zza;
    private final Object zzb = new Object();
    private boolean zzc = false;
    private BillingClientStateListener zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzaf(BillingClientImpl billingClientImpl, BillingClientStateListener billingClientStateListener, zzae zzaeVar) {
        this.zza = billingClientImpl;
        this.zzd = billingClientStateListener;
    }

    private final void zzd(BillingResult billingResult) {
        synchronized (this.zzb) {
            BillingClientStateListener billingClientStateListener = this.zzd;
            if (billingClientStateListener != null) {
                billingClientStateListener.onBillingSetupFinished(billingResult);
            }
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Handler zzO;
        Future zzS;
        BillingResult zzQ;
        zzar zzarVar;
        zzb.zzi("BillingClient", "Billing service connected.");
        this.zza.zzg = zzd.zzn(iBinder);
        BillingClientImpl billingClientImpl = this.zza;
        Callable callable = new Callable() { // from class: com.android.billingclient.api.zzac
            @Override // java.util.concurrent.Callable
            public final Object call() {
                zzaf.this.zza();
                return null;
            }
        };
        Runnable runnable = new Runnable() { // from class: com.android.billingclient.api.zzad
            @Override // java.lang.Runnable
            public final void run() {
                zzaf.this.zzb();
            }
        };
        zzO = billingClientImpl.zzO();
        zzS = billingClientImpl.zzS(callable, 30000L, runnable, zzO);
        if (zzS == null) {
            zzQ = this.zza.zzQ();
            zzarVar = this.zza.zzf;
            zzarVar.zza(zzaq.zza(25, 6, zzQ));
            zzd(zzQ);
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        zzar zzarVar;
        zzb.zzj("BillingClient", "Billing service disconnected.");
        zzarVar = this.zza.zzf;
        zzarVar.zzc(zzgd.zzw());
        this.zza.zzg = null;
        this.zza.zza = 0;
        synchronized (this.zzb) {
            BillingClientStateListener billingClientStateListener = this.zzd;
            if (billingClientStateListener != null) {
                billingClientStateListener.onBillingServiceDisconnected();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:104:0x01cc  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x01df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final /* synthetic */ Object zza() throws Exception {
        Bundle bundle;
        int i;
        int i2;
        zzar zzarVar;
        zzar zzarVar2;
        Context context;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        zze zzeVar;
        int i16;
        zze zzeVar2;
        zze zzeVar3;
        zze zzeVar4;
        synchronized (this.zzb) {
            if (this.zzc) {
                return null;
            }
            if (TextUtils.isEmpty(null)) {
                bundle = null;
            } else {
                bundle = new Bundle();
                bundle.putString("accountName", null);
            }
            int i17 = 3;
            try {
                context = this.zza.zze;
                String packageName = context.getPackageName();
                int i18 = 20;
                i2 = 3;
                while (true) {
                    if (i18 < 3) {
                        i18 = 0;
                        break;
                    }
                    if (bundle == null) {
                        try {
                            zzeVar3 = this.zza.zzg;
                            i2 = zzeVar3.zzq(i18, packageName, "subs");
                        } catch (Exception e) {
                            e = e;
                            i17 = i2;
                            zzb.zzk("BillingClient", "Exception while checking if billing is supported; try to reconnect", e);
                            this.zza.zza = 0;
                            this.zza.zzg = null;
                            i = 42;
                            i2 = i17;
                            if (i2 != 0) {
                            }
                            return null;
                        }
                    } else {
                        zzeVar4 = this.zza.zzg;
                        i2 = zzeVar4.zzc(i18, packageName, "subs", bundle);
                    }
                    if (i2 == 0) {
                        zzb.zzi("BillingClient", "highestLevelSupportedForSubs: " + i18);
                        break;
                    }
                    i18--;
                }
                boolean z = true;
                this.zza.zzj = i18 >= 5;
                this.zza.zzi = i18 >= 3;
                if (i18 < 3) {
                    zzb.zzi("BillingClient", "In-app billing API does not support subscription on this device.");
                    i = 9;
                } else {
                    i = 1;
                }
                int i19 = 20;
                while (true) {
                    if (i19 < 3) {
                        break;
                    }
                    if (bundle == null) {
                        zzeVar2 = this.zza.zzg;
                        i2 = zzeVar2.zzq(i19, packageName, "inapp");
                    } else {
                        zzeVar = this.zza.zzg;
                        i2 = zzeVar.zzc(i19, packageName, "inapp", bundle);
                    }
                    if (i2 == 0) {
                        this.zza.zzk = i19;
                        i16 = this.zza.zzk;
                        zzb.zzi("BillingClient", "mHighestLevelSupportedForInApp: " + i16);
                        break;
                    }
                    i19--;
                }
                BillingClientImpl billingClientImpl = this.zza;
                i3 = billingClientImpl.zzk;
                billingClientImpl.zzw = i3 >= 20;
                BillingClientImpl billingClientImpl2 = this.zza;
                i4 = billingClientImpl2.zzk;
                billingClientImpl2.zzv = i4 >= 19;
                BillingClientImpl billingClientImpl3 = this.zza;
                i5 = billingClientImpl3.zzk;
                billingClientImpl3.zzu = i5 >= 18;
                BillingClientImpl billingClientImpl4 = this.zza;
                i6 = billingClientImpl4.zzk;
                billingClientImpl4.zzt = i6 >= 17;
                BillingClientImpl billingClientImpl5 = this.zza;
                i7 = billingClientImpl5.zzk;
                billingClientImpl5.zzs = i7 >= 16;
                BillingClientImpl billingClientImpl6 = this.zza;
                i8 = billingClientImpl6.zzk;
                billingClientImpl6.zzr = i8 >= 15;
                BillingClientImpl billingClientImpl7 = this.zza;
                i9 = billingClientImpl7.zzk;
                billingClientImpl7.zzq = i9 >= 14;
                BillingClientImpl billingClientImpl8 = this.zza;
                i10 = billingClientImpl8.zzk;
                billingClientImpl8.zzp = i10 >= 12;
                BillingClientImpl billingClientImpl9 = this.zza;
                i11 = billingClientImpl9.zzk;
                billingClientImpl9.zzo = i11 >= 10;
                BillingClientImpl billingClientImpl10 = this.zza;
                i12 = billingClientImpl10.zzk;
                billingClientImpl10.zzn = i12 >= 9;
                BillingClientImpl billingClientImpl11 = this.zza;
                i13 = billingClientImpl11.zzk;
                billingClientImpl11.zzm = i13 >= 8;
                BillingClientImpl billingClientImpl12 = this.zza;
                i14 = billingClientImpl12.zzk;
                if (i14 < 6) {
                    z = false;
                }
                billingClientImpl12.zzl = z;
                i15 = this.zza.zzk;
                if (i15 < 3) {
                    zzb.zzj("BillingClient", "In-app billing API version 3 is not supported on this device.");
                    i = 36;
                }
                if (i2 == 0) {
                    this.zza.zza = 2;
                } else {
                    this.zza.zza = 0;
                    this.zza.zzg = null;
                }
            } catch (Exception e2) {
                e = e2;
            }
            if (i2 != 0) {
                zzarVar2 = this.zza.zzf;
                zzarVar2.zzb(zzaq.zzb(6));
                zzd(zzat.zzl);
            } else {
                zzarVar = this.zza.zzf;
                BillingResult billingResult = zzat.zza;
                zzarVar.zza(zzaq.zza(i, 6, billingResult));
                zzd(billingResult);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzb() {
        zzar zzarVar;
        this.zza.zza = 0;
        this.zza.zzg = null;
        zzarVar = this.zza.zzf;
        BillingResult billingResult = zzat.zzn;
        zzarVar.zza(zzaq.zza(24, 6, billingResult));
        zzd(billingResult);
    }
}
