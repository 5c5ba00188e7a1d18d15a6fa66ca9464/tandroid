package kotlinx.coroutines;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: CompletionState.kt */
/* loaded from: classes.dex */
public class CompletedExceptionally {
    private static final /* synthetic */ AtomicIntegerFieldUpdater _handled$FU = AtomicIntegerFieldUpdater.newUpdater(CompletedExceptionally.class, "_handled");
    private volatile /* synthetic */ int _handled;
    public final Throwable cause;

    public CompletedExceptionally(Throwable th, boolean z) {
        this.cause = th;
        this._handled = z ? 1 : 0;
    }

    public /* synthetic */ CompletedExceptionally(Throwable th, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(th, (i & 2) != 0 ? false : z);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [boolean, int] */
    public final boolean getHandled() {
        return this._handled;
    }

    public final boolean makeHandled() {
        return _handled$FU.compareAndSet(this, 0, 1);
    }

    public String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '[' + this.cause + ']';
    }
}
