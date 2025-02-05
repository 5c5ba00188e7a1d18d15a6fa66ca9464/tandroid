package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;

/* loaded from: classes.dex */
public abstract class CompletionStateKt {
    public static final Object recoverResult(Object obj, Continuation continuation) {
        if (obj instanceof CompletedExceptionally) {
            Result.Companion companion = Result.Companion;
            obj = ResultKt.createFailure(((CompletedExceptionally) obj).cause);
        }
        return Result.constructor-impl(obj);
    }

    public static final Object toState(Object obj, Function1 function1) {
        Throwable th = Result.exceptionOrNull-impl(obj);
        return th == null ? function1 != null ? new CompletedWithCancellation(obj, function1) : obj : new CompletedExceptionally(th, false, 2, null);
    }

    public static final Object toState(Object obj, CancellableContinuation cancellableContinuation) {
        Throwable th = Result.exceptionOrNull-impl(obj);
        return th == null ? obj : new CompletedExceptionally(th, false, 2, null);
    }

    public static /* synthetic */ Object toState$default(Object obj, Function1 function1, int i, Object obj2) {
        if ((i & 1) != 0) {
            function1 = null;
        }
        return toState(obj, function1);
    }
}
