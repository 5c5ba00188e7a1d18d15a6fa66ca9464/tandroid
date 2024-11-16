package j$.util.stream;

import org.telegram.messenger.LiteMode;

/* loaded from: classes2.dex */
final class m2 extends z {
    final /* synthetic */ long m;
    final /* synthetic */ long n;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    m2(b bVar, int i, long j, long j2) {
        super(bVar, i, 0);
        this.m = j;
        this.n = j2;
    }

    @Override // j$.util.stream.b
    final F0 w0(j$.util.Q q, j$.util.function.I i, b bVar) {
        long j;
        long j2;
        long o0 = bVar.o0(q);
        if (o0 > 0 && q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            return t0.F(bVar, t0.y(bVar.r0(), q, this.m, this.n), true);
        }
        if (S2.ORDERED.d(bVar.s0())) {
            return (F0) new o2(this, bVar, q, i, this.m, this.n).invoke();
        }
        j$.util.E e = (j$.util.E) bVar.F0(q);
        long j3 = this.m;
        long j4 = this.n;
        if (j3 <= o0) {
            j = j4 >= 0 ? Math.min(j4, o0 - j3) : o0 - j3;
            j2 = 0;
        } else {
            j = j4;
            j2 = j3;
        }
        return t0.F(this, new o3(e, j2, j), true);
    }

    @Override // j$.util.stream.b
    final j$.util.Q x0(b bVar, j$.util.Q q) {
        long j;
        long j2;
        long o0 = bVar.o0(q);
        long j3 = this.n;
        if (o0 > 0 && q.hasCharacteristics(LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM)) {
            j$.util.E e = (j$.util.E) bVar.F0(q);
            long j4 = this.m;
            return new i3(e, j4, t0.x(j4, j3));
        }
        if (S2.ORDERED.d(bVar.s0())) {
            return ((F0) new o2(this, bVar, q, new Q1(5), this.m, this.n).invoke()).spliterator();
        }
        j$.util.E e2 = (j$.util.E) bVar.F0(q);
        long j5 = this.m;
        if (j5 <= o0) {
            long j6 = o0 - j5;
            if (j3 >= 0) {
                j6 = Math.min(j3, j6);
            }
            j = j6;
            j2 = 0;
        } else {
            j = j3;
            j2 = j5;
        }
        return new o3(e2, j2, j);
    }

    @Override // j$.util.stream.b
    final e2 z0(int i, e2 e2Var) {
        return new l2(this, e2Var);
    }
}
