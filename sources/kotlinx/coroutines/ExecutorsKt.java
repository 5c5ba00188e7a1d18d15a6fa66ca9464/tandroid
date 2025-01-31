package kotlinx.coroutines;

import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public abstract class ExecutorsKt {
    public static final ExecutorCoroutineDispatcher from(ExecutorService executorService) {
        return new ExecutorCoroutineDispatcherImpl(executorService);
    }
}
