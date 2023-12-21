package com.google.android.gms.internal.mlkit_common;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
/* compiled from: com.google.mlkit:common@@18.10.0 */
/* loaded from: classes.dex */
public final class zzsn implements zzsk {
    final List zza;

    public zzsn(Context context, zzsm zzsmVar) {
        ArrayList arrayList = new ArrayList();
        this.zza = arrayList;
        if (zzsmVar.zzc()) {
            arrayList.add(new zzta(context, zzsmVar));
        }
    }
}
