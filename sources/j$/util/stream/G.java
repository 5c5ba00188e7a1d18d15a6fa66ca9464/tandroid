package j$.util.stream;

import j$.util.function.Predicate;
import j$.util.function.Supplier;
/* loaded from: classes2.dex */
final class G implements C3 {
    final boolean a;
    final Object b;
    final Predicate c;
    final Supplier d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public G(boolean z, U2 u2, Object obj, J0 j0, b bVar) {
        this.a = z;
        this.b = obj;
        this.c = j0;
        this.d = bVar;
    }

    @Override // j$.util.stream.C3
    public final Object a(u0 u0Var, j$.util.Q q) {
        return new M(this, u0Var, q).invoke();
    }

    @Override // j$.util.stream.C3
    public final int b() {
        return T2.u | (this.a ? 0 : T2.r);
    }

    @Override // j$.util.stream.C3
    public final Object c(u0 u0Var, j$.util.Q q) {
        D3 d3 = (D3) this.d.get();
        u0Var.X0(q, d3);
        Object obj = d3.get();
        return obj != null ? obj : this.b;
    }
}
