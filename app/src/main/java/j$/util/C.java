package j$.util;

import j$.util.function.Consumer;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class C implements t {
    private final double[] a;
    private int b;
    private final int c;
    private final int d;

    public C(double[] dArr, int i, int i2, int i3) {
        this.a = dArr;
        this.b = i;
        this.c = i2;
        this.d = i3 | 64 | 16384;
    }

    @Override // j$.util.t, j$.util.u
    public /* synthetic */ boolean b(Consumer consumer) {
        return a.j(this, consumer);
    }

    @Override // j$.util.u
    public int characteristics() {
        return this.d;
    }

    @Override // j$.util.t
    /* renamed from: e */
    public void forEachRemaining(j$.util.function.f fVar) {
        int i;
        fVar.getClass();
        double[] dArr = this.a;
        int length = dArr.length;
        int i2 = this.c;
        if (length < i2 || (i = this.b) < 0) {
            return;
        }
        this.b = i2;
        if (i >= i2) {
            return;
        }
        do {
            fVar.accept(dArr[i]);
            i++;
        } while (i < i2);
    }

    @Override // j$.util.u
    public long estimateSize() {
        return this.c - this.b;
    }

    @Override // j$.util.t, j$.util.u
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        a.b(this, consumer);
    }

    @Override // j$.util.u
    public Comparator getComparator() {
        if (a.f(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.u
    public /* synthetic */ long getExactSizeIfKnown() {
        return a.e(this);
    }

    @Override // j$.util.u
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return a.f(this, i);
    }

    @Override // j$.util.t
    /* renamed from: k */
    public boolean tryAdvance(j$.util.function.f fVar) {
        fVar.getClass();
        int i = this.b;
        if (i < 0 || i >= this.c) {
            return false;
        }
        double[] dArr = this.a;
        this.b = i + 1;
        fVar.accept(dArr[i]);
        return true;
    }

    @Override // j$.util.t, j$.util.w, j$.util.u
    public t trySplit() {
        int i = this.b;
        int i2 = (this.c + i) >>> 1;
        if (i >= i2) {
            return null;
        }
        double[] dArr = this.a;
        this.b = i2;
        return new C(dArr, i, i2, this.d);
    }
}
