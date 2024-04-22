package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class f4 implements j$.util.s {
    final boolean a;
    final y2 b;
    private Supplier c;
    j$.util.s d;
    m3 e;
    j$.util.function.c f;
    long g;
    e h;
    boolean i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f4(y2 y2Var, Supplier supplier, boolean z) {
        this.b = y2Var;
        this.c = supplier;
        this.d = null;
        this.a = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f4(y2 y2Var, j$.util.s sVar, boolean z) {
        this.b = y2Var;
        this.c = null;
        this.d = sVar;
        this.a = z;
    }

    private boolean f() {
        boolean b;
        while (this.h.count() == 0) {
            if (!this.e.o()) {
                b bVar = (b) this.f;
                switch (bVar.a) {
                    case 4:
                        o4 o4Var = (o4) bVar.b;
                        b = o4Var.d.b(o4Var.e);
                        break;
                    case 5:
                        q4 q4Var = (q4) bVar.b;
                        b = q4Var.d.b(q4Var.e);
                        break;
                    case 6:
                        s4 s4Var = (s4) bVar.b;
                        b = s4Var.d.b(s4Var.e);
                        break;
                    default:
                        L4 l4 = (L4) bVar.b;
                        b = l4.d.b(l4.e);
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

    @Override // j$.util.s
    public final int characteristics() {
        h();
        int g = d4.g(this.b.n0()) & d4.f;
        return (g & 64) != 0 ? (g & (-16449)) | (this.d.characteristics() & 16448) : g;
    }

    @Override // j$.util.s
    public final long estimateSize() {
        h();
        return this.d.estimateSize();
    }

    @Override // j$.util.s
    public Comparator getComparator() {
        if (j$.util.a.f(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.s
    public final long getExactSizeIfKnown() {
        h();
        if (d4.SIZED.d(this.b.n0())) {
            return this.d.getExactSizeIfKnown();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void h() {
        if (this.d == null) {
            this.d = (j$.util.s) this.c.get();
            this.c = null;
        }
    }

    @Override // j$.util.s
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    abstract void j();

    abstract f4 l(j$.util.s sVar);

    public final String toString() {
        return String.format("%s[%s]", getClass().getName(), this.d);
    }

    @Override // j$.util.s
    public j$.util.s trySplit() {
        if (!this.a || this.i) {
            return null;
        }
        h();
        j$.util.s trySplit = this.d.trySplit();
        if (trySplit == null) {
            return null;
        }
        return l(trySplit);
    }
}
