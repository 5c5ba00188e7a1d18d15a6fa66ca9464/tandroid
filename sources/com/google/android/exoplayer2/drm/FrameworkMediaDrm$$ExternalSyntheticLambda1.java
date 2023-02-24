package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.ExoMediaDrm;
import java.util.UUID;
/* loaded from: classes.dex */
public final /* synthetic */ class FrameworkMediaDrm$$ExternalSyntheticLambda1 implements ExoMediaDrm.Provider {
    public static final /* synthetic */ FrameworkMediaDrm$$ExternalSyntheticLambda1 INSTANCE = new FrameworkMediaDrm$$ExternalSyntheticLambda1();

    private /* synthetic */ FrameworkMediaDrm$$ExternalSyntheticLambda1() {
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaDrm.Provider
    public final ExoMediaDrm acquireExoMediaDrm(UUID uuid) {
        ExoMediaDrm lambda$static$0;
        lambda$static$0 = FrameworkMediaDrm.lambda$static$0(uuid);
        return lambda$static$0;
    }
}
