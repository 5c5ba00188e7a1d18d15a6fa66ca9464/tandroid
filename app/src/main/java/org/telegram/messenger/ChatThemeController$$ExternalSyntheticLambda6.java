package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatThemeController$$ExternalSyntheticLambda6 implements RequestDelegate {
    public final /* synthetic */ ResultCallback f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ ChatThemeController$$ExternalSyntheticLambda6(ResultCallback resultCallback, boolean z) {
        this.f$0 = resultCallback;
        this.f$1 = z;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatThemeController.lambda$requestAllChatThemes$3(this.f$0, this.f$1, tLObject, tLRPC$TL_error);
    }
}
