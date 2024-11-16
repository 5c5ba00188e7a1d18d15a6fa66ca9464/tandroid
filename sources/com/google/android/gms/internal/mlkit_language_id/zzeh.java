package com.google.android.gms.internal.mlkit_language_id;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.android.gms.internal.mlkit_language_id.zzeo;
import java.util.Map;

/* loaded from: classes.dex */
final class zzeh extends zzee {
    zzeh() {
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    final int zza(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        throw new NoSuchMethodError();
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    final zzej zza(Object obj) {
        return ((zzeo.zzc) obj).zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    final void zza(zzib zzibVar, Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        throw new NoSuchMethodError();
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    final boolean zza(zzfz zzfzVar) {
        return zzfzVar instanceof zzeo.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    final zzej zzb(Object obj) {
        zzeo.zzc zzcVar = (zzeo.zzc) obj;
        if (zzcVar.zzc.zzc()) {
            zzcVar.zzc = (zzej) zzcVar.zzc.clone();
        }
        return zzcVar.zzc;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    final void zzc(Object obj) {
        zza(obj).zzb();
    }
}
