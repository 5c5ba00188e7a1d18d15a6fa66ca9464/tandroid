package j$.util.stream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class c extends u0 implements BaseStream {
    private final c a;
    private final c b;
    protected final int c;
    private c d;
    private int e;
    private int f;
    private j$.util.Q g;
    private boolean h;
    private boolean i;
    private Runnable j;
    private boolean k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(j$.util.Q q, int i, boolean z) {
        this.b = null;
        this.g = q;
        this.a = this;
        int i2 = T2.g & i;
        this.c = i2;
        this.f = ((i2 << 1) ^ (-1)) & T2.l;
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
        this.c = T2.h & i;
        this.f = T2.a(i, cVar.f);
        c cVar2 = cVar.a;
        this.a = cVar2;
        if (k1()) {
            cVar2.i = true;
        }
        this.e = cVar.e + 1;
    }

    private j$.util.Q m1(int i) {
        int i2;
        int i3;
        c cVar = this.a;
        j$.util.Q q = cVar.g;
        if (q != null) {
            cVar.g = null;
            if (cVar.k && cVar.i) {
                c cVar2 = cVar.d;
                int i4 = 1;
                while (cVar != this) {
                    int i5 = cVar2.c;
                    if (cVar2.k1()) {
                        if (T2.SHORT_CIRCUIT.d(i5)) {
                            i5 &= T2.u ^ (-1);
                        }
                        q = cVar2.j1(cVar, q);
                        if (q.hasCharacteristics(64)) {
                            i2 = (T2.t ^ (-1)) & i5;
                            i3 = T2.s;
                        } else {
                            i2 = (T2.s ^ (-1)) & i5;
                            i3 = T2.t;
                        }
                        i5 = i3 | i2;
                        i4 = 0;
                    }
                    cVar2.e = i4;
                    cVar2.f = T2.a(i5, cVar.f);
                    i4++;
                    c cVar3 = cVar2;
                    cVar2 = cVar2.d;
                    cVar = cVar3;
                }
            }
            if (i != 0) {
                this.f = T2.a(i, this.f);
            }
            return q;
        }
        throw new IllegalStateException("source already consumed or closed");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final void F0(j$.util.Q q, f2 f2Var) {
        f2Var.getClass();
        if (T2.SHORT_CIRCUIT.d(this.f)) {
            G0(q, f2Var);
            return;
        }
        f2Var.f(q.getExactSizeIfKnown());
        q.forEachRemaining(f2Var);
        f2Var.end();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final void G0(j$.util.Q q, f2 f2Var) {
        c cVar = this;
        while (cVar.e > 0) {
            cVar = cVar.b;
        }
        f2Var.f(q.getExactSizeIfKnown());
        cVar.d1(q, f2Var);
        f2Var.end();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final long I0(j$.util.Q q) {
        if (T2.SIZED.d(this.f)) {
            return q.getExactSizeIfKnown();
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final int K0() {
        return this.f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final f2 X0(j$.util.Q q, f2 f2Var) {
        f2Var.getClass();
        F0(q, Y0(f2Var));
        return f2Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j$.util.stream.u0
    public final f2 Y0(f2 f2Var) {
        f2Var.getClass();
        c cVar = this;
        while (cVar.e > 0) {
            c cVar2 = cVar.b;
            f2Var = cVar.l1(cVar2.f, f2Var);
            cVar = cVar2;
        }
        return f2Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final D0 Z0(j$.util.Q q, boolean z, j$.util.function.N n) {
        if (this.a.k) {
            return c1(this, q, z, n);
        }
        y0 T0 = T0(I0(q), n);
        X0(q, T0);
        return T0.build();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object a1(C3 c3) {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        return this.a.k ? c3.a(this, m1(c3.b())) : c3.c(this, m1(c3.b()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final D0 b1(j$.util.function.N n) {
        c cVar;
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        if (this.a.k && (cVar = this.b) != null && k1()) {
            this.e = 0;
            return i1(cVar.m1(0), n, cVar);
        }
        return Z0(m1(0), true, n);
    }

    abstract D0 c1(u0 u0Var, j$.util.Q q, boolean z, j$.util.function.N n);

    @Override // j$.util.stream.BaseStream, java.lang.AutoCloseable
    public final void close() {
        this.h = true;
        this.g = null;
        c cVar = this.a;
        Runnable runnable = cVar.j;
        if (runnable != null) {
            cVar.j = null;
            runnable.run();
        }
    }

    abstract void d1(j$.util.Q q, f2 f2Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract U2 e1();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final U2 f1() {
        c cVar = this;
        while (cVar.e > 0) {
            cVar = cVar.b;
        }
        return cVar.e1();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean g1() {
        return T2.ORDERED.d(this.f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ j$.util.Q h1() {
        return m1(0);
    }

    D0 i1(j$.util.Q q, j$.util.function.N n, c cVar) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }

    @Override // j$.util.stream.BaseStream
    public final boolean isParallel() {
        return this.a.k;
    }

    j$.util.Q j1(c cVar, j$.util.Q q) {
        return i1(q, new b(0), cVar).spliterator();
    }

    abstract boolean k1();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract f2 l1(int i, f2 f2Var);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j$.util.Q n1() {
        c cVar = this.a;
        if (this == cVar) {
            if (this.h) {
                throw new IllegalStateException("stream has already been operated upon or closed");
            }
            this.h = true;
            j$.util.Q q = cVar.g;
            if (q != null) {
                cVar.g = null;
                return q;
            }
            throw new IllegalStateException("source already consumed or closed");
        }
        throw new IllegalStateException();
    }

    abstract j$.util.Q o1(u0 u0Var, a aVar, boolean z);

    @Override // j$.util.stream.BaseStream
    public final BaseStream onClose(Runnable runnable) {
        c cVar = this.a;
        Runnable runnable2 = cVar.j;
        if (runnable2 != null) {
            runnable = new B3(runnable2, runnable);
        }
        cVar.j = runnable;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final j$.util.Q p1(j$.util.Q q) {
        return this.e == 0 ? q : o1(this, new a(q, 0), this.a.k);
    }

    public final BaseStream parallel() {
        this.a.k = true;
        return this;
    }

    public final BaseStream sequential() {
        this.a.k = false;
        return this;
    }

    public j$.util.Q spliterator() {
        if (this.h) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.h = true;
        c cVar = this.a;
        if (this == cVar) {
            j$.util.Q q = cVar.g;
            if (q != null) {
                cVar.g = null;
                return q;
            }
            throw new IllegalStateException("source already consumed or closed");
        }
        return o1(this, new a(this, 1), cVar.k);
    }
}
