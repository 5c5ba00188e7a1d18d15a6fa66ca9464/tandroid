package org.telegram.ui;

import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public final /* synthetic */ class ChannelCreateActivity$$ExternalSyntheticLambda17 implements Runnable {
    public final /* synthetic */ ChannelCreateActivity f$0;
    public final /* synthetic */ TLObject f$1;

    public /* synthetic */ ChannelCreateActivity$$ExternalSyntheticLambda17(ChannelCreateActivity channelCreateActivity, TLObject tLObject) {
        this.f$0 = channelCreateActivity;
        this.f$1 = tLObject;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadAdminedChannels$20(this.f$1);
    }
}
