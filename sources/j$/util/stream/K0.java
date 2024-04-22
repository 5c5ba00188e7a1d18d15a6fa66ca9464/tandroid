package j$.util.stream;
/* loaded from: classes2.dex */
abstract class K0 extends M0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public K0(c cVar, e4 e4Var, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean B0() {
        return true;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ IntStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ IntStream sequential() {
        sequential();
        return this;
    }
}
