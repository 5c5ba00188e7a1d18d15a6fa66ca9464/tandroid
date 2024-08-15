package j$.util.stream;
/* loaded from: classes2.dex */
final class t extends Y1 {
    public final /* synthetic */ int b;
    final /* synthetic */ c c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ t(c cVar, f2 f2Var, int i) {
        super(f2Var);
        this.b = i;
        this.c = cVar;
    }

    @Override // j$.util.stream.c2, j$.util.function.m
    public final void accept(double d) {
        int i = this.b;
        f2 f2Var = this.a;
        c cVar = this.c;
        switch (i) {
            case 0:
                f2Var.accept(((j$.util.function.z) ((j$.util.function.B) ((u) cVar).m)).b(d));
                return;
            case 1:
                f2Var.accept((f2) ((j$.util.function.p) ((v) cVar).m).apply(d));
                return;
            case 2:
                f2Var.accept(((j$.util.function.t) ((j$.util.function.v) ((w) cVar).m)).a(d));
                return;
            case 3:
                f2Var.accept(((j$.util.function.y) ((x) cVar).m).applyAsLong(d));
                return;
            case 4:
                F f = (F) ((j$.util.function.p) ((u) cVar).m).apply(d);
                if (f != null) {
                    try {
                        f.sequential().H(new s(1, this));
                    } catch (Throwable th) {
                        try {
                            f.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (f != null) {
                    f.close();
                    return;
                }
                return;
            case 5:
                if (((j$.util.function.q) ((j$.util.function.s) ((u) cVar).m)).e(d)) {
                    f2Var.accept(d);
                    return;
                }
                return;
            default:
                ((j$.util.function.m) ((u) cVar).m).accept(d);
                f2Var.accept(d);
                return;
        }
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        int i = this.b;
        f2 f2Var = this.a;
        switch (i) {
            case 4:
                f2Var.f(-1L);
                return;
            case 5:
                f2Var.f(-1L);
                return;
            default:
                f2Var.f(j);
                return;
        }
    }
}
