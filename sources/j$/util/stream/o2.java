package j$.util.stream;

import java.util.concurrent.CountedCompleter;
/* loaded from: classes2.dex */
final class o2 extends c {
    private final b j;
    private final j$.util.function.I k;
    private final long l;
    private final long m;
    private long n;
    private volatile boolean o;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o2(b bVar, b bVar2, j$.util.Q q, j$.util.function.I i, long j, long j2) {
        super(bVar2, q);
        this.j = bVar;
        this.k = i;
        this.l = j;
        this.m = j2;
    }

    o2(o2 o2Var, j$.util.Q q) {
        super(o2Var, q);
        this.j = o2Var.j;
        this.k = o2Var.k;
        this.l = o2Var.l;
        this.m = o2Var.m;
    }

    private long j(long j) {
        if (this.o) {
            return this.n;
        }
        o2 o2Var = (o2) this.d;
        o2 o2Var2 = (o2) this.e;
        if (o2Var == null || o2Var2 == null) {
            return this.n;
        }
        long j2 = o2Var.j(j);
        return j2 >= j ? j2 : j2 + o2Var2.j(j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.e
    public final Object a() {
        if (c()) {
            x0 v0 = this.j.v0(S2.SIZED.e(this.j.c) ? this.j.o0(this.b) : -1L, this.k);
            e2 z0 = this.j.z0(this.a.s0(), v0);
            b bVar = this.a;
            bVar.j0(this.b, bVar.E0(z0));
            return v0.b();
        }
        b bVar2 = this.a;
        x0 v02 = bVar2.v0(-1L, this.k);
        bVar2.D0(this.b, v02);
        F0 b = v02.b();
        this.n = b.count();
        this.o = true;
        this.b = null;
        return b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // j$.util.stream.e
    public final e d(j$.util.Q q) {
        return new o2(this, q);
    }

    @Override // j$.util.stream.c
    protected final void g() {
        this.i = true;
        if (this.o) {
            e(t0.L(this.j.q0()));
        }
    }

    @Override // j$.util.stream.c
    protected final Object i() {
        return t0.L(this.j.q0());
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x00df, code lost:
        if (r2 >= r0) goto L51;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006c  */
    @Override // j$.util.stream.e, java.util.concurrent.CountedCompleter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void onCompletion(CountedCompleter countedCompleter) {
        o2 o2Var;
        F0 I;
        e eVar = this.d;
        if (!(eVar == null)) {
            this.n = ((o2) eVar).n + ((o2) this.e).n;
            if (this.i) {
                this.n = 0L;
            } else if (this.n != 0) {
                I = ((o2) this.d).n == 0 ? (F0) ((o2) this.e).b() : t0.I(this.j.q0(), (F0) ((o2) this.d).b(), (F0) ((o2) this.e).b());
                F0 f0 = I;
                if (c()) {
                    f0 = f0.t(this.l, this.m >= 0 ? Math.min(f0.count(), this.l + this.m) : this.n, this.k);
                }
                e(f0);
                this.o = true;
            }
            I = t0.L(this.j.q0());
            F0 f02 = I;
            if (c()) {
            }
            e(f02);
            this.o = true;
        }
        if (this.m >= 0 && !c()) {
            long j = this.l + this.m;
            long j2 = this.o ? this.n : j(j);
            if (j2 < j) {
                o2 o2Var2 = (o2) ((e) getCompleter());
                o2 o2Var3 = this;
                while (true) {
                    if (o2Var2 != null) {
                        if (o2Var3 == o2Var2.e && (o2Var = (o2) o2Var2.d) != null) {
                            j2 += o2Var.j(j);
                            if (j2 >= j) {
                                break;
                            }
                        }
                        o2Var3 = o2Var2;
                        o2Var2 = (o2) ((e) o2Var2.getCompleter());
                    }
                }
            }
            h();
        }
        super.onCompletion(countedCompleter);
    }
}
