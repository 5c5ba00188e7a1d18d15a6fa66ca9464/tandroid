package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class g3 extends V2 implements j$.util.H {
    g3(u0 u0Var, j$.util.Q q, boolean z) {
        super(u0Var, q, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g3(u0 u0Var, a aVar, boolean z) {
        super(u0Var, aVar, z);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.p(this, consumer);
    }

    @Override // j$.util.N
    /* renamed from: c */
    public final void forEachRemaining(j$.util.function.K k) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(k));
            return;
        }
        k.getClass();
        h();
        f3 f3Var = new f3(k, 1);
        this.b.X0(this.d, f3Var);
        this.i = true;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.g(this, consumer);
    }

    @Override // j$.util.stream.V2
    final void i() {
        K2 k2 = new K2();
        this.h = k2;
        this.e = this.b.Y0(new f3(k2, 0));
        this.f = new a(this, 5);
    }

    @Override // j$.util.N
    /* renamed from: j */
    public final boolean tryAdvance(j$.util.function.K k) {
        k.getClass();
        boolean f = f();
        if (f) {
            K2 k2 = (K2) this.h;
            long j = this.g;
            int t = k2.t(j);
            k.accept((k2.c == 0 && t == 0) ? ((int[]) k2.e)[(int) j] : ((int[][]) k2.f)[t][(int) (j - k2.d[t])]);
        }
        return f;
    }

    @Override // j$.util.stream.V2
    final V2 k(j$.util.Q q) {
        return new g3(this.b, q, this.a);
    }

    @Override // j$.util.stream.V2, j$.util.Q
    public final j$.util.H trySplit() {
        return (j$.util.H) super.trySplit();
    }
}
