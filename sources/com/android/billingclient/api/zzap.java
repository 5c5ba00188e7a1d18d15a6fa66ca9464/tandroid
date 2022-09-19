package com.android.billingclient.api;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import com.google.android.gms.internal.play_billing.zzb;
import java.util.concurrent.Callable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@5.0.0 */
/* loaded from: classes.dex */
public final class zzap implements ServiceConnection {
    final /* synthetic */ BillingClientImpl zza;
    private final Object zzb = new Object();
    private boolean zzc = false;
    private BillingClientStateListener zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
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
        zzb.zzn("BillingClient", "Billing service connected.");
        BillingClientImpl.zzB(this.zza, com.google.android.gms.internal.play_billing.zzd.zzo(iBinder));
        BillingClientImpl billingClientImpl = this.zza;
        if (BillingClientImpl.zzp(billingClientImpl, new Callable() { // from class: com.android.billingclient.api.zzam
            @Override // java.util.concurrent.Callable
            public final Object call() {
                zzap.this.zza();
                return null;
            }
        }, 30000L, new Runnable() { // from class: com.android.billingclient.api.zzan
            @Override // java.lang.Runnable
            public final void run() {
                zzap.this.zzb();
            }
        }, BillingClientImpl.zzf(billingClientImpl)) == null) {
            zzd(BillingClientImpl.zzh(this.zza));
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        zzb.zzo("BillingClient", "Billing service disconnected.");
        BillingClientImpl.zzB(this.zza, null);
        BillingClientImpl.zzq(this.zza, 0);
        synchronized (this.zzb) {
            BillingClientStateListener billingClientStateListener = this.zzd;
            if (billingClientStateListener != null) {
                billingClientStateListener.onBillingServiceDisconnected();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0168  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final /* synthetic */ Object zza() throws Exception {
        Bundle bundle;
        int i;
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
            int i2 = 3;
            try {
                String packageName = BillingClientImpl.zzb(this.zza).getPackageName();
                int i3 = 17;
                i = 3;
                while (true) {
                    if (i3 < 3) {
                        i3 = 0;
                        break;
                    }
                    if (bundle == null) {
                        try {
                            i = BillingClientImpl.zzj(this.zza).zzr(i3, packageName, "subs");
                        } catch (Exception e) {
                            e = e;
                            i2 = i;
                            zzb.zzp("BillingClient", "Exception while checking if billing is supported; try to reconnect", e);
                            BillingClientImpl.zzq(this.zza, 0);
                            BillingClientImpl.zzB(this.zza, null);
                            i = i2;
                            if (i != 0) {
                            }
                            return null;
                        }
                    } else {
                        i = BillingClientImpl.zzj(this.zza).zzc(i3, packageName, "subs", bundle);
                    }
                    if (i == 0) {
                        break;
                    }
                    i3--;
                }
                boolean z = true;
                BillingClientImpl.zzC(this.zza, i3 >= 5);
                BillingClientImpl.zzD(this.zza, i3 >= 3);
                if (i3 < 3) {
                    zzb.zzn("BillingClient", "In-app billing API does not support subscription on this device.");
                }
                int i4 = 17;
                while (true) {
                    if (i4 < 3) {
                        break;
                    }
                    if (bundle == null) {
                        i = BillingClientImpl.zzj(this.zza).zzr(i4, packageName, "inapp");
                    } else {
                        i = BillingClientImpl.zzj(this.zza).zzc(i4, packageName, "inapp", bundle);
                    }
                    if (i == 0) {
                        BillingClientImpl.zzr(this.zza, i4);
                        break;
                    }
                    i4--;
                }
                BillingClientImpl billingClientImpl = this.zza;
                BillingClientImpl.zzx(billingClientImpl, BillingClientImpl.zza(billingClientImpl) >= 17);
                BillingClientImpl billingClientImpl2 = this.zza;
                BillingClientImpl.zzw(billingClientImpl2, BillingClientImpl.zza(billingClientImpl2) >= 16);
                BillingClientImpl billingClientImpl3 = this.zza;
                BillingClientImpl.zzv(billingClientImpl3, BillingClientImpl.zza(billingClientImpl3) >= 15);
                BillingClientImpl billingClientImpl4 = this.zza;
                BillingClientImpl.zzu(billingClientImpl4, BillingClientImpl.zza(billingClientImpl4) >= 14);
                BillingClientImpl billingClientImpl5 = this.zza;
                BillingClientImpl.zzt(billingClientImpl5, BillingClientImpl.zza(billingClientImpl5) >= 12);
                BillingClientImpl billingClientImpl6 = this.zza;
                BillingClientImpl.zzs(billingClientImpl6, BillingClientImpl.zza(billingClientImpl6) >= 10);
                BillingClientImpl billingClientImpl7 = this.zza;
                BillingClientImpl.zzA(billingClientImpl7, BillingClientImpl.zza(billingClientImpl7) >= 9);
                BillingClientImpl billingClientImpl8 = this.zza;
                BillingClientImpl.zzz(billingClientImpl8, BillingClientImpl.zza(billingClientImpl8) >= 8);
                BillingClientImpl billingClientImpl9 = this.zza;
                if (BillingClientImpl.zza(billingClientImpl9) < 6) {
                    z = false;
                }
                BillingClientImpl.zzy(billingClientImpl9, z);
                if (BillingClientImpl.zza(this.zza) < 3) {
                    zzb.zzo("BillingClient", "In-app billing API version 3 is not supported on this device.");
                }
                if (i == 0) {
                    BillingClientImpl.zzq(this.zza, 2);
                } else {
                    BillingClientImpl.zzq(this.zza, 0);
                    BillingClientImpl.zzB(this.zza, null);
                }
            } catch (Exception e2) {
                e = e2;
            }
            if (i != 0) {
                zzd(zzbb.zzl);
            } else {
                zzd(zzbb.zza);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zzb() {
        BillingClientImpl.zzq(this.zza, 0);
        BillingClientImpl.zzB(this.zza, null);
        zzd(zzbb.zzn);
    }
}
