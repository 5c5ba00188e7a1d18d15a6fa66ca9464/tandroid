package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzej implements ObjectEncoder {
    static final zzej zza = new zzej();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;
    private static final FieldDescriptor zzd;
    private static final FieldDescriptor zze;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("errorCode");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        zzb = builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("isColdCall");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        zzc = builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("imageInfo");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        zzd = builder3.withProperty(zzciVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("detectorOptions");
        zzci zzciVar4 = new zzci();
        zzciVar4.zza(4);
        zze = builder4.withProperty(zzciVar4.zzb()).build();
    }

    private zzej() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zzdh zzdhVar = (zzdh) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        objectEncoderContext.add(zzb, zzdhVar.zza());
        objectEncoderContext.add(zzc, zzdhVar.zzc());
        objectEncoderContext.add(zzd, (Object) null);
        objectEncoderContext.add(zze, zzdhVar.zzb());
    }
}
