package com.google.android.gms.internal.mlkit_common;

/* loaded from: classes.dex */
final class zzao extends zzag {
    private final zzaq zza;

    zzao(zzaq zzaqVar, int i) {
        super(zzaqVar.size(), i);
        this.zza = zzaqVar;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzag
    protected final Object zza(int i) {
        return this.zza.get(i);
    }
}
