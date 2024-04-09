package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import java.io.IOException;
/* compiled from: com.google.android.gms:play-services-mlkit-image-labeling@@16.0.8 */
/* loaded from: classes.dex */
final class zzht implements ObjectEncoder {
    static final zzht zza = new zzht();

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("inferenceCommonLogEvent");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("smartReplies");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("resultStatus");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        builder3.withProperty(zzciVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("suggestionsCount");
        zzci zzciVar4 = new zzci();
        zzciVar4.zza(4);
        builder4.withProperty(zzciVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("blacklistErrorCode");
        zzci zzciVar5 = new zzci();
        zzciVar5.zza(5);
        builder5.withProperty(zzciVar5.zzb()).build();
    }

    private zzht() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) throws IOException {
        zzly zzlyVar = (zzly) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        throw null;
    }
}
