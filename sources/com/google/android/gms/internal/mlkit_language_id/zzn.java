package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
final class zzn extends zzi {
    private final zzk zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(zzk zzkVar, int i) {
        super(zzkVar.size(), i);
        this.zza = zzkVar;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzi
    protected final Object zza(int i) {
        return this.zza.get(i);
    }
}
