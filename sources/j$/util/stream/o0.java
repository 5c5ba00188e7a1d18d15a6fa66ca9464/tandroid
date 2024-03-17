package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class o0 implements N4, O4 {
    private final boolean a;

    /* JADX INFO: Access modifiers changed from: protected */
    public o0(boolean z) {
        this.a = z;
    }

    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.N4
    public int b() {
        if (this.a) {
            return 0;
        }
        return d4.r;
    }

    @Override // j$.util.stream.N4
    public Object c(y2 y2Var, j$.util.t tVar) {
        (this.a ? new q0(y2Var, tVar, this) : new r0(y2Var, tVar, y2Var.v0(this))).invoke();
        return null;
    }

    @Override // j$.util.stream.N4
    public Object d(y2 y2Var, j$.util.t tVar) {
        c cVar = (c) y2Var;
        cVar.n0(cVar.v0(this), tVar);
        return null;
    }

    @Override // j$.util.function.y
    public /* bridge */ /* synthetic */ Object get() {
        return null;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void m() {
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void n(long j) {
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }
}
