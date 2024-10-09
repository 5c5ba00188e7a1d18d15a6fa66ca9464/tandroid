package j$.util.stream;

import j$.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class c1 extends J2 implements B0, v0 {
    @Override // j$.util.stream.J2
    /* renamed from: A */
    public final j$.util.H spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.F0
    /* renamed from: B, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Integer[] numArr, int i) {
        t0.o(this, numArr, i);
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

    @Override // j$.util.stream.e2, j$.util.function.n
    public final /* synthetic */ void accept(double d) {
        t0.b();
        throw null;
    }

    @Override // j$.util.stream.J2, j$.util.function.F
    public final void accept(int i) {
        super.accept(i);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(long j) {
        t0.l();
        throw null;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final /* bridge */ /* synthetic */ void r(Object obj) {
        o((Integer) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.v0, j$.util.stream.x0
    public final B0 b() {
        return this;
    }

    @Override // j$.util.stream.x0
    public final F0 b() {
        return this;
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final void d(Object obj, int i) {
        super.d((int[]) obj, i);
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final Object e() {
        return (int[]) super.e();
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final void g(Object obj) {
        super.g((j$.util.function.F) obj);
    }

    @Override // j$.util.stream.e2
    public final void m() {
    }

    @Override // j$.util.stream.e2
    public final void n(long j) {
        clear();
        x(j);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void o(Integer num) {
        t0.g(this, num);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ boolean q() {
        return false;
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ Object[] s(j$.util.function.I i) {
        return t0.m(this, i);
    }

    @Override // j$.util.stream.J2, j$.util.stream.N2, java.lang.Iterable
    public final j$.util.N spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.J2, j$.util.stream.N2, java.lang.Iterable
    public final j$.util.Q spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.u(this, j, j2);
    }
}
