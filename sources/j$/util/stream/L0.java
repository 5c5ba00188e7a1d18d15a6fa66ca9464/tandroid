package j$.util.stream;

import j$.util.function.Consumer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class L0 extends O0 implements z0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public L0(z0 z0Var, z0 z0Var2) {
        super(z0Var, z0Var2);
    }

    @Override // j$.util.stream.D0
    /* renamed from: f */
    public final /* synthetic */ void e(Double[] dArr, int i) {
        u0.s0(this, dArr, i);
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ void forEach(Consumer consumer) {
        u0.v0(this, consumer);
    }

    @Override // j$.util.stream.C0
    public final Object newArray(int i) {
        return new double[i];
    }

    @Override // j$.util.stream.D0
    public final /* synthetic */ D0 q(long j, long j2, j$.util.function.N n) {
        return u0.y0(this, j, j2);
    }

    @Override // j$.util.stream.D0
    public final j$.util.N spliterator() {
        return new c1(this);
    }

    @Override // j$.util.stream.D0
    public final j$.util.Q spliterator() {
        return new c1(this);
    }
}
