package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DrmSessionEventListener;
import com.google.android.exoplayer2.util.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultDrmSession$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ DefaultDrmSession$$ExternalSyntheticLambda3 INSTANCE = new DefaultDrmSession$$ExternalSyntheticLambda3();

    private /* synthetic */ DefaultDrmSession$$ExternalSyntheticLambda3() {
    }

    @Override // com.google.android.exoplayer2.util.Consumer
    public final void accept(Object obj) {
        ((DrmSessionEventListener.EventDispatcher) obj).drmKeysRemoved();
    }
}
