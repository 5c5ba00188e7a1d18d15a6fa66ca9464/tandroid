package j$.util.stream;
/* loaded from: classes2.dex */
abstract class c1 extends e1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c1(c cVar, e4 e4Var, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean B0() {
        return true;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ LongStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ LongStream sequential() {
        sequential();
        return this;
    }
}
