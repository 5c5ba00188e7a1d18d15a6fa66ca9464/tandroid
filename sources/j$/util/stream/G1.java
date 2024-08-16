package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class G1 extends t0 {
    final /* synthetic */ j$.util.function.B h;
    final /* synthetic */ int i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public G1(T2 t2, j$.util.function.B b, int i) {
        super(t2);
        this.h = b;
        this.i = i;
    }

    @Override // j$.util.stream.t0
    public final N1 d0() {
        return new H1(this.i, this.h);
    }
}
