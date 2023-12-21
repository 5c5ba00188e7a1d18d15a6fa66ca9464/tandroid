package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatThemeController$$ExternalSyntheticLambda11 implements RequestDelegate {
    public static final /* synthetic */ ChatThemeController$$ExternalSyntheticLambda11 INSTANCE = new ChatThemeController$$ExternalSyntheticLambda11();

    private /* synthetic */ ChatThemeController$$ExternalSyntheticLambda11() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ChatThemeController.lambda$clearWallpaper$10(tLObject, tLRPC$TL_error);
    }
}
