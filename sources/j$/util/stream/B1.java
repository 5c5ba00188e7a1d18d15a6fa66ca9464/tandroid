package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
final class B1 extends P1 implements O1, c2 {
    final /* synthetic */ Supplier b;
    final /* synthetic */ j$.util.function.z0 c;
    final /* synthetic */ j$.util.function.f d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public B1(Supplier supplier, j$.util.function.z0 z0Var, j$.util.function.f fVar) {
        this.b = supplier;
        this.c = z0Var;
        this.d = fVar;
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        this.c.accept(this.a, d);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(int i) {
        u0.p0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(long j) {
        u0.q0();
        throw null;
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        p((Double) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void end() {
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        this.a = this.b.get();
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.stream.O1
    public final void k(O1 o1) {
        this.a = this.d.apply(this.a, ((B1) o1).a);
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void p(Double d) {
        u0.j0(this, d);
    }
}
