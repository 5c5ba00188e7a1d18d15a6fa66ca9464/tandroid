package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
abstract class p0 implements N4, O4 {
    private final boolean a;

    /* JADX INFO: Access modifiers changed from: protected */
    public p0(boolean z) {
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
    public Object c(y2 y2Var, j$.util.s sVar) {
        (this.a ? new r0(y2Var, sVar, this) : new s0(y2Var, sVar, y2Var.q0(this))).invoke();
        return null;
    }

    @Override // j$.util.stream.N4
    public Object d(y2 y2Var, j$.util.s sVar) {
        c cVar = (c) y2Var;
        cVar.i0(cVar.q0(this), sVar);
        return null;
    }

    @Override // j$.util.function.Supplier
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
