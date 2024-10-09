package com.google.mlkit.vision.label;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.android.gms.common.api.OptionalModuleApi;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator;
import java.io.Closeable;

/* loaded from: classes.dex */
public interface ImageLabeler extends Closeable, LifecycleObserver, MultiFlavorDetectorCreator.MultiFlavorDetector, OptionalModuleApi {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void close();

    Task process(InputImage inputImage);
}
