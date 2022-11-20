package j$.util.stream;

import j$.util.Comparator$-CC;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class M3 extends c3 {
    private final boolean l;
    private final Comparator m;

    /* JADX INFO: Access modifiers changed from: package-private */
    public M3(c cVar) {
        super(cVar, e4.REFERENCE, d4.q | d4.o);
        this.l = true;
        this.m = Comparator$-CC.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public M3(c cVar, Comparator comparator) {
        super(cVar, e4.REFERENCE, d4.q | d4.p);
        this.l = false;
        Objects.requireNonNull(comparator);
        this.m = comparator;
    }

    @Override // j$.util.stream.c
    public A1 E0(y2 y2Var, j$.util.u uVar, j$.util.function.m mVar) {
        if (!d4.SORTED.d(y2Var.s0()) || !this.l) {
            Object[] q = y2Var.p0(uVar, true, mVar).q(mVar);
            Arrays.sort(q, this.m);
            return new D1(q);
        }
        return y2Var.p0(uVar, false, mVar);
    }

    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        Objects.requireNonNull(m3Var);
        return (!d4.SORTED.d(i) || !this.l) ? d4.SIZED.d(i) ? new R3(m3Var, this.m) : new N3(m3Var, this.m) : m3Var;
    }
}
