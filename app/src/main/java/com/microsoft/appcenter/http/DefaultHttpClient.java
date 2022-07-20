package com.microsoft.appcenter.http;

import android.os.AsyncTask;
import com.microsoft.appcenter.http.DefaultHttpClientCallTask;
import com.microsoft.appcenter.http.HttpClient;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.HandlerUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
/* loaded from: classes.dex */
public class DefaultHttpClient implements HttpClient, DefaultHttpClientCallTask.Tracker {
    private final boolean mCompressionEnabled;
    private final Set<DefaultHttpClientCallTask> mTasks = new HashSet();

    @Override // com.microsoft.appcenter.http.HttpClient
    public void reopen() {
    }

    public DefaultHttpClient(boolean z) {
        this.mCompressionEnabled = z;
    }

    @Override // com.microsoft.appcenter.http.HttpClient
    public ServiceCall callAsync(String str, String str2, Map<String, String> map, HttpClient.CallTemplate callTemplate, ServiceCallback serviceCallback) {
        DefaultHttpClientCallTask defaultHttpClientCallTask = new DefaultHttpClientCallTask(str, str2, map, callTemplate, serviceCallback, this, this.mCompressionEnabled);
        try {
            defaultHttpClientCallTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } catch (RejectedExecutionException e) {
            HandlerUtils.runOnUiThread(new AnonymousClass1(this, serviceCallback, e));
        }
        return new AnonymousClass2(this, defaultHttpClientCallTask);
    }

    /* renamed from: com.microsoft.appcenter.http.DefaultHttpClient$1 */
    /* loaded from: classes.dex */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ RejectedExecutionException val$e;
        final /* synthetic */ ServiceCallback val$serviceCallback;

        AnonymousClass1(DefaultHttpClient defaultHttpClient, ServiceCallback serviceCallback, RejectedExecutionException rejectedExecutionException) {
            this.val$serviceCallback = serviceCallback;
            this.val$e = rejectedExecutionException;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.val$serviceCallback.onCallFailed(this.val$e);
        }
    }

    /* renamed from: com.microsoft.appcenter.http.DefaultHttpClient$2 */
    /* loaded from: classes.dex */
    class AnonymousClass2 implements ServiceCall {
        final /* synthetic */ DefaultHttpClientCallTask val$task;

        AnonymousClass2(DefaultHttpClient defaultHttpClient, DefaultHttpClientCallTask defaultHttpClientCallTask) {
            this.val$task = defaultHttpClientCallTask;
        }

        @Override // com.microsoft.appcenter.http.ServiceCall
        public void cancel() {
            this.val$task.cancel(true);
        }
    }

    @Override // com.microsoft.appcenter.http.DefaultHttpClientCallTask.Tracker
    public synchronized void onStart(DefaultHttpClientCallTask defaultHttpClientCallTask) {
        this.mTasks.add(defaultHttpClientCallTask);
    }

    @Override // com.microsoft.appcenter.http.DefaultHttpClientCallTask.Tracker
    public synchronized void onFinish(DefaultHttpClientCallTask defaultHttpClientCallTask) {
        this.mTasks.remove(defaultHttpClientCallTask);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        if (this.mTasks.size() > 0) {
            AppCenterLog.debug("AppCenter", "Cancelling " + this.mTasks.size() + " network call(s).");
            for (DefaultHttpClientCallTask defaultHttpClientCallTask : this.mTasks) {
                defaultHttpClientCallTask.cancel(true);
            }
            this.mTasks.clear();
        }
    }
}
