package j$.util.stream;
/* loaded from: classes2.dex */
abstract class b2 implements B1 {
    @Override // j$.util.stream.B1
    public B1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.B1
    public long count() {
        return 0L;
    }

    public void d(Object obj, int i) {
    }

    public void g(Object obj) {
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.B1
    public Object[] q(j$.util.function.m mVar) {
        return (Object[]) mVar.apply(0);
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ B1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.q(this, j, j2, mVar);
    }
}
