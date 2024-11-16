package j$.util.stream;

import j$.util.function.Supplier;

/* loaded from: classes2.dex */
final class r0 implements x3 {
    final q0 a;
    final Supplier b;

    r0(T2 t2, q0 q0Var, Supplier supplier) {
        this.a = q0Var;
        this.b = supplier;
    }

    @Override // j$.util.stream.x3
    public final Object a(b bVar, j$.util.Q q) {
        p0 p0Var = (p0) this.b.get();
        bVar.D0(q, p0Var);
        return Boolean.valueOf(p0Var.b);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // j$.util.stream.x3
    public final Object c(b bVar, j$.util.Q q) {
        return (Boolean) new s0(this, bVar, q).invoke();
    }

    @Override // j$.util.stream.x3
    public final int d() {
        return S2.u | S2.r;
    }
}
