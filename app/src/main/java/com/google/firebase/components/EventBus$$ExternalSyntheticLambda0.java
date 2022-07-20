package com.google.firebase.components;

import com.google.firebase.events.Event;
import java.util.Map;
/* loaded from: classes.dex */
public final /* synthetic */ class EventBus$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Map.Entry f$0;
    public final /* synthetic */ Event f$1;

    public /* synthetic */ EventBus$$ExternalSyntheticLambda0(Map.Entry entry, Event event) {
        this.f$0 = entry;
        this.f$1 = event;
    }

    @Override // java.lang.Runnable
    public final void run() {
        EventBus.lambda$publish$0(this.f$0, this.f$1);
    }
}
