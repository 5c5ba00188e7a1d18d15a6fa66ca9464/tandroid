package org.telegram.ui.Components.Paint.Views;

import android.content.DialogInterface;
import org.telegram.messenger.MediaController;
/* loaded from: classes4.dex */
public final /* synthetic */ class LPhotoPaintView$$ExternalSyntheticLambda7 implements DialogInterface.OnDismissListener {
    public static final /* synthetic */ LPhotoPaintView$$ExternalSyntheticLambda7 INSTANCE = new LPhotoPaintView$$ExternalSyntheticLambda7();

    private /* synthetic */ LPhotoPaintView$$ExternalSyntheticLambda7() {
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        MediaController.forceBroadcastNewPhotos = false;
    }
}
