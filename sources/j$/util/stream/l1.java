package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class l1 implements N4 {
    private final e4 a;
    final k1 b;
    final Supplier c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l1(e4 e4Var, k1 k1Var, Supplier supplier) {
        this.a = e4Var;
        this.b = k1Var;
        this.c = supplier;
    }

    @Override // j$.util.stream.N4
    public int b() {
        return d4.u | d4.r;
    }

    @Override // j$.util.stream.N4
    public Object c(y2 y2Var, j$.util.s sVar) {
        return (Boolean) new m1(this, y2Var, sVar).invoke();
    }

    @Override // j$.util.stream.N4
    public Object d(y2 y2Var, j$.util.s sVar) {
        j1 j1Var = (j1) this.c.get();
        c cVar = (c) y2Var;
        Objects.requireNonNull(j1Var);
        cVar.i0(cVar.q0(j1Var), sVar);
        return Boolean.valueOf(j1Var.b);
    }
}
