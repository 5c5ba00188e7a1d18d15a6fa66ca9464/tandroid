package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class Q0 implements z0 {
    final double[] a;
    int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Q0(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.a = new double[(int) j];
        this.b = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Q0(double[] dArr) {
        this.a = dArr;
        this.b = dArr.length;
    }

    @Override // j$.util.stream.C0, j$.util.stream.D0
    public final C0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.D0
    public final /* bridge */ /* synthetic */ D0 a(int i) {
        a(i);
        throw null;
    }

    @Override // j$.util.stream.C0
    public final Object b() {
        double[] dArr = this.a;
        int length = dArr.length;
        int i = this.b;
        return length == i ? dArr : Arrays.copyOf(dArr, i);
    }

    @Override // j$.util.stream.C0
    public final void c(Object obj, int i) {
        int i2 = this.b;
        System.arraycopy(this.a, 0, (double[]) obj, i, i2);
    }

    @Override // j$.util.stream.D0
    public final long count() {
        return this.b;
    }

    @Override // j$.util.stream.C0
    public final void d(Object obj) {
        j$.util.function.m mVar = (j$.util.function.m) obj;
        for (int i = 0; i < this.b; i++) {
            mVar.accept(this.a[i]);
        }
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ void forEach(Consumer consumer) {
        u0.v0(this, consumer);
    }

    @Override // j$.util.stream.D0
    /* renamed from: i */
    public final /* synthetic */ void e(Double[] dArr, int i) {
        u0.s0(this, dArr, i);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ int j() {
        return 0;
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ Object[] o(j$.util.function.N n) {
        return u0.r0(this, n);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.y0(this, j, j2);
    }

    @Override // j$.util.stream.C0, j$.util.stream.D0
    public final j$.util.N spliterator() {
        return j$.util.f0.j(this.a, 0, this.b);
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return j$.util.f0.j(this.a, 0, this.b);
    }

    public String toString() {
        double[] dArr = this.a;
        return String.format("DoubleArrayNode[%d][%s]", Integer.valueOf(dArr.length - this.b), Arrays.toString(dArr));
    }
}
