package com.google.android.gms.internal.mlkit_vision_common;
/* compiled from: com.google.mlkit:vision-common@@17.3.0 */
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
