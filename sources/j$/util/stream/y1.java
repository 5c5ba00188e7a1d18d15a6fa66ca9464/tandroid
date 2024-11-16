package j$.util.stream;

/* loaded from: classes2.dex */
final class y1 extends t0 {
    public final /* synthetic */ int h;
    final /* synthetic */ Object i;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ y1(T2 t2, Object obj, int i) {
        super(t2);
        this.h = i;
        this.i = obj;
    }

    @Override // j$.util.stream.t0
    public final N1 d0() {
        switch (this.h) {
            case 0:
                return new z1((j$.util.function.j) this.i);
            case 1:
                return new C1((j$.util.function.f) this.i);
            case 2:
                return new I1((j$.util.function.B) this.i);
            default:
                return new M1((j$.util.function.S) this.i);
        }
    }
}
