package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$BotInfo;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda86 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$BotInfo f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda86(MediaDataController mediaDataController, TLRPC$BotInfo tLRPC$BotInfo, long j) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$BotInfo;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$putBotInfo$157(this.f$1, this.f$2);
    }
}
