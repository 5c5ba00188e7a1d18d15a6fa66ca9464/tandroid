package j$.util.stream;
/* loaded from: classes2.dex */
class r3 extends g3 {
    long b;
    long c;
    final /* synthetic */ s3 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r3(s3 s3Var, m3 m3Var) {
        super(m3Var);
        this.d = s3Var;
        this.b = s3Var.l;
        long j = s3Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.k3, j$.util.stream.m3
    public void accept(int i) {
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
        this.a.accept(i);
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a.n(B3.c(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.g3, j$.util.stream.m3
    public boolean o() {
        return this.c == 0 || this.a.o();
    }
}
