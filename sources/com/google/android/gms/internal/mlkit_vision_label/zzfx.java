package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzfx implements ObjectEncoder {
    static final zzfx zza = new zzfx();

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("mode");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("landmark");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("classification");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        builder3.withProperty(zzciVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("prominentFaceOnly");
        zzci zzciVar4 = new zzci();
        zzciVar4.zza(4);
        builder4.withProperty(zzciVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("tracking");
        zzci zzciVar5 = new zzci();
        zzciVar5.zza(5);
        builder5.withProperty(zzciVar5.zzb()).build();
        FieldDescriptor.Builder builder6 = FieldDescriptor.builder("minFaceSize");
        zzci zzciVar6 = new zzci();
        zzciVar6.zza(6);
        builder6.withProperty(zzciVar6.zzb()).build();
    }

    private zzfx() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zznb zznbVar = (zznb) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        throw null;
    }
}
