package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.util.EventDispatcher;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultDrmSession$$ExternalSyntheticLambda0 implements EventDispatcher.Event {
    public final /* synthetic */ Exception f$0;

    public /* synthetic */ DefaultDrmSession$$ExternalSyntheticLambda0(Exception exc) {
        this.f$0 = exc;
    }

    @Override // com.google.android.exoplayer2.util.EventDispatcher.Event
    public final void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmSessionManagerError(this.f$0);
    }
}
