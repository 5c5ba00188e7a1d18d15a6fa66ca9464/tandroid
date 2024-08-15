package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
public abstract class Z1 implements d2 {
    protected final f2 a;

    public Z1(f2 f2Var) {
        f2Var.getClass();
        this.a = f2Var;
    }

    @Override // j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final /* synthetic */ void accept(double d) {
        u0.i0();
        throw null;
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
    public void end() {
        this.a.end();
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void g(Integer num) {
        u0.l0(this, num);
    }

    @Override // j$.util.stream.f2
    public boolean h() {
        return this.a.h();
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }
}
