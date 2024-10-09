package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;

/* loaded from: classes.dex */
final class zzei implements ObjectEncoder {
    static final zzei zza = new zzei();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;
    private static final FieldDescriptor zzd;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("logEventKey");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        zzb = builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("eventCount");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        zzc = builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("inferenceDurationStats");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        zzd = builder3.withProperty(zzciVar3.zzb()).build();
    }

    private zzei() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        zzdj zzdjVar = (zzdj) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        objectEncoderContext.add(zzb, zzdjVar.zza());
        objectEncoderContext.add(zzc, zzdjVar.zzc());
        objectEncoderContext.add(zzd, zzdjVar.zzb());
    }
}
