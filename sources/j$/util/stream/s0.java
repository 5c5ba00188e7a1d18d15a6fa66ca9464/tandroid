package j$.util.stream;

import j$.util.function.Supplier;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class s0 implements C3 {
    final r0 a;
    final Supplier b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s0(U2 u2, r0 r0Var, n nVar) {
        this.a = r0Var;
        this.b = nVar;
    }

    @Override // j$.util.stream.C3
    public final Object a(u0 u0Var, j$.util.Q q) {
        return (Boolean) new t0(this, u0Var, q).invoke();
    }

    @Override // j$.util.stream.C3
    public final int b() {
        return T2.u | T2.r;
    }

    @Override // j$.util.stream.C3
    public final Object c(u0 u0Var, j$.util.Q q) {
        q0 q0Var = (q0) this.b.get();
        u0Var.X0(q, q0Var);
        return Boolean.valueOf(q0Var.b);
    }
}
