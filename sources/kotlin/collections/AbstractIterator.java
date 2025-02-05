package kotlin.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
public abstract class AbstractIterator implements Iterator {
    private Object nextValue;
    private State state = State.NotReady;

    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[State.values().length];
            try {
                iArr[State.Done.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[State.Ready.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private final boolean tryToComputeNext() {
        this.state = State.Failed;
        computeNext();
        return this.state == State.Ready;
    }

    protected abstract void computeNext();

    protected final void done() {
        this.state = State.Done;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        State state = this.state;
        if (state == State.Failed) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int i = WhenMappings.$EnumSwitchMapping$0[state.ordinal()];
        if (i == 1) {
            return false;
        }
        if (i != 2) {
            return tryToComputeNext();
        }
        return true;
    }

    @Override // java.util.Iterator
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        this.state = State.NotReady;
        return this.nextValue;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    protected final void setNext(Object obj) {
        this.nextValue = obj;
        this.state = State.Ready;
    }
}
