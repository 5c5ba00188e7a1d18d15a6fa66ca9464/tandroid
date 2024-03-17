package j$.util;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Comparator;
import java.util.Objects;
import org.telegram.messenger.LiteMode;
/* loaded from: classes2.dex */
final class A implements t.a {
    private final double[] a;
    private int b;
    private final int c;
    private final int d;

    public A(double[] dArr, int i, int i2, int i3) {
        this.a = dArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 64 | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM;
    }

    @Override // j$.util.t.a, j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.j(this, consumer);
    }

    @Override // j$.util.t
    public int characteristics() {
        return this.d;
    }

    @Override // j$.util.u
    /* renamed from: e */
    public void forEachRemaining(j$.util.function.f fVar) {
        int i;
        Objects.requireNonNull(fVar);
        double[] dArr = this.a;
        int length = dArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i < i2) {
            do {
                fVar.accept(dArr[i]);
                i++;
            } while (i < i2);
        }
    }

    @Override // j$.util.t
    public long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.t.a, j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.b(this, consumer);
    }

    @Override // j$.util.t
    public Comparator getComparator() {
        if (a.f(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.t
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }

    @Override // j$.util.u
    /* renamed from: k */
    public boolean tryAdvance(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        double[] dArr = this.a;
        this.b = i + 1;
        fVar.accept(dArr[i]);
        return true;
    }

    @Override // j$.util.u, j$.util.t
    public t.a trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        double[] dArr = this.a;
        this.b = i2;
        return new A(dArr, i, i2, this.d);
    }
}
