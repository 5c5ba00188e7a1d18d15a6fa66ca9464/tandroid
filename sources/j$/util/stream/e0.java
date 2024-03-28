package j$.util.stream;

import j$.util.function.Predicate;
import j$.util.function.Supplier;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class e0 implements O4 {
    private final f4 a;
    final boolean b;
    final Object c;
    final Predicate d;
    final Supplier e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e0(boolean z, f4 f4Var, Object obj, Predicate predicate, Supplier supplier) {
        this.b = z;
        this.a = f4Var;
        this.c = obj;
        this.d = predicate;
        this.e = supplier;
    }

    @Override // j$.util.stream.O4
    public int b() {
        return e4.u | (this.b ? 0 : e4.r);
    }

    @Override // j$.util.stream.O4
    public Object c(z2 z2Var, j$.util.t tVar) {
        return new k0(this, z2Var, tVar).invoke();
    }

    @Override // j$.util.stream.O4
    public Object d(z2 z2Var, j$.util.t tVar) {
        P4 p4 = (P4) this.e.get();
        c cVar = (c) z2Var;
        Objects.requireNonNull(p4);
        cVar.l0(cVar.t0(p4), tVar);
        Object obj = p4.get();
        return obj != null ? obj : this.c;
    }
}
