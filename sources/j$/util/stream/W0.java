package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class W0 extends Y0 implements D0 {
    @Override // j$.util.stream.Y0, j$.util.stream.F0
    public final E0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.Y0, j$.util.stream.F0
    public final /* bridge */ /* synthetic */ F0 a(int i) {
        a(i);
        throw null;
    }

    @Override // j$.util.stream.F0
    /* renamed from: b */
    public final /* synthetic */ void i(Long[] lArr, int i) {
        t0.p(this, lArr, i);
    }

    @Override // j$.util.stream.E0
    public final Object e() {
        long[] jArr;
        jArr = t0.f;
        return jArr;
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.s(this, consumer);
    }

    @Override // j$.util.stream.F0
    public final j$.util.N spliterator() {
        return j$.util.f0.d();
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return j$.util.f0.d();
    }

    @Override // j$.util.stream.Y0, j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.v(this, j, j2);
    }
}
