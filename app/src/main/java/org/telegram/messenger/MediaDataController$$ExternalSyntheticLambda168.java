package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_getFeaturedStickers;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda168 implements RequestDelegate {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ TLRPC$TL_messages_getFeaturedStickers f$1;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda168(MediaDataController mediaDataController, TLRPC$TL_messages_getFeaturedStickers tLRPC$TL_messages_getFeaturedStickers) {
        this.f$0 = mediaDataController;
        this.f$1 = tLRPC$TL_messages_getFeaturedStickers;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadFeaturedStickers$40(this.f$1, tLObject, tLRPC$TL_error);
    }
}
