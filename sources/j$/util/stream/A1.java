package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
final class A1 extends O1 implements N1, b2 {
    final /* synthetic */ Supplier b;
    final /* synthetic */ j$.util.function.i0 c;
    final /* synthetic */ j$.util.function.f d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public A1(Supplier supplier, j$.util.function.i0 i0Var, j$.util.function.f fVar) {
        this.b = supplier;
        this.c = i0Var;
        this.d = fVar;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        this.c.accept(this.a, d);
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
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        r((Double) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.N1
    public final void h(N1 n1) {
        this.a = this.d.apply(this.a, ((A1) n1).a);
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
        this.a = this.b.get();
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
