package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class h0 extends j0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public h0(c cVar, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean k1() {
        return true;
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* bridge */ /* synthetic */ LongStream parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* bridge */ /* synthetic */ LongStream sequential() {
        sequential();
        return this;
    }
}
