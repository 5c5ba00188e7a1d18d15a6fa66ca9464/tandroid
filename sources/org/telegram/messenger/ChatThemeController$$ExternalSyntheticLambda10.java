package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatThemeController$$ExternalSyntheticLambda10 implements RequestDelegate {
    public static final /* synthetic */ ChatThemeController$$ExternalSyntheticLambda10 INSTANCE = new ChatThemeController$$ExternalSyntheticLambda10();

    private /* synthetic */ ChatThemeController$$ExternalSyntheticLambda10() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatThemeController.lambda$clearWallpaper$9(tLObject, tLRPC$TL_error);
    }
}
