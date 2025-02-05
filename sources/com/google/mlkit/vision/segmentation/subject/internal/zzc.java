package com.google.mlkit.vision.segmentation.subject.internal;

import com.google.android.gms.internal.mlkit_vision_subject_segmentation.zztw;
import com.google.mlkit.common.sdkinternal.ExecutorSelector;
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions;

/* loaded from: classes.dex */
public final class zzc {
    private final zze zza;
    private final ExecutorSelector zzb;

    zzc(zze zzeVar, ExecutorSelector executorSelector) {
        this.zza = zzeVar;
        this.zzb = executorSelector;
    }

    public final zzd zza(SubjectSegmenterOptions subjectSegmenterOptions) {
        return new zzd(subjectSegmenterOptions, (zzj) this.zza.get(subjectSegmenterOptions), this.zzb.getExecutorToUse(subjectSegmenterOptions.zzb()), zztw.zzb("subject-segmentation"));
    }
}
