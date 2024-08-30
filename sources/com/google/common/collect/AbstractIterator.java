package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;
/* loaded from: classes.dex */
public abstract class AbstractIterator extends UnmodifiableIterator {
    private Object next;
    private State state = State.NOT_READY;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static /* synthetic */ class 1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$collect$AbstractIterator$State;

        static {
            int[] iArr = new int[State.values().length];
            $SwitchMap$com$google$common$collect$AbstractIterator$State = iArr;
            try {
                iArr[State.DONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$common$collect$AbstractIterator$State[State.READY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum State {
        READY,
        NOT_READY,
        DONE,
        FAILED
    }

    private boolean tryToComputeNext() {
        this.state = State.FAILED;
        this.next = computeNext();
        if (this.state != State.DONE) {
            this.state = State.READY;
            return true;
        }
        return false;
    }

    protected abstract Object computeNext();

    /* JADX INFO: Access modifiers changed from: protected */
    public final Object endOfData() {
        this.state = State.DONE;
        return null;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        Preconditions.checkState(this.state != State.FAILED);
        int i = 1.$SwitchMap$com$google$common$collect$AbstractIterator$State[this.state.ordinal()];
        if (i != 1) {
            if (i != 2) {
                return tryToComputeNext();
            }
            return true;
        }
        return false;
    }

    @Override // java.util.Iterator
    public final Object next() {
        if (hasNext()) {
            this.state = State.NOT_READY;
            Object uncheckedCastNullableTToT = NullnessCasts.uncheckedCastNullableTToT(this.next);
            this.next = null;
            return uncheckedCastNullableTToT;
        }
        throw new NoSuchElementException();
    }
}
