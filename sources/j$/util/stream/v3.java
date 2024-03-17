package j$.util.stream;

import j$.util.t;
import org.telegram.messenger.LiteMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class v3 extends b1 {
    final /* synthetic */ long l;
    final /* synthetic */ long m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public v3(c cVar, e4 e4Var, int i, long j, long j2) {
        super(cVar, e4Var, i);
        this.l = j;
        this.m = j2;
    }

    @Override // j$.util.stream.c
    A1 E0(y2 y2Var, j$.util.t tVar, j$.util.function.m mVar) {
        long q0 = y2Var.q0(tVar);
        if (q0 > 0 && tVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            return x2.h(y2Var, B3.b(y2Var.r0(), tVar, this.l, this.m), true);
        }
        return !d4.ORDERED.d(y2Var.s0()) ? x2.h(this, N0((t.c) y2Var.w0(tVar), this.l, this.m, q0), true) : (A1) new A3(this, y2Var, tVar, mVar, this.l, this.m).invoke();
    }

    @Override // j$.util.stream.c
    j$.util.t F0(y2 y2Var, j$.util.t tVar) {
        long d;
        long q0 = y2Var.q0(tVar);
        if (q0 > 0 && tVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            t.c cVar = (t.c) y2Var.w0(tVar);
            long j = this.l;
            d = B3.d(j, this.m);
            return new y4(cVar, j, d);
        }
        return !d4.ORDERED.d(y2Var.s0()) ? N0((t.c) y2Var.w0(tVar), this.l, this.m, q0) : ((A1) new A3(this, y2Var, tVar, new j$.util.function.m() { // from class: j$.util.stream.t3
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Long[i];
            }
        }, this.l, this.m).invoke()).spliterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public m3 H0(int i, m3 m3Var) {
        return new u3(this, m3Var);
    }

    t.c N0(t.c cVar, long j, long j2, long j3) {
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
        return new G4(cVar, j4, j5);
    }
}
