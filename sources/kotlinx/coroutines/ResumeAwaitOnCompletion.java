package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;

/* loaded from: classes.dex */
final class ResumeAwaitOnCompletion extends JobNode {
    private final CancellableContinuationImpl continuation;

    public ResumeAwaitOnCompletion(CancellableContinuationImpl cancellableContinuationImpl) {
        this.continuation = cancellableContinuationImpl;
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    @Override // kotlinx.coroutines.CompletionHandlerBase
    public void invoke(Throwable th) {
        CancellableContinuationImpl cancellableContinuationImpl;
        Object unboxState;
        Object state$kotlinx_coroutines_core = getJob().getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            cancellableContinuationImpl = this.continuation;
            Result.Companion companion = Result.Companion;
            unboxState = ResultKt.createFailure(((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
        } else {
            cancellableContinuationImpl = this.continuation;
            Result.Companion companion2 = Result.Companion;
            unboxState = JobSupportKt.unboxState(state$kotlinx_coroutines_core);
        }
        cancellableContinuationImpl.resumeWith(Result.constructor-impl(unboxState));
    }
}
