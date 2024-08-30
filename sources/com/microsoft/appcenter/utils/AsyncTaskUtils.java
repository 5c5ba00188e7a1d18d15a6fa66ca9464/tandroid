package com.microsoft.appcenter.utils;

import android.os.AsyncTask;
import java.util.concurrent.RejectedExecutionException;
/* loaded from: classes.dex */
public abstract class AsyncTaskUtils {
    public static AsyncTask execute(String str, AsyncTask asyncTask, Object... objArr) {
        try {
            return asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objArr);
        } catch (RejectedExecutionException e) {
            AppCenterLog.warn(str, "THREAD_POOL_EXECUTOR saturated, fall back on SERIAL_EXECUTOR which has an unbounded queue", e);
            return asyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, objArr);
        }
    }
}
