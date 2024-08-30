package com.google.android.datatransport.runtime.synchronization;
/* loaded from: classes.dex */
public interface SynchronizationGuard {

    /* loaded from: classes.dex */
    public interface CriticalSection {
        Object execute();
    }

    Object runCriticalSection(CriticalSection criticalSection);
}
