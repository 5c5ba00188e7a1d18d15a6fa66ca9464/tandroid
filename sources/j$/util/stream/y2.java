package j$.util.stream;

import java.util.Arrays;
/* loaded from: classes2.dex */
final class y2 extends b0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public y2(c cVar) {
        super(cVar, T2.q | T2.o);
    }

    @Override // j$.util.stream.c
    public final D0 i1(j$.util.Q q, j$.util.function.N n, c cVar) {
        if (T2.SORTED.d(cVar.K0())) {
            return cVar.Z0(q, false, n);
        }
        int[] iArr = (int[]) ((A0) cVar.Z0(q, true, n)).b();
        Arrays.sort(iArr);
        return new Z0(iArr);
    }

    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        f2Var.getClass();
        return T2.SORTED.d(i) ? f2Var : T2.SIZED.d(i) ? new D2(f2Var) : new v2(f2Var);
    }
}
