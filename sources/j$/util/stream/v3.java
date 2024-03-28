package j$.util.stream;
/* loaded from: classes2.dex */
class v3 extends i3 {
    long b;
    long c;
    final /* synthetic */ w3 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public v3(w3 w3Var, n3 n3Var) {
        super(n3Var);
        this.d = w3Var;
        this.b = w3Var.l;
        long j = w3Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.m3, j$.util.function.q
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

    @Override // j$.util.stream.n3
    public void n(long j) {
        this.a.n(C3.c(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.i3, j$.util.stream.n3
    public boolean o() {
        return this.c == 0 || this.a.o();
    }
}
