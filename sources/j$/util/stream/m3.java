package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class m3 extends p3 implements j$.util.H {
    /* JADX INFO: Access modifiers changed from: package-private */
    public m3(j$.util.H h, long j, long j2) {
        super(h, j, j2);
    }

    m3(j$.util.H h, long j, long j2, long j3, long j4) {
        super(h, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.p(this, consumer);
    }

    @Override // j$.util.stream.r3
    protected final j$.util.Q f(j$.util.Q q, long j, long j2, long j3, long j4) {
        return new m3((j$.util.H) q, j, j2, j3, j4);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.g(this, consumer);
    }

    @Override // j$.util.stream.p3
    protected final Object g() {
        return new l3(0);
    }
}
