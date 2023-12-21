package com.google.mlkit.vision.segmentation.subject;

import com.google.mlkit.common.sdkinternal.MlKitContext;
import com.google.mlkit.vision.segmentation.subject.internal.zzc;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public class SubjectSegmentation {
    public static SubjectSegmenter getClient(SubjectSegmenterOptions subjectSegmenterOptions) {
        return ((zzc) MlKitContext.getInstance().get(zzc.class)).zza(subjectSegmenterOptions);
    }
}
