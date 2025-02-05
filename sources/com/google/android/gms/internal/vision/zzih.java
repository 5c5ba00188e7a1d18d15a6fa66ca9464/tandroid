package com.google.android.gms.internal.vision;

import org.telegram.tgnet.ConnectionsManager;

/* loaded from: classes.dex */
final class zzih extends zzif {
    private final byte[] zzd;
    private final boolean zze;
    private int zzf;
    private int zzg;
    private int zzh;
    private int zzi;
    private int zzk;

    private zzih(byte[] bArr, int i, int i2, boolean z) {
        super();
        this.zzk = ConnectionsManager.DEFAULT_DATACENTER_ID;
        this.zzd = bArr;
        this.zzf = i2 + i;
        this.zzh = i;
        this.zzi = i;
        this.zze = z;
    }

    private final void zzz() {
        int i = this.zzf + this.zzg;
        this.zzf = i;
        int i2 = i - this.zzi;
        int i3 = this.zzk;
        if (i2 <= i3) {
            this.zzg = 0;
            return;
        }
        int i4 = i2 - i3;
        this.zzg = i4;
        this.zzf = i - i4;
    }

    @Override // com.google.android.gms.internal.vision.zzif
    public final int zzc(int i) {
        if (i < 0) {
            throw zzjk.zzb();
        }
        int zzu = i + zzu();
        int i2 = this.zzk;
        if (zzu > i2) {
            throw zzjk.zza();
        }
        this.zzk = zzu;
        zzz();
        return i2;
    }

    @Override // com.google.android.gms.internal.vision.zzif
    public final int zzu() {
        return this.zzh - this.zzi;
    }
}
