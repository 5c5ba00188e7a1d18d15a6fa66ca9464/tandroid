package j$.util.stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class s implements j$.util.function.m {
    public final /* synthetic */ int a;
    public final /* synthetic */ f2 b;

    public /* synthetic */ s(int i, f2 f2Var) {
        this.a = i;
        this.b = f2Var;
    }

    @Override // j$.util.function.m
    public final void accept(double d) {
        int i = this.a;
        f2 f2Var = this.b;
        switch (i) {
            case 0:
                f2Var.accept(d);
                return;
            default:
                ((t) f2Var).a.accept(d);
                return;
        }
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        switch (this.a) {
            case 0:
                mVar.getClass();
                return new j$.util.function.j(this, mVar);
            default:
                mVar.getClass();
                return new j$.util.function.j(this, mVar);
        }
    }
}
