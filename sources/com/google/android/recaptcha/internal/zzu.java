package com.google.android.recaptcha.internal;

import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.text.StringsKt__StringsKt;

/* loaded from: classes.dex */
public final class zzu implements Comparable {
    private int zza;
    private long zzb;
    private long zzc;

    public final String toString() {
        String padEnd$default;
        String padEnd$default2;
        String padEnd$default3;
        String padEnd$default4;
        padEnd$default = StringsKt__StringsKt.padEnd$default(String.valueOf(this.zzb / this.zza), 10, (char) 0, 2, null);
        padEnd$default2 = StringsKt__StringsKt.padEnd$default(String.valueOf(this.zzc), 10, (char) 0, 2, null);
        padEnd$default3 = StringsKt__StringsKt.padEnd$default(String.valueOf(this.zzb), 10, (char) 0, 2, null);
        padEnd$default4 = StringsKt__StringsKt.padEnd$default(String.valueOf(this.zza), 5, (char) 0, 2, null);
        return "avgExecutionTime: " + padEnd$default + " us| maxExecutionTime: " + padEnd$default2 + " us| totalTime: " + padEnd$default3 + " us| #Usages: " + padEnd$default4;
    }

    @Override // java.lang.Comparable
    /* renamed from: zza, reason: merged with bridge method [inline-methods] */
    public final int compareTo(zzu zzuVar) {
        int compareValues;
        compareValues = ComparisonsKt__ComparisonsKt.compareValues(Long.valueOf(this.zzb), Long.valueOf(zzuVar.zzb));
        return compareValues;
    }

    public final int zzb() {
        return this.zza;
    }

    public final long zzc() {
        return this.zzc;
    }

    public final long zzd() {
        return this.zzb;
    }

    public final void zze(long j) {
        this.zzc = j;
    }

    public final void zzf(long j) {
        this.zzb = j;
    }

    public final void zzg(int i) {
        this.zza = i;
    }
}
