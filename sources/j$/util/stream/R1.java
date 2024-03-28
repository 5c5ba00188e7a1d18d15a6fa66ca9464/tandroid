package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class R1 extends S1 implements z1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public R1(z1 z1Var, z1 z1Var2) {
        super(z1Var, z1Var2);
    }

    @Override // j$.util.stream.B1
    /* renamed from: a */
    public /* synthetic */ void i(Long[] lArr, int i) {
        p1.j(this, lArr, i);
    }

    @Override // j$.util.stream.A1
    /* renamed from: f */
    public long[] c(int i) {
        return new long[i];
    }

    @Override // j$.util.stream.B1
    public /* synthetic */ void forEach(Consumer consumer) {
        p1.m(this, consumer);
    }

    @Override // j$.util.stream.B1
    /* renamed from: h */
    public /* synthetic */ z1 r(long j, long j2, j$.util.function.m mVar) {
        return p1.p(this, j, j2, mVar);
    }

    @Override // j$.util.stream.B1
    public j$.util.u spliterator() {
        return new i2(this);
    }

    @Override // j$.util.stream.B1
    public j$.util.t spliterator() {
        return new i2(this);
    }
}
