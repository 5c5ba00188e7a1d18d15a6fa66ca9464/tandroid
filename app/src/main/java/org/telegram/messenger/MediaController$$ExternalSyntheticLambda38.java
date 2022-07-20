package org.telegram.messenger;

import org.telegram.ui.ActionBar.AlertDialog;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaController$$ExternalSyntheticLambda38 implements Runnable {
    public final /* synthetic */ AlertDialog f$0;
    public final /* synthetic */ boolean[] f$1;

    public /* synthetic */ MediaController$$ExternalSyntheticLambda38(AlertDialog alertDialog, boolean[] zArr) {
        this.f$0 = alertDialog;
        this.f$1 = zArr;
    }

    @Override // java.lang.Runnable
    public final void run() {
        MediaController.lambda$saveFile$37(this.f$0, this.f$1);
    }
}
