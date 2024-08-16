package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class w2 extends z {
    /* JADX INFO: Access modifiers changed from: package-private */
    public w2(b bVar) {
        super(bVar, S2.q | S2.o, 0);
    }

    @Override // j$.util.stream.b
    public final F0 w0(j$.util.Q q, j$.util.function.I i, b bVar) {
        if (S2.SORTED.d(bVar.s0())) {
            return bVar.k0(q, false, i);
        }
        double[] dArr = (double[]) ((z0) bVar.k0(q, true, i)).e();
        Arrays.sort(dArr);
        return new R0(dArr);
    }

    @Override // j$.util.stream.b
    public final e2 z0(int i, e2 e2Var) {
        e2Var.getClass();
        return S2.SORTED.d(i) ? e2Var : S2.SIZED.d(i) ? new B2(e2Var) : new t2(e2Var);
    }
}
