package j$.util.stream;
/* loaded from: classes2.dex */
class s3 extends h3 {
    long b;
    long c;
    final /* synthetic */ t3 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s3(t3 t3Var, n3 n3Var) {
        super(n3Var);
        this.d = t3Var;
        this.b = t3Var.l;
        long j = t3Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.l3, j$.util.stream.n3
    public void accept(int i) {
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

    @Override // j$.util.stream.n3
    public void n(long j) {
        this.a.n(C3.c(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.h3, j$.util.stream.n3
    public boolean o() {
        return this.c == 0 || this.a.o();
    }
}
