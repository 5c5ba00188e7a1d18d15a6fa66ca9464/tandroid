package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Objects;
/* loaded from: classes2.dex */
abstract class c extends z2 implements g {
    private final c a;
    private final c b;
    protected final int c;
    private c d;
    private int e;
    private int f;
    private j$.util.t g;
    private boolean h;
    private boolean i;
    private Runnable j;
    private boolean k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(c cVar, int i) {
        if (cVar.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        cVar.h = true;
        cVar.d = this;
        this.b = cVar;
        this.c = e4.h & i;
        this.f = e4.a(i, cVar.f);
        c cVar2 = cVar.a;
        this.a = cVar2;
        if (E0()) {
            cVar2.i = true;
        }
        this.e = cVar.e + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(j$.util.t tVar, int i, boolean z) {
        this.b = null;
        this.g = tVar;
        this.a = this;
        int i2 = e4.g & i;
        this.c = i2;
        this.f = ((i2 << 1) ^ (-1)) & e4.l;
        this.e = 0;
        this.k = z;
    }

    private j$.util.t G0(int i) {
        int i2;
        int i3;
        c cVar = this.a;
        j$.util.t tVar = cVar.g;
        if (tVar != null) {
            cVar.g = null;
            if (cVar.k && cVar.i) {
                c cVar2 = cVar.d;
                int i4 = 1;
                while (cVar != this) {
                    int i5 = cVar2.c;
                    if (cVar2.E0()) {
                        i4 = 0;
                        if (e4.SHORT_CIRCUIT.d(i5)) {
                            i5 &= e4.u ^ (-1);
                        }
                        tVar = cVar2.D0(cVar, tVar);
                        if (tVar.hasCharacteristics(64)) {
                            i2 = i5 & (e4.t ^ (-1));
                            i3 = e4.s;
                        } else {
                            i2 = i5 & (e4.s ^ (-1));
                            i3 = e4.t;
                        }
                        i5 = i2 | i3;
                    }
                    cVar2.e = i4;
                    cVar2.f = e4.a(i5, cVar.f);
                    i4++;
                    c cVar3 = cVar2;
                    cVar2 = cVar2.d;
                    cVar = cVar3;
                }
            }
            if (i != 0) {
                this.f = e4.a(i, this.f);
            }
            return tVar;
        }
        throw new IllegalStateException("source already consumed or closed");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean A0() {
        return e4.ORDERED.d(this.f);
    }

    public /* synthetic */ j$.util.t B0() {
        return G0(0);
    }

    B1 C0(z2 z2Var, j$.util.t tVar, j$.util.function.m mVar) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }

    j$.util.t D0(z2 z2Var, j$.util.t tVar) {
        return C0(z2Var, tVar, new j$.util.function.m() { // from class: j$.util.stream.a
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Object[i];
            }
        }).spliterator();
    }

    abstract boolean E0();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract n3 F0(int i, n3 n3Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j$.util.t H0() {
        c cVar = this.a;
        if (this == cVar) {
            if (this.h) {
                throw new IllegalStateException("stream has already been operated upon or closed");
            }
            this.h = true;
            j$.util.t tVar = cVar.g;
            if (tVar != null) {
                cVar.g = null;
                return tVar;
            }
            throw new IllegalStateException("source already consumed or closed");
        }
        throw new IllegalStateException();
    }

    abstract j$.util.t I0(z2 z2Var, Supplier supplier, boolean z);

    @Override // j$.util.stream.g, java.lang.AutoCloseable
    public void close() {
        this.h = true;
        this.g = null;
        c cVar = this.a;
        Runnable runnable = cVar.j;
        if (runnable != null) {
            cVar.j = null;
            runnable.run();
        }
    }

    @Override // j$.util.stream.g
    public final boolean isParallel() {
        return this.a.k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final void l0(n3 n3Var, j$.util.t tVar) {
        Objects.requireNonNull(n3Var);
        if (e4.SHORT_CIRCUIT.d(this.f)) {
            m0(n3Var, tVar);
            return;
        }
        n3Var.n(tVar.getExactSizeIfKnown());
        tVar.forEachRemaining(n3Var);
        n3Var.m();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final void m0(n3 n3Var, j$.util.t tVar) {
        c cVar = this;
        while (cVar.e > 0) {
            cVar = cVar.b;
        }
        n3Var.n(tVar.getExactSizeIfKnown());
        cVar.y0(tVar, n3Var);
        n3Var.m();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final B1 n0(j$.util.t tVar, boolean z, j$.util.function.m mVar) {
        if (this.a.k) {
            return x0(this, tVar, z, mVar);
        }
        t1 r0 = r0(o0(tVar), mVar);
        Objects.requireNonNull(r0);
        l0(t0(r0), tVar);
        return r0.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final long o0(j$.util.t tVar) {
        if (e4.SIZED.d(this.f)) {
            return tVar.getExactSizeIfKnown();
        }
        return -1L;
    }

    @Override // j$.util.stream.g
    public g onClose(Runnable runnable) {
        c cVar = this.a;
        Runnable runnable2 = cVar.j;
        if (runnable2 != null) {
            runnable = new N4(runnable2, runnable);
        }
        cVar.j = runnable;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final f4 p0() {
        c cVar = this;
        while (cVar.e > 0) {
            cVar = cVar.b;
        }
        return cVar.z0();
    }

    public final g parallel() {
        this.a.k = true;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final int q0() {
        return this.f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final n3 s0(n3 n3Var, j$.util.t tVar) {
        Objects.requireNonNull(n3Var);
        l0(t0(n3Var), tVar);
        return n3Var;
    }

    public final g sequential() {
        this.a.k = false;
        return this;
    }

    public j$.util.t spliterator() {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        c cVar = this.a;
        if (this == cVar) {
            j$.util.t tVar = cVar.g;
            if (tVar != null) {
                cVar.g = null;
                return tVar;
            }
            throw new IllegalStateException("source already consumed or closed");
        }
        return I0(this, new b(this), cVar.k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final n3 t0(n3 n3Var) {
        Objects.requireNonNull(n3Var);
        for (c cVar = this; cVar.e > 0; cVar = cVar.b) {
            n3Var = cVar.F0(cVar.b.f, n3Var);
        }
        return n3Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.z2
    public final j$.util.t u0(j$.util.t tVar) {
        return this.e == 0 ? tVar : I0(this, new b(tVar), this.a.k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object v0(O4 o4) {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        return this.a.k ? o4.c(this, G0(o4.b())) : o4.d(this, G0(o4.b()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final B1 w0(j$.util.function.m mVar) {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        if (this.a.k && this.b != null && E0()) {
            this.e = 0;
            c cVar = this.b;
            return C0(cVar, cVar.G0(0), mVar);
        }
        return n0(G0(0), true, mVar);
    }

    abstract B1 x0(z2 z2Var, j$.util.t tVar, boolean z, j$.util.function.m mVar);

    abstract void y0(j$.util.t tVar, n3 n3Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract f4 z0();
}
