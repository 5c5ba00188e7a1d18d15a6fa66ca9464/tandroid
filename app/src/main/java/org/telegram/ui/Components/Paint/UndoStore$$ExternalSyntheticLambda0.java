package org.telegram.ui.Components.Paint;
/* loaded from: classes3.dex */
public final /* synthetic */ class UndoStore$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ UndoStore f$0;

    public /* synthetic */ UndoStore$$ExternalSyntheticLambda0(UndoStore undoStore) {
        this.f$0 = undoStore;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$notifyOfHistoryChanges$0();
    }
}
