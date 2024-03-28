package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class t4 extends g4 implements t.c {
    /* JADX INFO: Access modifiers changed from: package-private */
    public t4(z2 z2Var, Supplier supplier, boolean z) {
        super(z2Var, supplier, z);
    }

    t4(z2 z2Var, j$.util.t tVar, boolean z) {
        super(z2Var, tVar, z);
    }

    @Override // j$.util.t
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.l(this, consumer);
    }

    @Override // j$.util.u
    /* renamed from: d */
    public void forEachRemaining(j$.util.function.q qVar) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(qVar));
            return;
        }
        Objects.requireNonNull(qVar);
        h();
        this.b.s0(new s4(qVar), this.d);
        this.i = true;
    }

    @Override // j$.util.t
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.d(this, consumer);
    }

    @Override // j$.util.u
    /* renamed from: i */
    public boolean tryAdvance(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        boolean a = a();
        if (a) {
            Z3 z3 = (Z3) this.h;
            long j = this.g;
            int w = z3.w(j);
            qVar.accept((z3.c == 0 && w == 0) ? ((long[]) z3.e)[(int) j] : ((long[][]) z3.f)[w][(int) (j - z3.d[w])]);
        }
        return a;
    }

    @Override // j$.util.stream.g4
    void j() {
        Z3 z3 = new Z3();
        this.h = z3;
        this.e = this.b.t0(new s4(z3));
        this.f = new b(this);
    }

    @Override // j$.util.stream.g4
    g4 l(j$.util.t tVar) {
        return new t4(this.b, tVar, this.a);
    }

    @Override // j$.util.stream.g4, j$.util.t
    public t.c trySplit() {
        return (t.c) super.trySplit();
    }
}
