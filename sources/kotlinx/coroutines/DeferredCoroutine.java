package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;

/* loaded from: classes.dex */
class DeferredCoroutine extends AbstractCoroutine implements Deferred {
    public DeferredCoroutine(CoroutineContext coroutineContext, boolean z) {
        super(coroutineContext, true, z);
    }

    @Override // kotlinx.coroutines.Deferred
    public Object getCompleted() {
        return getCompletedInternal$kotlinx_coroutines_core();
    }
}
