package org.telegram.ui.Components;

import org.telegram.ui.Components.Paint.UndoStore;
/* loaded from: classes3.dex */
public final /* synthetic */ class PhotoPaintView$$ExternalSyntheticLambda20 implements UndoStore.UndoStoreDelegate {
    public final /* synthetic */ PhotoPaintView f$0;

    public /* synthetic */ PhotoPaintView$$ExternalSyntheticLambda20(PhotoPaintView photoPaintView) {
        this.f$0 = photoPaintView;
    }

    @Override // org.telegram.ui.Components.Paint.UndoStore.UndoStoreDelegate
    public final void historyChanged() {
        this.f$0.lambda$new$0();
    }
}
