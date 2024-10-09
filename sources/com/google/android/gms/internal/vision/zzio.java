package com.google.android.gms.internal.vision;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.android.gms.internal.vision.zzjb;
import java.util.Collections;
import java.util.Map;

/* loaded from: classes.dex */
public class zzio {
    private static volatile zzio zzc;
    private static volatile zzio zzd;
    private static final zzio zze = new zzio(true);
    private final Map zzf = Collections.emptyMap();

    /* loaded from: classes.dex */
    private static final class zza {
        private final Object zza;
        private final int zzb;

        zza(Object obj, int i) {
            this.zza = obj;
            this.zzb = i;
        }

        public final boolean equals(Object obj) {
            if (!(obj instanceof zza)) {
                return false;
            }
            zza zzaVar = (zza) obj;
            return this.zza == zzaVar.zza && this.zzb == zzaVar.zzb;
        }

        public final int hashCode() {
            return (System.identityHashCode(this.zza) * 65535) + this.zzb;
        }
    }

    private zzio(boolean z) {
    }

    public static zzio zzb() {
        zzio zzioVar = zzc;
        if (zzioVar == null) {
            synchronized (zzio.class) {
                try {
                    zzioVar = zzc;
                    if (zzioVar == null) {
                        zzioVar = zze;
                        zzc = zzioVar;
                    }
                } finally {
                }
            }
        }
        return zzioVar;
    }

    public static zzio zzc() {
        zzio zzioVar = zzd;
        if (zzioVar != null) {
            return zzioVar;
        }
        synchronized (zzio.class) {
            try {
                zzio zzioVar2 = zzd;
                if (zzioVar2 != null) {
                    return zzioVar2;
                }
                zzio zza2 = zziz.zza(zzio.class);
                zzd = zza2;
                return zza2;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public final zzjb.zze zza(zzkk zzkkVar, int i) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.zzf.get(new zza(zzkkVar, i)));
        return null;
    }
}
