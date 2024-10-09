package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;

/* loaded from: classes.dex */
final class zzku implements ObjectEncoder {
    static final zzku zza = new zzku();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;
    private static final FieldDescriptor zzd;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("languageOption");
        zzbz zzbzVar = new zzbz();
        zzbzVar.zza(3);
        zzb = builder.withProperty(zzbzVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("isUsingLegacyApi");
        zzbz zzbzVar2 = new zzbz();
        zzbzVar2.zza(4);
        zzc = builder2.withProperty(zzbzVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("sdkVersion");
        zzbz zzbzVar3 = new zzbz();
        zzbzVar3.zza(5);
        zzd = builder3.withProperty(zzbzVar3.zzb()).build();
    }

    private zzku() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }
}
