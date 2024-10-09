package j$.util.stream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class f0 extends h0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public f0(j$.util.Q q, int i) {
        super(q, i);
    }

    @Override // j$.util.stream.h0, j$.util.stream.LongStream
    public final void V(j$.util.function.W w) {
        j$.util.K H0;
        if (isParallel()) {
            super.V(w);
        } else {
            H0 = h0.H0(B0());
            H0.e(w);
        }
    }

    @Override // j$.util.stream.h0, j$.util.stream.LongStream
    public final void d(j$.util.function.W w) {
        j$.util.K H0;
        if (isParallel()) {
            super.d(w);
        } else {
            H0 = h0.H0(B0());
            H0.e(w);
        }
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ LongStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.b, j$.util.stream.BaseStream
    public final /* bridge */ /* synthetic */ LongStream sequential() {
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
