package com.android.billingclient.api;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
public final class zzbc {
    private boolean zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzbc(zzbb zzbbVar) {
    }

    public final zzbc zza() {
        this.zza = true;
        return this;
    }

    public final zzbe zzb() {
        if (this.zza) {
            return new zzbe(true, false, null);
        }
        throw new IllegalArgumentException("Pending purchases for one-time products must be supported.");
    }
}
