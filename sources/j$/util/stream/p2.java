package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class p2 extends d {
    private final c j;
    private final j$.util.function.N k;
    private final long l;
    private final long m;
    private long n;
    private volatile boolean o;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p2(c cVar, c cVar2, j$.util.Q q, j$.util.function.N n, long j, long j2) {
        super(cVar2, q);
        this.j = cVar;
        this.k = n;
        this.l = j;
        this.m = j2;
    }

    p2(p2 p2Var, j$.util.Q q) {
        super(p2Var, q);
        this.j = p2Var.j;
        this.k = p2Var.k;
        this.l = p2Var.l;
        this.m = p2Var.m;
    }

    private long j(long j) {
        if (this.o) {
            return this.n;
        }
        p2 p2Var = (p2) this.d;
        p2 p2Var2 = (p2) this.e;
        if (p2Var == null || p2Var2 == null) {
            return this.n;
        }
        long j2 = p2Var.j(j);
        return j2 >= j ? j2 : j2 + p2Var2.j(j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final Object a() {
        if (c() == null) {
            y0 T0 = this.j.T0(T2.SIZED.e(this.j.c) ? this.j.I0(this.b) : -1L, this.k);
            f2 l1 = this.j.l1(this.a.K0(), T0);
            u0 u0Var = this.a;
            u0Var.G0(this.b, u0Var.Y0(l1));
            return T0.build();
        }
        u0 u0Var2 = this.a;
        y0 T02 = u0Var2.T0(-1L, this.k);
        u0Var2.X0(this.b, T02);
        D0 build = T02.build();
        this.n = build.count();
        this.o = true;
        this.b = null;
        return build;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public final f d(j$.util.Q q) {
        return new p2(this, q);
    }

    @Override // j$.util.stream.d
    protected final void g() {
        this.i = true;
        if (this.o) {
            e(i());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.d
    /* renamed from: k */
    public final X0 i() {
        return u1.n(this.j.e1());
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x006c  */
    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void onCompletion(CountedCompleter countedCompleter) {
        p2 p2Var;
        D0 l;
        f fVar = this.d;
        boolean z = true;
        if (!(fVar == null)) {
            this.n = ((p2) fVar).n + ((p2) this.e).n;
            if (this.i) {
                this.n = 0L;
            } else if (this.n != 0) {
                l = ((p2) this.d).n == 0 ? (D0) ((p2) this.e).b() : u1.l(this.j.e1(), (D0) ((p2) this.d).b(), (D0) ((p2) this.e).b());
                D0 d0 = l;
                if (c() != null) {
                    d0 = d0.q(this.l, this.m >= 0 ? Math.min(d0.count(), this.l + this.m) : this.n, this.k);
                }
                e(d0);
                this.o = true;
            }
            l = i();
            D0 d02 = l;
            if (c() != null) {
            }
            e(d02);
            this.o = true;
        }
        if (this.m >= 0) {
            if (!(c() == null)) {
                long j = this.l + this.m;
                long j2 = this.o ? this.n : j(j);
                if (j2 < j) {
                    p2 p2Var2 = (p2) c();
                    p2 p2Var3 = this;
                    while (true) {
                        if (p2Var2 != null) {
                            if (p2Var3 == p2Var2.e && (p2Var = (p2) p2Var2.d) != null) {
                                j2 += p2Var.j(j);
                                if (j2 >= j) {
                                    break;
                                }
                            }
                            p2Var3 = p2Var2;
                            p2Var2 = (p2) p2Var2.c();
                        } else if (j2 < j) {
                            z = false;
                        }
                    }
                }
                if (z) {
                    h();
                }
            }
        }
        super.onCompletion(countedCompleter);
    }
}
