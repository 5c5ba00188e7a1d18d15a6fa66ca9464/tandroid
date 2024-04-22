package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class P1 extends R1 implements w1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public P1(w1 w1Var, w1 w1Var2) {
        super(w1Var, w1Var2);
    }

    @Override // j$.util.stream.A1
    /* renamed from: a */
    public /* synthetic */ void i(Integer[] numArr, int i) {
        o1.i(this, numArr, i);
    }

    @Override // j$.util.stream.z1
    /* renamed from: f */
    public int[] c(int i) {
        return new int[i];
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ void forEach(Consumer consumer) {
        o1.l(this, consumer);
    }

    @Override // j$.util.stream.A1
    /* renamed from: h */
    public /* synthetic */ w1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.o(this, j, j2, mVar);
    }

    @Override // j$.util.stream.A1
    public j$.util.t spliterator() {
        return new g2(this);
    }

    @Override // j$.util.stream.A1
    public j$.util.s spliterator() {
        return new g2(this);
    }
}
