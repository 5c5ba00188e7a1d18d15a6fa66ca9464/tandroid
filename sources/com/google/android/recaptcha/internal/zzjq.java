package com.google.android.recaptcha.internal;

import java.util.List;

/* loaded from: classes.dex */
final class zzjq extends zzjs {
    private zzjq() {
        super(null);
    }

    /* synthetic */ zzjq(zzjp zzjpVar) {
        super(null);
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final List zza(Object obj, long j) {
        zzjb zzjbVar = (zzjb) zzlv.zzf(obj, j);
        if (zzjbVar.zzc()) {
            return zzjbVar;
        }
        int size = zzjbVar.size();
        zzjb zzd = zzjbVar.zzd(size == 0 ? 10 : size + size);
        zzlv.zzs(obj, j, zzd);
        return zzd;
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final void zzb(Object obj, long j) {
        ((zzjb) zzlv.zzf(obj, j)).zzb();
    }

    @Override // com.google.android.recaptcha.internal.zzjs
    final void zzc(Object obj, Object obj2, long j) {
        zzjb zzjbVar = (zzjb) zzlv.zzf(obj, j);
        zzjb zzjbVar2 = (zzjb) zzlv.zzf(obj2, j);
        int size = zzjbVar.size();
        int size2 = zzjbVar2.size();
        if (size > 0 && size2 > 0) {
            if (!zzjbVar.zzc()) {
                zzjbVar = zzjbVar.zzd(size2 + size);
            }
            zzjbVar.addAll(zzjbVar2);
        }
        if (size > 0) {
            zzjbVar2 = zzjbVar;
        }
        zzlv.zzs(obj, j, zzjbVar2);
    }
}
