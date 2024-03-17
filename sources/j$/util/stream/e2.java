package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class e2 extends W3 implements w1, q1 {
    @Override // j$.util.stream.W3
    public t.b B() {
        return super.spliterator();
    }

    @Override // j$.util.function.Consumer
    /* renamed from: C */
    public /* synthetic */ void accept(Integer num) {
        o1.b(this, num);
    }

    @Override // j$.util.stream.A1
    /* renamed from: D */
    public /* synthetic */ void i(Integer[] numArr, int i) {
        o1.i(this, numArr, i);
    }

    @Override // j$.util.stream.A1
    /* renamed from: E */
    public /* synthetic */ w1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.o(this, j, j2, mVar);
    }

    @Override // j$.util.stream.s1
    public A1 a() {
        return this;
    }

    @Override // j$.util.stream.q1, j$.util.stream.s1
    public w1 a() {
        return this;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    @Override // j$.util.stream.W3, j$.util.function.l
    public void accept(int i) {
        super.accept(i);
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.function.Consumer
    public /* synthetic */ Consumer andThen(Consumer consumer) {
        return Consumer.-CC.$default$andThen(this, consumer);
    }

    @Override // j$.util.stream.z1, j$.util.stream.A1
    public z1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.Z3, j$.util.stream.z1
    public void d(Object obj, int i) {
        super.d((int[]) obj, i);
    }

    @Override // j$.util.stream.Z3, j$.util.stream.z1
    public Object e() {
        return (int[]) super.e();
    }

    @Override // j$.util.stream.Z3, j$.util.stream.z1
    public void g(Object obj) {
        super.g((j$.util.function.l) obj);
    }

    @Override // j$.util.stream.m3
    public void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        clear();
        x(j);
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ boolean o() {
        return false;
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ Object[] q(j$.util.function.m mVar) {
        return o1.g(this, mVar);
    }

    @Override // j$.util.stream.W3, j$.util.stream.Z3, java.lang.Iterable, j$.lang.e
    public j$.util.u spliterator() {
        return super.spliterator();
    }

    @Override // j$.util.stream.A1
    public /* bridge */ /* synthetic */ A1 b(int i) {
        b(i);
        throw null;
    }

    @Override // j$.util.stream.W3, j$.util.stream.Z3, java.lang.Iterable, j$.lang.e
    public j$.util.t spliterator() {
        return super.spliterator();
    }
}
