package com.microsoft.appcenter.utils.async;

import com.microsoft.appcenter.utils.HandlerUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class DefaultAppCenterFuture implements AppCenterFuture {
    private Collection mConsumers;
    private final CountDownLatch mLatch = new CountDownLatch(1);
    private Object mResult;

    public synchronized void complete(final Object obj) {
        if (!isDone()) {
            this.mResult = obj;
            this.mLatch.countDown();
            if (this.mConsumers != null) {
                HandlerUtils.runOnUiThread(new Runnable() { // from class: com.microsoft.appcenter.utils.async.DefaultAppCenterFuture.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Iterator it = DefaultAppCenterFuture.this.mConsumers.iterator();
                        while (it.hasNext()) {
                            ((AppCenterConsumer) it.next()).accept(obj);
                        }
                        DefaultAppCenterFuture.this.mConsumers = null;
                    }
                });
            }
        }
    }

    @Override // com.microsoft.appcenter.utils.async.AppCenterFuture
    public Object get() {
        while (true) {
            try {
                this.mLatch.await();
                return this.mResult;
            } catch (InterruptedException unused) {
            }
        }
    }

    public boolean isDone() {
        while (true) {
            try {
                return this.mLatch.await(0L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException unused) {
            }
        }
    }

    @Override // com.microsoft.appcenter.utils.async.AppCenterFuture
    public synchronized void thenAccept(final AppCenterConsumer appCenterConsumer) {
        try {
            if (isDone()) {
                HandlerUtils.runOnUiThread(new Runnable() { // from class: com.microsoft.appcenter.utils.async.DefaultAppCenterFuture.1
                    @Override // java.lang.Runnable
                    public void run() {
                        appCenterConsumer.accept(DefaultAppCenterFuture.this.mResult);
                    }
                });
            } else {
                if (this.mConsumers == null) {
                    this.mConsumers = new LinkedList();
                }
                this.mConsumers.add(appCenterConsumer);
            }
        } catch (Throwable th) {
            throw th;
        }
    }
}
