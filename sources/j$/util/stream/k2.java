package j$.util.stream;
/* loaded from: classes2.dex */
final class k2 extends a2 {
    long b;
    long c;
    final /* synthetic */ l2 d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public k2(l2 l2Var, f2 f2Var) {
        super(f2Var);
        this.d = l2Var;
        this.b = l2Var.l;
        long j = l2Var.m;
        this.c = j < 0 ? Long.MAX_VALUE : j;
    }

    @Override // j$.util.stream.e2, j$.util.stream.f2
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

    @Override // j$.util.stream.f2
    public final void f(long j) {
        this.a.f(u0.E0(j, this.d.l, this.c));
    }

    @Override // j$.util.stream.a2, j$.util.stream.f2
    public final boolean h() {
        return this.c == 0 || this.a.h();
    }
}
