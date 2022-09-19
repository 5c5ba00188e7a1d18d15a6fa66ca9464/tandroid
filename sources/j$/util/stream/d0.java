package j$.util.stream;

import j$.util.function.Predicate;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class d0 implements N4 {
    private final e4 a;
    final boolean b;
    final Object c;
    final Predicate d;
    final j$.util.function.y e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d0(boolean z, e4 e4Var, Object obj, Predicate predicate, j$.util.function.y yVar) {
        this.b = z;
        this.a = e4Var;
        this.c = obj;
        this.d = predicate;
        this.e = yVar;
    }

    @Override // j$.util.stream.N4
    public int b() {
        return d4.u | (this.b ? 0 : d4.r);
    }

    @Override // j$.util.stream.N4
    public Object c(y2 y2Var, j$.util.u uVar) {
        return new j0(this, y2Var, uVar).invoke();
    }

    @Override // j$.util.stream.N4
    public Object d(y2 y2Var, j$.util.u uVar) {
        O4 o4 = (O4) this.e.get();
        c cVar = (c) y2Var;
        o4.getClass();
        cVar.n0(cVar.v0(o4), uVar);
        Object obj = o4.get();
        return obj != null ? obj : this.c;
    }
}
