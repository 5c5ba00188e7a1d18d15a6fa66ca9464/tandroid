package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;

/* loaded from: classes2.dex */
final class h3 extends U2 implements j$.util.K {
    h3(b bVar, j$.util.Q q, boolean z) {
        super(bVar, q, z);
    }

    h3(b bVar, Supplier supplier, boolean z) {
        super(bVar, supplier, z);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.h(this, consumer);
    }

    @Override // j$.util.N
    /* renamed from: d */
    public final void e(j$.util.function.W w) {
        if (this.h != null || this.i) {
            while (p(w)) {
            }
            return;
        }
        w.getClass();
        h();
        g3 g3Var = new g3(w, 1);
        this.b.D0(this.d, g3Var);
        this.i = true;
    }

    @Override // j$.util.N
    /* renamed from: i */
    public final boolean p(j$.util.function.W w) {
        w.getClass();
        boolean b = b();
        if (b) {
            L2 l2 = (L2) this.h;
            long j = this.g;
            int w2 = l2.w(j);
            w.accept((l2.c == 0 && w2 == 0) ? ((long[]) l2.e)[(int) j] : ((long[][]) l2.f)[w2][(int) (j - l2.d[w2])]);
        }
        return b;
    }

    @Override // j$.util.stream.U2
    final void j() {
        L2 l2 = new L2();
        this.h = l2;
        this.e = this.b.E0(new g3(l2, 0));
        this.f = new a(this, 5);
    }

    @Override // j$.util.stream.U2
    final U2 k(j$.util.Q q) {
        return new h3(this.b, q, this.a);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.q(this, consumer);
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.K trySplit() {
        return (j$.util.K) super.trySplit();
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.N trySplit() {
        return (j$.util.K) super.trySplit();
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.Q trySplit() {
        return (j$.util.K) super.trySplit();
    }
}
