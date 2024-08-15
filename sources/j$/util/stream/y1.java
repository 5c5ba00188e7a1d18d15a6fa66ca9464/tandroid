package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class y1 implements O1, c2 {
    private double a;
    final /* synthetic */ double b;
    final /* synthetic */ j$.util.function.i c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y1(double d, j$.util.function.i iVar) {
        this.b = d;
        this.c = iVar;
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        this.a = this.c.applyAsDouble(this.a, d);
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
        this.a = this.b;
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        return Double.valueOf(this.a);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.stream.O1
    public final void k(O1 o1) {
        accept(((y1) o1).a);
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
