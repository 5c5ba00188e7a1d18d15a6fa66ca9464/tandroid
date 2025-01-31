package com.google.android.recaptcha.internal;

import kotlin.jvm.internal.DefaultConstructorMarker;

/* loaded from: classes.dex */
public final class zzbd {
    public static final zzbc zza = new zzbc(null);
    private String zzb;
    private String zzc;
    private String zzd;

    private zzbd(zzbd zzbdVar) {
        this(zzbdVar.zzb, zzbdVar.zzc);
        this.zzd = zzbdVar.zzd;
    }

    private zzbd(String str, String str2) {
        this.zzb = str;
        this.zzc = str2;
    }

    public /* synthetic */ zzbd(String str, String str2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2);
    }

    public final zzbb zza(zzne zzneVar) {
        return new zzbb(zzneVar, this.zzb, this.zzc, this.zzd, null);
    }

    public final zzbd zzb() {
        return new zzbd(this);
    }

    public final zzbd zzc(String str) {
        this.zzd = str;
        return this;
    }

    public final String zzd() {
        return this.zzc;
    }
}
