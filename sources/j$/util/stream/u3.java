package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class u3 extends v3 implements j$.util.K, j$.util.function.h0 {
    long e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u3(j$.util.K k, long j, long j2) {
        super(k, j, j2);
    }

    u3(j$.util.K k, u3 u3Var) {
        super(k, u3Var);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }

    @Override // j$.util.function.h0
    public final void accept(long j) {
        this.e = j;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }

    @Override // j$.util.stream.y3
    protected final j$.util.Q r(j$.util.Q q) {
        return new u3((j$.util.K) q, this);
    }

    @Override // j$.util.stream.v3
    protected final void t(Object obj) {
        ((j$.util.function.h0) obj).accept(this.e);
    }

    @Override // j$.util.stream.v3
    protected final Z2 u() {
        return new Y2();
    }
}
