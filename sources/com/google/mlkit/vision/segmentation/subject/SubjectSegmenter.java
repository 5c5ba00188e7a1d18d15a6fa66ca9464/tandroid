package com.google.mlkit.vision.segmentation.subject;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.android.gms.common.api.OptionalModuleApi;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
/* compiled from: com.google.android.gms:play-services-mlkit-subject-segmentation@@16.0.0-beta1 */
/* loaded from: classes.dex */
public interface SubjectSegmenter extends OptionalModuleApi, LifecycleObserver, OptionalModuleApi {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void close();

    Task<SubjectSegmentationResult> process(InputImage inputImage);
}
