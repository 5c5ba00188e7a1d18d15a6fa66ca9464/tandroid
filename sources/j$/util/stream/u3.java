package j$.util.stream;
/* loaded from: classes2.dex */
class u3 extends h3 {
    long b;
    long c;
    final /* synthetic */ v3 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public u3(v3 v3Var, m3 m3Var) {
        super(m3Var);
        this.d = v3Var;
        this.b = v3Var.l;
        long j = v3Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.l3, j$.util.function.q
    public void accept(long j) {
        long j2 = this.b;
        if (j2 != 0) {
            this.b = j2 - 1;
            return;
        }
        long j3 = this.c;
        if (j3 > 0) {
            this.c = j3 - 1;
            this.a.accept(j);
        }
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a.n(B3.c(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.h3, j$.util.stream.m3
    public boolean o() {
        return this.c == 0 || this.a.o();
    }
}
