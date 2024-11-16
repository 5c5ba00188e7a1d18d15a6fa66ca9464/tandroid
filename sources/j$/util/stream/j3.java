package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class j3 extends l3 implements j$.util.H {
    j3(j$.util.H h, long j, long j2) {
        super(h, j, j2);
    }

    j3(j$.util.H h, long j, long j2, long j3, long j4) {
        super(h, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.stream.n3
    protected final j$.util.Q b(j$.util.Q q, long j, long j2, long j3, long j4) {
        return new j3((j$.util.H) q, j, j2, j3, j4);
    }

    @Override // j$.util.stream.l3
    protected final Object f() {
        return new A0(1);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.o(this, consumer);
    }
}
