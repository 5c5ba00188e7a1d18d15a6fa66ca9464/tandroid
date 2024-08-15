package j$.util.stream;

import j$.util.function.LongFunction;
/* loaded from: classes2.dex */
final class f0 extends a2 {
    public final /* synthetic */ int b;
    final /* synthetic */ c c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ f0(c cVar, f2 f2Var, int i) {
        super(f2Var);
        this.b = i;
        this.c = cVar;
    }

    @Override // j$.util.stream.e2, j$.util.stream.f2
    public final void accept(long j) {
        int i = this.b;
        c cVar = this.c;
        f2 f2Var = this.a;
        switch (i) {
            case 0:
                f2Var.accept(j);
                return;
            case 1:
                f2Var.accept(((j$.util.function.w0) ((x) cVar).m).applyAsLong(j));
                return;
            case 2:
                f2Var.accept((f2) ((LongFunction) ((v) cVar).m).apply(j));
                return;
            case 3:
                f2Var.accept(((j$.util.function.q0) ((j$.util.function.s0) ((w) cVar).m)).a(j));
                return;
            case 4:
                f2Var.accept(((j$.util.function.n0) ((j$.util.function.p0) ((u) cVar).m)).a(j));
                return;
            case 5:
                LongStream longStream = (LongStream) ((LongFunction) ((x) cVar).m).apply(j);
                if (longStream != null) {
                    try {
                        longStream.sequential().E(new e0(1, this));
                    } catch (Throwable th) {
                        try {
                            longStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                if (longStream != null) {
                    longStream.close();
                    return;
                }
                return;
            case 6:
                if (((j$.util.function.k0) ((j$.util.function.m0) ((x) cVar).m)).e(j)) {
                    f2Var.accept(j);
                    return;
                }
                return;
            default:
                ((j$.util.function.h0) ((x) cVar).m).accept(j);
                f2Var.accept(j);
                return;
        }
    }

    @Override // j$.util.stream.f2
    public final void f(long j) {
        int i = this.b;
        f2 f2Var = this.a;
        switch (i) {
            case 5:
                f2Var.f(-1L);
                return;
            case 6:
                f2Var.f(-1L);
                return;
            default:
                f2Var.f(j);
                return;
        }
    }
}
