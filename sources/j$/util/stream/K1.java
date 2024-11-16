package j$.util.stream;

/* loaded from: classes2.dex */
final class K1 extends t0 {
    final /* synthetic */ j$.util.function.S h;
    final /* synthetic */ long i;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    K1(T2 t2, j$.util.function.S s, long j) {
        super(t2);
        this.h = s;
        this.i = j;
    }

    @Override // j$.util.stream.t0
    public final N1 d0() {
        return new L1(this.i, this.h);
    }
}
