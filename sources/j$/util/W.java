package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;

/* loaded from: classes2.dex */
final class W implements Q {
    private final Object[] a;
    private int b;
    private final int c;
    private final int d;

    public W(Object[] objArr, int i, int i2, int i3) {
        this.a = objArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 16448;
    }

    @Override // j$.util.Q
    public final void a(Consumer consumer) {
        int i;
        consumer.getClass();
        Object[] objArr = this.a;
        int length = objArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                consumer.r(objArr[i]);
                i++;
            } while (i < i2);
        }
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return this.d;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        if (a.k(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final /* synthetic */ long getExactSizeIfKnown() {
        return a.j(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return a.k(this, i);
    }

    @Override // j$.util.Q
    public final boolean s(Consumer consumer) {
        consumer.getClass();
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        this.b = i + 1;
        consumer.r(this.a[i]);
        return true;
    }

    @Override // j$.util.Q
    public final Q trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        this.b = i2;
        return new W(this.a, i, i2, this.d);
    }
}
