package com.google.android.gms.internal.mlkit_vision_label;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;

/* loaded from: classes.dex */
final class zzhz implements ObjectEncoder {
    static final zzhz zza = new zzhz();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("errorSpace");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        zzb = builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("errorCode");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        zzc = builder2.withProperty(zzciVar2.zzb()).build();
    }

    private zzhz() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }
}
