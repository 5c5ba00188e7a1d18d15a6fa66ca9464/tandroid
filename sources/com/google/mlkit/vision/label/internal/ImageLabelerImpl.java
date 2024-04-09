package com.google.mlkit.vision.label.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.sdkinternal.MLTask;
import com.google.mlkit.common.sdkinternal.OptionalModuleUtils;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.MobileVisionBase;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import java.util.List;
import java.util.concurrent.Executor;
/* compiled from: com.google.mlkit:image-labeling-common@@18.1.0 */
/* loaded from: classes.dex */
public class ImageLabelerImpl extends MobileVisionBase<List<ImageLabel>> implements ImageLabeler {
    private final Feature zzb;

    private ImageLabelerImpl(MLTask mLTask, Executor executor, Feature feature) {
        super(mLTask, executor);
        this.zzb = feature;
    }

    @Override // com.google.android.gms.common.api.OptionalModuleApi
    public final Feature[] getOptionalFeatures() {
        Feature feature = this.zzb;
        return feature != null ? new Feature[]{feature} : OptionalModuleUtils.EMPTY_FEATURES;
    }

    public static ImageLabelerImpl newInstance(MLTask<List<ImageLabel>, InputImage> mLTask, Executor executor, Feature feature) {
        return new ImageLabelerImpl(mLTask, executor, feature);
    }

    @Override // com.google.mlkit.vision.label.ImageLabeler
    public final Task<List<ImageLabel>> process(InputImage inputImage) {
        return processBase(inputImage);
    }
}
