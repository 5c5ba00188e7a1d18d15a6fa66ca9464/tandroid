package j$.util.function;

/* loaded from: classes2.dex */
public final /* synthetic */ class c0 implements f0 {
    public final /* synthetic */ int a;
    public final /* synthetic */ f0 b;
    public final /* synthetic */ f0 c;

    public /* synthetic */ c0(f0 f0Var, f0 f0Var2, int i) {
        this.a = i;
        this.b = f0Var;
        this.c = f0Var2;
    }

    @Override // j$.util.function.f0
    public final f0 a(f0 f0Var) {
        switch (this.a) {
            case 0:
                f0Var.getClass();
                break;
            default:
                f0Var.getClass();
                break;
        }
        return new c0(this, f0Var, 0);
    }

    @Override // j$.util.function.f0
    public final long applyAsLong(long j) {
        switch (this.a) {
            case 0:
                return this.c.applyAsLong(this.b.applyAsLong(j));
            default:
                return this.b.applyAsLong(this.c.applyAsLong(j));
        }
    }

    @Override // j$.util.function.f0
    public final f0 c(f0 f0Var) {
        switch (this.a) {
            case 0:
                f0Var.getClass();
                break;
            default:
                f0Var.getClass();
                break;
        }
        return new c0(this, f0Var, 1);
    }
}
