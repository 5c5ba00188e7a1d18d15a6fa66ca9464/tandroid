package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class k3 extends l3 implements j$.util.K {
    /* JADX INFO: Access modifiers changed from: package-private */
    public k3(j$.util.K k, long j, long j2) {
        super(k, j, j2);
    }

    k3(j$.util.K k, long j, long j2, long j3, long j4) {
        super(k, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.stream.n3
    protected final j$.util.Q b(j$.util.Q q, long j, long j2, long j3, long j4) {
        return new k3((j$.util.K) q, j, j2, j3, j4);
    }

    @Override // j$.util.stream.l3
    protected final Object f() {
        return new C0(1);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }
}
