package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class V0 extends X0 implements B0 {
    @Override // j$.util.stream.X0, j$.util.stream.D0
    public final C0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.X0, j$.util.stream.D0
    public final /* bridge */ /* synthetic */ D0 a(int i) {
        a(i);
        throw null;
    }

    @Override // j$.util.stream.C0
    public final Object b() {
        long[] jArr;
        jArr = u1.f;
        return jArr;
    }

    @Override // j$.util.stream.D0
    /* renamed from: f */
    public final /* synthetic */ void e(Long[] lArr, int i) {
        u0.u0(this, lArr, i);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ void forEach(Consumer consumer) {
        u0.x0(this, consumer);
    }

    @Override // j$.util.stream.X0, j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.A0(this, j, j2);
    }

    @Override // j$.util.stream.D0
    public final j$.util.N spliterator() {
        return j$.util.f0.d();
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return j$.util.f0.d();
    }
}
