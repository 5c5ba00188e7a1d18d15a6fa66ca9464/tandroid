package com.google.android.exoplayer2.source;

import android.util.SparseArray;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Consumer;
/* loaded from: classes.dex */
final class SpannedData<V> {
    private final Consumer<V> removeCallback;
    private final SparseArray<V> spans = new SparseArray<>();
    private int memoizedReadIndex = -1;

    public SpannedData(Consumer<V> consumer) {
        this.removeCallback = consumer;
    }

    public V get(int i) {
        if (this.memoizedReadIndex == -1) {
            this.memoizedReadIndex = 0;
        }
        while (true) {
            int i2 = this.memoizedReadIndex;
            if (i2 <= 0 || i >= this.spans.keyAt(i2)) {
                break;
            }
            this.memoizedReadIndex--;
        }
        while (this.memoizedReadIndex < this.spans.size() - 1 && i >= this.spans.keyAt(this.memoizedReadIndex + 1)) {
            this.memoizedReadIndex++;
        }
        return this.spans.valueAt(this.memoizedReadIndex);
    }

    public void appendSpan(int i, V v) {
        if (this.memoizedReadIndex == -1) {
            Assertions.checkState(this.spans.size() == 0);
            this.memoizedReadIndex = 0;
        }
        if (this.spans.size() > 0) {
            SparseArray<V> sparseArray = this.spans;
            int keyAt = sparseArray.keyAt(sparseArray.size() - 1);
            Assertions.checkArgument(i >= keyAt);
            if (keyAt == i) {
                SparseArray<V> sparseArray2 = this.spans;
                this.removeCallback.accept(sparseArray2.valueAt(sparseArray2.size() - 1));
            }
        }
        this.spans.append(i, v);
    }

    public V getEndValue() {
        SparseArray<V> sparseArray = this.spans;
        return sparseArray.valueAt(sparseArray.size() - 1);
    }

    public void discardTo(int i) {
        int i2 = 0;
        while (i2 < this.spans.size() - 1) {
            int i3 = i2 + 1;
            if (i < this.spans.keyAt(i3)) {
                return;
            }
            this.removeCallback.accept(this.spans.valueAt(i2));
            this.spans.removeAt(i2);
            int i4 = this.memoizedReadIndex;
            if (i4 > 0) {
                this.memoizedReadIndex = i4 - 1;
            }
            i2 = i3;
        }
    }

    public void discardFrom(int i) {
        for (int size = this.spans.size() - 1; size >= 0 && i < this.spans.keyAt(size); size--) {
            this.removeCallback.accept(this.spans.valueAt(size));
            this.spans.removeAt(size);
        }
        this.memoizedReadIndex = this.spans.size() > 0 ? Math.min(this.memoizedReadIndex, this.spans.size() - 1) : -1;
    }

    public void clear() {
        for (int i = 0; i < this.spans.size(); i++) {
            this.removeCallback.accept(this.spans.valueAt(i));
        }
        this.memoizedReadIndex = -1;
        this.spans.clear();
    }

    public boolean isEmpty() {
        return this.spans.size() == 0;
    }
}
