package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
public abstract class PendingResultUtil {
    private static final zas zaa = new zao();

    public interface ResultConverter {
        Object convert(Result result);
    }

    public static Task toResponseTask(PendingResult pendingResult, Response response) {
        return toTask(pendingResult, new zaq(response));
    }

    public static Task toTask(PendingResult pendingResult, ResultConverter resultConverter) {
        zas zasVar = zaa;
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        pendingResult.addStatusListener(new zap(pendingResult, taskCompletionSource, resultConverter, zasVar));
        return taskCompletionSource.getTask();
    }

    public static Task toVoidTask(PendingResult pendingResult) {
        return toTask(pendingResult, new zar());
    }
}
