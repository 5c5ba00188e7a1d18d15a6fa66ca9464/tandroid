package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
/* loaded from: classes.dex */
public final /* synthetic */ class Uploader$$ExternalSyntheticLambda2 implements SynchronizationGuard.CriticalSection {
    public final /* synthetic */ Uploader f$0;
    public final /* synthetic */ TransportContext f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ Uploader$$ExternalSyntheticLambda2(Uploader uploader, TransportContext transportContext, int i) {
        this.f$0 = uploader;
        this.f$1 = transportContext;
        this.f$2 = i;
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
    public final Object execute() {
        Object lambda$upload$0;
        lambda$upload$0 = this.f$0.lambda$upload$0(this.f$1, this.f$2);
        return lambda$upload$0;
    }
}
