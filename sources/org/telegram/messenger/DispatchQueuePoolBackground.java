package org.telegram.messenger;

import android.os.SystemClock;
import android.util.SparseIntArray;
import java.util.ArrayList;
import org.telegram.ui.Components.Reactions.HwEmojis;

/* loaded from: classes3.dex */
public class DispatchQueuePoolBackground {
    public static final String THREAD_PREFIX = "DispatchQueuePoolThreadSafety_";
    private static DispatchQueuePoolBackground backgroundQueue;
    static ArrayList<Runnable> updateTaskCollection;
    private boolean cleanupScheduled;
    private int createdCount;
    private int maxCount;
    private int totalTasksCount;
    private static final ArrayList<ArrayList<Runnable>> freeCollections = new ArrayList<>();
    private static final Runnable finishCollectUpdateRunnable = new Runnable() { // from class: org.telegram.messenger.DispatchQueuePoolBackground.2
        @Override // java.lang.Runnable
        public void run() {
            DispatchQueuePoolBackground.finishCollectUpdateRunnables();
        }
    };
    private ArrayList<DispatchQueue> queues = new ArrayList<>(10);
    private SparseIntArray busyQueuesMap = new SparseIntArray();
    private ArrayList<DispatchQueue> busyQueues = new ArrayList<>(10);
    private Runnable cleanupRunnable = new Runnable() { // from class: org.telegram.messenger.DispatchQueuePoolBackground.1
        @Override // java.lang.Runnable
        public void run() {
            if (!DispatchQueuePoolBackground.this.queues.isEmpty()) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                int i = 0;
                while (i < DispatchQueuePoolBackground.this.queues.size()) {
                    DispatchQueue dispatchQueue = (DispatchQueue) DispatchQueuePoolBackground.this.queues.get(i);
                    if (dispatchQueue.getLastTaskTime() < elapsedRealtime - 30000) {
                        dispatchQueue.recycle();
                        DispatchQueuePoolBackground.this.queues.remove(i);
                        DispatchQueuePoolBackground.access$110(DispatchQueuePoolBackground.this);
                        i--;
                    }
                    i++;
                }
            }
            if (DispatchQueuePoolBackground.this.queues.isEmpty() && DispatchQueuePoolBackground.this.busyQueues.isEmpty()) {
                DispatchQueuePoolBackground.this.cleanupScheduled = false;
            } else {
                Utilities.globalQueue.postRunnable(this, 30000L);
                DispatchQueuePoolBackground.this.cleanupScheduled = true;
            }
        }
    };
    private int guid = Utilities.random.nextInt();

    private DispatchQueuePoolBackground(int i) {
        this.maxCount = i;
    }

    static /* synthetic */ int access$110(DispatchQueuePoolBackground dispatchQueuePoolBackground) {
        int i = dispatchQueuePoolBackground.createdCount;
        dispatchQueuePoolBackground.createdCount = i - 1;
        return i;
    }

    public static void execute(Runnable runnable) {
        execute(runnable, false);
    }

    public static void execute(Runnable runnable, boolean z) {
        if (Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
            if (BuildVars.DEBUG_VERSION) {
                FileLog.e(new RuntimeException("wrong thread"));
                return;
            }
            return;
        }
        if (updateTaskCollection == null) {
            ArrayList<ArrayList<Runnable>> arrayList = freeCollections;
            updateTaskCollection = !arrayList.isEmpty() ? arrayList.remove(arrayList.size() - 1) : new ArrayList<>(100);
            if (!z) {
                AndroidUtilities.runOnUIThread(finishCollectUpdateRunnable);
            }
        }
        updateTaskCollection.add(runnable);
        if (z) {
            Runnable runnable2 = finishCollectUpdateRunnable;
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            runnable2.run();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00ad  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void execute(ArrayList<Runnable> arrayList) {
        ArrayList<DispatchQueue> arrayList2;
        final DispatchQueue dispatchQueue;
        for (int i = 0; i < arrayList.size(); i++) {
            final Runnable runnable = arrayList.get(i);
            if (runnable != null) {
                if (!this.busyQueues.isEmpty() && (this.totalTasksCount / 2 <= this.busyQueues.size() || (this.queues.isEmpty() && this.createdCount >= this.maxCount))) {
                    arrayList2 = this.busyQueues;
                } else if (this.queues.isEmpty()) {
                    dispatchQueue = new DispatchQueue(THREAD_PREFIX + this.guid + "_" + Utilities.random.nextInt());
                    dispatchQueue.setPriority(10);
                    this.createdCount = this.createdCount + 1;
                    if (!this.cleanupScheduled) {
                        Utilities.globalQueue.postRunnable(this.cleanupRunnable, 30000L);
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
                    dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.DispatchQueuePoolBackground$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            DispatchQueuePoolBackground.this.lambda$execute$1(runnable, dispatchQueue);
                        }
                    });
                } else {
                    arrayList2 = this.queues;
                }
                dispatchQueue = arrayList2.remove(0);
                if (!this.cleanupScheduled) {
                }
                this.totalTasksCount++;
                this.busyQueues.add(dispatchQueue);
                this.busyQueuesMap.put(dispatchQueue.index, this.busyQueuesMap.get(dispatchQueue.index, 0) + 1);
                if (!HwEmojis.isHwEnabled()) {
                }
                dispatchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.DispatchQueuePoolBackground$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DispatchQueuePoolBackground.this.lambda$execute$1(runnable, dispatchQueue);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void finishCollectUpdateRunnables() {
        ArrayList<Runnable> arrayList = updateTaskCollection;
        if (arrayList == null || arrayList.isEmpty()) {
            updateTaskCollection = null;
            return;
        }
        final ArrayList<Runnable> arrayList2 = updateTaskCollection;
        updateTaskCollection = null;
        if (backgroundQueue == null) {
            backgroundQueue = new DispatchQueuePoolBackground(Math.max(1, Runtime.getRuntime().availableProcessors()));
        }
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.DispatchQueuePoolBackground$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DispatchQueuePoolBackground.lambda$finishCollectUpdateRunnables$3(arrayList2);
            }
        });
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
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.DispatchQueuePoolBackground$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DispatchQueuePoolBackground.this.lambda$execute$0(dispatchQueue);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$finishCollectUpdateRunnables$2(ArrayList arrayList) {
        freeCollections.add(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$finishCollectUpdateRunnables$3(final ArrayList arrayList) {
        backgroundQueue.execute((ArrayList<Runnable>) arrayList);
        arrayList.clear();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.DispatchQueuePoolBackground$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                DispatchQueuePoolBackground.lambda$finishCollectUpdateRunnables$2(arrayList);
            }
        });
    }
}
