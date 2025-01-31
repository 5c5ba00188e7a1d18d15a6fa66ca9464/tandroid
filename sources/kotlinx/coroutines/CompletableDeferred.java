package kotlinx.coroutines;

/* loaded from: classes.dex */
public interface CompletableDeferred extends Deferred {
    boolean complete(Object obj);

    boolean completeExceptionally(Throwable th);
}
