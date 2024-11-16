package j$.util.stream;

/* loaded from: classes2.dex */
final class h2 extends Y1 {
    long b;
    long c;
    final /* synthetic */ i2 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    h2(i2 i2Var, e2 e2Var) {
        super(e2Var);
        this.d = i2Var;
        this.b = i2Var.m;
        long j = i2Var.n;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.e2
    public final void accept(int i) {
        long j = this.b;
        if (j != 0) {
            this.b = j - 1;
            return;
        }
        long j2 = this.c;
        if (j2 > 0) {
            this.c = j2 - 1;
            this.a.accept(i);
        }
    }

    @Override // j$.util.stream.Y1, j$.util.stream.e2
    public final void n(long j) {
        this.a.n(t0.z(j, this.d.m, this.c));
    }

    @Override // j$.util.stream.Y1, j$.util.stream.e2
    public final boolean q() {
        return this.c == 0 || this.a.q();
    }
}
