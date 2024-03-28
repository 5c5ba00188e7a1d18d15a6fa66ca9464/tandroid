package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class m1 implements O4 {
    private final f4 a;
    final l1 b;
    final Supplier c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m1(f4 f4Var, l1 l1Var, Supplier supplier) {
        this.a = f4Var;
        this.b = l1Var;
        this.c = supplier;
    }

    @Override // j$.util.stream.O4
    public int b() {
        return e4.u | e4.r;
    }

    @Override // j$.util.stream.O4
    public Object c(z2 z2Var, j$.util.t tVar) {
        return (Boolean) new n1(this, z2Var, tVar).invoke();
    }

    @Override // j$.util.stream.O4
    public Object d(z2 z2Var, j$.util.t tVar) {
        k1 k1Var = (k1) this.c.get();
        c cVar = (c) z2Var;
        Objects.requireNonNull(k1Var);
        cVar.l0(cVar.t0(k1Var), tVar);
        return Boolean.valueOf(k1Var.b);
    }
}
