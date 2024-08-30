package com.google.mlkit.vision.label;

import com.google.android.gms.common.internal.Preconditions;
import com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator;
/* loaded from: classes.dex */
public abstract class ImageLabeling {
    public static ImageLabeler getClient(ImageLabelerOptionsBase imageLabelerOptionsBase) {
        Preconditions.checkNotNull(imageLabelerOptionsBase, "options cannot be null");
        return (ImageLabeler) MultiFlavorDetectorCreator.getInstance().create(imageLabelerOptionsBase);
    }
}
