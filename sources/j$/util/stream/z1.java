package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class z1 extends u1 {
    public final /* synthetic */ int h;
    final /* synthetic */ Object i;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ z1(U2 u2, Object obj, int i) {
        super(u2);
        this.h = i;
        this.i = obj;
    }

    @Override // j$.util.stream.u1
    public final O1 u() {
        int i = this.h;
        Object obj = this.i;
        switch (i) {
            case 0:
                return new A1((j$.util.function.i) obj);
            case 1:
                return new D1((j$.util.function.f) obj);
            case 2:
                return new J1((j$.util.function.G) obj);
            default:
                return new N1((j$.util.function.d0) obj);
        }
    }
}
