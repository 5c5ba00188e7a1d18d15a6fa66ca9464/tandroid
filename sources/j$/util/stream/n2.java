package j$.util.stream;

import org.telegram.messenger.LiteMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class n2 extends A {
    final /* synthetic */ long l;
    final /* synthetic */ long m;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public n2(c cVar, int i, long j, long j2) {
        super(cVar, i);
        this.l = j;
        this.m = j2;
    }

    static j$.util.E s1(j$.util.E e, long j, long j2, long j3) {
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
        return new s3(e, j4, j5);
    }

    @Override // j$.util.stream.c
    final D0 i1(j$.util.Q q, j$.util.function.N n, c cVar) {
        long I0 = cVar.I0(q);
        return (I0 <= 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) ? !T2.ORDERED.d(cVar.K0()) ? u1.i(this, s1((j$.util.E) cVar.p1(q), this.l, this.m, I0), true) : (D0) new p2(this, cVar, q, n, this.l, this.m).invoke() : u1.i(cVar, u0.D0(cVar.f1(), q, this.l, this.m), true);
    }

    @Override // j$.util.stream.c
    final j$.util.Q j1(c cVar, j$.util.Q q) {
        long I0 = cVar.I0(q);
        if (I0 <= 0 || !q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            return !T2.ORDERED.d(cVar.K0()) ? s1((j$.util.E) cVar.p1(q), this.l, this.m, I0) : ((D0) new p2(this, cVar, q, new J0(6), this.l, this.m).invoke()).spliterator();
        }
        long j = this.l;
        return new k3((j$.util.E) cVar.p1(q), j, u0.C0(j, this.m));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.c
    public final f2 l1(int i, f2 f2Var) {
        return new m2(this, f2Var);
    }
}
