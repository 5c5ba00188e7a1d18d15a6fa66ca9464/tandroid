package j$.util.stream;
/* loaded from: classes2.dex */
class x3 extends f3 {
    long b;
    long c;
    final /* synthetic */ y3 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x3(y3 y3Var, m3 m3Var) {
        super(m3Var);
        this.d = y3Var;
        this.b = y3Var.l;
        long j = y3Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.j3, j$.util.stream.m3
    public void accept(double d) {
        long j = this.b;
        if (j != 0) {
            this.b = j - 1;
            return;
        }
        long j2 = this.c;
        if (j2 <= 0) {
            return;
        }
        this.c = j2 - 1;
        this.a.accept(d);
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a.n(B3.c(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.f3, j$.util.stream.m3
    public boolean o() {
        return this.c == 0 || this.a.o();
    }
}
