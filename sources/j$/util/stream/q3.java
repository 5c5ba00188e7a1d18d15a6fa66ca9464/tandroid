package j$.util.stream;

import org.telegram.messenger.LiteMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class q3 extends d3 {
    final /* synthetic */ long l;
    final /* synthetic */ long m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public q3(c cVar, f4 f4Var, int i, long j, long j2) {
        super(cVar, f4Var, i);
        this.l = j;
        this.m = j2;
    }

    @Override // j$.util.stream.c
    B1 C0(z2 z2Var, j$.util.t tVar, j$.util.function.m mVar) {
        long o0 = z2Var.o0(tVar);
        if (o0 > 0 && tVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            return y2.e(z2Var, C3.b(z2Var.p0(), tVar, this.l, this.m), true, mVar);
        }
        return !e4.ORDERED.d(z2Var.q0()) ? y2.e(this, J0(z2Var.u0(tVar), this.l, this.m, o0), true, mVar) : (B1) new B3(this, z2Var, tVar, mVar, this.l, this.m).invoke();
    }

    @Override // j$.util.stream.c
    j$.util.t D0(z2 z2Var, j$.util.t tVar) {
        long d;
        long o0 = z2Var.o0(tVar);
        if (o0 > 0 && tVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            j$.util.t u0 = z2Var.u0(tVar);
            long j = this.l;
            d = C3.d(j, this.m);
            return new D4(u0, j, d);
        }
        return !e4.ORDERED.d(z2Var.q0()) ? J0(z2Var.u0(tVar), this.l, this.m, o0) : ((B1) new B3(this, z2Var, tVar, new j$.util.function.m() { // from class: j$.util.stream.o3
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Object[i];
            }
        }, this.l, this.m).invoke()).spliterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public n3 F0(int i, n3 n3Var) {
        return new p3(this, n3Var);
    }

    j$.util.t J0(j$.util.t tVar, long j, long j2, long j3) {
        long j4;
        long j5;
        if (j <= j3) {
            long j6 = j3 - j;
            j5 = j2 >= 0 ? Math.min(j2, j6) : j6;
            j4 = 0;
        } else {
            j4 = j;
            j5 = j2;
        }
        return new J4(tVar, j4, j5);
    }
}
