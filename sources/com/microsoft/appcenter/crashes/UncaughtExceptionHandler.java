package com.microsoft.appcenter.crashes;

import com.microsoft.appcenter.utils.ShutdownHelper;
import java.lang.Thread;
/* loaded from: classes.dex */
class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
    private boolean mIgnoreDefaultExceptionHandler = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void register() {
        this.mDefaultUncaughtExceptionHandler = !this.mIgnoreDefaultExceptionHandler ? Thread.getDefaultUncaughtExceptionHandler() : null;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        Crashes.getInstance().saveUncaughtException(thread, th);
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.mDefaultUncaughtExceptionHandler;
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
        } else {
            ShutdownHelper.shutdown(10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregister() {
        Thread.setDefaultUncaughtExceptionHandler(this.mDefaultUncaughtExceptionHandler);
    }
}
