package org.telegram.messenger;

import org.telegram.ui.Components.Bulletin;

/* loaded from: classes3.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda16 implements Runnable {
    public final /* synthetic */ Bulletin.UndoButton f$0;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda16(Bulletin.UndoButton undoButton) {
        this.f$0 = undoButton;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.undo();
    }
}
