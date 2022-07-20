package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.util.EventDispatcher;
/* loaded from: classes.dex */
public final /* synthetic */ class EventDispatcher$HandlerAndListener$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ EventDispatcher.HandlerAndListener f$0;
    public final /* synthetic */ EventDispatcher.Event f$1;

    public /* synthetic */ EventDispatcher$HandlerAndListener$$ExternalSyntheticLambda0(EventDispatcher.HandlerAndListener handlerAndListener, EventDispatcher.Event event) {
        this.f$0 = handlerAndListener;
        this.f$1 = event;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$dispatch$0(this.f$1);
    }
}
