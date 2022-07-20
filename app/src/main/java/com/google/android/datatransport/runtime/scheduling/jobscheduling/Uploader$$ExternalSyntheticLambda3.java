package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.TransportContext;
import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
/* loaded from: classes.dex */
public final /* synthetic */ class Uploader$$ExternalSyntheticLambda3 implements SynchronizationGuard.CriticalSection {
    public final /* synthetic */ Uploader f$0;
    public final /* synthetic */ TransportContext f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ Uploader$$ExternalSyntheticLambda3(Uploader uploader, TransportContext transportContext, long j) {
        this.f$0 = uploader;
        this.f$1 = transportContext;
        this.f$2 = j;
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
    public final Object execute() {
        Object lambda$logAndUpdateState$7;
        lambda$logAndUpdateState$7 = this.f$0.lambda$logAndUpdateState$7(this.f$1, this.f$2);
        return lambda$logAndUpdateState$7;
    }
}
