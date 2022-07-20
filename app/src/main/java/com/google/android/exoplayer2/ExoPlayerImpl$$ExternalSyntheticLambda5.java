package com.google.android.exoplayer2;

import com.google.android.exoplayer2.BasePlayer;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final /* synthetic */ class ExoPlayerImpl$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ CopyOnWriteArrayList f$0;
    public final /* synthetic */ BasePlayer.ListenerInvocation f$1;

    public /* synthetic */ ExoPlayerImpl$$ExternalSyntheticLambda5(CopyOnWriteArrayList copyOnWriteArrayList, BasePlayer.ListenerInvocation listenerInvocation) {
        this.f$0 = copyOnWriteArrayList;
        this.f$1 = listenerInvocation;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ExoPlayerImpl.invokeAll(this.f$0, this.f$1);
    }
}
