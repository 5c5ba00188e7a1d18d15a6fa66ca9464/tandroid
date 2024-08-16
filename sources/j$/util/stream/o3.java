package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class o3 extends r3 implements j$.util.E, j$.util.function.n {
    double e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o3(j$.util.E e, long j, long j2) {
        super(e, j, j2);
    }

    o3(j$.util.E e, o3 o3Var) {
        super(e, o3Var);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }

    @Override // j$.util.function.n
    public final void accept(double d) {
        this.e = d;
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }

    @Override // j$.util.stream.u3
    protected final j$.util.Q u(j$.util.Q q) {
        return new o3((j$.util.E) q, this);
    }

    @Override // j$.util.stream.r3
    protected final void w(Object obj) {
        ((j$.util.function.n) obj).accept(this.e);
    }

    @Override // j$.util.stream.r3
    protected final Y2 x() {
        return new V2();
    }
}
