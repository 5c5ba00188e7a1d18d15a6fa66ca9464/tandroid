package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class j1 implements D0 {
    final long[] a;
    int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j1(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.a = new long[(int) j];
        this.b = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j1(long[] jArr) {
        this.a = jArr;
        this.b = jArr.length;
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
        System.arraycopy(this.a, 0, (long[]) obj, i, i2);
    }

    @Override // j$.util.stream.E0
    public final Object e() {
        long[] jArr = this.a;
        int length = jArr.length;
        int i = this.b;
        return length == i ? jArr : Arrays.copyOf(jArr, i);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.s(this, consumer);
    }

    @Override // j$.util.stream.E0
    public final void g(Object obj) {
        j$.util.function.W w = (j$.util.function.W) obj;
        for (int i = 0; i < this.b; i++) {
            w.accept(this.a[i]);
        }
    }

    @Override // j$.util.stream.F0
    /* renamed from: k, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Long[] lArr, int i) {
        t0.p(this, lArr, i);
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
        return j$.util.f0.l(this.a, 0, this.b);
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return j$.util.f0.l(this.a, 0, this.b);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.v(this, j, j2);
    }

    public String toString() {
        long[] jArr = this.a;
        return String.format("LongArrayNode[%d][%s]", Integer.valueOf(jArr.length - this.b), Arrays.toString(jArr));
    }
}
