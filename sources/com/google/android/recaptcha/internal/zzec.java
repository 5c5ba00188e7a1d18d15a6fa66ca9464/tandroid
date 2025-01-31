package com.google.android.recaptcha.internal;

import java.math.BigInteger;

/* loaded from: classes.dex */
public final class zzec {
    public static final zzeb zza = new zzeb(null);
    private static final zzea zzb = new zzea(11, ((long) Math.pow(2.0d, 32.0d)) ^ 20919936621L, (long) Math.pow(2.0d, 48.0d));
    private final zzea zzc;
    private long zzd;

    public zzec(long j, long j2, zzea zzeaVar) {
        this.zzc = zzeaVar;
        this.zzd = Math.abs(j);
    }

    public final long zza() {
        zzea zzeaVar = this.zzc;
        long longValue = (BigInteger.valueOf(zzeaVar.zzb()).multiply(BigInteger.valueOf(this.zzd)).mod(BigInteger.valueOf(zzeaVar.zza())).longValue() + 11) % this.zzc.zza();
        this.zzd = longValue;
        return longValue % 255;
    }
}
