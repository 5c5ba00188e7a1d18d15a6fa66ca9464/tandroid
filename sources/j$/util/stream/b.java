package j$.util.stream;

import j$.util.function.Supplier;

/* loaded from: classes2.dex */
abstract class b implements BaseStream {
    private final b a;
    private final b b;
    protected final int c;
    private b d;
    private int e;
    private int f;
    private j$.util.Q g;
    private boolean h;
    private boolean i;
    private Runnable j;
    private boolean k;

    b() {
    }

    b(j$.util.Q q, int i, boolean z) {
        this();
        this.b = null;
        this.g = q;
        this.a = this;
        int i2 = S2.g & i;
        this.c = i2;
        this.f = ((i2 << 1) ^ (-1)) & S2.l;
        this.e = 0;
        this.k = z;
    }

    b(b bVar, int i) {
        this();
        if (bVar.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        bVar.h = true;
        bVar.d = this;
        this.b = bVar;
        this.c = S2.h & i;
        this.f = S2.a(i, bVar.f);
        b bVar2 = bVar.a;
        this.a = bVar2;
        if (y0()) {
            bVar2.i = true;
        }
        this.e = bVar.e + 1;
    }

    private j$.util.Q A0(int i) {
        int i2;
        int i3;
        b bVar = this.a;
        j$.util.Q q = bVar.g;
        if (q == null) {
            throw new IllegalStateException("source already consumed or closed");
        }
        bVar.g = null;
        if (bVar.k && bVar.i) {
            b bVar2 = bVar.d;
            int i4 = 1;
            while (bVar != this) {
                int i5 = bVar2.c;
                if (bVar2.y0()) {
                    if (S2.SHORT_CIRCUIT.d(i5)) {
                        i5 &= S2.u ^ (-1);
                    }
                    q = bVar2.x0(bVar, q);
                    if (q.hasCharacteristics(64)) {
                        i2 = (S2.t ^ (-1)) & i5;
                        i3 = S2.s;
                    } else {
                        i2 = (S2.s ^ (-1)) & i5;
                        i3 = S2.t;
                    }
                    i5 = i2 | i3;
                    i4 = 0;
                }
                bVar2.e = i4;
                bVar2.f = S2.a(i5, bVar.f);
                i4++;
                b bVar3 = bVar2;
                bVar2 = bVar2.d;
                bVar = bVar3;
            }
        }
        if (i != 0) {
            this.f = S2.a(i, this.f);
        }
        return q;
    }

    final j$.util.Q B0() {
        b bVar = this.a;
        if (this != bVar) {
            throw new IllegalStateException();
        }
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        j$.util.Q q = bVar.g;
        if (q == null) {
            throw new IllegalStateException("source already consumed or closed");
        }
        bVar.g = null;
        return q;
    }

    abstract j$.util.Q C0(b bVar, Supplier supplier, boolean z);

    final e2 D0(j$.util.Q q, e2 e2Var) {
        e2Var.getClass();
        i0(q, E0(e2Var));
        return e2Var;
    }

    final e2 E0(e2 e2Var) {
        e2Var.getClass();
        b bVar = this;
        while (bVar.e > 0) {
            b bVar2 = bVar.b;
            e2Var = bVar.z0(bVar2.f, e2Var);
            bVar = bVar2;
        }
        return e2Var;
    }

    final j$.util.Q F0(j$.util.Q q) {
        return this.e == 0 ? q : C0(this, new a(q, 9), this.a.k);
    }

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final void close() {
        this.h = true;
        this.g = null;
        b bVar = this.a;
        Runnable runnable = bVar.j;
        if (runnable != null) {
            bVar.j = null;
            runnable.run();
        }
    }

    final void i0(j$.util.Q q, e2 e2Var) {
        e2Var.getClass();
        if (S2.SHORT_CIRCUIT.d(this.f)) {
            j0(q, e2Var);
            return;
        }
        e2Var.n(q.getExactSizeIfKnown());
        q.a(e2Var);
        e2Var.m();
    }

    @Override // j$.util.stream.BaseStream
    public final boolean isParallel() {
        return this.a.k;
    }

    final void j0(j$.util.Q q, e2 e2Var) {
        b bVar = this;
        while (bVar.e > 0) {
            bVar = bVar.b;
        }
        e2Var.n(q.getExactSizeIfKnown());
        bVar.p0(q, e2Var);
        e2Var.m();
    }

    final F0 k0(j$.util.Q q, boolean z, j$.util.function.I i) {
        if (this.a.k) {
            return n0(this, q, z, i);
        }
        x0 v0 = v0(o0(q), i);
        D0(q, v0);
        return v0.b();
    }

    final Object l0(x3 x3Var) {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        return this.a.k ? x3Var.c(this, A0(x3Var.d())) : x3Var.a(this, A0(x3Var.d()));
    }

    final F0 m0(j$.util.function.I i) {
        b bVar;
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        if (!this.a.k || (bVar = this.b) == null || !y0()) {
            return k0(A0(0), true, i);
        }
        this.e = 0;
        return w0(bVar.A0(0), i, bVar);
    }

    abstract F0 n0(b bVar, j$.util.Q q, boolean z, j$.util.function.I i);

    final long o0(j$.util.Q q) {
        if (S2.SIZED.d(this.f)) {
            return q.getExactSizeIfKnown();
        }
        return -1L;
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream onClose(Runnable runnable) {
        b bVar = this.a;
        Runnable runnable2 = bVar.j;
        if (runnable2 != null) {
            runnable = new w3(runnable2, runnable);
        }
        bVar.j = runnable;
        return this;
    }

    abstract void p0(j$.util.Q q, e2 e2Var);

    @Override // j$.util.stream.BaseStream
    public final BaseStream parallel() {
        this.a.k = true;
        return this;
    }

    abstract T2 q0();

    final T2 r0() {
        b bVar = this;
        while (bVar.e > 0) {
            bVar = bVar.b;
        }
        return bVar.q0();
    }

    final int s0() {
        return this.f;
    }

    @Override // j$.util.stream.BaseStream
    public final BaseStream sequential() {
        this.a.k = false;
        return this;
    }

    @Override // j$.util.stream.BaseStream, j$.util.stream.D
    public j$.util.Q spliterator() {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        b bVar = this.a;
        if (this != bVar) {
            return C0(this, new a(this, 0), bVar.k);
        }
        j$.util.Q q = bVar.g;
        if (q == null) {
            throw new IllegalStateException("source already consumed or closed");
        }
        bVar.g = null;
        return q;
    }

    final boolean t0() {
        return S2.ORDERED.d(this.f);
    }

    final /* synthetic */ j$.util.Q u0() {
        return A0(0);
    }

    abstract x0 v0(long j, j$.util.function.I i);

    F0 w0(j$.util.Q q, j$.util.function.I i, b bVar) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }

    j$.util.Q x0(b bVar, j$.util.Q q) {
        return w0(q, new l(12), bVar).spliterator();
    }

    abstract boolean y0();

    abstract e2 z0(int i, e2 e2Var);
}
