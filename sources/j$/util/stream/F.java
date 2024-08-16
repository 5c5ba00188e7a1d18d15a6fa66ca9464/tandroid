package j$.util.stream;

import j$.util.function.Predicate;
import j$.util.function.Supplier;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class F implements x3 {
    final boolean a;
    final Object b;
    final Predicate c;
    final Supplier d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public F(boolean z, T2 t2, Object obj, Predicate predicate, Supplier supplier) {
        this.a = z;
        this.b = obj;
        this.c = predicate;
        this.d = supplier;
    }

    @Override // j$.util.stream.x3
    public final Object a(b bVar, j$.util.Q q) {
        y3 y3Var = (y3) this.d.get();
        bVar.D0(q, y3Var);
        Object obj = y3Var.get();
        return obj != null ? obj : this.b;
    }

    @Override // j$.util.stream.x3
    public final Object c(b bVar, j$.util.Q q) {
        return new L(this, bVar, q).invoke();
    }

    @Override // j$.util.stream.x3
    public final int d() {
        return S2.u | (this.a ? 0 : S2.r);
    }
}
