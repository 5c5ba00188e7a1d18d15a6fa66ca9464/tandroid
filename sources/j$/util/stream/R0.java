package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class R0 implements z0 {
    final double[] a;
    int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public R0(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.a = new double[(int) j];
        this.b = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public R0(double[] dArr) {
        this.a = dArr;
        this.b = dArr.length;
    }

    @Override // j$.util.stream.E0, j$.util.stream.F0
    public final E0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.F0
    public final /* bridge */ /* synthetic */ F0 a(int i) {
        a(i);
        throw null;
    }

    @Override // j$.util.stream.F0
    public final long count() {
        return this.b;
    }

    @Override // j$.util.stream.E0
    public final void d(Object obj, int i) {
        int i2 = this.b;
        System.arraycopy(this.a, 0, (double[]) obj, i, i2);
    }

    @Override // j$.util.stream.E0
    public final Object e() {
        double[] dArr = this.a;
        int length = dArr.length;
        int i = this.b;
        return length == i ? dArr : Arrays.copyOf(dArr, i);
    }

    @Override // j$.util.stream.F0
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Double[] dArr, int i) {
        t0.n(this, dArr, i);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.q(this, consumer);
    }

    @Override // j$.util.stream.E0
    public final void g(Object obj) {
        j$.util.function.n nVar = (j$.util.function.n) obj;
        for (int i = 0; i < this.b; i++) {
            nVar.accept(this.a[i]);
        }
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ Object[] s(j$.util.function.I i) {
        return t0.m(this, i);
    }

    @Override // j$.util.stream.E0, j$.util.stream.F0
    public final j$.util.N spliterator() {
        return j$.util.f0.j(this.a, 0, this.b);
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return j$.util.f0.j(this.a, 0, this.b);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.t(this, j, j2);
    }

    public String toString() {
        double[] dArr = this.a;
        return String.format("DoubleArrayNode[%d][%s]", Integer.valueOf(dArr.length - this.b), Arrays.toString(dArr));
    }
}
