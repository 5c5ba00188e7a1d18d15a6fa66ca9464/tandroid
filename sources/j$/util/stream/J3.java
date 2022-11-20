package j$.util.stream;

import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes2.dex */
final class J3 extends Q {
    /* JADX INFO: Access modifiers changed from: package-private */
    public J3(c cVar) {
        super(cVar, e4.DOUBLE_VALUE, d4.q | d4.o);
    }

    @Override // j$.util.stream.c
    public A1 E0(y2 y2Var, j$.util.u uVar, j$.util.function.m mVar) {
        if (d4.SORTED.d(y2Var.s0())) {
            return y2Var.p0(uVar, false, mVar);
        }
        double[] dArr = (double[]) ((u1) y2Var.p0(uVar, true, mVar)).e();
        Arrays.sort(dArr);
        return new T1(dArr);
    }

    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        Objects.requireNonNull(m3Var);
        return d4.SORTED.d(i) ? m3Var : d4.SIZED.d(i) ? new O3(m3Var) : new G3(m3Var);
    }
}
