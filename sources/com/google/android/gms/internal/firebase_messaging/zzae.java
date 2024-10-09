package com.google.android.gms.internal.firebase_messaging;

import com.google.firebase.encoders.ObjectEncoder;
import java.io.OutputStream;
import java.util.Map;

/* loaded from: classes.dex */
public final class zzae {
    private final Map zza;
    private final Map zzb;
    private final ObjectEncoder zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzae(Map map, Map map2, ObjectEncoder objectEncoder) {
        this.zza = map;
        this.zzb = map2;
        this.zzc = objectEncoder;
    }

    public final void zza(Object obj, OutputStream outputStream) {
        new zzab(outputStream, this.zza, this.zzb, this.zzc).zzf(obj);
    }
}
