package com.google.android.datatransport.runtime;

import com.google.android.datatransport.runtime.dagger.internal.Factory;
import com.google.android.datatransport.runtime.dagger.internal.Preconditions;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ExecutionModule_ExecutorFactory implements Factory<Executor> {
    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Executor mo70get() {
        return executor();
    }

    public static ExecutionModule_ExecutorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Executor executor() {
        return (Executor) Preconditions.checkNotNull(ExecutionModule.executor(), "Cannot return null from a non-@Nullable @Provides method");
    }

    /* loaded from: classes.dex */
    private static final class InstanceHolder {
        private static final ExecutionModule_ExecutorFactory INSTANCE = new ExecutionModule_ExecutorFactory();
    }
}
