package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$StickerSet;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda92 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$StickerSet f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda92(MediaDataController mediaDataController, TLRPC$StickerSet tLRPC$StickerSet, int i) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$StickerSet;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$toggleStickerSetInternal$80(this.f$1, this.f$2);
    }
}
