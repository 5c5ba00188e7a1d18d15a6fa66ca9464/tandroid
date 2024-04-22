package j$.util.stream;

import j$.util.function.Supplier;
import java.util.Objects;
/* loaded from: classes2.dex */
abstract class c extends y2 implements g {
    private final c a;
    private final c b;
    protected final int c;
    private c d;
    private int e;
    private int f;
    private j$.util.s g;
    private boolean h;
    private boolean i;
    private Runnable j;
    private boolean k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(j$.util.s sVar, int i, boolean z) {
        this.b = null;
        this.g = sVar;
        this.a = this;
        int i2 = d4.g & i;
        this.c = i2;
        this.f = ((i2 << 1) ^ (-1)) & d4.l;
        this.e = 0;
        this.k = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(c cVar, int i) {
        if (cVar.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        cVar.h = true;
        cVar.d = this;
        this.b = cVar;
        this.c = d4.h & i;
        this.f = d4.a(i, cVar.f);
        c cVar2 = cVar.a;
        this.a = cVar2;
        if (B0()) {
            cVar2.i = true;
        }
        this.e = cVar.e + 1;
    }

    private j$.util.s D0(int i) {
        int i2;
        int i3;
        c cVar = this.a;
        j$.util.s sVar = cVar.g;
        if (sVar != null) {
            cVar.g = null;
            if (cVar.k && cVar.i) {
                c cVar2 = cVar.d;
                int i4 = 1;
                while (cVar != this) {
                    int i5 = cVar2.c;
                    if (cVar2.B0()) {
                        i4 = 0;
                        if (d4.SHORT_CIRCUIT.d(i5)) {
                            i5 &= d4.u ^ (-1);
                        }
                        sVar = cVar2.A0(cVar, sVar);
                        if (sVar.hasCharacteristics(64)) {
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
            return sVar;
        }
        throw new IllegalStateException("source already consumed or closed");
    }

    j$.util.s A0(y2 y2Var, j$.util.s sVar) {
        return z0(y2Var, sVar, new j$.util.function.m() { // from class: j$.util.stream.a
            @Override // j$.util.function.m
            public final Object apply(int i) {
                return new Object[i];
            }
        }).spliterator();
    }

    abstract boolean B0();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract m3 C0(int i, m3 m3Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j$.util.s E0() {
        c cVar = this.a;
        if (this == cVar) {
            if (this.h) {
                throw new IllegalStateException("stream has already been operated upon or closed");
            }
            this.h = true;
            j$.util.s sVar = cVar.g;
            if (sVar != null) {
                cVar.g = null;
                return sVar;
            }
            throw new IllegalStateException("source already consumed or closed");
        }
        throw new IllegalStateException();
    }

    abstract j$.util.s F0(y2 y2Var, Supplier supplier, boolean z);

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

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final void i0(m3 m3Var, j$.util.s sVar) {
        Objects.requireNonNull(m3Var);
        if (d4.SHORT_CIRCUIT.d(this.f)) {
            j0(m3Var, sVar);
            return;
        }
        m3Var.n(sVar.getExactSizeIfKnown());
        sVar.forEachRemaining(m3Var);
        m3Var.m();
    }

    @Override // j$.util.stream.g
    public final boolean isParallel() {
        return this.a.k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final void j0(m3 m3Var, j$.util.s sVar) {
        c cVar = this;
        while (cVar.e > 0) {
            cVar = cVar.b;
        }
        m3Var.n(sVar.getExactSizeIfKnown());
        cVar.v0(sVar, m3Var);
        m3Var.m();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final A1 k0(j$.util.s sVar, boolean z, j$.util.function.m mVar) {
        if (this.a.k) {
            return u0(this, sVar, z, mVar);
        }
        s1 o0 = o0(l0(sVar), mVar);
        Objects.requireNonNull(o0);
        i0(q0(o0), sVar);
        return o0.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final long l0(j$.util.s sVar) {
        if (d4.SIZED.d(this.f)) {
            return sVar.getExactSizeIfKnown();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final e4 m0() {
        c cVar = this;
        while (cVar.e > 0) {
            cVar = cVar.b;
        }
        return cVar.w0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final int n0() {
        return this.f;
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
    public final m3 p0(m3 m3Var, j$.util.s sVar) {
        Objects.requireNonNull(m3Var);
        i0(q0(m3Var), sVar);
        return m3Var;
    }

    public final g parallel() {
        this.a.k = true;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final m3 q0(m3 m3Var) {
        Objects.requireNonNull(m3Var);
        for (c cVar = this; cVar.e > 0; cVar = cVar.b) {
            m3Var = cVar.C0(cVar.b.f, m3Var);
        }
        return m3Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.y2
    public final j$.util.s r0(j$.util.s sVar) {
        return this.e == 0 ? sVar : F0(this, new b(sVar), this.a.k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object s0(N4 n4) {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        return this.a.k ? n4.c(this, D0(n4.b())) : n4.d(this, D0(n4.b()));
    }

    public final g sequential() {
        this.a.k = false;
        return this;
    }

    public j$.util.s spliterator() {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        c cVar = this.a;
        if (this == cVar) {
            j$.util.s sVar = cVar.g;
            if (sVar != null) {
                cVar.g = null;
                return sVar;
            }
            throw new IllegalStateException("source already consumed or closed");
        }
        return F0(this, new b(this), cVar.k);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final A1 t0(j$.util.function.m mVar) {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        if (this.a.k && this.b != null && B0()) {
            this.e = 0;
            c cVar = this.b;
            return z0(cVar, cVar.D0(0), mVar);
        }
        return k0(D0(0), true, mVar);
    }

    abstract A1 u0(y2 y2Var, j$.util.s sVar, boolean z, j$.util.function.m mVar);

    abstract void v0(j$.util.s sVar, m3 m3Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract e4 w0();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean x0() {
        return d4.ORDERED.d(this.f);
    }

    public /* synthetic */ j$.util.s y0() {
        return D0(0);
    }

    A1 z0(y2 y2Var, j$.util.s sVar, j$.util.function.m mVar) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }
}
