package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class c0 extends d0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c0(c cVar, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean k1() {
        return false;
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
