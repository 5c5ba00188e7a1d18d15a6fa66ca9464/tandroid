package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class Q1 extends R1 implements y1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public Q1(y1 y1Var, y1 y1Var2) {
        super(y1Var, y1Var2);
    }

    @Override // j$.util.stream.A1
    /* renamed from: a */
    public /* synthetic */ void i(Long[] lArr, int i) {
        o1.j(this, lArr, i);
    }

    @Override // j$.util.stream.z1
    /* renamed from: f */
    public long[] c(int i) {
        return new long[i];
    }

    @Override // j$.util.stream.A1
    public /* synthetic */ void forEach(Consumer consumer) {
        o1.m(this, consumer);
    }

    @Override // j$.util.stream.A1
    /* renamed from: h */
    public /* synthetic */ y1 r(long j, long j2, j$.util.function.m mVar) {
        return o1.p(this, j, j2, mVar);
    }

    @Override // j$.util.stream.A1
    /* renamed from: spliterator  reason: collision with other method in class */
    public j$.util.w mo313spliterator() {
        return new h2(this);
    }

    @Override // j$.util.stream.A1
    /* renamed from: spliterator */
    public j$.util.u mo313spliterator() {
        return new h2(this);
    }
}
