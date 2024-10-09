package com.google.mlkit.vision.label;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.mlkit.vision.common.internal.MultiFlavorDetectorCreator;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public abstract class ImageLabelerOptionsBase implements MultiFlavorDetectorCreator.DetectorOptions {
    private final Executor zza;
    private final float zzb;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static abstract class Builder {
        private float zza = -1.0f;
        private Executor zzb;

        public Builder setConfidenceThreshold(float f) {
            boolean z = false;
            if (Float.compare(f, 0.0f) >= 0 && Float.compare(f, 1.0f) <= 0) {
                z = true;
            }
            Preconditions.checkArgument(z, "Confidence Threshold should be in range [0.0f, 1.0f].");
            this.zza = f;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ImageLabelerOptionsBase(Builder builder) {
        this.zzb = builder.zza;
        this.zza = builder.zzb;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ImageLabelerOptionsBase)) {
            return false;
        }
        ImageLabelerOptionsBase imageLabelerOptionsBase = (ImageLabelerOptionsBase) obj;
        return getClass().equals(imageLabelerOptionsBase.getClass()) && Float.compare(this.zzb, imageLabelerOptionsBase.zzb) == 0 && Objects.equal(imageLabelerOptionsBase.zza, this.zza);
    }

    public float getConfidenceThreshold() {
        return this.zzb;
    }

    public Executor getExecutor() {
        return this.zza;
    }

    public int hashCode() {
        return Objects.hashCode(getClass(), Float.valueOf(this.zzb), this.zza);
    }
}
