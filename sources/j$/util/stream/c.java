package j$.util.stream;

import java.util.Objects;
/* loaded from: classes2.dex */
abstract class c extends y2 implements g {
    private final c a;
    private final c b;
    protected final int c;
    private c d;
    private int e;
    private int f;
    private j$.util.u g;
    private boolean h;
    private boolean i;
    private Runnable j;
    private boolean k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(c cVar, int i) {
        if (!cVar.h) {
            cVar.h = true;
            cVar.d = this;
            this.b = cVar;
            this.c = d4.h & i;
            this.f = d4.a(i, cVar.f);
            c cVar2 = cVar.a;
            this.a = cVar2;
            if (G0()) {
                cVar2.i = true;
            }
            this.e = cVar.e + 1;
            return;
        }
        throw new IllegalStateException("stream has already been operated upon or closed");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(j$.util.u uVar, int i, boolean z) {
        this.b = null;
        this.g = uVar;
        this.a = this;
        int i2 = d4.g & i;
        this.c = i2;
        this.f = ((i2 << 1) ^ (-1)) & d4.l;
        this.e = 0;
        this.k = z;
    }

    private j$.util.u I0(int i) {
        int i2;
        int i3;
        c cVar = this.a;
        j$.util.u uVar = cVar.g;
        if (uVar != null) {
            cVar.g = null;
            if (cVar.k && cVar.i) {
                c cVar2 = cVar.d;
                int i4 = 1;
                while (cVar != this) {
                    int i5 = cVar2.c;
                    if (cVar2.G0()) {
                        i4 = 0;
                        if (d4.SHORT_CIRCUIT.d(i5)) {
                            i5 &= d4.u ^ (-1);
                        }
                        uVar = cVar2.F0(cVar, uVar);
                        if (uVar.hasCharacteristics(64)) {
                            i2 = i5 & (d4.t ^ (-1));
                            i3 = d4.s;
                        } else {
                            i2 = i5 & (d4.s ^ (-1));
                            i3 = d4.t;
                        }
                        i5 = i2 | i3;
                    }
                    cVar2.e = i4;
                    cVar2.f = d4.a(i5, cVar.f);
                    i4++;
                    c cVar3 = cVar2;
                    cVar2 = cVar2.d;
                    cVar = cVar3;
                }
            }
            if (i != 0) {
                this.f = d4.a(i, this.f);
            }
            return uVar;
        }
        throw new IllegalStateException("source already consumed or closed");
    }

    abstract void A0(j$.util.u uVar, m3 m3Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract e4 B0();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean C0() {
        return d4.ORDERED.d(this.f);
    }

    public /* synthetic */ j$.util.u D0() {
        return I0(0);
    }

    A1 E0(y2 y2Var, j$.util.u uVar, j$.util.function.m mVar) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }

    j$.util.u F0(y2 y2Var, j$.util.u uVar) {
        return E0(y2Var, uVar, a.a).spliterator();
    }

    abstract boolean G0();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract m3 H0(int i, m3 m3Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j$.util.u J0() {
        c cVar = this.a;
        if (this == cVar) {
            if (this.h) {
                throw new IllegalStateException("stream has already been operated upon or closed");
            }
            this.h = true;
            j$.util.u uVar = cVar.g;
            if (uVar == null) {
                throw new IllegalStateException("source already consumed or closed");
            }
            cVar.g = null;
            return uVar;
        }
        throw new IllegalStateException();
    }

    abstract j$.util.u K0(y2 y2Var, j$.util.function.y yVar, boolean z);

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
    @Override // j$.util.stream.y2
    public final void n0(m3 m3Var, j$.util.u uVar) {
        Objects.requireNonNull(m3Var);
        if (d4.SHORT_CIRCUIT.d(this.f)) {
            o0(m3Var, uVar);
            return;
        }
        m3Var.n(uVar.getExactSizeIfKnown());
        uVar.forEachRemaining(m3Var);
        m3Var.m();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final void o0(m3 m3Var, j$.util.u uVar) {
        c cVar = this;
        while (cVar.e > 0) {
            cVar = cVar.b;
        }
        m3Var.n(uVar.getExactSizeIfKnown());
        cVar.A0(uVar, m3Var);
        m3Var.m();
    }

    @Override // j$.util.stream.g
    public g onClose(Runnable runnable) {
        c cVar = this.a;
        Runnable runnable2 = cVar.j;
        if (runnable2 != null) {
            runnable = new M4(runnable2, runnable);
        }
        cVar.j = runnable;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final A1 p0(j$.util.u uVar, boolean z, j$.util.function.m mVar) {
        if (this.a.k) {
            return z0(this, uVar, z, mVar);
        }
        s1 t0 = t0(q0(uVar), mVar);
        Objects.requireNonNull(t0);
        n0(v0(t0), uVar);
        return t0.a();
    }

    public final g parallel() {
        this.a.k = true;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final long q0(j$.util.u uVar) {
        if (d4.SIZED.d(this.f)) {
            return uVar.getExactSizeIfKnown();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final e4 r0() {
        c cVar = this;
        while (cVar.e > 0) {
            cVar = cVar.b;
        }
        return cVar.B0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final int s0() {
        return this.f;
    }

    public final g sequential() {
        this.a.k = false;
        return this;
    }

    public j$.util.u spliterator() {
        if (!this.h) {
            this.h = true;
            c cVar = this.a;
            if (this != cVar) {
                return K0(this, new b(this), cVar.k);
            }
            j$.util.u uVar = cVar.g;
            if (uVar == null) {
                throw new IllegalStateException("source already consumed or closed");
            }
            cVar.g = null;
            return uVar;
        }
        throw new IllegalStateException("stream has already been operated upon or closed");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final m3 u0(m3 m3Var, j$.util.u uVar) {
        Objects.requireNonNull(m3Var);
        n0(v0(m3Var), uVar);
        return m3Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final m3 v0(m3 m3Var) {
        Objects.requireNonNull(m3Var);
        for (c cVar = this; cVar.e > 0; cVar = cVar.b) {
            m3Var = cVar.H0(cVar.b.f, m3Var);
        }
        return m3Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final j$.util.u w0(j$.util.u uVar) {
        return this.e == 0 ? uVar : K0(this, new b(uVar), this.a.k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object x0(N4 n4) {
        if (!this.h) {
            this.h = true;
            return this.a.k ? n4.c(this, I0(n4.b())) : n4.d(this, I0(n4.b()));
        }
        throw new IllegalStateException("stream has already been operated upon or closed");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final A1 y0(j$.util.function.m mVar) {
        if (!this.h) {
            this.h = true;
            if (!this.a.k || this.b == null || !G0()) {
                return p0(I0(0), true, mVar);
            }
            this.e = 0;
            c cVar = this.b;
            return E0(cVar, cVar.I0(0), mVar);
        }
        throw new IllegalStateException("stream has already been operated upon or closed");
    }

    abstract A1 z0(y2 y2Var, j$.util.u uVar, boolean z, j$.util.function.m mVar);
}
