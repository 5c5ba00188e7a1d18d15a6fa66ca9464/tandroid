package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class M1 implements O1, e2 {
    private long a;
    final /* synthetic */ long b;
    final /* synthetic */ j$.util.function.d0 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public M1(long j, j$.util.function.d0 d0Var) {
        this.b = j;
        this.c = d0Var;
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final /* synthetic */ void accept(double d) {
        u0.i0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ void accept(int i) {
        u0.p0();
        throw null;
    }

    @Override // j$.util.stream.f2
    public final void accept(long j) {
        this.a = this.c.applyAsLong(this.a, j);
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        l((Long) obj);
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
        return Long.valueOf(this.a);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }

    @Override // j$.util.stream.O1
    public final void k(O1 o1) {
        accept(((M1) o1).a);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void l(Long l) {
        u0.n0(this, l);
    }
}
