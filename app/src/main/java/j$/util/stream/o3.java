package j$.util.stream;
/* loaded from: classes2.dex */
class o3 extends i3 {
    long b;
    long c;
    final /* synthetic */ p3 d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o3(p3 p3Var, m3 m3Var) {
        super(m3Var);
        this.d = p3Var;
        this.b = p3Var.l;
        long j = p3Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.function.Consumer
    public void accept(Object obj) {
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
        this.a.accept((m3) obj);
    }

    @Override // j$.util.stream.m3
    public void n(long j) {
        this.a.n(B3.c(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.i3, j$.util.stream.m3
    public boolean o() {
        return this.c == 0 || this.a.o();
    }
}
