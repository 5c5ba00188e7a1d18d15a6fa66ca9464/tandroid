package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class Z0 implements A0 {
    final int[] a;
    int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Z0(long j) {
        if (j >= 2147483639) {
            throw new IllegalArgumentException("Stream size exceeds max array size");
        }
        this.a = new int[(int) j];
        this.b = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Z0(int[] iArr) {
        this.a = iArr;
        this.b = iArr.length;
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
        int[] iArr = this.a;
        int length = iArr.length;
        int i = this.b;
        return length == i ? iArr : Arrays.copyOf(iArr, i);
    }

    @Override // j$.util.stream.C0
    public final void c(Object obj, int i) {
        int i2 = this.b;
        System.arraycopy(this.a, 0, (int[]) obj, i, i2);
    }

    @Override // j$.util.stream.D0
    public final long count() {
        return this.b;
    }

    @Override // j$.util.stream.C0
    public final void d(Object obj) {
        j$.util.function.K k = (j$.util.function.K) obj;
        for (int i = 0; i < this.b; i++) {
            k.accept(this.a[i]);
        }
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ void forEach(Consumer consumer) {
        u0.w0(this, consumer);
    }

    @Override // j$.util.stream.D0
    /* renamed from: i */
    public final /* synthetic */ void e(Integer[] numArr, int i) {
        u0.t0(this, numArr, i);
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
        return u0.z0(this, j, j2);
    }

    @Override // j$.util.stream.C0, j$.util.stream.D0
    public final j$.util.N spliterator() {
        return j$.util.f0.k(this.a, 0, this.b);
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return j$.util.f0.k(this.a, 0, this.b);
    }

    public String toString() {
        int[] iArr = this.a;
        return String.format("IntArrayNode[%d][%s]", Integer.valueOf(iArr.length - this.b), Arrays.toString(iArr));
    }
}
