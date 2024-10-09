package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
final class zzcw implements zzdd {
    private final zzdd[] zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzcw(zzdd... zzddVarArr) {
        this.zza = zzddVarArr;
    }

    @Override // com.google.android.gms.internal.play_billing.zzdd
    public final zzdc zzb(Class cls) {
        zzdd[] zzddVarArr = this.zza;
        for (int i = 0; i < 2; i++) {
            zzdd zzddVar = zzddVarArr[i];
            if (zzddVar.zzc(cls)) {
                return zzddVar.zzb(cls);
            }
        }
        throw new UnsupportedOperationException("No factory is available for message type: ".concat(cls.getName()));
    }

    @Override // com.google.android.gms.internal.play_billing.zzdd
    public final boolean zzc(Class cls) {
        zzdd[] zzddVarArr = this.zza;
        for (int i = 0; i < 2; i++) {
            if (zzddVarArr[i].zzc(cls)) {
                return true;
            }
        }
        return false;
    }
}
