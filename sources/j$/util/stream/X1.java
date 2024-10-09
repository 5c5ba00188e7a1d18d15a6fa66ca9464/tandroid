package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
public abstract class X1 implements b2 {
    protected final e2 a;

    public X1(e2 e2Var) {
        e2Var.getClass();
        this.a = e2Var;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(long j) {
        t0.l();
        throw null;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        r((Double) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    @Override // j$.util.stream.e2
    public void m() {
        this.a.m();
    }

    @Override // j$.util.stream.e2
    public void n(long j) {
        this.a.n(j);
    }

    @Override // j$.util.stream.e2
    public boolean q() {
        return this.a.q();
    }

    @Override // j$.util.stream.b2
    public final /* synthetic */ void r(Double d) {
        t0.e(this, d);
    }
}
