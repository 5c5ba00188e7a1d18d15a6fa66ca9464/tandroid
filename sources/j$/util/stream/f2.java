package j$.util.stream;

/* loaded from: classes2.dex */
final class f2 extends a2 {
    long b;
    long c;
    final /* synthetic */ g2 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    f2(g2 g2Var, e2 e2Var) {
        super(e2Var);
        this.d = g2Var;
        this.b = g2Var.m;
        long j = g2Var.n;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.function.Consumer
    /* renamed from: accept */
    public final void r(Object obj) {
        long j = this.b;
        if (j != 0) {
            this.b = j - 1;
            return;
        }
        long j2 = this.c;
        if (j2 > 0) {
            this.c = j2 - 1;
            this.a.r((e2) obj);
        }
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public final void n(long j) {
        this.a.n(t0.z(j, this.d.m, this.c));
    }

    @Override // j$.util.stream.a2, j$.util.stream.e2
    public final boolean q() {
        return this.c == 0 || this.a.q();
    }
}
