package com.google.mlkit.vision.label;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.android.gms.common.api.OptionalModuleApi;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator;
import java.util.List;
/* compiled from: com.google.mlkit:image-labeling-common@@18.1.0 */
/* loaded from: classes.dex */
public interface ImageLabeler extends MultiFlavorDetectorCreator.MultiFlavorDetector, OptionalModuleApi, MultiFlavorDetectorCreator.MultiFlavorDetector, OptionalModuleApi {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void close();

    Task<List<ImageLabel>> process(InputImage inputImage);
}
