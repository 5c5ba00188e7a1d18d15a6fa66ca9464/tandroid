package com.google.android.gms.internal.play_billing;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzda {
    public static final int zza(int i, Object obj, Object obj2) {
        zzcz zzczVar = (zzcz) obj;
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj2);
        if (zzczVar.isEmpty()) {
            return 0;
        }
        Iterator it = zzczVar.entrySet().iterator();
        if (!it.hasNext()) {
            return 0;
        }
        Map.Entry entry = (Map.Entry) it.next();
        entry.getKey();
        entry.getValue();
        throw null;
    }

    public static final Object zzb(Object obj, Object obj2) {
        zzcz zzczVar = (zzcz) obj;
        zzcz zzczVar2 = (zzcz) obj2;
        if (!zzczVar2.isEmpty()) {
            if (!zzczVar.zze()) {
                zzczVar = zzczVar.zzb();
            }
            zzczVar.zzd(zzczVar2);
        }
        return zzczVar;
    }
}
