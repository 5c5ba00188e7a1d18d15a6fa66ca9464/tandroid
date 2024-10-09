package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
final class zzs extends zzo {
    private final zzu zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(zzu zzuVar, int i) {
        super(zzuVar.size(), i);
        this.zza = zzuVar;
    }

    @Override // com.google.android.gms.internal.play_billing.zzo
    protected final Object zza(int i) {
        return this.zza.get(i);
    }
}
