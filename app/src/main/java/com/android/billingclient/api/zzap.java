package com.android.billingclient.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import com.google.android.gms.internal.play_billing.zzb;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final class zzap implements ServiceConnection {
    final /* synthetic */ BillingClientImpl zza;
    private final Object zzb = new Object();
    private boolean zzc = false;
    private BillingClientStateListener zzd;

    public /* synthetic */ zzap(BillingClientImpl billingClientImpl, BillingClientStateListener billingClientStateListener, zzao zzaoVar) {
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
        Handler zzF;
        Future zzJ;
        BillingResult zzH;
        zzb.zzn("BillingClient", "Billing service connected.");
        this.zza.zzf = com.google.android.gms.internal.play_billing.zzd.zzo(iBinder);
        BillingClientImpl billingClientImpl = this.zza;
        Callable callable = new Callable() { // from class: com.android.billingclient.api.zzam
            @Override // java.util.concurrent.Callable
            public final Object call() {
                zzap.this.zza();
                return null;
            }
        };
        Runnable runnable = new Runnable() { // from class: com.android.billingclient.api.zzan
            @Override // java.lang.Runnable
            public final void run() {
                zzap.this.zzb();
            }
        };
        zzF = billingClientImpl.zzF();
        zzJ = billingClientImpl.zzJ(callable, 30000L, runnable, zzF);
        if (zzJ == null) {
            zzH = this.zza.zzH();
            zzd(zzH);
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        zzb.zzo("BillingClient", "Billing service disconnected.");
        this.zza.zzf = null;
        this.zza.zza = 0;
        synchronized (this.zzb) {
            BillingClientStateListener billingClientStateListener = this.zzd;
            if (billingClientStateListener != null) {
                billingClientStateListener.onBillingServiceDisconnected();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:90:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0168  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final /* synthetic */ Object zza() throws Exception {
        Bundle bundle;
        int i;
        Exception e;
        Context context;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        com.google.android.gms.internal.play_billing.zze zzeVar;
        com.google.android.gms.internal.play_billing.zze zzeVar2;
        com.google.android.gms.internal.play_billing.zze zzeVar3;
        com.google.android.gms.internal.play_billing.zze zzeVar4;
        synchronized (this.zzb) {
            if (this.zzc) {
                return null;
            }
            if (!TextUtils.isEmpty(null)) {
                bundle = new Bundle();
                bundle.putString("accountName", null);
            } else {
                bundle = null;
            }
            int i12 = 3;
            try {
                context = this.zza.zze;
                String packageName = context.getPackageName();
                int i13 = 17;
                i = 3;
                while (true) {
                    if (i13 < 3) {
                        i13 = 0;
                        break;
                    }
                    if (bundle == null) {
                        try {
                            zzeVar3 = this.zza.zzf;
                            i = zzeVar3.zzr(i13, packageName, "subs");
                        } catch (Exception e2) {
                            e = e2;
                            i12 = i;
                            zzb.zzp("BillingClient", "Exception while checking if billing is supported; try to reconnect", e);
                            this.zza.zza = 0;
                            this.zza.zzf = null;
                            i = i12;
                            if (i != 0) {
                            }
                            return null;
                        }
                    } else {
                        zzeVar4 = this.zza.zzf;
                        i = zzeVar4.zzc(i13, packageName, "subs", bundle);
                    }
                    if (i == 0) {
                        break;
                    }
                    i13--;
                }
                boolean z = true;
                this.zza.zzi = i13 >= 5;
                this.zza.zzh = i13 >= 3;
                if (i13 < 3) {
                    zzb.zzn("BillingClient", "In-app billing API does not support subscription on this device.");
                }
                int i14 = 17;
                while (true) {
                    if (i14 < 3) {
                        break;
                    }
                    if (bundle == null) {
                        zzeVar2 = this.zza.zzf;
                        i = zzeVar2.zzr(i14, packageName, "inapp");
                    } else {
                        zzeVar = this.zza.zzf;
                        i = zzeVar.zzc(i14, packageName, "inapp", bundle);
                    }
                    if (i == 0) {
                        this.zza.zzj = i14;
                        break;
                    }
                    i14--;
                }
                BillingClientImpl billingClientImpl = this.zza;
                i2 = billingClientImpl.zzj;
                billingClientImpl.zzs = i2 >= 17;
                BillingClientImpl billingClientImpl2 = this.zza;
                i3 = billingClientImpl2.zzj;
                billingClientImpl2.zzr = i3 >= 16;
                BillingClientImpl billingClientImpl3 = this.zza;
                i4 = billingClientImpl3.zzj;
                billingClientImpl3.zzq = i4 >= 15;
                BillingClientImpl billingClientImpl4 = this.zza;
                i5 = billingClientImpl4.zzj;
                billingClientImpl4.zzp = i5 >= 14;
                BillingClientImpl billingClientImpl5 = this.zza;
                i6 = billingClientImpl5.zzj;
                billingClientImpl5.zzo = i6 >= 12;
                BillingClientImpl billingClientImpl6 = this.zza;
                i7 = billingClientImpl6.zzj;
                billingClientImpl6.zzn = i7 >= 10;
                BillingClientImpl billingClientImpl7 = this.zza;
                i8 = billingClientImpl7.zzj;
                billingClientImpl7.zzm = i8 >= 9;
                BillingClientImpl billingClientImpl8 = this.zza;
                i9 = billingClientImpl8.zzj;
                billingClientImpl8.zzl = i9 >= 8;
                BillingClientImpl billingClientImpl9 = this.zza;
                i10 = billingClientImpl9.zzj;
                if (i10 < 6) {
                    z = false;
                }
                billingClientImpl9.zzk = z;
                i11 = this.zza.zzj;
                if (i11 < 3) {
                    zzb.zzo("BillingClient", "In-app billing API version 3 is not supported on this device.");
                }
                if (i == 0) {
                    this.zza.zza = 2;
                } else {
                    this.zza.zza = 0;
                    this.zza.zzf = null;
                }
            } catch (Exception e3) {
                e = e3;
            }
            if (i != 0) {
                zzd(zzbb.zzl);
            } else {
                zzd(zzbb.zza);
            }
            return null;
        }
    }

    public final /* synthetic */ void zzb() {
        this.zza.zza = 0;
        this.zza.zzf = null;
        zzd(zzbb.zzn);
    }
}
