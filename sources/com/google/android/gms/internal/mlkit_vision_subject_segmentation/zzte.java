package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import android.content.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        Iterator it = this.zza.iterator();
        while (it.hasNext()) {
            ((zztb) it.next()).zza(zztaVar);
        }
    }
}
