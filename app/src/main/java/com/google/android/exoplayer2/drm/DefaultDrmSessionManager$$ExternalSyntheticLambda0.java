package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSession;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultDrmSessionManager$$ExternalSyntheticLambda0 implements DefaultDrmSession.ReleaseCallback {
    public final /* synthetic */ DefaultDrmSessionManager f$0;

    public /* synthetic */ DefaultDrmSessionManager$$ExternalSyntheticLambda0(DefaultDrmSessionManager defaultDrmSessionManager) {
        this.f$0 = defaultDrmSessionManager;
    }

    @Override // com.google.android.exoplayer2.drm.DefaultDrmSession.ReleaseCallback
    public final void onSessionReleased(DefaultDrmSession defaultDrmSession) {
        this.f$0.onSessionReleased(defaultDrmSession);
    }
}
