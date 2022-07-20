package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import com.google.android.datatransport.runtime.synchronization.SynchronizationGuard;
import java.util.Map;
/* loaded from: classes.dex */
public final /* synthetic */ class Uploader$$ExternalSyntheticLambda6 implements SynchronizationGuard.CriticalSection {
    public final /* synthetic */ Uploader f$0;
    public final /* synthetic */ Map f$1;

    public /* synthetic */ Uploader$$ExternalSyntheticLambda6(Uploader uploader, Map map) {
        this.f$0 = uploader;
        this.f$1 = map;
    }

    @Override // com.google.android.datatransport.runtime.synchronization.SynchronizationGuard.CriticalSection
    public final Object execute() {
        Object lambda$logAndUpdateState$6;
        lambda$logAndUpdateState$6 = this.f$0.lambda$logAndUpdateState$6(this.f$1);
        return lambda$logAndUpdateState$6;
    }
}
