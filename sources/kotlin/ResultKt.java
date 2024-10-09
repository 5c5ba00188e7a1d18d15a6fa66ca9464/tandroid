package kotlin;

import kotlin.Result;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes.dex */
public abstract class ResultKt {
    public static final Object createFailure(Throwable exception) {
        Intrinsics.checkNotNullParameter(exception, "exception");
        return new Result.Failure(exception);
    }
}
