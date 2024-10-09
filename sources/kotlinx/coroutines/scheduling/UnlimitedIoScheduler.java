package kotlinx.coroutines.scheduling;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineDispatcher;

/* loaded from: classes.dex */
final class UnlimitedIoScheduler extends CoroutineDispatcher {
    public static final UnlimitedIoScheduler INSTANCE = new UnlimitedIoScheduler();

    private UnlimitedIoScheduler() {
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        DefaultScheduler.INSTANCE.dispatchWithContext$kotlinx_coroutines_core(runnable, TasksKt.BlockingContext, false);
    }
}
