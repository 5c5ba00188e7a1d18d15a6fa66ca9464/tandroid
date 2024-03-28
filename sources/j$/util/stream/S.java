package j$.util.stream;
/* loaded from: classes2.dex */
abstract class S extends U {
    /* JADX INFO: Access modifiers changed from: package-private */
    public S(c cVar, f4 f4Var, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean E0() {
        return true;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ V parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.g
    public /* bridge */ /* synthetic */ V sequential() {
        sequential();
        return this;
    }
}
