package j$.util.stream;

/* loaded from: classes2.dex */
final class y extends A {
    y(j$.util.Q q, int i) {
        super(q, i);
    }

    @Override // j$.util.stream.A, j$.util.stream.D
    public final void f0(j$.util.function.n nVar) {
        j$.util.E H0;
        if (isParallel()) {
            super.f0(nVar);
        } else {
            H0 = A.H0(B0());
            H0.e(nVar);
        }
    }

    @Override // j$.util.stream.A, j$.util.stream.D
    public final void j(j$.util.function.n nVar) {
        j$.util.E H0;
        if (isParallel()) {
            super.j(nVar);
        } else {
            H0 = A.H0(B0());
            H0.e(nVar);
        }
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ D parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ D sequential() {
        sequential();
        return this;
    }

    @Override // j$.util.stream.b
    final boolean y0() {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.b
    final e2 z0(int i, e2 e2Var) {
        throw new UnsupportedOperationException();
    }
}
