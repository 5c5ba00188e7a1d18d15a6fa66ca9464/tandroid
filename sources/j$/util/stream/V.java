package j$.util.stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class V implements j$.util.function.K {
    public final /* synthetic */ int a;
    public final /* synthetic */ f2 b;

    public /* synthetic */ V(int i, f2 f2Var) {
        this.a = i;
        this.b = f2Var;
    }

    @Override // j$.util.function.K
    public final void accept(int i) {
        int i2 = this.a;
        f2 f2Var = this.b;
        switch (i2) {
            case 0:
                f2Var.accept(i);
                return;
            default:
                ((X) f2Var).a.accept(i);
                return;
        }
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        switch (this.a) {
            case 0:
                k.getClass();
                return new j$.util.function.H(this, k);
            default:
                k.getClass();
                return new j$.util.function.H(this, k);
        }
    }
}
