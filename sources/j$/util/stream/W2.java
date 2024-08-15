package j$.util.stream;
/* loaded from: classes2.dex */
final class W2 extends Z2 implements j$.util.function.m {
    final double[] c = new double[128];

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.Z2
    public final void a(Object obj, long j) {
        j$.util.function.m mVar = (j$.util.function.m) obj;
        for (int i = 0; i < j; i++) {
            mVar.accept(this.c[i]);
        }
    }

    @Override // j$.util.function.m
    public final void accept(double d) {
        int i = this.b;
        this.b = i + 1;
        this.c[i] = d;
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }
}
