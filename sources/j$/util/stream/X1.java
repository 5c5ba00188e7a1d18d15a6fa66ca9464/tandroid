package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class X1 extends b2 implements v1 {
    @Override // j$.util.stream.B1
    /* renamed from: a */
    public /* synthetic */ void i(Double[] dArr, int i) {
        p1.h(this, dArr, i);
    }

    @Override // j$.util.stream.b2, j$.util.stream.B1
    public A1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.A1
    public Object e() {
        double[] dArr;
        dArr = y2.g;
        return dArr;
    }

    @Override // j$.util.stream.b2, j$.util.stream.B1
    /* renamed from: f */
    public /* synthetic */ v1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.n(this, j, j2, mVar);
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ void forEach(Consumer consumer) {
        p1.k(this, consumer);
    }

    @Override // j$.util.stream.B1
    public j$.util.u spliterator() {
        return j$.util.J.b();
    }

    @Override // j$.util.stream.b2, j$.util.stream.B1
    public /* bridge */ /* synthetic */ B1 b(int i) {
        b(i);
        throw null;
    }

    @Override // j$.util.stream.B1
    public j$.util.t spliterator() {
        return j$.util.J.b();
    }
}
