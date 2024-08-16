package j$.util.stream;
/* loaded from: classes2.dex */
final class x extends z {
    public final /* synthetic */ int m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ x(b bVar, int i, int i2) {
        super(bVar, i, 1);
        this.m = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final e2 z0(int i, e2 e2Var) {
        switch (this.m) {
            case 0:
                return e2Var;
            case 1:
                return new V(1, e2Var);
            default:
                return new d0(e2Var);
        }
    }
}
