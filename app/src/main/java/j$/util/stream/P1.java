package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class P1 extends R1 implements w1 {
    public P1(w1 w1Var, w1 w1Var2) {
        super(w1Var, w1Var2);
    }

    /* renamed from: a */
    public /* synthetic */ void i(Integer[] numArr, int i) {
        o1.i(this, numArr, i);
    }

    /* renamed from: f */
    public int[] c(int i) {
        return new int[i];
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ void forEach(Consumer consumer) {
        o1.l(this, consumer);
    }

    /* renamed from: h */
    public /* synthetic */ w1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.o(this, j, j2, mVar);
    }

    @Override // j$.util.stream.A1
    /* renamed from: spliterator */
    public j$.util.w mo69spliterator() {
        return new g2(this);
    }

    @Override // j$.util.stream.A1
    /* renamed from: spliterator */
    public j$.util.u mo69spliterator() {
        return new g2(this);
    }
}
