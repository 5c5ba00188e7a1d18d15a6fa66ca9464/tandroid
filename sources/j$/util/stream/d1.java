package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class d1 extends e1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public d1(c cVar, f4 f4Var, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean E0() {
        return false;
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
