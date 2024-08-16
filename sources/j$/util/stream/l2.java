package j$.util.stream;
/* loaded from: classes2.dex */
final class l2 extends X1 {
    long b;
    long c;
    final /* synthetic */ m2 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public l2(m2 m2Var, e2 e2Var) {
        super(e2Var);
        this.d = m2Var;
        this.b = m2Var.m;
        long j = m2Var.n;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.e2, j$.util.function.n
    public final void accept(double d) {
        long j = this.b;
        if (j != 0) {
            this.b = j - 1;
            return;
        }
        long j2 = this.c;
        if (j2 > 0) {
            this.c = j2 - 1;
            this.a.accept(d);
        }
    }

    @Override // j$.util.stream.X1, j$.util.stream.e2
    public final void n(long j) {
        this.a.n(t0.z(j, this.d.m, this.c));
    }

    @Override // j$.util.stream.X1, j$.util.stream.e2
    public final boolean q() {
        return this.c == 0 || this.a.q();
    }
}
