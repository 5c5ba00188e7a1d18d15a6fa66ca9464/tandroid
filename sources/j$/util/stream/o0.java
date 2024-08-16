package j$.util.stream;
/* loaded from: classes2.dex */
final class o0 extends p0 implements b2 {
    final /* synthetic */ q0 c;
    final /* synthetic */ j$.util.function.r d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o0(j$.util.function.r rVar, q0 q0Var) {
        super(q0Var);
        this.c = q0Var;
        this.d = rVar;
    }

    @Override // j$.util.stream.p0, j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean test = this.d.a.test(d);
        q0 q0Var = this.c;
        z = q0Var.a;
        if (test == z) {
            this.a = true;
            z2 = q0Var.b;
            this.b = z2;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        r((Double) obj);
    }

    @Override // j$.util.function.n
    public final /* synthetic */ j$.util.function.n k(j$.util.function.n nVar) {
        return j$.com.android.tools.r8.a.b(this, nVar);
    }

    @Override // j$.util.stream.b2
    public final /* synthetic */ void r(Double d) {
        t0.e(this, d);
    }
}
