package com.google.android.gms.internal.mlkit_vision_label;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;

/* loaded from: classes.dex */
final class zzgk implements ObjectEncoder {
    static final zzgk zza = new zzgk();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;
    private static final FieldDescriptor zzd;
    private static final FieldDescriptor zze;
    private static final FieldDescriptor zzf;
    private static final FieldDescriptor zzg;
    private static final FieldDescriptor zzh;
    private static final FieldDescriptor zzi;
    private static final FieldDescriptor zzj;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("name");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        zzb = builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("version");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        zzc = builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("source");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        zzd = builder3.withProperty(zzciVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("uri");
        zzci zzciVar4 = new zzci();
        zzciVar4.zza(4);
        zze = builder4.withProperty(zzciVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("hash");
        zzci zzciVar5 = new zzci();
        zzciVar5.zza(5);
        zzf = builder5.withProperty(zzciVar5.zzb()).build();
        FieldDescriptor.Builder builder6 = FieldDescriptor.builder("modelType");
        zzci zzciVar6 = new zzci();
        zzciVar6.zza(6);
        zzg = builder6.withProperty(zzciVar6.zzb()).build();
        FieldDescriptor.Builder builder7 = FieldDescriptor.builder("size");
        zzci zzciVar7 = new zzci();
        zzciVar7.zza(7);
        zzh = builder7.withProperty(zzciVar7.zzb()).build();
        FieldDescriptor.Builder builder8 = FieldDescriptor.builder("hasLabelMap");
        zzci zzciVar8 = new zzci();
        zzciVar8.zza(8);
        zzi = builder8.withProperty(zzciVar8.zzb()).build();
        FieldDescriptor.Builder builder9 = FieldDescriptor.builder("isManifestModel");
        zzci zzciVar9 = new zzci();
        zzciVar9.zza(9);
        zzj = builder9.withProperty(zzciVar9.zzb()).build();
    }

    private zzgk() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }
}
