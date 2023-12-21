package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public final class zzte implements zztb {
    final List zza;

    public zzte(Context context, zztd zztdVar) {
        ArrayList arrayList = new ArrayList();
        this.zza = arrayList;
        if (zztdVar.zzc()) {
            arrayList.add(new zztt(context, zztdVar));
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztb
    public final void zza(zzta zztaVar) {
        for (zztb zztbVar : this.zza) {
            zztbVar.zza(zztaVar);
        }
    }
}
