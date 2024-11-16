package com.google.mlkit.vision.label.defaults;

import com.google.mlkit.vision.label.ImageLabelerOptionsBase;

/* loaded from: classes.dex */
public class ImageLabelerOptions extends ImageLabelerOptionsBase {
    public static final ImageLabelerOptions DEFAULT_OPTIONS = new Builder().build();

    public static class Builder extends ImageLabelerOptionsBase.Builder {
        public Builder() {
            setConfidenceThreshold(0.5f);
        }

        public ImageLabelerOptions build() {
            return new ImageLabelerOptions(this, null);
        }

        @Override // com.google.mlkit.vision.label.ImageLabelerOptionsBase.Builder
        public Builder setConfidenceThreshold(float f) {
            return (Builder) super.setConfidenceThreshold(f);
        }
    }

    /* synthetic */ ImageLabelerOptions(Builder builder, zza zzaVar) {
        super(builder);
    }
}
