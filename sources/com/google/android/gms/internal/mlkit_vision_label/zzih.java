package com.google.android.gms.internal.mlkit_vision_label;

import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;

/* loaded from: classes.dex */
final class zzih implements ObjectEncoder {
    static final zzih zza = new zzih();
    private static final FieldDescriptor zzb;
    private static final FieldDescriptor zzc;
    private static final FieldDescriptor zzd;
    private static final FieldDescriptor zze;
    private static final FieldDescriptor zzf;
    private static final FieldDescriptor zzg;
    private static final FieldDescriptor zzh;
    private static final FieldDescriptor zzi;
    private static final FieldDescriptor zzj;
    private static final FieldDescriptor zzk;
    private static final FieldDescriptor zzl;
    private static final FieldDescriptor zzm;
    private static final FieldDescriptor zzn;
    private static final FieldDescriptor zzo;

    static {
        FieldDescriptor.Builder builder = FieldDescriptor.builder("appId");
        zzci zzciVar = new zzci();
        zzciVar.zza(1);
        zzb = builder.withProperty(zzciVar.zzb()).build();
        FieldDescriptor.Builder builder2 = FieldDescriptor.builder("appVersion");
        zzci zzciVar2 = new zzci();
        zzciVar2.zza(2);
        zzc = builder2.withProperty(zzciVar2.zzb()).build();
        FieldDescriptor.Builder builder3 = FieldDescriptor.builder("firebaseProjectId");
        zzci zzciVar3 = new zzci();
        zzciVar3.zza(3);
        zzd = builder3.withProperty(zzciVar3.zzb()).build();
        FieldDescriptor.Builder builder4 = FieldDescriptor.builder("mlSdkVersion");
        zzci zzciVar4 = new zzci();
        zzciVar4.zza(4);
        zze = builder4.withProperty(zzciVar4.zzb()).build();
        FieldDescriptor.Builder builder5 = FieldDescriptor.builder("tfliteSchemaVersion");
        zzci zzciVar5 = new zzci();
        zzciVar5.zza(5);
        zzf = builder5.withProperty(zzciVar5.zzb()).build();
        FieldDescriptor.Builder builder6 = FieldDescriptor.builder("gcmSenderId");
        zzci zzciVar6 = new zzci();
        zzciVar6.zza(6);
        zzg = builder6.withProperty(zzciVar6.zzb()).build();
        FieldDescriptor.Builder builder7 = FieldDescriptor.builder("apiKey");
        zzci zzciVar7 = new zzci();
        zzciVar7.zza(7);
        zzh = builder7.withProperty(zzciVar7.zzb()).build();
        FieldDescriptor.Builder builder8 = FieldDescriptor.builder("languages");
        zzci zzciVar8 = new zzci();
        zzciVar8.zza(8);
        zzi = builder8.withProperty(zzciVar8.zzb()).build();
        FieldDescriptor.Builder builder9 = FieldDescriptor.builder("mlSdkInstanceId");
        zzci zzciVar9 = new zzci();
        zzciVar9.zza(9);
        zzj = builder9.withProperty(zzciVar9.zzb()).build();
        FieldDescriptor.Builder builder10 = FieldDescriptor.builder("isClearcutClient");
        zzci zzciVar10 = new zzci();
        zzciVar10.zza(10);
        zzk = builder10.withProperty(zzciVar10.zzb()).build();
        FieldDescriptor.Builder builder11 = FieldDescriptor.builder("isStandaloneMlkit");
        zzci zzciVar11 = new zzci();
        zzciVar11.zza(11);
        zzl = builder11.withProperty(zzciVar11.zzb()).build();
        FieldDescriptor.Builder builder12 = FieldDescriptor.builder("isJsonLogging");
        zzci zzciVar12 = new zzci();
        zzciVar12.zza(12);
        zzm = builder12.withProperty(zzciVar12.zzb()).build();
        FieldDescriptor.Builder builder13 = FieldDescriptor.builder("buildLevel");
        zzci zzciVar13 = new zzci();
        zzciVar13.zza(13);
        zzn = builder13.withProperty(zzciVar13.zzb()).build();
        FieldDescriptor.Builder builder14 = FieldDescriptor.builder("optionalModuleVersion");
        zzci zzciVar14 = new zzci();
        zzciVar14.zza(14);
        zzo = builder14.withProperty(zzciVar14.zzb()).build();
    }

    private zzih() {
    }

    @Override // com.google.firebase.encoders.ObjectEncoder
    public final /* bridge */ /* synthetic */ void encode(Object obj, Object obj2) {
        zzmj zzmjVar = (zzmj) obj;
        ObjectEncoderContext objectEncoderContext = (ObjectEncoderContext) obj2;
        objectEncoderContext.add(zzb, zzmjVar.zzg());
        objectEncoderContext.add(zzc, zzmjVar.zzh());
        objectEncoderContext.add(zzd, (Object) null);
        objectEncoderContext.add(zze, zzmjVar.zzj());
        objectEncoderContext.add(zzf, zzmjVar.zzk());
        objectEncoderContext.add(zzg, (Object) null);
        objectEncoderContext.add(zzh, (Object) null);
        objectEncoderContext.add(zzi, zzmjVar.zza());
        objectEncoderContext.add(zzj, zzmjVar.zzi());
        objectEncoderContext.add(zzk, zzmjVar.zzb());
        objectEncoderContext.add(zzl, zzmjVar.zzd());
        objectEncoderContext.add(zzm, zzmjVar.zzc());
        objectEncoderContext.add(zzn, zzmjVar.zze());
        objectEncoderContext.add(zzo, zzmjVar.zzf());
    }
}
