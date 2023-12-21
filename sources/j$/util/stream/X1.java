package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class X1 extends a2 implements w1 {
    @Override // j$.util.stream.A1
    /* renamed from: a */
    public /* synthetic */ void i(Integer[] numArr, int i) {
        o1.i(this, numArr, i);
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    public z1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.z1
    public Object e() {
        int[] iArr;
        iArr = x2.e;
        return iArr;
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    /* renamed from: f */
    public /* synthetic */ w1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.o(this, j, j2, mVar);
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ void forEach(Consumer consumer) {
        o1.l(this, consumer);
    }

    @Override // j$.util.stream.A1
    public j$.util.u spliterator() {
        return j$.util.J.c();
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    public /* bridge */ /* synthetic */ A1 b(int i) {
        b(i);
        throw null;
    }

    @Override // j$.util.stream.A1
    public j$.util.t spliterator() {
        return j$.util.J.c();
    }
}
