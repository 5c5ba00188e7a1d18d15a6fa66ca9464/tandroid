package j$.util.stream;
/* loaded from: classes2.dex */
abstract class b1 extends d1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public b1(c cVar, e4 e4Var, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean G0() {
        return true;
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
