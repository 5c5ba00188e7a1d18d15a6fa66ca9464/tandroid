package org.telegram.ui.ActionBar;

import android.graphics.Bitmap;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$WallPaper;
/* loaded from: classes3.dex */
public final /* synthetic */ class EmojiThemes$$ExternalSyntheticLambda3 implements ResultCallback {
    public final /* synthetic */ ResultCallback f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ TLRPC$WallPaper f$2;

    public /* synthetic */ EmojiThemes$$ExternalSyntheticLambda3(ResultCallback resultCallback, long j, TLRPC$WallPaper tLRPC$WallPaper) {
        this.f$0 = resultCallback;
        this.f$1 = j;
        this.f$2 = tLRPC$WallPaper;
    }

    @Override // org.telegram.tgnet.ResultCallback
    public final void onComplete(Object obj) {
        EmojiThemes.lambda$loadWallpaper$1(this.f$0, this.f$1, this.f$2, (Bitmap) obj);
    }

    @Override // org.telegram.tgnet.ResultCallback
    public /* synthetic */ void onError(TLRPC$TL_error tLRPC$TL_error) {
        ResultCallback.CC.$default$onError(this, tLRPC$TL_error);
    }
}
