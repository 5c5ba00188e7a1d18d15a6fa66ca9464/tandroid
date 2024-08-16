package j$.util.stream;

import j$.util.Comparator$-CC;
import java.util.Arrays;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class z2 extends V1 {
    private final boolean m;
    private final Comparator n;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z2(b bVar) {
        super(bVar, S2.q | S2.o, 0);
        this.m = true;
        this.n = Comparator$-CC.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public z2(b bVar, Comparator comparator) {
        super(bVar, S2.q | S2.p, 0);
        this.m = false;
        comparator.getClass();
        this.n = comparator;
    }

    @Override // j$.util.stream.b
    public final F0 w0(j$.util.Q q, j$.util.function.I i, b bVar) {
        if (S2.SORTED.d(bVar.s0()) && this.m) {
            return bVar.k0(q, false, i);
        }
        Object[] s = bVar.k0(q, true, i).s(i);
        Arrays.sort(s, this.n);
        return new I0(s);
    }

    @Override // j$.util.stream.b
    public final e2 z0(int i, e2 e2Var) {
        e2Var.getClass();
        if (S2.SORTED.d(i) && this.m) {
            return e2Var;
        }
        boolean d = S2.SIZED.d(i);
        Comparator comparator = this.n;
        return d ? new E2(e2Var, comparator) : new A2(e2Var, comparator);
    }
}
