package org.telegram.messenger;

import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes.dex */
public final /* synthetic */ class FileRefController$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ TLRPC$TL_theme f$0;

    public /* synthetic */ FileRefController$$ExternalSyntheticLambda9(TLRPC$TL_theme tLRPC$TL_theme) {
        this.f$0 = tLRPC$TL_theme;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Theme.setThemeFileReference(this.f$0);
    }
}
