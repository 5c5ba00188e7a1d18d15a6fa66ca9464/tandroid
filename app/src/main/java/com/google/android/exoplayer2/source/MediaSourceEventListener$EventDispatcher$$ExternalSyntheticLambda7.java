package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.source.MediaSourceEventListener;
import java.io.IOException;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaSourceEventListener$EventDispatcher$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ MediaSourceEventListener.EventDispatcher f$0;
    public final /* synthetic */ MediaSourceEventListener f$1;
    public final /* synthetic */ MediaSourceEventListener.LoadEventInfo f$2;
    public final /* synthetic */ MediaSourceEventListener.MediaLoadData f$3;
    public final /* synthetic */ IOException f$4;
    public final /* synthetic */ boolean f$5;

    public /* synthetic */ MediaSourceEventListener$EventDispatcher$$ExternalSyntheticLambda7(MediaSourceEventListener.EventDispatcher eventDispatcher, MediaSourceEventListener mediaSourceEventListener, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData, IOException iOException, boolean z) {
        this.f$0 = eventDispatcher;
        this.f$1 = mediaSourceEventListener;
        this.f$2 = loadEventInfo;
        this.f$3 = mediaLoadData;
        this.f$4 = iOException;
        this.f$5 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$loadError$5(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
