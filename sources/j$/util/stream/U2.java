package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Comparator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class U2 implements j$.util.Q {
    final boolean a;
    final b b;
    private Supplier c;
    j$.util.Q d;
    e2 e;
    a f;
    long g;
    d h;
    boolean i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public U2(b bVar, j$.util.Q q, boolean z) {
        this.b = bVar;
        this.c = null;
        this.d = q;
        this.a = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public U2(b bVar, Supplier supplier, boolean z) {
        this.b = bVar;
        this.c = supplier;
        this.d = null;
        this.a = z;
    }

    private boolean f() {
        while (this.h.count() == 0) {
            if (this.e.q() || !this.f.f()) {
                if (this.i) {
                    return false;
                }
                this.e.m();
                this.i = true;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean b() {
        d dVar = this.h;
        if (dVar == null) {
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
        boolean z = j < dVar.count();
        if (z) {
            return z;
        }
        this.g = 0L;
        this.h.clear();
        return f();
    }

    @Override // j$.util.Q
    public final int characteristics() {
        h();
        int g = S2.g(this.b.s0()) & S2.f;
        return (g & 64) != 0 ? (g & (-16449)) | (this.d.characteristics() & 16448) : g;
    }

    @Override // j$.util.Q
    public final long estimateSize() {
        h();
        return this.d.estimateSize();
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
        if (S2.SIZED.d(this.b.s0())) {
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

    abstract void j();

    abstract U2 k(j$.util.Q q);

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
