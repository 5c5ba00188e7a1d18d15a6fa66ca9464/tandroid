package j$.util.stream;
/* loaded from: classes2.dex */
final class p0 extends q0 implements c2 {
    final /* synthetic */ r0 c;
    final /* synthetic */ j$.util.function.s d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p0(j$.util.function.s sVar, r0 r0Var) {
        super(r0Var);
        this.c = r0Var;
        this.d = sVar;
    }

    @Override // j$.util.stream.q0, j$.util.stream.f2, j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean e = ((j$.util.function.q) this.d).e(d);
        r0 r0Var = this.c;
        z = r0Var.a;
        if (e == z) {
            this.a = true;
            z2 = r0Var.b;
            this.b = z2;
        }
    }

    @Override // j$.util.function.Consumer
    public final /* bridge */ /* synthetic */ void accept(Object obj) {
        p((Double) obj);
    }

    @Override // j$.util.function.m
    public final j$.util.function.m m(j$.util.function.m mVar) {
        mVar.getClass();
        return new j$.util.function.j(this, mVar);
    }

    @Override // j$.util.stream.c2
    public final /* synthetic */ void p(Double d) {
        u0.j0(this, d);
    }
}
