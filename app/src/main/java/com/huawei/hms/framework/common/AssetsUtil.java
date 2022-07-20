package com.huawei.hms.framework.common;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public class AssetsUtil {
    private static final ExecutorService EXECUTOR_SERVICE = ExecutorsUtils.newSingleThreadExecutor("AssetsUtil_Operate");
    private static final int GET_SP_TIMEOUT = 5;
    private static final String TAG = "AssetsUtil";
    private static final String THREAD_NAME = "AssetsUtil_Operate";

    public static String[] list(Context context, String str) {
        if (context == null) {
            Logger.w("AssetsUtil", "context is null");
            return new String[0];
        }
        FutureTask futureTask = new FutureTask(new AnonymousClass1(context, str));
        EXECUTOR_SERVICE.execute(futureTask);
        try {
            return (String[]) futureTask.get(5L, TimeUnit.SECONDS);
        } catch (TimeoutException unused) {
            Logger.w("AssetsUtil", "get local config files from sp task timed out");
            return new String[0];
        } catch (Exception unused2) {
            Logger.w("AssetsUtil", "get local config files from sp task occur unknown Exception");
            return new String[0];
        } catch (InterruptedException e) {
            Logger.w("AssetsUtil", "get local config files from sp task interrupted", e);
            return new String[0];
        } catch (ExecutionException e2) {
            Logger.w("AssetsUtil", "get local config files from sp task failed", e2);
            return new String[0];
        } finally {
            futureTask.cancel(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.huawei.hms.framework.common.AssetsUtil$1 */
    /* loaded from: classes.dex */
    public static class AnonymousClass1 implements Callable<String[]> {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$path;

        AnonymousClass1(Context context, String str) {
            this.val$context = context;
            this.val$path = str;
        }

        @Override // java.util.concurrent.Callable
        public String[] call() throws Exception {
            return this.val$context.getAssets().list(this.val$path);
        }
    }

    public static InputStream open(Context context, String str) throws IOException {
        if (context == null) {
            Logger.w("AssetsUtil", "context is null");
            return null;
        }
        try {
            return context.getAssets().open(str);
        } catch (RuntimeException e) {
            Logger.e("AssetsUtil", "AssetManager has been destroyed", e);
            return null;
        }
    }
}
