package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class u2 extends b4 implements B1, t1 {
    @Override // j$.util.stream.t1
    public B1 a() {
        return this;
    }

    @Override // j$.util.stream.n3
    public /* synthetic */ void accept(double d) {
        p1.f(this);
        throw null;
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

    @Override // j$.util.stream.b4, j$.util.function.Consumer
    public void accept(Object obj) {
        super.accept(obj);
    }

    @Override // j$.util.stream.B1
    public B1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.b4, j$.lang.e
    public void forEach(Consumer consumer) {
        super.forEach(consumer);
    }

    @Override // j$.util.stream.b4, j$.util.stream.B1
    public void i(Object[] objArr, int i) {
        super.i(objArr, i);
    }

    @Override // j$.util.stream.n3
    public void m() {
    }

    @Override // j$.util.stream.n3
    public void n(long j) {
        clear();
        u(j);
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
    public Object[] q(j$.util.function.m mVar) {
        long count = count();
        if (count < 2147483639) {
            Object[] objArr = (Object[]) mVar.apply((int) count);
            i(objArr, 0);
            return objArr;
        }
        throw new IllegalArgumentException("Stream size exceeds max array size");
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ B1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.q(this, j, j2, mVar);
    }

    @Override // j$.util.stream.b4, java.lang.Iterable, j$.lang.e
    public j$.util.t spliterator() {
        return super.spliterator();
    }
}
