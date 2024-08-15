package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class i1 implements B0 {
    final long[] a;
    int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i1(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.a = new long[(int) j];
        this.b = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i1(long[] jArr) {
        this.a = jArr;
        this.b = jArr.length;
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
        long[] jArr = this.a;
        int length = jArr.length;
        int i = this.b;
        return length == i ? jArr : Arrays.copyOf(jArr, i);
    }

    @Override // j$.util.stream.C0
    public final void c(Object obj, int i) {
        int i2 = this.b;
        System.arraycopy(this.a, 0, (long[]) obj, i, i2);
    }

    @Override // j$.util.stream.D0
    public final long count() {
        return this.b;
    }

    @Override // j$.util.stream.C0
    public final void d(Object obj) {
        j$.util.function.h0 h0Var = (j$.util.function.h0) obj;
        for (int i = 0; i < this.b; i++) {
            h0Var.accept(this.a[i]);
        }
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ void forEach(Consumer consumer) {
        u0.x0(this, consumer);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ int j() {
        return 0;
    }

    @Override // j$.util.stream.D0
    /* renamed from: m */
    public final /* synthetic */ void e(Long[] lArr, int i) {
        u0.u0(this, lArr, i);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ Object[] o(j$.util.function.N n) {
        return u0.r0(this, n);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.A0(this, j, j2);
    }

    @Override // j$.util.stream.C0, j$.util.stream.D0
    public final j$.util.N spliterator() {
        return j$.util.f0.l(this.a, 0, this.b);
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return j$.util.f0.l(this.a, 0, this.b);
    }

    public String toString() {
        long[] jArr = this.a;
        return String.format("LongArrayNode[%d][%s]", Integer.valueOf(jArr.length - this.b), Arrays.toString(jArr));
    }
}
