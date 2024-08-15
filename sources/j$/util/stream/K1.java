package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
final class K1 extends P1 implements O1, d2 {
    final /* synthetic */ Supplier b;
    final /* synthetic */ j$.util.function.C0 c;
    final /* synthetic */ j$.util.function.f d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public K1(Supplier supplier, j$.util.function.C0 c0, j$.util.function.f fVar) {
        this.b = supplier;
        this.c = c0;
        this.d = fVar;
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final /* synthetic */ void accept(double d) {
        u0.i0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public final void accept(int i) {
        this.c.accept(this.a, i);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(long j) {
        u0.q0();
        throw null;
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        g((Integer) obj);
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

    @Override // j$.util.stream.d2
    public final /* synthetic */ void g(Integer num) {
        u0.l0(this, num);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.stream.O1
    public final void k(O1 o1) {
        this.a = this.d.apply(this.a, ((K1) o1).a);
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }
}
