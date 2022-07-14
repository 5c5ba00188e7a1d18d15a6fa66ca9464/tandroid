package j$.util.stream;

import j$.util.Spliterator;
import j$.util.stream.AbstractShortCircuitTask;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes2.dex */
abstract class AbstractShortCircuitTask<P_IN, P_OUT, R, K extends AbstractShortCircuitTask<P_IN, P_OUT, R, K>> extends AbstractTask<P_IN, P_OUT, R, K> {
    protected volatile boolean canceled;
    protected final AtomicReference<R> sharedResult;

    protected abstract R getEmptyResult();

    /* JADX WARN: Generic types in debug info not equals: j$.util.Spliterator != java.util.Spliterator<P_IN> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.PipelineHelper != java.util.stream.PipelineHelper<P_OUT> */
    public AbstractShortCircuitTask(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
        super(pipelineHelper, spliterator);
        this.sharedResult = new AtomicReference<>(null);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.Spliterator != java.util.Spliterator<P_IN> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    public AbstractShortCircuitTask(K parent, Spliterator<P_IN> spliterator) {
        super(parent, spliterator);
        this.sharedResult = parent.sharedResult;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0054, code lost:
        r9 = r6.doLeaf();
     */
    /* JADX WARN: Generic types in debug info not equals: j$.util.Spliterator != java.util.Spliterator<P_IN> */
    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v1, types: [j$.util.stream.AbstractShortCircuitTask] */
    @Override // j$.util.stream.AbstractTask, java.util.concurrent.CountedCompleter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void compute() {
        Object obj;
        Spliterator<P_IN> trySplit;
        K taskToFork;
        AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask;
        Spliterator<P_IN> spliterator = this.spliterator;
        long sizeEstimate = spliterator.estimateSize();
        long sizeThreshold = getTargetSize(sizeEstimate);
        boolean forkRight = false;
        AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask2 = this;
        AtomicReference<R> sr = this.sharedResult;
        while (true) {
            R r = sr.get();
            obj = r;
            if (r != null) {
                break;
            } else if (abstractShortCircuitTask2.taskCanceled()) {
                obj = abstractShortCircuitTask2.getEmptyResult();
                break;
            } else if (sizeEstimate <= sizeThreshold || (trySplit = spliterator.trySplit()) == null) {
                break;
            } else {
                AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask3 = (AbstractShortCircuitTask) abstractShortCircuitTask2.makeChild(trySplit);
                abstractShortCircuitTask2.leftChild = abstractShortCircuitTask3;
                AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask4 = (AbstractShortCircuitTask) abstractShortCircuitTask2.makeChild(spliterator);
                abstractShortCircuitTask2.rightChild = abstractShortCircuitTask4;
                abstractShortCircuitTask2.setPendingCount(1);
                if (forkRight) {
                    forkRight = false;
                    spliterator = trySplit;
                    abstractShortCircuitTask = abstractShortCircuitTask3;
                    taskToFork = abstractShortCircuitTask4;
                } else {
                    forkRight = true;
                    abstractShortCircuitTask = abstractShortCircuitTask4;
                    taskToFork = abstractShortCircuitTask3;
                }
                taskToFork.fork();
                sizeEstimate = spliterator.estimateSize();
                abstractShortCircuitTask2 = abstractShortCircuitTask;
            }
        }
        abstractShortCircuitTask2.setLocalResult(obj);
        abstractShortCircuitTask2.tryComplete();
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    public void shortCircuit(R result) {
        if (result != null) {
            this.sharedResult.compareAndSet(null, result);
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    @Override // j$.util.stream.AbstractTask
    public void setLocalResult(R localResult) {
        if (isRoot()) {
            if (localResult != null) {
                this.sharedResult.compareAndSet(null, localResult);
                return;
            }
            return;
        }
        super.setLocalResult(localResult);
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    @Override // j$.util.stream.AbstractTask, java.util.concurrent.CountedCompleter, java.util.concurrent.ForkJoinTask
    public R getRawResult() {
        return getLocalResult();
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    @Override // j$.util.stream.AbstractTask
    public R getLocalResult() {
        if (isRoot()) {
            R answer = this.sharedResult.get();
            return answer == null ? getEmptyResult() : answer;
        }
        return (R) super.getLocalResult();
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    public void cancel() {
        this.canceled = true;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    protected boolean taskCanceled() {
        boolean cancel = this.canceled;
        if (!cancel) {
            for (AbstractShortCircuitTask abstractShortCircuitTask = (AbstractShortCircuitTask) getParent(); !cancel && abstractShortCircuitTask != null; abstractShortCircuitTask = (AbstractShortCircuitTask) abstractShortCircuitTask.getParent()) {
                cancel = abstractShortCircuitTask.canceled;
            }
        }
        return cancel;
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.stream.AbstractShortCircuitTask != java.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K extends j$.util.stream.AbstractShortCircuitTask<P_IN, P_OUT, R, K>> */
    public void cancelLaterNodes() {
        K node = this;
        for (AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask = (AbstractShortCircuitTask) getParent(); abstractShortCircuitTask != null; abstractShortCircuitTask = (AbstractShortCircuitTask) abstractShortCircuitTask.getParent()) {
            if (abstractShortCircuitTask.leftChild == node) {
                AbstractShortCircuitTask abstractShortCircuitTask2 = (AbstractShortCircuitTask) abstractShortCircuitTask.rightChild;
                if (!abstractShortCircuitTask2.canceled) {
                    abstractShortCircuitTask2.cancel();
                }
            }
            node = abstractShortCircuitTask;
        }
    }
}
