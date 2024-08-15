package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class a0 extends d0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public a0(j$.util.Q q, int i) {
        super(q, i);
    }

    @Override // j$.util.stream.d0, j$.util.stream.IntStream
    public final void O(j$.util.function.K k) {
        j$.util.H r1;
        if (isParallel()) {
            super.O(k);
            return;
        }
        r1 = d0.r1(n1());
        r1.c(k);
    }

    @Override // j$.util.stream.d0, j$.util.stream.IntStream
    public final void V(j$.util.function.K k) {
        j$.util.H r1;
        if (isParallel()) {
            super.V(k);
            return;
        }
        r1 = d0.r1(n1());
        r1.c(k);
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
    public final /* bridge */ /* synthetic */ IntStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* bridge */ /* synthetic */ IntStream sequential() {
        sequential();
        return this;
    }
}
