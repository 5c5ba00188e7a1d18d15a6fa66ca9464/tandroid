package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Q1 extends S1 implements x1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Q1(x1 x1Var, x1 x1Var2) {
        super(x1Var, x1Var2);
    }

    @Override // j$.util.stream.B1
    /* renamed from: a */
    public /* synthetic */ void i(Integer[] numArr, int i) {
        p1.i(this, numArr, i);
    }

    @Override // j$.util.stream.A1
    /* renamed from: f */
    public int[] c(int i) {
        return new int[i];
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ void forEach(Consumer consumer) {
        p1.l(this, consumer);
    }

    @Override // j$.util.stream.B1
    /* renamed from: h */
    public /* synthetic */ x1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.o(this, j, j2, mVar);
    }

    @Override // j$.util.stream.B1
    public j$.util.u spliterator() {
        return new h2(this);
    }

    @Override // j$.util.stream.B1
    public j$.util.t spliterator() {
        return new h2(this);
    }
}
