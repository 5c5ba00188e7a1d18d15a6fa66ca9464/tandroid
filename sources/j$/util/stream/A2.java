package j$.util.stream;

import j$.util.Comparator$-CC;
import java.util.Arrays;
import java.util.Comparator;
/* loaded from: classes2.dex */
final class A2 extends V1 {
    private final boolean l;
    private final Comparator m;

    /* JADX INFO: Access modifiers changed from: package-private */
    public A2(c cVar) {
        super(cVar, T2.q | T2.o);
        this.l = true;
        this.m = Comparator$-CC.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public A2(c cVar, Comparator comparator) {
        super(cVar, T2.q | T2.p);
        this.l = false;
        comparator.getClass();
        this.m = comparator;
    }

    @Override // j$.util.stream.c
    public final D0 i1(j$.util.Q q, j$.util.function.N n, c cVar) {
        if (T2.SORTED.d(cVar.K0()) && this.l) {
            return cVar.Z0(q, false, n);
        }
        Object[] o = cVar.Z0(q, true, n).o(n);
        Arrays.sort(o, this.m);
        return new G0(o);
    }

    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        f2Var.getClass();
        if (T2.SORTED.d(i) && this.l) {
            return f2Var;
        }
        boolean d = T2.SIZED.d(i);
        Comparator comparator = this.m;
        return d ? new F2(f2Var, comparator) : new B2(f2Var, comparator);
    }
}
