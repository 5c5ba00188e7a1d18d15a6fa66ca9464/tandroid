package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
final class zzdo implements zzdc {
    private final zzdf zza;
    private final String zzb;
    private final Object[] zzc;
    private final int zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzdo(zzdf zzdfVar, String str, Object[] objArr) {
        this.zza = zzdfVar;
        this.zzb = str;
        this.zzc = objArr;
        char charAt = str.charAt(0);
        if (charAt < 55296) {
            this.zzd = charAt;
            return;
        }
        int i = charAt & 8191;
        int i2 = 1;
        int i3 = 13;
        while (true) {
            int i4 = i2 + 1;
            char charAt2 = str.charAt(i2);
            if (charAt2 < 55296) {
                this.zzd = i | (charAt2 << i3);
                return;
            } else {
                i |= (charAt2 & 8191) << i3;
                i3 += 13;
                i2 = i4;
            }
        }
    }

    @Override // com.google.android.gms.internal.play_billing.zzdc
    public final zzdf zza() {
        return this.zza;
    }

    @Override // com.google.android.gms.internal.play_billing.zzdc
    public final boolean zzb() {
        return (this.zzd & 2) == 2;
    }

    @Override // com.google.android.gms.internal.play_billing.zzdc
    public final int zzc() {
        return (this.zzd & 1) != 0 ? 1 : 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String zzd() {
        return this.zzb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object[] zze() {
        return this.zzc;
    }
}
