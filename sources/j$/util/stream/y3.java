package j$.util.stream;
/* loaded from: classes2.dex */
class y3 extends g3 {
    long b;
    long c;
    final /* synthetic */ z3 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public y3(z3 z3Var, n3 n3Var) {
        super(n3Var);
        this.d = z3Var;
        this.b = z3Var.l;
        long j = z3Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.k3, j$.util.stream.n3
    public void accept(double d) {
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

    @Override // j$.util.stream.n3
    public void n(long j) {
        this.a.n(C3.c(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.g3, j$.util.stream.n3
    public boolean o() {
        return this.c == 0 || this.a.o();
    }
}
