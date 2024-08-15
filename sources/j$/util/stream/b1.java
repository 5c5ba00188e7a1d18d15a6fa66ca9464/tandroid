package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class b1 extends K2 implements A0, w0 {
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

    @Override // j$.util.stream.K2, j$.util.function.K
    public final void accept(int i) {
        super.accept(i);
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

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final Object b() {
        return (int[]) super.b();
    }

    @Override // j$.util.stream.w0, j$.util.stream.y0
    public final A0 build() {
        return this;
    }

    @Override // j$.util.stream.y0
    public final D0 build() {
        return this;
    }

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final void c(Object obj, int i) {
        super.c((int[]) obj, i);
    }

    @Override // j$.util.stream.O2, j$.util.stream.C0
    public final void d(Object obj) {
        super.d((j$.util.function.K) obj);
    }

    @Override // j$.util.stream.f2
    public final void end() {
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        clear();
        u(j);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void g(Integer num) {
        u0.l0(this, num);
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

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.z0(this, j, j2);
    }

    @Override // j$.util.stream.K2, j$.util.stream.O2, java.lang.Iterable
    public final j$.util.N spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.K2, j$.util.stream.O2, java.lang.Iterable
    public final j$.util.Q spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.K2
    public final j$.util.H x() {
        return super.spliterator();
    }

    @Override // j$.util.stream.D0
    /* renamed from: y */
    public final /* synthetic */ void e(Integer[] numArr, int i) {
        u0.t0(this, numArr, i);
    }
}
