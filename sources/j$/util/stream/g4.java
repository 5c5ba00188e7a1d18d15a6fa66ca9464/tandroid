package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class g4 implements j$.util.t {
    final boolean a;
    final z2 b;
    private Supplier c;
    j$.util.t d;
    n3 e;
    j$.util.function.c f;
    long g;
    e h;
    boolean i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g4(z2 z2Var, Supplier supplier, boolean z) {
        this.b = z2Var;
        this.c = supplier;
        this.d = null;
        this.a = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g4(z2 z2Var, j$.util.t tVar, boolean z) {
        this.b = z2Var;
        this.c = null;
        this.d = tVar;
        this.a = z;
    }

    private boolean f() {
        boolean b;
        while (this.h.count() == 0) {
            if (!this.e.o()) {
                b bVar = (b) this.f;
                switch (bVar.a) {
                    case 4:
                        p4 p4Var = (p4) bVar.b;
                        b = p4Var.d.b(p4Var.e);
                        break;
                    case 5:
                        r4 r4Var = (r4) bVar.b;
                        b = r4Var.d.b(r4Var.e);
                        break;
                    case 6:
                        t4 t4Var = (t4) bVar.b;
                        b = t4Var.d.b(t4Var.e);
                        break;
                    default:
                        M4 m4 = (M4) bVar.b;
                        b = m4.d.b(m4.e);
                        break;
                }
                if (b) {
                    continue;
                }
            }
            if (this.i) {
                return false;
            }
            this.e.m();
            this.i = true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean a() {
        e eVar = this.h;
        if (eVar == null) {
            if (this.i) {
                return false;
            }
            h();
            j();
            this.g = 0L;
            this.e.n(this.d.getExactSizeIfKnown());
            return f();
        }
        long j = this.g + 1;
        this.g = j;
        boolean z = j < eVar.count();
        if (z) {
            return z;
        }
        this.g = 0L;
        this.h.clear();
        return f();
    }

    @Override // j$.util.t
    public final int characteristics() {
        h();
        int g = e4.g(this.b.q0()) & e4.f;
        return (g & 64) != 0 ? (g & (-16449)) | (this.d.characteristics() & 16448) : g;
    }

    @Override // j$.util.t
    public final long estimateSize() {
        h();
        return this.d.estimateSize();
    }

    @Override // j$.util.t
    public Comparator getComparator() {
        if (j$.util.a.f(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.t
    public final long getExactSizeIfKnown() {
        h();
        if (e4.SIZED.d(this.b.q0())) {
            return this.d.getExactSizeIfKnown();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void h() {
        if (this.d == null) {
            this.d = (j$.util.t) this.c.get();
            this.c = null;
        }
    }

    @Override // j$.util.t
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    abstract void j();

    abstract g4 l(j$.util.t tVar);

    public final String toString() {
        return String.format("%s[%s]", getClass().getName(), this.d);
    }

    @Override // j$.util.t
    public j$.util.t trySplit() {
        if (!this.a || this.i) {
            return null;
        }
        h();
        j$.util.t trySplit = this.d.trySplit();
        if (trySplit == null) {
            return null;
        }
        return l(trySplit);
    }
}
