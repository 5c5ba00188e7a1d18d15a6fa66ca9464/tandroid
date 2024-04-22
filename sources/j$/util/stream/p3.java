package j$.util.stream;

import org.telegram.messenger.LiteMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class p3 extends c3 {
    final /* synthetic */ long l;
    final /* synthetic */ long m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public p3(c cVar, e4 e4Var, int i, long j, long j2) {
        super(cVar, e4Var, i);
        this.l = j;
        this.m = j2;
    }

    @Override // j$.util.stream.c
    j$.util.s A0(y2 y2Var, j$.util.s sVar) {
        long d;
        long l0 = y2Var.l0(sVar);
        if (l0 > 0 && sVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            j$.util.s r0 = y2Var.r0(sVar);
            long j = this.l;
            d = B3.d(j, this.m);
            return new C4(r0, j, d);
        }
        return !d4.ORDERED.d(y2Var.n0()) ? G0(y2Var.r0(sVar), this.l, this.m, l0) : ((A1) new A3(this, y2Var, sVar, new j$.util.function.m() { // from class: j$.util.stream.n3
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Object[i];
            }
        }, this.l, this.m).invoke()).spliterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public m3 C0(int i, m3 m3Var) {
        return new o3(this, m3Var);
    }

    j$.util.s G0(j$.util.s sVar, long j, long j2, long j3) {
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
        return new I4(sVar, j4, j5);
    }

    @Override // j$.util.stream.c
    A1 z0(y2 y2Var, j$.util.s sVar, j$.util.function.m mVar) {
        long l0 = y2Var.l0(sVar);
        if (l0 > 0 && sVar.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            return x2.e(y2Var, B3.b(y2Var.m0(), sVar, this.l, this.m), true, mVar);
        }
        return !d4.ORDERED.d(y2Var.n0()) ? x2.e(this, G0(y2Var.r0(sVar), this.l, this.m, l0), true, mVar) : (A1) new A3(this, y2Var, sVar, mVar, this.l, this.m).invoke();
    }
}
