package org.telegram.messenger;

import android.os.SystemClock;
import android.util.SparseIntArray;
import java.util.LinkedList;
import org.telegram.ui.Components.Reactions.HwEmojis;
/* loaded from: classes3.dex */
public class DispatchQueuePool {
    private boolean cleanupScheduled;
    private int createdCount;
    private int maxCount;
    private int totalTasksCount;
    private LinkedList<DispatchQueue> queues = new LinkedList<>();
    private SparseIntArray busyQueuesMap = new SparseIntArray();
    private LinkedList<DispatchQueue> busyQueues = new LinkedList<>();
    private Runnable cleanupRunnable = new Runnable() { // from class: org.telegram.messenger.DispatchQueuePool.1
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
    };
    private int guid = Utilities.random.nextInt();

    public DispatchQueuePool(int i) {
        this.maxCount = i;
    }

    static /* synthetic */ int access$110(DispatchQueuePool dispatchQueuePool) {
        int i = dispatchQueuePool.createdCount;
        dispatchQueuePool.createdCount = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$execute$0(DispatchQueue dispatchQueue) {
        this.totalTasksCount--;
        int i = this.busyQueuesMap.get(dispatchQueue.index) - 1;
        if (i != 0) {
            this.busyQueuesMap.put(dispatchQueue.index, i);
            return;
        }
        this.busyQueuesMap.delete(dispatchQueue.index);
        this.busyQueues.remove(dispatchQueue);
        this.queues.add(dispatchQueue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$execute$1(Runnable runnable, final DispatchQueue dispatchQueue) {
        runnable.run();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DispatchQueuePool$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DispatchQueuePool.this.lambda$execute$0(dispatchQueue);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x009a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void execute(final Runnable runnable) {
        LinkedList<DispatchQueue> linkedList;
        final DispatchQueue dispatchQueue;
        if (!this.busyQueues.isEmpty() && (this.totalTasksCount / 2 <= this.busyQueues.size() || (this.queues.isEmpty() && this.createdCount >= this.maxCount))) {
            linkedList = this.busyQueues;
        } else if (this.queues.isEmpty()) {
            dispatchQueue = new DispatchQueue("DispatchQueuePool" + this.guid + "_" + Utilities.random.nextInt());
            dispatchQueue.setPriority(10);
            this.createdCount = this.createdCount + 1;
            if (!this.cleanupScheduled) {
                AndroidUtilities.runOnUIThread(this.cleanupRunnable, 30000L);
                this.cleanupScheduled = true;
            }
            this.totalTasksCount++;
            this.busyQueues.add(dispatchQueue);
            this.busyQueuesMap.put(dispatchQueue.index, this.busyQueuesMap.get(dispatchQueue.index, 0) + 1);
            if (!HwEmojis.isHwEnabled()) {
                dispatchQueue.setPriority(1);
            } else if (dispatchQueue.getPriority() != 10) {
                dispatchQueue.setPriority(10);
            }
            dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.DispatchQueuePool$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DispatchQueuePool.this.lambda$execute$1(runnable, dispatchQueue);
                }
            });
        } else {
            linkedList = this.queues;
        }
        dispatchQueue = linkedList.remove(0);
        if (!this.cleanupScheduled) {
        }
        this.totalTasksCount++;
        this.busyQueues.add(dispatchQueue);
        this.busyQueuesMap.put(dispatchQueue.index, this.busyQueuesMap.get(dispatchQueue.index, 0) + 1);
        if (!HwEmojis.isHwEnabled()) {
        }
        dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.DispatchQueuePool$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DispatchQueuePool.this.lambda$execute$1(runnable, dispatchQueue);
            }
        });
    }
}
