package org.telegram.messenger;

import org.telegram.messenger.Utilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda174 implements RequestDelegate {
    public final /* synthetic */ MediaDataController f$0;
    public final /* synthetic */ Utilities.Callback f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda174(MediaDataController mediaDataController, Utilities.Callback callback, int i, long j) {
        this.f$0 = mediaDataController;
        this.f$1 = callback;
        this.f$2 = i;
        this.f$3 = j;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$loadStickers$73(this.f$1, this.f$2, this.f$3, tLObject, tLRPC$TL_error);
    }
}
