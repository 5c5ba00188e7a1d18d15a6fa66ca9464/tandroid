package com.google.android.gms.internal.mlkit_vision_common;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;

/* loaded from: classes.dex */
final class zzfc implements ObjectEncoder {
    static final zzfc zza = new zzfc();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("errorCode");
        zzae zzaeVar = new zzae();
        zzaeVar.zza(1);
        zzb = builder.withProperty(zzaeVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("durationMs");
        zzae zzaeVar2 = new zzae();
        zzaeVar2.zza(2);
        zzc = builder2.withProperty(zzaeVar2.zzb()).build();
    }

    private zzfc() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }
}
