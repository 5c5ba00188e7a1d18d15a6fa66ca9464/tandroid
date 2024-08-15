package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class t3 extends v3 implements j$.util.H, j$.util.function.K {
    int e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t3(j$.util.H h, long j, long j2) {
        super(h, j, j2);
    }

    t3(j$.util.H h, t3 t3Var) {
        super(h, t3Var);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.p(this, consumer);
    }

    @Override // j$.util.function.K
    public final void accept(int i) {
        this.e = i;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.g(this, consumer);
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }

    @Override // j$.util.stream.y3
    protected final j$.util.Q r(j$.util.Q q) {
        return new t3((j$.util.H) q, this);
    }

    @Override // j$.util.stream.v3
    protected final void t(Object obj) {
        ((j$.util.function.K) obj).accept(this.e);
    }

    @Override // j$.util.stream.v3
    protected final Z2 u() {
        return new X2();
    }
}
