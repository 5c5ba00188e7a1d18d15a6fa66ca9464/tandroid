package androidx.core.util;
/* loaded from: classes.dex */
public class Pools$SimplePool<T> implements Pools$Pool<T> {
    private final Object[] mPool;
    private int mPoolSize;

    public Pools$SimplePool(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("The max pool size must be > 0");
        }
        this.mPool = new Object[i];
    }

    @Override // androidx.core.util.Pools$Pool
    public T acquire() {
        int i = this.mPoolSize;
        if (i > 0) {
            int i2 = i - 1;
            Object[] objArr = this.mPool;
            T t = (T) objArr[i2];
            objArr[i2] = null;
            this.mPoolSize = i - 1;
            return t;
        }
        return null;
    }

    @Override // androidx.core.util.Pools$Pool
    public boolean release(T t) {
        if (isInPool(t)) {
            throw new IllegalStateException("Already in the pool!");
        }
        int i = this.mPoolSize;
        Object[] objArr = this.mPool;
        if (i < objArr.length) {
            objArr[i] = t;
            this.mPoolSize = i + 1;
            return true;
        }
        return false;
    }

    private boolean isInPool(T t) {
        for (int i = 0; i < this.mPoolSize; i++) {
            if (this.mPool[i] == t) {
                return true;
            }
        }
        return false;
    }
}
