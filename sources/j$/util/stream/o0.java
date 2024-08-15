package j$.util.stream;
/* loaded from: classes2.dex */
final class o0 extends q0 implements e2 {
    final /* synthetic */ r0 c;
    final /* synthetic */ j$.util.function.m0 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o0(j$.util.function.m0 m0Var, r0 r0Var) {
        super(r0Var);
        this.c = r0Var;
        this.d = m0Var;
    }

    @Override // j$.util.stream.q0, j$.util.stream.f2
    public final void accept(long j) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean e = ((j$.util.function.k0) this.d).e(j);
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
        l((Long) obj);
    }

    @Override // j$.util.function.h0
    public final j$.util.function.h0 i(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        return new j$.util.function.e0(this, h0Var);
    }

    @Override // j$.util.stream.e2
    public final /* synthetic */ void l(Long l) {
        u0.n0(this, l);
    }
}
