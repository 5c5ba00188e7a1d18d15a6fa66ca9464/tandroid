package org.telegram.messenger;

import android.os.SystemClock;
import android.util.SparseIntArray;
import java.util.LinkedList;
/* loaded from: classes.dex */
public class DispatchQueuePool {
    private boolean cleanupScheduled;
    private int createdCount;
    private int maxCount;
    private int totalTasksCount;
    private LinkedList<DispatchQueue> queues = new LinkedList<>();
    private SparseIntArray busyQueuesMap = new SparseIntArray();
    private LinkedList<DispatchQueue> busyQueues = new LinkedList<>();
    private Runnable cleanupRunnable = new AnonymousClass1();
    private int guid = Utilities.random.nextInt();

    static /* synthetic */ int access$110(DispatchQueuePool dispatchQueuePool) {
        int i = dispatchQueuePool.createdCount;
        dispatchQueuePool.createdCount = i - 1;
        return i;
    }

    /* renamed from: org.telegram.messenger.DispatchQueuePool$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            DispatchQueuePool.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!DispatchQueuePool.this.queues.isEmpty()) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                int size = DispatchQueuePool.this.queues.size();
                int i = 0;
                while (i < size) {
                    DispatchQueue dispatchQueue = (DispatchQueue) DispatchQueuePool.this.queues.get(i);
                    if (dispatchQueue.getLastTaskTime() < elapsedRealtime - 30000) {
                        dispatchQueue.recycle();
                        DispatchQueuePool.this.queues.remove(i);
                        DispatchQueuePool.access$110(DispatchQueuePool.this);
                        i--;
                        size--;
                    }
                    i++;
                }
            }
            if (DispatchQueuePool.this.queues.isEmpty() && DispatchQueuePool.this.busyQueues.isEmpty()) {
                DispatchQueuePool.this.cleanupScheduled = false;
                return;
            }
            AndroidUtilities.runOnUIThread(this, 30000L);
            DispatchQueuePool.this.cleanupScheduled = true;
        }
    }

    public DispatchQueuePool(int i) {
        this.maxCount = i;
    }

    public void execute(Runnable runnable) {
        DispatchQueue dispatchQueue;
        if (!this.busyQueues.isEmpty() && (this.totalTasksCount / 2 <= this.busyQueues.size() || (this.queues.isEmpty() && this.createdCount >= this.maxCount))) {
            dispatchQueue = this.busyQueues.remove(0);
        } else if (this.queues.isEmpty()) {
            dispatchQueue = new DispatchQueue("DispatchQueuePool" + this.guid + "_" + Utilities.random.nextInt());
            dispatchQueue.setPriority(10);
            this.createdCount = this.createdCount + 1;
        } else {
            dispatchQueue = this.queues.remove(0);
        }
        if (!this.cleanupScheduled) {
            AndroidUtilities.runOnUIThread(this.cleanupRunnable, 30000L);
            this.cleanupScheduled = true;
        }
        this.totalTasksCount++;
        this.busyQueues.add(dispatchQueue);
        this.busyQueuesMap.put(dispatchQueue.index, this.busyQueuesMap.get(dispatchQueue.index, 0) + 1);
        dispatchQueue.postRunnable(new DispatchQueuePool$$ExternalSyntheticLambda0(this, runnable, dispatchQueue));
    }

    public /* synthetic */ void lambda$execute$1(Runnable runnable, DispatchQueue dispatchQueue) {
        runnable.run();
        AndroidUtilities.runOnUIThread(new DispatchQueuePool$$ExternalSyntheticLambda1(this, dispatchQueue));
    }

    public /* synthetic */ void lambda$execute$0(DispatchQueue dispatchQueue) {
        this.totalTasksCount--;
        int i = this.busyQueuesMap.get(dispatchQueue.index) - 1;
        if (i == 0) {
            this.busyQueuesMap.delete(dispatchQueue.index);
            this.busyQueues.remove(dispatchQueue);
            this.queues.add(dispatchQueue);
            return;
        }
        this.busyQueuesMap.put(dispatchQueue.index, i);
    }
}
