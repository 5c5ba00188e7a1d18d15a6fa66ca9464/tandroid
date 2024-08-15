package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class V2 implements j$.util.Q {
    final boolean a;
    final u0 b;
    private Supplier c;
    j$.util.Q d;
    f2 e;
    a f;
    long g;
    e h;
    boolean i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public V2(u0 u0Var, j$.util.Q q, boolean z) {
        this.b = u0Var;
        this.c = null;
        this.d = q;
        this.a = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public V2(u0 u0Var, a aVar, boolean z) {
        this.b = u0Var;
        this.c = aVar;
        this.d = null;
        this.a = z;
    }

    private boolean g() {
        boolean a;
        while (this.h.count() == 0) {
            if (!this.e.h()) {
                a aVar = this.f;
                int i = aVar.a;
                Object obj = aVar.b;
                switch (i) {
                    case 4:
                        e3 e3Var = (e3) obj;
                        a = e3Var.d.a(e3Var.e);
                        break;
                    case 5:
                        g3 g3Var = (g3) obj;
                        a = g3Var.d.a(g3Var.e);
                        break;
                    case 6:
                        i3 i3Var = (i3) obj;
                        a = i3Var.d.a(i3Var.e);
                        break;
                    default:
                        A3 a3 = (A3) obj;
                        a = a3.d.a(a3.e);
                        break;
                }
                if (a) {
                    continue;
                }
            }
            if (this.i) {
                return false;
            }
            this.e.end();
            this.i = true;
        }
        return true;
    }

    @Override // j$.util.Q
    public final int characteristics() {
        h();
        int g = T2.g(this.b.K0()) & T2.f;
        return (g & 64) != 0 ? (g & (-16449)) | (this.d.characteristics() & 16448) : g;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        h();
        return this.d.estimateSize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean f() {
        e eVar = this.h;
        if (eVar == null) {
            if (this.i) {
                return false;
            }
            h();
            i();
            this.g = 0L;
            this.e.f(this.d.getExactSizeIfKnown());
            return g();
        }
        long j = this.g + 1;
        this.g = j;
        boolean z = j < eVar.count();
        if (z) {
            return z;
        }
        this.g = 0L;
        this.h.clear();
        return g();
    }

    @Override // j$.util.Q
    public final Comparator getComparator() {
        if (j$.util.a.k(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.Q
    public final long getExactSizeIfKnown() {
        h();
        if (T2.SIZED.d(this.b.K0())) {
            return this.d.getExactSizeIfKnown();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void h() {
        if (this.d == null) {
            this.d = (j$.util.Q) this.c.get();
            this.c = null;
        }
    }

    @Override // j$.util.Q
    public final /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.k(this, i);
    }

    abstract void i();

    abstract V2 k(j$.util.Q q);

    public final String toString() {
        return String.format("%s[%s]", getClass().getName(), this.d);
    }

    @Override // j$.util.Q
    public j$.util.Q trySplit() {
        if (!this.a || this.i) {
            return null;
        }
        h();
        j$.util.Q trySplit = this.d.trySplit();
        if (trySplit == null) {
            return null;
        }
        return k(trySplit);
    }
}
