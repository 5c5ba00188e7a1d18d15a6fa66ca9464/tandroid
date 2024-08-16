package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class K1 extends t0 {
    final /* synthetic */ j$.util.function.S h;
    final /* synthetic */ long i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public K1(T2 t2, j$.util.function.S s, long j) {
        super(t2);
        this.h = s;
        this.i = j;
    }

    @Override // j$.util.stream.t0
    public final N1 d0() {
        return new L1(this.i, this.h);
    }
}
