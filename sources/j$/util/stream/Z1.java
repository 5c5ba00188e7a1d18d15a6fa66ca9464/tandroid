package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class Z1 extends b2 implements z1 {
    @Override // j$.util.stream.B1
    /* renamed from: a */
    public /* synthetic */ void i(Long[] lArr, int i) {
        p1.j(this, lArr, i);
    }

    @Override // j$.util.stream.b2, j$.util.stream.B1
    public A1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.A1
    public Object e() {
        long[] jArr;
        jArr = y2.f;
        return jArr;
    }

    @Override // j$.util.stream.b2, j$.util.stream.B1
    /* renamed from: f */
    public /* synthetic */ z1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.p(this, j, j2, mVar);
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ void forEach(Consumer consumer) {
        p1.m(this, consumer);
    }

    @Override // j$.util.stream.B1
    public j$.util.u spliterator() {
        return j$.util.J.d();
    }

    @Override // j$.util.stream.b2, j$.util.stream.B1
    public /* bridge */ /* synthetic */ B1 b(int i) {
        b(i);
        throw null;
    }

    @Override // j$.util.stream.B1
    public j$.util.t spliterator() {
        return j$.util.J.d();
    }
}
