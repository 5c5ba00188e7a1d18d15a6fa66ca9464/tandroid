package j$.util.stream;
/* loaded from: classes2.dex */
final class j2 extends Z1 {
    long b;
    long c;
    final /* synthetic */ k2 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public j2(k2 k2Var, e2 e2Var) {
        super(e2Var);
        this.d = k2Var;
        this.b = k2Var.m;
        long j = k2Var.n;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.e2
    public final void accept(long j) {
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

    @Override // j$.util.stream.Z1, j$.util.stream.e2
    public final void n(long j) {
        this.a.n(t0.z(j, this.d.m, this.c));
    }

    @Override // j$.util.stream.Z1, j$.util.stream.e2
    public final boolean q() {
        return this.c == 0 || this.a.q();
    }
}
