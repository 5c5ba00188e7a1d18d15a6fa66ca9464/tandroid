package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class V0 extends Y0 implements B0 {
    V0() {
    }

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
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Integer[] numArr, int i) {
        t0.o(this, numArr, i);
    }

    @Override // j$.util.stream.E0
    public final Object e() {
        int[] iArr;
        iArr = t0.e;
        return iArr;
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.r(this, consumer);
    }

    @Override // j$.util.stream.F0
    public final j$.util.N spliterator() {
        return j$.util.f0.c();
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return j$.util.f0.c();
    }

    @Override // j$.util.stream.Y0, j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.u(this, j, j2);
    }
}
