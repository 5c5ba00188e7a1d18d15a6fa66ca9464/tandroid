package j$.util.stream;

import j$.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class M0 extends P0 implements z0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public M0(z0 z0Var, z0 z0Var2) {
        super(z0Var, z0Var2);
    }

    @Override // j$.util.stream.F0
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public final /* synthetic */ void i(Double[] dArr, int i) {
        t0.n(this, dArr, i);
    }

    @Override // j$.util.stream.E0
    public final Object c(int i) {
        return new double[i];
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ void forEach(Consumer consumer) {
        t0.q(this, consumer);
    }

    @Override // j$.util.stream.F0
    public final j$.util.N spliterator() {
        return new d1(this);
    }

    @Override // j$.util.stream.F0
    public final j$.util.Q spliterator() {
        return new d1(this);
    }

    @Override // j$.util.stream.F0
    public final /* synthetic */ F0 t(long j, long j2, j$.util.function.I i) {
        return t0.t(this, j, j2);
    }
}
