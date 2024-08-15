package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class B extends C {
    /* JADX INFO: Access modifiers changed from: package-private */
    public B(c cVar, int i) {
        super(cVar, i);
    }

    @Override // j$.util.stream.c
    final boolean k1() {
        return false;
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* bridge */ /* synthetic */ F parallel() {
        parallel();
        return this;
    }

    @Override // j$.util.stream.c, j$.util.stream.BaseStream, j$.util.stream.F
    public final /* bridge */ /* synthetic */ F sequential() {
        sequential();
        return this;
    }
}
