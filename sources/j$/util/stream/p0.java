package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class p0 implements O4, P4 {
    private final boolean a;

    /* JADX INFO: Access modifiers changed from: protected */
    public p0(boolean z) {
        this.a = z;
    }

    public /* synthetic */ void accept(double d) {
        p1.f(this);
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        p1.d(this);
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        p1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.O4
    public int b() {
        if (this.a) {
            return 0;
        }
        return e4.r;
    }

    @Override // j$.util.stream.O4
    public Object c(z2 z2Var, j$.util.t tVar) {
        (this.a ? new r0(z2Var, tVar, this) : new s0(z2Var, tVar, z2Var.t0(this))).invoke();
        return null;
    }

    @Override // j$.util.stream.O4
    public Object d(z2 z2Var, j$.util.t tVar) {
        c cVar = (c) z2Var;
        cVar.l0(cVar.t0(this), tVar);
        return null;
    }

    @Override // j$.util.function.Supplier
    public /* bridge */ /* synthetic */ Object get() {
        return null;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ boolean o() {
        return false;
    }
}
