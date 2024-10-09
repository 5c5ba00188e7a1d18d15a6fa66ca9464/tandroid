package j$.util.stream;

/* loaded from: classes2.dex */
final class w1 extends t0 {
    final /* synthetic */ j$.util.function.j h;
    final /* synthetic */ double i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public w1(T2 t2, j$.util.function.j jVar, double d) {
        super(t2);
        this.h = jVar;
        this.i = d;
    }

    @Override // j$.util.stream.t0
    public final N1 d0() {
        return new x1(this.i, this.h);
    }
}
