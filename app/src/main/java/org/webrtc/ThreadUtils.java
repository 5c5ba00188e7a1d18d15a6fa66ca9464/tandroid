package org.webrtc;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/* loaded from: classes3.dex */
public class ThreadUtils {

    /* loaded from: classes3.dex */
    public interface BlockingOperation {
        void run() throws InterruptedException;
    }

    /* loaded from: classes3.dex */
    public static class ThreadChecker {
        private Thread thread = Thread.currentThread();

        public void checkIsOnValidThread() {
            if (this.thread == null) {
                this.thread = Thread.currentThread();
            }
            if (Thread.currentThread() == this.thread) {
                return;
            }
            throw new IllegalStateException("Wrong thread");
        }

        public void detachThread() {
            this.thread = null;
        }
    }

    public static void checkIsOnMainThread() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            return;
        }
        throw new IllegalStateException("Not on main thread!");
    }

    public static void executeUninterruptibly(BlockingOperation blockingOperation) {
        boolean z = false;
        while (true) {
            try {
                blockingOperation.run();
                break;
            } catch (InterruptedException unused) {
                z = true;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean joinUninterruptibly(Thread thread, long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        boolean z = false;
        long j2 = j;
        while (j2 > 0) {
            try {
                thread.join(j2);
                break;
            } catch (InterruptedException unused) {
                j2 = j - (SystemClock.elapsedRealtime() - elapsedRealtime);
                z = true;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
        return !thread.isAlive();
    }

    /* renamed from: org.webrtc.ThreadUtils$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 implements BlockingOperation {
        final /* synthetic */ Thread val$thread;

        AnonymousClass1(Thread thread) {
            this.val$thread = thread;
        }

        @Override // org.webrtc.ThreadUtils.BlockingOperation
        public void run() throws InterruptedException {
            this.val$thread.join();
        }
    }

    public static void joinUninterruptibly(Thread thread) {
        executeUninterruptibly(new AnonymousClass1(thread));
    }

    /* renamed from: org.webrtc.ThreadUtils$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements BlockingOperation {
        final /* synthetic */ CountDownLatch val$latch;

        AnonymousClass2(CountDownLatch countDownLatch) {
            this.val$latch = countDownLatch;
        }

        @Override // org.webrtc.ThreadUtils.BlockingOperation
        public void run() throws InterruptedException {
            this.val$latch.await();
        }
    }

    public static void awaitUninterruptibly(CountDownLatch countDownLatch) {
        executeUninterruptibly(new AnonymousClass2(countDownLatch));
    }

    public static boolean awaitUninterruptibly(CountDownLatch countDownLatch, long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        boolean z = false;
        long j2 = j;
        boolean z2 = false;
        do {
            try {
                z = countDownLatch.await(j2, TimeUnit.MILLISECONDS);
                break;
            } catch (InterruptedException unused) {
                z2 = true;
                j2 = j - (SystemClock.elapsedRealtime() - elapsedRealtime);
                if (j2 <= 0) {
                }
            }
        } while (j2 <= 0);
        if (z2) {
            Thread.currentThread().interrupt();
        }
        return z;
    }

    public static <V> V invokeAtFrontUninterruptibly(Handler handler, Callable<V> callable) {
        if (handler.getLooper().getThread() == Thread.currentThread()) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        C1Result c1Result = new C1Result();
        C1CaughtException c1CaughtException = new C1CaughtException();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        handler.post(new AnonymousClass3(c1Result, callable, c1CaughtException, countDownLatch));
        awaitUninterruptibly(countDownLatch);
        if (c1CaughtException.e != null) {
            RuntimeException runtimeException = new RuntimeException(c1CaughtException.e);
            runtimeException.setStackTrace(concatStackTraces(c1CaughtException.e.getStackTrace(), runtimeException.getStackTrace()));
            throw runtimeException;
        }
        return c1Result.value;
    }

    /* renamed from: org.webrtc.ThreadUtils$1CaughtException */
    /* loaded from: classes3.dex */
    public class C1CaughtException {
        Exception e;

        C1CaughtException() {
        }
    }

    /* renamed from: org.webrtc.ThreadUtils$1Result */
    /* loaded from: classes3.dex */
    public class C1Result {
        public V value;

        C1Result() {
        }
    }

    /* renamed from: org.webrtc.ThreadUtils$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ CountDownLatch val$barrier;
        final /* synthetic */ Callable val$callable;
        final /* synthetic */ C1CaughtException val$caughtException;
        final /* synthetic */ C1Result val$result;

        AnonymousClass3(C1Result c1Result, Callable callable, C1CaughtException c1CaughtException, CountDownLatch countDownLatch) {
            this.val$result = c1Result;
            this.val$callable = callable;
            this.val$caughtException = c1CaughtException;
            this.val$barrier = countDownLatch;
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [V, java.lang.Object] */
        @Override // java.lang.Runnable
        public void run() {
            try {
                this.val$result.value = this.val$callable.call();
            } catch (Exception e) {
                this.val$caughtException.e = e;
            }
            this.val$barrier.countDown();
        }
    }

    /* renamed from: org.webrtc.ThreadUtils$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements Callable<Void> {
        final /* synthetic */ Runnable val$runner;

        AnonymousClass4(Runnable runnable) {
            this.val$runner = runnable;
        }

        @Override // java.util.concurrent.Callable
        public Void call() {
            this.val$runner.run();
            return null;
        }
    }

    public static void invokeAtFrontUninterruptibly(Handler handler, Runnable runnable) {
        invokeAtFrontUninterruptibly(handler, new AnonymousClass4(runnable));
    }

    static StackTraceElement[] concatStackTraces(StackTraceElement[] stackTraceElementArr, StackTraceElement[] stackTraceElementArr2) {
        StackTraceElement[] stackTraceElementArr3 = new StackTraceElement[stackTraceElementArr.length + stackTraceElementArr2.length];
        System.arraycopy(stackTraceElementArr, 0, stackTraceElementArr3, 0, stackTraceElementArr.length);
        System.arraycopy(stackTraceElementArr2, 0, stackTraceElementArr3, stackTraceElementArr.length, stackTraceElementArr2.length);
        return stackTraceElementArr3;
    }
}
