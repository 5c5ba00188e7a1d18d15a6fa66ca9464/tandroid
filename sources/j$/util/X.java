package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
final class X implements E {
    private final double[] a;
    private int b;
    private final int c;
    private final int d;

    public X(double[] dArr, int i, int i2, int i3) {
        this.a = dArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 64 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
    }

    @Override // j$.util.E, j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return a.n(this, consumer);
    }

    @Override // j$.util.Q
    public final int characteristics() {
        return this.d;
    }

    @Override // j$.util.N
    /* renamed from: d */
    public final void forEachRemaining(j$.util.function.m mVar) {
        int i;
        mVar.getClass();
        double[] dArr = this.a;
        int length = dArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                mVar.accept(dArr[i]);
                i++;
            } while (i < i2);
        }
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.E, j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.f(this, consumer);
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
        return a.i(this);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return a.k(this, i);
    }

    @Override // j$.util.N
    /* renamed from: o */
    public final boolean tryAdvance(j$.util.function.m mVar) {
        mVar.getClass();
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        this.b = i + 1;
        mVar.accept(this.a[i]);
        return true;
    }

    @Override // j$.util.Q
    public final E trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        this.b = i2;
        return new X(this.a, i, i2, this.d);
    }
}
