package com.google.android.gms.internal.mlkit_vision_label;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;

/* loaded from: classes.dex */
final class zzhq implements ObjectEncoder {
    static final zzhq zza = new zzhq();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;
    private static final FieldDescriptor zzd;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("detectorOptions");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        zzb = builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("errorCode");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        zzc = builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("durationMs");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        zzd = builder3.withProperty(zzciVar3.zzb()).build();
    }

    private zzhq() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }
}
