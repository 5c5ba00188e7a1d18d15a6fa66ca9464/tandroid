package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class t0 implements w0 {
    public final /* synthetic */ int a;
    public final /* synthetic */ w0 b;
    public final /* synthetic */ w0 c;

    public /* synthetic */ t0(w0 w0Var, w0 w0Var2, int i) {
        this.a = i;
        this.b = w0Var;
        this.c = w0Var2;
    }

    @Override // j$.util.function.w0
    public final w0 a(w0 w0Var) {
        switch (this.a) {
            case 0:
                w0Var.getClass();
                return new t0(this, w0Var, 1);
            default:
                w0Var.getClass();
                return new t0(this, w0Var, 1);
        }
    }

    @Override // j$.util.function.w0
    public final long applyAsLong(long j) {
        int i = this.a;
        w0 w0Var = this.c;
        w0 w0Var2 = this.b;
        switch (i) {
            case 0:
                return w0Var.applyAsLong(w0Var2.applyAsLong(j));
            default:
                return w0Var2.applyAsLong(w0Var.applyAsLong(j));
        }
    }

    @Override // j$.util.function.w0
    public final w0 b(w0 w0Var) {
        switch (this.a) {
            case 0:
                w0Var.getClass();
                return new t0(this, w0Var, 0);
            default:
                w0Var.getClass();
                return new t0(this, w0Var, 0);
        }
    }
}
