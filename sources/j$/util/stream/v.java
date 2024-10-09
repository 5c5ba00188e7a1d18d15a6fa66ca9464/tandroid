package j$.util.stream;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class v extends a0 {
    public final /* synthetic */ int m;
    final /* synthetic */ Object n;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ v(b bVar, int i, Object obj, int i2) {
        super(bVar, i, 1);
        this.m = i2;
        this.n = obj;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public v(b bVar, j$.util.function.F f) {
        super(bVar, 0, 1);
        this.m = 1;
        this.n = f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.b
    public final e2 z0(int i, e2 e2Var) {
        switch (this.m) {
            case 0:
                return new s(this, e2Var, 2);
            case 1:
                return new W(this, e2Var, 0);
            case 2:
                return new W(this, e2Var, 1);
            case 3:
                return new W(this, e2Var, 5);
            case 4:
                return new W(this, e2Var, 6);
            case 5:
                return new e0(this, e2Var, 2);
            case 6:
                return new o(this, e2Var, 4);
            default:
                return new R1(this, e2Var);
        }
    }
}
