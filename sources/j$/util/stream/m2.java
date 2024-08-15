package j$.util.stream;
/* loaded from: classes2.dex */
final class m2 extends Y1 {
    long b;
    long c;
    final /* synthetic */ n2 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public m2(n2 n2Var, f2 f2Var) {
        super(f2Var);
        this.d = n2Var;
        this.b = n2Var.l;
        long j = n2Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
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

    @Override // j$.util.stream.f2
    public final void f(long j) {
        this.a.f(u0.E0(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.Y1, j$.util.stream.f2
    public final boolean h() {
        return this.c == 0 || this.a.h();
    }
}
