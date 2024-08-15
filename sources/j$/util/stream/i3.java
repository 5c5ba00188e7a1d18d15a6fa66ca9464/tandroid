package j$.util.stream;

import j$.util.function.Consumer;
/* loaded from: classes2.dex */
final class i3 extends V2 implements j$.util.K {
    i3(u0 u0Var, j$.util.Q q, boolean z) {
        super(u0Var, q, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i3(u0 u0Var, a aVar, boolean z) {
        super(u0Var, aVar, z);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean a(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }

    @Override // j$.util.N
    /* renamed from: b */
    public final void forEachRemaining(j$.util.function.h0 h0Var) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(h0Var));
            return;
        }
        h0Var.getClass();
        h();
        h3 h3Var = new h3(h0Var, 1);
        this.b.X0(this.d, h3Var);
        this.i = true;
    }

    @Override // j$.util.N
    /* renamed from: e */
    public final boolean tryAdvance(j$.util.function.h0 h0Var) {
        h0Var.getClass();
        boolean f = f();
        if (f) {
            M2 m2 = (M2) this.h;
            long j = this.g;
            int t = m2.t(j);
            h0Var.accept((m2.c == 0 && t == 0) ? ((long[]) m2.e)[(int) j] : ((long[][]) m2.f)[t][(int) (j - m2.d[t])]);
        }
        return f;
    }

    @Override // j$.util.Q
    public final /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.stream.V2
    final void i() {
        M2 m2 = new M2();
        this.h = m2;
        this.e = this.b.Y0(new h3(m2, 0));
        this.f = new a(this, 6);
    }

    @Override // j$.util.stream.V2
    final V2 k(j$.util.Q q) {
        return new i3(this.b, q, this.a);
    }

    @Override // j$.util.stream.V2, j$.util.Q
    public final j$.util.K trySplit() {
        return (j$.util.K) super.trySplit();
    }
}
