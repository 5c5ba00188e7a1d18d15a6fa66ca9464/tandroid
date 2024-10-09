package com.google.android.datatransport.runtime;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
abstract class ExecutionModule {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Executor executor() {
        return new SafeLoggingExecutor(Executors.newSingleThreadExecutor());
    }
}
