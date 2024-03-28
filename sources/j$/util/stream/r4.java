package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class r4 extends g4 implements t.b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public r4(z2 z2Var, Supplier supplier, boolean z) {
        super(z2Var, supplier, z);
    }

    r4(z2 z2Var, j$.util.t tVar, boolean z) {
        super(z2Var, tVar, z);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.k(this, consumer);
    }

    @Override // j$.util.u
    /* renamed from: c */
    public void forEachRemaining(j$.util.function.l lVar) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(lVar));
            return;
        }
        Objects.requireNonNull(lVar);
        h();
        this.b.s0(new q4(lVar), this.d);
        this.i = true;
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.c(this, consumer);
    }

    @Override // j$.util.u
    /* renamed from: g */
    public boolean tryAdvance(j$.util.function.l lVar) {
        Objects.requireNonNull(lVar);
        boolean a = a();
        if (a) {
            X3 x3 = (X3) this.h;
            long j = this.g;
            int w = x3.w(j);
            lVar.accept((x3.c == 0 && w == 0) ? ((int[]) x3.e)[(int) j] : ((int[][]) x3.f)[w][(int) (j - x3.d[w])]);
        }
        return a;
    }

    @Override // j$.util.stream.g4
    void j() {
        X3 x3 = new X3();
        this.h = x3;
        this.e = this.b.t0(new q4(x3));
        this.f = new b(this);
    }

    @Override // j$.util.stream.g4
    g4 l(j$.util.t tVar) {
        return new r4(this.b, tVar, this.a);
    }

    @Override // j$.util.stream.g4, j$.util.t
    public t.b trySplit() {
        return (t.b) super.trySplit();
    }
}
