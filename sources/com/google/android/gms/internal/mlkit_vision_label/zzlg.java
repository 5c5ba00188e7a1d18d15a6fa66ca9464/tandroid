package com.google.android.gms.internal.mlkit_vision_label;

import com.google.android.gms.common.internal.Objects;

/* loaded from: classes.dex */
public final class zzlg {
    private final Integer zza = null;
    private final Float zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzlg(zzle zzleVar, zzlf zzlfVar) {
        Float f;
        f = zzleVar.zza;
        this.zzb = f;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzlg)) {
            return false;
        }
        zzlg zzlgVar = (zzlg) obj;
        Integer num = zzlgVar.zza;
        return Objects.equal(null, null) && Objects.equal(this.zzb, zzlgVar.zzb) && Objects.equal(null, null);
    }

    public final int hashCode() {
        return Objects.hashCode(null, this.zzb, null);
    }

    public final Float zza() {
        return this.zzb;
    }
}
