package org.telegram.ui.Components;

import java.util.ArrayList;
import org.telegram.ui.Components.AudioPlayerAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ AudioPlayerAlert.ListAdapter f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ArrayList f$2;

    public /* synthetic */ AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda2(AudioPlayerAlert.ListAdapter listAdapter, String str, ArrayList arrayList) {
        this.f$0 = listAdapter;
        this.f$1 = str;
        this.f$2 = arrayList;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$processSearch$1(this.f$1, this.f$2);
    }
}
