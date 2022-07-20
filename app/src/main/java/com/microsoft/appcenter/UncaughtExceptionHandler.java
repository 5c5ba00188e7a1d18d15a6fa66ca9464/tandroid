package com.microsoft.appcenter;

import android.os.Handler;
import com.microsoft.appcenter.channel.Channel;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.ShutdownHelper;
import java.lang.Thread;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Channel mChannel;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
    private final Handler mHandler;

    public UncaughtExceptionHandler(Handler handler, Channel channel) {
        this.mHandler = handler;
        this.mChannel = channel;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        if (AppCenter.getInstance().isInstanceEnabled()) {
            Semaphore semaphore = new Semaphore(0);
            this.mHandler.post(new AnonymousClass1(semaphore));
            try {
                if (!semaphore.tryAcquire(5000L, TimeUnit.MILLISECONDS)) {
                    AppCenterLog.error("AppCenter", "Timeout waiting for looper tasks to complete.");
                }
            } catch (InterruptedException e) {
                AppCenterLog.warn("AppCenter", "Interrupted while waiting looper to flush.", e);
            }
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.mDefaultUncaughtExceptionHandler;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        } else {
            ShutdownHelper.shutdown(10);
        }
    }

    /* renamed from: com.microsoft.appcenter.UncaughtExceptionHandler$1 */
    /* loaded from: classes.dex */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ Semaphore val$semaphore;

        AnonymousClass1(Semaphore semaphore) {
            UncaughtExceptionHandler.this = r1;
            this.val$semaphore = semaphore;
        }

        @Override // java.lang.Runnable
        public void run() {
            UncaughtExceptionHandler.this.mChannel.shutdown();
            AppCenterLog.debug("AppCenter", "Channel completed shutdown.");
            this.val$semaphore.release();
        }
    }

    public void register() {
        this.mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
}
