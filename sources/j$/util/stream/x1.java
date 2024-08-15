package j$.util.stream;
/* loaded from: classes2.dex */
final class x1 extends u1 {
    final /* synthetic */ j$.util.function.i h;
    final /* synthetic */ double i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x1(U2 u2, j$.util.function.i iVar, double d) {
        super(u2);
        this.h = iVar;
        this.i = d;
    }

    @Override // j$.util.stream.u1
    public final O1 u() {
        return new y1(this.i, this.h);
    }
}
