package org.telegram.messenger;

import java.io.File;
import org.telegram.tgnet.ResultCallback;
/* loaded from: classes.dex */
public final /* synthetic */ class ChatThemeController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ File f$0;
    public final /* synthetic */ ResultCallback f$1;

    public /* synthetic */ ChatThemeController$$ExternalSyntheticLambda1(File file, ResultCallback resultCallback) {
        this.f$0 = file;
        this.f$1 = resultCallback;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ChatThemeController.lambda$getWallpaperBitmap$6(this.f$0, this.f$1);
    }
}
