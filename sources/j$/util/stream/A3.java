package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class A3 extends d {
    private final c j;
    private final j$.util.function.m k;
    private final long l;
    private final long m;
    private long n;
    private volatile boolean o;

    A3(A3 a3, j$.util.u uVar) {
        super(a3, uVar);
        this.j = a3.j;
        this.k = a3.k;
        this.l = a3.l;
        this.m = a3.m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public A3(c cVar, y2 y2Var, j$.util.u uVar, j$.util.function.m mVar, long j, long j2) {
        super(y2Var, uVar);
        this.j = cVar;
        this.k = mVar;
        this.l = j;
        this.m = j2;
    }

    private long m(long j) {
        if (this.o) {
            return this.n;
        }
        A3 a3 = (A3) this.d;
        A3 a32 = (A3) this.e;
        if (a3 == null || a32 == null) {
            return this.n;
        }
        long m = a3.m(j);
        return m >= j ? m : m + a32.m(j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        if (e()) {
            s1 t0 = this.j.t0(d4.SIZED.e(this.j.c) ? this.j.q0(this.b) : -1L, this.k);
            m3 H0 = this.j.H0(this.a.s0(), t0);
            y2 y2Var = this.a;
            y2Var.o0(y2Var.v0(H0), this.b);
            return t0.a();
        }
        y2 y2Var2 = this.a;
        s1 t02 = y2Var2.t0(-1L, this.k);
        y2Var2.u0(t02, this.b);
        A1 a = t02.a();
        this.n = a.count();
        this.o = true;
        this.b = null;
        return a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.u uVar) {
        return new A3(this, uVar);
    }

    @Override // j$.util.stream.d
    protected void i() {
        this.i = true;
        if (this.o) {
            g(k());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.d
    /* renamed from: n */
    public final A1 k() {
        return x2.k(this.j.B0());
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0065  */
    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void onCompletion(CountedCompleter countedCompleter) {
        A3 a3;
        A1 i;
        boolean z = true;
        if (!d()) {
            this.n = ((A3) this.d).n + ((A3) this.e).n;
            if (this.i) {
                this.n = 0L;
            } else if (this.n != 0) {
                i = ((A3) this.d).n == 0 ? (A1) ((A3) this.e).b() : x2.i(this.j.B0(), (A1) ((A3) this.d).b(), (A1) ((A3) this.e).b());
                A1 a1 = i;
                if (e()) {
                    a1 = a1.r(this.l, this.m >= 0 ? Math.min(a1.count(), this.l + this.m) : this.n, this.k);
                }
                g(a1);
                this.o = true;
            }
            i = k();
            A1 a12 = i;
            if (e()) {
            }
            g(a12);
            this.o = true;
        }
        if (this.m >= 0 && !e()) {
            long j = this.l + this.m;
            long m = this.o ? this.n : m(j);
            if (m < j) {
                A3 a32 = (A3) c();
                A3 a33 = this;
                while (true) {
                    if (a32 != null) {
                        if (a33 == a32.e && (a3 = (A3) a32.d) != null) {
                            m += a3.m(j);
                            if (m >= j) {
                                break;
                            }
                        }
                        a33 = a32;
                        a32 = (A3) a32.c();
                    } else if (m < j) {
                        z = false;
                    }
                }
            }
            if (z) {
                j();
            }
        }
        this.b = null;
        this.e = null;
        this.d = null;
    }
}
