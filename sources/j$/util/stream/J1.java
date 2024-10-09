package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;

/* loaded from: classes2.dex */
final class J1 extends O1 implements N1, c2 {
    final /* synthetic */ Supplier b;
    final /* synthetic */ j$.util.function.l0 c;
    final /* synthetic */ j$.util.function.f d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public J1(Supplier supplier, j$.util.function.l0 l0Var, j$.util.function.f fVar) {
        this.b = supplier;
        this.c = l0Var;
        this.d = fVar;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final void accept(int i) {
        this.c.accept(this.a, i);
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

    @Override // j$.util.stream.N1
    public final void h(N1 n1) {
        this.a = this.d.apply(this.a, ((J1) n1).a);
    }

    @Override // j$.util.function.F
    public final /* synthetic */ j$.util.function.F l(j$.util.function.F f) {
        return j$.com.android.tools.r8.a.c(this, f);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void m() {
    }

    @Override // j$.util.stream.e2
    public final void n(long j) {
        this.a = this.b.get();
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void o(Integer num) {
        t0.g(this, num);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }
}
