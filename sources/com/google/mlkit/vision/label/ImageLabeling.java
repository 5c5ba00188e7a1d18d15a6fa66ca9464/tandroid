package com.google.mlkit.vision.label;

import com.google.android.gms.common.internal.Preconditions;
import com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator;
/* compiled from: com.google.mlkit:image-labeling-common@@18.1.0 */
/* loaded from: classes.dex */
public class ImageLabeling {
    public static ImageLabeler getClient(ImageLabelerOptionsBase imageLabelerOptionsBase) {
        Preconditions.checkNotNull(imageLabelerOptionsBase, "options cannot be null");
        return (ImageLabeler) MultiFlavorDetectorCreator.getInstance().create(imageLabelerOptionsBase);
    }
}
