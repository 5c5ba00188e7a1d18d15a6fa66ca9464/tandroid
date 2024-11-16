package j$.util.stream;

import j$.util.function.Consumer;

/* loaded from: classes2.dex */
final class T0 extends H2 implements z0, u0 {
    T0() {
    }

    @Override // j$.util.stream.H2
    /* renamed from: A */
    public final j$.util.E spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.F0
    /* renamed from: B, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Double[] dArr, int i) {
        t0.n(this, dArr, i);
    }

    @Override // j$.util.stream.E0, j$.util.stream.F0
    public final E0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.F0
    public final /* bridge */ /* synthetic */ F0 a(int i) {
        a(i);
        throw null;
    }

    @Override // j$.util.stream.H2, j$.util.function.n
    public final void accept(double d) {
        super.accept(d);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(long j) {
        t0.l();
        throw null;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        r((Double) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.x0
    public final F0 b() {
        return this;
    }

    @Override // j$.util.stream.u0, j$.util.stream.x0
    public final z0 b() {
        return this;
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final void d(Object obj, int i) {
        super.d((double[]) obj, i);
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final Object e() {
        return (double[]) super.e();
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final void g(Object obj) {
        super.g((j$.util.function.n) obj);
    }

    @Override // j$.util.stream.e2
    public final void m() {
    }

    @Override // j$.util.stream.e2
    public final void n(long j) {
        clear();
        x(j);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }

    @Override // j$.util.stream.b2
    public final /* synthetic */ void r(Double d) {
        t0.e(this, d);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ Object[] s(j$.util.function.I i) {
        return t0.m(this, i);
    }

    @Override // j$.util.stream.H2, j$.util.stream.N2, java.lang.Iterable
    public final j$.util.N spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.H2, j$.util.stream.N2, java.lang.Iterable
    public final j$.util.Q spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.t(this, j, j2);
    }
}
