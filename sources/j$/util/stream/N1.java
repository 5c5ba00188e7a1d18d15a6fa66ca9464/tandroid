package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class N1 implements O1, e2 {
    private boolean a;
    private long b;
    final /* synthetic */ j$.util.function.d0 c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public N1(j$.util.function.d0 d0Var) {
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
        if (this.a) {
            this.a = false;
        } else {
            j = this.c.applyAsLong(this.b, j);
        }
        this.b = j;
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
        this.a = true;
        this.b = 0L;
    }

    @Override // j$.util.function.Supplier
    public final Object get() {
        return this.a ? j$.util.n.a() : j$.util.n.d(this.b);
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
        N1 n1 = (N1) o1;
        if (n1.a) {
            return;
        }
        accept(n1.b);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void l(Long l) {
        u0.n0(this, l);
    }
}
