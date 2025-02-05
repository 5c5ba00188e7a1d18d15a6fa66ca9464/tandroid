package com.google.android.gms.internal.clearcut;

import android.net.Uri;

/* loaded from: classes.dex */
public final class zzao {
    private final String zzef;
    private final Uri zzeg;
    private final String zzeh;
    private final String zzei;
    private final boolean zzej;
    private final boolean zzek;

    public zzao(Uri uri) {
        this(null, uri, "", "", false, false);
    }

    private zzao(String str, Uri uri, String str2, String str3, boolean z, boolean z2) {
        this.zzef = str;
        this.zzeg = uri;
        this.zzeh = str2;
        this.zzei = str3;
        this.zzej = z;
        this.zzek = z2;
    }

    public final zzae zza(String str, Object obj, zzan zzanVar) {
        zzae zza;
        zza = zzae.zza(this, str, obj, zzanVar);
        return zza;
    }

    public final zzae zza(String str, String str2) {
        zzae zza;
        zza = zzae.zza(this, str, (String) null);
        return zza;
    }

    public final zzae zzc(String str, boolean z) {
        zzae zza;
        zza = zzae.zza(this, str, false);
        return zza;
    }

    public final zzao zzc(String str) {
        boolean z = this.zzej;
        if (z) {
            throw new IllegalStateException("Cannot set GServices prefix and skip GServices");
        }
        return new zzao(this.zzef, this.zzeg, str, this.zzei, z, this.zzek);
    }

    public final zzao zzd(String str) {
        return new zzao(this.zzef, this.zzeg, this.zzeh, str, this.zzej, this.zzek);
    }
}
