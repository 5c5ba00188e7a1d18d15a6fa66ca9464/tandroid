package com.google.android.recaptcha.internal;

import java.util.HashMap;
import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public final class zzcl {
    private final zzaa zza;
    private final zzck zzb;
    private final HashMap zzc;
    private final zzcd zzd;
    private final zzag zze;

    public zzcl(zzcd zzcdVar, zzag zzagVar, zzaa zzaaVar) {
        this.zzd = zzcdVar;
        this.zze = zzagVar;
        this.zza = zzaaVar;
        zzck zzckVar = new zzck();
        this.zzb = zzckVar;
        HashMap hashMap = new HashMap();
        this.zzc = hashMap;
        zzckVar.zze(NotificationCenter.needDeleteDialog, hashMap);
    }

    public final zzaa zza() {
        return this.zza;
    }

    public final zzck zzb() {
        return this.zzb;
    }

    public final void zzc() {
        this.zzb.zzd();
        this.zzb.zze(NotificationCenter.needDeleteDialog, this.zzc);
    }

    public final zzag zzd() {
        return this.zze;
    }

    public final zzcd zze() {
        return this.zzd;
    }

    public final void zzf(int i, Object obj) {
        this.zzc.put(Integer.valueOf(i - 2), obj);
    }
}
