package com.google.android.gms.internal.mlkit_vision_common;
/* loaded from: classes.dex */
final class zzn extends zzh {
    private final zzp zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(zzp zzpVar, int i) {
        super(zzpVar.size(), i);
        this.zza = zzpVar;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.zzh
    protected final Object zza(int i) {
        return this.zza.get(i);
    }
}
