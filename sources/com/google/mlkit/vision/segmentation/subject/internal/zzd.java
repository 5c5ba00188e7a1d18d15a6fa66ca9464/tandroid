package com.google.mlkit.vision.segmentation.subject.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zznz;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzoa;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzob;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzoc;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzqx;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztl;
import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzto;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.sdkinternal.OptionalModuleUtils;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.MobileVisionBase;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenter;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class zzd extends MobileVisionBase implements SubjectSegmenter {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzd(SubjectSegmenterOptions subjectSegmenterOptions, zzj zzjVar, Executor executor, zztl zztlVar) {
        super(zzjVar, executor);
        zzoc zzocVar = new zzoc();
        zzocVar.zze(zznz.zzb);
        zzqx zzqxVar = new zzqx();
        zzqxVar.zzc(subjectSegmenterOptions.zza());
        zzqxVar.zzb(zzoa.zza);
        zzocVar.zzg(zzqxVar.zzd());
        zztlVar.zzd(zzto.zzg(zzocVar, 1), zzob.zzew);
    }

    @Override // com.google.android.gms.common.api.OptionalModuleApi
    public final Feature[] getOptionalFeatures() {
        return new Feature[]{OptionalModuleUtils.FEATURE_SUBJECT_SEGMENTATION};
    }

    @Override // com.google.mlkit.vision.segmentation.subject.SubjectSegmenter
    public final Task process(InputImage inputImage) {
        return super.processBase(inputImage);
    }
}
