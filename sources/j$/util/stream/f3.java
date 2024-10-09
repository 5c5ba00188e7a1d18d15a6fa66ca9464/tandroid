package j$.util.stream;

import j$.util.function.Consumer;
import j$.util.function.Supplier;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class f3 extends U2 implements j$.util.H {
    f3(b bVar, j$.util.Q q, boolean z) {
        super(bVar, q, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f3(b bVar, Supplier supplier, boolean z) {
        super(bVar, supplier, z);
    }

    @Override // j$.util.Q
    public final /* synthetic */ void a(Consumer consumer) {
        j$.util.a.f(this, consumer);
    }

    @Override // j$.util.N
    /* renamed from: c */
    public final void e(j$.util.function.F f) {
        if (this.h != null || this.i) {
            do {
            } while (p(f));
            return;
        }
        f.getClass();
        h();
        e3 e3Var = new e3(f, 1);
        this.b.D0(this.d, e3Var);
        this.i = true;
    }

    @Override // j$.util.N
    /* renamed from: g */
    public final boolean p(j$.util.function.F f) {
        f.getClass();
        boolean b = b();
        if (b) {
            J2 j2 = (J2) this.h;
            long j = this.g;
            int w = j2.w(j);
            f.accept((j2.c == 0 && w == 0) ? ((int[]) j2.e)[(int) j] : ((int[][]) j2.f)[w][(int) (j - j2.d[w])]);
        }
        return b;
    }

    @Override // j$.util.stream.U2
    final void j() {
        J2 j2 = new J2();
        this.h = j2;
        this.e = this.b.E0(new e3(j2, 0));
        this.f = new a(this, 4);
    }

    @Override // j$.util.stream.U2
    final U2 k(j$.util.Q q) {
        return new f3(this.b, q, this.a);
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean s(Consumer consumer) {
        return j$.util.a.o(this, consumer);
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.H trySplit() {
        return (j$.util.H) super.trySplit();
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.N trySplit() {
        return (j$.util.H) super.trySplit();
    }

    @Override // j$.util.stream.U2, j$.util.Q
    public final j$.util.Q trySplit() {
        return (j$.util.H) super.trySplit();
    }
}
