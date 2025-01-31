package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
final class zzjv implements zzkc {
    private final zzkc[] zza;

    zzjv(zzkc... zzkcVarArr) {
        this.zza = zzkcVarArr;
    }

    @Override // com.google.android.recaptcha.internal.zzkc
    public final zzkb zzb(Class cls) {
        for (int i = 0; i < 2; i++) {
            zzkc zzkcVar = this.zza[i];
            if (zzkcVar.zzc(cls)) {
                return zzkcVar.zzb(cls);
            }
        }
        throw new UnsupportedOperationException("No factory is available for message type: ".concat(cls.getName()));
    }

    @Override // com.google.android.recaptcha.internal.zzkc
    public final boolean zzc(Class cls) {
        for (int i = 0; i < 2; i++) {
            if (this.zza[i].zzc(cls)) {
                return true;
            }
        }
        return false;
    }
}
