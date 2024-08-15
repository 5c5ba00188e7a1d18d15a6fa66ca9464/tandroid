package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class s3 extends v3 implements j$.util.E, j$.util.function.m {
    double e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s3(j$.util.E e, long j, long j2) {
        super(e, j, j2);
    }

    s3(j$.util.E e, s3 s3Var) {
        super(e, s3Var);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }

    @Override // j$.util.function.m
    public final void accept(double d) {
        this.e = d;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }

    @Override // j$.util.stream.y3
    protected final j$.util.Q r(j$.util.Q q) {
        return new s3((j$.util.E) q, this);
    }

    @Override // j$.util.stream.v3
    protected final void t(Object obj) {
        ((j$.util.function.m) obj).accept(this.e);
    }

    @Override // j$.util.stream.v3
    protected final Z2 u() {
        return new W2();
    }
}
