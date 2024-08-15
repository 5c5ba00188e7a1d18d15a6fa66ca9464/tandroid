package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class e3 extends V2 implements j$.util.E {
    e3(u0 u0Var, j$.util.Q q, boolean z) {
        super(u0Var, q, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e3(u0 u0Var, a aVar, boolean z) {
        super(u0Var, aVar, z);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }

    @Override // j$.util.N
    /* renamed from: d */
    public final void forEachRemaining(j$.util.function.m mVar) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(mVar));
            return;
        }
        mVar.getClass();
        h();
        d3 d3Var = new d3(mVar, 1);
        this.b.X0(this.d, d3Var);
        this.i = true;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.stream.V2
    final void i() {
        I2 i2 = new I2();
        this.h = i2;
        this.e = this.b.Y0(new d3(i2, 0));
        this.f = new a(this, 4);
    }

    @Override // j$.util.stream.V2
    final V2 k(j$.util.Q q) {
        return new e3(this.b, q, this.a);
    }

    @Override // j$.util.N
    /* renamed from: o */
    public final boolean tryAdvance(j$.util.function.m mVar) {
        mVar.getClass();
        boolean f = f();
        if (f) {
            I2 i2 = (I2) this.h;
            long j = this.g;
            int t = i2.t(j);
            mVar.accept((i2.c == 0 && t == 0) ? ((double[]) i2.e)[(int) j] : ((double[][]) i2.f)[t][(int) (j - i2.d[t])]);
        }
        return f;
    }

    @Override // j$.util.stream.V2, j$.util.Q
    public final j$.util.E trySplit() {
        return (j$.util.E) super.trySplit();
    }
}
