package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_updateBotCommands;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda107 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$TL_updateBotCommands f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda107(MediaDataController mediaDataController, TLRPC$TL_updateBotCommands tLRPC$TL_updateBotCommands, long j) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$TL_updateBotCommands;
        this.f$2 = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$updateBotInfo$162(this.f$1, this.f$2);
    }
}
