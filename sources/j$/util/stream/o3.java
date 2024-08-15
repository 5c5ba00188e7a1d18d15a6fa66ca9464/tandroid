package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class o3 extends p3 implements j$.util.K {
    /* JADX INFO: Access modifiers changed from: package-private */
    public o3(j$.util.K k, long j, long j2) {
        super(k, j, j2);
    }

    o3(j$.util.K k, long j, long j2, long j3, long j4) {
        super(k, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }

    @Override // j$.util.stream.r3
    protected final j$.util.Q f(j$.util.Q q, long j, long j2, long j3, long j4) {
        return new o3((j$.util.K) q, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.stream.p3
    protected final Object g() {
        return new n3(0);
    }
}
