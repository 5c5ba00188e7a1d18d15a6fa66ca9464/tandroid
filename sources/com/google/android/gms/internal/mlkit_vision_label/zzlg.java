package com.google.android.gms.internal.mlkit_vision_label;

import com.google.android.gms.common.internal.Objects;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
public final class zzlg {
    private final Integer zza = null;
    private final Float zzb;
    private final zzkm zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzlg(zzle zzleVar, zzlf zzlfVar) {
        Float f;
        f = zzleVar.zza;
        this.zzb = f;
        this.zzc = null;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzlg) {
            zzlg zzlgVar = (zzlg) obj;
            Integer num = zzlgVar.zza;
            return Objects.equal(null, null) && Objects.equal(this.zzb, zzlgVar.zzb) && Objects.equal(null, null);
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hashCode(null, this.zzb, null);
    }

    @zzcm(zza = 2)
    public final Float zza() {
        return this.zzb;
    }
}
