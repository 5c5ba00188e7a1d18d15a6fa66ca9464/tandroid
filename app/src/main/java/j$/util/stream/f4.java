package j$.util.stream;

import java.util.Comparator;
/* loaded from: classes2.dex */
public abstract class f4 implements j$.util.u {
    final boolean a;
    final y2 b;
    private j$.util.function.y c;
    j$.util.u d;
    m3 e;
    j$.util.function.c f;
    long g;
    e h;
    boolean i;

    public f4(y2 y2Var, j$.util.function.y yVar, boolean z) {
        this.b = y2Var;
        this.c = yVar;
        this.d = null;
        this.a = z;
    }

    public f4(y2 y2Var, j$.util.u uVar, boolean z) {
        this.b = y2Var;
        this.c = null;
        this.d = uVar;
        this.a = z;
    }

    private boolean f() {
        boolean z;
        while (this.h.count() == 0) {
            if (!this.e.o()) {
                b bVar = (b) this.f;
                switch (bVar.a) {
                    case 4:
                        o4 o4Var = (o4) bVar.b;
                        z = o4Var.d.b(o4Var.e);
                        break;
                    case 5:
                        q4 q4Var = (q4) bVar.b;
                        z = q4Var.d.b(q4Var.e);
                        break;
                    case 6:
                        s4 s4Var = (s4) bVar.b;
                        z = s4Var.d.b(s4Var.e);
                        break;
                    default:
                        L4 l4 = (L4) bVar.b;
                        z = l4.d.b(l4.e);
                        break;
                }
                if (z) {
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

    public final boolean a() {
        e eVar = this.h;
        boolean z = false;
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
        if (j < eVar.count()) {
            z = true;
        }
        if (z) {
            return z;
        }
        this.g = 0L;
        this.h.clear();
        return f();
    }

    @Override // j$.util.u
    public final int characteristics() {
        h();
        int g = d4.g(this.b.s0()) & d4.f;
        return (g & 64) != 0 ? (g & (-16449)) | (this.d.characteristics() & 16448) : g;
    }

    @Override // j$.util.u
    public final long estimateSize() {
        h();
        return this.d.estimateSize();
    }

    @Override // j$.util.u
    public Comparator getComparator() {
        if (j$.util.a.f(this, 4)) {
            return null;
        }
        throw new IllegalStateException();
    }

    @Override // j$.util.u
    public final long getExactSizeIfKnown() {
        h();
        if (d4.SIZED.d(this.b.s0())) {
            return this.d.getExactSizeIfKnown();
        }
        return -1L;
    }

    public final void h() {
        if (this.d == null) {
            this.d = (j$.util.u) this.c.get();
            this.c = null;
        }
    }

    @Override // j$.util.u
    public /* synthetic */ boolean hasCharacteristics(int i) {
        return j$.util.a.f(this, i);
    }

    abstract void j();

    abstract f4 l(j$.util.u uVar);

    public final String toString() {
        return String.format("%s[%s]", getClass().getName(), this.d);
    }

    @Override // j$.util.u
    public j$.util.u trySplit() {
        if (!this.a || this.i) {
            return null;
        }
        h();
        j$.util.u trySplit = this.d.trySplit();
        if (trySplit != null) {
            return l(trySplit);
        }
        return null;
    }
}
