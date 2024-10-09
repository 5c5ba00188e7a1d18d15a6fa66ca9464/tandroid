package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
public abstract class Z1 implements d2 {
    protected final e2 a;

    public Z1(e2 e2Var) {
        e2Var.getClass();
        this.a = e2Var;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        j((Long) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.function.W
    public final /* synthetic */ j$.util.function.W f(j$.util.function.W w) {
        return j$.com.android.tools.r8.a.d(this, w);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void j(Long l) {
        t0.i(this, l);
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
}
