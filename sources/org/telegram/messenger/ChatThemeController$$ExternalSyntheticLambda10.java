package org.telegram.messenger;

import android.util.Pair;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatThemeController$$ExternalSyntheticLambda10 implements ResultCallback {
    public static final /* synthetic */ ChatThemeController$$ExternalSyntheticLambda10 INSTANCE = new ChatThemeController$$ExternalSyntheticLambda10();

    private /* synthetic */ ChatThemeController$$ExternalSyntheticLambda10() {
    }

    @Override // org.telegram.tgnet.ResultCallback
    public final void onComplete(Object obj) {
        ChatThemeController.lambda$preloadAllWallpaperThumbs$4((Pair) obj);
    }

    @Override // org.telegram.tgnet.ResultCallback
    public /* synthetic */ void onError(TLRPC$TL_error tLRPC$TL_error) {
        ResultCallback.-CC.$default$onError(this, tLRPC$TL_error);
    }
}
