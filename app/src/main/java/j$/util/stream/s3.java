package j$.util.stream;

import j$.util.u;
/* loaded from: classes2.dex */
public class s3 extends J0 {
    final /* synthetic */ long l;
    final /* synthetic */ long m;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s3(c cVar, e4 e4Var, int i, long j, long j2) {
        super(cVar, e4Var, i);
        this.l = j;
        this.m = j2;
    }

    @Override // j$.util.stream.c
    A1 E0(y2 y2Var, j$.util.u uVar, j$.util.function.m mVar) {
        long q0 = y2Var.q0(uVar);
        if (q0 > 0 && uVar.hasCharacteristics(16384)) {
            return x2.g(y2Var, B3.b(y2Var.r0(), uVar, this.l, this.m), true);
        }
        return !d4.ORDERED.d(y2Var.s0()) ? x2.g(this, N0((u.a) y2Var.w0(uVar), this.l, this.m, q0), true) : (A1) new A3(this, y2Var, uVar, mVar, this.l, this.m).invoke();
    }

    @Override // j$.util.stream.c
    j$.util.u F0(y2 y2Var, j$.util.u uVar) {
        long d;
        long q0 = y2Var.q0(uVar);
        if (q0 > 0 && uVar.hasCharacteristics(16384)) {
            u.a aVar = (u.a) y2Var.w0(uVar);
            long j = this.l;
            d = B3.d(j, this.m);
            return new w4(aVar, j, d);
        }
        return !d4.ORDERED.d(y2Var.s0()) ? N0((u.a) y2Var.w0(uVar), this.l, this.m, q0) : ((A1) new A3(this, y2Var, uVar, q3.a, this.l, this.m).invoke()).mo69spliterator();
    }

    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        return new r3(this, m3Var);
    }

    u.a N0(u.a aVar, long j, long j2, long j3) {
        long j4;
        long j5;
        if (j <= j3) {
            long j6 = j3 - j;
            j4 = j2 >= 0 ? Math.min(j2, j6) : j6;
            j5 = 0;
        } else {
            j5 = j;
            j4 = j2;
        }
        return new F4(aVar, j5, j4);
    }
}
