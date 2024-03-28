package j$.util.stream;
/* loaded from: classes2.dex */
abstract class c1 extends e1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c1(c cVar, f4 f4Var, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean E0() {
        return true;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ f1 parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ f1 sequential() {
        sequential();
        return this;
    }
}
