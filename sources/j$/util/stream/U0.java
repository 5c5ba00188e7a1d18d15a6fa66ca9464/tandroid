package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class U0 extends X0 implements A0 {
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
        int[] iArr;
        iArr = u1.e;
        return iArr;
    }

    @Override // j$.util.stream.D0
    /* renamed from: f */
    public final /* synthetic */ void e(Integer[] numArr, int i) {
        u0.t0(this, numArr, i);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ void forEach(Consumer consumer) {
        u0.w0(this, consumer);
    }

    @Override // j$.util.stream.X0, j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.z0(this, j, j2);
    }

    @Override // j$.util.stream.D0
    public final j$.util.N spliterator() {
        return j$.util.f0.c();
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return j$.util.f0.c();
    }
}
