package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class x1 implements N1, b2 {
    private double a;
    final /* synthetic */ double b;
    final /* synthetic */ j$.util.function.j c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x1(double d, j$.util.function.j jVar) {
        this.b = d;
        this.c = jVar;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        this.a = this.c.applyAsDouble(this.a, d);
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

    @Override // j$.util.function.Supplier
    public final Object get() {
        return Double.valueOf(this.a);
    }

    @Override // j$.util.stream.N1
    public final void h(N1 n1) {
        accept(((x1) n1).a);
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
    }

    @Override // j$.util.stream.e2
    public final void n(long j) {
        this.a = this.b;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }

    @Override // j$.util.stream.b2
    public final /* synthetic */ void r(Double d) {
        t0.e(this, d);
    }
}
