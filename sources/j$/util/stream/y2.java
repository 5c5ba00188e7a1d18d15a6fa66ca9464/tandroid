package j$.util.stream;

import java.util.Arrays;

/* loaded from: classes2.dex */
final class y2 extends g0 {
    y2(b bVar) {
        super(bVar, S2.q | S2.o, 0);
    }

    @Override // j$.util.stream.b
    public final F0 w0(j$.util.Q q, j$.util.function.I i, b bVar) {
        if (S2.SORTED.d(bVar.s0())) {
            return bVar.k0(q, false, i);
        }
        long[] jArr = (long[]) ((D0) bVar.k0(q, true, i)).e();
        Arrays.sort(jArr);
        return new j1(jArr);
    }

    @Override // j$.util.stream.b
    public final e2 z0(int i, e2 e2Var) {
        e2Var.getClass();
        return S2.SORTED.d(i) ? e2Var : S2.SIZED.d(i) ? new D2(e2Var) : new v2(e2Var);
    }
}
