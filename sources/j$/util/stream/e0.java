package j$.util.stream;

import j$.util.function.Predicate;
import j$.util.function.Supplier;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class e0 implements N4 {
    private final e4 a;
    final boolean b;
    final Object c;
    final Predicate d;
    final Supplier e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e0(boolean z, e4 e4Var, Object obj, Predicate predicate, Supplier supplier) {
        this.b = z;
        this.a = e4Var;
        this.c = obj;
        this.d = predicate;
        this.e = supplier;
    }

    @Override // j$.util.stream.N4
    public int b() {
        return d4.u | (this.b ? 0 : d4.r);
    }

    @Override // j$.util.stream.N4
    public Object c(y2 y2Var, j$.util.s sVar) {
        return new k0(this, y2Var, sVar).invoke();
    }

    @Override // j$.util.stream.N4
    public Object d(y2 y2Var, j$.util.s sVar) {
        O4 o4 = (O4) this.e.get();
        c cVar = (c) y2Var;
        Objects.requireNonNull(o4);
        cVar.i0(cVar.q0(o4), sVar);
        Object obj = o4.get();
        return obj != null ? obj : this.c;
    }
}
