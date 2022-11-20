package j$.util.stream;

import j$.util.function.Consumer;
import java.util.Objects;
/* loaded from: classes2.dex */
final class o4 extends f4 implements j$.util.t {
    /* JADX INFO: Access modifiers changed from: package-private */
    public o4(y2 y2Var, j$.util.function.y yVar, boolean z) {
        super(y2Var, yVar, z);
    }

    o4(y2 y2Var, j$.util.u uVar, boolean z) {
        super(y2Var, uVar, z);
    }

    @Override // j$.util.u
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.j(this, consumer);
    }

    @Override // j$.util.w
    /* renamed from: e */
    public void forEachRemaining(j$.util.function.f fVar) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(fVar));
            return;
        }
        Objects.requireNonNull(fVar);
        h();
        this.b.u0(new n4(fVar), this.d);
        this.i = true;
    }

    @Override // j$.util.u
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.b(this, consumer);
    }

    @Override // j$.util.stream.f4
    void j() {
        U3 u3 = new U3();
        this.h = u3;
        this.e = this.b.v0(new n4(u3));
        this.f = new b(this);
    }

    @Override // j$.util.w
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
    f4 l(j$.util.u uVar) {
        return new o4(this.b, uVar, this.a);
    }

    @Override // j$.util.stream.f4, j$.util.u
    public j$.util.t trySplit() {
        return (j$.util.t) super.trySplit();
    }
}
