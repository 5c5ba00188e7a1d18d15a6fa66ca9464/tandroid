package com.google.android.exoplayer2.util;

import java.util.Arrays;
/* loaded from: classes.dex */
public final class TimedValueQueue {
    private int first;
    private int size;
    private long[] timestamps;
    private Object[] values;

    public TimedValueQueue() {
        this(10);
    }

    public TimedValueQueue(int i) {
        this.timestamps = new long[i];
        this.values = newArray(i);
    }

    private void addUnchecked(long j, Object obj) {
        int i = this.first;
        int i2 = this.size;
        Object[] objArr = this.values;
        int length = (i + i2) % objArr.length;
        this.timestamps[length] = j;
        objArr[length] = obj;
        this.size = i2 + 1;
    }

    private void clearBufferOnTimeDiscontinuity(long j) {
        int i = this.size;
        if (i > 0) {
            if (j <= this.timestamps[((this.first + i) - 1) % this.values.length]) {
                clear();
            }
        }
    }

    private void doubleCapacityIfFull() {
        int length = this.values.length;
        if (this.size < length) {
            return;
        }
        int i = length * 2;
        long[] jArr = new long[i];
        Object[] newArray = newArray(i);
        int i2 = this.first;
        int i3 = length - i2;
        System.arraycopy(this.timestamps, i2, jArr, 0, i3);
        System.arraycopy(this.values, this.first, newArray, 0, i3);
        int i4 = this.first;
        if (i4 > 0) {
            System.arraycopy(this.timestamps, 0, jArr, i3, i4);
            System.arraycopy(this.values, 0, newArray, i3, this.first);
        }
        this.timestamps = jArr;
        this.values = newArray;
        this.first = 0;
    }

    private static Object[] newArray(int i) {
        return new Object[i];
    }

    private Object poll(long j, boolean z) {
        Object obj = null;
        long j2 = Long.MAX_VALUE;
        while (this.size > 0) {
            long j3 = j - this.timestamps[this.first];
            if (j3 < 0 && (z || (-j3) >= j2)) {
                break;
            }
            obj = popFirst();
            j2 = j3;
        }
        return obj;
    }

    private Object popFirst() {
        Assertions.checkState(this.size > 0);
        Object[] objArr = this.values;
        int i = this.first;
        Object obj = objArr[i];
        objArr[i] = null;
        this.first = (i + 1) % objArr.length;
        this.size--;
        return obj;
    }

    public synchronized void add(long j, Object obj) {
        clearBufferOnTimeDiscontinuity(j);
        doubleCapacityIfFull();
        addUnchecked(j, obj);
    }

    public synchronized void clear() {
        this.first = 0;
        this.size = 0;
        Arrays.fill(this.values, (Object) null);
    }

    public synchronized Object pollFirst() {
        return this.size == 0 ? null : popFirst();
    }

    public synchronized Object pollFloor(long j) {
        return poll(j, true);
    }

    public synchronized int size() {
        return this.size;
    }
}
