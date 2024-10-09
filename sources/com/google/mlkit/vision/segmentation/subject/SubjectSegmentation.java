package com.google.mlkit.vision.segmentation.subject;

import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.vision.segmentation.subject.internal.zzc;

/* loaded from: classes.dex */
public abstract class SubjectSegmentation {
    public static SubjectSegmenter getClient(SubjectSegmenterOptions subjectSegmenterOptions) {
        return ((zzc) MlKitContext.getInstance().get(zzc.class)).zza(subjectSegmenterOptions);
    }
}
