package j$.util.stream;

/* loaded from: classes2.dex */
final class V2 extends Y2 implements j$.util.function.n {
    final double[] c = new double[128];

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.Y2
    public final void a(Object obj, long j) {
        j$.util.function.n nVar = (j$.util.function.n) obj;
        for (int i = 0; i < j; i++) {
            nVar.accept(this.c[i]);
        }
    }

    @Override // j$.util.function.n
    public final void accept(double d) {
        int i = this.b;
        this.b = i + 1;
        this.c[i] = d;
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }
}
