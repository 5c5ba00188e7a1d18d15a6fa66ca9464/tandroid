package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;
import j$.util.s;
import java.util.Objects;
/* loaded from: classes2.dex */
final class s4 extends f4 implements s.c {
    /* JADX INFO: Access modifiers changed from: package-private */
    public s4(y2 y2Var, Supplier supplier, boolean z) {
        super(y2Var, supplier, z);
    }

    s4(y2 y2Var, j$.util.s sVar, boolean z) {
        super(y2Var, sVar, z);
    }

    @Override // j$.util.s
    public /* synthetic */ boolean b(Consumer consumer) {
        return j$.util.a.l(this, consumer);
    }

    @Override // j$.util.t
    /* renamed from: d */
    public void forEachRemaining(j$.util.function.q qVar) {
        if (this.h != null || this.i) {
            do {
            } while (tryAdvance(qVar));
            return;
        }
        Objects.requireNonNull(qVar);
        h();
        this.b.p0(new r4(qVar), this.d);
        this.i = true;
    }

    @Override // j$.util.s
    public /* synthetic */ void forEachRemaining(Consumer consumer) {
        j$.util.a.d(this, consumer);
    }

    @Override // j$.util.t
    /* renamed from: i */
    public boolean tryAdvance(j$.util.function.q qVar) {
        Objects.requireNonNull(qVar);
        boolean a = a();
        if (a) {
            Y3 y3 = (Y3) this.h;
            long j = this.g;
            int w = y3.w(j);
            qVar.accept((y3.c == 0 && w == 0) ? ((long[]) y3.e)[(int) j] : ((long[][]) y3.f)[w][(int) (j - y3.d[w])]);
        }
        return a;
    }

    @Override // j$.util.stream.f4
    void j() {
        Y3 y3 = new Y3();
        this.h = y3;
        this.e = this.b.q0(new r4(y3));
        this.f = new b(this);
    }

    @Override // j$.util.stream.f4
    f4 l(j$.util.s sVar) {
        return new s4(this.b, sVar, this.a);
    }

    @Override // j$.util.stream.f4, j$.util.s
    public s.c trySplit() {
        return (s.c) super.trySplit();
    }
}
