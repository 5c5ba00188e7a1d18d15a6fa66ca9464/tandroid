package j$.util.stream;

/* loaded from: classes2.dex */
abstract class Y0 implements F0 {
    Y0() {
    }

    @Override // j$.util.stream.F0
    public F0 a(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override // j$.util.stream.F0
    public final long count() {
        return 0L;
    }

    public final void d(Object obj, int i) {
    }

    public final void g(Object obj) {
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ int p() {
        return 0;
    }

    @Override // j$.util.stream.F0
    public final Object[] s(j$.util.function.I i) {
        return (Object[]) i.apply(0);
    }

    @Override // j$.util.stream.F0
    public /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.w(this, j, j2, i);
    }
}
