package com.google.android.gms.internal.mlkit_vision_label;

import com.google.android.gms.common.internal.Objects;
/* loaded from: classes.dex */
public final class zzjt {
    private final zzjr zza;
    private final Integer zzb;
    private final Integer zzc;
    private final Boolean zzd;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzjt(zzjq zzjqVar, zzjs zzjsVar) {
        zzjr zzjrVar;
        Integer num;
        zzjrVar = zzjqVar.zza;
        this.zza = zzjrVar;
        num = zzjqVar.zzb;
        this.zzb = num;
        this.zzc = null;
        this.zzd = null;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzjt) {
            zzjt zzjtVar = (zzjt) obj;
            return Objects.equal(this.zza, zzjtVar.zza) && Objects.equal(this.zzb, zzjtVar.zzb) && Objects.equal(null, null) && Objects.equal(null, null);
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zza, this.zzb, null, null);
    }

    public final zzjr zza() {
        return this.zza;
    }

    public final Integer zzb() {
        return this.zzb;
    }
}
