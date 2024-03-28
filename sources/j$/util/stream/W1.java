package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class W1 extends V3 implements v1, q1 {
    @Override // j$.util.stream.V3
    public t.a B() {
        return super.spliterator();
    }

    @Override // j$.util.function.Consumer
    /* renamed from: C */
    public /* synthetic */ void accept(Double d) {
        p1.a(this, d);
    }

    @Override // j$.util.stream.B1
    /* renamed from: D */
    public /* synthetic */ void i(Double[] dArr, int i) {
        p1.h(this, dArr, i);
    }

    @Override // j$.util.stream.B1
    /* renamed from: E */
    public /* synthetic */ v1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.n(this, j, j2, mVar);
    }

    @Override // j$.util.stream.t1
    public B1 a() {
        return this;
    }

    @Override // j$.util.stream.q1, j$.util.stream.t1
    public v1 a() {
        return this;
    }

    @Override // j$.util.stream.V3, j$.util.function.f
    public void accept(double d) {
        super.accept(d);
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(int i) {
        p1.d(this);
        throw null;
    }

    @Override // j$.util.stream.n3, j$.util.stream.m3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        p1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.A1, j$.util.stream.B1
    public A1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.a4, j$.util.stream.A1
    public void d(Object obj, int i) {
        super.d((double[]) obj, i);
    }

    @Override // j$.util.stream.a4, j$.util.stream.A1
    public Object e() {
        return (double[]) super.e();
    }

    @Override // j$.util.stream.a4, j$.util.stream.A1
    public void g(Object obj) {
        super.g((j$.util.function.f) obj);
    }

    @Override // j$.util.stream.n3
    public void m() {
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        clear();
        x(j);
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ boolean o() {
        return false;
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ Object[] q(j$.util.function.m mVar) {
        return p1.g(this, mVar);
    }

    @Override // j$.util.stream.V3, j$.util.stream.a4, java.lang.Iterable, j$.lang.e
    public j$.util.u spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.B1
    public /* bridge */ /* synthetic */ B1 b(int i) {
        b(i);
        throw null;
    }

    @Override // j$.util.stream.V3, j$.util.stream.a4, java.lang.Iterable, j$.lang.e
    public j$.util.t spliterator() {
        return super.spliterator();
    }
}
