package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;

/* loaded from: classes.dex */
final class zzhp implements ObjectEncoder {
    static final zzhp zza = new zzhp();
    private static final FieldDescriptor zzb;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("api");
        zzbz zzbzVar = new zzbz();
        zzbzVar.zza(1);
        zzb = builder.withProperty(zzbzVar.zzb()).build();
    }

    private zzhp() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }
}
