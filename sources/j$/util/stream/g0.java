package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class g0 extends j0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public g0(j$.util.Q q, int i) {
        super(q, i);
    }

    @Override // j$.util.stream.j0, j$.util.stream.LongStream
    public final void E(j$.util.function.h0 h0Var) {
        j$.util.K r1;
        if (isParallel()) {
            super.E(h0Var);
            return;
        }
        r1 = j0.r1(n1());
        r1.b(h0Var);
    }

    @Override // j$.util.stream.c
    final boolean k1() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        throw new UnsupportedOperationException();
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* bridge */ /* synthetic */ LongStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* bridge */ /* synthetic */ LongStream sequential() {
        sequential();
        return this;
    }

    @Override // j$.util.stream.j0, j$.util.stream.LongStream
    public final void x(j$.util.function.h0 h0Var) {
        j$.util.K r1;
        if (isParallel()) {
            super.x(h0Var);
            return;
        }
        r1 = j0.r1(n1());
        r1.b(h0Var);
    }
}
