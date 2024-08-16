package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class l1 extends L2 implements D0, w0 {
    @Override // j$.util.stream.L2
    public final j$.util.K A() {
        return super.spliterator();
    }

    @Override // j$.util.stream.F0
    /* renamed from: B */
    public final /* synthetic */ void i(Long[] lArr, int i) {
        t0.p(this, lArr, i);
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

    @Override // j$.util.stream.e2
    public final /* synthetic */ void accept(int i) {
        t0.k();
        throw null;
    }

    @Override // j$.util.stream.L2, j$.util.function.W
    public final void accept(long j) {
        super.accept(j);
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        j((Long) obj);
    }

    @Override // j$.util.function.Consumer
    public final /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.w0, j$.util.stream.x0
    public final D0 b() {
        return this;
    }

    @Override // j$.util.stream.x0
    public final F0 b() {
        return this;
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final void d(Object obj, int i) {
        super.d((long[]) obj, i);
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final Object e() {
        return (long[]) super.e();
    }

    @Override // j$.util.stream.N2, j$.util.stream.E0
    public final void g(Object obj) {
        super.g((j$.util.function.W) obj);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void j(Long l) {
        t0.i(this, l);
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

    @Override // j$.util.stream.F0
    public final /* synthetic */ Object[] s(j$.util.function.I i) {
        return t0.m(this, i);
    }

    @Override // j$.util.stream.L2, j$.util.stream.N2, java.lang.Iterable
    public final j$.util.N spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.L2, j$.util.stream.N2, java.lang.Iterable
    public final j$.util.Q spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.v(this, j, j2);
    }
}
