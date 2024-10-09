package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class a1 implements B0 {
    final int[] a;
    int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a1(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.a = new int[(int) j];
        this.b = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a1(int[] iArr) {
        this.a = iArr;
        this.b = iArr.length;
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
        System.arraycopy(this.a, 0, (int[]) obj, i, i2);
    }

    @Override // j$.util.stream.E0
    public final Object e() {
        int[] iArr = this.a;
        int length = iArr.length;
        int i = this.b;
        return length == i ? iArr : Arrays.copyOf(iArr, i);
    }

    @Override // j$.util.stream.F0
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Integer[] numArr, int i) {
        t0.o(this, numArr, i);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.r(this, consumer);
    }

    @Override // j$.util.stream.E0
    public final void g(Object obj) {
        j$.util.function.F f = (j$.util.function.F) obj;
        for (int i = 0; i < this.b; i++) {
            f.accept(this.a[i]);
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
        return j$.util.f0.k(this.a, 0, this.b);
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return j$.util.f0.k(this.a, 0, this.b);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.u(this, j, j2);
    }

    public String toString() {
        int[] iArr = this.a;
        return String.format("IntArrayNode[%d][%s]", Integer.valueOf(iArr.length - this.b), Arrays.toString(iArr));
    }
}
