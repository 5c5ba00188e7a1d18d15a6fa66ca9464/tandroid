package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class W1 extends a2 implements u1 {
    @Override // j$.util.stream.A1
    /* renamed from: a */
    public /* synthetic */ void i(Double[] dArr, int i) {
        o1.h(this, dArr, i);
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    public z1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.z1
    public Object e() {
        double[] dArr;
        dArr = x2.g;
        return dArr;
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    /* renamed from: f */
    public /* synthetic */ u1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.n(this, j, j2, mVar);
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ void forEach(Consumer consumer) {
        o1.k(this, consumer);
    }

    @Override // j$.util.stream.A1
    public j$.util.t spliterator() {
        return j$.util.I.b();
    }

    @Override // j$.util.stream.a2, j$.util.stream.A1
    public /* bridge */ /* synthetic */ A1 b(int i) {
        b(i);
        throw null;
    }

    @Override // j$.util.stream.A1
    public j$.util.s spliterator() {
        return j$.util.I.b();
    }
}
