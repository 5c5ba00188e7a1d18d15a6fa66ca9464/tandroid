package j$.util.stream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Z extends b0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Z(j$.util.Q q, int i) {
        super(q, i);
    }

    @Override // j$.util.stream.b0, j$.util.stream.IntStream
    public final void C(j$.util.function.F f) {
        j$.util.H H0;
        if (isParallel()) {
            super.C(f);
        } else {
            H0 = b0.H0(B0());
            H0.e(f);
        }
    }

    @Override // j$.util.stream.b0, j$.util.stream.IntStream
    public final void L(j$.util.function.F f) {
        j$.util.H H0;
        if (isParallel()) {
            super.L(f);
        } else {
            H0 = b0.H0(B0());
            H0.e(f);
        }
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ IntStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ IntStream sequential() {
        sequential();
        return this;
    }

    @Override // j$.util.stream.b
    final boolean y0() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final e2 z0(int i, e2 e2Var) {
        throw new UnsupportedOperationException();
    }
}
