package com.google.android.datatransport.runtime.synchronization;

/* loaded from: classes.dex */
public interface SynchronizationGuard {

    public interface CriticalSection {
        Object execute();
    }

    Object runCriticalSection(CriticalSection criticalSection);
}
