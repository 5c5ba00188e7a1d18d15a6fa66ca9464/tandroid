package j$.util.stream;
/* loaded from: classes2.dex */
abstract class a2 implements A1 {
    @Override // j$.util.stream.A1
    public A1 b(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.A1
    public long count() {
        return 0L;
    }

    public void d(Object obj, int i) {
    }

    public void g(Object obj) {
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.A1
    public Object[] q(j$.util.function.m mVar) {
        return (Object[]) mVar.apply(0);
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ A1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.q(this, j, j2, mVar);
    }
}
