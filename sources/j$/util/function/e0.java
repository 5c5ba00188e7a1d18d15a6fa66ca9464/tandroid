package j$.util.function;
/* loaded from: classes2.dex */
public final /* synthetic */ class e0 implements h0 {
    public final /* synthetic */ h0 a;
    public final /* synthetic */ h0 b;

    public /* synthetic */ e0(h0 h0Var, h0 h0Var2) {
        this.a = h0Var;
        this.b = h0Var2;
    }

    @Override // j$.util.function.h0
    public final void accept(long j) {
        this.a.accept(j);
        this.b.accept(j);
    }

    @Override // j$.util.function.h0
    public final h0 i(h0 h0Var) {
        h0Var.getClass();
        return new e0(this, h0Var);
    }
}
