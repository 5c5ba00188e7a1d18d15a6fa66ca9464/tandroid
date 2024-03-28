package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class B3 extends d {
    private final c j;
    private final j$.util.function.m k;
    private final long l;
    private final long m;
    private long n;
    private volatile boolean o;

    B3(B3 b3, j$.util.t tVar) {
        super(b3, tVar);
        this.j = b3.j;
        this.k = b3.k;
        this.l = b3.l;
        this.m = b3.m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public B3(c cVar, z2 z2Var, j$.util.t tVar, j$.util.function.m mVar, long j, long j2) {
        super(z2Var, tVar);
        this.j = cVar;
        this.k = mVar;
        this.l = j;
        this.m = j2;
    }

    private long m(long j) {
        if (this.o) {
            return this.n;
        }
        B3 b3 = (B3) this.d;
        B3 b32 = (B3) this.e;
        if (b3 == null || b32 == null) {
            return this.n;
        }
        long m = b3.m(j);
        return m >= j ? m : m + b32.m(j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public Object a() {
        if (e()) {
            t1 r0 = this.j.r0(e4.SIZED.e(this.j.c) ? this.j.o0(this.b) : -1L, this.k);
            n3 F0 = this.j.F0(this.a.q0(), r0);
            z2 z2Var = this.a;
            z2Var.m0(z2Var.t0(F0), this.b);
            return r0.a();
        }
        z2 z2Var2 = this.a;
        t1 r02 = z2Var2.r0(-1L, this.k);
        z2Var2.s0(r02, this.b);
        B1 a = r02.a();
        this.n = a.count();
        this.o = true;
        this.b = null;
        return a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.f
    public f f(j$.util.t tVar) {
        return new B3(this, tVar);
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
    public final B1 k() {
        return y2.k(this.j.z0());
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0065  */
    @Override // j$.util.stream.f, java.util.concurrent.CountedCompleter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void onCompletion(CountedCompleter countedCompleter) {
        B3 b3;
        B1 i;
        boolean z = true;
        if (!d()) {
            this.n = ((B3) this.d).n + ((B3) this.e).n;
            if (this.i) {
                this.n = 0L;
            } else if (this.n != 0) {
                i = ((B3) this.d).n == 0 ? (B1) ((B3) this.e).b() : y2.i(this.j.z0(), (B1) ((B3) this.d).b(), (B1) ((B3) this.e).b());
                B1 b1 = i;
                if (e()) {
                    b1 = b1.r(this.l, this.m >= 0 ? Math.min(b1.count(), this.l + this.m) : this.n, this.k);
                }
                g(b1);
                this.o = true;
            }
            i = k();
            B1 b12 = i;
            if (e()) {
            }
            g(b12);
            this.o = true;
        }
        if (this.m >= 0 && !e()) {
            long j = this.l + this.m;
            long m = this.o ? this.n : m(j);
            if (m < j) {
                B3 b32 = (B3) c();
                B3 b33 = this;
                while (true) {
                    if (b32 != null) {
                        if (b33 == b32.e && (b3 = (B3) b32.d) != null) {
                            m += b3.m(j);
                            if (m >= j) {
                                break;
                            }
                        }
                        b33 = b32;
                        b32 = (B3) b32.c();
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
