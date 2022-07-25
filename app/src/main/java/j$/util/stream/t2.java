package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class t2 extends a4 implements A1, s1 {
    @Override // j$.util.stream.s1
    /* renamed from: a */
    public A1 mo315a() {
        return this;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(double d) {
        o1.f(this);
        throw null;
    }

    @Override // j$.util.stream.m3
    public /* synthetic */ void accept(int i) {
        o1.d(this);
        throw null;
    }

    @Override // j$.util.stream.m3, j$.util.stream.l3, j$.util.function.q
    public /* synthetic */ void accept(long j) {
        o1.e(this);
        throw null;
    }

    @Override // j$.util.stream.a4, j$.util.function.Consumer
    public void accept(Object obj) {
        super.accept(obj);
    }

    @Override // j$.util.stream.A1
    /* renamed from: b */
    public A1 mo316b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.a4, j$.lang.e
    public void forEach(Consumer consumer) {
        super.forEach(consumer);
    }

    @Override // j$.util.stream.a4, j$.util.stream.A1
    public void i(Object[] objArr, int i) {
        super.i(objArr, i);
    }

    @Override // j$.util.stream.m3
    public void m() {
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        clear();
        u(j);
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
    public Object[] q(j$.util.function.m mVar) {
        long count = count();
        if (count < 2147483639) {
            Object[] objArr = (Object[]) mVar.apply((int) count);
            i(objArr, 0);
            return objArr;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ A1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.q(this, j, j2, mVar);
    }

    @Override // j$.util.stream.a4, java.lang.Iterable, j$.lang.e
    /* renamed from: spliterator */
    public j$.util.u mo317spliterator() {
        return super.mo317spliterator();
    }
}
