package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class k3 extends p3 implements j$.util.E {
    /* JADX INFO: Access modifiers changed from: package-private */
    public k3(j$.util.E e, long j, long j2) {
        super(e, j, j2);
    }

    k3(j$.util.E e, long j, long j2, long j3, long j4) {
        super(e, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }

    @Override // j$.util.stream.r3
    protected final j$.util.Q f(j$.util.Q q, long j, long j2, long j3, long j4) {
        return new k3((j$.util.E) q, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.stream.p3
    protected final Object g() {
        return new j3(0);
    }
}
