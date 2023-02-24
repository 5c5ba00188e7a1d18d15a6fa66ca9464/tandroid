package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DrmSessionEventListener;
import com.google.android.exoplayer2.util.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultDrmSession$$ExternalSyntheticLambda2 implements Consumer {
    public static final /* synthetic */ DefaultDrmSession$$ExternalSyntheticLambda2 INSTANCE = new DefaultDrmSession$$ExternalSyntheticLambda2();

    private /* synthetic */ DefaultDrmSession$$ExternalSyntheticLambda2() {
    }

    @Override // com.google.android.exoplayer2.util.Consumer
    public final void accept(Object obj) {
        ((DrmSessionEventListener.EventDispatcher) obj).drmKeysLoaded();
    }
}
