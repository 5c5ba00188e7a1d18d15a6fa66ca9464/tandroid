package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import j$.util.s;
import java.util.Objects;
/* loaded from: classes2.dex */
final class o4 extends f4 implements s.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public o4(y2 y2Var, Supplier supplier, boolean z) {
        super(y2Var, supplier, z);
    }

    o4(y2 y2Var, j$.util.s sVar, boolean z) {
        super(y2Var, sVar, z);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.j(this, consumer);
    }

    @Override // j$.util.t
    /* renamed from: e */
    public void forEachRemaining(j$.util.function.f fVar) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(fVar));
            return;
        }
        Objects.requireNonNull(fVar);
        h();
        this.b.p0(new n4(fVar), this.d);
        this.i = true;
    }

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }

    @Override // j$.util.stream.f4
    void j() {
        U3 u3 = new U3();
        this.h = u3;
        this.e = this.b.q0(new n4(u3));
        this.f = new b(this);
    }

    @Override // j$.util.t
    /* renamed from: k */
    public boolean tryAdvance(j$.util.function.f fVar) {
        Objects.requireNonNull(fVar);
        boolean a = a();
        if (a) {
            U3 u3 = (U3) this.h;
            long j = this.g;
            int w = u3.w(j);
            fVar.accept((u3.c == 0 && w == 0) ? ((double[]) u3.e)[(int) j] : ((double[][]) u3.f)[w][(int) (j - u3.d[w])]);
        }
        return a;
    }

    @Override // j$.util.stream.f4
    f4 l(j$.util.s sVar) {
        return new o4(this.b, sVar, this.a);
    }

    @Override // j$.util.stream.f4, j$.util.s
    public s.a trySplit() {
        return (s.a) super.trySplit();
    }
}
