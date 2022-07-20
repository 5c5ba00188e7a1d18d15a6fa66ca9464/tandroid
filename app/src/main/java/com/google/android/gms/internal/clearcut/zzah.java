package com.google.android.gms.internal.clearcut;
/* loaded from: classes.dex */
public final /* synthetic */ class zzah implements zzam {
    private final String zzea;
    private final boolean zzeb = false;

    public zzah(String str, boolean z) {
        this.zzea = str;
    }

    @Override // com.google.android.gms.internal.clearcut.zzam
    public final Object zzp() {
        Boolean valueOf;
        valueOf = Boolean.valueOf(zzy.zza(zzae.zzh.getContentResolver(), this.zzea, this.zzeb));
        return valueOf;
    }
}
