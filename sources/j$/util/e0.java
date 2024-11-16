package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;

/* loaded from: classes2.dex */
final class e0 implements K {
    private final long[] a;
    private int b;
    private final int c;
    private final int d;

    public e0(long[] jArr, int i, int i2, int i3) {
        this.a = jArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 16448;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        a.h(this, consumer);
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return this.d;
    }

    @Override // j$.util.N
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public final void e(j$.util.function.W w) {
        int i;
        w.getClass();
        long[] jArr = this.a;
        int length = jArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                w.accept(jArr[i]);
                i++;
            } while (i < i2);
        }
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

    @Override // j$.util.N
    /* renamed from: i, reason: merged with bridge method [inline-methods] */
    public final boolean p(j$.util.function.W w) {
        w.getClass();
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        this.b = i + 1;
        w.accept(this.a[i]);
        return true;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return a.q(this, consumer);
    }

    @Override // j$.util.Q
    public final K trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        this.b = i2;
        return new e0(this.a, i, i2, this.d);
    }
}
