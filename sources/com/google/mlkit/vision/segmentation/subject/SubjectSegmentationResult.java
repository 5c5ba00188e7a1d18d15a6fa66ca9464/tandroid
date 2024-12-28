package com.google.mlkit.vision.segmentation.subject;

import android.graphics.Bitmap;
import java.nio.FloatBuffer;
import java.util.List;

/* loaded from: classes.dex */
public class SubjectSegmentationResult {
    private final List zza;
    private final FloatBuffer zzb;
    private final Bitmap zzc;

    public SubjectSegmentationResult(List list, FloatBuffer floatBuffer, Bitmap bitmap) {
        this.zza = list;
        this.zzb = floatBuffer;
        this.zzc = bitmap;
    }

    public List getSubjects() {
        return this.zza;
    }
}
