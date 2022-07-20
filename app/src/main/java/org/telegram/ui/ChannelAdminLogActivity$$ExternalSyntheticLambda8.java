package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChannelAdminLogActivity$$ExternalSyntheticLambda8 implements MessagesStorage.IntCallback {
    public final /* synthetic */ ChannelAdminLogActivity f$0;

    public /* synthetic */ ChannelAdminLogActivity$$ExternalSyntheticLambda8(ChannelAdminLogActivity channelAdminLogActivity) {
        this.f$0 = channelAdminLogActivity;
    }

    @Override // org.telegram.messenger.MessagesStorage.IntCallback
    public final void run(int i) {
        this.f$0.lambda$createView$7(i);
    }
}
