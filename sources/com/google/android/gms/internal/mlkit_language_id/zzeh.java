package com.google.android.gms.internal.mlkit_language_id;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.android.gms.internal.mlkit_language_id.zzeo;
import java.util.Map;

/* loaded from: classes.dex */
final class zzeh extends zzee {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    public final int zza(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        throw new NoSuchMethodError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    public final zzej zza(Object obj) {
        return ((zzeo.zzc) obj).zzc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    public final void zza(zzib zzibVar, Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        throw new NoSuchMethodError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    public final boolean zza(zzfz zzfzVar) {
        return zzfzVar instanceof zzeo.zzc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    public final zzej zzb(Object obj) {
        zzeo.zzc zzcVar = (zzeo.zzc) obj;
        if (zzcVar.zzc.zzc()) {
            zzcVar.zzc = (zzej) zzcVar.zzc.clone();
        }
        return zzcVar.zzc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzee
    public final void zzc(Object obj) {
        zza(obj).zzb();
    }
}
