package com.google.android.gms.internal.mlkit_vision_label;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class zzni implements zzno {
    final List zza;

    public zzni(Context context, zznh zznhVar) {
        ArrayList arrayList = new ArrayList();
        this.zza = arrayList;
        if (zznhVar.zzc()) {
            arrayList.add(new zznx(context, zznhVar));
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzno
    public final void zza(zznf zznfVar) {
        for (zzno zznoVar : this.zza) {
            zznoVar.zza(zznfVar);
        }
    }
}
