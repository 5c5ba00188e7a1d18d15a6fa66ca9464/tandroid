package org.telegram.ui.Components;
/* loaded from: classes3.dex */
public final /* synthetic */ class PlayingGameDrawable$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PlayingGameDrawable f$0;

    public /* synthetic */ PlayingGameDrawable$$ExternalSyntheticLambda0(PlayingGameDrawable playingGameDrawable) {
        this.f$0 = playingGameDrawable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.checkUpdate();
    }
}
