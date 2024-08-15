package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class z extends C {
    /* JADX INFO: Access modifiers changed from: package-private */
    public z(j$.util.Q q, int i) {
        super(q, i);
    }

    @Override // j$.util.stream.C, j$.util.stream.F
    public final void H(j$.util.function.m mVar) {
        j$.util.E r1;
        if (isParallel()) {
            super.H(mVar);
            return;
        }
        r1 = C.r1(n1());
        r1.d(mVar);
    }

    @Override // j$.util.stream.C, j$.util.stream.F
    public final void g0(j$.util.function.m mVar) {
        j$.util.E r1;
        if (isParallel()) {
            super.g0(mVar);
            return;
        }
        r1 = C.r1(n1());
        r1.d(mVar);
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
    public final /* bridge */ /* synthetic */ F parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* bridge */ /* synthetic */ F sequential() {
        sequential();
        return this;
    }
}
