package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$Message;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda90 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$Message f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda90(MediaDataController mediaDataController, TLRPC$Message tLRPC$Message, String str) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$Message;
        this.f$2 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$verifyAnimatedStickerMessage$50(this.f$1, this.f$2);
    }
}
