package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;

/* loaded from: classes.dex */
final class zzkp implements ObjectEncoder {
    static final zzkp zza = new zzkp();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("errorCode");
        zzbz zzbzVar = new zzbz();
        zzbzVar.zza(1);
        zzb = builder.withProperty(zzbzVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("subjectSegmenterOptions");
        zzbz zzbzVar2 = new zzbz();
        zzbzVar2.zza(2);
        zzc = builder2.withProperty(zzbzVar2.zzb()).build();
    }

    private zzkp() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        zzqz zzqzVar = (zzqz) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        objectEncoderContext.add(zzb, zzqzVar.zza());
        objectEncoderContext.add(zzc, zzqzVar.zzb());
    }
}
