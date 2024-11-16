package j$.util.stream;

/* loaded from: classes2.dex */
final class X extends g0 {
    public final /* synthetic */ int m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ X(b bVar, int i, int i2) {
        super(bVar, i, 1);
        this.m = i2;
    }

    @Override // j$.util.stream.b
    final e2 z0(int i, e2 e2Var) {
        switch (this.m) {
            case 0:
                return new V(0, e2Var);
            default:
                return e2Var;
        }
    }
}
