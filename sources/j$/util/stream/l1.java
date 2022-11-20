package j$.util.stream;

import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class l1 implements N4 {
    private final e4 a;
    final k1 b;
    final j$.util.function.y c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l1(e4 e4Var, k1 k1Var, j$.util.function.y yVar) {
        this.a = e4Var;
        this.b = k1Var;
        this.c = yVar;
    }

    @Override // j$.util.stream.N4
    public int b() {
        return d4.u | d4.r;
    }

    @Override // j$.util.stream.N4
    public Object c(y2 y2Var, j$.util.u uVar) {
        return (Boolean) new m1(this, y2Var, uVar).invoke();
    }

    @Override // j$.util.stream.N4
    public Object d(y2 y2Var, j$.util.u uVar) {
        j1 j1Var = (j1) this.c.get();
        c cVar = (c) y2Var;
        Objects.requireNonNull(j1Var);
        cVar.n0(cVar.v0(j1Var), uVar);
        return Boolean.valueOf(j1Var.b);
    }
}
