package org.telegram.ui.Components;

import org.telegram.ui.Components.AudioPlayerAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AudioPlayerAlert.ListAdapter f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda1(AudioPlayerAlert.ListAdapter listAdapter, String str) {
        this.f$0 = listAdapter;
        this.f$1 = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$search$0(this.f$1);
    }
}
