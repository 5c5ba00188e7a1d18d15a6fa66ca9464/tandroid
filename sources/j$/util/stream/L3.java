package j$.util.stream;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
final class L3 extends c1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public L3(c cVar) {
        super(cVar, e4.LONG_VALUE, d4.q | d4.o);
    }

    @Override // j$.util.stream.c
    public m3 C0(int i, m3 m3Var) {
        Objects.requireNonNull(m3Var);
        return d4.SORTED.d(i) ? m3Var : d4.SIZED.d(i) ? new Q3(m3Var) : new I3(m3Var);
    }

    @Override // j$.util.stream.c
    public A1 z0(y2 y2Var, j$.util.s sVar, j$.util.function.m mVar) {
        if (d4.SORTED.d(y2Var.n0())) {
            return y2Var.k0(sVar, false, mVar);
        }
        long[] jArr = (long[]) ((y1) y2Var.k0(sVar, true, mVar)).e();
        Arrays.sort(jArr);
        return new l2(jArr);
    }
}
