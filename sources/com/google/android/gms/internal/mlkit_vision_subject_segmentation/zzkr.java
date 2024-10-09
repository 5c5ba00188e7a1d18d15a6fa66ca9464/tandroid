package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;

/* loaded from: classes.dex */
final class zzkr implements ObjectEncoder {
    static final zzkr zza = new zzkr();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;
    private static final FieldDescriptor zzd;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("errorCode");
        zzbz zzbzVar = new zzbz();
        zzbzVar.zza(1);
        zzb = builder.withProperty(zzbzVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("durationMs");
        zzbz zzbzVar2 = new zzbz();
        zzbzVar2.zza(2);
        zzc = builder2.withProperty(zzbzVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("subjectSegmenterOptions");
        zzbz zzbzVar3 = new zzbz();
        zzbzVar3.zza(3);
        zzd = builder3.withProperty(zzbzVar3.zzb()).build();
    }

    private zzkr() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        zzrf zzrfVar = (zzrf) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        objectEncoderContext.add(zzb, zzrfVar.zza());
        objectEncoderContext.add(zzc, zzrfVar.zzc());
        objectEncoderContext.add(zzd, zzrfVar.zzb());
    }
}
