package com.google.android.gms.internal.play_billing;

import sun.misc.Unsafe;
/* compiled from: com.android.billingclient:billing@@6.0.1 */
/* loaded from: classes.dex */
final class zzeo extends zzep {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzeo(Unsafe unsafe) {
        super(unsafe);
    }

    @Override // com.google.android.gms.internal.play_billing.zzep
    public final double zza(Object obj, long j) {
        return Double.longBitsToDouble(this.zza.getLong(obj, j));
    }

    @Override // com.google.android.gms.internal.play_billing.zzep
    public final float zzb(Object obj, long j) {
        return Float.intBitsToFloat(this.zza.getInt(obj, j));
    }

    @Override // com.google.android.gms.internal.play_billing.zzep
    public final void zzc(Object obj, long j, boolean z) {
        if (zzeq.zzb) {
            zzeq.zzD(obj, j, r3 ? (byte) 1 : (byte) 0);
        } else {
            zzeq.zzE(obj, j, r3 ? (byte) 1 : (byte) 0);
        }
    }

    @Override // com.google.android.gms.internal.play_billing.zzep
    public final void zzd(Object obj, long j, byte b) {
        if (zzeq.zzb) {
            zzeq.zzD(obj, j, b);
        } else {
            zzeq.zzE(obj, j, b);
        }
    }

    @Override // com.google.android.gms.internal.play_billing.zzep
    public final void zze(Object obj, long j, double d) {
        this.zza.putLong(obj, j, Double.doubleToLongBits(d));
    }

    @Override // com.google.android.gms.internal.play_billing.zzep
    public final void zzf(Object obj, long j, float f) {
        this.zza.putInt(obj, j, Float.floatToIntBits(f));
    }

    @Override // com.google.android.gms.internal.play_billing.zzep
    public final boolean zzg(Object obj, long j) {
        if (zzeq.zzb) {
            return zzeq.zzt(obj, j);
        }
        return zzeq.zzu(obj, j);
    }
}
