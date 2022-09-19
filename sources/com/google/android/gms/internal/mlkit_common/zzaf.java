package com.google.android.gms.internal.mlkit_common;
/* compiled from: com.google.mlkit:common@@17.0.0 */
/* loaded from: classes.dex */
final class zzaf<E> extends zzaa<E> {
    private final zzad<E> zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaf(zzad<E> zzadVar, int i) {
        super(zzadVar.size(), i);
        this.zza = zzadVar;
    }

    @Override // com.google.android.gms.internal.mlkit_common.zzaa
    protected final E zza(int i) {
        return this.zza.get(i);
    }
}
