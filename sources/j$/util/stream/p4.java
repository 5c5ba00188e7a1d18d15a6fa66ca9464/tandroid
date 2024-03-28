package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class p4 extends g4 implements t.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public p4(z2 z2Var, Supplier supplier, boolean z) {
        super(z2Var, supplier, z);
    }

    p4(z2 z2Var, j$.util.t tVar, boolean z) {
        super(z2Var, tVar, z);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.j(this, consumer);
    }

    @Override // j$.util.u
    /* renamed from: e */
    public void forEachRemaining(j$.util.function.f fVar) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(fVar));
            return;
        }
        Objects.requireNonNull(fVar);
        h();
        this.b.s0(new o4(fVar), this.d);
        this.i = true;
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }

    @Override // j$.util.stream.g4
    void j() {
        V3 v3 = new V3();
        this.h = v3;
        this.e = this.b.t0(new o4(v3));
        this.f = new b(this);
    }

    @Override // j$.util.u
    /* renamed from: k */
    public boolean tryAdvance(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        boolean a = a();
        if (a) {
            V3 v3 = (V3) this.h;
            long j = this.g;
            int w = v3.w(j);
            fVar.accept((v3.c == 0 && w == 0) ? ((double[]) v3.e)[(int) j] : ((double[][]) v3.f)[w][(int) (j - v3.d[w])]);
        }
        return a;
    }

    @Override // j$.util.stream.g4
    g4 l(j$.util.t tVar) {
        return new p4(this.b, tVar, this.a);
    }

    @Override // j$.util.stream.g4, j$.util.t
    public t.a trySplit() {
        return (t.a) super.trySplit();
    }
}
