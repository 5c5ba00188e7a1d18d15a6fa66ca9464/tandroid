package j$.util.stream;
/* loaded from: classes2.dex */
final class i2 extends Z1 {
    long b;
    long c;
    final /* synthetic */ j2 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i2(j2 j2Var, f2 f2Var) {
        super(f2Var);
        this.d = j2Var;
        this.b = j2Var.l;
        long j = j2Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.d2, j$.util.stream.f2
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

    @Override // j$.util.stream.f2
    public final void f(long j) {
        this.a.f(u0.E0(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.Z1, j$.util.stream.f2
    public final boolean h() {
        return this.c == 0 || this.a.h();
    }
}
