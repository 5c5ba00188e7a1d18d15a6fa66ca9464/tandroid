package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class k1 extends M2 implements B0, x0 {
    @Override // j$.util.stream.C0, j$.util.stream.D0
    public final C0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.D0
    public final /* bridge */ /* synthetic */ D0 a(int i) {
        a(i);
        throw null;
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

    @Override // j$.util.stream.M2, j$.util.function.h0
    public final void accept(long j) {
        super.accept(j);
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        l((Long) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final Object b() {
        return (long[]) super.b();
    }

    @Override // j$.util.stream.x0, j$.util.stream.y0
    public final B0 build() {
        return this;
    }

    @Override // j$.util.stream.y0
    public final D0 build() {
        return this;
    }

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final void c(Object obj, int i) {
        super.c((long[]) obj, i);
    }

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final void d(Object obj) {
        super.d((j$.util.function.h0) obj);
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

    @Override // j$.util.stream.e2
    public final /* synthetic */ void l(Long l) {
        u0.n0(this, l);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ Object[] o(j$.util.function.N n) {
        return u0.r0(this, n);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.A0(this, j, j2);
    }

    @Override // j$.util.stream.M2, j$.util.stream.O2, java.lang.Iterable
    public final j$.util.N spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.M2, j$.util.stream.O2, java.lang.Iterable
    public final j$.util.Q spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.M2
    public final j$.util.K x() {
        return super.spliterator();
    }

    @Override // j$.util.stream.D0
    /* renamed from: y */
    public final /* synthetic */ void e(Long[] lArr, int i) {
        u0.u0(this, lArr, i);
    }
}
