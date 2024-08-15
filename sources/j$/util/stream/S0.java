package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class S0 extends I2 implements z0, v0 {
    @Override // j$.util.stream.C0, j$.util.stream.D0
    public final C0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.D0
    public final /* bridge */ /* synthetic */ D0 a(int i) {
        a(i);
        throw null;
    }

    @Override // j$.util.stream.I2, j$.util.function.m
    public final void accept(double d) {
        super.accept(d);
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

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final Object b() {
        return (double[]) super.b();
    }

    @Override // j$.util.stream.y0
    public final D0 build() {
        return this;
    }

    @Override // j$.util.stream.v0, j$.util.stream.y0
    public final z0 build() {
        return this;
    }

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final void c(Object obj, int i) {
        super.c((double[]) obj, i);
    }

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final void d(Object obj) {
        super.d((j$.util.function.m) obj);
    }

    @Override // j$.util.stream.f2
    public final void end() {
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        clear();
        u(j);
    }

    @Override // j$.util.stream.f2
    public final /* synthetic */ boolean h() {
        return false;
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ int j() {
        return 0;
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ Object[] o(j$.util.function.N n) {
        return u0.r0(this, n);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void p(Double d) {
        u0.j0(this, d);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.y0(this, j, j2);
    }

    @Override // j$.util.stream.I2, j$.util.stream.O2, java.lang.Iterable
    public final j$.util.N spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.I2, j$.util.stream.O2, java.lang.Iterable
    public final j$.util.Q spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.I2
    public final j$.util.E x() {
        return super.spliterator();
    }

    @Override // j$.util.stream.D0
    /* renamed from: y */
    public final /* synthetic */ void e(Double[] dArr, int i) {
        u0.s0(this, dArr, i);
    }
}
