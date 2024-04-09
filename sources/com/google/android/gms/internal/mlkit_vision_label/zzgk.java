package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzgk implements ObjectEncoder {
    static final zzgk zza = new zzgk();

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("name");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("version");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("source");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        builder3.withProperty(zzciVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("uri");
        zzci zzciVar4 = new zzci();
        zzciVar4.zza(4);
        builder4.withProperty(zzciVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("hash");
        zzci zzciVar5 = new zzci();
        zzciVar5.zza(5);
        builder5.withProperty(zzciVar5.zzb()).build();
        FieldDescriptor.Builder builder6 = FieldDescriptor.builder("modelType");
        zzci zzciVar6 = new zzci();
        zzciVar6.zza(6);
        builder6.withProperty(zzciVar6.zzb()).build();
        FieldDescriptor.Builder builder7 = FieldDescriptor.builder("size");
        zzci zzciVar7 = new zzci();
        zzciVar7.zza(7);
        builder7.withProperty(zzciVar7.zzb()).build();
        FieldDescriptor.Builder builder8 = FieldDescriptor.builder("hasLabelMap");
        zzci zzciVar8 = new zzci();
        zzciVar8.zza(8);
        builder8.withProperty(zzciVar8.zzb()).build();
        FieldDescriptor.Builder builder9 = FieldDescriptor.builder("isManifestModel");
        zzci zzciVar9 = new zzci();
        zzciVar9.zza(9);
        builder9.withProperty(zzciVar9.zzb()).build();
    }

    private zzgk() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zzkk zzkkVar = (zzkk) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        throw null;
    }
}
