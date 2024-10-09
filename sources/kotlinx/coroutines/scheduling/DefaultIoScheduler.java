package kotlinx.coroutines.scheduling;

import java.util.concurrent.Executor;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import kotlinx.coroutines.internal.SystemPropsKt;
import kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt;

/* loaded from: classes.dex */
public final class DefaultIoScheduler extends ExecutorCoroutineDispatcher implements Executor {
    public static final DefaultIoScheduler INSTANCE = new DefaultIoScheduler();
    private static final CoroutineDispatcher default;

    static {
        int coerceAtLeast;
        int systemProp$default;
        UnlimitedIoScheduler unlimitedIoScheduler = UnlimitedIoScheduler.INSTANCE;
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(64, SystemPropsKt.getAVAILABLE_PROCESSORS());
        systemProp$default = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.io.parallelism", coerceAtLeast, 0, 0, 12, (Object) null);
        default = unlimitedIoScheduler.limitedParallelism(systemProp$default);
    }

    private DefaultIoScheduler() {
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new IllegalStateException("Cannot be invoked on Dispatchers.IO".toString());
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        default.dispatch(coroutineContext, runnable);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        dispatch(EmptyCoroutineContext.INSTANCE, runnable);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public String toString() {
        return "Dispatchers.IO";
    }
}
