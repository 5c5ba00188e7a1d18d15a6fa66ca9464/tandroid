package j$.util.stream;
/* loaded from: classes2.dex */
final class n0 extends q0 implements d2 {
    final /* synthetic */ r0 c;
    final /* synthetic */ j$.util.function.Q d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public n0(j$.util.function.Q q, r0 r0Var) {
        super(r0Var);
        this.c = r0Var;
        this.d = q;
    }

    @Override // j$.util.stream.q0, j$.util.stream.f2
    public final void accept(int i) {
        boolean z;
        boolean z2;
        if (this.a) {
            return;
        }
        boolean e = ((j$.util.function.O) this.d).e(i);
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
        g((Integer) obj);
    }

    @Override // j$.util.stream.d2
    public final /* synthetic */ void g(Integer num) {
        u0.l0(this, num);
    }

    @Override // j$.util.function.K
    public final j$.util.function.K n(j$.util.function.K k) {
        k.getClass();
        return new j$.util.function.H(this, k);
    }
}
