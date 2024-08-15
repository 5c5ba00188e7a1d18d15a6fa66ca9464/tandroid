package j$.util.stream;
/* loaded from: classes2.dex */
public final /* synthetic */ class e0 implements j$.util.function.h0 {
    public final /* synthetic */ int a;
    public final /* synthetic */ f2 b;

    public /* synthetic */ e0(int i, f2 f2Var) {
        this.a = i;
        this.b = f2Var;
    }

    @Override // j$.util.function.h0
    public final void accept(long j) {
        int i = this.a;
        f2 f2Var = this.b;
        switch (i) {
            case 0:
                f2Var.accept(j);
                return;
            default:
                ((f0) f2Var).a.accept(j);
                return;
        }
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        switch (this.a) {
            case 0:
                h0Var.getClass();
                return new j$.util.function.e0(this, h0Var);
            default:
                h0Var.getClass();
                return new j$.util.function.e0(this, h0Var);
        }
    }
}
