package j$.util.stream;

import j$.util.Comparator$-CC;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes2.dex */
final class N3 extends d3 {
    private final boolean l;
    private final Comparator m;

    /* JADX INFO: Access modifiers changed from: package-private */
    public N3(c cVar) {
        super(cVar, f4.REFERENCE, e4.q | e4.o);
        this.l = true;
        this.m = Comparator$-CC.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public N3(c cVar, Comparator comparator) {
        super(cVar, f4.REFERENCE, e4.q | e4.p);
        this.l = false;
        Objects.requireNonNull(comparator);
        this.m = comparator;
    }

    @Override // j$.util.stream.c
    public B1 C0(z2 z2Var, j$.util.t tVar, j$.util.function.m mVar) {
        if (e4.SORTED.d(z2Var.q0()) && this.l) {
            return z2Var.n0(tVar, false, mVar);
        }
        Object[] q = z2Var.n0(tVar, true, mVar).q(mVar);
        Arrays.sort(q, this.m);
        return new E1(q);
    }

    @Override // j$.util.stream.c
    public n3 F0(int i, n3 n3Var) {
        Objects.requireNonNull(n3Var);
        return (e4.SORTED.d(i) && this.l) ? n3Var : e4.SIZED.d(i) ? new S3(n3Var, this.m) : new O3(n3Var, this.m);
    }
}
