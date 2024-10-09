package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
public abstract class Y1 implements c2 {
    protected final e2 a;

    public Y1(e2 e2Var) {
        e2Var.getClass();
        this.a = e2Var;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        t0.b();
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
        o((Integer) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    @Override // j$.util.stream.e2
    public void m() {
        this.a.m();
    }

    @Override // j$.util.stream.e2
    public void n(long j) {
        this.a.n(j);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void o(Integer num) {
        t0.g(this, num);
    }

    @Override // j$.util.stream.e2
    public boolean q() {
        return this.a.q();
    }
}
