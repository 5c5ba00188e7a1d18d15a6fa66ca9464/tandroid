package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_messages_getFeaturedStickers;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda84 implements Runnable {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLObject f$1;
    public final /* synthetic */ TLRPC$TL_messages_getFeaturedStickers f$2;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda84(MediaDataController mediaDataController, TLObject tLObject, TLRPC$TL_messages_getFeaturedStickers tLRPC$TL_messages_getFeaturedStickers) {
        this.f$0 = mediaDataController;
        this.f$1 = tLObject;
        this.f$2 = tLRPC$TL_messages_getFeaturedStickers;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadFeaturedStickers$39(this.f$1, this.f$2);
    }
}
