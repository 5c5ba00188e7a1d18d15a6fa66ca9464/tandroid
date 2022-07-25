package j$.util.stream;
/* loaded from: classes2.dex */
abstract class Q extends T {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Q(c cVar, e4 e4Var, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean G0() {
        return true;
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: parallel */
    public /* bridge */ /* synthetic */ U mo332parallel() {
        mo332parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g, j$.util.stream.IntStream
    /* renamed from: sequential */
    public /* bridge */ /* synthetic */ U mo333sequential() {
        mo333sequential();
        return this;
    }
}
