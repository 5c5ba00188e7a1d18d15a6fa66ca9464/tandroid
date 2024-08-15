package j$.util.stream;
/* loaded from: classes2.dex */
final class X extends Z1 {
    public final /* synthetic */ int b;
    final /* synthetic */ c c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ X(c cVar, f2 f2Var, int i) {
        super(f2Var);
        this.b = i;
        this.c = cVar;
    }

    @Override // j$.util.stream.d2, j$.util.stream.f2
    public final void accept(int i) {
        int i2 = this.b;
        c cVar = this.c;
        f2 f2Var = this.a;
        switch (i2) {
            case 0:
                f2Var.accept(i);
                return;
            case 1:
                ((j$.util.function.K) ((w) cVar).m).accept(i);
                f2Var.accept(i);
                return;
            case 2:
                f2Var.accept(i);
                return;
            case 3:
                f2Var.accept(((j$.util.function.Y) ((j$.util.function.a0) ((w) cVar).m)).b(i));
                return;
            case 4:
                f2Var.accept((f2) ((j$.util.function.N) ((v) cVar).m).apply(i));
                return;
            case 5:
                f2Var.accept(((j$.util.function.X) ((x) cVar).m).applyAsLong(i));
                return;
            case 6:
                f2Var.accept(((j$.util.function.S) ((j$.util.function.U) ((u) cVar).m)).a(i));
                return;
            case 7:
                IntStream intStream = (IntStream) ((j$.util.function.N) ((w) cVar).m).apply(i);
                if (intStream != null) {
                    try {
                        intStream.sequential().V(new V(1, this));
                    } catch (Throwable th) {
                        try {
                            intStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (intStream != null) {
                    intStream.close();
                    return;
                }
                return;
            default:
                if (((j$.util.function.O) ((j$.util.function.Q) ((w) cVar).m)).e(i)) {
                    f2Var.accept(i);
                    return;
                }
                return;
        }
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        int i = this.b;
        f2 f2Var = this.a;
        switch (i) {
            case 7:
                f2Var.f(-1L);
                return;
            case 8:
                f2Var.f(-1L);
                return;
            default:
                f2Var.f(j);
                return;
        }
    }
}
