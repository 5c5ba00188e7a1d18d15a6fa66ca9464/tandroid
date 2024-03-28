package j$.util.stream;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
final class L3 extends K0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public L3(c cVar) {
        super(cVar, f4.INT_VALUE, e4.q | e4.o);
    }

    @Override // j$.util.stream.c
    public B1 C0(z2 z2Var, j$.util.t tVar, j$.util.function.m mVar) {
        if (e4.SORTED.d(z2Var.q0())) {
            return z2Var.n0(tVar, false, mVar);
        }
        int[] iArr = (int[]) ((x1) z2Var.n0(tVar, true, mVar)).e();
        Arrays.sort(iArr);
        return new d2(iArr);
    }

    @Override // j$.util.stream.c
    public n3 F0(int i, n3 n3Var) {
        Objects.requireNonNull(n3Var);
        return e4.SORTED.d(i) ? n3Var : e4.SIZED.d(i) ? new Q3(n3Var) : new I3(n3Var);
    }
}
