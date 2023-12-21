package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.t;
import java.util.Objects;
/* loaded from: classes2.dex */
final class q4 extends f4 implements t.b {
    /* JADX INFO: Access modifiers changed from: package-private */
    public q4(y2 y2Var, j$.util.function.y yVar, boolean z) {
        super(y2Var, yVar, z);
    }

    q4(y2 y2Var, j$.util.t tVar, boolean z) {
        super(y2Var, tVar, z);
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
        this.b.u0(new p4(lVar), this.d);
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
            W3 w3 = (W3) this.h;
            long j = this.g;
            int w = w3.w(j);
            lVar.accept((w3.c == 0 && w == 0) ? ((int[]) w3.e)[(int) j] : ((int[][]) w3.f)[w][(int) (j - w3.d[w])]);
        }
        return a;
    }

    @Override // j$.util.stream.f4
    void j() {
        W3 w3 = new W3();
        this.h = w3;
        this.e = this.b.v0(new p4(w3));
        this.f = new b(this);
    }

    @Override // j$.util.stream.f4
    f4 l(j$.util.t tVar) {
        return new q4(this.b, tVar, this.a);
    }

    @Override // j$.util.stream.f4, j$.util.t
    public t.b trySplit() {
        return (t.b) super.trySplit();
    }
}
