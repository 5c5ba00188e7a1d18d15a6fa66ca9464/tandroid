package org.telegram.ui.Components;

import android.content.DialogInterface;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoPaintView$$ExternalSyntheticLambda1 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ PhotoPaintView f$0;

    public /* synthetic */ PhotoPaintView$$ExternalSyntheticLambda1(PhotoPaintView photoPaintView) {
        this.f$0 = photoPaintView;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$openStickersView$8(dialogInterface);
    }
}
