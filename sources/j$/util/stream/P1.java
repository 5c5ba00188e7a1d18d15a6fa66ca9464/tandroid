package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class P1 extends S1 implements v1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public P1(v1 v1Var, v1 v1Var2) {
        super(v1Var, v1Var2);
    }

    @Override // j$.util.stream.B1
    /* renamed from: a */
    public /* synthetic */ void i(Double[] dArr, int i) {
        p1.h(this, dArr, i);
    }

    @Override // j$.util.stream.A1
    /* renamed from: f */
    public double[] c(int i) {
        return new double[i];
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ void forEach(Consumer consumer) {
        p1.k(this, consumer);
    }

    @Override // j$.util.stream.B1
    /* renamed from: h */
    public /* synthetic */ v1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.n(this, j, j2, mVar);
    }

    @Override // j$.util.stream.B1
    public j$.util.u spliterator() {
        return new g2(this);
    }

    @Override // j$.util.stream.B1
    public j$.util.t spliterator() {
        return new g2(this);
    }
}
