package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.util.EventDispatcher;
/* loaded from: classes.dex */
public final /* synthetic */ class DefaultDrmSessionManager$$ExternalSyntheticLambda1 implements EventDispatcher.Event {
    public final /* synthetic */ DefaultDrmSessionManager.MissingSchemeDataException f$0;

    public /* synthetic */ DefaultDrmSessionManager$$ExternalSyntheticLambda1(DefaultDrmSessionManager.MissingSchemeDataException missingSchemeDataException) {
        this.f$0 = missingSchemeDataException;
    }

    @Override // com.google.android.exoplayer2.util.EventDispatcher.Event
    public final void sendTo(Object obj) {
        ((DefaultDrmSessionEventListener) obj).onDrmSessionManagerError(this.f$0);
    }
}
