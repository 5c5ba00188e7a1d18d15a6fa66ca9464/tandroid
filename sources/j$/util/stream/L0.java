package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class L0 extends M0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public L0(c cVar, e4 e4Var, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean B0() {
        return false;
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
