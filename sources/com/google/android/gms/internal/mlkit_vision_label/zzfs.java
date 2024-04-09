package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzfs implements ObjectEncoder {
    static final zzfs zza = new zzfs();

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("modelType");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("isSuccessful");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("modelName");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        builder3.withProperty(zzciVar3.zzb()).build();
    }

    private zzfs() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zzjk zzjkVar = (zzjk) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        throw null;
    }
}
