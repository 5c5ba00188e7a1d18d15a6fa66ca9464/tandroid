package com.google.android.gms.internal.play_billing;

import org.telegram.tgnet.ConnectionsManager;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
final class zzbc extends zzbe {
    private final byte[] zzb;
    private int zzc;
    private int zzd;
    private int zze;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzbc(byte[] bArr, int i, int i2, boolean z, zzbb zzbbVar) {
        super(null);
        this.zze = ConnectionsManager.DEFAULT_DATACENTER_ID;
        this.zzb = bArr;
        this.zzc = 0;
    }

    public final int zza(int i) throws zzci {
        int i2 = this.zze;
        this.zze = 0;
        int i3 = this.zzc + this.zzd;
        this.zzc = i3;
        if (i3 > 0) {
            this.zzd = i3;
            this.zzc = i3 - i3;
        } else {
            this.zzd = 0;
        }
        return i2;
    }
}
