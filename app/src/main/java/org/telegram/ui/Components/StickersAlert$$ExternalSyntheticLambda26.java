package org.telegram.ui.Components;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickersAlert$$ExternalSyntheticLambda26 implements MessagesStorage.StringCallback {
    public final /* synthetic */ StickersAlert f$0;

    public /* synthetic */ StickersAlert$$ExternalSyntheticLambda26(StickersAlert stickersAlert) {
        this.f$0 = stickersAlert;
    }

    @Override // org.telegram.messenger.MessagesStorage.StringCallback
    public final void run(String str) {
        this.f$0.lambda$showNameEnterAlert$28(str);
    }
}
