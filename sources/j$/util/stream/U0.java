package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class U0 extends Y0 implements z0 {
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
    public final /* synthetic */ void i(Double[] dArr, int i) {
        t0.n(this, dArr, i);
    }

    @Override // j$.util.stream.E0
    public final Object e() {
        double[] dArr;
        dArr = t0.g;
        return dArr;
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.q(this, consumer);
    }

    @Override // j$.util.stream.F0
    public final j$.util.N spliterator() {
        return j$.util.f0.b();
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return j$.util.f0.b();
    }

    @Override // j$.util.stream.Y0, j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.t(this, j, j2);
    }
}
