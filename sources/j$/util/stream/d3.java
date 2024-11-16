package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;

/* loaded from: classes2.dex */
final class d3 extends U2 implements j$.util.E {
    d3(b bVar, j$.util.Q q, boolean z) {
        super(bVar, q, z);
    }

    d3(b bVar, Supplier supplier, boolean z) {
        super(bVar, supplier, z);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }

    @Override // j$.util.N
    public final void e(j$.util.function.n nVar) {
        if (this.h != null || this.i) {
            while (p(nVar)) {
            }
            return;
        }
        nVar.getClass();
        h();
        c3 c3Var = new c3(nVar, 1);
        this.b.D0(this.d, c3Var);
        this.i = true;
    }

    @Override // j$.util.stream.U2
    final void j() {
        H2 h2 = new H2();
        this.h = h2;
        this.e = this.b.E0(new c3(h2, 0));
        this.f = new a(this, 3);
    }

    @Override // j$.util.stream.U2
    final U2 k(j$.util.Q q) {
        return new d3(this.b, q, this.a);
    }

    @Override // j$.util.N
    public final boolean p(j$.util.function.n nVar) {
        nVar.getClass();
        boolean b = b();
        if (b) {
            H2 h2 = (H2) this.h;
            long j = this.g;
            int w = h2.w(j);
            nVar.accept((h2.c == 0 && w == 0) ? ((double[]) h2.e)[(int) j] : ((double[][]) h2.f)[w][(int) (j - h2.d[w])]);
        }
        return b;
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.n(this, consumer);
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.E trySplit() {
        return (j$.util.E) super.trySplit();
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.N trySplit() {
        return (j$.util.E) super.trySplit();
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.Q trySplit() {
        return (j$.util.E) super.trySplit();
    }
}
