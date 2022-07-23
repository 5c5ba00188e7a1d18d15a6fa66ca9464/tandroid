package j$.util.stream;
/* loaded from: classes2.dex */
public class a1 extends d1 {
    public a1(j$.util.u uVar, int i, boolean z) {
        super(uVar, i, z);
    }

    @Override // j$.util.stream.c
    final boolean G0() {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.c
    public final m3 H0(int i, m3 m3Var) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.d1, j$.util.stream.e1
    public void Z(j$.util.function.q qVar) {
        j$.util.v M0;
        if (!isParallel()) {
            M0 = d1.M0(J0());
            M0.d(qVar);
            return;
        }
        qVar.getClass();
        x0(new m0(qVar, true));
    }

    @Override // j$.util.stream.d1, j$.util.stream.e1
    public void d(j$.util.function.q qVar) {
        j$.util.v M0;
        if (isParallel()) {
            super.d(qVar);
            return;
        }
        M0 = d1.M0(J0());
        M0.d(qVar);
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    public /* bridge */ /* synthetic */ e1 parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    public /* bridge */ /* synthetic */ e1 sequential() {
        sequential();
        return this;
    }
}
