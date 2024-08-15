package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class H1 extends u1 {
    final /* synthetic */ j$.util.function.G h;
    final /* synthetic */ int i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public H1(U2 u2, j$.util.function.G g, int i) {
        super(u2);
        this.h = g;
        this.i = i;
    }

    @Override // j$.util.stream.u1
    public final O1 u() {
        return new I1(this.i, this.h);
    }
}
